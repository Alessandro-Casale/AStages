package com.alessandro.astages.engine.server.manager;

import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.exception.UnsupportedMethodException;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.holder.ARestrictionHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.util.AStagesUtils;
import com.alessandro.astages.engine.server.restriction.AScreenRestriction;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import com.alessandro.astages.api.manager.AManager;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@NotNullParams
public class AScreenManager extends AManager<AScreenRestriction, MenuType<?>, AbstractContainerMenu> {
    @Override
    public AScreenRestriction getRestriction(AHolder holder, AbstractContainerMenu object) {
        throw UnsupportedMethodException.useInstead("AScreenManager.getRestriction(AHolder, AbstractContainerMenu, BlockState, BlockEntity)");
    }

    public AScreenRestriction getRestriction(AHolder holder, AbstractContainerMenu menu, @Nullable BlockState state, @Nullable BlockEntity entity) {
        if (holder.isServerActive()) {
            var serverRestriction = getRegistry().stream().filter(r ->
                AStagesUtils.hasStage(holder, AStageType.SERVER, r.getStage()) &&
                    r.isRestricted(menu, state, entity)
            ).findFirst().orElse(null);

            if (serverRestriction == null) { return null; } // If the stage is unlocked in the server, pass!
        }

        if (holder.isPlayerActive()) {
            return getRegistry().stream().filter(r ->
                AStagesUtils.hasStage(holder, AStageType.PLAYER, r.getStage()) &&
                    r.isRestricted(menu, state, entity)
            ).findFirst().orElse(null);
        }

        return null;
    }

    public ARestrictionHolder<AScreenRestriction> getHolder(AHolder holder, AbstractContainerMenu menu, @Nullable BlockState state, @Nullable BlockEntity entity) {
        return ARestrictionHolder.hold(getRestriction(holder, menu, state, entity));
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.SCREEN;
    }
}