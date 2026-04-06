package com.alessandro.astages.infrastructure.mixin.recipe.minecraft;

import com.alessandro.astages.api.develop.UnderDevelopment;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.api.wrapper.RecipeWrapper;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@UnderDevelopment
@Mixin(StonecutterScreen.class)
public class AStonecutterScreen {
    @Redirect(method = "renderRecipes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/StonecutterMenu;getRecipes()Ljava/util/List;"))
    public List<RecipeHolder<StonecutterRecipe>> astages$renderRecipes(@NotNull StonecutterMenu instance) {
        var defaultRecipes = instance.getRecipes();
        var iterator = defaultRecipes.listIterator();

        while (iterator.hasNext()) {
            var recipe = iterator.next();
            var restriction = AClientRestrictionManager.RECIPE_INSTANCE.getRestriction(AClientHolder.serverAndPlayer(), new RecipeWrapper(recipe.value().getType(), recipe.id()));

            if (restriction != null) {
                iterator.remove();
            }
        }

        return defaultRecipes;
    }
}