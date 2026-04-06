package com.alessandro.astages.api.base;

import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;

import java.util.*;
import java.util.function.Predicate;

@NotNullParamsAndMethodsReturn
public class OrderedMultiMap<K, V extends Comparable<V>> {
    private final Map<K, SortedSet<V>> map;

    private OrderedMultiMap() {
        map = new HashMap<>();
    }

    public static <K, V extends Comparable<V>> OrderedMultiMap<K, V> create() {
        return new OrderedMultiMap<>();
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
    }

    public SortedSet<V> get(K key) {
        return map.getOrDefault(key, new TreeSet<>());
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public void clear() {
        map.clear();
    }

    public void removeValues(Predicate<V> predicate) {
        var iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            var entry = iterator.next();
            var values = entry.getValue();

            values.removeIf(predicate);

            if (values.isEmpty()) {
                iterator.remove();
            }
        }
    }

    @Override
    public String toString() {
        return "OrderedMultiMap{" +
            "map=" + map +
            '}';
    }
}
