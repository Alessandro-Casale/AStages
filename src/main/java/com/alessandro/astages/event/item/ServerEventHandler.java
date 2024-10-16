package com.alessandro.astages.event.item;

import com.alessandro.astages.AStages;
import com.alessandro.astages.core.AItemRestriction;
import com.alessandro.astages.core.ARestrictionManager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = AStages.MODID)
public class ServerEventHandler {
    @SubscribeEvent
    public static void onItemPickup(@NotNull EntityItemPickupEvent event) {
        if (event.getEntity() != null) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.getEntity(), event.getItem().getItem());

            // This change the name, but "create" a new stack!
            // event.getItem().getItem().setHoverName(Component.literal("UNFAMILIAR!").withStyle(ChatFormatting.RESET));
            // event.getItem().getItem().name

            if (restriction != null && !restriction.canPickedUp) {
                event.setCanceled(true);
                event.getItem().setPickUpDelay(restriction.pickUpDelay);
            }
        }
    }

//    @SubscribeEvent
//    public static void onLoggedIn(PlayerEvent.@NotNull PlayerLoggedInEvent event) {
//            event.getEntity().inventoryMenu.addSlotListener(new APlayerInventoryListener(event.getEntity()));
//            event.getEntity().inventoryMenu.slotsChanged(event.getEntity().getInventory());
//    }
//

//    public static void onStageAdded(StageAddedPlayerEvent event) {
//        MinecraftForge.EVENT_BUS.post(new PlayerInventoryChangedEvent(event.getEntity(), 0))
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
//                // event.getEntity().inventoryMenu.slotsChanged(event.getEntity().getInventory());
//            } else if (!restriction.canBeStoredInInventory) {
//                event.getEntity().getInventory().clearOrCountMatchingItems(stack -> stack.is(event.getStack().getItem()), event.getStack().getCount(), event.getEntity().inventoryMenu.getCraftSlots());
//                event.getEntity().containerMenu.broadcastChanges();
//                event.getEntity().inventoryMenu.slotsChanged(event.getEntity().getInventory());

//                event.getEntity().getInventory().setItem(event.getSlotIndex(), ItemStack.EMPTY);
//                event.getEntity().getInventory().setChanged();
//
//                if (event.getEntity().getMainHandItem().is(event.getStack().getItem())) {
//                    event.getEntity().setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
//                }
//
//                if (event.getEntity().getOffhandItem().is(event.getStack().getItem())) {
//                    event.getEntity().setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
//                }
//
//                event.getEntity().getInventory().setChanged();

    // event.getEntity().getInventory().clearOrCountMatchingItems(stack -> stack.is(event.getStack().getItem()), event.getStack().getCount(), event.getEntity().inventoryMenu.getCraftSlots());

    // event.getEntity().getInventory().setItem(event.getSlotIndex(), ItemStack.EMPTY);
    // .setCount(0);
    // event.getEntity().getInventory().setChanged();
    // event.getEntity().inventoryMenu.broadcastChanges();

    // event.getEntity().inventoryMenu.broadcastFullState();


    // event.getEntity().containerMenu.broadcastChanges();
    // event.getEntity().containerMenu.sendAllDataToRemote();
    // event.getEntity().inventoryMenu.slotsChanged(event.getEntity().getInventory());
    // event.getEntity().containerMenu.slotsChanged();
    // event.getEntity().inventoryMenu.broadcastChanges();
    // event.getEntity().inventoryMenu.slotsChanged(event.getEntity().getInventory());
    // event.getEntity().containerMenu.slotsChanged(event.getEntity().getInventory());
    // event.getMenu().sendAllDataToRemote();

//                if (event.getEntity().isCreative()) {
//                    event.getEntity(). new ClientboundContainerSetSlotPacket(event.getMenu().containerId, event.getMenu().incrementStateId(), event.getSlotIndex(), event.getStack()));
////                }
//            }
//        }
//    }

    @SubscribeEvent
    public static void onItemUsed(PlayerInteractEvent.@NotNull RightClickItem event) {
        if (event.getItemStack().isEmpty()) {
            return;
        }

        var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.getEntity(), event.getItemStack());

        if (restriction != null && !restriction.canItemBeUsed) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onBlockUsed(PlayerInteractEvent.@NotNull RightClickEmpty event) {
        // if (event.getItemStack().isEmpty()) { return; }

        // event.setUseBlock(Event.Result.DENY);
        // event.getEntity().sendSystemMessage(event.getItemStack().getDisplayName());

        var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.getEntity(), new ItemStack(event.getLevel().getBlockState(event.getPos()).getBlock()));

        if (restriction != null && !restriction.canBlockBeUsed) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onBlockUsed(PlayerInteractEvent.@NotNull RightClickBlock event) {
        // if (event.getItemStack().isEmpty()) { return; }

        // event.setUseBlock(Event.Result.DENY);
        // event.getEntity().sendSystemMessage(event.getItemStack().getDisplayName());

        var restrictionBlock = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.getEntity(), new ItemStack(event.getLevel().getBlockState(event.getPos()).getBlock()));
        var restrictionItem = ARestrictionManager.ITEM_INSTANCE.getRestriction(event.getEntity(), event.getItemStack());

        if (restrictionItem != null && !restrictionItem.canItemBeUsed) {
            event.setUseItem(Event.Result.DENY);
        }

        if (restrictionBlock != null && !restrictionBlock.canBlockBeUsed) {
            event.setUseBlock(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.@NotNull EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player player) {
            var restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(player, new ItemStack(event.getPlacedBlock().getBlock()));

            if (restriction != null && !restriction.canBePlaced) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityHurt(@NotNull LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            ItemStack stack = player.getMainHandItem();
            AItemRestriction restriction = ARestrictionManager.ITEM_INSTANCE.getRestriction(player, stack);

            if (restriction != null && !restriction.canAttack) {
                event.setCanceled(true);
            }
        }
    }

    /*
    @SubscribeEvent
    public static void itemRename(PlayerEvent.NameFormat event) {
        event.setDisplayname(Component.literal("UNFAMILIAR!"));
    }
    */

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

            // slotContent.getHighlightTip()

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
//                }
                }
            }
        }
    }
}
