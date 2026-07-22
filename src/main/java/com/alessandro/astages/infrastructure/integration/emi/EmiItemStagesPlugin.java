package com.alessandro.astages.infrastructure.integration.emi;


import com.alessandro.astages.api.viewer.EntryViewerMultipleManager;
import com.alessandro.astages.api.viewer.EntryViewerWrapper;
import com.alessandro.astages.api.constant.AOperation;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientBaseItemRestriction;
import dev.emi.emi.api.*;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.FluidEmiStack;
import dev.emi.emi.registry.EmiStackList;
import dev.emi.emi.runtime.EmiHidden;
import dev.emi.emi.search.EmiSearch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
@NotNullParamsAndMethodsReturn
@EmiEntrypoint
public class EmiItemStagesPlugin implements EmiPlugin {
    private static final EntryViewerWrapper<EmiStack> ITEM_WRAPPER = new EntryViewerWrapper<>() {
        @Override
        public @Unmodifiable Collection<EmiStack> getAllEntries() {
            return EmiApi.getIndexStacks();
        }

        @Override
        public void showEntries(Collection<EmiStack> entries) {
            EmiHidden.pluginDisabledStacks.removeAll(entries);
        }

        @Override
        public void hideEntries(Collection<EmiStack> entries) {
            EmiHidden.pluginDisabledStacks.addAll(entries);
        }

        @Override
        public void reload() {
            EmiStackList.bakeFiltered();
            EmiSearch.update();
        }

        @Override
        public Set<String> evaluateStages(EmiStack entry) {
            return AClientRestrictionManager.ITEM_INSTANCE.getStagesForStack(entry.getItemStack());
        }

        @Override
        public @Nullable AClientBaseItemRestriction<?, ?> evaluateRestriction(AClientHolder holder, EmiStack entry) {
            return AClientRestrictionManager.ITEM_INSTANCE.getRestriction(holder, entry.getItemStack());
        }

        @Override
        public boolean isRuntimeAvailable() {
            return checkRuntime(!RUNTIME, EmiItemStagesPlugin.class);
        }
    };

    private static final EntryViewerWrapper<FluidEmiStack> FLUID_WRAPPER = new EntryViewerWrapper<>() {
        @Override
        public @Unmodifiable Collection<FluidEmiStack> getAllEntries() {
            return EmiApi.getIndexStacks().stream()
                .filter(entry -> entry instanceof FluidEmiStack)
                .map(entry -> (FluidEmiStack) entry)
                .toList();
        }

        @Override
        public void showEntries(Collection<FluidEmiStack> entries) {
            EmiHidden.pluginDisabledStacks.removeAll(entries);
        }

        @Override
        public void hideEntries(Collection<FluidEmiStack> entries) {
            EmiHidden.pluginDisabledStacks.addAll(entries);
        }

        @Override
        public void reload() {
            EmiStackList.bakeFiltered();
            EmiSearch.update();
        }

        @Override
        public Set<String> evaluateStages(FluidEmiStack entry) {
            var rs = BuiltInRegistries.FLUID.getKey((Fluid) entry.getKey());
            return AClientRestrictionManager.ITEM_INSTANCE.getStagesForResourceLocation(rs);
        }

        @Override
        public @Nullable AClientBaseItemRestriction<?, ?> evaluateRestriction(AClientHolder holder, FluidEmiStack entry) {
            var rs = BuiltInRegistries.FLUID.getKey((Fluid) entry.getKey());
            return AClientRestrictionManager.ITEM_INSTANCE.getRestrictionForResourceLocation(holder, rs);
        }

        @Override
        public boolean isRuntimeAvailable() {
            return checkRuntime(!RUNTIME, EmiItemStagesPlugin.class);
        }
    };

    private static boolean RUNTIME = false;
    public static final EntryViewerMultipleManager MANAGER = EntryViewerMultipleManager.create(ITEM_WRAPPER, FLUID_WRAPPER);

    @Override
    public void initialize(EmiInitRegistry registry) {
        RUNTIME = false;
    }

    @Override
    public void register(EmiRegistry registry) {
        RUNTIME = true;
        MANAGER.tryPostponedBuild();
    }

    public static void onReloadStarted() {
        RUNTIME = false;
    }

    public static void onReloadFinished() {
        MANAGER.buildCache();
    }

    public static void onStagesChanged(AOperation operation, Set<String> syncedStages) {
        MANAGER.onStageChanged(syncedStages);
    }
}
