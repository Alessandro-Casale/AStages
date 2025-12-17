package com.alessandro.astages.event;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.AStagesUtils;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.constant.ASyncOperation;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.event.player.StageSyncedPlayerEvent;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.AStageManager;
import com.alessandro.astages.event.custom.ContainerChangedEvent;
import com.alessandro.astages.event.custom.PlayerInventoryChangedEvent;
import net.minecraft.server.level.ServerPlayer;
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

        if (!player.level().isClientSide && player instanceof ServerPlayer serverPlayer) {
            var playerStages = AStagesUtils.getStages(AHolder.player(player));
            AStagesUtils.synchronizeWithClient(AHolder.player(player), serverPlayer, AOperation.LOGIN, playerStages, true);

            ARestrictionManager.clearClientOnLogin(serverPlayer);
            AStageManager.clientSynchronization(serverPlayer);
            ARestrictionManager.reflectServerStagesChangesToClients(serverPlayer);
            ARestrictionManager.reflectSimpleIdsChangesToClients(serverPlayer, ARestrictionManager.SIMPLE_IDS, ASyncOperation.ADD);
            ARestrictionManager.reflectAllStagesChangesToClients(serverPlayer, ARestrictionManager.ALL_STAGES, ASyncOperation.ADD);
            ARestrictionManager.clientSynchronization(serverPlayer);
        }
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
