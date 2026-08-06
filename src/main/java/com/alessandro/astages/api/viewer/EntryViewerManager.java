package com.alessandro.astages.api.viewer;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.engine.store.Attributes;

import java.util.*;

@NotNullParamsAndMethodsReturn
public class EntryViewerManager<ENTRY> implements AViewerManager {
    private final EntryViewerWrapper<ENTRY> wrapper;
    private Map<String, List<ENTRY>> STAGE_TO_ENTRY_CACHE; // Sets, why not?

    private boolean PENDING_BUILD = false;

    private EntryViewerManager(EntryViewerWrapper<ENTRY> wrapper) {
        this.wrapper = wrapper;
    }

    public static <T> EntryViewerManager<T> fromWrapper(EntryViewerWrapper<T> wrapper) {
        return new EntryViewerManager<>(wrapper);
    }

    @Override
    public void tryPostponedBuild() {
        if (!PENDING_BUILD) { return; }

        buildCache();
        PENDING_BUILD = false;
    }

    @Override
    public void buildCache() {
        if (!wrapper.isRuntimeAvailable()) {
            PENDING_BUILD = true;
            return;
        }

        AStages.TIMER.start();

        STAGE_TO_ENTRY_CACHE = new HashMap<>();
        var hidden = new HashSet<ENTRY>();

        var holder = AClientHolder.serverAndPlayer();

        wrapper.getAllEntries()
            .forEach(entry -> {

                for (var stage : wrapper.evaluateStages(entry)) {
                    STAGE_TO_ENTRY_CACHE
                        .computeIfAbsent(stage, key -> new ArrayList<>())
                        .add(entry);
                }

                var restriction = wrapper.evaluateRestriction(holder, entry);
                if (restriction != null && restriction.isEnabled(Attributes.HIDING_RECIPE_VIEWER)) {
                    hidden.add(entry);
                }
            });

        AStages.TIMER.stop();
        AStages.LOGGER.debug("Cache built in {}", AStages.TIMER);

        if (!hidden.isEmpty()) {
            AStages.TIMER.reset().start();

            wrapper.hideEntries(hidden);
            wrapper.reload();

            AStages.TIMER.stop();
            AStages.LOGGER.debug("Hide entries in {}", AStages.TIMER);
            AStages.TIMER.reset();
        }

        PENDING_BUILD = false;
    }

    @Override
    public void onStageChanged(Set<String> stages) {
        var affectedStacks = new HashSet<ENTRY>();
        for (var stage : stages) {
            affectedStacks.addAll(STAGE_TO_ENTRY_CACHE.getOrDefault(stage, new ArrayList<>()));
        }

        var holder = AClientHolder.serverAndPlayer();

        var toShow = new HashSet<ENTRY>();
        var toHide = new HashSet<ENTRY>();
        for (var entry : affectedStacks) {
            var restriction = wrapper.evaluateRestriction(holder, entry);

            (restriction != null ? toHide : toShow).add(entry);
        }

        boolean isChanged = false;

        if (!toShow.isEmpty()) {
            isChanged = true;
            wrapper.showEntries(toShow);
        }

        if (!toHide.isEmpty()) {
            isChanged = true;
            wrapper.hideEntries(toHide);
        }

        if (isChanged) {
            wrapper.reload();
        }
    }
}
