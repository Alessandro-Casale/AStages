package com.alessandro.astages.infrastructure.loot.modifier;

import com.alessandro.astages.AStages;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> MODIFIERS = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, AStages.MODID);

    public static final Supplier<MapCodec<ALootModifier>> STAGE_LOOT_MODIFIER_CODEC = MODIFIERS.register("astages_loot_modifier", () -> ALootModifier.CODEC);
}
