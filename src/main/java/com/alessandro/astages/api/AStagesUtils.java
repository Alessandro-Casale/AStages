package com.alessandro.astages.api;

import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.constant.AStatus;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.holder.AStageHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.capability.OfflinePlayerStage;
import com.alessandro.astages.capability.ServerStage;
import com.alessandro.astages.config.AStagesCommon;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.AStageManager;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@NotNullParamsAndMethodsReturn
public class AStagesUtils {
    public static Set<String> getStages(AHolder holder) {
        return holder.getStages().getAllStages();
    }

    public static boolean hasStage(AHolder holder, String stage) {
        return getStages(holder).contains(stage);
    }

    public static boolean hasStage(AHolder holder, AStageType type, String stage) {
        return holder.getStages().getForType(type).contains(stage);
    }

    public static boolean hasStage(AStageHolder holder, AStageType type, String stage) {
        return holder.getForType(type).contains(stage);
    }

    public static boolean hasAtLeastOneStage(AHolder holder, List<String> stages) {
        return getStages(holder).stream().anyMatch(stages::contains);
    }

    public static boolean hasAllStages(AHolder holder, List<String> stages) {
        return getStages(holder).containsAll(stages);
    }

    public static void addStage(AHolder holder, String stage, boolean silentTitle) {
        holder.perform(
            player -> {
                OfflinePlayerStage.addPlayerStage(player, stage);
                OfflinePlayerStage.synchronizeWithClient(player, AOperation.ADD, stage, silentTitle);
            }, server -> {
                ServerStage.addServerStage(stage);
                ServerStage.synchronizeWithClient(null, AOperation.ADD, stage);
            }
        );
    }

    public static void addStages(AHolder holder, Set<String> stages, boolean silentTitle) {
        holder.perform(
            player -> {
                OfflinePlayerStage.addPlayerStages(player, stages);
                OfflinePlayerStage.synchronizeWithClient(player, AOperation.ADD_ALL, stages, silentTitle);
            }, server -> {
                ServerStage.addServerStages(stages);
                ServerStage.synchronizeWithClient(null, AOperation.ADD_ALL, stages);
            }
        );
    }

    public static void addAllStages(AHolder holder, boolean silentTitle) {
        var stages = ARestrictionManager.ALL_STAGES;

        holder.perform(
            player -> {
                OfflinePlayerStage.addPlayerStages(player, stages);
                OfflinePlayerStage.synchronizeWithClient(player, AOperation.ADD_ALL, stages, silentTitle);
            }, server -> {
                ServerStage.addServerStages(stages);
                ServerStage.synchronizeWithClient(null, AOperation.ADD_ALL, stages);
            }
        );
    }

    public static AStatus removeStage(AHolder holder, String stage, boolean silentTitle) {
        AtomicReference<AStatus> toReturn = new AtomicReference<>(AStatus.NOT_PRESENT);

        holder.perform(
            player -> {
                toReturn.set(OfflinePlayerStage.removePlayerStage(player, stage));
                OfflinePlayerStage.synchronizeWithClient(player, AOperation.REMOVE, stage, silentTitle);
            }, server -> {
                toReturn.set(ServerStage.removeServerStage(stage));
                ServerStage.synchronizeWithClient(null, AOperation.REMOVE, stage);
            }
        );

        return toReturn.get();
    }

    public static AStatus removeStages(AHolder holder, Set<String> stages, boolean silentTitle) {
        AtomicReference<AStatus> toReturn = new AtomicReference<>(AStatus.NOT_PRESENT);

        holder.perform(
            player -> {
                toReturn.set(OfflinePlayerStage.removePlayerStages(player, stages));
                OfflinePlayerStage.synchronizeWithClient(player, AOperation.REMOVE_ALL, stages, silentTitle);
            }, server -> {
                toReturn.set(ServerStage.removeServerStages(stages));
                ServerStage.synchronizeWithClient(null, AOperation.REMOVE_ALL, stages);
            }
        );

        return toReturn.get();
    }

    public static AStatus removeAllStages(AHolder holder, boolean silentTitle) {
        AtomicReference<AStatus> toReturn = new AtomicReference<>(AStatus.NOT_PRESENT);

        holder.perform(
            player -> {
                var stages = OfflinePlayerStage.getPlayerStagesFromCache(player);
                toReturn.set(OfflinePlayerStage.removePlayerStages(player, stages));
                OfflinePlayerStage.synchronizeWithClient(player, AOperation.REMOVE_ALL, stages, silentTitle);
            }, server -> {
                var stages = ServerStage.getServerStages();
                toReturn.set(ServerStage.removeServerStages(stages));
                ServerStage.synchronizeWithClient(null, AOperation.REMOVE_ALL, stages);
            }
        );

        return toReturn.get();
    }

    public static void synchronizeWithClient(AHolder holder, ServerPlayer toPlayer, AOperation operation, Set<String> stages, boolean silentTitle) {
        holder.perform(
            player -> OfflinePlayerStage.synchronizeWithClient(player, operation, stages, silentTitle),
            server -> ServerStage.synchronizeWithClient(toPlayer, operation, stages)
        );
    }


    public static void checkPlayerStages(@Nullable Player player, AOperation operation, Set<String> stages) {
        checkStages(player, stage -> {
            if (AStageManager.GENERIC_INSTANCE.isServerOnly(stage)) {
                throw new IllegalArgumentException("Trying to add stage " + stage + " that is marked as available in server only!");
            }
        }, operation, stages);
    }

    public static void checkServerStages(AOperation operation, Set<String> stages) {
        checkStages(ServerLifecycleHooks.getCurrentServer(), stage -> {
            if (AStageManager.GENERIC_INSTANCE.isPlayerOnly(stage)) {
                throw new IllegalArgumentException("Trying to add stage " + stage + " that is marked as available in player only!");
            }
        }, operation, stages);
    }

    public static void checkStages(@Nullable CommandSource chatSource, Consumer<String> checker, AOperation operation, Set<String> stages) {
        if (operation.supportOnlyOneStage() && stages.size() != 1) {
            throw new IllegalArgumentException("Trying to perform an action that supports single stage, using multiple ones!");
        }

        if (operation.needToBeChecked()) {
            for (var stage : stages) {
                checker.accept(stage);
            }
        }

        if (AStagesCommon.ENABLE_STAGE_WARNING.get()) {
            if (chatSource != null && operation.handleStageRecognization()) {
                for (var stage : stages) {
                    if (!ARestrictionManager.ALL_STAGES.contains(stage)) {
                        chatSource.sendSystemMessage(Component.literal("⚠ Warning: stage " + stage + " not recognized!").withStyle(ChatFormatting.GOLD));
                    }
                }
            }
        }
    }
}
