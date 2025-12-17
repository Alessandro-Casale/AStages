package com.alessandro.astages.integration.jade;

import com.alessandro.astages.api.APlayerUtils;
import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.capability.AProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Contract;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

@NotNullParamsAndMethodsReturn
public enum AStagesBlockComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    private static final String OWNER_KEY = "owner";

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getServerData().contains(OWNER_KEY)) {
            iTooltip.add(
                Component.literal("Owner: " + blockAccessor.getServerData().getString(OWNER_KEY))
            );
        }
    }

    @Contract(pure = true)
    @Override
    public ResourceLocation getUid() {
        return AResourceLocation.fromNamespaceAndPath("block_component_provider");
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        BlockEntity blockEntity = blockAccessor.getBlockEntity();

        var blockStage = blockEntity.getData(AProvider.BLOCK_STAGE);
        if (blockAccessor.getPlayer().getServer() != null) {
            var player = APlayerUtils.getPlayerFromUUID(blockAccessor.getPlayer().getServer(), blockStage.getOwner());

            if (player != null) {
                compoundTag.putString(OWNER_KEY, player.getName().getString());
            }
        }
    }
}
