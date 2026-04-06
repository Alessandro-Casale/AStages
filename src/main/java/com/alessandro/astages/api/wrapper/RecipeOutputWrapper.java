package com.alessandro.astages.api.wrapper;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeType;

public record RecipeOutputWrapper(RecipeType<?> type, Item output) { }
