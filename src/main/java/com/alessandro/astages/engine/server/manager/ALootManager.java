package com.alessandro.astages.engine.server.manager;

import com.alessandro.astages.api.exception.UnsupportedMethodException;
import com.alessandro.astages.api.holder.ARestrictionHolder;
import com.alessandro.astages.api.loot.ALootPayload;
import com.alessandro.astages.api.manager.AManager;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.stage.AStageContext;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.api.util.AEvaluatorUtils;
import com.alessandro.astages.engine.server.restriction.ALootRestriction;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import net.minecraft.world.item.ItemStack;

@NotNullParams
public class ALootManager extends AManager<ALootRestriction, Void, ItemStack> {
    @Override
    public ALootRestriction getRestriction(AStageContext context, ItemStack object) {
        throw UnsupportedMethodException.useInstead("ALootManager.getRestriction(AStageContext, ItemStack, EntityType<?>, ALootPayload)");
    }

    public ALootRestriction getRestriction(AStageContext context, ItemStack stack, ALootPayload payload) {
        return AEvaluatorUtils.evaluateRestrictionUsing(getRegistry().getRestrictions(), AEvaluatorUtils.calculateEvaluationMode(getRegistry()), context, r -> r.isRestricted(stack, payload));
    }

    public ARestrictionHolder<ALootRestriction> getHolder(AStageContext context, ItemStack stack, ALootPayload payload) {
        return ARestrictionHolder.hold(getRestriction(context, stack, payload));
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.LOOT;
    }
}
