package com.treefinance.crawler.lang;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Enumeration;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Jerry
 * @since 14:59 2018/5/8
 */
public interface Attributes {

    void setAttribute(@Nonnull String name, @Nullable Object attribute);

    Object getAttribute(@Nonnull String name);

    default <T> T getAttribute(@Nonnull String name, @Nonnull Class<T> type) {
        Object value = getAttribute(name);
        return value == null ? null : type.cast(value);
    }

    default Object getOrDefaultAttribute(@Nonnull String name, @Nullable Object defaultValue) {
        Object value = getAttribute(name);
        return value == null ? defaultValue : value;
    }

    default <T> T getOrDefaultAttribute(@Nonnull String name, @Nullable T defaultValue, @Nonnull Class<T> type) {
        T value = getAttribute(name, type);
        return value == null ? defaultValue : value;
    }

    Object computeAttribute(@Nonnull String name, @Nonnull BiFunction<String, Object, Object> mappingFunction);

    default <T> T computeAttribute(@Nonnull String name, @Nonnull BiFunction<String, Object, Object> mappingFunction, @Nonnull Class<T> type) {
        Object value = computeAttribute(name, mappingFunction);

        return value == null ? null : type.cast(value);
    }

    Object computeAttributeIfAbsent(@Nonnull String name, @Nonnull Function<String, Object> mappingFunction);

    default <T> T computeAttributeIfAbsent(@Nonnull String name, @Nonnull Function<String, Object> mappingFunction, @Nonnull Class<T> type) {
        Object value = computeAttributeIfAbsent(name, mappingFunction);

        return value == null ? null : type.cast(value);
    }

    void removeAttribute(@Nonnull String name);

    Map<String, Object> getAttributes();

    Enumeration<String> getAttributeNames();

    void clearAttributes();

    void addAttributes(@Nullable Map<String, Object> attributes);

    default void addAttributes(@Nullable Attributes attributes) {
        if (attributes != null) {
            Enumeration<String> e = attributes.getAttributeNames();
            while (e.hasMoreElements()) {
                String name = e.nextElement();
                setAttribute(name, attributes.getAttribute(name));
            }
        }
    }
}
