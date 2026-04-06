package com.alessandro.astages.infrastructure.integration.jei;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.event.sync.ClientSynchronizeServerStagesEvent;
import com.alessandro.astages.api.event.sync.ClientSynchronizeStagesEvent;
import com.alessandro.astages.api.event.update.ClientRecipeUpdateEvent;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.util.AStagesClientUtils;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.infrastructure.integration.Mods;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.util.thread.EffectiveSide;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

@NotNullParamsAndMethodsReturn
@JeiPlugin
public class JeiRecipeStagesPlugin implements IModPlugin {
    private IJeiRuntime runtime;
    private static final ResourceLocation PLUGIN_ID = AResourceLocation.fromNamespaceAndPath("recipe_jei");

    public JeiRecipeStagesPlugin() {
        if (!Mods.JEI.isLoaded()) return;

        if (EffectiveSide.get().isClient()) {
            ALoader.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientRecipeUpdateEvent.class,
                e -> updateRecipeGui(null, null)
            );

            ALoader.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientSynchronizeStagesEvent.class, e -> {
                if (e.getOperation() != AOperation.LOGIN) {
                    updateRecipeGui(e.getOperation(), e.getStagesSynced());
                }
            });

            ALoader.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientSynchronizeServerStagesEvent.class, e -> {
                if (e.getOperation() != AOperation.LOGIN) {
                    updateRecipeGui(e.getOperation(), e.getStagesSynced());
                }
            });
        }
    }

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
    }

    public void updateRecipeGui(@Nullable AOperation operation, @Nullable Set<String> syncedStages) {
        if (runtime != null && AClientRestrictionManager.ableToUpdateJeiUI()) {
            AStages.LOGGER.info("AStages client recipe update started!");
            var time = System.currentTimeMillis();

            restrictAllRecipesForMods();

            updateRecipesForType(RecipeType.CRAFTING, RecipeTypes.CRAFTING);
            updateRecipesForType(RecipeType.SMELTING, RecipeTypes.SMELTING);
            updateRecipesForType(RecipeType.SMOKING, RecipeTypes.SMOKING);
            updateRecipesForType(RecipeType.CAMPFIRE_COOKING, RecipeTypes.CAMPFIRE_COOKING);
            updateRecipesForType(RecipeType.BLASTING, RecipeTypes.BLASTING);
            updateRecipesForType(RecipeType.SMITHING, RecipeTypes.SMITHING);
            updateRecipesForType(RecipeType.STONECUTTING, RecipeTypes.STONECUTTING);

            AStages.LOGGER.info("AStages recipe update completed in {} ms!", System.currentTimeMillis() - time);
        }
    }

    private <I extends RecipeInput, T extends Recipe<I>> void updateRecipesForType(RecipeType<T> vanillaType, mezz.jei.api.recipe.RecipeType<RecipeHolder<T>> jeiType) {
        if (runtime == null) { return; }

        var map = AClientRestrictionManager.RECIPE_INSTANCE.getAllRecipesForType(vanillaType);
        List<RecipeHolder<T>> recipeList;
        Supplier<Stream<RecipeHolder<T>>> lookup = () -> runtime.getRecipeManager().createRecipeLookup(jeiType).includeHidden().get();

        for (var stage : map.keySet()) {
            recipeList = lookup.get().filter(c -> map.get(stage).contains(c.id())).toList();

            if (AStagesClientUtils.hasStage(AClientHolder.serverAndPlayer(), stage)) {
                runtime.getRecipeManager().unhideRecipes(jeiType, recipeList);
            } else {
                runtime.getRecipeManager().hideRecipes(jeiType, recipeList);
            }
        }
    }

    private void restrictAllRecipesForMods() {
        for (var mod : AClientRestrictionManager.RECIPE_INSTANCE.getRegistry().getModRestrictions()) {
            var ignored = mod.getIgnoredRecipeIds();
            restrictAllRecipesForModAndType(RecipeTypes.CRAFTING, mod.getModId(), mod.getStage(), ignored);
            restrictAllRecipesForModAndType(RecipeTypes.SMELTING, mod.getModId(), mod.getStage(), ignored);
            restrictAllRecipesForModAndType(RecipeTypes.SMOKING, mod.getModId(), mod.getStage(), ignored);
            restrictAllRecipesForModAndType(RecipeTypes.CAMPFIRE_COOKING, mod.getModId(), mod.getStage(), ignored);
            restrictAllRecipesForModAndType(RecipeTypes.BLASTING, mod.getModId(), mod.getStage(), ignored);
            restrictAllRecipesForModAndType(RecipeTypes.SMITHING, mod.getModId(), mod.getStage(), ignored);
            restrictAllRecipesForModAndType(RecipeTypes.STONECUTTING, mod.getModId(), mod.getStage(), ignored);
        }
    }

    private <I extends RecipeInput, T extends Recipe<I>> void restrictAllRecipesForModAndType(mezz.jei.api.recipe.RecipeType<RecipeHolder<T>> type, String modId, String stage, List<ResourceLocation> ignoredRecipeIds) {
        var newList = runtime.getRecipeManager().createRecipeLookup(type).includeHidden().get()
            .filter(r -> r.id().getNamespace().equals(modId) && !ignoredRecipeIds.contains(r.id()))
            .toList();

        if (AStagesClientUtils.hasStage(AClientHolder.serverAndPlayer(), stage)) {
            runtime.getRecipeManager().unhideRecipes(type, newList);
        } else {
            runtime.getRecipeManager().hideRecipes(type, newList);
        }
    }
}