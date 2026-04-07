package com.alessandro.astages.api.store.config;

import com.alessandro.astages.api.nullability.NotNullParams;

@NotNullParams
public interface AConfigurablePreset<C extends AConfigurablePreset<C>> {
    @SuppressWarnings({"unchecked", "unused"})
    default C withPreset(AConfigPreset<? super C>... presets) {
        for (var preset : presets) {
            preset.applyTo((C) this);
        }

        return (C) this;
    }
}