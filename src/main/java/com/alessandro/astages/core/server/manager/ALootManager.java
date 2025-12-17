package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.api.AStagesUtils;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.holder.ARestrictionHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.core.server.restriction.ALootRestriction;
import com.alessandro.astages.store.ARestrictionType;
import com.alessandro.astages.store.ARestrictionTypes;
import com.alessandro.astages.store.server.AManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

@NotNullParams
public class ALootManager extends AManager<ALootRestriction, Void, ItemStack> {
    public ALootRestriction getRestriction(AHolder holder, ItemStack stack, @Nullable EntityType<?> entityType, @Nullable ResourceLocation lootTable) {
        if (holder.isServerActive()) {
            var serverRestriction = getRestrictions().stream().filter(r ->
                AStagesUtils.hasStage(holder, AStageType.SERVER, r.getStage()) &&
                    r.isRestricted(stack, entityType, lootTable)
            ).findFirst().orElse(null);

            if (serverRestriction == null) { return null; } // If the stage is unlocked in the server, pass!
        }

        if (holder.isPlayerActive()) {
            return getRestrictions().stream().filter(r ->
                AStagesUtils.hasStage(holder, AStageType.PLAYER, r.getStage()) &&
                    r.isRestricted(stack, entityType, lootTable)
            ).findFirst().orElse(null);
        }

        return null;
    }

    public ARestrictionHolder<ALootRestriction> getHolder(AHolder holder, ItemStack stack, @Nullable EntityType<?> entityType, @Nullable ResourceLocation lootTable) {
        return ARestrictionHolder.hold(getRestriction(holder, stack, entityType, lootTable));
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.LOOT;
    }
}
