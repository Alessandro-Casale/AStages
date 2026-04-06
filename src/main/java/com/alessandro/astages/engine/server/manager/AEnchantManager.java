package com.alessandro.astages.engine.server.manager;

import com.alessandro.astages.engine.server.restriction.AEnchantRestriction;
import com.alessandro.astages.api.wrapper.EnchantWrapper;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import com.alessandro.astages.api.manager.AManager;
import net.minecraft.world.item.enchantment.Enchantment;

public class AEnchantManager extends AManager<AEnchantRestriction, Enchantment, EnchantWrapper> {
    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.ENCHANT;
    }
}