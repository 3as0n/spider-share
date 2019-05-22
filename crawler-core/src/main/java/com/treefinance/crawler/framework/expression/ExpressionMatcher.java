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

import com.treefinance.crawler.framework.util.UrlUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jerry
 * @since 12:07 2018/5/18
 */
public class ExpressionMatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpressionMatcher.class);

    private static final String EXPRESSION_REGEXP = "(\\s*)\\$\\{\\s*([^}]+)\\s*}(\\s*)";

    private static final Pattern EXPRESSION_PATTERN = Pattern.compile(EXPRESSION_REGEXP);

    private static final int PLACEHOLDER_GROUP = 2;

    private String text;

    private Matcher matcher;

    private Boolean find;

    private ExpressionMatcher() {}

    private ExpressionMatcher(@Nullable String text) {
        this.text = StringUtils.defaultString(text);
        if (!this.text.isEmpty()) {
            this.matcher = makeMatcher(this.text);
            this.find = null;
        } else {
            this.matcher = null;
            this.find = false;
        }
    }

    public static ExpressionMatcher match(@Nullable String text) {
        return new ExpressionMatcher(text);
    }

    public void reset(String newText) {
        this.text = StringUtils.defaultString(newText);
        if (this.text.isEmpty()) {
            this.matcher = null;
            this.find = false;
        } else if (matcher == null) {
            this.matcher = makeMatcher(this.text);
            this.find = null;
        } else {
            this.matcher.reset(this.text);
            this.find = null;
        }
    }

    public boolean findExp() {
        if (find == null) {
            find = matcher.find();
        }
        return find;
    }

    public String evalExp(@Nonnull ExpEvalContext context) {
        return this.evalExp(context, null);
    }

    public String evalExp(@Nonnull ExpEvalContext context, @Nullable BiFunction<String, String, String> mappingFunction) {
        Objects.requireNonNull(context);
        if (find()) {
            LOGGER.debug("Find expression and eval. input: {}", text);

            String result = replace(context, mappingFunction);

            LOGGER.debug("Exp-eval result: {}", result);

            return result;
        }

        return text;
    }

    public String evalExp(@Nonnull Supplier<ExpEvalContext> contextSupplier) {
        return this.evalExp(contextSupplier, null);
    }

    // --------------------------------------------- base methods

    public String evalExp(@Nonnull Supplier<ExpEvalContext> contextSupplier, @Nullable BiFunction<String, String, String> mappingFunction) {
        if (find()) {
            ExpEvalContext context = Objects.requireNonNull(contextSupplier.get());

            LOGGER.debug("Find expression and eval. input: {}", text);

            String result = replace(context, mappingFunction);

            LOGGER.debug("Exp-eval result: {}", result);

            return result;
        }

        return text;
    }

    public String evalExp(@Nullable Map<String, Object> placeholderMapping) {
        return this.evalExp(placeholderMapping, null);
    }

    public String evalExp(@Nullable Map<String, Object> placeholderMapping, @Nullable BiFunction<String, String, String> mappingFunction) {
        return this.evalExp(() -> new ExpEvalContext(placeholderMapping), mappingFunction);
    }

    public String evalExp(@Nullable Map<String, Object> placeholderMapping, @Nullable List<String> urlEncodedKeys, @Nullable String charset) {
        return this.evalExp(() -> new UrlExpEvalContext(placeholderMapping, urlEncodedKeys, charset));
    }

    public Object evalRefExp(@Nonnull Supplier<RefExpEvalContext> contextSupplier) {
        if (Boolean.FALSE.equals(find)) {
            return text;
        }

        RefExpEvalContext context = Objects.requireNonNull(contextSupplier.get());

        return matchOrEvalExp(context);
    }

    public Object evalRefExp(@Nonnull RefExpEvalContext context) {
        Objects.requireNonNull(context);
        if (Boolean.FALSE.equals(find)) {
            return text;
        }

        return matchOrEvalExp(context);
    }

    public Object evalExpWithObject(Map<String, Object> placeholderMapping) {
        return evalRefExp(() -> new RefExpEvalContext(placeholderMapping));
    }

    public Object evalExpWithObject(@Nonnull Supplier<Map<String, Object>> placeholderSupplier) {
        return evalRefExp(() -> new RefExpEvalContext(placeholderSupplier.get()));
    }

    public Object evalExpWithObject(@Nonnull Supplier<Map<String, Object>> placeholderSupplier, boolean failOnUnknown, boolean allowNull) {
        return evalRefExp(() -> new RefExpEvalContext(placeholderSupplier.get(), failOnUnknown, allowNull));
    }

    public String evalExpSpecial(Map<String, Object> placeholderMapping) {
        return (String)evalRefExp(() -> new RefExpEvalContext(placeholderMapping, true));
    }

    public String evalExpSpecial(@Nonnull Supplier<Map<String, Object>> placeholderSupplier) {
        return (String)evalRefExp(() -> new RefExpEvalContext(placeholderSupplier.get(), true));
    }

    public String evalExpSpecial(@Nonnull Supplier<Map<String, Object>> placeholderSupplier, boolean failOnUnknown, boolean allowNull) {
        return (String)evalRefExp(() -> new RefExpEvalContext(placeholderSupplier.get(), failOnUnknown, allowNull, true));
    }

    // --------------------------------------------- extended methods

    @Nonnull
    private Matcher makeMatcher(String value) {
        return EXPRESSION_PATTERN.matcher(value);
    }

    private String getPlaceholder() {
        return matcher.group(PLACEHOLDER_GROUP);
    }

    private boolean find() {
        if (find == null) {
            find = matcher.find();
        } else if (find) {
            return matcher.find(0);
        }

        return find;
    }

    private String replace(@Nonnull ExpEvalContext context, @Nullable BiFunction<String, String, String> mappingFunction) {
        BiFunction<String, String, String> function = mappingFunction;

        if (context instanceof UrlExpEvalContext && function == null) {
            function = (placeholder, replacement) -> {
                if (((UrlExpEvalContext)context).needUrlEncode(placeholder)) {
                    LOGGER.info("Encoding url key[{}] with charset[{}]", placeholder, ((UrlExpEvalContext)context).getCharset());

                    return UrlUtils.urlEncode(replacement, ((UrlExpEvalContext)context).getCharset(), false);
                }
                return null;
            };
        }

        return toReplace(context, function);
    }

    private String toReplace(@Nonnull ExpEvalContext context, @Nullable BiFunction<String, String, String> mappingFunction) {
        PlaceholderResolver resolver = new PlaceholderResolver(context);
        resolver.validate();

        StringBuffer sb = new StringBuffer();
        do {
            String prefix = StringUtils.defaultString(matcher.group(1));
            String suffix = StringUtils.defaultString(matcher.group(3));

            String placeholder = getPlaceholder();
            String replacement = resolver.resolveAsString(placeholder);
            if (replacement != null) {
                if (mappingFunction != null) {
                    String newValue = mappingFunction.apply(placeholder, replacement);
                    if (newValue != null) {
                        replacement = newValue;
                    }
                }
                matcher.appendReplacement(sb, Matcher.quoteReplacement(prefix + replacement + suffix));
            }
        } while (matcher.find());
        matcher.appendTail(sb);
        return sb.toString();
    }

    private Object matchOrEvalExp(@Nonnull RefExpEvalContext context) {
        if (matcher.matches()) {
            find = true;

            if (context.isNullableIfMatch()) {
                PlaceholderResolver placeholderResolver = new PlaceholderResolver(context.getPlaceholderMapping(), false, true, false);
                if (context.isFormat()) {
                    return placeholderResolver.resolveAsString(getPlaceholder());
                } else {
                    return placeholderResolver.resolve(getPlaceholder());
                }
            } else {
                PlaceholderResolver placeholderResolver = new PlaceholderResolver(context);
                if (context.isFormat()) {
                    Object result = placeholderResolver.resolveAsString(getPlaceholder());

                    return result == null ? text : result;
                } else {
                    return placeholderResolver.resolve(getPlaceholder());
                }
            }
        }

        matcher.reset();

        return evalExp(context);
    }

}
