package com.alessandro.astages.infrastructure.integration.rei;

import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.engine.AClientRestrictionManager;
import me.shedaniel.rei.api.client.entry.filtering.base.BasicFilteringRule;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.forge.REIPluginClient;
import net.minecraft.world.item.ItemStack;

@REIPluginClient
public class ReiItemStagesPlugin implements REIClientPlugin {
    public static BasicFilteringRule.MarkDirty filteringRule;
    public static boolean toggle = true;
//
//    private static final Set<ResourceLocation> currentlyFiltered = new HashSet<>();
//
//    @Override
//    public void registerEntries(EntryRegistry registry) {
//        REIClientPlugin.super.registerEntries(registry);
//    }
//
//    @Override
//    public void registerBasicEntryFiltering(BasicFilteringRule<?> rule) {
//        VanillaEntryTypes.ITEM
//
//    }


//    @Override
//    public void registerBasicEntryFiltering(BasicFilteringRule<?> rule) {
//        rule.
//    }


    @Override
    public void registerBasicEntryFiltering(BasicFilteringRule<?> rule) {
        filteringRule = rule.hide(() ->
            EntryRegistry.getInstance()
                .getEntryStacks()
                .filter((entry) -> {
                    if (entry.getType() == VanillaEntryTypes.ITEM) {
                        ItemStack stack = entry.castValue();
                        return AClientRestrictionManager.ITEM_INSTANCE.getRestriction(AClientHolder.player(), stack) != null;
                    }

                    return false;
                })
                .toList()
        );
    }

//    @Override
//    public double getPriority() {
//        return -1000;
//    }
}