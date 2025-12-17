package com.alessandro.astages.api;

import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.config.AStagesCommon;
import com.alessandro.astages.core.AStageManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerPlayer;

public class ATitleUtils {
    public static void showTitles(ServerPlayer player, AOperation operation, String stageKey) {
        Component title = Component.empty();
        Component subtitle = Component.empty();
        Component chatMessage = null;
        var fadeIn = 0;
        var fadeOut = 0;
        var stay = 0;

        var stage = AStageManager.GENERIC_INSTANCE.getStage(stageKey);
        if (stage != null) {
            fadeIn = stage.getFadeIn();
            stay = stage.getStay();
            fadeOut = stage.getFadeOut();

            if (operation == AOperation.ADD) {
                if (stage.getAddTitle() != null) {
                    title = stage.getAddTitle();
                }

                if (stage.getAddSubTitle() != null) {
                    subtitle = stage.getAddSubTitle();
                }

                if (stage.getAddChatMessage() != null) {
                    chatMessage = stage.getAddChatMessage();
                }
            } else if (operation == AOperation.REMOVE) {
                if (stage.getRemoveTitle() != null) {
                    title = stage.getRemoveTitle();
                }

                if (stage.getRemoveSubTitle() != null) {
                    subtitle = stage.getRemoveSubTitle();
                }

                if (stage.getRemoveChatMessage() != null) {
                    chatMessage = stage.getRemoveChatMessage();
                }
            }
        } else {
            if (AStagesCommon.ENABLE_TITLE_AFTER_STAGE_ADDING.get()) {
                fadeIn = 20;
                stay = 60;
                fadeOut = 20;

                if (operation == AOperation.ADD) {
                    title = Component.translatable("title.astages.add", ATextUtils.stageToDescription(stageKey)).withStyle(AStagesCommon.TITLE_COLOR.get());
                }
            }
        }

        APlayerUtils.sendVanillaPacket(player, new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut));
        APlayerUtils.sendVanillaPacket(player, new ClientboundSetTitleTextPacket(title));
        APlayerUtils.sendVanillaPacket(player, new ClientboundSetSubtitleTextPacket(subtitle));
        if (chatMessage != null) { player.sendSystemMessage(chatMessage); }
    }
}
