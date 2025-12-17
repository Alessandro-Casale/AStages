package com.alessandro.astages.mixin.recipe.minecraft;

import com.alessandro.astages.api.APlayerUtils;
import com.alessandro.astages.api.develop.UnderDevelopment;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@UnderDevelopment
@Mixin(StonecutterMenu.class)
public class AStonecutterMenu {
    @Shadow
    @Final
    private ContainerLevelAccess access;

    @Shadow private List<RecipeHolder<StonecutterRecipe>> recipes;

    @Unique
    private UUID astages$playerUUID = null;

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("RETURN"))
    public void astages$init(int containerId, @NotNull Inventory playerInventory, ContainerLevelAccess access, CallbackInfo ci) {
        astages$playerUUID = playerInventory.player.getUUID();
    }

    @Inject(method = "slotsChanged", at = @At("RETURN"))
    public void astages$slotsChanged(Container inventory, CallbackInfo ci) {
        AtomicReference<Player> player = new AtomicReference<>();
        access.execute((level1, pos) -> player.set(APlayerUtils.getPlayerFromUUID(Objects.requireNonNull(level1.getServer()), astages$playerUUID)));

        if (player.get() != null) {
            var iterator = recipes.listIterator();
            while (iterator.hasNext()) {
                var recipe = iterator.next();
                var restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(AHolder.serverAndPlayer(player.get()), new RecipeWrapper(recipe.value().getType(), recipe.id()));

                if (restriction != null) {
                    iterator.remove();
                }
            }
        }
    }
}
