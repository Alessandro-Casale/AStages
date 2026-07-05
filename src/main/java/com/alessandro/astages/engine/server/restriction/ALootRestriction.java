package com.alessandro.astages.engine.server.restriction;

import com.alessandro.astages.api.constant.AFilter;
import com.alessandro.astages.api.exception.UnsupportedMethodException;
import com.alessandro.astages.api.loot.ALootPayload;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.restriction.ARestriction;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;

// /give Dev chest{BlockEntityTag:{LootTable:"chests/village/village_toolsmith"}}
// /give Dev chest{BlockEntityTag:{LootTable:"minecraft:chests/simple_dungeon"}}
// /setblock ~ ~1 ~ minecraft:chest{LootTable:"minecraft:chests/simple_dungeon"}

@NotNullParamsAndMethodsReturn
public class ALootRestriction extends ARestriction<ALootRestriction, Void, ItemStack> {
    public static String IDENTIFIER = "/loot";

    private final Set<Item> restrictedItems = new HashSet<>();
    private final Set<ResourceLocation> restrictedTags = new HashSet<>();
    private final Set<String> restrictedMods = new HashSet<>();
    private final Set<Item> ignoredItems = new HashSet<>();
    private final Set<ResourceLocation> ignoredTags = new HashSet<>();

    private final Set<Block> restrictedBlocks = new HashSet<>();
    private final Set<BlockState> restrictedBlockStates = new HashSet<>();
    private final Set<BlockState> ignoredBlockStates = new HashSet<>();

    private AFilter entityFilter = AFilter.PARTIAL;
    private final List<EntityType<?>> entities = new ArrayList<>();
    private AFilter damageTypeFilter = AFilter.PARTIAL;
    private final List<DamageType> damageTypes = new ArrayList<>();
    private AFilter lootTableFilter = AFilter.PARTIAL;
    private final List<ResourceLocation> lootTables = new ArrayList<>();

    private Function<ItemStack, ItemStack> replacer;

    public ALootRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AttributeStore allowedAttributes() {
        var defaultAttributes = AttributeStore.builder()
            .addAttribute(Attributes.HAS_REPLACER)
            .addAttribute(Attributes.APPLY_EVERYWHERE);

        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withSelf(defaultAttributes)
            .withPlugin(ARestrictionManager.ATTACHED_ATTRIBUTES, ALootRestriction.class)
            .build();
    }

    @Override
    public ALootRestriction restrict(Void unused) {
        throw UnsupportedMethodException.useInstead("any of ALootRestriction.restrict/ignore/set/apply(###) methods");
    }

    public ALootRestriction restrictItems(Item... items) {
        restrictedItems.addAll(List.of(items));
        return this;
    }

    public ALootRestriction restrictTags(ResourceLocation... tags) {
        restrictedTags.addAll(List.of(tags));
        return this;
    }

    public ALootRestriction restrictMods(String... modIds) {
        restrictedMods.addAll(List.of(modIds));
        return this;
    }

    public ALootRestriction ignoredItems(Item... items) {
        ignoredItems.addAll(List.of(items));
        return this;
    }

    public ALootRestriction ignoredTags(ResourceLocation... tags) {
        ignoredTags.addAll(List.of(tags));
        return this;
    }

    public ALootRestriction restrictBlocks(Block... blocks) {
        restrictedBlocks.addAll(List.of(blocks));
        return this;
    }

    public ALootRestriction restrictBlockStates(BlockState... blockStates) {
        restrictedBlockStates.addAll(List.of(blockStates));
        return this;
    }

    public ALootRestriction ignoredBlockStates(BlockState... blockStates) {
        ignoredBlockStates.addAll(List.of(blockStates));
        return this;
    }

    public ALootRestriction restrictForEntities(EntityType<?>... entityTypes) {
        entities.addAll(List.of(entityTypes));
        return this;
    }

    public ALootRestriction restrictForDamageTypes(DamageType... damageTypes) {
        this.damageTypes.addAll(List.of(damageTypes));
        return this;
    }

    public ALootRestriction restrictForLootTables(ResourceLocation... lootTables) {
        this.lootTables.addAll(List.of(lootTables));
        return this;
    }

    @Override
    public boolean isRestricted(ItemStack stack) {
        if (stack.isEmpty()) { return false; }

        if (!ignoredItems.isEmpty() &&
            ignoredItems.stream().anyMatch(stack::is)) {
            return false;
        }

        if (!ignoredTags.isEmpty() &&
            ignoredTags.stream().anyMatch(ignoredTag -> stack.getTags().anyMatch(tag -> tag.location().equals(ignoredTag)))) {
            return false;
        }

        // Prefer mod check first!
        var registry = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (registry != null) {
            if (!restrictedMods.isEmpty() &&
                restrictedMods.stream().anyMatch(modId -> modId.equals(registry.getNamespace()))) {
                return true;
            }
        }

        if (!restrictedTags.isEmpty() &&
            stack.getTags().anyMatch(tag -> restrictedTags.contains(tag.location())) ) {
            return true;
        }

        if  (!restrictedItems.isEmpty() &&
            restrictedItems.stream().anyMatch(stack::is)) {
            return true;
        }

        return false;
    }

    public boolean isRestricted(ItemStack stack, ALootPayload payload) {
        if (get(Attributes.APPLY_EVERYWHERE)) {
            return isRestricted(stack);
        }

        Set<BiPredicate<ItemStack, ALootPayload>> activeChecks = new HashSet<>();

        if (!restrictedBlocks.isEmpty() || !restrictedBlockStates.isEmpty() || !ignoredBlockStates.isEmpty()) {
            activeChecks.add(this::checkBlock);
        }
        if (!entities.isEmpty()) {
            activeChecks.add(this::checkEntity);
        }

        if (!damageTypes.isEmpty()) {
            activeChecks.add(this::checkDamageType);
        }

        if (!lootTables.isEmpty()) {
            activeChecks.add(this::checkLootTable);
        }

        return activeChecks.stream().anyMatch(
            check -> check.test(stack, payload)
        );
    }

    public Function<ItemStack, ItemStack> getReplacer() {
        return replacer;
    }

    public ALootRestriction replacer(Function<ItemStack, ItemStack> replacer) {
        this.replacer = replacer;
        return set(Attributes.HAS_REPLACER, true);
    }

    public ALootRestriction entityFilter(AFilter filter) {
        entityFilter = filter;
        return this;
    }

    public ALootRestriction damageTypeFilter(AFilter filter) {
        damageTypeFilter = filter;
        return this;
    }

    public ALootRestriction lootTableFilter(AFilter filter) {
        lootTableFilter = filter;
        return this;
    }

    public ALootRestriction applyEverywhere() {
        return set(Attributes.APPLY_EVERYWHERE, true);
    }

    private boolean checkBlock(ItemStack stack, ALootPayload payload) {
        var blockState = payload.blockState();
        if (blockState == null) { return false; }

        if (ignoredBlockStates.contains(blockState)) { return false; }
        return restrictedBlockStates.contains(blockState) || restrictedBlocks.stream().anyMatch(blockState::is);
    }

    private boolean checkEntity(ItemStack stack, ALootPayload payload) {
        var entityType = payload.entityType();
        if (entityType == null) { return false; }

        if (entityFilter == AFilter.ALL) { return entities.contains(entityType); }
        return entities.contains(entityType) && isRestricted(stack);

    }

    private boolean checkDamageType(ItemStack stack, ALootPayload payload) {
        var damageType = payload.damageType();
        if (damageType == null) { return false; }

        if (damageTypeFilter == AFilter.ALL) { return damageTypes.contains(damageType); }
        return damageTypes.contains(damageType) && isRestricted(stack);
    }

    private boolean checkLootTable(ItemStack stack, ALootPayload payload) {
        var lootTable = payload.lootTable();
        if (lootTable == null) { return false; }

        if (lootTableFilter == AFilter.ALL) { return lootTables.contains(lootTable); }
        return lootTables.contains(lootTable) && isRestricted(stack);
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ALootRestriction setReplacer(Function<ItemStack, ItemStack> replacer) {
        this.replacer = replacer;
        set(Attributes.HAS_REPLACER, true);
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ALootRestriction setEntityFilter(AFilter filter) {
        entityFilter = filter;
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ALootRestriction setLootTableFilter(AFilter filter) {
        lootTableFilter = filter;
        return this;
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public ALootRestriction applyForEveryLootTableAndDrop(boolean value) {
        set(Attributes.APPLY_EVERYWHERE, value);
        return this;
    }
}