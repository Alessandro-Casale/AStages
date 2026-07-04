package com.alessandro.astages.api.alert;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.util.AComponentUtils;
import com.alessandro.astages.api.util.APlayerUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

@NotNullParamsAndMethodsReturn
public class StageAlert {
    private Component title;
    private Component subtitle;
    private Component chatMessage;
    private Component actionBarMessage;
    private int fadeIn = -1;
    private int stay = -1;
    private int fadeOut = -1;

    private final boolean showTitle;
    private final boolean showSubtitle;
    private final boolean displayChatMessage;
    private final boolean displayActionBarMessage;

    public StageAlert(boolean showTitle, boolean showSubtitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        this.showTitle = showTitle;
        this.showSubtitle = showSubtitle;
        this.displayChatMessage = displayChatMessage;
        this.displayActionBarMessage = displayActionBarMessage;
    }


    public static StageAlert init() {
        return new StageAlert(true, true, true, true);
    }

    public static StageAlert init(boolean showTitle, boolean showSubtitle, boolean displayChatMessage, boolean displayActionBarMessage) {
        return new StageAlert(showTitle, showSubtitle, displayChatMessage, displayActionBarMessage);
    }

    public void alert(ServerPlayer player) {
        if (fadeIn > 0 && stay > 0 && fadeOut > 0) {
            APlayerUtils.sendVanillaPacket(player, new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut));
        }

        APlayerUtils.sendVanillaPacket(player, new ClientboundSetTitleTextPacket(AComponentUtils.nullToEmpty(title)));
        APlayerUtils.sendVanillaPacket(player, new ClientboundSetSubtitleTextPacket(AComponentUtils.nullToEmpty(subtitle)));
        if (chatMessage != null) { player.sendSystemMessage(chatMessage); }
        if (actionBarMessage != null) { player.displayClientMessage(actionBarMessage, true); }
    }

    public void alert(MinecraftServer server) {
        for (var player : server.getPlayerList().getPlayers()) {
            alert(player);
        }
    }

    public void configureTitle(Component title) {
        this.title = showTitle ? title : null;
    }

    public void configureSubtitle(Component subtitle) {
        this.subtitle = showSubtitle ? subtitle : null;
    }

    public void configureChatMessage(Component chatMessage) {
        this.chatMessage = displayChatMessage ? chatMessage : null;
    }

    public void configureActionBarMessage(Component actionBarMessage) {
        this.actionBarMessage = displayActionBarMessage ? actionBarMessage : null;
    }

    public void setupTimings(int fadeIn, int stay, int fadeOut) {
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }
}