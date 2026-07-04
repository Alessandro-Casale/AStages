package com.alessandro.astages.api.util;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.constant.AStatus;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.stage.BaseStage;
import com.alessandro.astages.api.alert.StageAlert;
import com.alessandro.astages.engine.store.StageAttributes;
import com.alessandro.astages.infrastructure.config.AStagesCommon;
import net.minecraft.network.chat.Component;

import java.util.Set;

@NotNullParams
public class ATitleUtils {
    public static void displayStageAlert(AHolder holder, AOperation operation, Set<String> stageKeys, AStatus status, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        AStages.LOGGER.debug("{}: {}", operation, stageKeys);

        switch (operation) {
            case ADD ->
                AAlertUtils.genericAlert(
                    ATitleUtils::addStageAlert,
                    holder,
                    ASetUtils.getOnlyElement(stageKeys),
                    status,
                    showTitle,
                    displayChatMessage,
                    displayActionBarMessage
                );

            case ADD_ALL -> {
                if (AStagesCommon.ENABLE_ADD_ALL_OPERATION.get()) {
                    AAlertUtils.genericAlertsWithHeader(
                        ATitleUtils::addAllStagesHeader,
                        ATitleUtils::addAllStagesAlert,
                        holder,
                        stageKeys,
                        status,
                        showTitle,
                        displayChatMessage,
                        displayActionBarMessage
                    );
                }
            }

            case REMOVE ->
                AAlertUtils.genericAlert(
                    ATitleUtils::removeStageAlert,
                    holder,
                    ASetUtils.getOnlyElement(stageKeys),
                    status,
                    showTitle,
                    displayChatMessage,
                    displayActionBarMessage
                );

            case REMOVE_ALL -> {
                if (AStagesCommon.ENABLE_REMOVE_ALL_OPERATION.get()) {
                    AAlertUtils.genericAlertsWithHeader(
                        ATitleUtils::removeAllStagesHeader,
                        ATitleUtils::removeAllStagesAlert,
                        holder,
                        stageKeys,
                        status,
                        showTitle,
                        displayChatMessage,
                        displayActionBarMessage
                    );
                }
            }
        }
    }

    public static void addStageAlert(StageAlert builder, AHolder holder, AStageType heldType, String stageKey, AStatus status, @Nullable BaseStage<?> customConfig) {
        if (customConfig != null) {
            builder.configureTitle(customConfig.getMessageOrNull(StageAttributes.TITLE_ADD, stageKey));
            builder.configureSubtitle(customConfig.getMessageOrNull(StageAttributes.SUBTITLE_ADD, stageKey));
            builder.configureChatMessage(customConfig.getMessageOrNull(StageAttributes.CHAT_MESSAGE_ADD, stageKey));
            builder.configureActionBarMessage(customConfig.getMessageOrNull(StageAttributes.ACTION_BAR_MESSAGE_ADD, stageKey));

            builder.setupTimings(
                customConfig.get(StageAttributes.FADE_IN),
                customConfig.get(StageAttributes.STAY),
                customConfig.get(StageAttributes.FADE_OUT)
            );
        } else {
            var component = Component.translatable("message.astages." + AStageType.getDescriptionIdFor(heldType) + ".add", stageKey).withStyle(AStagesCommon.STAGE_ADD_COLOR.get());

            switch (AStagesCommon.STAGE_ADD_DISPLAY_TYPE.get()) {
                case TITLE -> builder.configureTitle(component);
                case CHAT -> builder.configureChatMessage(component);
                case ACTION_BAR -> builder.configureActionBarMessage(component);
            }

            builder.setupTimings(
                AStagesCommon.STAGE_ADD_FADE_IN_TICKS.get(),
                AStagesCommon.STAGE_ADD_STAY_TICKS.get(),
                AStagesCommon.STAGE_ADD_FADE_OUT_TICKS.get()
            );
        }
    }

    public static void addAllStagesHeader(StageAlert header, AHolder holder, AStageType heldType) {
        header.configureChatMessage(Component.translatable("message.astages." + AStageType.getDescriptionIdFor(heldType) + ".add_all").withStyle(AStagesCommon.STAGE_ADD_COLOR.get()));
    }

    public static void addAllStagesAlert(StageAlert builder, AHolder holder, AStageType heldType, String stageKey, AStatus status, @Nullable BaseStage<?> customConfig) {
        if (customConfig != null) {
            builder.configureChatMessage(customConfig.getMessageOrNull(StageAttributes.CHAT_MESSAGE_ADD, stageKey));
        } else {
            var component = Component.translatable("message.astages." + AStageType.getDescriptionIdFor(heldType) + ".list_item", stageKey).withStyle(AStagesCommon.STAGE_ADD_COLOR.get());
            builder.configureChatMessage(component);
        }
    }

    public static void removeStageAlert(StageAlert builder, AHolder holder, AStageType heldType, String stageKey, AStatus status, @Nullable BaseStage<?> customConfig) {
        if (customConfig != null) {
            builder.configureTitle(customConfig.getMessageOrNull(StageAttributes.TITLE_REMOVE, stageKey));
            builder.configureSubtitle(customConfig.getMessageOrNull(StageAttributes.SUBTITLE_REMOVE, stageKey));
            builder.configureChatMessage(customConfig.getMessageOrNull(StageAttributes.CHAT_MESSAGE_REMOVE, stageKey));
            builder.configureActionBarMessage(customConfig.getMessageOrNull(StageAttributes.ACTION_BAR_MESSAGE_REMOVE, stageKey));

            builder.setupTimings(
                customConfig.get(StageAttributes.FADE_IN),
                customConfig.get(StageAttributes.STAY),
                customConfig.get(StageAttributes.FADE_OUT)
            );
        } else {
            var component = Component.translatable("message.astages." + AStageType.getDescriptionIdFor(heldType) + ".remove", stageKey).withStyle(AStagesCommon.STAGE_REMOVE_COLOR.get());

            if (status == AStatus.NOT_PRESENT) {
                component = Component.translatable("message.astages." + AStageType.getDescriptionIdFor(heldType) + ".not_present", stageKey).withStyle(AStagesCommon.STAGE_REMOVE_COLOR.get());
            }

            switch (AStagesCommon.STAGE_REMOVE_DISPLAY_TYPE.get()) {
                case TITLE -> builder.configureTitle(component);
                case CHAT -> builder.configureChatMessage(component);
                case ACTION_BAR -> builder.configureActionBarMessage(component);
            }

            builder.setupTimings(
                AStagesCommon.STAGE_REMOVE_FADE_IN_TICKS.get(),
                AStagesCommon.STAGE_REMOVE_STAY_TICKS.get(),
                AStagesCommon.STAGE_REMOVE_FADE_OUT_TICKS.get()
            );
        }
    }

    public static void removeAllStagesHeader(StageAlert header, AHolder holder, AStageType heldType) {
        header.configureChatMessage(Component.translatable("message.astages." + AStageType.getDescriptionIdFor(heldType) + ".remove_all").withStyle(AStagesCommon.STAGE_REMOVE_COLOR.get()));
    }

    public static void removeAllStagesAlert(StageAlert builder, AHolder holder, AStageType heldType, String stageKey, AStatus status, @Nullable BaseStage<?> customConfig) {
        if (customConfig != null) {
            builder.configureChatMessage(customConfig.getMessageOrNull(StageAttributes.CHAT_MESSAGE_REMOVE, stageKey));
        } else {
            var component = Component.translatable("message.astages." + AStageType.getDescriptionIdFor(heldType) + ".list_item", stageKey).withStyle(AStagesCommon.STAGE_REMOVE_COLOR.get());
            builder.configureChatMessage(component);
        }
    }
}