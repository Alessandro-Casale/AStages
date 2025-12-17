package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.core.server.restriction.AEnchantRestriction;
import com.alessandro.astages.core.wrapper.EnchantWrapper;
import com.alessandro.astages.store.ARestrictionType;
import com.alessandro.astages.store.ARestrictionTypes;
import com.alessandro.astages.store.server.AManager;
import net.minecraft.world.item.enchantment.Enchantment;

public class AEnchantManager extends AManager<AEnchantRestriction, Enchantment, EnchantWrapper> {
    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.ENCHANT;
    }
}
