package com.alessandro.astages.mixin.ore;

import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.config.AStagesClient;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelChunkSection.class)
public class ALevelChunkSection {
    @ModifyReturnValue(method = "getBlockState", at = @At("RETURN"))
    public BlockState astages$getBlockState(BlockState original) {
        if (AStagesClient.LEVEL_CHUNK_SECTION_EXPERIMENTAL_SETTINGS.get() && Thread.currentThread().getThreadGroup() == SidedThreadGroups.CLIENT) {
            return AClientRestrictionManager.ORE_INSTANCE.getReplacement(AClientHolder.serverAndPlayer(), original);
        }

        return original;
    }
}