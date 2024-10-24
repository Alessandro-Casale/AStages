package com.alessandro.astages.datageneration;

import com.alessandro.astages.AStages;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ALanguageProvider extends LanguageProvider {
    public ALanguageProvider(PackOutput output, String locale) {
        super(output, AStages.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add("tooltip.astages.hidden_name", "Unfamiliar Item");
        add("tooltip.astages.item_description", "This item has been staged to [%s]");
        add("message.astages.drop", "You dropped the %s.");
        add("message.astages.mine", "You do not know how to mine with the %s.");
        add("message.astages.attach", "You don't know how to attack with the %s.");
        add("message.astages.use", "You don't know how to use the %s.");
        add("message.astages.pickup", "You couldn't pick up the %s.");
    }
}
