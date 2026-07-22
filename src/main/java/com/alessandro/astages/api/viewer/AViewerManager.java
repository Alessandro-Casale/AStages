package com.alessandro.astages.api.viewer;

import java.util.Set;

public interface AViewerManager {
    void tryPostponedBuild();
    void buildCache();

    void onStageChanged(Set<String> stages);
}
