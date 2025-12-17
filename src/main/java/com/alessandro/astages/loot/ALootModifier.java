package com.alessandro.astages.loot;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;

@NotNullParamsAndMethodsReturn
public class ALootModifier /* extends LootModifier */ {
//    public static final Codec<ALootModifier> CODEC = RecordCodecBuilder.create(
//        instance -> codecStart(instance).apply(instance, ALootModifier::new)
//    );
//
//    public ALootModifier(LootItemCondition[] conditionsIn) {
//        super(conditionsIn);
//    }
//
//    @Override
//    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
//        for (var condition : conditions) {
//            AStages.LOGGER.debug(condition.toString());
//        }
//
//        var entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
//
//        if (entity != null) {
//            AStages.LOGGER.debug(entity.getType().toString());
//        }
//
//        generatedLoot.add(new ItemStack(Items.EMERALD, 10));
//        return generatedLoot;
//    }
//
//    @Override
//    public MapCodec<? extends IGlobalLootModifier> codec() {
//        return CODEC;
//    }
}
