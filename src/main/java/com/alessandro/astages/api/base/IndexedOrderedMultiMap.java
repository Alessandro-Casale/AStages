package com.alessandro.astages.api.base;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;

import java.util.*;

@NotNullParamsAndMethodsReturn
public class IndexedOrderedMultiMap<K, V extends Comparable<V>> {
    private final Map<K, SortedSet<V>> map;
    private final Map<V, Set<K>> reverseMap;

    private IndexedOrderedMultiMap() {
        map = new HashMap<>();
        reverseMap = new HashMap<>();
    }

    public static <K, V extends Comparable<V>> IndexedOrderedMultiMap<K, V> create() {
        return new IndexedOrderedMultiMap<>();
    }

    public Collection<SortedSet<V>> values() {
        return map.values();
    }

    public Set<K> keySet() {
        return map.keySet();
    }

    public Set<Map.Entry<K, SortedSet<V>>> entrySet() {
        return map.entrySet();
    }

    public void put(K key, V value) {
        map.computeIfAbsent(key, k -> new TreeSet<>()).add(value);
        reverseMap.computeIfAbsent(value, v -> new HashSet<>()).add(key);
    }

    public SortedSet<V> get(K key) {
        return map.getOrDefault(key, Collections.emptySortedSet());
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public void clear() {
        map.clear();
        reverseMap.clear();
    }

    public void removeValue(V value) {
        var keys = reverseMap.remove(value);
        if (keys == null) return;

        for (var key : keys) {
            var set = map.get(key);
            if (set != null) {
                set.remove(value);
                if (set.isEmpty()) {
                    map.remove(key);
                }
            }
        }
    }
}