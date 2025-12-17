package com.alessandro.astages.integration.kubejs;

import com.alessandro.astages.api.AStagesClientUtils;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;

import java.util.ArrayList;
import java.util.List;

@NotNullParamsAndMethodsReturn
public class AStagesClientJSUtil {
    public static List<String> getClientStages() {
        return new ArrayList<>(AStagesClientUtils.getStages(AClientHolder.player()));
    }
}
