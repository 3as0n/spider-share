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

package com.treefinance.crawler.framework.format;

import com.datatrees.common.conf.Configuration;
import com.treefinance.crawler.framework.config.xml.extractor.FieldExtractor;
import com.treefinance.crawler.framework.consts.Constants;
import com.treefinance.crawler.framework.context.AbstractProcessorContext;
import com.treefinance.crawler.framework.context.RequestUtil;
import com.treefinance.crawler.framework.context.function.LinkNode;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.format.datetime.DateTimeFormats;
import com.treefinance.crawler.framework.format.number.NumberUnit;
import com.treefinance.crawler.framework.format.number.NumberUnitMapping;
import com.treefinance.crawler.framework.util.FieldUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.format.DateTimeFormatter;

import javax.annotation.Nonnull;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Jerry
 * @since 16:55 2018/7/16
 */
public class FormatConfig implements Serializable {

    private final SpiderRequest request;

    private final SpiderResponse response;

    private final String pattern;

    public FormatConfig(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response, @Nonnull FieldExtractor fieldExtractor) {
        this(request, response, fieldExtractor.getFormat());
    }

    public FormatConfig(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response, String pattern) {
        this.request = request;
        this.response = response;
        this.pattern = pattern;
    }

    public SpiderRequest getRequest() {
        return request;
    }

    public SpiderResponse getResponse() {
        return response;
    }

    public String getPattern() {
        return pattern;
    }

    public String trimmedPattern() {
        return StringUtils.trimToEmpty(pattern);
    }

    @Nonnull
    public DateTimeFormats getDateTimeFormats() {
        return request.computeExtraIfAbsent(Constants.CRAWLER_DATE_FORMAT, k -> new DateTimeFormats(), DateTimeFormats.class);
    }

    public DateTimeFormatter getDateTimeFormatter(String pattern) {
        return getDateTimeFormats().getFormatter(pattern);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    public Map<String, NumberUnit> getNumberFormatMap(Configuration conf) {
        return (Map<String, NumberUnit>)request.computeExtraIfAbsent(Constants.CRAWLER_REQUEST_NUMBER_MAP, key -> NumberUnitMapping.getNumberUnitMap(conf));
    }

    public AbstractProcessorContext getProcessorContext() {
        return request.getProcessorContext();
    }

    public Object getSourceFieldValue(String fieldName) {
        return FieldUtils.getSourceFieldValue(fieldName, request, response);
    }

    public LinkNode getCurrentLinkNode() {
        return RequestUtil.getCurrentUrl(request);
    }

    public String getCurrentUrl() {
        LinkNode linkNode = getCurrentLinkNode();
        return linkNode == null ? null : linkNode.getUrl();
    }

    @Nonnull
    public FormatConfig withPattern(String pattern) {
        return new FormatConfig(request, response, pattern);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE).append("pattern", pattern).toString();
    }
}
