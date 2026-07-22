package com.alessandro.astages.api.viewer;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public class EntryViewerMultipleManager implements AViewerManager {
    private final List<AViewerManager> managers;

    private EntryViewerMultipleManager(List<AViewerManager> managers) {
        this.managers = managers;
    }

    public static EntryViewerMultipleManager create(AViewerManager... managers) {
        return new EntryViewerMultipleManager(List.of(managers));
    }

    public static EntryViewerMultipleManager create(EntryViewerWrapper<?>... wrappers) {
        var managers = new ArrayList<AViewerManager>();

        for (var wrapper : wrappers) {
            managers.add(EntryViewerManager.fromWrapper(wrapper));
        }

        return new EntryViewerMultipleManager(managers);
    }

    @Override
    public void tryPostponedBuild() {
        for (AViewerManager manager : managers) {
            manager.tryPostponedBuild();
        }
    }

    @Override
    public void buildCache() {
        for (AViewerManager manager : managers) {
            manager.buildCache();
        }
    }

    @Override
    public void onStageChanged(Set<String> stages) {
        for (AViewerManager manager : managers) {
            manager.onStageChanged(stages);
        }
    }
}
