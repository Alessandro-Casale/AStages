package com.alessandro.astages.infrastructure.integration.kubejs.util;

import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.util.AStagesClientUtils;

import java.util.ArrayList;
import java.util.List;

@NotNullParamsAndMethodsReturn
public class KubeJSClientUtils {
    public static List<String> getClientStages() {
        return new ArrayList<>(AStagesClientUtils.getStages(AClientHolder.player()));
    }
}
