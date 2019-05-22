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

package com.treefinance.crawler.framework.process.segment.impl;

import com.treefinance.crawler.framework.config.xml.segment.JsonPathSegment;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.expression.StandardExpression;
import com.treefinance.crawler.framework.process.segment.SegmentBase;
import com.treefinance.crawler.framework.util.json.JsonPathUtil;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.List;

/**
 * @author Jerry
 * @datetime 2015-07-17 19:45
 */
public class JsonPathSegmentImpl extends SegmentBase<JsonPathSegment> {

    public JsonPathSegmentImpl(@Nonnull JsonPathSegment segment) {
        super(segment);
    }

    @Override
    protected List<String> splitInputContent(String content, JsonPathSegment segment, SpiderRequest request, SpiderResponse response) {
        if (StringUtils.isEmpty(content)) {
            return Collections.emptyList();
        }

        String jsonPath = segment.getJsonpath();

        logger.debug("Json path: {}", jsonPath);

        jsonPath = StringUtils.trimToEmpty(jsonPath);

        if (!jsonPath.isEmpty()) {
            jsonPath = StandardExpression.eval(jsonPath, request, response);

            logger.debug("Actual json path: {}", jsonPath);

            List<String> segments = JsonPathUtil.readAsList(content, jsonPath);
            logger.info("jsonpath: {}, segments size: {}", jsonPath, segments.size());

            return segments;
        }

        return Collections.singletonList(content);
    }
}
