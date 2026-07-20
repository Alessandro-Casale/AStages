package com.alessandro.astages.infrastructure.integration.emi;


import com.alessandro.astages.AStages;
import com.alessandro.astages.EntryViewerMultipleManager;
import com.alessandro.astages.EntryViewerWrapper;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientBaseItemRestriction;
import dev.emi.emi.api.*;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.registry.EmiStackList;
import dev.emi.emi.runtime.EmiHidden;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Set;

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
            if (!RUNTIME) {
                AStages.LOGGER.error("[EmiItemStagesPlugin] Instance is reloading!");
                return false;
            }

            return true;
        }
    };

    private static boolean RUNTIME = false;
    public static final EntryViewerMultipleManager MANAGER = EntryViewerMultipleManager.create(
        ITEM_WRAPPER
    );

    @Override
    public void initialize(EmiInitRegistry registry) {
        RUNTIME = false;
    }

    @Override
    public void register(EmiRegistry registry) {
        RUNTIME = true;
        MANAGER.tryPostponedBuild();
    }
}
