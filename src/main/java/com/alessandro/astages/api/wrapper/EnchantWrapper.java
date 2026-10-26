package com.alessandro.astages.api.wrapper;

import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;

public record EnchantWrapper(Holder<Enchantment> enchantment, int level) { }
