package com.alessandro.astages.engine.server.stage;

import com.alessandro.astages.api.feature.ClientSynchronizable;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.stage.AStageBaseManager;
import com.alessandro.astages.api.stage.Stage;
import com.alessandro.astages.engine.AStageManager;
import com.alessandro.astages.engine.store.StageAttributes;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.stages.SyncPermanentStageS2C;
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
        getStages().forEach((stageKey, stage) ->
            Networking.sendTo(player, new SyncPermanentStageS2C(stage.getStage(), stage.get(StageAttributes.ICON)))
        );
    }
}