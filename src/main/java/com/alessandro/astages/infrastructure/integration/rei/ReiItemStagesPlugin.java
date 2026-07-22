package com.alessandro.astages.infrastructure.integration.rei;

import com.alessandro.astages.EntryViewerMultipleManager;
import com.alessandro.astages.EntryViewerWrapper;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientBaseItemRestriction;
import dev.architectury.fluid.FluidStack;
import me.shedaniel.rei.api.client.entry.filtering.base.BasicFilteringRule;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.plugins.PluginManager;
import me.shedaniel.rei.api.common.registry.ReloadStage;
import me.shedaniel.rei.forge.REIPluginClient;
import net.minecraft.core.registries.BuiltInRegistries;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
@NotNullParamsAndMethodsReturn
@REIPluginClient
public class ReiItemStagesPlugin implements REIClientPlugin {
    private static final EntryViewerWrapper<EntryStack<?>> ITEM_WRAPPER = new EntryViewerWrapper<>() {
        @Override
        public @Unmodifiable Collection<EntryStack<?>> getAllEntries() {
            return EntryRegistry.getInstance().getEntryStacks()
                .filter(e -> e.getType() == VanillaEntryTypes.ITEM)
                .toList();
        }

        @Override
        public void showEntries(Collection<EntryStack<?>> entries) {
            RULE.show(entries);
        }

        @Override
        public void hideEntries(Collection<EntryStack<?>> entries) {
            RULE.hide(entries);
        }

        @Override
        public Set<String> evaluateStages(EntryStack<?> entry) {
            return AClientRestrictionManager.ITEM_INSTANCE.getStagesForStack(entry.castValue());
        }

        @Override
        public @Nullable AClientBaseItemRestriction<?, ?> evaluateRestriction(AClientHolder holder, EntryStack<?> entry) {
            return AClientRestrictionManager.ITEM_INSTANCE.getRestriction(holder, entry.castValue());
        }

        @Override
        public boolean isRuntimeAvailable() {
            return checkRuntime(EntryRegistry.getInstance().isReloading(), ReiItemStagesPlugin.class);
        }
    };

    private static final EntryViewerWrapper<EntryStack<?>> FLUID_WRAPPER = new EntryViewerWrapper<>() {
        @Override
        public @Unmodifiable Collection<EntryStack<?>> getAllEntries() {
            return EntryRegistry.getInstance().getEntryStacks()
                .filter(e -> e.getType() == VanillaEntryTypes.FLUID)
                .toList();
        }

        @Override
        public void showEntries(Collection<EntryStack<?>> entries) {
            RULE.show(entries);
        }

        @Override
        public void hideEntries(Collection<EntryStack<?>> entries) {
            RULE.hide(entries);
        }

        @Override
        public Set<String> evaluateStages(EntryStack<?> entry) {
            FluidStack stack = entry.castValue();
            var rs = BuiltInRegistries.FLUID.getKey(stack.getFluid());

            return AClientRestrictionManager.ITEM_INSTANCE.getStagesForResourceLocation(rs);
        }

        @Override
        public @Nullable AClientBaseItemRestriction<?, ?> evaluateRestriction(AClientHolder holder, EntryStack<?> entry) {
            FluidStack stack = entry.castValue();
            var rs = BuiltInRegistries.FLUID.getKey(stack.getFluid());

            return AClientRestrictionManager.ITEM_INSTANCE.getRestrictionForResourceLocation(holder, rs);
        }

        @Override
        public boolean isRuntimeAvailable() {
            return checkRuntime(EntryRegistry.getInstance().isReloading(), ReiItemStagesPlugin.class);
        }
    };

    private static BasicFilteringRule<?> RULE;
    public static EntryViewerMultipleManager MANAGER = EntryViewerMultipleManager.create(ITEM_WRAPPER, FLUID_WRAPPER);

    @Override
    public void registerBasicEntryFiltering(BasicFilteringRule<?> rule) {
        RULE = rule;
    }

    @Override
    public void postStage(PluginManager<REIClientPlugin> manager, ReloadStage stage) {
        if (stage == ReloadStage.END) {
            MANAGER.tryPostponedBuild();
        }
    }
}