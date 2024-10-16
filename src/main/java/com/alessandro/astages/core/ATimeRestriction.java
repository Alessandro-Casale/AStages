package com.alessandro.astages.core;

import com.alessandro.astages.util.ARestriction;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class ATimeRestriction implements ARestriction {
    public final String id;

    public int seconds = 0;

    public ATimeRestriction(String id) {
        this.id = id;
    }

    public int getSeconds() {
        return seconds;
    }

    public ATimeRestriction setSeconds(int seconds) {
        this.seconds = seconds;

        return this;
    }
}
