package com.alessandro.astages.engine.server.manager;

import com.alessandro.astages.api.exception.UnsupportedMethodException;
import com.alessandro.astages.api.holder.ARestrictionHolder;
import com.alessandro.astages.api.manager.AManager;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.stage.AStageContext;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.api.util.AEvaluatorUtils;
import com.alessandro.astages.engine.server.restriction.AScreenRestriction;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@NotNullParams
public class AScreenManager extends AManager<AScreenRestriction, MenuType<?>, AbstractContainerMenu> {
    @Override
    public AScreenRestriction getRestriction(AStageContext context, AbstractContainerMenu object) {
        throw UnsupportedMethodException.useInstead("AScreenManager.getRestriction(AStageContext, AbstractContainerMenu, BlockState, BlockEntity)");
    }

    public AScreenRestriction getRestriction(AStageContext context, AbstractContainerMenu menu, @Nullable BlockState state, @Nullable BlockEntity entity) {
        return AEvaluatorUtils.evaluateRestrictionUsing(getRegistry().getRestrictions(), AEvaluatorUtils.calculateEvaluationMode(getRegistry()), context, r -> r.isRestricted(menu, state, entity));
    }

    public ARestrictionHolder<AScreenRestriction> getHolder(AStageContext context, AbstractContainerMenu menu, @Nullable BlockState state, @Nullable BlockEntity entity) {
        return ARestrictionHolder.hold(getRestriction(context, menu, state, entity));
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.SCREEN;
    }
}