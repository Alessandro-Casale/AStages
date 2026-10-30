package com.alessandro.astages.engine.server.manager;

import com.alessandro.astages.api.feature.ClientSynchronizable;
import com.alessandro.astages.api.holder.ARestrictionHolder;
import com.alessandro.astages.api.manager.AMinimalManager;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.api.stage.AStageContext;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.engine.server.evaluator.AItemEvaluator;
import com.alessandro.astages.engine.server.registry.AItemRegistry;
import com.alessandro.astages.engine.server.restriction.item.*;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import com.alessandro.astages.engine.store.Attributes;
import com.alessandro.astages.infrastructure.networking.Networking;
import com.alessandro.astages.infrastructure.networking.packet.item.SyncItemModS2C;
import com.alessandro.astages.infrastructure.networking.packet.item.SyncItemPredicateS2C;
import com.alessandro.astages.infrastructure.networking.packet.item.SyncItemS2C;
import com.alessandro.astages.infrastructure.networking.packet.item.SyncItemTagS2C;
import com.alessandro.astages.infrastructure.networking.packet.reload.RequestRestrictionDeleteS2C;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.*;

@NotNullParams
public class AItemManager implements AMinimalManager<ABaseItemRestriction<?, ?>, ItemStack>, ClientSynchronizable {
    private final AItemRegistry registry = new AItemRegistry();
    private final AItemEvaluator evaluator = new AItemEvaluator(registry);

    private final Set<ABaseItemRestriction<?, ?>> inventoryCache = new HashSet<>();
    private final Set<ABaseItemRestriction<?, ?>> equipmentCache = new HashSet<>();
    private final Set<ABaseItemRestriction<?, ?>> containersCache = new HashSet<>();

    private final Map<Class<?>, List<Integer>> containersWhitelist = new HashMap<>();

    @Override
    public ABaseItemRestriction<?, ?> getRestriction(String id) {
        return registry.getById(id);
    }

    @Override
    public ABaseItemRestriction<?, ?> getRestriction(AStageContext context, ItemStack stack) {
        return evaluator.evaluate(context, stack);
    }

    public ABaseItemRestriction<?, ?> getInventoryRestriction(AStageContext context, ItemStack stack) {
        return evaluator.evaluateCache(inventoryCache, context, stack);
    }

    public ARestrictionHolder<ABaseItemRestriction<?, ?>> getInventoryHolder(AStageContext context, ItemStack stack) {
        return ARestrictionHolder.hold(getInventoryRestriction(context, stack));
    }

    public ABaseItemRestriction<?, ?> getEquipmentRestriction(AStageContext context, ItemStack stack) {
        return evaluator.evaluateCache(equipmentCache, context, stack);
    }

    public ARestrictionHolder<ABaseItemRestriction<?, ?>> getEquipmentHolder(AStageContext context, ItemStack stack) {
        return ARestrictionHolder.hold(getEquipmentRestriction(context, stack));
    }

    public ABaseItemRestriction<?, ?> getContainerRestriction(AStageContext context, ItemStack stack, Slot slot) {
        return evaluator.evaluateContainer(containersWhitelist, containersCache, context, stack, slot);
    }

    public ARestrictionHolder<ABaseItemRestriction<?, ?>> getContainersHolder(AStageContext context, ItemStack stack, Slot slot) {
        return ARestrictionHolder.hold(getContainerRestriction(context, stack, slot));
    }

    public Set<ABaseItemRestriction<?,?>> getAllRestrictions(ItemStack stack) {
        return evaluator.evaluateAll(stack);
    }

    public void addRestriction(AItemRestriction restriction) {
        registry.register(restriction);
    }

    public void addRestriction(AItemTagRestriction restriction) {
        registry.register(restriction);
    }

    public void addRestriction(AItemModRestriction restriction) {
        registry.register(restriction);
    }

    public void addRestriction(AItemPredicateRestriction restriction) {
        registry.register(restriction);
    }

    @Override
    public void removeRestriction(String id) {
        var restriction = registry.remove(id);
        inventoryCache.remove(restriction);
        equipmentCache.remove(restriction);
        containersCache.remove(restriction);

        Networking.sendTo(null, new RequestRestrictionDeleteS2C(id, associatedType()));
    }

    @Override
    public void onReloadStarted() {
        registry.clear();

        inventoryCache.clear();
        equipmentCache.clear();
        containersCache.clear();
    }

    @Override
    public void onReloadFinished() {
        registry.forEach(restriction -> {
            if (restriction.isDisabled(Attributes.STORING_IN_INVENTORY)) {
                inventoryCache.add(restriction);
            }

            if (restriction.isDisabled(Attributes.EQUIPPING)) {
                equipmentCache.add(restriction);
            }

            if (restriction.isDisabled(Attributes.STORING_IN_CONTAINERS)) {
                containersCache.add(restriction);
            }
        });
    }

    @Override
    public AItemRegistry getRegistry() {
        return registry;
    }

    @Override
    public void synchronizeWithClient(@Nullable ServerPlayer player) {
        registry.getItemRestrictions()
            .forEach(restriction -> Networking.sendTo(player, new SyncItemS2C(restriction)));

        registry.getTagRestrictions()
            .forEach(restriction -> Networking.sendTo(player, new SyncItemTagS2C(restriction)));

        registry.getModRestrictions()
            .forEach(restriction -> Networking.sendTo(player, new SyncItemModS2C(restriction)));

        registry.getPredicateRestrictions()
            .forEach(restriction -> Networking.sendTo(player, new SyncItemPredicateS2C(restriction)));
    }

    public void recalculateCaches(ABaseItemRestriction<?, ?> restriction) {
        inventoryCache.remove(restriction);
        equipmentCache.remove(restriction);
        containersCache.remove(restriction);

        if (restriction.isDisabled(Attributes.STORING_IN_INVENTORY)) {
            inventoryCache.add(restriction);
        }

        if (restriction.isDisabled(Attributes.EQUIPPING)) {
            equipmentCache.add(restriction);
        }

        if (restriction.isDisabled(Attributes.STORING_IN_CONTAINERS)) {
            containersCache.add(restriction);
        }
    }

    public void whiteListContainer(Class<?> containerClass, @Nullable List<Integer> slots) {
        containersWhitelist.put(containerClass, slots);
    }

    public ARestrictionType associatedType() {
        return ARestrictionTypes.ITEM;
    }
}