package com.alessandro.astages.loot;

import com.alessandro.astages.AStages;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class DataProvider extends GlobalLootModifierProvider {
    public DataProvider(PackOutput output) {
        super(output, AStages.MODID);
    }

    @Override
    protected void start() {
//        add("adrop_modifier", new ADropModifier());
    }
}
