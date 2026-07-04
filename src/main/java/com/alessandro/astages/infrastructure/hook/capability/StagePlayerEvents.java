package com.alessandro.astages.infrastructure.hook.capability;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.event.player.StageSyncedPlayerEvent;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.stage.event.ExpiredEvent;
import com.alessandro.astages.api.stage.event.GrantedEvent;
import com.alessandro.astages.api.stage.event.TickEvent;
import com.alessandro.astages.api.util.AFileIOUtils;
import com.alessandro.astages.api.util.APlayerUtils;
import com.alessandro.astages.api.util.AStagesUtils;
import com.alessandro.astages.engine.AStageManager;
import com.alessandro.astages.engine.store.StageAttributes;
import com.alessandro.astages.infrastructure.capability.OfflinePlayerStage;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashMap;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class StagePlayerEvents {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        var file = OfflinePlayerStage.getTemporaryStagesFile(event.getEntity());
        var actualTimerMap = AFileIOUtils.readMapOrDefault(file, String.class, Integer.class);

        actualTimerMap.forEach((stage, actualTimer) -> AStageManager.TEMPORARY_INSTANCE.addAlreadyObtainedStageToExpire(event.getEntity().getUUID(), stage, actualTimer));
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        var file = OfflinePlayerStage.getTemporaryStagesFile(event.getEntity());

        var mapToWrite = new HashMap<String, Integer>();
        var stageContainer = AStageManager.TEMPORARY_INSTANCE.getStageContainersForPlayer(event.getEntity().getUUID());
        if (stageContainer == null) { return; }
        for (var stage : stageContainer) { mapToWrite.put(stage.getStage().getStage(), stage.getCurrentTimer().getCurrentTicks()); }

        AFileIOUtils.writeFileContent(file, mapToWrite);
    }

    @SubscribeEvent
    public static void onClientSync(StageSyncedPlayerEvent event) {
        if (event.getOperation() != AOperation.ADD && event.getOperation() != AOperation.ADD_ALL) { return; }

        var player = event.getPlayer();
        var server = player.getServer();
        var isClientSide = player.level().isClientSide;

        var stages = AStageManager.GENERIC_INSTANCE.getStagesWithCustomGrantedEvent(event.getStagesSynced());
        var temporaryStages = AStageManager.TEMPORARY_INSTANCE.getStages(event.getStagesSynced());

        for (var stage : temporaryStages) {
            AStageManager.TEMPORARY_INSTANCE.addStageToExpire(player.getUUID(), stage.getStage());
        }

        if (!stages.isEmpty()) {
            for (var stage : stages) {
                stage.postGrantedEvent(new GrantedEvent(player, server, isClientSide));
            }
        }
    }

    @Info("For stage expiration calculation! And ticking also!")
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        var player = event.getEntity();
        var stages = AStageManager.TEMPORARY_INSTANCE.getStageContainersForPlayer(player.getUUID());
        if (stages == null) { return; }
        stages.forEach(container -> {
            var stage = container.getStage();

            if (!stage.isValueNull(StageAttributes.TICK_EVENT)) {
                stage.postTickEvent(new TickEvent(player, player.getServer(), false));
            }
        });

        APlayerUtils.runOnceASecond(player, ignoredPlayer -> {
            var listIterator = stages.iterator();
            while (listIterator.hasNext()) {
                var stageContainer = listIterator.next();
                var wasExpired = stageContainer.subtractTicks(20);

                if (wasExpired) {
                    var stage = stageContainer.getStage();
                    if (!stage.isValueNull(StageAttributes.EXPIRED_EVENT)) {
                        stage.postExpiredEvent(new ExpiredEvent(player, player.getServer(), false));
                    }

                    listIterator.remove();
                    AStagesUtils.removeStage(AHolder.player(player), stage.getStage(), true, true, true);
                }
            }
        });
    }
}