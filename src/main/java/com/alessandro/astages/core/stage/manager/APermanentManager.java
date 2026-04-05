package com.alessandro.astages.core.stage.manager;

import com.alessandro.astages.api.feature.ClientSynchronizable;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.stage.Stage;
import com.alessandro.astages.core.AStageManager;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.packet.stages.StageSyncerS2CPacket;
import com.alessandro.astages.store.StageAttributes;
import com.alessandro.astages.store.stage.AStageBaseManager;
import net.minecraft.server.level.ServerPlayer;

public class APermanentManager extends AStageBaseManager<Stage> implements ClientSynchronizable {
    public void addStage(Stage stage) {
        if (AStageManager.GENERIC_INSTANCE.checkForDuplicates(stage)) {
            AStageManager.GENERIC_INSTANCE.addStageNoCheck(stage);
            addStageInternal(stage.getStage(), stage);
        }
    }

    @Override
    public void synchronizeWithClient(@Nullable ServerPlayer player) {
        getStages().forEach((stageKey, stage) -> {
            ANetworking.sendTo(player, new StageSyncerS2CPacket(stage.getStage(), stage.get(StageAttributes.ICON)));
        });
    }
}