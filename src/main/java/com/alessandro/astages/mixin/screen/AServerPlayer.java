package com.alessandro.astages.mixin.screen;

import com.alessandro.astages.core.ARestrictionManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.OptionalInt;

@Mixin(value = ServerPlayer.class)
public class AServerPlayer {
    @Shadow public int containerCounter;

    @Shadow public ServerGamePacketListenerImpl connection;

    @Unique
    private ServerPlayer serverPlayer$self() {
        return (ServerPlayer) (Object) this;
    }

    /**
     * @author Alessandro
     * @reason Add possibility to lock menus under stages
     */
    @Overwrite
    public OptionalInt openMenu(@Nullable MenuProvider pMenu) {
        if (pMenu == null) {
            return OptionalInt.empty();
        } else {
            if (serverPlayer$self().containerMenu != serverPlayer$self().inventoryMenu) {
                serverPlayer$self().closeContainer();
            }

            serverPlayer$self().nextContainerCounter();
            AbstractContainerMenu abstractcontainermenu = pMenu.createMenu(containerCounter, serverPlayer$self().getInventory(), serverPlayer$self());
            if (abstractcontainermenu == null) {
                if (serverPlayer$self().isSpectator()) {
                    serverPlayer$self().displayClientMessage(Component.translatable("container.spectatorCantOpen").withStyle(ChatFormatting.RED), true);
                }

                return OptionalInt.empty();
            } else {
                // New part
                var restriction = ARestrictionManager.SCREEN_INSTANCE.getRestriction(serverPlayer$self(), abstractcontainermenu.getType());

                if (restriction == null) {
                    connection.send(new ClientboundOpenScreenPacket(abstractcontainermenu.containerId, abstractcontainermenu.getType(), pMenu.getDisplayName()));
                    serverPlayer$self().initMenu(abstractcontainermenu);
                    serverPlayer$self().containerMenu = abstractcontainermenu;
                    MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(serverPlayer$self(), serverPlayer$self().containerMenu));
                }

                // Old part
                return OptionalInt.of(this.containerCounter);
            }
        }
    }
}
