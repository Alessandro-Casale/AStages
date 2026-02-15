package com.alessandro.astages.event.stage;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.AFileIOUtils;
import com.alessandro.astages.api.AServerUtils;
import com.alessandro.astages.api.AStagesUtils;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.event.server.StageSyncedServerEvent;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.stage.event.ExpiredEvent;
import com.alessandro.astages.api.stage.event.GrantedEvent;
import com.alessandro.astages.api.stage.event.TickEvent;
import com.alessandro.astages.capability.ServerStage;
import com.alessandro.astages.core.AStageManager;
import com.alessandro.astages.store.StageAttributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashMap;

@NotNullParams
@EventBusSubscriber(modid = AStages.MODID)
public class ServerEventHandler {
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        var file = ServerStage.getTemporaryStagesFile();
        var actualTimerMap = AFileIOUtils.readMapOrDefault(file, String.class, Integer.class);

        actualTimerMap.forEach(AStageManager.TEMPORARY_INSTANCE::addAlreadyObtainedServerStageToExpire);
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        var file = ServerStage.getTemporaryStagesFile();

        var mapToWrite = new HashMap<String, Integer>();
        var stageContainer = AStageManager.TEMPORARY_INSTANCE.getStageContainersForServer();
        if (stageContainer == null) { return; }
        for (var stage : stageContainer) { mapToWrite.put(stage.getStage().getStage(), stage.getCurrentTimer().getCurrentTicks()); }

        AFileIOUtils.writeFileContent(file, mapToWrite);
    }

    @SubscribeEvent
    public static void onClientSync(StageSyncedServerEvent event) {
        if (event.getOperation() != AOperation.ADD && event.getOperation() != AOperation.ADD_ALL) { return; }

        var server = event.getServer();
        var isClientSide = false;

        var stages = AStageManager.GENERIC_INSTANCE.getStagesWithCustomGrantedEvent(event.getStagesSynced());
        var temporaryStages = AStageManager.TEMPORARY_INSTANCE.getStages(event.getStagesSynced());

        for (var stage : temporaryStages) {
            AStageManager.TEMPORARY_INSTANCE.addServerStageToExpire(stage.getStage());
        }

        if (!stages.isEmpty()) {
            for (var stage : stages) {
                stage.postGrantedEvent(new GrantedEvent(null, server, isClientSide));
            }
        }
    }

    @Info("For stage expiration calculation! And ticking also!")
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Pre event) {
        var server = event.getServer();
        var stages = AStageManager.TEMPORARY_INSTANCE.getStageContainersForServer();
        if (stages == null) { return; }
        stages.forEach(container -> {
            var stage = container.getStage();

            if (!stage.isValueNull(StageAttributes.TICK_EVENT)) {
                stage.postTickEvent(new TickEvent(null, server, false));
            }
        });

        AServerUtils.runOnceASecond(event.getServer(), ignoredServer -> {
            var listIterator = stages.iterator();
            while (listIterator.hasNext()) {
                var stageContainer = listIterator.next();
                var wasExpired = stageContainer.subtractTicks(20);

                if (wasExpired) {
                    var stage = stageContainer.getStage();
                    if (!stage.isValueNull(StageAttributes.EXPIRED_EVENT)) {
                        stage.postExpiredEvent(new ExpiredEvent(null, server, false));
                    }

                    listIterator.remove();
                    AStagesUtils.removeStage(AHolder.server(), stage.getStage(), true);
                }
            }
        });
    }
}