package com.alessandro.astages.api.util;

import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.engine.AStageManager;
import com.alessandro.astages.engine.store.StageAttributes;
import com.alessandro.astages.infrastructure.config.AStagesCommon;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerPlayer;

public class ATitleUtils {
    public static void showTitles(ServerPlayer player, AOperation operation, String stageKey) {
        Component title = null;
        Component subtitle = null;
        Component chatMessage = null;
        var fadeIn = StageAttributes.FADE_IN.getDefaultValue();
        var fadeOut = StageAttributes.STAY.getDefaultValue();
        var stay = StageAttributes.FADE_OUT.getDefaultValue();

        var stage = AStageManager.GENERIC_INSTANCE.getStage(stageKey); // Has custom config!
        if (stage != null) {
            fadeIn = stage.get(StageAttributes.FADE_IN);
            stay = stage.get(StageAttributes.STAY);
            fadeOut = stage.get(StageAttributes.FADE_OUT);

            switch (operation) {
                case ADD: {
                    title = stage.getMessageOrNull(StageAttributes.TITLE_ADD, stageKey);
                    subtitle = stage.getMessageOrNull(StageAttributes.SUBTITLE_ADD, stageKey);
                    chatMessage = stage.getMessageOrNull(StageAttributes.CHAT_MESSAGE_ADD, stageKey);
                    break;
                }
                case REMOVE: {
                    title = stage.getMessageOrNull(StageAttributes.TITLE_REMOVE, stageKey);
                    subtitle = stage.getMessageOrNull(StageAttributes.SUBTITLE_REMOVE, stageKey);
                    chatMessage = stage.getMessageOrNull(StageAttributes.CHAT_MESSAGE_REMOVE, stageKey);
                    break;
                }
            }
        } else if (AStagesCommon.ENABLE_TITLE_AFTER_STAGE_ADDING.get()) {
            if (operation == AOperation.ADD) {
                title = StageAttributes.TITLE_ADD.getDefaultValue().apply(stageKey);
            }
        }

        APlayerUtils.sendVanillaPacket(player, new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut));

        if (title != null) { APlayerUtils.sendVanillaPacket(player, new ClientboundSetTitleTextPacket(title)); }
        if (subtitle != null) { APlayerUtils.sendVanillaPacket(player, new ClientboundSetSubtitleTextPacket(subtitle)); }
        if (chatMessage != null) { player.sendSystemMessage(chatMessage); }
    }
}