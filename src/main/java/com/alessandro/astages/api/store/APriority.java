package com.alessandro.astages.api.store;

import com.alessandro.astages.api.nullability.NotNullMethodsReturn;

@NotNullMethodsReturn
public class APriority {
    private final int priority;

    private APriority(int priority) {
        this.priority = priority;
    }

    public static APriority build(int priority) {
        return new APriority(priority);
    }

    public int getPriority() {
        return priority;
    }
}
