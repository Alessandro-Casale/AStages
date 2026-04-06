package com.alessandro.astages.api.wrapper;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

public record RecipeWrapper(RecipeType<?> type, ResourceLocation recipe) { }
