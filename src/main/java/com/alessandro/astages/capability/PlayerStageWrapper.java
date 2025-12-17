package com.alessandro.astages.capability;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.world.entity.player.Player;

import java.util.List;

@NotNullParamsAndMethodsReturn
@SuppressWarnings("removal")
public class PlayerStageWrapper {
    public static PlayerStage getPlayerData(Player player) {
        return player.getData(AProvider.PLAYER_STAGE.get());
    }

    public static List<String> getStages(Player player) {
        return player.getData(AProvider.PLAYER_STAGE.get()).getStages();
    }
}
