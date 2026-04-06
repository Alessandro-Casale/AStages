package com.alessandro.astages.engine.server.manager;

import com.alessandro.astages.api.manager.AManager;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.engine.server.restriction.ARegionRestriction;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import net.minecraft.core.BlockPos;

@NotNullParams
public class ARegionManager extends AManager<ARegionRestriction, Void, BlockPos> {
    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.REGION;
    }
}
