package com.alessandro.astages.api.util;

import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.constant.AStatus;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.holder.AStageHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.stage.Stage;
import com.alessandro.astages.api.stage.TemporaryStage;
import com.alessandro.astages.api.time.ATime;
import com.alessandro.astages.engine.AStageManager;
import com.alessandro.astages.engine.server.MiscStorage;
import com.alessandro.astages.infrastructure.capability.OfflinePlayerStage;
import com.alessandro.astages.infrastructure.capability.ServerStage;
import com.alessandro.astages.infrastructure.config.AStagesCommon;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@NotNullParamsAndMethodsReturn
public class AStagesUtils {
    public static Stage customizeStage(String stageKey) {
        var stage = new Stage(stageKey);
        AStageManager.PERMANENT_INSTANCE.addStage(stage);
        return stage;
    }

    public static Stage customizeStage(String stageKey, String description) {
        var stage = new Stage(stageKey, description);
        AStageManager.PERMANENT_INSTANCE.addStage(stage);
        return stage;
    }

    public static TemporaryStage customizeTemporaryStage(String stageKey, ATime initialTime) {
        var stage = new TemporaryStage(stageKey, initialTime);
        AStageManager.TEMPORARY_INSTANCE.addStage(stage);
        return stage;
    }

    public static TemporaryStage customizeTemporaryStage(String stageKey, String description, ATime initialTime) {
        var stage = new TemporaryStage(stageKey, description, initialTime);
        AStageManager.TEMPORARY_INSTANCE.addStage(stage);
        return stage;
    }

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

    public static boolean hasAtLeastOneStage(AHolder holder, Set<String> stages) {
        return getStages(holder).stream().anyMatch(stages::contains);
    }

    public static boolean hasAllStages(AHolder holder, Set<String> stages) {
        return getStages(holder).containsAll(stages);
    }

    public static void addStage(AHolder holder, String stage, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        holder.perform(
            player -> {
                OfflinePlayerStage.addPlayerStage(player, stage);
                var isSynced = OfflinePlayerStage.synchronizeWithClient(player, AOperation.ADD, stage);
                OfflinePlayerStage.displayStageAlert(player, AOperation.ADD, stage, AStatus.SUCCESSFUL, !isSynced, showTitle, displayChatMessage, displayActionBarMessage);
            }, server -> {
                ServerStage.addServerStage(stage);
                var isSynced = ServerStage.synchronizeWithClient(null, AOperation.ADD, stage);
                ServerStage.displayStageAlert(server, AOperation.ADD, stage, AStatus.SUCCESSFUL, !isSynced, showTitle, displayChatMessage, displayActionBarMessage);
            }
        );
    }

    public static void addStages(AHolder holder, Set<String> stages, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        holder.perform(
            player -> {
                OfflinePlayerStage.addPlayerStages(player, stages);
                var isSynced = OfflinePlayerStage.synchronizeWithClient(player, AOperation.ADD_ALL, stages);
                OfflinePlayerStage.displayStageAlert(player, AOperation.ADD_ALL, stages, AStatus.SUCCESSFUL, !isSynced, showTitle, displayChatMessage, displayActionBarMessage);
            }, server -> {
                ServerStage.addServerStages(stages);
                var isSynced = ServerStage.synchronizeWithClient(null, AOperation.ADD_ALL, stages);
                ServerStage.displayStageAlert(server, AOperation.ADD_ALL, stages, AStatus.SUCCESSFUL, !isSynced, showTitle, displayChatMessage, displayActionBarMessage);
            }
        );
    }

    public static void addAllStages(AHolder holder, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        var stages = MiscStorage.ALL_STAGES;

        holder.perform(
            player -> {
                OfflinePlayerStage.addPlayerStages(player, stages);
                var isSynced = OfflinePlayerStage.synchronizeWithClient(player, AOperation.ADD_ALL, stages);
                OfflinePlayerStage.displayStageAlert(player, AOperation.ADD_ALL, stages, AStatus.SUCCESSFUL, !isSynced, showTitle, displayChatMessage, displayActionBarMessage);
            }, server -> {
                ServerStage.addServerStages(stages);
                var isSynced = ServerStage.synchronizeWithClient(null, AOperation.ADD_ALL, stages);
                ServerStage.displayStageAlert(server, AOperation.ADD_ALL, stages, AStatus.SUCCESSFUL, !isSynced, showTitle, displayChatMessage, displayActionBarMessage);
            }
        );
    }

    public static AStatus removeStage(AHolder holder, String stage, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        AtomicReference<AStatus> toReturn = new AtomicReference<>(AStatus.NOT_PRESENT);

        holder.perform(
            player -> {
                toReturn.set(OfflinePlayerStage.removePlayerStage(player, stage));
                var isSynced = OfflinePlayerStage.synchronizeWithClient(player, AOperation.REMOVE, stage);
                OfflinePlayerStage.displayStageAlert(player, AOperation.REMOVE, stage, toReturn.get(), !isSynced, showTitle, displayChatMessage, displayActionBarMessage);
            }, server -> {
                toReturn.set(ServerStage.removeServerStage(stage));
                var isSynced = ServerStage.synchronizeWithClient(null, AOperation.REMOVE, stage);
                ServerStage.displayStageAlert(server, AOperation.REMOVE, stage, toReturn.get(), !isSynced, showTitle, displayChatMessage, displayActionBarMessage);
            }
        );

        return toReturn.get();
    }

    public static AStatus removeStages(AHolder holder, Set<String> stages, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        AtomicReference<AStatus> toReturn = new AtomicReference<>(AStatus.NOT_PRESENT);

        holder.perform(
            player -> {
                toReturn.set(OfflinePlayerStage.removePlayerStages(player, stages));
                var isSynced = OfflinePlayerStage.synchronizeWithClient(player, AOperation.REMOVE_ALL, stages);
                OfflinePlayerStage.displayStageAlert(player, AOperation.REMOVE_ALL, stages, toReturn.get(), !isSynced, showTitle, displayChatMessage, displayActionBarMessage);
            }, server -> {
                toReturn.set(ServerStage.removeServerStages(stages));
                var isSynced = ServerStage.synchronizeWithClient(null, AOperation.REMOVE_ALL, stages);
                ServerStage.displayStageAlert(server, AOperation.REMOVE_ALL, stages, toReturn.get(), !isSynced, showTitle, displayChatMessage, displayActionBarMessage);
            }
        );

        return toReturn.get();
    }

    public static AStatus removeAllStages(AHolder holder, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        AtomicReference<AStatus> toReturn = new AtomicReference<>(AStatus.NOT_PRESENT);

        holder.perform(
            player -> {
                var stages = OfflinePlayerStage.getPlayerStagesFromCache(player);
                toReturn.set(OfflinePlayerStage.removePlayerStages(player, stages));
                var isSynced = OfflinePlayerStage.synchronizeWithClient(player, AOperation.REMOVE_ALL, stages);
                OfflinePlayerStage.displayStageAlert(player, AOperation.REMOVE_ALL, stages, toReturn.get(), !isSynced, showTitle, displayChatMessage, displayActionBarMessage);
            }, server -> {
                var stages = ServerStage.getServerStages();
                toReturn.set(ServerStage.removeServerStages(stages));
                var isSynced = ServerStage.synchronizeWithClient(null, AOperation.REMOVE_ALL, stages);
                ServerStage.displayStageAlert(server, AOperation.REMOVE_ALL, stages, toReturn.get(), !isSynced, showTitle, displayChatMessage, displayActionBarMessage);
            }
        );

        return toReturn.get();
    }

    public static void synchronizeWithClient(AHolder holder, ServerPlayer toPlayer, AOperation operation, Set<String> stages) {
        holder.perform(
            player -> OfflinePlayerStage.synchronizeWithClient(player, operation, stages),
            server -> ServerStage.synchronizeWithClient(toPlayer, operation, stages)
        );
    }

    public static void checkPlayerStage(UUID player, AOperation operation, String stage) {
        checkPlayerStages(player, operation, ASetUtils.singleton(stage));
    }

    public static void checkPlayerStages(UUID player, AOperation operation, Set<String> stages) {
        checkStages(APlayerUtils.getPlayerFromUUID(player), stage -> {
            if (AStageManager.GENERIC_INSTANCE.isServerOnly(stage)) {
                throw new IllegalArgumentException(Component.translatable("message.astages.check.server_only", stage).getString());
            }
        }, operation, stages);
    }

    public static void checkServerStage(AOperation operation, String stage) {
        checkServerStages(operation, ASetUtils.singleton(stage));
    }

    public static void checkServerStages(AOperation operation, Set<String> stages) {
        checkStages(ServerLifecycleHooks.getCurrentServer(), stage -> {
            if (AStageManager.GENERIC_INSTANCE.isPlayerOnly(stage)) {
                throw new IllegalArgumentException(Component.translatable("message.astages.check.player_only", stage).getString());
            }
        }, operation, stages);
    }

    public static void checkStages(@Nullable CommandSource chatSource, Consumer<String> checker, AOperation operation, Set<String> stages) {
        if (operation.supportOnlyOneStage() && stages.size() != 1) {
            throw new IllegalArgumentException(Component.translatable("message.astages.check.unsupported_multiple_stage_action").getString());
        }

        if (operation.needToBeChecked()) {
            for (var stage : stages) {
                checker.accept(stage);
            }
        }

        if (AStagesCommon.ENABLE_STAGE_WARNING.get()) {
            if (chatSource != null && operation.handleStageRecognization()) {
                for (var stage : stages) {
                    if (!MiscStorage.ALL_STAGES.contains(stage)) {
                        chatSource.sendSystemMessage(Component.translatable("message.astages.warning.unknown_stage", stage).withStyle(ChatFormatting.GOLD));
                    }
                }
            }
        }
    }
}