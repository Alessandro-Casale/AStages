package com.alessandro.astages.api.hash;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

@NotNullParamsAndMethodsReturn
public class CustomItemStackKey {
    private final ItemStack stack;

    private CustomItemStackKey(ItemStack stack) {
        this.stack = stack;
    }

    @Contract(value = "_ -> new", pure = true)
    public static CustomItemStackKey build(ItemStack stack) {
        return new CustomItemStackKey(stack);
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof CustomItemStackKey that)) return false;

        return ItemStack.matches(that.stack, stack);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(stack.getDescriptionId());
        // result = 31 * result + Objects.hashCode(stack.getCount()); // IGNORE COUNT!
        result = 31 * result + Objects.hashCode(stack.getComponents());
        return result;
    }
}
