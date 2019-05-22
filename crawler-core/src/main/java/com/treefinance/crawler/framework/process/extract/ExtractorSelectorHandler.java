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

package com.treefinance.crawler.framework.process.extract;

import com.treefinance.crawler.framework.config.xml.extractor.ExtractorSelector;
import com.treefinance.crawler.framework.config.xml.page.PageExtractor;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.util.FieldUtils;
import com.treefinance.toolkit.util.Preconditions;
import com.treefinance.toolkit.util.RegExp;
import com.treefinance.toolkit.util.json.Jackson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Jerry
 * @since 17:56 2018/7/25
 */
public class ExtractorSelectorHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final List<ExtractorSelector> extractorSelectors;
    private final Map<String, PageExtractor> pageExtractors;

    public ExtractorSelectorHandler(@Nonnull List<ExtractorSelector> extractorSelectors, @Nonnull Map<String, PageExtractor> pageExtractors) {
        this.extractorSelectors = Objects.requireNonNull(extractorSelectors);
        this.pageExtractors = Objects.requireNonNull(pageExtractors);
    }

    public List<PageExtractor> select(SpiderRequest request) {
        Object input = request.getInput();
        Preconditions.notNull("input", input);

        if (logger.isTraceEnabled()) {
            logger.trace("Extract input: {}", Jackson.toJSONString(input));
        }

        List<PageExtractor> selected = new ArrayList<>();

        Map<String, PageExtractor> total = new HashMap<>(pageExtractors);
        if (!extractorSelectors.isEmpty()) {
            Map<String, String> fields = new HashMap<>();
            List<PageExtractor> alternative = new ArrayList<>();
            for (ExtractorSelector selector : extractorSelectors) {
                String value = fields.computeIfAbsent(selector.getField(), fieldName -> FieldUtils.getFieldValueAsString(input, fieldName));

                logger.trace("extractor selector >>> field: {}, value: {}, contains: {}, dis-contains: {}", selector.getField(), value, selector.getContainRegex(),
                    selector.getDisContainRegex());

                if (value == null)
                    continue;

                PageExtractor pageExtractor = selector.getPageExtractor();
                if (match(value, selector.getDisContainRegex())) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("matched dis-contain pattern: {}, value: {}, field: {}", selector.getDisContainRegex(), value, selector.getField());
                    } else {
                        logger.debug("matched dis-contain pattern: {}, field: {}", selector.getDisContainRegex(), selector.getField());
                    }
                    selected.remove(pageExtractor);
                } else if (match(value, selector.getContainRegex())) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("matched contain pattern: {}, value: {}, field: {}", selector.getContainRegex(), value, selector.getField());
                    } else {
                        logger.debug("matched contain pattern: {}, field: {}", selector.getContainRegex(), selector.getField());
                    }
                    selected.add(pageExtractor);
                } else if (!Boolean.TRUE.equals(pageExtractor.getDisAlternative())) {
                    alternative.add(pageExtractor);
                }
                total.remove(pageExtractor.getId());
            }
            selected.addAll(alternative);
        }

        if (!total.isEmpty()) {
            for (PageExtractor pageExtractor : total.values()) {
                if (!Boolean.TRUE.equals(pageExtractor.getDisAlternative())) {
                    selected.add(pageExtractor);
                }
            }
        }

        return selected;
    }

    private boolean match(String value, String regex) {
        return StringUtils.isNotBlank(regex) && RegExp.find(value, regex);
    }
}
