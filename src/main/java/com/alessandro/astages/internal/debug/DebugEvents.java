package com.alessandro.astages.internal.debug;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.constant.AEventPhase;
import com.alessandro.astages.api.event.AddRestrictionEvent;
import com.alessandro.astages.api.event.AddStageEvent;
import com.alessandro.astages.api.store.APriorities;
import com.alessandro.astages.api.time.ATime;
import com.alessandro.astages.api.util.ARestrictionUtils;
import com.alessandro.astages.api.util.AStagesUtils;
import com.alessandro.astages.engine.AClientModelManager;
import com.alessandro.astages.engine.AModelManager;
import com.alessandro.astages.engine.store.AModel;
import com.alessandro.astages.infrastructure.config.AStagesCommon;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.List;
import java.util.function.Predicate;

@EventBusSubscriber(modid = AStages.MODID)
public class DebugEvents {
    @SubscribeEvent
    public static void addRestriction(AddRestrictionEvent event) {
        if (!AStagesCommon.ENABLE_TEST_MODE.get()) { return; }
        if (event.getEventPhase() != AEventPhase.RELOAD_STARTED) { return; }

        ARestrictionUtils.addRestrictionForStructure("id1", "stage1", List.of(BuiltinStructures.BASTION_REMNANT));

        // ============================================================
        // 1) PROPERTIES — split into coherent groups, because every deny
        //    message only fires while the related attribute is still at
        //    its "denied" default (e.g. PICKUP_MESSAGE only shows up if
        //    you do NOT call allowPickup()). Mixing allow*() calls with
        //    their matching messages in the same restriction means the
        //    messages would never be shown.
        // ============================================================

        // --- 1A) "Everything allowed": verifies that every allow*()/show*()
        //     actually moves the attribute from its denied default to the
        //     allowed state. No messages set here: if a message ever shows
        //     up in this restriction, some allow*()/show*() method is broken.
        ARestrictionUtils.addRestrictionForItem("astages:item/property_allow_everything", "test_property_allow", Items.GOLDEN_APPLE)
            .allowPickup()
            .allowEquip()
            .allowInventoryStorage()
            .allowContainerStorage()
            .allowAttack()
            .allowPlacement()
            .allowMining()
            .allowLeftClick()
            .allowRightClick()
            .showInRecipeViewer()
            .globalShowName()
            .pickupDelay(0);

        // --- 1B) "Blocked actions + messages": no allow*() calls (everything
        //     stays at its denied default), only the 6 action-deny messages +
        //     pickupDelay. Using a "block" item (COBBLESTONE) because
        //     BLOCK_BREAKING/BLOCK_PLACING are evaluated on the stack that
        //     represents the mined/placed BLOCK, not the tool in hand.
        ARestrictionUtils.addRestrictionForItem("astages:item/property_deny_messages", "test_property_deny", Items.COBBLESTONE)
            .pickupDelay(100)
            .dropMessage(stack -> Component.literal("[DROP] you can't drop " + stack.getHoverName().getString()))
            .attackMessage(stack -> Component.literal("[ATTACK] you can't attack with " + stack.getHoverName().getString()))
            .pickupMessage(stack -> Component.literal("[PICKUP] you can't pick up " + stack.getHoverName().getString()))
            .useMessage(stack -> Component.literal("[USE] you can't use " + stack.getHoverName().getString()))
            .breakMessage(stack -> Component.literal("[BREAK] you can't break " + stack.getHoverName().getString()))
            .placeMessage(stack -> Component.literal("[PLACE] you can't place " + stack.getHoverName().getString()));

        // --- 1C) "Hidden names": no show*Name() calls (everything stays at its
        //     "hidden" default), only the 5 placeholder messages. showInRecipeViewer()
        //     here demonstrates it's an INDEPENDENT toggle from showRecipeViewerName():
        //     the item stays visible in JEI/REI but shows the placeholder name
        //     instead of the real one.
        ARestrictionUtils.addRestrictionForItem("astages:item/property_hidden_names", "test_property_hidden_names", Items.NETHER_STAR)
            .allowInventoryStorage()
            .showInRecipeViewer()
            .actionBarMessage(stack -> Component.literal("Custom Action Bar Message"))
            .tooltipMessage(stack -> Component.literal("Custom Tooltip Message"))
            .recipeViewerMessage(stack -> Component.literal("Custom Recipe Viewer Message"))
            .jadeItemMessage(stack -> Component.literal("Custom Jade Item Message"))
            .jadeBlockMessage(stack -> Component.literal("Custom Jade Block Message"));

        // --- 1D) "Placed-block interaction": BLOCK_INTERACTIONS has the opposite
        //     polarity from the others (default true = allowed), so it needs its
        //     own test via disableBlockInteraction(). It only kicks in when the
        //     held item has no restriction of its own (fallback onto the clicked
        //     block), so use a different block from the ones above.
        ARestrictionUtils.addRestrictionForItem("astages:item/property_block_interaction", "test_property_block_interaction", Items.CHEST)
            .disableBlockInteraction()
            .useMessage(stack -> Component.literal("[BLOCK_INTERACTION] you can't open " + stack.getHoverName().getString()));

        // --- 1E) "Preset" variant: verifies setArmorAttributes() (used by
        //     addRestrictionForArmor), which sets a coherent block of attributes
        //     in one call (EQUIPPING=false, everything else=true).
        ARestrictionUtils.addRestrictionForArmor("astages:item/armor_showcase", "test_armor_showcase",
            List.of(Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE));


        // ============================================================
        // 2) CASCADING PROGRESSION — mod -> tag -> item -> predicate,
        //    using ignoreItems/ignoreTags to explicitly delegate to the
        //    more specific level, with separate stages so each can be
        //    unlocked one at a time during testing.
        // ============================================================

        // Level 1 (LOWEST): blocks the whole "minecraft" namespace, but ignores
        // the diamond sword and logs (delegated to the levels below), plus every
        // item used by another test group in this file, so this broad ban doesn't
        // leak into those tests too.
        ARestrictionUtils.addRestrictionForMod("astages:mod/minecraft_cascade", "test_cascade_mod", "minecraft")
            .ignoreItems(Items.DIAMOND_SWORD, Items.GOLDEN_APPLE, Items.COBBLESTONE, Items.NETHER_STAR, Items.CHEST,
                Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.IRON_INGOT, Items.NETHERITE_INGOT, Items.OAK_LOG);

        // Level 2 (LOW): blocks all logs, but ignores oak_log
        // (the item level will decide that one).
        ARestrictionUtils.addRestrictionForTag("astages:tag/logs_cascade", "test_cascade_tag", ItemTags.LOGS)
            .ignoreItems(Items.OAK_LOG);

        // Level 3 (DEFAULT): specifically blocks the diamond sword and oak_log.
        ARestrictionUtils.addRestrictionForItem("astages:item/cascade_targets", "test_cascade_item",
            List.of(Items.DIAMOND_SWORD, Items.OAK_LOG));

        // Level 4 (HIGH): a predicate that blocks ONLY enchanted diamond swords,
        // with a stage independent from all the others.
        var enchantedSwordModel = AResourceLocation.fromNamespaceAndPath("predicate/enchanted_diamond_sword");
        AModelManager.MODELS.registerModel(enchantedSwordModel, new AModel<>((Predicate<ItemStack>) stack ->
            stack.is(Items.DIAMOND_SWORD) && stack.isEnchanted()));
        AClientModelManager.MODELS.registerModel(enchantedSwordModel, new AModel<>((Predicate<ItemStack>) stack ->
            stack.is(Items.DIAMOND_SWORD) && stack.isEnchanted()));
        ARestrictionUtils.addRestrictionForPredicate("astages:predicate/enchanted_diamond_sword", "test_cascade_predicate", enchantedSwordModel);

        // What this group verifies, unlocking the stages one at a time:
        //  - only "test_cascade_mod" unlocked      -> logs and sword are STILL blocked (by tag/item)
        //  - + "test_cascade_tag" unlocked         -> only oak_log and diamond_sword remain blocked (by item)
        //  - + "test_cascade_item" unlocked        -> a non-enchanted diamond sword is finally obtainable,
        //                                              but an enchanted one is still blocked by the predicate
        //  - + "test_cascade_predicate" unlocked   -> everything is free


        // ============================================================
        // 3) OVERLAP WITHOUT DELEGATION — two (or more) restrictions that
        //    genuinely cover the same item, without ignoreItems/ignoreTags,
        //    to see how the conflict gets resolved.
        // ============================================================

        // Same item (iron ingot) covered by both a tag and an item restriction,
        // independent stages: no delegation, so BOTH end up RESTRICTED.
        ARestrictionUtils.addRestrictionForTag("astages:tag/iron_ingot_overlap", "test_overlap_tag",
            TagKey.create(Registries.ITEM, AResourceLocation.parse("c:ingots/iron")));
        ARestrictionUtils.addRestrictionForItem("astages:item/iron_ingot_overlap", "test_overlap_item", Items.IRON_INGOT);

        // Overlap between restrictions of the SAME type (two mod restrictions on
        // the same modId): useful to check the tie-break when priorities are
        // identical (LOWEST == LOWEST). Both ignore every item used by another
        // test group in this file, leaving any OTHER vanilla item (e.g. a plain
        // Items.STONE) as the one to try in-game to observe the tie-break itself.
        ARestrictionUtils.addRestrictionForMod("astages:mod/minecraft_tie_a", "test_same_type_overlap_a", "minecraft")
            .ignoreItems(Items.GOLDEN_APPLE, Items.COBBLESTONE, Items.NETHER_STAR, Items.CHEST, Items.DIAMOND_HELMET,
                Items.DIAMOND_CHESTPLATE, Items.DIAMOND_SWORD, Items.IRON_INGOT, Items.NETHERITE_INGOT)
            .ignoreTags(ItemTags.LOGS);
        ARestrictionUtils.addRestrictionForMod("astages:mod/minecraft_tie_b", "test_same_type_overlap_b", "minecraft")
            .ignoreItems(Items.GOLDEN_APPLE, Items.COBBLESTONE, Items.NETHER_STAR, Items.CHEST, Items.DIAMOND_HELMET,
                Items.DIAMOND_CHESTPLATE, Items.DIAMOND_SWORD, Items.IRON_INGOT, Items.NETHERITE_INGOT)
            .ignoreTags(ItemTags.LOGS);

        // What this verifies:
        //  - unlocking only test_overlap_tag, the iron ingot must stay blocked by test_overlap_item (and vice versa)
        //  - with neither stage unlocked, which message/attribute "wins" between tag (LOW) and item (DEFAULT)?
        //  - the two "twin" mod restrictions (same priority) on a neutral item (e.g. Items.STONE): which one
        //    wins? Is it deterministic, or does it depend on the internal HashSet iteration order? (likely
        //    another symptom of the priority issue)


        // ============================================================
        // 4) EXPLICIT PRIORITY — same item, same stage, different
        //    priorities set by hand, to isolate ONLY the priority-picking
        //    logic (no stage variable in the mix).
        // ============================================================
        ARestrictionUtils.addRestrictionForItem("astages:item/priority_override_default", "test_priority_override", Items.NETHERITE_INGOT);

        ARestrictionUtils.addRestrictionForItem("astages:item/priority_override_highest", "test_priority_override", Items.NETHERITE_INGOT)
            .setPriority(APriorities.HIGHEST);

        // Expected (semantically): the HIGHEST restriction should be the
        // "authoritative" one (e.g. its message/attributes should be the ones
        // shown to the player). With the priority bug in AEvaluatorUtils
        // (getPrioritizedRestrictionWithCondition picks the LOWEST priority,
        // not the highest), the DEFAULT one wins instead.


        // ============================================================
        // 5) RECIPES — same reasoning as above (use both restriction
        //    types, test priority, avoid unintended conflicts). Recipes
        //    have no "properties" to toggle (ABaseRecipeRestriction adds
        //    no default attributes) and no tag/predicate level, so there
        //    are only 2 types and 2 meaningful levels: mod -> specific.
        //
        //    Also note: ARecipeRestriction defaults to APriorities.DEFAULT (0)
        //    and ARecipeModRestriction to APriorities.LOW (-5) (both now go
        //    through ABaseRecipeRestriction's APriority constructor overload),
        //    mirroring the item/mod distinction used above.
        //
        //    Naming trap: ARecipeModRestriction.ignoreItems(String...)
        //    does NOT take item ids, it takes RECIPE ids to exclude from
        //    the mod-wide ban.
        //
        //    Recipe ids below follow typical vanilla naming and match the
        //    ones already used elsewhere in this class, but double-check
        //    them against your installed version if a test doesn't trigger.
        // ============================================================

        // --- 5A) CASCADE (mod -> specific), with delegation. A mod-wide ban
        //     on the whole "minecraft" recipe namespace, delegating two
        //     specific recipes (one CRAFTING, one SMITHING) to their own
        //     restrictions below, and ignoring the recipes used by 5B/5C so
        //     this broad ban doesn't leak into those tests.
        ARestrictionUtils.addRestrictionForModRecipe("astages:recipemod/minecraft_cascade", "test_recipe_cascade_mod", "minecraft")
            .ignoreItems("minecraft:diamond_sword", "minecraft:netherite_sword_smithing",
                "minecraft:golden_apple", "minecraft:iron_pickaxe");

        // A restriction can only hold recipes of ONE RecipeType at a time
        // (restrict() throws if you mix types), so crafting and smithing
        // need two separate restrictions even within the same cascade level.
        ARestrictionUtils.addRestrictionForRecipe("astages:recipe/cascade_crafting", "test_recipe_cascade_specific", RecipeType.CRAFTING,
            List.of(AResourceLocation.parse("minecraft:diamond_sword")));
        ARestrictionUtils.addRestrictionForRecipe("astages:recipe/cascade_smithing", "test_recipe_cascade_specific", RecipeType.SMITHING,
            List.of(AResourceLocation.parse("minecraft:netherite_sword_smithing")));

        // What this verifies:
        //  - only "test_recipe_cascade_mod" unlocked   -> both recipes still blocked (by the specific restrictions)
        //  - + "test_recipe_cascade_specific" unlocked -> both recipes become craftable

        // --- 5B) OVERLAP WITHOUT DELEGATION, exposing the STRUCTURAL priority
        //     issue described above: ARecipeEvaluator.evaluateCache always
        //     checks mod-wide restrictions FIRST and returns immediately if one
        //     matches (AEvaluatorUtils.firstNonNull), before ever looking at the
        //     specific-recipe cache. So a mod restriction wins over a
        //     specific-recipe restriction on the same recipe REGARDLESS of
        //     priority, unlike what setPriority() would suggest.
        ARestrictionUtils.addRestrictionForRecipe("astages:recipe/priority_specific", "test_recipe_priority_specific", RecipeType.CRAFTING,
                List.of(AResourceLocation.parse("minecraft:golden_apple")))
            .setPriority(APriorities.HIGHEST);

        ARestrictionUtils.addRestrictionForModRecipe("astages:recipemod/priority_wide", "test_recipe_priority_wide", "minecraft")
            .ignoreItems("minecraft:diamond_sword", "minecraft:netherite_sword_smithing", "minecraft:iron_pickaxe");
            // deliberately does NOT ignore "minecraft:golden_apple": that's the point of this test.

        // Expected (semantically): with HIGHEST priority, the specific restriction
        // should "win". Actually: as long as "test_recipe_priority_wide" isn't
        // unlocked, the mod restriction wins unconditionally, no matter what
        // priority the specific restriction has.

        // --- 5C) PRIORITY TIE-BREAK within the SAME branch: two specific-recipe
        //     restrictions on the exact same recipe id/type, same stage, only the
        //     priority differs. Isolates the min-priority-wins bug already found
        //     for items (same shared AEvaluatorUtils.getPrioritizedRestrictionWithCondition).
        ARestrictionUtils.addRestrictionForRecipe("astages:recipe/priority_tiebreak_default", "test_recipe_priority_tiebreak", RecipeType.CRAFTING,
            List.of(AResourceLocation.parse("minecraft:iron_pickaxe")));

        ARestrictionUtils.addRestrictionForRecipe("astages:recipe/priority_tiebreak_highest", "test_recipe_priority_tiebreak", RecipeType.CRAFTING,
                List.of(AResourceLocation.parse("minecraft:iron_pickaxe")))
            .setPriority(APriorities.HIGHEST);

        // Expected: HIGHEST should win; with the min-priority bug, DEFAULT (0) wins instead.
    }

    @SubscribeEvent
    public static void addStage(AddStageEvent event) {
        if (!AStagesCommon.ENABLE_TEST_MODE.get()) { return; }
        if (event.getEventPhase() != AEventPhase.RELOAD_STARTED) { return; }

        AStagesUtils.customizeStage("stage_permanent");

        AStagesUtils.customizeTemporaryStage("stage_temporary", new ATime("1m"))
            .whenGranted(e -> e.getPlayer())
            .everyTick(e -> {
                var server = e.getServer();

                if (server != null) {
                    server.sendSystemMessage(Component.literal("Tick!"));
                }
            })
            .whenExpired(e -> e.getPlayer());

        AStagesUtils.customizeStage("stage_server_only")
            .serverOnly();

        AStagesUtils.customizeStage("stage_player_only")
            .playerOnly();
    }

    @SubscribeEvent
    public static void checkMobSpawnType(FinalizeSpawnEvent event) {
        if (!AStagesCommon.SHOW_SPAWN_TYPES.get()) { return; }

        var whitelistSpawnTypes = AStagesCommon.getWhitelistSpawnTypes();
        var whitelistEntityTypes = AStagesCommon.getWhitelistEntityTypes();
        if ((whitelistSpawnTypes.isEmpty() || whitelistSpawnTypes.contains(event.getSpawnType())) &&
            (whitelistEntityTypes.isEmpty() || whitelistEntityTypes.contains(event.getEntity().getType()))) {
            AStages.LOGGER.info("[AStages-SpawnTypes] {} for spawn type {}", event.getEntity().getType(), event.getSpawnType().name());
        }
    }

    @SubscribeEvent
    public static void serverStarted(ServerStartedEvent event) {
//        NeoForgeRegistries.ATTACHMENT_TYPES.entrySet()
//            .forEach(entry -> {
//                AStages.LOGGER.debug(entry.getKey().toString());
//            });
    }

    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Pre event) {
        if (!AStagesCommon.ENABLE_TEST_MODE.get()) { return; }

//        AStages.LOGGER.debug(AClientStageManager.PERMANENT_INSTANCE.getStages().toString());
//        AStages.LOGGER.debug(AClientStageManager.TEMPORARY_INSTANCE.getStages().toString());
//        AStages.LOGGER.debug(AStageManager.PERMANENT_INSTANCE.getStages().toString());
//        AStages.LOGGER.debug(AStageManager.TEMPORARY_INSTANCE.getStages().toString());
    }
}
