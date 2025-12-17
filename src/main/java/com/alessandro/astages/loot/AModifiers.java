package com.alessandro.astages.loot;

import com.alessandro.astages.AStages;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class AModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> MODIFIERS = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, AStages.MODID);

    // public static final RegistryObject<Codec<ALootModifier>> STAGE_LOOT_MODIFIER_CODEC = MODIFIERS.register("stage_loot_modifier", () -> ALootModifier.CODEC);
}
