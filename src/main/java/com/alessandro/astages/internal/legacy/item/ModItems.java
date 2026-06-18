package com.alessandro.astages.internal.legacy.item;

import com.alessandro.astages.AStages;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

@Deprecated(forRemoval=true)
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, AStages.MODID);
}
