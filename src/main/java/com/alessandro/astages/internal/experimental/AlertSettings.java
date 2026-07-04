package com.alessandro.astages.internal.experimental;

import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.constant.AStatus;
import com.alessandro.astages.api.holder.AHolder;

import java.util.Set;

public record AlertSettings(AHolder holder,
                            AOperation operation,
                            AStatus status,
                            Set<String> stageKeys,
                            boolean showTitle,
                            boolean displayChatMessage,
                            boolean displayActionBarMessage) { }