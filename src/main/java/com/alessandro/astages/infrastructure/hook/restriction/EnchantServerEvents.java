package com.alessandro.astages.infrastructure.hook.restriction;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.wrapper.EnchantWrapper;
import com.alessandro.astages.engine.ARestrictionManager;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.hook.CommonEventSettings;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.concurrent.atomic.AtomicBoolean;

@NotNullParamsAndMethodsReturn
@EventBusSubscriber(modid = AStages.MODID)
public class EnchantServerEvents {
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if (!CommonEventSettings.requireSlotCheck()) { return; }

        if (!event.getEntity().level().isClientSide && !(event.getEntity() instanceof FakePlayer)) {
            Player player = event.getEntity();
            Inventory inventory = player.getInventory();

            if (CommonEventSettings.getSlotChanged() == null) {
                for (int i = 0; i < inventory.getContainerSize(); i++) {
                    ItemStack slotContent = inventory.getItem(i);

                    if (slotContent.getItem() instanceof EnchantedBookItem) {
                        var parsedBook = removeAllRestrictedEnchantmentFromEnchantedBook(player, slotContent);

                        if (!parsedBook.isEmpty()) {
                            inventory.setItem(CommonEventSettings.getSlotChanged(), parsedBook);
                        }
                    } else if (slotContent.isEnchanted()) {
                        removeAllRestrictedEnchantmentFromStack(player, slotContent);
                    }
                }
            } else {
                ItemStack slotContent = inventory.getItem(CommonEventSettings.getSlotChanged());

                if (slotContent.getItem() instanceof EnchantedBookItem) {
                    var parsedBook = removeAllRestrictedEnchantmentFromEnchantedBook(player, slotContent);

                    if (!parsedBook.isEmpty()) {
                        inventory.setItem(CommonEventSettings.getSlotChanged(), parsedBook);
                    }
                } else if (slotContent.isEnchanted()) {
                    removeAllRestrictedEnchantmentFromStack(player, slotContent);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        if (event.getRight() == ItemStack.EMPTY || event.getLeft() == ItemStack.EMPTY) {
            return;
        }

        var player = event.getPlayer();
        var isClientSide = player.level().isClientSide;
        if (isClientSide) { return; }

        if (stackHasBannedEnchantments(player, event.getLeft())) {
            event.setCanceled(true);
            return;
        }

        if (stackHasBannedEnchantments(player, event.getRight())) {
            event.setCanceled(true);
        }
    }

    public static boolean stackHasBannedEnchantments(Player player, ItemStack stack) {
        if (stack.getItem() instanceof EnchantedBookItem) {
            if (stack.has(DataComponents.STORED_ENCHANTMENTS)) {
                var data = stack.get(DataComponents.STORED_ENCHANTMENTS); // Where enchantments are stored!
                if (data == null) { return false; }
                var enchantments = data.entrySet();

                for (var holder : enchantments) {
                    var enchantment = holder.getKey().value();
                    var level = holder.getIntValue();

                    var restriction = ARestrictionManager.ENCHANT_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), new EnchantWrapper(enchantment, level));

                    if (restriction != null && restriction.isDisabled(Attributes.ANVIL)) {
                        return true;
                    }
                }
            } else {
                return false;
            }
        } else {
            if (stack.has(DataComponents.ENCHANTMENTS)) {
                var data = stack.get(DataComponents.ENCHANTMENTS); // Where enchantments are stored!
                if (data == null) { return false; }
                var enchantments = data.entrySet();

                for (var holder : enchantments) {
                    var enchantment = holder.getKey().value();
                    var level = holder.getIntValue();

                    var restriction = ARestrictionManager.ENCHANT_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), new EnchantWrapper(enchantment, level));

                    if (restriction != null && restriction.isDisabled(Attributes.ANVIL)) {
                        return true;
                    }
                }
            } else {
                return false;
            }
        }

        return false;
    }

    public static ItemStack removeAllRestrictedEnchantmentFromEnchantedBook(Player player, ItemStack enchantedBook) {
        if (enchantedBook.isEmpty()) { return ItemStack.EMPTY; }

        var enchantments = new ItemEnchantments.Mutable(enchantedBook.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY));

        var atLeastOneRemoved = new AtomicBoolean(false);
        enchantments.removeIf(holder -> {
            var restriction = ARestrictionManager.ENCHANT_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), new EnchantWrapper(holder.value(), enchantments.getLevel(holder)));

            if (restriction != null && restriction.isDisabled(Attributes.STORING_IN_INVENTORY)) {
                atLeastOneRemoved.set(true);
                return true;
            }

            return false;
        });

        if (atLeastOneRemoved.get()) {
            var newStack = enchantedBook.copy();
            EnchantmentHelper.setEnchantments(newStack, enchantments.toImmutable());
            return newStack;
        }

        return ItemStack.EMPTY;
    }

    public static void removeAllRestrictedEnchantmentFromStack(Player player, ItemStack itemStack) {
        if (itemStack.isEmpty()) { return; }

        var enchantments = new ItemEnchantments.Mutable(itemStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY));

        var atLeastOneRemoved = new AtomicBoolean(false);
        enchantments.removeIf(holder -> {
            var restriction = ARestrictionManager.ENCHANT_INSTANCE.getRestriction(AHolder.serverAndPlayer(player), new EnchantWrapper(holder.value(), enchantments.getLevel(holder)));

            if (restriction != null && restriction.isDisabled(Attributes.STORING_IN_INVENTORY)) {
                atLeastOneRemoved.set(true);
                return true;
            }

            return false;
        });

        if (atLeastOneRemoved.get()) {
            EnchantmentHelper.setEnchantments(itemStack, enchantments.toImmutable());
        }
    }
}
