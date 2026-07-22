package com.alessandro.astages.infrastructure.integration.jei;

import com.alessandro.astages.EntryViewerMultipleManager;
import com.alessandro.astages.EntryViewerWrapper;
import com.alessandro.astages.api.AResourceLocation;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.restriction.item.AClientBaseItemRestriction;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Set;

@NotNullParamsAndMethodsReturn
@JeiPlugin
public class JeiItemStagesPlugin implements IModPlugin {
    private static final EntryViewerWrapper<ItemStack> ITEM_WRAPPER = new EntryViewerWrapper<>() {
        @Override
        public @Unmodifiable Collection<ItemStack> getAllEntries() {
                return RUNTIME.getIngredientManager().getAllItemStacks();
        }

        @Override
        public void showEntries(Collection<ItemStack> entries) {
            RUNTIME.getIngredientManager().addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, entries);
        }

        @Override
        public void hideEntries(Collection<ItemStack> entries) {
            RUNTIME.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, entries);
        }

        @Override
        public Set<String> evaluateStages(ItemStack entry) {
            return AClientRestrictionManager.ITEM_INSTANCE.getStagesForStack(entry);
        }

        @Override
        public @Nullable AClientBaseItemRestriction<?, ?> evaluateRestriction(AClientHolder holder, ItemStack entry) {
            return AClientRestrictionManager.ITEM_INSTANCE.getRestriction(holder, entry);
        }

        @Override
        public boolean isRuntimeAvailable() {
            return checkRuntime(RUNTIME == null, JeiItemStagesPlugin.class);
        }
    };

    private static final EntryViewerWrapper<FluidStack> FLUID_WRAPPER = new EntryViewerWrapper<>() {
        @Override
        public @Unmodifiable Collection<FluidStack> getAllEntries() {
            return RUNTIME.getIngredientManager().getAllIngredients(NeoForgeTypes.FLUID_STACK);
        }

        @Override
        public void showEntries(Collection<FluidStack> entries) {
            RUNTIME.getIngredientManager().addIngredientsAtRuntime(NeoForgeTypes.FLUID_STACK, entries);
        }

        @Override
        public void hideEntries(Collection<FluidStack> entries) {
            RUNTIME.getIngredientManager().removeIngredientsAtRuntime(NeoForgeTypes.FLUID_STACK, entries);
        }

        @Override
        public Set<String> evaluateStages(FluidStack entry) {
            var rs = BuiltInRegistries.FLUID.getKey(entry.getFluid());

            return AClientRestrictionManager.ITEM_INSTANCE.getStagesForResourceLocation(rs);
        }

        @Override
        public @Nullable AClientBaseItemRestriction<?, ?> evaluateRestriction(AClientHolder holder, FluidStack entry) {
            var rs = BuiltInRegistries.FLUID.getKey(entry.getFluid());

            return AClientRestrictionManager.ITEM_INSTANCE.getRestrictionForResourceLocation(holder, rs);
        }

        @Override
        public boolean isRuntimeAvailable() {
            return checkRuntime(RUNTIME == null, JeiItemStagesPlugin.class);
        }
    };

    private static IJeiRuntime RUNTIME;
    public static EntryViewerMultipleManager MANAGER = EntryViewerMultipleManager.create(ITEM_WRAPPER, FLUID_WRAPPER);

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        RUNTIME = jeiRuntime;
    }

    @Override
    public ResourceLocation getPluginUid() {
        return AResourceLocation.fromNamespaceAndPath("item_jei");
    }
}