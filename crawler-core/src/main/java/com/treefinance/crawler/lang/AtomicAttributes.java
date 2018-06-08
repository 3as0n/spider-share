package com.treefinance.crawler.lang;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Jerry
 * @since 15:06 2018/5/8
 */
public class AtomicAttributes implements Attributes {

    private final AtomicReference<ConcurrentMap<String, Object>> _map = new AtomicReference<>();

    public AtomicAttributes() {
    }

    public AtomicAttributes(AtomicAttributes attributes) {
        ConcurrentMap<String, Object> map = Objects.requireNonNull(attributes).map();
        if (map != null) _map.set(new ConcurrentHashMap<>(map));
    }

    private ConcurrentMap<String, Object> map() {
        return _map.get();
    }

    private ConcurrentMap<String, Object> ensureMap() {
        while (true) {
            ConcurrentMap<String, Object> map = map();
            if (map != null) return map;
            map = new ConcurrentHashMap<>();
            if (_map.compareAndSet(null, map)) return map;
        }
    }

    @Override
    public void setAttribute(@Nonnull String name, @Nullable Object attribute) {
        if (attribute == null) removeAttribute(name);
        else ensureMap().put(name, attribute);
    }

    @Nullable
    @Override
    public Object getAttribute(@Nonnull String name) {
        Map<String, Object> map = map();
        return map == null ? null : map.get(name);
    }

    @Override
    public Object computeAttribute(@Nonnull String name, @Nonnull BiFunction<String, Object, Object> mappingFunction) {
        return ensureMap().compute(name, mappingFunction);
    }

    @Override
    public Object computeAttributeIfAbsent(@Nonnull String name, @Nonnull Function<String, Object> mappingFunction) {
        return ensureMap().computeIfAbsent(name, mappingFunction);
    }

    @Override
    public void removeAttribute(@Nonnull String name) {
        Map<String, Object> map = map();
        if (map != null) map.remove(name);
    }

    @Nonnull
    @Override
    public Map<String, Object> getAttributes() {
        ConcurrentMap<String, Object> map = map();
        return map == null ? Collections.emptyMap() : Collections.unmodifiableMap(map);
    }

    @Nonnull
    @Override
    public Enumeration<String> getAttributeNames() {
        Map<String, Object> map = map();
        return map == null ? Collections.emptyEnumeration() : Collections.enumeration(Collections.unmodifiableSet(map.keySet()));
    }

    @Override
    public void clearAttributes() {
        Map<String, Object> map = map();
        if (map != null) map.clear();
    }

    @Override
    public void addAttributes(@Nullable Map<String, Object> attributes) {
        if (attributes != null) ensureMap().putAll(attributes);
    }

    public int size() {
        Map<String, Object> map = map();
        return map == null ? 0 : map.size();
    }

    @Override
    public String toString() {
        Map<String, Object> map = map();
        return map == null ? "{}" : map.toString();
    }

}
