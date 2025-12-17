package com.alessandro.astages.integration.jei;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.event.custom.ClientSynchronizeServerStagesEvent;
import com.alessandro.astages.event.custom.ClientSynchronizeStagesEvent;
import com.alessandro.astages.event.custom.actions.ClientItemUpdateEvent;
import com.alessandro.astages.integration.Mods;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.util.thread.EffectiveSide;
import net.neoforged.neoforge.common.NeoForge;

import java.util.*;

@NotNullParamsAndMethodsReturn
@JeiPlugin
public class AItemStagesJEIPlugin implements IModPlugin {
    private static final HashMap<String, List<ItemStack>> ITEM_CACHE = new HashMap<>();
    private static final HashMap<String, HashMap<IIngredientType<?>, List<Object>>> GENERIC_CACHE = new HashMap<>();


    private IJeiRuntime runtime;
    private static final ResourceLocation PLUGIN_ID = AResourceLocation.fromNamespaceAndPath("item_jei");

    public AItemStagesJEIPlugin() {
        if (!Mods.JEI.isLoaded()) return;

        if (EffectiveSide.get().isClient() && !EffectiveSide.get().isServer()) {
            NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientItemUpdateEvent.class, e -> updateGui(null, null));

            NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientSynchronizeStagesEvent.class, e -> {
                if (e.getOperation() != AOperation.LOGIN) {
                    updateGui(e.getOperation(), e.getStagesSynced());
                }
            });

            NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientSynchronizeServerStagesEvent.class, e -> {
                if (e.getOperation() != AOperation.LOGIN) {
                    updateGui(e.getOperation(), e.getStagesSynced());
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

    @SuppressWarnings("unchecked")
    public <T> void updateGui(@Nullable AOperation operation, @Nullable Set<String> stages) {
        if (runtime != null && AClientRestrictionManager.ableToUpdateJeiUI()) {
            // TODO: HYBRID
            var manager = runtime.getIngredientManager();
            if (stages == null || operation == null) { // Build Cache
                AStages.LOGGER.info("Started CACHE building!");
                AStages.TIMER.start();

                GENERIC_CACHE.clear();
                ITEM_CACHE.clear();

                // Items
                var ingredients = manager.getAllIngredients(VanillaTypes.ITEM_STACK);
                ingredients.forEach(ingredient -> {
                    var st = AClientRestrictionManager.ITEM_INSTANCE.getStagesForStack(ingredient);
                    if (!st.isEmpty()) {
                        st.forEach(s -> ITEM_CACHE.computeIfAbsent(s, e -> new ArrayList<>()).add(ingredient));
                    }
                });

                // Other Types
                manager.getRegisteredIngredientTypes().forEach(type -> {
                    if (type != VanillaTypes.ITEM_STACK) {
                        manager.getAllIngredients(type).forEach(ingredient -> {
                            var rs = manager.getIngredientHelper(ingredient).getResourceLocation(ingredient);
                            var st = AClientRestrictionManager.ITEM_INSTANCE.getStagesForResourceLocation(rs);

                            for (var stage : st) {
                                GENERIC_CACHE.computeIfAbsent(stage, s -> new HashMap<>())
                                    .computeIfAbsent(type, t -> new ArrayList<>())
                                    .add(ingredient);
                            }
                        });
                    }
                });

                AStages.TIMER.stop();
                AStages.LOGGER.info("Ended CACHE building! In {}!", AStages.TIMER);

                for (var stage : ITEM_CACHE.keySet()) {
                    // You don't need to "add" stacks, is really weird, no?!
                    if (!ClientPlayerStage.hasStage(stage)) {
                        var itemList = ITEM_CACHE.get(stage);
                        if (itemList.size() < 500) {
                            manager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, ITEM_CACHE.get(stage));
                        } else {
                            var mod = itemList.size() % 500;
                            for (int i = 0; i <= itemList.size(); i += 500) {
                                try {
                                    manager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, itemList.subList(i, i + 500));
                                } catch (IndexOutOfBoundsException exception) {
                                    manager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, itemList.subList(itemList.size() - mod, itemList.size() - 1));
                                }
                            }
                        }
                    }
                }

                for (var stage : GENERIC_CACHE.keySet()) {
                    if (!ClientPlayerStage.hasStage(stage)) {
                        for (var type : GENERIC_CACHE.get(stage).keySet()) {
                            manager.removeIngredientsAtRuntime((IIngredientType<T>) type, (Collection<T>) GENERIC_CACHE.get(stage).get(type));
                        }
                    }
                }
                return;
            }

            switch (operation) {
                case REMOVE -> {
                    for (var stage : stages) {
                        if (ITEM_CACHE.containsKey(stage)) {
                            // manager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, ITEM_CACHE.get(stage));
                            var itemList = ITEM_CACHE.get(stage);
                            if (itemList.size() < 500) {
                                manager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, ITEM_CACHE.get(stage));
                            } else {
                                var mod = itemList.size() % 500;
                                for (int i = 0; i <= itemList.size(); i += 500) {
                                    try {
                                        manager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, itemList.subList(i, i + 500));
                                    } catch (IndexOutOfBoundsException exception) {
                                        manager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, itemList.subList(itemList.size() - mod, itemList.size() - 1));
                                    }
                                }
                            }
                        }

                        if (GENERIC_CACHE.containsKey(stage)) {
                            for (var type : GENERIC_CACHE.get(stage).keySet()) {
                                manager.removeIngredientsAtRuntime((IIngredientType<T>) type, (Collection<T>) GENERIC_CACHE.get(stage).get(type));
                            }
                        }
                    }
                }
                case ADD -> {
                    for (var stage : stages) {
                        if (ITEM_CACHE.containsKey(stage)) {
                            // manager.addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, ITEM_CACHE.get(stage));
                            var itemList = ITEM_CACHE.get(stage);
                            if (itemList.size() < 500) {
                                manager.addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, ITEM_CACHE.get(stage));
                            } else {
                                var mod = itemList.size() % 500;
                                for (int i = 0; i <= itemList.size(); i += 500) {
                                    try {
                                        manager.addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, itemList.subList(i, i + 500));
                                    } catch (IndexOutOfBoundsException exception) {
                                        manager.addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, itemList.subList(itemList.size() - mod, itemList.size() - 1));
                                    }
                                }
                            }
                        }

                        if (GENERIC_CACHE.containsKey(stage)) {
                            for (var type : GENERIC_CACHE.get(stage).keySet()) {
                                manager.addIngredientsAtRuntime((IIngredientType<T>) type, (Collection<T>) GENERIC_CACHE.get(stage).get(type));
                            }
                        }
                    }
                }
                case REMOVE_ALL -> {
                    for (var stage : ITEM_CACHE.keySet()) {
                        manager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, ITEM_CACHE.get(stage));
                    }

                    for (var stage : GENERIC_CACHE.keySet()) {
                        for (var type : GENERIC_CACHE.get(stage).keySet()) {
                            manager.removeIngredientsAtRuntime((IIngredientType<T>) type, (Collection<T>) GENERIC_CACHE.get(stage).get(type));
                        }
                    }
                }
            }

//            AClientRestrictionManager.setWaitingForItemUpdate(false);

//            if (operation == PlayerStage.Operation.REMOVE) {
//                for (var stage : stages) {
//                    manager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, CACHE.get(stage));
//                }
//            } else if (operation == PlayerStage.Operation.ADD) {
//                for (var stage : stages) {
//                    manager.addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, CACHE.get(stage));
//                }
//            }


//            var manager = runtime.getIngredientManager();
//            var ingredients = manager.getAllIngredients(VanillaTypes.ITEM_STACK);
//            if (!asyncItemsToHide.isEmpty()) {
//                manager.addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, asyncItemsToHide);
//                asyncItemsToHide.clear();
//            }

            // TODO: ASYNCHRONOUS
//            var executor = Executors.newFixedThreadPool(1);
//            ForkJoinPool pool = new ForkJoinPool();
//            AStages.LOGGER.debug("Number of JEI items: {}!", ingredients.size());
//            executor.submit(() -> {
//                asyncItemsToHide = pool.invoke(new JeiTask(ingredients, 0, ingredients.size()));
//                Minecraft.getInstance().submit(() -> {
//                    manager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, asyncItemsToHide);
//                    AStages.LOGGER.debug("Ended Async!");
//                    if (Minecraft.getInstance().player != null) {
//                        Minecraft.getInstance().player.sendSystemMessage(Component.literal("Now JEI is updated!"));
//                    }
//                });
//            });

            // TODO: PARALLELISM
//            AStages.LOGGER.debug("Started Parallelism");
//            AStages.TIMER.start();

//            CompletableFuture.supplyAsync(() -> pool.invoke(new JeiTask(ingredients, 0, ingredients.size())))
//                .thenAccept(result -> {
//                    asyncItemsToHide = result;
//
//                    if (!result.isEmpty()) {
//                        manager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, result);
//                        AStages.LOGGER.debug("Ended Async!");
//                    }
//                });

//            asyncItemsToHide = pool.invoke(new JeiTask(ingredients, 0, ingredients.size()));
//            if (!asyncItemsToHide.isEmpty()) {
//                manager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, asyncItemsToHide);
//            }

//            AStages.TIMER.stop();
//            AStages.LOGGER.warn("AStages-JEI Parallelism took {}!", AStages.TIMER);
//            AStages.LOGGER.debug("Ended Parallelism");
        }
    }
}
