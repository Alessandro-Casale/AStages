package com.alessandro.astages.integration.jei;

import com.alessandro.astages.AStages;
import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.core.ARecipeRestriction;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.event.custom.StageSyncedPlayerEvent;
import com.alessandro.astages.integration.Mods;
// import com.blamejared.crafttweaker.api.recipe.manager.RecipeManagerWrapper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;

@JeiPlugin
public class ARecipeStagesJEIPlugin implements IModPlugin {
    private IJeiRuntime runtime;
    private static final ResourceLocation PLUGIN_ID = new ResourceLocation(AStages.MODID, "item_jei");
    private final List<ItemStack> itemsToHide = new ArrayList<>();

    public ARecipeStagesJEIPlugin() {
        if (!Mods.JEI.isLoaded()) return;

        if (EffectiveSide.get().isClient()) {
            MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, StageSyncedPlayerEvent.class, e -> updateRecipeGui());
            MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, false, RecipesUpdatedEvent.class, e -> updateRecipeGui());
        }
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
    }

    @SuppressWarnings("unchecked")
    public <C extends Container, T extends Recipe<C>> void updateRecipeGui() {
        if (runtime == null) { return; }
        // if (Minecraft.getInstance().level == null) { return; }

//
//        var categories = runtime.getRecipeManager().createRecipeCategoryLookup().get().toList();
//
//        for (var category : categories) {
//            var type = category.getRecipeType();
//
//            if (canCast(type.getRecipeClass(), Recipe.class)) {
//                var recipes = (Stream<Recipe<?>>) runtime.getRecipeManager().createRecipeLookup(type).get();
//                var newList = recipes.filter(recipe -> recipe.getId().equals(new ResourceLocation("minecraft", "charcoal"))).toList();
//                runtime.getRecipeManager().hideRecipes((RecipeType<Recipe<?>>) type, newList);
//            }
//        }

        var categories = runtime.getRecipeManager().createRecipeCategoryLookup().get().toList();
        // var manager = Minecraft.getInstance().level.getRecipeManager();

        // Vanilla RecipeType
//        Map<RecipeType<?>, List<Recipe<?>>> newMap = new HashMap<>();

//        AStages.LOGGER.debug(ARestrictionManager.RECIPE_INSTANCE.restrictionsForType.toString());

        AStages.LOGGER.debug("INSTANTIATE UPDATE!");
        AStages.LOGGER.debug(ARestrictionManager.RECIPE_INSTANCE.getRestrictions().toString());
        for (Map.Entry<String, List<ARecipeRestriction>> entry : ARestrictionManager.RECIPE_INSTANCE.getRestrictions().entrySet()) {
            for (var restriction : entry.getValue()) {
                for (var recipeLocation : restriction.recipes) {
                    for (var category : categories) {
                        AStages.LOGGER.debug("AAAAAA");

                        // var recipes = (Stream<Recipe<?>>) runtime.getRecipeManager().createRecipeLookup(category.getRecipeType()).get();

                        var caster = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager().getAllRecipesFor((net.minecraft.world.item.crafting.RecipeType<T>) restriction.type).get(0);
                        if (!canCast(category.getRecipeType().getRecipeClass(), caster.getClass())) {
                            continue;
                        }

                        var recipes = (Stream<Recipe<?>>) runtime.getRecipeManager().createRecipeLookup(category.getRecipeType()).get();
                        var recipe = (Recipe<?>) recipes.filter(r -> r.getId().equals(recipeLocation)).findFirst().orElse(null);

                        if (recipe == null) {
                            AStages.LOGGER.warn("No recipe found for type {} and id {}", restriction.type, recipeLocation);
                            continue;
                        }

//                        AStages.LOGGER.debug(category.getRecipeType().toString());
//
//                        AStages.LOGGER.debug("Recipe {} hide!", recipeLocation);

                        if (!ClientPlayerStage.getPlayerStages().contains(entry.getKey())) {
                            List<Recipe<?>> newList = new ArrayList<>();
                            newList.add(recipe);

                            runtime.getRecipeManager().hideRecipes(category.getRecipeType(), cast(newList));
                        }
                    }

//                    var newList = newMap.getOrDefault(recipe.getType(), Collections.emptyList());
//                    newList.add(recipe);
//                    newMap.put(recipe.getType(), newList);
                }
            }
        }

//        for (var vanillaType : newMap.keySet()) {
//            for (var category : categories) {
//                var type = category.getRecipeType();
//
//                if (canCast(type.getRecipeClass(), vanillaType.getClass())) {
//                    runtime.getRecipeManager().createRecipeLookup()
//                    runtime.getRecipeManager().hideRecipes(category.getRecipeType(), newMap.get(vanillaType));
//                }
//            }
//        }
    }

//    public static void forEachRecipe() {
//
//    }

//    public RecipeManagerInternal getInternal() {
//        return ((ARecipeManagerAccessor) runtime.getRecipeManager()).getInternal();
//    }
//
//    public List<IRecipeCategory<?>> getCategories() {
//        return ((ARecipeManagerInternalAccessor) getInternal()).getRecipeCategories();
//    }

    public static boolean canCast(Class<?> obj, Class<?> caster){
        try {
            if(obj == caster) return true;

            List<Class<?>> d1 = getParent(obj);
            if(d1.contains(caster)) return true;

            d1 = getParent(caster);
            if(d1.contains(obj)) return true;
        } catch (Exception e){
            AStages.LOGGER.debug(e.getLocalizedMessage());
        }

        return false;
    }

    public static @NotNull List<Class<?>> getParent(@NotNull Class<?> obj){
        List<Class<?>> classes = Collections.synchronizedList(new ArrayList<>());

        for (Class<?> anInterface : obj.getInterfaces()) {
            classes.add(anInterface);
            classes.addAll(getParent(anInterface));
        }

        if (obj.getSuperclass() != null) {
            classes.add(obj.getSuperclass());
            classes.addAll(getParent(obj.getSuperclass()));
        }

        return classes;
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object o) {

        return (T) o;
    }
}
