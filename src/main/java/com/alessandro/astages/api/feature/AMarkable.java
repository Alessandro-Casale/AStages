package com.alessandro.astages.api.feature;

import com.alessandro.astages.api.develop.Info;

@Info("Mark a restriction as dirty to tell the server to update all clients! NOT during SERVER LOADING!")
public interface AMarkable {
    void markAsDirty();
}
