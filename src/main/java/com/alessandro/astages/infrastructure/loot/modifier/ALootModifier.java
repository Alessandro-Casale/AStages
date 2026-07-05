package com.alessandro.astages.infrastructure.loot.modifier;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.engine.loot.ALootProcessor;
import com.alessandro.astages.infrastructure.config.AStagesCommon;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

@NotNullParamsAndMethodsReturn
public class ALootModifier extends LootModifier {
    public static final MapCodec<ALootModifier> CODEC = RecordCodecBuilder.mapCodec(
        instance -> codecStart(instance).apply(instance, ALootModifier::new)
    );

    public ALootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (AStagesCommon.FORCE_LAST_LOOT_MODIFIER.get()) { return generatedLoot; }

        return ALootProcessor.apply(generatedLoot, context);
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}