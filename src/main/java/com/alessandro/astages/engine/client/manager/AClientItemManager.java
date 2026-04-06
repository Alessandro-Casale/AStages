package com.alessandro.astages.engine.client.manager;

import com.alessandro.astages.api.ALoader;
import com.alessandro.astages.api.develop.UnderDevelopment;
import com.alessandro.astages.api.event.sync.ClientSynchronizeServerStagesEvent;
import com.alessandro.astages.api.event.sync.ClientSynchronizeStagesEvent;
import com.alessandro.astages.api.holder.AClientHolder;
import com.alessandro.astages.api.manager.AClientMinimalManager;
import com.alessandro.astages.api.nullability.NotNullParams;
import com.alessandro.astages.api.store.ARestrictionType;
import com.alessandro.astages.engine.AClientRestrictionManager;
import com.alessandro.astages.engine.client.evaluator.AClientItemEvaluator;
import com.alessandro.astages.engine.client.registry.AClientItemRegistry;
import com.alessandro.astages.engine.client.restriction.item.*;
import com.alessandro.astages.engine.store.ARestrictionTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;

import java.util.Set;

@NotNullParams
public class AClientItemManager implements AClientMinimalManager<AClientBaseItemRestriction<?, ?>, ItemStack> {
    private final AClientItemRegistry registry = new AClientItemRegistry();
    private final AClientItemEvaluator evaluator = new AClientItemEvaluator(registry);

    static {
        ALoader.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientSynchronizeStagesEvent.class,
            e -> AClientRestrictionManager.ITEM_INSTANCE.getRegistry().clearProperties()
        );

        ALoader.EVENT_BUS.addListener(EventPriority.NORMAL, false, ClientSynchronizeServerStagesEvent.class,
            e -> AClientRestrictionManager.ITEM_INSTANCE.getRegistry().clearProperties()
        );
    }

    @Override
    public AClientBaseItemRestriction<?, ?> getRestriction(String id) {
        return registry.getById(id);
    }

    @Override
    public AClientBaseItemRestriction<?, ?> getRestriction(AClientHolder holder, ItemStack stack) {
        return evaluator.evaluate(holder, stack);
    }

    @UnderDevelopment
    public AClientItemPropertyRestriction getProperties(AClientHolder holder, ItemStack stack) {
        return evaluator.evaluateProperties(holder, stack);
    }

    public Set<String> getStagesForStack(ItemStack stack) {
        return evaluator.evaluateStages(stack);
    }

    public Set<String> getStagesForResourceLocation(ResourceLocation resourceLocation) {
        return evaluator.evaluateStages(resourceLocation);
    }

    public void addRestriction(AClientItemRestriction restriction) {
        registry.register(restriction);
    }

    public void addRestriction(AClientItemModRestriction restriction) {
        registry.register(restriction);
    }

    public void addRestriction(AClientItemTagRestriction restriction) {
        registry.register(restriction);
    }

    public void addRestriction(AClientItemPredicateRestriction restriction) {
        registry.register(restriction);
    }

    public void addRestriction(AClientItemPropertyRestriction restriction) {
        registry.register(restriction);
    }

    @Override
    public void reloadBeforeScripts() {
        registry.clear();
    }

    @Override
    public void reloadAfterScripts() {
        registry.clearProperties();
    }

    @Override
    public void removeRestriction(String id) {
        registry.remove(id);
        registry.clearProperties();
    }

    @Override
    public AClientItemRegistry getRegistry() {
        return registry;
    }

    @Override
    public ARestrictionType associatedType() {
        return ARestrictionTypes.ITEM;
    }
}