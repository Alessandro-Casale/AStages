package com.alessandro.astages.engine.client.registry;

import com.alessandro.astages.api.hash.CustomItemStackKey;
import com.alessandro.astages.api.manager.registry.AClientRegistry;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.engine.client.restriction.item.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public class AClientItemRegistry extends AClientRegistry<AClientBaseItemRestriction<?, ?>> {
    private final Set<AClientItemRestriction> items = new HashSet<>();
    private final Set<AClientItemModRestriction> mods = new HashSet<>();
    private final Set<AClientItemTagRestriction> tags = new HashSet<>();
    private final Set<AClientItemPredicateRestriction> predicates = new HashSet<>();

    private final HashMap<CustomItemStackKey, AClientItemPropertyRestriction> properties = new HashMap<>();

    public Set<AClientItemRestriction> getItemRestrictions() {
        return items;
    }
    public Set<AClientItemModRestriction> getModRestrictions() {
        return mods;
    }
    public Set<AClientItemTagRestriction> getTagRestrictions() {
        return tags;
    }
    public Set<AClientItemPredicateRestriction> getPredicateRestrictions() {
        return predicates;
    }

    public @Nullable AClientItemPropertyRestriction getProperty(CustomItemStackKey key) {
        return properties.getOrDefault(key, null);
    }

    public void setProperty(CustomItemStackKey key, AClientItemPropertyRestriction property) {
        properties.put(key, property);
    }

    public void setNullProperty(CustomItemStackKey key) {
        properties.put(key, null);
    }

    public boolean isPropertyPresent(CustomItemStackKey key) {
        return properties.containsKey(key);
    }

    public HashMap<CustomItemStackKey, AClientItemPropertyRestriction> getProperties() {
        return properties;
    }

    public void register(AClientItemRestriction restriction) {
        super.register(restriction);
        items.add(restriction);
    }

    public void register(AClientItemModRestriction restriction) {
        super.register(restriction);
        mods.add(restriction);
    }

    public void register(AClientItemTagRestriction restriction) {
        super.register(restriction);
        tags.add(restriction);
    }

    public void register(AClientItemPredicateRestriction restriction) {
        super.register(restriction);
        predicates.add(restriction);
    }

    public void register(AClientItemPropertyRestriction restriction) {
        properties.put(CustomItemStackKey.build(restriction.getStack()), restriction);
    }

    @Override
    public @Nullable AClientBaseItemRestriction<?, ?> remove(String id) {
        var restriction = super.remove(id);

        if (restriction != null) {
            if (restriction instanceof AClientItemRestriction) { items.remove(restriction); }
            else if (restriction instanceof AClientItemModRestriction) { mods.remove(restriction); }
            else if (restriction instanceof AClientItemTagRestriction) { tags.remove(restriction); }
            else if (restriction instanceof AClientItemPredicateRestriction) { predicates.remove(restriction); }
        }

        return restriction;
    }

    @Override
    public void clear() {
        super.clear();
        items.clear();
        mods.clear();
        tags.clear();
        predicates.clear();
    }

    public void clearProperties() {
        properties.clear();
    }
}