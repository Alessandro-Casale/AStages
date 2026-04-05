package com.alessandro.astages.core.server.restriction;

import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.store.AttributeStore;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.store.server.ARestriction;
import com.alessandro.astages.util.AFilter;
import com.google.errorprone.annotations.DoNotCall;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

// /give Dev chest{BlockEntityTag:{LootTable:"chests/village/village_toolsmith"}}
// /setblock ~ ~1 ~ minecraft:chest{LootTable:"minecraft:chests/simple_dungeon"}

@NotNullParamsAndMethodsReturn
public class ALootRestriction extends ARestriction<ALootRestriction, Void, ItemStack> {
    public static String IDENTIFIER = "/loot";

    private final List<Item> restrictedItems = new ArrayList<>();
    private final List<ResourceLocation> restrictedTags = new ArrayList<>();
    private final List<String> restrictedMods = new ArrayList<>();
    private final List<Item> ignoredItems = new ArrayList<>();
    private final List<ResourceLocation> ignoredTags = new ArrayList<>();

    private AFilter entityFilter = AFilter.PARTIAL;
    private final List<EntityType<?>> entities = new ArrayList<>();
    private AFilter lootTableFilter = AFilter.PARTIAL;
    private final List<ResourceLocation> lootTables = new ArrayList<>();

    private Function<ItemStack, ItemStack> replacer;

    public ALootRestriction(String id, String stage) {
        super(id, stage);
    }

    @SuppressWarnings("unused")
    public static ALootRestriction newBuilder() {
        return new ALootRestriction("null", "null");
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
    @DoNotCall
    @Info("Prefer using methods below!")
    public ALootRestriction restrict(Void unused) {
        return null;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ALootRestriction restrictItems(Item... items) {
        restrictedItems.addAll(List.of(items));
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ALootRestriction restrictTags(ResourceLocation... tags) {
        restrictedTags.addAll(List.of(tags));
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ALootRestriction restrictMods(String... modIds) {
        restrictedMods.addAll(List.of(modIds));
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ALootRestriction ignoredItems(Item... items) {
        ignoredItems.addAll(List.of(items));
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ALootRestriction ignoredTags(ResourceLocation... tags) {
        ignoredTags.addAll(List.of(tags));
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ALootRestriction restrictForEntities(EntityType<?>... entityTypes) {
        entities.addAll(List.of(entityTypes));
        return this;
    }

    @SuppressWarnings("unused")
    public ALootRestriction restrictForLootTables(ResourceLocation... lootTables) {
        this.lootTables.addAll(List.of(lootTables));
        return this;
    }

    @SuppressWarnings("unused")
    public ALootRestriction setReplacer(Function<ItemStack, ItemStack> replacer) {
        this.replacer = replacer;
        set(Attributes.HAS_REPLACER, true);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ALootRestriction setEntityFilter(AFilter filter) {
        entityFilter = filter;
        return this;
    }

    @SuppressWarnings("unused")
    public ALootRestriction setLootTableFilter(AFilter filter) {
        lootTableFilter = filter;
        return this;
    }

    @SuppressWarnings("unused")
    public ALootRestriction applyForEveryLootTableAndDrop(boolean value) {
        set(Attributes.APPLY_EVERYWHERE, value);
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

        if  (!restrictedItems.isEmpty() &&
                restrictedItems.stream().anyMatch(stack::is)) {
            return true;
        }

        if (!restrictedTags.isEmpty() &&
                restrictedTags.stream().anyMatch(tag -> stack.getTags().anyMatch(t -> t.location().equals(tag)))) {
            return true;
        }

        return false;
    }

    public boolean isRestricted(ItemStack stack, @Nullable EntityType<?> entityType, @Nullable ResourceLocation lootTable) {
        if (get(Attributes.APPLY_EVERYWHERE)) {
            return isRestricted(stack);
        }

        if (entityType != null) {
            if (entityFilter == AFilter.ALL) { return entities.contains(entityType); }

            return entities.contains(entityType) && isRestricted(stack);
        }

        if (lootTable != null) {
            if (lootTableFilter == AFilter.ALL) { return lootTables.contains(lootTable); }

            return lootTables.contains(lootTable) && isRestricted(stack);
        }

        return false;
    }

    public Function<ItemStack, ItemStack> getReplacer() {
        return replacer;
    }
}