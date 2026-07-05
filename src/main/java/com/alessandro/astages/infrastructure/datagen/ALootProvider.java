package com.alessandro.astages.infrastructure.datagen;

import com.alessandro.astages.AStages;
import com.alessandro.astages.infrastructure.loot.modifier.ALootModifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;

import java.util.concurrent.CompletableFuture;

public class ALootProvider extends GlobalLootModifierProvider {
    public ALootProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, AStages.MODID);
    }

    @Override
    protected void start() {
        // Applied to all loot tables! (Blocks, entities and so on!)
        add("astages_loot_modifier_instance", new ALootModifier(new LootItemCondition[]{ }));
    }
}
