package com.alessandro.astages.infrastructure.loot;

import com.alessandro.astages.api.AResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

public class ALootParams {
    public static final LootContextParam<LootContextParamSet> PARAM_SET = new LootContextParam<>(AResourceLocation.fromNamespaceAndPath("param_set"));
    public static final LootContextParam<ResourceLocation> PARAM_SET_ID = new LootContextParam<>(AResourceLocation.fromNamespaceAndPath("param_set_id"));
}