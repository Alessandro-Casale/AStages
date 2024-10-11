package com.alessandro.astages.integration.kubejs;

import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.capability.PlayerStageProvider;
import com.alessandro.astages.core.ADimensionRestriction;
import com.alessandro.astages.core.AItemRestriction;
import com.alessandro.astages.core.AMobRestriction;
import com.alessandro.astages.core.ARestrictionManager;
//import com.alessandro.astages.util.RestrictionType;
import com.alessandro.astages.util.ARestriction;
import com.alessandro.astages.util.ARestrictionType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class AStagesKubeJSUtil {
    // Player Stages
    public static void addStageForPlayer(String stage, @NotNull Player player) {
        player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> {
            playerStage.addStage(stage);
            playerStage.setChangedFor(player, PlayerStage.Operation.ADD, stage);
        });
    }

    public static void removeStageForPlayer(String stage, @NotNull Player player) {
        player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> {
            playerStage.removeStage(stage);
            playerStage.setChangedFor(player, PlayerStage.Operation.REMOVE, stage);
        });
    }

    public static void removeAllStagesForPlayer(@NotNull Player player) {
        player.getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> {
            playerStage.removeAllStages();
            playerStage.setChangedFor(player, PlayerStage.Operation.REMOVE_ALL, null);
        });
    }

    public static ARestriction getRestrictionById(ARestrictionType type, String id) {
        return ARestrictionManager.getRestrictionById(type, id);
    }

    // ITEM Restrictions
    public static @NotNull AItemRestriction addRestrictionForItem(String id, String stage, Item @NotNull ... items) {
        // var restriction = new AItemRestriction(id, RestrictionType.RUNTIME);
        var restriction = new AItemRestriction(id);

        for (var item : items) {
            restriction.restrict(itemStack -> itemStack.is(item));
        }

        ARestrictionManager.ITEM_INSTANCE.addRestriction(stage, restriction);

        return restriction;
    }

    public static @NotNull AItemRestriction addRestrictionForPredicate(String id, String stage, Predicate<ItemStack> predicate) {
        // var restriction = new AItemRestriction(id, RestrictionType.RUNTIME);
        var restriction = new AItemRestriction(id);
        restriction.restrict(predicate);

        ARestrictionManager.ITEM_INSTANCE.addRestriction(stage, restriction);

        return restriction;
    }

    public static @NotNull AItemRestriction addRestrictionForMod(String id, String stage, String modId) {
        // var restriction = new AItemRestriction(id, RestrictionType.RUNTIME);
        var restriction = new AItemRestriction(id);
        restriction.restrict(itemStack -> modId.equalsIgnoreCase(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemStack.getItem())).getNamespace()));

        ARestrictionManager.ITEM_INSTANCE.addRestriction(stage, restriction);

        return restriction;
    }

    // DIMENSION Restrictions
    public static @NotNull ADimensionRestriction addRestrictionForDimension(String id, String stage, ResourceLocation dimension) {
        var restriction = new ADimensionRestriction(id);
        restriction.restrict(dimension);

        ARestrictionManager.DIMENSION_INSTANCE.addRestriction(stage, restriction);

        return restriction;
    }

    // MOB Restrictions
    public static @NotNull AMobRestriction addRestrictionForMob(String id, String stage, EntityType<?> mob) {
        var restriction = new AMobRestriction(id);
        restriction.restrict(mob);

        ARestrictionManager.MOB_INSTANCE.addRestriction(stage, restriction);

        return restriction;
    }
}
