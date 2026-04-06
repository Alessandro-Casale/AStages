package com.alessandro.astages.infrastructure.hook.inventory;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.event.player.StageSyncedPlayerEvent;
import com.alessandro.astages.api.event.world.ContainerChangedEvent;
import com.alessandro.astages.api.event.world.PlayerInventoryChangedEvent;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.infrastructure.hook.CommonEventSettings;
import com.alessandro.astages.infrastructure.listener.AContainerSlotListener;
import com.alessandro.astages.infrastructure.listener.AInventorySlotListener;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class PlayerEventHandler {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        var player = event.getEntity();
        player.inventoryMenu.addSlotListener(new AInventorySlotListener(player));
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        event.getEntity().inventoryMenu.addSlotListener(new AInventorySlotListener(event.getEntity()));
    }

    @Info("For container checking!")
    @SubscribeEvent
    public static void onContainerOpen(PlayerContainerEvent.Open event) {
        event.getContainer().addSlotListener(new AContainerSlotListener(event.getEntity()));

        CommonEventSettings.setPlayerAnotherContainerOpened(event.getEntity(), event.getContainer());
        CommonEventSettings.containerChanged();
    }

    @Info("For inventory checking!")
    @SubscribeEvent
    public static void onContainerClose(PlayerContainerEvent.Close event) {
        CommonEventSettings.setPlayerAnotherContainerOpened(event.getEntity(), null);

        CommonEventSettings.allInventoryChanged();
    }

    @Info("For inventory checking!")
    @SubscribeEvent
    public static void onInventoryChanged(PlayerInventoryChangedEvent event) {
        CommonEventSettings.slotChanged(event.getSlot());
    }

    @Info("For whole inventory checking!")
    @SubscribeEvent
    public static void onStageSynced(StageSyncedPlayerEvent event) {
        CommonEventSettings.allInventoryChanged();
    }

    @Info("For container checking!")
    @SubscribeEvent
    public static void onPlayerContainerChanged(ContainerChangedEvent event) {
        CommonEventSettings.containerChanged();
    }
}
