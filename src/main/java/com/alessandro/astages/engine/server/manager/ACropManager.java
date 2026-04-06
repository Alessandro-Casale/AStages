package com.alessandro.astages.engine.server.manager;

import com.alessandro.astages.engine.server.restriction.ACropRestriction;
import com.alessandro.astages.api.wrapper.CropWrapper;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import com.alessandro.astages.api.manager.AManager;
import net.minecraft.world.level.block.Block;

public class ACropManager extends AManager<ACropRestriction, Block, CropWrapper> {
    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.CROP;
    }
}