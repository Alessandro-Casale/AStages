package com.alessandro.astages.engine.util;

import com.alessandro.astages.api.nullability.Nullable;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.util.FakePlayer;

public class EventGuards {
    public static boolean isValidPlayer(@Nullable Player player) {
        return player != null && !player.level().isClientSide && !(player instanceof FakePlayer);
    }
}