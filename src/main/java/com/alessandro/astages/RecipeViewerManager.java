package com.alessandro.astages;

import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.store.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.*;

@NotNullParams
public class RecipeViewerManager<T> {
    private final RecipeViewerWrapper wrapper;
    private Map<String, List<ItemStack>> STAGE_TO_ENTRY_CACHE; // Sets, why not?

    public RecipeViewerManager(RecipeViewerWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public void buildCache() {
        AStages.TIMER.start();
        STAGE_TO_ENTRY_CACHE = new HashMap<>();
        var hidden = new HashSet<ItemStack>();

        var holder = AClientHolder.serverAndPlayer();

        AStages.LOGGER.debug("Restrictions {}", AClientRestrictionManager.ITEM_INSTANCE.getRegistry().getRestrictions());
        wrapper.getAllStacks()
            .forEach(stack -> {
                for (var stage : AClientRestrictionManager.ITEM_INSTANCE.getStagesForStack(stack)) {
                    STAGE_TO_ENTRY_CACHE
                        .computeIfAbsent(stage, key -> new ArrayList<>())
                        .add(stack);
                }

                var restriction = AClientRestrictionManager.ITEM_INSTANCE.getRestriction(holder, stack);
                if (restriction != null && restriction.isEnabled(Attributes.HIDING_JEI)) {
                    hidden.add(stack);
                }
            });

        AStages.TIMER.stop();
        AStages.LOGGER.debug("Cache built in {}", AStages.TIMER);

        AStages.TIMER.reset().start();
        AStages.LOGGER.debug("Cache {}", STAGE_TO_ENTRY_CACHE);
        AStages.LOGGER.debug("Hide {}", hidden);
        if (!hidden.isEmpty()) { wrapper.hideStacks(hidden); }
        AStages.TIMER.stop();
        AStages.LOGGER.debug("Hide entries in {}", AStages.TIMER);
    }

    public void onStageChanged(AOperation operation, Set<String> stages) {
        var affectedStacks = new HashSet<ItemStack>();
        for (var stage : stages) {
            affectedStacks.addAll(STAGE_TO_ENTRY_CACHE.get(stage));
        }

        var holder = AClientHolder.serverAndPlayer();

        var toShow = new HashSet<ItemStack>();
        var toHide = new HashSet<ItemStack>();
        for (var stack : affectedStacks) {
            var restriction = AClientRestrictionManager.ITEM_INSTANCE.getRestriction(holder, stack);

            (restriction != null ? toHide : toShow).add(stack);
        }

        if (!toShow.isEmpty()) { wrapper.showStacks(toShow); }
        if (!toHide.isEmpty()) { wrapper.hideStacks(toHide); }
    }
}
