package com.alessandro.astages.infrastructure.integration.kubejs.util;

import com.alessandro.astages.api.constant.ACompareCondition;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.stage.Stage;
import com.alessandro.astages.api.stage.TemporaryStage;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.api.store.config.AConfigPreset;
import com.alessandro.astages.api.time.ATime;
import com.alessandro.astages.api.util.APresetUtils;
import com.alessandro.astages.api.util.ARestrictionUtils;
import com.alessandro.astages.api.util.AStagesUtils;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.server.restriction.*;
import com.alessandro.astages.engine.server.restriction.item.AItemModRestriction;
import com.alessandro.astages.engine.server.restriction.item.AItemPredicateRestriction;
import com.alessandro.astages.engine.server.restriction.item.AItemRestriction;
import com.alessandro.astages.engine.server.restriction.item.AItemTagRestriction;
import com.alessandro.astages.engine.server.restriction.recipe.ARecipeModRestriction;
import com.alessandro.astages.engine.server.restriction.recipe.ARecipeRestriction;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
@NotNullParamsAndMethodsReturn
public class KubeJSServerUtils {
    // PLAYER Stages
    public static void addStageToPlayer(Player player, String stage) {
        AStagesUtils.addStage(AHolder.player(player), stage, true, true, true);
    }

    public static void addStagesToPlayer(Player player, Set<String> stages) {
        AStagesUtils.addStages(AHolder.player(player), stages, true, true, true);
    }

    public static void removeStageFromPlayer(Player player, String stage) {
        AStagesUtils.removeStage(AHolder.player(player), stage, true, true, true);
    }

    public static void removeStagesFromPlayer(Player player, Set<String> stages) {
        AStagesUtils.removeStages(AHolder.player(player), stages, true, true, true);
    }

    public static void removeAllStagesFromPlayer(Player player) {
        AStagesUtils.removeAllStages(AHolder.player(player), true, true, true);
    }

    public static void addStageToPlayer(Player player, String stage, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        AStagesUtils.addStage(AHolder.player(player), stage, showTitle, displayChatMessage, displayActionBarMessage);
    }

    public static void addStagesToPlayer(Player player, Set<String> stages, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        AStagesUtils.addStages(AHolder.player(player), stages, showTitle, displayChatMessage, displayActionBarMessage);
    }

    public static void removeStageFromPlayer(Player player, String stage, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        AStagesUtils.removeStage(AHolder.player(player), stage, showTitle, displayChatMessage, displayActionBarMessage);
    }

    public static void removeStagesFromPlayer(Player player, Set<String> stages, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        AStagesUtils.removeStages(AHolder.player(player), stages, showTitle, displayChatMessage, displayActionBarMessage);
    }

    public static void removeAllStagesFromPlayer(Player player, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        AStagesUtils.removeAllStages(AHolder.player(player), showTitle, displayChatMessage, displayActionBarMessage);
    }

    public static Set<String> getStagesFromPlayer(Player player) {
        return AStagesUtils.getStages(AHolder.player(player));
    }

    public static boolean playerHasStage(Player player, String stage) {
        return AStagesUtils.hasStage(AHolder.player(player), stage);
    }

    public static boolean playerHasAtLeastOneStage(Player player, Set<String> stages) {
        return AStagesUtils.hasAtLeastOneStage(AHolder.player(player), stages);
    }

    public static boolean playerHasAllStages(Player player, Set<String> stages) {
        return AStagesUtils.hasAllStages(AHolder.player(player), stages);
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public static void addStageToPlayer(String stage, Player player) {
        AStagesUtils.addStage(AHolder.player(player), stage, false, false, false);
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public static void removeStageFromPlayer(String stage, Player player) {
        AStagesUtils.removeStage(AHolder.player(player), stage, false, false, false);
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public static boolean playerHasStage(String stage, Player player) {
        return AStagesUtils.hasStage(AHolder.player(player), stage);
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public static boolean playerHasAtLeastOneStage(Set<String> stages, Player player) {
        return AStagesUtils.hasAtLeastOneStage(AHolder.player(player), stages);
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public static boolean playerHasAllStages(Set<String> stages, Player player) {
        return AStagesUtils.hasAllStages(AHolder.player(player), stages);
    }

    // SERVER Stages
    public static void addStageToServer(String stage) {
        AStagesUtils.addStage(AHolder.server(), stage, true, true, true);
    }

    public static void addStagesToServer(Set<String> stages) {
        AStagesUtils.addStages(AHolder.server(), stages, true, true, true);
    }

    public static void removeStageFromServer(String stage) {
        AStagesUtils.removeStage(AHolder.server(), stage, true, true, true);
    }

    public static void removeStagesFromServer(Set<String> stages) {
        AStagesUtils.removeStages(AHolder.server(), stages, true, true, true);
    }

    public static void removeAllStagesFromServer() {
        AStagesUtils.removeAllStages(AHolder.server(), true, true, true);
    }

    public static void addStageToServer(String stage, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        AStagesUtils.addStage(AHolder.server(), stage, showTitle, displayChatMessage, displayActionBarMessage);
    }

    public static void addStagesToServer(Set<String> stages, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        AStagesUtils.addStages(AHolder.server(), stages, showTitle, displayChatMessage, displayActionBarMessage);
    }

    public static void removeStageFromServer(String stage, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        AStagesUtils.removeStage(AHolder.server(), stage, showTitle, displayChatMessage, displayActionBarMessage);
    }

    public static void removeStagesFromServer(Set<String> stages, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        AStagesUtils.removeStages(AHolder.server(), stages, showTitle, displayChatMessage, displayActionBarMessage);
    }

    public static void removeAllStagesFromServer(boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        AStagesUtils.removeAllStages(AHolder.server(), showTitle, displayChatMessage, displayActionBarMessage);
    }

    public static Set<String> getStagesFromServer() {
        return AStagesUtils.getStages(AHolder.server());
    }

    public static boolean serverHasStage(String stage) {
        return AStagesUtils.hasStage(AHolder.server(), stage);
    }

    public static boolean serverHasAtLeastOneStage(Set<String> stages) {
        return AStagesUtils.hasAtLeastOneStage(AHolder.server(), stages);
    }

    public static boolean serverHasAllStages(Set<String> stages) {
        return AStagesUtils.hasAllStages(AHolder.server(), stages);
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public static void addStageToServer(String stage, MinecraftServer server) { // Server ignored!
        AStagesUtils.addStage(AHolder.server(), stage, false, false, false);
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public static void removeStageFromServer(String stage, MinecraftServer server) { // Server ignored!
        AStagesUtils.removeStage(AHolder.server(), stage, false, false, false);
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public static void removeAllStagesFromServer(MinecraftServer server) { // Server ignored!
        AStagesUtils.removeAllStages(AHolder.server(), false, false, false);
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public static boolean serverHasStage(String stage, MinecraftServer server) { // Server ignored!
        return AStagesUtils.hasStage(AHolder.server(), stage);
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public static boolean serverHasAtLeastOneStage(Set<String> stages, MinecraftServer server) { // Server ignored!
        return AStagesUtils.hasAtLeastOneStage(AHolder.server(), stages);
    }

    @Deprecated(forRemoval = true, since = "3.0.0")
    public static boolean serverHasAllStages(Set<String> stages, MinecraftServer server) { // Server ignored!
        return AStagesUtils.hasAllStages(AHolder.server(), stages);
    }

    // General
    public static <T> @Nullable T getRestrictionById(ARestrictionType type, String id) {
        return ARestrictionManager.getRestrictionById(id, type);
    }

    // Presets (old `config` system)
    public static <T> AConfigPreset<T> createPresetFor(Class<T> clazz) {
        return APresetUtils.createPresetFor(clazz);
    }

    // STAGES
    public static Stage customizeStage(String stageKey) {
        return AStagesUtils.customizeStage(stageKey);
    }

    public static Stage customizeStage(String stageKey, String description) {
        return AStagesUtils.customizeStage(stageKey, description);
    }

    public static TemporaryStage customizeTemporaryStage(String stageKey, ATime initialTime) {
        return AStagesUtils.customizeTemporaryStage(stageKey, initialTime);
    }

    public static TemporaryStage customizeTemporaryStage(String stageKey, String description, ATime initialTime) {
        return AStagesUtils.customizeTemporaryStage(stageKey, description, initialTime);
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

    public static AItemTagRestriction addRestrictionForTag(String id, String stage, TagKey<Item> tag) {
        return ARestrictionUtils.addRestrictionForTag(id, stage, tag);
    }

    public static AItemRestriction addRestrictionForArmor(String id, String stage, Item... armors) {
        return ARestrictionUtils.addRestrictionForArmor(id, stage, List.of(armors));
    }

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