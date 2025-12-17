package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.core.server.restriction.ACropRestriction;
import com.alessandro.astages.core.wrapper.CropWrapper;
import com.alessandro.astages.store.ARestrictionType;
import com.alessandro.astages.store.ARestrictionTypes;
import com.alessandro.astages.store.server.AManager;
import net.minecraft.world.level.block.Block;

public class ACropManager extends AManager<ACropRestriction, Block, CropWrapper> {
    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.CROP;
    }
}
