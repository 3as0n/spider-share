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

package com.treefinance.crawler.framework.process.fields;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.datatrees.common.conf.Configuration;
import com.datatrees.common.protocol.util.CharsetUtil;
import com.datatrees.common.protocol.util.UrlUtils;
import com.datatrees.crawler.core.domain.config.extractor.FieldExtractor;
import com.datatrees.crawler.core.domain.config.extractor.FieldVisibleType;
import com.datatrees.crawler.core.domain.config.extractor.ResultType;
import com.datatrees.crawler.core.domain.config.plugin.AbstractPlugin;
import com.datatrees.crawler.core.processor.AbstractProcessorContext;
import com.datatrees.crawler.core.processor.Constants;
import com.datatrees.crawler.core.processor.bean.LinkNode;
import com.datatrees.crawler.core.processor.common.RequestUtil;
import com.datatrees.crawler.core.processor.common.ResponseUtil;
import com.datatrees.crawler.core.processor.common.exception.FormatException;
import com.datatrees.crawler.core.processor.common.exception.ResultEmptyException;
import com.datatrees.crawler.core.processor.plugin.PluginConstants;
import com.google.common.collect.ImmutableList;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.context.pipeline.SingletonProcessorValve;
import com.treefinance.crawler.framework.expression.StandardExpression;
import com.treefinance.crawler.framework.extension.plugin.PluginCaller;
import com.treefinance.crawler.framework.extension.plugin.PluginUtil;
import com.treefinance.crawler.framework.format.Formatter;
import com.treefinance.crawler.framework.process.ProcessorFactory;
import com.treefinance.crawler.framework.process.operation.OperationPipeline;
import com.treefinance.crawler.framework.util.FieldUtils;
import com.treefinance.crawler.framework.util.SourceUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

/**
 * field extractor should be parallel
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 18, 2014 1:45:17 PM
 */
public class FieldExtractorImpl extends SingletonProcessorValve {

    private final FieldExtractor fieldExtractor;

    public FieldExtractorImpl(@Nonnull FieldExtractor fieldExtractor) {
        this.fieldExtractor = Objects.requireNonNull(fieldExtractor);
    }

    @Override
    protected void initial(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) {
        // reset input content with source id.
        String sourceId = fieldExtractor.getSourceId();
        if (StringUtils.isNotEmpty(sourceId)) {
            Object result = SourceUtils.getSourceFieldValue(sourceId, request, response);
            request.setInput(result != null ? result.toString() : null);
        }

        // encode input content
        String input = (String) request.getInput();
        if (StringUtils.isNotEmpty(input)) {
            String encoding = fieldExtractor.getEncoding();
            logger.debug("input content encoding: {}", encoding);

            Charset charset = CharsetUtil.getCharset(encoding, null);
            if (charset != null) {
                request.setInput(new String(input.getBytes(), charset));
            }
        }
    }

    @Override
    protected boolean isSkipped(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) {
        boolean skipped = super.isSkipped(request, response);

        if (!skipped) {
            FieldExtractResultSet fieldExtractResultSet;
            // if to skip with the stand-by field.
            if (Boolean.TRUE.equals(fieldExtractor.getStandBy()) && (fieldExtractResultSet = ResponseUtil.getFieldExtractResultSet(response)) != null && fieldExtractResultSet.isNotEmptyResult(fieldExtractor.getId())) {
                logger.debug("Skip field extractor with matching the stand-by flag. Field-Extractor: {}", fieldExtractor);
                return true;
            }
        }

        return skipped;
    }

    /**
     * process field extractor field extractor can have multi operation the order of operation is
     * serial field extractor it's self is parallel need PLUGIN_RESULT_MAP for plugin implement and
     * FIELDS_RESULT_MAP for field result map
     */
    @SuppressWarnings("unchecked")
    @Override
    public void process(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {
        logger.debug("start field extractor >>  {}, ", fieldExtractor);

        FieldExtractResultSet fieldExtractResultSet = ResponseUtil.prepareFieldExtractResultSet(response);

        Object fieldValue = this.extract(request, response, fieldExtractResultSet);

        fieldValue = this.defaultValueIfEmpty(fieldValue, request, response, fieldExtractResultSet);

        fieldValue = this.tryResolveUrl(fieldValue, request);

        boolean notEmpty = BooleanUtils.isTrue(fieldExtractor.getNotEmpty());
        if (notEmpty && (fieldValue == null || StringUtils.isEmpty(fieldValue.toString()))) {
            throw new ResultEmptyException(fieldExtractor + " >> result should not be Empty!");
        }

        String id = fieldExtractor.getId();
        FieldExtractResult fieldExtractResult = new FieldExtractResult(fieldExtractor, fieldValue);
        fieldExtractResultSet.put(id, fieldExtractResult);

        // set field result visible
        FieldVisibleType fieldVisibleType = fieldExtractor.getFieldVisibleType();
        if (fieldVisibleType != null) {
            switch (fieldVisibleType) {
                case PROCESSOR_RESULT:
                    request.addResultScope(id, fieldValue);
                    break;
                case CONTEXT:
                    request.addContextScope(id, fieldValue);
                    break;
                default:
                    request.addVisibleScope(id, fieldValue);
                    break;
            }
        }

        if (notEmpty) {
            logger.info("end not-empty field extractor result: {}", fieldExtractResult);
        } else {
            logger.debug("end field extractor result: {}", fieldExtractResult);
        }
    }

    @Nullable
    private Object tryResolveUrl(Object fieldValue, @Nonnull SpiderRequest request) {
        try {
            if (fieldValue instanceof String && fieldExtractor.getField().contains(Constants.CRAWLER_URL_FIELD)) {
                LinkNode current = RequestUtil.getCurrentUrl(request);
                if (current != null) {
                    String baseURL = (StringUtils.isNotEmpty(current.getBaseUrl()) ? current.getBaseUrl() : current.getUrl());
                    logger.debug("resolve url: {}, base-url: {}", fieldValue, baseURL);
                    return UrlUtils.resolveUrl(baseURL, (String) fieldValue);
                }
            }
            return fieldValue;
        } catch (Exception e) {
            logger.warn("error resolving url for field result: {} ", fieldValue, e);
        }
        return null;
    }

    @Nullable
    private Object extract(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response, FieldExtractResultSet fieldExtractResultSet) throws ResultEmptyException {
        try {
            if (StringUtils.isEmpty((String) request.getInput())) {
                logger.warn("Skip field extract processing with the empty input. fieldExtractor: {}", fieldExtractor);
                return null;
            }

            Object fieldResult;
            AbstractPlugin plugin = fieldExtractor.getPlugin();
            if (plugin != null) {
                fieldResult = this.extractWithPlugin(plugin, request);
            } else {
                OperationPipeline pipeline = new OperationPipeline(fieldExtractor);
                fieldResult = pipeline.start(request, fieldExtractResultSet);
            }

            // format
            return this.format(fieldResult, fieldExtractor.getResultType(), fieldExtractor.getFormat(), request, response);
        } catch (ResultEmptyException e) {
            throw e;
        } catch (Exception e) {
            logger.warn("Error processing field extractor: {}", fieldExtractor, e);
        }
        return null;
    }

    private Object extractWithPlugin(AbstractPlugin pluginDesc, SpiderRequest request) {
        AbstractProcessorContext context = request.getProcessorContext();

        Object fieldResult = PluginCaller.call(pluginDesc, context, () -> {
            Map<String, String> params = new HashMap<>();

            params.put(PluginConstants.PAGE_CONTENT, (String) request.getInput());
            LinkNode requestLinkNode = RequestUtil.getCurrentUrl(request);
            if (requestLinkNode != null) {
                params.put(PluginConstants.CURRENT_URL, requestLinkNode.getUrl());
                params.put(PluginConstants.REDIRECT_URL, requestLinkNode.getRedirectUrl());
            }
            params.put(PluginConstants.FIELD, fieldExtractor.getField());

            return params;
        });

        // get pluginDesc json result
        Map<String, Object> pluginResultMap = PluginUtil.checkPluginResult((String) fieldResult);

        return pluginResultMap.get(PluginConstants.FIELD);
    }

    private Object format(Object fieldValue, ResultType type, String formatPattern, SpiderRequest request, SpiderResponse response) throws FormatException {
        if (fieldValue instanceof String && type != null) {
            Configuration conf = request.getConfiguration();
            Formatter formatter = ProcessorFactory.getFormatter(type, conf);
            return formatter.format((String) fieldValue, formatPattern, request, response);
        }
        return fieldValue;
    }

    private Object defaultValueIfEmpty(Object fieldValue, SpiderRequest request, SpiderResponse response, FieldExtractResultSet fieldExtractResultSet) {
        String defaultValue = fieldExtractor.getDefaultValue();
        if (defaultValue != null && FieldUtils.isNullOrEmptyString(fieldValue)) {
            Object result;
            ResultType type = fieldExtractor.getResultType();
            if (ResultType.String.equals(type)) {
                result = StandardExpression.eval(defaultValue, ImmutableList.of(fieldExtractResultSet.resultMap(), request.getGlobalScopeAsMap()));
            } else {
                String val = type != null ? StringUtils.trim(defaultValue) : defaultValue;
                result = StandardExpression.evalWithObject(val, ImmutableList.of(fieldExtractResultSet.resultMap(), request.getGlobalScopeAsMap()));
            }
            try {
                return this.format(result, type, fieldExtractor.getFormat(), request, response);
            } catch (Exception e) {
                logger.warn("Error formatting default value for field: {}", fieldExtractor, e);
                return null;
            }
        }
        return fieldValue;
    }

}
