/*
 * Copyright © 2015 - 2018 杭州大树网络技术有限公司. All Rights Reserved
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

import com.treefinance.crawler.framework.context.FieldScopes;
import com.treefinance.crawler.framework.context.RequestUtil;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;

import java.util.List;
import java.util.Map;

/**
 * @author Jerry
 * @since 15:11 2018/5/16
 */
public final class ExpressionParser {

    private final ExpressionMatcher matcher;

    private ExpressionParser() {
        this(null);
    }

    private ExpressionParser(String text) {
        this.matcher = ExpressionMatcher.match(text);
    }

    public static ExpressionParser parse(String text) {
        return new ExpressionParser(text);
    }

    public void reset(String newText) {
        matcher.reset(newText);
    }

    public boolean findExp() {
        return matcher.findExp();
    }

    public String evalExp(List<Map<String, Object>> fieldScopes) {
        return matcher.evalExp(() -> new ExpEvalContext(FieldScopes.merge(fieldScopes)));
    }

    public String evalExp(List<Map<String, Object>> fieldScopes, boolean failOnUnknown, boolean allowNull) {
        return matcher.evalExp(() -> new ExpEvalContext(FieldScopes.merge(fieldScopes), failOnUnknown, allowNull));
    }

    public String evalExp(Map<String, Object> placeholder) {
        return matcher.evalExp(placeholder);
    }

    public String evalExp(Map<String, Object> placeholder, boolean failOnUnknown, boolean allowNull) {
        return matcher.evalExp(new ExpEvalContext(placeholder, failOnUnknown, allowNull));
    }

    public Object evalExpWithObject(Map<String, Object> placeholder) {
        return matcher.evalExpWithObject(placeholder);
    }

    public Object evalWithObject(SpiderRequest request, SpiderResponse response) {
        return matcher.evalExpWithObject(FieldScopes.getVisibleFields(request, response));
    }

    public String evalUrl(SpiderRequest request, SpiderResponse response, boolean failOnUnknown, boolean allowNull) {
        return matcher.evalExp(() -> {
            String charset = RequestUtil.getContentCharset(request);
            Map<String, Object> visibleFields = FieldScopes.getVisibleFields(request, response);
            return new UrlExpEvalContext(visibleFields, failOnUnknown, allowNull, StandardExpression.URL_ENCODED_KEYS, charset);
        });
    }

    public String evalUrl(SpiderRequest request, SpiderResponse response, String charset) {
        return matcher.evalExp(() -> {
            Map<String, Object> visibleFields = FieldScopes.getVisibleFields(request, response);
            return new UrlExpEvalContext(visibleFields, StandardExpression.URL_ENCODED_KEYS, charset);
        });
    }

}
