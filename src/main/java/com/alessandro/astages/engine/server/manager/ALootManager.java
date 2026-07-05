package com.alessandro.astages.engine.server.manager;

import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.exception.UnsupportedMethodException;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.holder.ARestrictionHolder;
import com.alessandro.astages.api.loot.ALootPayload;
import com.alessandro.astages.api.manager.AManager;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.api.util.AStagesUtils;
import com.alessandro.astages.engine.server.restriction.ALootRestriction;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

@NotNullParams
public class ALootManager extends AManager<ALootRestriction, Void, ItemStack> {
    @Override
    public ALootRestriction getRestriction(AHolder holder, ItemStack object) {
        throw UnsupportedMethodException.useInstead("ALootManager.getRestriction(AHolder, ItemStack, EntityType<?>, ALootPayload)");
    }

    public ALootRestriction getRestriction(AHolder holder, ItemStack stack, ALootPayload payload) {
        if (holder.isServerActive()) {
            var serverRestriction = getRegistry().stream().filter(r ->
                !AStagesUtils.hasStage(holder, AStageType.SERVER, r.getStage()) &&
                    r.isRestricted(stack, payload)
            ).findFirst().orElse(null);

            if (serverRestriction == null) { return null; } // If the stage is unlocked in the server, pass!
        }

        if (holder.isPlayerActive()) {
            return getRegistry().stream().filter(r ->
                !AStagesUtils.hasStage(holder, AStageType.PLAYER, r.getStage()) &&
                    r.isRestricted(stack, payload)
            ).findFirst().orElse(null);
        }

        return null;
    }

    public ARestrictionHolder<ALootRestriction> getHolder(AHolder holder, ItemStack stack, ALootPayload payload) {
        return ARestrictionHolder.hold(getRestriction(holder, stack, payload));
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.LOOT;
    }
}
