package com.alessandro.astages.infrastructure.mixin.loot;

import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.store.Attributes;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.Container;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RandomizableContainer.class)
public interface ARandomizableContainer {
    @WrapOperation(method = "unpackLootTable", at = @At(value = "INVOKE",target = "Lnet/minecraft/world/level/storage/loot/LootTable;fill(Lnet/minecraft/world/Container;Lnet/minecraft/world/level/storage/loot/LootParams;J)V"))
    default void astages$unpackLootTable(LootTable lootTable, Container container, LootParams params, long something, @NotNull Operation<Void> original, Player player) {
        original.call(lootTable, container, params, something);

        if (this instanceof RandomizableContainerBlockEntity blockEntity) {
            var size = blockEntity.getContainerSize();

            for (int slot = 0; slot < size; slot++) {
                var stack = blockEntity.getItem(slot);
                var copiedStack = stack.copy();
                var restriction = ARestrictionManager.LOOT_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), stack, null, lootTable.getLootTableId());

                if (restriction != null) {
                    blockEntity.removeItem(slot, stack.getCount());

                    if (restriction.isEnabled(Attributes.HAS_REPLACER)) {
                        var replacer = restriction.getReplacer().apply(copiedStack);

                        if (!replacer.isEmpty()) {
                            blockEntity.setItem(slot, replacer);
                        }
                    }
                }
            }
        }
    }
}
