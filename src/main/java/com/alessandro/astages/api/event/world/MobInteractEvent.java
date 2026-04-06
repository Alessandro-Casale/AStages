package com.alessandro.astages.api.event.world;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * Fired just before {@link Mob#mobInteract(Player, InteractionHand)} happens.
 */
public class MobInteractEvent extends PlayerEvent implements ICancellableEvent {
    private final InteractionHand hand;
    private final EntityType<?> mobType;

    public MobInteractEvent(Player player, InteractionHand hand, EntityType<?> mobType) {
        super(player);
        this.hand = hand;
        this.mobType = mobType;
    }

    public InteractionHand getHand() {
        return hand;
    }

    public EntityType<?> getMobType() {
        return mobType;
    }
}