package com.alessandro.astages.mixin;

import com.alessandro.astages.api.develop.UnderDevelopment;
import dev.latvian.mods.kubejs.item.ItemClickedKubeEvent;
import org.spongepowered.asm.mixin.Mixin;

@UnderDevelopment
@Mixin(value = ItemClickedKubeEvent.class, remap = false)
public abstract class APlayerEventJS {
//    @Shadow public abstract @Nullable Player getPlayer();

    /**
     * @author Alessandro
     * @reason support new stage system
     */
//    @Info("Checks if the player has the specified game stage")
//    @Overwrite
//    public boolean hasGameStage(String stage) {
//        AtomicBoolean toReturn = new AtomicBoolean(false);
//
//        Objects.requireNonNull(getPlayer()).getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> toReturn.set(playerStage.getStages().contains(stage)));
//
//        return toReturn.get();
//    }

    /**
     * @author Alessandro
     * @reason support new stage system
     */
//    @Info("Adds the specified game stage to the player")
//    @Overwrite
//    public void addGameStage(String stage) {
//        Objects.requireNonNull(getPlayer()).getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> {
//            playerStage.addStage(stage);
//            playerStage.setChangedFor(getPlayer(), PlayerStage.Operation.ADD, stage);
//        });
//    }

    /**
     * @author Alessandro
     * @reason support new stage system
     */
//    @Info("Removes the specified game stage from the player")
//    @Overwrite
//    public void removeGameStage(String stage) {
//        Objects.requireNonNull(getPlayer()).getCapability(PlayerStageProvider.PLAYER_STAGE).ifPresent(playerStage -> {
//            playerStage.removeStage(stage);
//            playerStage.setChangedFor(getPlayer(), PlayerStage.Operation.REMOVE, stage);
//        });
//    }
}
