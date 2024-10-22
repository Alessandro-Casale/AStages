package com.alessandro.astages.core;

import com.alessandro.astages.util.ARestriction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;

import java.util.ArrayList;
import java.util.List;

public class AScreenRestriction implements ARestriction {
    public final String id;

    public List<MenuType<?>> menus = new ArrayList<>();

    public AScreenRestriction(String id) {
        this.id = id;
    }

    public AScreenRestriction restrict(MenuType<?> menu) {
        menus.add(menu);

        return this;
    }

    public boolean isRestricted(MenuType<?> menu) {
        for (MenuType<?> men : menus) {
            if (men.equals(menu)) {
                return true;
            }
        }

        return false;
    }
}
