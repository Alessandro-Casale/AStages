package com.alessandro.astages.internal.debug;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.constant.AEventPhase;
import com.alessandro.astages.api.event.AddRestrictionEvent;
import com.alessandro.astages.api.event.AddStageEvent;
import com.alessandro.astages.api.time.ATime;
import com.alessandro.astages.api.util.ARestrictionUtils;
import com.alessandro.astages.api.util.AStagesUtils;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.config.AStagesCommon;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.List;

@EventBusSubscriber(modid = AStages.MODID)
public class DebugEvents {
    @SubscribeEvent
    public static void addRestriction(AddRestrictionEvent event) {
        if (!AStagesCommon.ENABLE_TEST_MODE.get()) { return; }
        if (event.getEventPhase() != AEventPhase.BEFORE_JS) { return; }

        ARestrictionUtils.addRestrictionForItem("astages:item1", "stage_item_1", Items.ACACIA_BOAT);
        ARestrictionUtils.addRestrictionForItem("astages:item2", "stage_item_2", Items.ACACIA_PLANKS)
            .allowInventoryStorage()
            .allowPickup()
            .allowContainerStorage();
        ARestrictionUtils.addRestrictionForTag("astages:item3", "stage_item_3", AResourceLocation.fromTag("#forge:ingots/iron"));

        ARestrictionUtils.addRestrictionForMob("astages:mob1", "stage_mob_1", EntityType.BEE)
            .restrictDimensionSpawn(AResourceLocation.parse("minecraft:overworld"))
            .restrictSpawnType(MobSpawnType.SPAWN_EGG)
            .restrictBiomeSpawn(AResourceLocation.parse("minecraft:plains"));

        ARestrictionUtils.addRestrictionForRecipe("astages:recipe1", "stage_recipe_1", RecipeType.SMITHING, List.of(AResourceLocation.fromNamespaceAndPath("minecraft", "netherite_sword_smithing")));

        ARestrictionUtils.addRestrictionForStructure("astages:structure1", "stage_structure_1", List.of(AResourceLocation.parse("minecraft:village_plains")))
            .set(Attributes.ENTERING, false);

        // TODO: Priority and restriction application is completely messed up, I think!
//        ARestrictionUtils.addRestrictionForLoot("astages:loot1", "stage_loot_1")
//            .applyEverywhere()
//            .restrictItems(Items.BONE)
//            .replacer(stack -> {
//                if (stack.is(Items.BONE)) {
//                    return Items.EMERALD.getDefaultInstance();
//                }
//
//                return stack;
//            });
//
//        ARestrictionUtils.addRestrictionForLoot("astages:loot2", "stage_loot_2")
//            .lootTableFilter(AFilter.PARTIAL)
//            .restrictForLootTables(AResourceLocation.parse("minecraft:chests/simple_dungeon"))
//            .restrictItems(Items.ROTTEN_FLESH);
    }

    @SubscribeEvent
    public static void addStage(AddStageEvent event) {
        if (!AStagesCommon.ENABLE_TEST_MODE.get()) { return; }
        if (event.getEventPhase() != AEventPhase.BEFORE_JS) { return; }

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