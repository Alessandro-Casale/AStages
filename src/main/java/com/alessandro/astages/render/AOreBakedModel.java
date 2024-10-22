package com.alessandro.astages.render;

import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AOreBakedModel implements IDynamicBakedModel {
    public final String stage;
    public final BakedModel original;
    public final BakedModel replacement;

    public AOreBakedModel(String stage, BlockState original, BlockState replacement) {
        this.stage = stage;
        this.original = AStagesUtil.getBakedModelFromState(original);
        this.replacement = AStagesUtil.getBakedModelFromState(replacement);
    }

    private BakedModel getCorrectModel() {
        return ClientPlayerStage.getPlayerStages().contains(stage) ? original : replacement;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, @NotNull RandomSource randomSource, @NotNull ModelData modelData, @Nullable RenderType renderType) {
        return getCorrectModel().getQuads(blockState, direction, randomSource, modelData, renderType);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return getCorrectModel().useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return getCorrectModel().isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return getCorrectModel().usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return getCorrectModel().isCustomRenderer();
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return getCorrectModel().getParticleIcon();
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return getCorrectModel().getOverrides();
    }

    @Override
    public String toString() {
        return "AOreBakedModel{" +
            "stage='" + stage + '\'' +
            ", original=" + original +
            ", replacement=" + replacement +
            '}';
    }
}
