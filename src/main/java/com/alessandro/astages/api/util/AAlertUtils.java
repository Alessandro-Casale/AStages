package com.alessandro.astages.api.util;

import com.alessandro.astages.api.alert.Alert;
import com.alessandro.astages.api.alert.Header;
import com.alessandro.astages.api.alert.StageAlert;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.constant.AStatus;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.stage.BaseStage;
import com.alessandro.astages.engine.AStageManager;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Set;

@NotNullParams
public class AAlertUtils {
    public static void genericHeader(Header<StageAlert, AHolder, AStageType> header, AHolder holder, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        if (!holder.holdOnlyOneType()) { return; }

        var heldType = holder.getHeldType();
        var builder = StageAlert.init(showTitle, showTitle, displayChatMessage, displayActionBarMessage);

        header.header(builder, holder, heldType);

        switch (heldType) {
            case PLAYER -> {
                var player = APlayerUtils.getPlayerFromUUID(holder.getPlayer());
                builder.alert(player);
            }
            case SERVER ->
                builder.alert(ServerLifecycleHooks.getCurrentServer());
        }
    }

    public static void genericAlert(Alert<StageAlert, AHolder, AStageType, String, AStatus, BaseStage<?>> alert, AHolder holder, String stageKey, AStatus status, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        if (!holder.holdOnlyOneType()) { return; }

        var heldType = holder.getHeldType();
        var builder = StageAlert.init(showTitle, showTitle, displayChatMessage, displayActionBarMessage);
        var customConfig = AStageManager.GENERIC_INSTANCE.getStage(stageKey);

        alert.alert(builder, holder, heldType, stageKey, status, customConfig);

        switch (heldType) {
            case PLAYER -> {
                var player = APlayerUtils.getPlayerFromUUID(holder.getPlayer());
                builder.alert(player);
            }
            case SERVER ->
                builder.alert(ServerLifecycleHooks.getCurrentServer());
        }
    }

    public static void genericAlerts(Alert<StageAlert, AHolder, AStageType, String, AStatus, BaseStage<?>> alert, AHolder holder, Set<String> stageKeys, AStatus status, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        if (!holder.holdOnlyOneType()) { return; }

        var heldType = holder.getHeldType();
        for (String stageKey : stageKeys) {
            var builder = StageAlert.init(showTitle, showTitle, displayChatMessage, displayActionBarMessage);
            var customConfig = AStageManager.GENERIC_INSTANCE.getStage(stageKey);

            alert.alert(builder, holder, heldType, stageKey, status, customConfig);

            switch (heldType) {
                case PLAYER -> {
                    var player = APlayerUtils.getPlayerFromUUID(holder.getPlayer());
                    builder.alert(player);
                }
                case SERVER ->
                    builder.alert(ServerLifecycleHooks.getCurrentServer());
            }
        }
    }

    public static void genericAlertWithHeader(Header<StageAlert, AHolder, AStageType> header, Alert<StageAlert, AHolder, AStageType, String, AStatus, BaseStage<?>> alert, AHolder holder, String stageKey, AStatus status, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        genericHeader(header, holder, showTitle, displayChatMessage, displayActionBarMessage);
        genericAlert(alert, holder, stageKey, status, showTitle, displayChatMessage, displayActionBarMessage);
    }

    public static void genericAlertsWithHeader(Header<StageAlert, AHolder, AStageType> header, Alert<StageAlert, AHolder, AStageType, String, AStatus, BaseStage<?>> alert, AHolder holder, Set<String> stageKeys, AStatus status, boolean showTitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        genericHeader(header, holder, showTitle, displayChatMessage, displayActionBarMessage);
        genericAlerts(alert, holder, stageKeys, status, showTitle, displayChatMessage, displayActionBarMessage);
    }
}