package com.alessandro.astages.api.util;

import com.alessandro.astages.api.base.IndexedOrderedMultiMap;
import com.alessandro.astages.api.base.OrderedMultiMap;
import com.alessandro.astages.api.constant.ACompareCondition;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.restriction.ARestriction;
import com.alessandro.astages.api.wrapper.OreWrapper;
import com.alessandro.astages.api.wrapper.RecipeModWrapper;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.server.restriction.*;
import com.alessandro.astages.engine.server.restriction.item.AItemModRestriction;
import com.alessandro.astages.engine.server.restriction.item.AItemPredicateRestriction;
import com.alessandro.astages.engine.server.restriction.item.AItemRestriction;
import com.alessandro.astages.engine.server.restriction.item.AItemTagRestriction;
import com.alessandro.astages.engine.server.restriction.recipe.ARecipeModRestriction;
import com.alessandro.astages.engine.server.restriction.recipe.ARecipeRestriction;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

@NotNullParamsAndMethodsReturn
public class ARestrictionUtils {
//    public static <R extends ARestriction<R, ?, V>, V> @Nullable ARestriction<R, ?, V> getRestriction(AHolder holder, AStageType type, List<R> restrictions, V object) {
//        return restrictions.stream().filter(r ->
//            AStagesUtils.hasStage(holder, type, r.getStage()) &&
//            r.isRestricted(object)
//        ).findFirst().orElse(null);
//    }

    public static AItemRestriction addRestrictionForItem(String id, String stage, Item item) {
        var restriction = new AItemRestriction(id, stage);
        restriction.restrict(item);
        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AItemRestriction addRestrictionForItem(String id, String stage, List<Item> items) {
        var restriction = new AItemRestriction(id, stage);
        for (var item : items) { restriction.restrict(item); }
        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AItemPredicateRestriction addRestrictionForPredicate(String id, String stage, ResourceLocation modelId) {
        var restriction = new AItemPredicateRestriction(id, stage);
        restriction.restrict(modelId);
        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AItemModRestriction addRestrictionForMod(String id, String stage, String modId) {
        var restriction = new AItemModRestriction(id, stage);
        restriction.restrict(modId);
        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AItemModRestriction addRestrictionForMod(String id, String stage, List<String> modIds) {
        var restriction = new AItemModRestriction(id, stage);
        for (var modId : modIds) { restriction.restrict(modId); }
        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AItemTagRestriction addRestrictionForTag(String id, String stage, TagKey<Item> tag) {
        var restriction = new AItemTagRestriction(id, stage);
        restriction.restrict(tag);
        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AItemRestriction addRestrictionForArmor(String id, String stage, List<Item> armors) {
        var restriction = new AItemRestriction(id, stage);
        for (var armor : armors) { restriction.restrict(armor); }
        restriction.setArmorAttributes();
        ARestrictionManager.ITEM_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static void whiteListContainer(Class<?> containerClass, @Nullable List<Integer> slots) {
        ARestrictionManager.ITEM_INSTANCE.whiteListContainer(containerClass, slots);
    }

    public static ADimensionRestriction addRestrictionForDimension(String id, String stage, ResourceLocation dimension) {
        var restriction = new ADimensionRestriction(id, stage);
        restriction.restrict(dimension);
        ARestrictionManager.DIMENSION_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AMobRestriction addRestrictionForMob(String id, String stage, EntityType<?> mob) {
        var restriction = new AMobRestriction(id, stage);
        restriction.restrict(mob);
        ARestrictionManager.MOB_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static ARecipeRestriction addRestrictionForRecipe(String id, String stage, RecipeType<?> recipeType, List<ResourceLocation> recipeIds) {
        var restriction = new ARecipeRestriction(id, stage);
        for (ResourceLocation r : recipeIds) { restriction.restrict(new RecipeWrapper(recipeType, r)); }
        ARestrictionManager.RECIPE_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static ARecipeModRestriction addRestrictionForModRecipe(String id, String stage, String modId) {
        var restriction = new ARecipeModRestriction(id, stage);
        restriction.restrict(new RecipeModWrapper(modId));
        ARestrictionManager.RECIPE_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AScreenRestriction addRestrictionForScreen(String id, String stage, MenuType<?> menu) {
        var restriction = new AScreenRestriction(id, stage);
        restriction.restrict(menu);
        ARestrictionManager.SCREEN_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AOreRestriction addRestrictionForOre(String id, String stage, BlockState original, BlockState replacement) {
        var restriction = new AOreRestriction(id, stage);
        restriction.restrict(new OreWrapper(original, replacement));
        ARestrictionManager.ORE_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static APetRestriction addRestrictionForPet(String id, String stage, EntityType<?> pet) {
        var restriction = new APetRestriction(id, stage);
        restriction.restrict(pet);
        ARestrictionManager.PET_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AStructureRestriction addRestrictionForStructure(String id, String stage, List<ResourceLocation> structures) {
        var restriction = new AStructureRestriction(id, stage);
        for (var structure : structures) { restriction.restrict(structure); }
        ARestrictionManager.STRUCTURE_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AEnchantRestriction addRestrictionForEnchant(String id, String stage, Enchantment enchantment) {
        var restriction = new AEnchantRestriction(id, stage);
        restriction.restrict(enchantment);
        ARestrictionManager.ENCHANT_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AEnchantRestriction addRestrictionForEnchant(String id, String stage, Enchantment enchantment, ACompareCondition compareCondition, int level) {
        var restriction = new AEnchantRestriction(id, stage);
        restriction.restrict(enchantment)
            .set(Attributes.COMPARE_CONDITION, compareCondition)
            .set(Attributes.LEVEL, level);
        ARestrictionManager.ENCHANT_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static ACropRestriction addRestrictionForCrop(String id, String stage, Block crop) {
        var restriction = new ACropRestriction(id, stage);
        restriction.restrict(crop);
        ARestrictionManager.CROP_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static ACropRestriction addRestrictionForCrop(String id, String stage, Block crop, ACompareCondition compareCondition, int age) {
        var restriction = new ACropRestriction(id, stage);
        restriction.restrict(crop)
            .set(Attributes.COMPARE_CONDITION, compareCondition)
            .set(Attributes.AGE, age);
        ARestrictionManager.CROP_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static AEffectRestriction addRestrictionForEffect(String id, String stage, MobEffect effect) {
        var restriction = new AEffectRestriction(id, stage);
        restriction.restrict(effect);
        ARestrictionManager.EFFECT_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static ARegionRestriction addRestrictionForRegion(String id, String stage, ARegionRestriction.Type type, BlockPos center, int deltaX, int deltaY, int deltaZ) {
        var restriction = new ARegionRestriction(id, stage);
        restriction.setArea(type, center, deltaX, deltaY, deltaZ);

        ARestrictionManager.REGION_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static ARegionRestriction addRestrictionForRegion(String id, String stage, ARegionRestriction.Type type, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        var restriction = new ARegionRestriction(id, stage);
        restriction.setArea(type, minX, minY, minZ, maxX, maxY, maxZ);
        ARestrictionManager.REGION_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static ARegionRestriction addRestrictionForRegion(String id, String stage, ARegionRestriction.Type type, BlockPos center, int radius) {
        var restriction = new ARegionRestriction(id, stage);
        restriction.setArea(type, center, radius);
        ARestrictionManager.REGION_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static ARegionRestriction addRestrictionForRegion(String id, String stage, ARegionRestriction.Type type, BlockPos from, BlockPos to) {
        var restriction = new ARegionRestriction(id, stage);
        restriction.setArea(type, from, to);
        ARestrictionManager.REGION_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static ALootRestriction addRestrictionForLoot(String id, String stage) {
        var restriction = new ALootRestriction(id, stage);
        ARestrictionManager.LOOT_INSTANCE.addRestriction(restriction);

        return restriction;
    }

    public static  <W, R extends ARestriction<R, ?, ?>> @Nullable R getRestrictionFromCache(AHolder holder, OrderedMultiMap<W, R> cache, W value) {
        if (holder.isServerActive()) {
            var serverRestriction = getRestrictionFromCache(holder, AStageType.SERVER, cache, value);
            if (serverRestriction == null) { return null; }
        }

        if (holder.isPlayerActive()) {
            return getRestrictionFromCache(holder, AStageType.PLAYER, cache, value);
        }

        return null;
    }

    public static  <W, R extends ARestriction<R, ?, ?>> @Nullable R getRestrictionFromCache(AHolder holder, AStageType type, OrderedMultiMap<W, R> cache, W value) {
        var restrictions = cache.get(value);

        if (!restrictions.isEmpty()) {
            for (var restriction : restrictions) {
                if (!AStagesUtils.hasStage(holder, type, restriction.getStage())) {
                    return restriction;
                }
            }
        }

        return null;
    }

    public static  <W, R extends ARestriction<R, ?, ?>> @Nullable R getRestrictionFromCache(AHolder holder, IndexedOrderedMultiMap<W, R> cache, W value) {
        if (holder.isServerActive()) {
            var serverRestriction = getRestrictionFromCache(holder, AStageType.SERVER, cache, value);
            if (serverRestriction == null) { return null; }
        }

        if (holder.isPlayerActive()) {
            return getRestrictionFromCache(holder, AStageType.PLAYER, cache, value);
        }

        return null;
    }

    public static  <W, R extends ARestriction<R, ?, ?>> @Nullable R getRestrictionFromCache(AHolder holder, AStageType type, IndexedOrderedMultiMap<W, R> cache, W value) {
        var restrictions = cache.get(value);

        if (!restrictions.isEmpty()) {
            for (var restriction : restrictions) {
                if (!AStagesUtils.hasStage(holder, type, restriction.getStage())) {
                    return restriction;
                }
            }
        }

        return null;
    }
}
