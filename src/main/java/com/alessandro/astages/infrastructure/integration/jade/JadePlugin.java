package com.alessandro.astages.infrastructure.integration.jade;

import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.infrastructure.integration.jade.component.AStagesBlockComponentProvider;
import com.alessandro.astages.infrastructure.integration.jade.handler.JadeItemHandler;
import com.alessandro.astages.infrastructure.integration.jade.handler.JadeMobHandler;
import com.alessandro.astages.infrastructure.integration.jade.handler.JadeOreHandler;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@NotNullParams
@WailaPlugin
public class JadePlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(AStagesBlockComponentProvider.INSTANCE, BlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(AStagesBlockComponentProvider.INSTANCE, Block.class);

        JadeOreHandler.registerClient(registration);
        JadeMobHandler.registerClient(registration);
        JadeItemHandler.registerClient(registration);
    }
}