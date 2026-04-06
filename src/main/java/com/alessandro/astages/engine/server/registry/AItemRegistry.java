package com.alessandro.astages.engine.server.registry;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.alessandro.astages.engine.server.restriction.item.*;
import com.alessandro.astages.api.manager.registry.ARegistry;

import java.util.HashSet;
import java.util.Set;

@NotNullParamsAndMethodsReturn
public class AItemRegistry extends ARegistry<ABaseItemRestriction<?, ?>> {
    private final Set<AItemRestriction> items = new HashSet<>();
    private final Set<AItemModRestriction> mods = new HashSet<>();
    private final Set<AItemTagRestriction> tags = new HashSet<>();
    private final Set<AItemPredicateRestriction> predicates = new HashSet<>();

    public Set<AItemRestriction> getItemRestrictions() {
        return items;
    }
    public Set<AItemModRestriction> getModRestrictions() {
        return mods;
    }
    public Set<AItemTagRestriction> getTagRestrictions() {
        return tags;
    }
    public Set<AItemPredicateRestriction> getPredicateRestrictions() {
        return predicates;
    }

    public void register(AItemRestriction restriction) {
        if (commonRegister(restriction)) {
            items.add(restriction);
        }
    }

    public void register(AItemModRestriction restriction) {
        if (commonRegister(restriction)) {
            mods.add(restriction);
        }
    }

    public void register(AItemTagRestriction restriction) {
        if (commonRegister(restriction)) {
            tags.add(restriction);
        }
    }

    public void register(AItemPredicateRestriction restriction) {
        if (commonRegister(restriction)) {
            predicates.add(restriction);
        }
    }

    public boolean commonRegister(ABaseItemRestriction<?, ?> restriction) {
        return super.register(restriction, true);
    }

    @Override
    public @Nullable ABaseItemRestriction<?, ?> remove(String id) {
        var restriction = super.remove(id);

        if (restriction != null) {
            if (restriction instanceof AItemRestriction) { items.remove(restriction); }
            else if (restriction instanceof AItemModRestriction) { mods.remove(restriction); }
            else if (restriction instanceof AItemTagRestriction) { tags.remove(restriction); }
            else if (restriction instanceof AItemPredicateRestriction) { predicates.remove(restriction); }
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

    // Every time, apply in THIS order!
//    private final OrderedMultiMap<String, AItemModRestriction> MOD_CACHE = OrderedMultiMap.create();
//    private final Map<ResourceLocation, AItemTagRestriction> TAG_CACHE = new HashMap<>();
//    private final Map<Item, AItemRestriction> ITEM_CACHE = new HashMap<>();

//        MOD_CACHE.clear();
//        TAG_CACHE.clear();
//        ITEM_CACHE.clear();
}