/*
 * Copyright © 2015 - 2018 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.treefinance.crawler.framework.expression;

import java.util.Map;

import com.treefinance.crawler.framework.context.FieldScopes;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;

/**
 * @author Jerry
 * @since 17:15 2018/5/15
 */
public class ExpressionEngine {

    private final SpiderRequest       request;

    private final SpiderResponse      response;

    private       Map<String, Object> visibleFields;

    public ExpressionEngine(SpiderRequest request, SpiderResponse response) {
        this.request = request;
        this.response = response;
    }

    public Map<String, Object> getVisibleFields() {
        if (visibleFields == null) {
            visibleFields = FieldScopes.getVisibleFields(request, response);
        }
        return visibleFields;
    }

    public String eval(String input) {
        return StandardExpression.eval(input, getVisibleFields());
    }
}
