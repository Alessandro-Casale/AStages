package com.alessandro.astages.integration.kubejs;

import com.alessandro.astages.api.ARestrictionUtils;
import com.alessandro.astages.api.AStagesUtils;
import com.alessandro.astages.api.constant.ACompareCondition;
import com.alessandro.astages.api.develop.UnderDevelopment;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.stage.Stage;
import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.capability.PlayerStageWrapper;
import com.alessandro.astages.capability.ServerStageData;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.AStageManager;
import com.alessandro.astages.core.server.restriction.*;
import com.alessandro.astages.core.server.restriction.item.AItemModRestriction;
import com.alessandro.astages.core.server.restriction.item.AItemPredicateRestriction;
import com.alessandro.astages.core.server.restriction.item.AItemRestriction;
import com.alessandro.astages.core.server.restriction.item.AItemTagRestriction;
import com.alessandro.astages.core.server.restriction.recipe.ARecipeModRestriction;
import com.alessandro.astages.core.server.restriction.recipe.ARecipeRestriction;
import com.alessandro.astages.store.ARestrictionType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

@NotNullParamsAndMethodsReturn
@SuppressWarnings("unused")
public class AStagesKubeJSUtil {
    // Player Stages
    public static void addStageToPlayer(String stage, Player player) {
        AStagesUtils.addStage(AHolder.player(player), stage, false);
    }

    public static void removeStageFromPlayer(String stage, Player player) {
        AStagesUtils.removeStage(AHolder.player(player), stage, false);
    }

    public static List<String> getStagesFromPlayer(Player player) {
        return new ArrayList<>(AStagesUtils.getStages(AHolder.player(player)));
    }

    public static void removeAllStagesFromPlayer(Player player) {
        AStagesUtils.removeAllStages(AHolder.player(player), false);
    }

    public static boolean playerHasStage(String stage, Player player) {
        return AStagesUtils.hasStage(AHolder.player(player), stage);
    }

    public static boolean playerHasAtLeastOneStage(List<String> stages, Player player) {
        return AStagesUtils.hasAtLeastOneStage(AHolder.player(player), stages);
    }

    public static boolean playerHasAllStages(List<String> stages, Player player) {
        return AStagesUtils.hasAllStages(AHolder.player(player), stages);
    }

    @SuppressWarnings("removal")
    @Deprecated(forRemoval = true)
    public static PlayerStage getPlayerData(Player player) {
        return PlayerStageWrapper.getPlayerData(player);
    }

    // Server Stages
    public static void addStageToServer(String stage, MinecraftServer server) { // Server ignored!
        AStagesUtils.addStage(AHolder.server(), stage, false);
    }

    public static void removeStageFromServer(String stage, MinecraftServer server) { // Server ignored!
        AStagesUtils.removeStage(AHolder.server(), stage, false);
    }

    public static void removeAllStagesFromServer(MinecraftServer server) { // Server ignored!
        AStagesUtils.removeAllStages(AHolder.server(), false);
    }

    public static boolean serverHasStage(String stage, MinecraftServer server) { // Server ignored!
        return AStagesUtils.hasStage(AHolder.server(), stage);
    }

    public static boolean serverHasAtLeastOneStage(List<String> stages, MinecraftServer server) { // Server ignored!
        return AStagesUtils.hasAtLeastOneStage(AHolder.server(), stages);
    }

    public static boolean serverHasAllStages(List<String> stages, MinecraftServer server) { // Server ignored!
        return AStagesUtils.hasAllStages(AHolder.server(), stages);
    }

    @SuppressWarnings("removal")
    @Deprecated(forRemoval = true)
    public static ServerStageData getServerData(MinecraftServer server) {
        return ServerStageData.getData(server);
    }

    // General
    public static <T> @Nullable T getRestrictionById(ARestrictionType type, String id) {
        return ARestrictionManager.getRestrictionById(id, type);
    }

    // STAGES
    public static Stage customizeStage(String s) {
        var stage = new Stage(s);
        AStageManager.PERMANENT_INSTANCE.addStage(stage);
        return stage;
    }

    // ITEM Restrictions
    public static AItemRestriction addRestrictionForItem(String id, String stage, Item... items) {
        return ARestrictionUtils.addRestrictionForItem(id, stage, List.of(items));
    }

    public static AItemPredicateRestriction addRestrictionForPredicate(String id, String stage, ResourceLocation modelId) {
        return ARestrictionUtils.addRestrictionForPredicate(id, stage, modelId);
    }

    public static AItemModRestriction addRestrictionForMod(String id, String stage, String... modIds) {
        return ARestrictionUtils.addRestrictionForMod(id, stage, List.of(modIds));
    }

    public static AItemTagRestriction addRestrictionForTag(String id, String stage, ResourceLocation name) {
        return ARestrictionUtils.addRestrictionForTag(id, stage, name);
    }

    public static AItemRestriction addRestrictionForArmor(String id, String stage, Item... armors) {
        return ARestrictionUtils.addRestrictionForArmor(id, stage, List.of(armors));
    }

    @UnderDevelopment("ADD TO WIKI PAGE")
    public static void whiteListContainer(Class<?> containerClass, @Nullable List<Integer> slots) {
        ARestrictionUtils.whiteListContainer(containerClass, slots);
    }

    // DIMENSION Restrictions
    public static ADimensionRestriction addRestrictionForDimension(String id, String stage, ResourceLocation dimension) {
        return ARestrictionUtils.addRestrictionForDimension(id, stage, dimension);
    }

    // MOB Restrictions
    public static AMobRestriction addRestrictionForMob(String id, String stage, EntityType<?> mob) {
        return ARestrictionUtils.addRestrictionForMob(id, stage, mob);
    }

    // RECIPE Restrictions
    public static ARecipeRestriction addRestrictionForRecipe(String id, String stage, RecipeType<?> recipeType, ResourceLocation... recipeIds) {
        return ARestrictionUtils.addRestrictionForRecipe(id, stage, recipeType, List.of(recipeIds));
    }

    public static ARecipeModRestriction addRestrictionForModRecipe(String id, String stage, String modId) {
        return ARestrictionUtils.addRestrictionForModRecipe(id, stage, modId);
    }

    // SCREEN Restrictions
    public static AScreenRestriction addRestrictionForScreen(String id, String stage, MenuType<?> menu) {
        return ARestrictionUtils.addRestrictionForScreen(id, stage, menu);
    }

    // ORE Restrictions
    public static AOreRestriction addRestrictionForOre(String id, String stage, BlockState original, BlockState replacement) {
        return ARestrictionUtils.addRestrictionForOre(id, stage, original, replacement);
    }

    // PET Restrictions
    public static APetRestriction addRestrictionForPet(String id, String stage, EntityType<?> pet) {
        return ARestrictionUtils.addRestrictionForPet(id, stage, pet);
    }

    // STRUCTURE Restrictions
    public static AStructureRestriction addRestrictionForStructure(String id, String stage, ResourceLocation... structures) {
        return ARestrictionUtils.addRestrictionForStructure(id, stage, List.of(structures));
    }

    // ENCHANT Restrictions
    public static AEnchantRestriction addRestrictionForEnchant(String id, String stage, Enchantment enchantment) {
        return ARestrictionUtils.addRestrictionForEnchant(id, stage, enchantment);
    }

    public static AEnchantRestriction addRestrictionForEnchant(String id, String stage, Enchantment enchantment, ACompareCondition compareCondition, int level) {
        return ARestrictionUtils.addRestrictionForEnchant(id, stage, enchantment, compareCondition, level);
    }

//    static {
//        ARestrictionUtils.addRestrictionForEnchant("id1", "stage_enchant_java", Registries.ENCHANTMENT.)
//    }

    // CROP Restrictions
    public static ACropRestriction addRestrictionForCrop(String id, String stage, Block crop) {
        return ARestrictionUtils.addRestrictionForCrop(id, stage, crop);
    }

    public static ACropRestriction addRestrictionForCrop(String id, String stage, Block crop, ACompareCondition compareCondition, int age) {
        return ARestrictionUtils.addRestrictionForCrop(id, stage, crop, compareCondition, age);
    }

    // EFFECT Restrictions
    public static AEffectRestriction addRestrictionForEffect(String id, String stage, MobEffect effect) {
        return ARestrictionUtils.addRestrictionForEffect(id, stage, effect);
    }

    // REGION Restrictions
    public static ARegionRestriction addRestrictionForRegion(String id, String stage, ARegionRestriction.Type type, BlockPos center, int deltaX, int deltaY, int deltaZ) {
        return ARestrictionUtils.addRestrictionForRegion(id, stage, type, center, deltaX, deltaY, deltaZ);
    }

    public static ARegionRestriction addRestrictionForRegion(String id, String stage, ARegionRestriction.Type type, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        return ARestrictionUtils.addRestrictionForRegion(id, stage, type, minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static ARegionRestriction addRestrictionForRegion(String id, String stage, ARegionRestriction.Type type, BlockPos center, int radius) {
        return ARestrictionUtils.addRestrictionForRegion(id, stage, type, center, radius);
    }

    public static ARegionRestriction addRestrictionForRegion(String id, String stage, ARegionRestriction.Type type, BlockPos from, BlockPos to) {
        return ARestrictionUtils.addRestrictionForRegion(id, stage, type, from, to);
    }

    // LOOT Restrictions
    public static ALootRestriction addRestrictionForLoot(String id, String stage) {
        return ARestrictionUtils.addRestrictionForLoot(id, stage);
    }
}
