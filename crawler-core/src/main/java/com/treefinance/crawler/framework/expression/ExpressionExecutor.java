/*
 * Copyright © 2015 - 2019 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.treefinance.crawler.framework.expression;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Jerry
 * @since 15:11 2018/5/16
 */
final class ExpressionExecutor {

    private ExpressionExecutor() {}

    public static boolean findExp(@Nullable String value) {
        return ExpressionMatcher.match(value).findExp();
    }

    public static String evalExp(@Nullable String value, @Nullable Map<String, Object> placeholder) {
        return ExpressionMatcher.match(value).evalExp(placeholder);
    }

    public static String evalExp(@Nullable String value, @Nullable Map<String, Object> placeholder, @Nullable List<String> urlEncodedKeys, @Nullable String charset) {
        return ExpressionMatcher.match(value).evalExp(placeholder, urlEncodedKeys, charset);
    }

    public static String evalExp(@Nullable String value, @Nonnull Supplier<Map<String, Object>> placeholderSupplier) {
        return ExpressionMatcher.match(value).evalExp(() -> new ExpEvalContext(placeholderSupplier.get()));
    }

    public static String evalExp(@Nullable String value, @Nonnull Supplier<Map<String, Object>> placeholderSupplier, @Nullable List<String> urlEncodedKeys,
        @Nullable String charset) {
        return ExpressionMatcher.match(value).evalExp(() -> new UrlExpEvalContext(placeholderSupplier.get(), urlEncodedKeys, charset));
    }

    public static String evalExp(@Nullable String value, @Nonnull ExpEvalContext context) {
        return ExpressionMatcher.match(value).evalExp(context);
    }

    public static Object evalExpWithObject(@Nullable String value, @Nullable Map<String, Object> placeholder) {
        return ExpressionMatcher.match(value).evalExpWithObject(placeholder);
    }

    public static Object evalExpWithObject(@Nullable String value, @Nonnull Supplier<Map<String, Object>> placeholderSupplier) {
        return ExpressionMatcher.match(value).evalExpWithObject(placeholderSupplier);
    }

    public static Object evalExpWithObject(@Nullable String value, @Nonnull Supplier<Map<String, Object>> placeholderSupplier, boolean failOnUnknown, boolean allowNull) {
        return ExpressionMatcher.match(value).evalExpWithObject(placeholderSupplier, failOnUnknown, allowNull);
    }

    public static String evalExpSpecial(@Nullable String value, @Nullable Map<String, Object> placeholder) {
        return ExpressionMatcher.match(value).evalExpSpecial(placeholder);
    }

    public static String evalExpSpecial(@Nullable String value, @Nonnull Supplier<Map<String, Object>> placeholderSupplier) {
        return ExpressionMatcher.match(value).evalExpSpecial(placeholderSupplier);
    }

    public static String evalExpSpecial(@Nullable String value, @Nonnull Supplier<Map<String, Object>> placeholderSupplier, boolean failOnUnknown, boolean allowNull) {
        return ExpressionMatcher.match(value).evalExpSpecial(placeholderSupplier, failOnUnknown, allowNull);
    }

}
