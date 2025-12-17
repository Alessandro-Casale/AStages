package com.alessandro.astages.core.stage.client;

import com.alessandro.astages.api.stage.ClientStage;
import com.alessandro.astages.store.stage.AStageClientBaseManager;
import net.minecraft.world.item.ItemStack;

public class AClientGenericManager extends AStageClientBaseManager<ClientStage> {
    public void addStage(String key, ClientStage stage) {
        addStageInternal(key, stage);
    }

    public boolean hasCustomStack(String key) {
        return getStages().containsKey(key);
    }

    public ItemStack getCustomStack(String key) {
        return getStages().get(key).stack();
    }
}
