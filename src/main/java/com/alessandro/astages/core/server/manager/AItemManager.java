package com.alessandro.astages.core.server.manager;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.AStagesUtils;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.holder.AHolder;
import com.alessandro.astages.api.holder.ARestrictionHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.config.AStagesCommon;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.server.restriction.item.*;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.packet.item.ItemModSyncerS2CPacket;
import com.alessandro.astages.networking.packet.item.ItemPredicateSyncerS2CPacket;
import com.alessandro.astages.networking.packet.item.ItemSyncerS2CPacket;
import com.alessandro.astages.networking.packet.item.ItemTagSyncerS2CPacket;
import com.alessandro.astages.networking.packet.reload.RequestRestrictionDeleteS2CPacket;
import com.alessandro.astages.store.ARestrictionType;
import com.alessandro.astages.store.ARestrictionTypes;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.api.feature.ClientSynchronizable;
import com.alessandro.astages.store.server.AMinimalManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NotNullParams
public class AItemManager implements AMinimalManager<ABaseItemRestriction<?, ?>>, ClientSynchronizable {
    private final Map<Class<?>, List<Integer>> containersWhitelist = new HashMap<>();

    private final List<ABaseItemRestriction<?, ?>> restrictions = new ArrayList<>();
    private final Map<String, ABaseItemRestriction<?, ?>> IDS = new HashMap<>();

    private final List<AItemRestriction> items = new ArrayList<>();
    private final List<AItemModRestriction> mods = new ArrayList<>();
    private final List<AItemTagRestriction> tags = new ArrayList<>();
    private final List<AItemPredicateRestriction> predicates = new ArrayList<>();

    // Every time, apply in THIS order!
//    private final OrderedMultiMap<String, AItemModRestriction> MOD_CACHE = OrderedMultiMap.create();
//    private final Map<ResourceLocation, AItemTagRestriction> TAG_CACHE = new HashMap<>();
//    private final Map<Item, AItemRestriction> ITEM_CACHE = new HashMap<>();

    private final List<ABaseItemRestriction<?, ?>> INVENTORY_CACHE = new ArrayList<>();
    private final List<ABaseItemRestriction<?, ?>> EQUIPMENT_CACHE = new ArrayList<>();
    private final List<ABaseItemRestriction<?, ?>> CONTAINERS_CACHE = new ArrayList<>();

    public List<AItemRestriction> getItemRestrictions() {
        return items;
    }

    public List<AItemModRestriction> getModRestrictions() {
        return mods;
    }

    public List<AItemTagRestriction> getTagRestrictions() {
        return tags;
    }

    public List<AItemPredicateRestriction> getPredicateRestrictions() {
        return predicates;
    }

    public List<ABaseItemRestriction<?, ?>> getRestrictions() {
        return restrictions;
    }

    public void reloadBeforeScripts() {
        restrictions.clear();
        IDS.clear();

        items.clear();
        mods.clear();
        tags.clear();
        predicates.clear();

//        MOD_CACHE.clear();
//        TAG_CACHE.clear();
//        ITEM_CACHE.clear();

        INVENTORY_CACHE.clear();
        EQUIPMENT_CACHE.clear();
        CONTAINERS_CACHE.clear();
    }

    public void reloadAfterScripts() {
        restrictions.forEach(restriction -> {
            if (restriction.isDisabled(Attributes.STORING_IN_INVENTORY)) {
                INVENTORY_CACHE.add(restriction);
            }

            if (restriction.isDisabled(Attributes.EQUIPPING)) {
                EQUIPMENT_CACHE.add(restriction);
            }

            if (restriction.isDisabled(Attributes.STORING_IN_CONTAINERS)) {
                CONTAINERS_CACHE.add(restriction);
            }
        });
    }

    public ABaseItemRestriction<?, ?> getRestriction(String id) {
        return IDS.getOrDefault(id, null);
    }

    public ARestrictionHolder<ABaseItemRestriction<?, ?>> getHolder(String id) {
        return ARestrictionHolder.hold(getRestriction(id));
    }

    public ABaseItemRestriction<?, ?> getRestriction(AHolder holder, ItemStack stack) {
        if (holder.isServerActive()) {
            var serverRestriction = restrictions.stream().filter(r ->
                AStagesUtils.hasStage(holder, AStageType.SERVER, r.getStage()) &&
                r.isRestricted(stack)
            ).findFirst().orElse(null);

            if (serverRestriction == null) { return null; } // If the stage is unlocked in the server, pass!
        }

        if (holder.isPlayerActive()) {
            return restrictions.stream().filter(r ->
                AStagesUtils.hasStage(holder, AStageType.PLAYER, r.getStage()) &&
                r.isRestricted(stack)
            ).findFirst().orElse(null);
        }

        return null;
    }

    public ARestrictionHolder<ABaseItemRestriction<?, ?>> getHolder(AHolder holder, ItemStack stack) {
        return ARestrictionHolder.hold(getRestriction(holder, stack));
    }

    public List<ABaseItemRestriction<?,?>> getAllRestrictions(ItemStack stack) {
        return restrictions.stream().filter(r -> r.isRestricted(stack)).toList();
    }

    public ABaseItemRestriction<?, ?> getInventoryRestriction(AHolder holder, ItemStack stack) {
        if (holder.isServerActive()) {
            var serverRestriction = INVENTORY_CACHE.stream().filter(r ->
                AStagesUtils.hasStage(holder, AStageType.SERVER, r.getStage()) &&
                    r.isRestricted(stack)
            ).findFirst().orElse(null);

            if (serverRestriction == null) { return null; } // If the stage is unlocked in the server, pass!
        }

        if (holder.isPlayerActive()) {
            return INVENTORY_CACHE.stream().filter(r ->
                AStagesUtils.hasStage(holder, AStageType.PLAYER, r.getStage()) &&
                    r.isRestricted(stack)
            ).findFirst().orElse(null);
        }

        return null;
    }

    public ARestrictionHolder<ABaseItemRestriction<?, ?>> getInventoryHolder(AHolder holder, ItemStack stack) {
        return ARestrictionHolder.hold(getInventoryRestriction(holder, stack));
    }

    public ABaseItemRestriction<?, ?> getEquipmentRestriction(AHolder holder, ItemStack stack) {
        if (holder.isServerActive()) {
            var serverRestriction = EQUIPMENT_CACHE.stream().filter(r ->
                AStagesUtils.hasStage(holder, AStageType.SERVER, r.getStage()) &&
                    r.isRestricted(stack)
            ).findFirst().orElse(null);

            if (serverRestriction == null) { return null; } // If the stage is unlocked in the server, pass!
        }

        if (holder.isPlayerActive()) {
            return EQUIPMENT_CACHE.stream().filter(r ->
                AStagesUtils.hasStage(holder, AStageType.PLAYER, r.getStage()) &&
                    r.isRestricted(stack)
            ).findFirst().orElse(null);
        }

        return null;
    }

    public ARestrictionHolder<ABaseItemRestriction<?, ?>> getEquipmentHolder(AHolder holder, ItemStack stack) {
        return ARestrictionHolder.hold(getEquipmentRestriction(holder, stack));
    }

    public ABaseItemRestriction<?, ?> getContainersRestriction(AHolder holder, ItemStack stack, Slot slot) {
        if (holder.isServerActive()) {
            var serverRestriction = getContainersRestriction(holder, AStageType.SERVER, stack, slot);

            if (serverRestriction == null) { return null; } // If the stage is unlocked in the server, pass!
        }

        if (holder.isPlayerActive()) {
            return getContainersRestriction(holder, AStageType.PLAYER, stack, slot);
        }

        return null;
    }

    public ARestrictionHolder<ABaseItemRestriction<?, ?>> getContainersHolder(AHolder holder, ItemStack stack, Slot slot) {
        return ARestrictionHolder.hold(getContainersRestriction(holder, stack, slot));
    }

    public ABaseItemRestriction<?, ?> getContainersRestriction(AHolder holder, AStageType type, ItemStack stack, Slot slot) {
        var container = slot.container;
        var index = slot.index;

        var isPresent = containersWhitelist.containsKey(container.getClass());
        if (isPresent) {
            var whitelistedIndexes = containersWhitelist.get(container.getClass());
            if (whitelistedIndexes == null) {
                return CONTAINERS_CACHE.stream().filter(r -> r.isRestricted(stack) && !AStagesUtils.hasStage(holder, type, r.getStage())).findFirst().orElse(null);
            } else if (whitelistedIndexes.contains(index)) {
                return CONTAINERS_CACHE.stream().filter(r -> r.isRestricted(stack) && !AStagesUtils.hasStage(holder, type, r.getStage())).findFirst().orElse(null);
            }
        }

        return null;
    }

    public void whiteListContainer(Class<?> containerClass, @Nullable List<Integer> slots) {
        containersWhitelist.put(containerClass, slots);
    }

    public void addRestriction(AItemRestriction restriction) {
        if (commonAddOperations(restriction)) {
            items.add(restriction);
        }
    }

    public void addRestriction(AItemTagRestriction restriction) {
        if (commonAddOperations(restriction)) {
            tags.add(restriction);
        }
    }

    public void addRestriction(AItemModRestriction restriction) {
        if (commonAddOperations(restriction)) {
            mods.add(restriction);
        }
    }

    public void addRestriction(AItemPredicateRestriction restriction) {
        if (commonAddOperations(restriction)) {
            predicates.add(restriction);
        }
    }

    private boolean commonAddOperations(ABaseItemRestriction<?, ?> restriction) {
        if (IDS.containsKey(restriction.getId())) {
            if (AStagesCommon.ENABLE_LOGS.get()) {
                AStages.LOGGER.warn("Restriction with id {} already found!", restriction.getId());
            }

            return false;
        }

        IDS.put(restriction.getId(), restriction);
        restrictions.add(restriction);

        ARestrictionManager.ALL_IDS.add(restriction.getId());
        ARestrictionManager.ALL_STAGES.add(restriction.getStage());
        return true;
    }

    public void recalculateInventoryAndEquipment(ABaseItemRestriction<?, ?> restriction) {
        INVENTORY_CACHE.removeIf(r -> r.getId().equals(restriction.getId()));
        EQUIPMENT_CACHE.removeIf(r -> r.getId().equals(restriction.getId()));
        CONTAINERS_CACHE.removeIf(r -> r.getId().equals(restriction.getId()));

        if (restriction.isDisabled(Attributes.STORING_IN_INVENTORY)) {
            INVENTORY_CACHE.add(restriction);
        }

        if (restriction.isDisabled(Attributes.EQUIPPING)) {
            EQUIPMENT_CACHE.add(restriction);
        }

        if (restriction.isDisabled(Attributes.STORING_IN_CONTAINERS)) {
            CONTAINERS_CACHE.add(restriction);
        }
    }

    public void removeRestriction(String id) {
        restrictions.removeIf(restriction -> restriction.getId().equals(id));
        items.removeIf(restriction -> restriction.getId().equals(id));
        mods.removeIf(restriction -> restriction.getId().equals(id));
        tags.removeIf(restriction -> restriction.getId().equals(id));
        predicates.removeIf(restriction -> restriction.getId().equals(id));
        INVENTORY_CACHE.removeIf(restriction -> restriction.getId().equals(id));
        EQUIPMENT_CACHE.removeIf(restriction -> restriction.getId().equals(id));
        CONTAINERS_CACHE.removeIf(restriction -> restriction.getId().equals(id));
        IDS.remove(id);

        ANetworking.sendTo(null, new RequestRestrictionDeleteS2CPacket(id, associatedType()));
    }

    @Override
    public void synchronizeWithClient(@Nullable ServerPlayer player) {
        items.forEach(restriction -> ANetworking.sendTo(player, new ItemSyncerS2CPacket(restriction)));
        tags.forEach(restriction -> ANetworking.sendTo(player, new ItemTagSyncerS2CPacket(restriction)));
        mods.forEach(restriction -> ANetworking.sendTo(player, new ItemModSyncerS2CPacket(restriction)));
        predicates.forEach(restriction -> ANetworking.sendTo(player, new ItemPredicateSyncerS2CPacket(restriction)));
    }

    public ARestrictionType associatedType() {
        return ARestrictionTypes.ITEM;
    }
}
