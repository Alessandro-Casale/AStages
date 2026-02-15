package com.alessandro.astages.core.client.manager;

import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.AStagesClientUtils;
import com.alessandro.astages.api.constant.AStageType;
import com.alessandro.astages.api.develop.Info;
import com.alessandro.astages.api.develop.UnderDevelopment;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.core.client.restriction.item.*;
import com.alessandro.astages.event.custom.ClientSynchronizeServerStagesEvent;
import com.alessandro.astages.event.custom.ClientSynchronizeStagesEvent;
import com.alessandro.astages.integration.jei.CustomItemStackKey;
import com.alessandro.astages.networking.ANetworking;
import com.alessandro.astages.networking.packet.item.RequestItemPropertyC2SPacket;
import com.alessandro.astages.store.ARestrictionType;
import com.alessandro.astages.store.ARestrictionTypes;
import com.alessandro.astages.store.Attributes;
import com.alessandro.astages.store.client.AClientMinimalManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;

import java.util.*;

@NotNullParams
public class AClientItemManager implements AClientMinimalManager<AClientBaseItemRestriction<?, ?>> {
    private final List<AClientBaseItemRestriction<?, ?>> restrictions = new ArrayList<>();
    private final HashMap<String, AClientBaseItemRestriction<?, ?>> IDS = new HashMap<>();

    private final List<AClientItemRestriction> items = new ArrayList<>();
    private final List<AClientItemTagRestriction> tags = new ArrayList<>();
    private final List<AClientItemModRestriction> mods = new ArrayList<>();
    private final List<AClientItemPredicateRestriction> predicates = new ArrayList<>();

    private final HashMap<CustomItemStackKey, AClientItemPropertyRestriction> properties = new HashMap<>();

    static {
        ALoader.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientSynchronizeStagesEvent.class,
            e -> AClientRestrictionManager.ITEM_INSTANCE.clearProperties()
        );

        ALoader.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientSynchronizeServerStagesEvent.class,
            e -> AClientRestrictionManager.ITEM_INSTANCE.clearProperties()
        );
    }

    public List<AClientItemRestriction> getItemRestrictions() {
        return items;
    }

    public List<AClientItemTagRestriction> getTagRestrictions() {
        return tags;
    }

    public List<AClientItemModRestriction> getModRestrictions() {
        return mods;
    }

    public List<AClientItemPredicateRestriction> getPredicates() {
        return predicates;
    }

    public List<AClientBaseItemRestriction<?, ?>> getRestrictions() {
        return restrictions;
    }

    public void clearProperties() {
        properties.clear();
    }

    public void reloadBeforeScripts() {
        restrictions.clear();
        items.clear();
        tags.clear();
        mods.clear();
        clearProperties();
    }

    public void addRestriction(AClientItemRestriction restriction) {
        commonAddOperations(restriction);
        items.add(restriction);
    }

    public void addRestriction(AClientItemTagRestriction restriction) {
        commonAddOperations(restriction);
        tags.add(restriction);
    }

    public void addRestriction(AClientItemModRestriction restriction) {
        commonAddOperations(restriction);
        mods.add(restriction);
    }

    public void addRestriction(AClientItemPredicateRestriction restriction) {
        commonAddOperations(restriction);
        predicates.add(restriction);

    }

    public void addRestriction(AClientItemPropertyRestriction restriction) {
        properties.put(CustomItemStackKey.build(restriction.stack()), restriction);
    }

    private void commonAddOperations(AClientBaseItemRestriction<?, ?> restriction) {
        restrictions.add(restriction);
        IDS.put(restriction.getId(), restriction);
    }

    @Override
    public AClientBaseItemRestriction<?, ?> getRestriction(String id) {
        return IDS.getOrDefault(id, null);
    }

    public AClientBaseItemRestriction<?, ?> getRestriction(AClientHolder holder, ItemStack stack) {
        if (holder.isServerActive()) {
            var serverRestriction = restrictions.stream().filter(r ->
                !AStagesClientUtils.hasStage(holder, AStageType.SERVER, r.getStage()) &&
                r.isRestricted(stack)
            ).findFirst().orElse(null);

            if (serverRestriction == null) { return null; } // If the stage is unlocked in the server, pass!
        }

        if (holder.isPlayerActive()) {
            return restrictions.stream().filter(r ->
                !AStagesClientUtils.hasStage(holder, AStageType.PLAYER, r.getStage()) &&
                r.isRestricted(stack)
            ).findFirst().orElse(null);
        }

        return null;
    }

    public String getRestrictionIdForStack(ItemStack stack) {
        if (stack.isEmpty()) { return null; }

        for (var restriction : restrictions) {
            if (restriction.isRestricted(stack)) {
                return restriction.getId();
            }
        }

        return null;
    }

    @UnderDevelopment
    @Info("Create strong association between requested restriction and properties")
    public AClientItemPropertyRestriction getProperties(AClientHolder holder, ItemStack stack) {
        if (stack.isEmpty()) { return null; }

        if (properties.containsKey(CustomItemStackKey.build(stack))) {
            var restriction = properties.get(CustomItemStackKey.build(stack));
            if (restriction != null) {
                return AStagesClientUtils.hasStage(holder, AStageType.SERVER, restriction.stage()) ||
                    AStagesClientUtils.hasStage(holder, AStageType.PLAYER, restriction.stage()) ? null : restriction;
            } else {
                return null;
            }
        }

        var id = getRestrictionIdForStack(stack);
        if (id != null) {
//            AStages.LOGGER.debug("Requested for stack: {}, id: {}", stack, id);
            ANetworking.sendToServer(new RequestItemPropertyC2SPacket(id, IDS.get(id).getStage(), stack));
        } else {
            properties.put(CustomItemStackKey.build(stack), null);
        }

        return null;
    }

    public Set<String> getStagesForStack(ItemStack stack) {
        Set<String> toReturn = new HashSet<>();

        restrictions.forEach(restriction -> {
            if (restriction.isRestricted(stack) && restriction.isEnabled(Attributes.HIDING_JEI)) { toReturn.add(restriction.getStage()); }
        });

        return toReturn;
    }

    public Set<String> getStagesForResourceLocation(ResourceLocation resourceLocation) {
        Set<String> toReturn = new HashSet<>();

        mods.forEach(restriction -> {
            if (restriction.getModIds().contains(resourceLocation.getNamespace()) && restriction.isEnabled(Attributes.HIDING_JEI)) {
                toReturn.add(restriction.getStage());
            }
        });

        return toReturn;
    }

    @Override
    public void removeRestriction(String id) {
        restrictions.removeIf(restriction -> restriction.getId().equals(id));
        items.removeIf(restriction -> restriction.getId().equals(id));
        mods.removeIf(restriction -> restriction.getId().equals(id));
        tags.removeIf(restriction -> restriction.getId().equals(id));
        predicates.removeIf(restriction -> restriction.getId().equals(id));
        IDS.remove(id);
        clearProperties();
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.ITEM;
    }
}
