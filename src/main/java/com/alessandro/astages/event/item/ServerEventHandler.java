package com.alessandro.astages.event.item;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AItemRestriction;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.event.custom.StageSyncedPlayerEvent;
import com.alessandro.astages.util.AStagesUtil;
import com.alessandro.astages.util.Info;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = AStages.MODID)
public class ServerEventHandler {
    @SubscribeEvent
    public static void onItemPickup(@NotNull EntityItemPickupEvent event) {
        if (event.getEntity() != null) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.getEntity(), event.getItem().getItem());

            if (restriction != null && !restriction.canPickedUp) {
                event.setCanceled(true);
                event.getItem().setPickUpDelay(restriction.pickUpDelay);
            }
        }
    }

    @SubscribeEvent
    public static void breakSpeed(PlayerEvent.@NotNull BreakSpeed event) {
        boolean isClientSide = event.getEntity().level().isClientSide;

        if (isClientSide) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(AStagesUtil.stateToStack(event.getState()));
            if (restriction != null && !restriction.canBeDig) { event.setNewSpeed(-1f); }
            return;
        }

        var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.getEntity(), AStagesUtil.stateToStack(event.getState()));
        if (restriction != null && !restriction.canBeDig) { event.setNewSpeed(-1f); }
    }

    @SubscribeEvent
    public static void onItemUsed(@NotNull PlayerInteractEvent event) {
        if (event.getLevel().isClientSide && event.isCancelable()) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.getItemStack());
            if (restriction != null && !restriction.canItemBeUsed) { event.setCanceled(true); }
            return;
        }

        if (event.isCancelable()) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.getEntity(), event.getItemStack());
            if (restriction != null && !restriction.canItemBeUsed) { event.setCanceled(true); }
        }
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.@NotNull EntityPlaceEvent event) {
        if (event.getLevel().isClientSide()) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(new ItemStack(event.getPlacedBlock().getBlock()));

            if (restriction != null && !restriction.canItemBeUsed) {
                event.setCanceled(true);
            }

            return;
        }

        if (event.getEntity() instanceof ServerPlayer player) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(player, new ItemStack(event.getPlacedBlock().getBlock()));

            if (restriction != null && !restriction.canBePlaced) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityHurt(@NotNull LivingHurtEvent event) {
        AStages.LOGGER.debug("Attack");

        if (event.getEntity() instanceof Player player) {
            ItemStack stack = player.getMainHandItem();
            AItemRestriction restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(player, stack);

            if (restriction != null && !restriction.canAttack) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.@NotNull PlayerTickEvent event) {
        if (event.player == null) {
            return;
        }

        Player player = event.player;
        Inventory inventory = player.getInventory();

        final int armorStart = inventory.items.size();
        final int armorEnd = armorStart + inventory.armor.size();

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack slotContent = inventory.getItem(i);

            if (!slotContent.isEmpty()) {
                if (i >= armorStart && i <= armorEnd) {
                    AItemRestriction restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.player, slotContent);

                    if (restriction != null && !restriction.canBeEquipped) {
                        inventory.setItem(i, ItemStack.EMPTY);
                        player.drop(slotContent, false);
                    }
                } else {
                    AItemRestriction restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.player, slotContent);

                    if (restriction != null && !restriction.canBeStoredInInventory) {
                        inventory.setItem(i, ItemStack.EMPTY);
                        player.drop(slotContent, false);
                    }
                }
            }
        }
    }
}
