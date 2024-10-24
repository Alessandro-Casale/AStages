package com.alessandro.astages.event.item;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.event.custom.PlayerInventoryChangedEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = AStages.MODID, value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onItemTooltip(@NotNull ItemTooltipEvent event) {
        if (event.getEntity() != null) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.getItemStack());
            if (restriction == null) { return; }

            if (restriction.hideTooltip) {
                event.getToolTip().clear();
                event.getToolTip().add(Component.literal("Unfamiliar Item").withStyle(ChatFormatting.RED));
            }
        }
    }

//    @SubscribeEvent
//    public static void onBlockPlaced(BlockEvent.@NotNull EntityPlaceEvent event) {
//
//        if (event.getEntity() instanceof ServerPlayer player) {
//            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(player, new ItemStack(event.getPlacedBlock().getBlock()));
//
//            if (restriction != null && !restriction.canBePlaced) {
//                event.setCanceled(true);
//            }
//        }
//    }

//    @SubscribeEvent
//    public static void onInventoryChanged(@NotNull PlayerInventoryChangedEvent event) {
//        var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.getStack());
//
//        int armorStart = event.getEntity().getInventory().items.size();
//        int armorEnd = armorStart + event.getEntity().getInventory().armor.size();
//
//        if (restriction != null) {
//            if (event.getSlotIndex() >= armorStart && event.getSlotIndex() <= armorEnd && !restriction.canBeEquipped) {
//                event.getEntity().getInventory().clearOrCountMatchingItems(stack -> stack.is(event.getStack().getItem()), event.getStack().getCount(), event.getEntity().inventoryMenu.getCraftSlots());
//                event.getEntity().containerMenu.broadcastChanges();
//                event.getEntity().inventoryMenu.slotsChanged(event.getEntity().getInventory());
//            } else if (!restriction.canBeStoredInInventory) {
//                event.getEntity().getInventory().clearOrCountMatchingItems(stack -> stack.is(event.getStack().getItem()), event.getStack().getCount(), event.getEntity().inventoryMenu.getCraftSlots());
//                // event.getEntity().inventoryMenu.broadcastFullState();
//                event.getEntity().containerMenu.broadcastChanges();
//                // event.getEntity().containerMenu.sendAllDataToRemote();
//                event.getEntity().inventoryMenu.slotsChanged(event.getEntity().getInventory());
//                // event.getEntity().containerMenu.slotsChanged(event.getEntity().getInventory());
//            }
//        }
//    }
}
