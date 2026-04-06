package com.alessandro.astages.internal.legacy;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;

@SuppressWarnings("removal")
@Deprecated(forRemoval = true)
@NotNullParamsAndMethodsReturn
public class AOreBakedModel /* implements IDynamicBakedModel*/ {
//    public final String stage;
//    public final BakedModel original;
//    public final BakedModel replacement;
//
//    public AOreBakedModel(String stage, BlockState original, BlockState replacement) {
//        this.stage = stage;
//        this.original = ABlockStateUtils.getBakedModelFromState(original);
//        this.replacement = ABlockStateUtils.getBakedModelFromState(replacement);
//    }
//
//    private BakedModel getCorrectModel() {
//        return ClientPlayerStage.hasStage(stage) ? original : replacement;
//    }
//
//    @Override
//    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, RandomSource randomSource, ModelData modelData, @Nullable RenderType renderType) {
//        return getCorrectModel().getQuads(blockState, direction, randomSource, modelData, renderType);
//    }
//
//    @Override
//    public boolean useAmbientOcclusion() {
//        return getCorrectModel().useAmbientOcclusion();
//    }
//
//    @Override
//    public boolean isGui3d() {
//        return getCorrectModel().isGui3d();
//    }
//
//    @Override
//    public boolean usesBlockLight() {
//        return getCorrectModel().usesBlockLight();
//    }
//
//    @Override
//    public boolean isCustomRenderer() {
//        return getCorrectModel().isCustomRenderer();
//    }
//
//    @Override
//    @SuppressWarnings("deprecation")
//    public TextureAtlasSprite getParticleIcon() {
//        return getCorrectModel().getParticleIcon();
//    }
//
//    @Override
//    public ItemOverrides getOverrides() {
//        return getCorrectModel().getOverrides();
//    }
//
//    @Override
//    public String toString() {
//        return "AOreBakedModel{" +
//            "stage='" + stage + '\'' +
//            ", original=" + original +
//            ", replacement=" + replacement +
//            '}';
//    }
}
