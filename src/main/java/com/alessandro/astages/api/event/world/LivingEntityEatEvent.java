package com.alessandro.astages.api.event.world;

import com.alessandro.astages.api.nullability.Nullable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.EntityEvent;

public class LivingEntityEatEvent extends EntityEvent {
    private final ItemStack food;

    public LivingEntityEatEvent(Entity entity, ItemStack food) {
        super(entity);
        this.food = food;
    }

    public ItemStack getFood() {
        return food;
    }

    public @Nullable ServerPlayer getPlayer() {
        if (getEntity() instanceof ServerPlayer player) {
            return player;
        } else {
            return null;
        }
    }
}
