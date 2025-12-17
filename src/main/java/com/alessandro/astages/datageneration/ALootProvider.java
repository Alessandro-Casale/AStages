package com.alessandro.astages.datageneration;

import com.alessandro.astages.AStages;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;

import java.util.concurrent.CompletableFuture;

public class ALootProvider extends GlobalLootModifierProvider {
    public ALootProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, AStages.MODID);
    }

    @Override
    protected void start() {
        // Applied to all loot tables! (Blocks, entities and so on!)
        // add("stage_loot_modifier_instance", new ALootModifier(new LootItemCondition[]{ }));
    }
}
