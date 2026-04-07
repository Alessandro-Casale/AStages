package com.alessandro.astages.engine.server.restriction;

import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.api.wrapper.EnchantWrapper;
import com.alessandro.astages.api.store.container.AttributeStore;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.api.restriction.ARestriction;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.world.item.enchantment.Enchantment;

@NotNullParamsAndMethodsReturn
public class AEnchantRestriction extends ARestriction<AEnchantRestriction, Enchantment, EnchantWrapper> {
    private Enchantment enchantment;

    public AEnchantRestriction(String id, String stage) {
        super(id, stage);
    }

    @Override
    public AttributeStore allowedAttributes() {
        var defaultAttributes = AttributeStore.builder()
            .addAttribute(Attributes.ANVIL)
            .addAttribute(Attributes.ENCHANTING_TABLE)
            .addAttribute(Attributes.STORING_IN_INVENTORY)

            .addAttribute(Attributes.COMPARE_CONDITION, true)
            .addAttribute(Attributes.LEVEL, true);

        return AttributeStore.compose()
            .withSuper(super.allowedAttributes())
            .withSelf(defaultAttributes)
            .withPlugin(ARestrictionManager.ATTACHED_ATTRIBUTES, AEnchantRestriction.class)
            .build();
    }

    @Override
    public AEnchantRestriction restrict(Enchantment enchantment) {
        this.enchantment = enchantment;

        return this;
    }

    @Override
    public boolean isRestricted(EnchantWrapper wrapper) {
        if (!isValueNull(Attributes.COMPARE_CONDITION) && !isValueNull(Attributes.LEVEL)) {
            return wrapper.enchantment().equals(enchantment) && elaborateRestriction(wrapper.level());
        } else {
            return wrapper.enchantment().equals(enchantment);
        }
    }

    private boolean elaborateRestriction(int level) {
        var thisLevel = get(Attributes.LEVEL);

        return switch (get(Attributes.COMPARE_CONDITION)) {
            case EQUAL -> thisLevel == level;
            case LESS -> level < thisLevel;
            case LESS_EQUAL -> level <= thisLevel;
            case GREAT -> level > thisLevel;
            case GREAT_EQUAL -> level >= thisLevel;
        };
    }

    @SuppressWarnings("unused")
    public AEnchantRestriction setCanBeUsedInAnvil(boolean value) {
        set(Attributes.ANVIL, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AEnchantRestriction setCanBeUsedInEnchantingTable(boolean value) {
        set(Attributes.ENCHANTING_TABLE, value);
        return this;
    }

    @SuppressWarnings("unused")
    public AEnchantRestriction setCanBeStoredInInventory(boolean value) {
        set(Attributes.STORING_IN_INVENTORY, value);
        return this;
    }
}
