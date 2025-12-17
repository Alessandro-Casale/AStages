package com.alessandro.astages.mixin.enchant;

import com.alessandro.astages.api.APlayerUtils;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.capability.AProvider;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.wrapper.EnchantWrapper;
import com.alessandro.astages.store.Attributes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(EnchantmentMenu.class)
public abstract class AEnchantmentMenu {
    @Shadow @Final private ContainerLevelAccess access;

    @Inject(method = "getEnchantmentList", at = @At("RETURN"))
    private void astages$enchant(RegistryAccess registryAccess, ItemStack stack, int slot, int cost, @NotNull CallbackInfoReturnable<List<EnchantmentInstance>> cir) {
        var value = cir.getReturnValue();
        AtomicReference<Player> owner = new AtomicReference<>();

        access.execute((l, pos) -> {
            var blockEntity = l.getBlockEntity(pos);

            if (blockEntity != null && l.getServer() != null) {
                var data = blockEntity.getData(AProvider.BLOCK_STAGE);
                owner.set(APlayerUtils.getPlayerFromUUID(l.getServer(), data.getOwner()));
            }
        });

        if (owner.get() instanceof ServerPlayer player) {
            List<Integer> toRemove = new ArrayList<>();

            for (int i = 0; i < value.size(); i++) {
                var restriction = ARestrictionManager.ENCHANT_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), new EnchantWrapper(value.get(i).enchantment.value(), value.get(i).level));

                if (restriction != null && restriction.isDisabled(Attributes.ENCHANTING_TABLE)) {
                    toRemove.add(i);
                }
            }

            for (int i : toRemove) {
                value.remove(i);
            }
        }
    }
}
