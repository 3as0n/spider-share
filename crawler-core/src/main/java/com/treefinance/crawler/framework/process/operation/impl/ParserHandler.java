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

package com.treefinance.crawler.framework.process.operation.impl;

import com.alibaba.fastjson.JSON;
import com.datatrees.common.util.GsonUtils;
import com.google.common.collect.ImmutableList;
import com.google.gson.reflect.TypeToken;
import com.treefinance.crawler.framework.config.xml.parser.IndexMapping;
import com.treefinance.crawler.framework.config.xml.parser.Parser;
import com.treefinance.crawler.framework.config.xml.parser.ParserPattern;
import com.treefinance.crawler.framework.config.xml.service.AbstractService;
import com.treefinance.crawler.framework.consts.Constants;
import com.treefinance.crawler.framework.context.AbstractProcessorContext;
import com.treefinance.crawler.framework.context.function.LinkNode;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.context.pipeline.InvokeException;
import com.treefinance.crawler.framework.exception.InvalidOperationException;
import com.treefinance.crawler.framework.exception.ResultEmptyException;
import com.treefinance.crawler.framework.expression.ExpressionParser;
import com.treefinance.crawler.framework.util.ServiceUtils;
import com.treefinance.crawler.framework.util.UrlExtractor;
import com.treefinance.toolkit.util.RegExp;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * parser segment content and send request or extract urls
 * 
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 20, 2014 8:57:12 PM
 */
class ParserHandler {

    private static final Logger logger = LoggerFactory.getLogger(ParserHandler.class);
    private final Parser parser;
    private boolean needRequest = false;
    private boolean needReturnUrlList = false;

    public ParserHandler(Parser parser, boolean needRequest) {
        this(parser, needRequest, false);
    }

    public ParserHandler(Parser parser, boolean needRequest, boolean needReturnUrlList) {
        this.parser = parser;
        this.needRequest = needRequest;
        this.needReturnUrlList = needReturnUrlList;
    }

    public Object parse(@Nonnull String content, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {
        String template = parser.getUrlTemplate();
        if (StringUtils.isEmpty(template)) {
            throw new InvalidOperationException("Invalid parser operation! - 'parser/url-template' must not be empty!");
        }

        logger.info("parser's url-template: {}", template);

        String refererTemplate = StringUtils.defaultString(parser.getLinkUrlTemplate());
        logger.info("parser's referer-template: {}", refererTemplate);
        String headers = StringUtils.defaultString(parser.getHeaders());
        logger.info("parser's header: {}", headers);
        String complexSource = ParserURLCombiner.encodeUrl(template, refererTemplate, headers);

        List<String> results = evalExp(complexSource, request, response, content);

        logger.info("after template combine: {}", results);
        if (isNeedRequest()) {
            if (parser.getSleepSecond() != null && parser.getSleepSecond() > 0) {
                logger.info("sleep {}s before parser request.", parser.getSleepSecond());
                TimeUnit.SECONDS.sleep(parser.getSleepSecond());
            }

            return sendRequest(results.get(0), request);
        } else if (isNeedReturnUrlList()) {
            // support multi parsers
            return results;
        } else {
            return results.get(0);
        }
    }

    public boolean isNeedRequest() {
        return needRequest;
    }

    public void setNeedRequest(boolean needRequest) {
        this.needRequest = needRequest;
    }

    public Parser getParser() {
        return parser;
    }

    public boolean isNeedReturnUrlList() {
        return needReturnUrlList;
    }

    public void setNeedReturnUrlList(boolean needReturnUrlList) {
        this.needReturnUrlList = needReturnUrlList;
    }

    /**
     * First: replaced from visible field scope; Second: replaced by the matched string in input content by regexp
     * Third: replaced by global visible field context;
     */
    private List<String> evalExp(String url, SpiderRequest request, SpiderResponse response, String content) {
        ExpressionParser parser = ExpressionParser.parse(url);
        String expUrl = parser.evalUrl(request, response, false, true);
        parser.reset(expUrl);

        if (parser.findExp()) {
            List<Map<String, Object>> fieldScopes = findByRegex(content);

            Map<String, Object> globalScope = request.getGlobalScopeAsMap();
            if (CollectionUtils.isNotEmpty(fieldScopes)) {
                List<String> list = fieldScopes.stream().map(fieldScope -> {
                    try {
                        return parser.evalExp(ImmutableList.of(fieldScope, globalScope), true, false);
                    } catch (Exception e) {
                        logger.warn("Error eval expression with url: {}, fieldScope: {}", expUrl, JSON.toJSONString(fieldScope));
                    }
                    return StringUtils.EMPTY;
                }).filter(item -> !item.isEmpty()).distinct().collect(Collectors.toList());

                if (!list.isEmpty()) {
                    return list;
                }
            }

            // if not find field result using default input context
            return Collections.singletonList(parser.evalExp(globalScope, true, false));
        }

        return Collections.singletonList(expUrl);
    }

    private List<Map<String, Object>> findByRegex(String content) {
        List<ParserPattern> mappings = parser.getPatterns();
        if (CollectionUtils.isEmpty(mappings)) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> fieldScopes = new ArrayList<>();
        for (ParserPattern parserPattern : mappings) {
            String regex = parserPattern.getRegex();
            if (StringUtils.isEmpty(regex))
                continue;

            List<IndexMapping> indexMappings = parserPattern.getIndexMappings();
            if (CollectionUtils.isEmpty(indexMappings))
                continue;

            Matcher m = RegExp.getMatcher(regex, content);
            int i = 0;
            while (m.find()) {
                Map<String, Object> indexFieldMap;
                if (fieldScopes.size() > i) {
                    indexFieldMap = fieldScopes.get(i);
                } else {
                    indexFieldMap = new HashMap<>();
                    fieldScopes.add(indexFieldMap);
                }

                for (IndexMapping indexMapping : indexMappings) {
                    if (StringUtils.isEmpty(indexMapping.getPlaceholder()))
                        continue;

                    try {
                        indexFieldMap.put(indexMapping.getPlaceholder(), m.group(indexMapping.getGroupIndex()));
                    } catch (Exception e) {
                        logger.error("Error mapping with the parser regex. - regex: {}", regex, e);
                    }
                }
                i++;
            }
        }

        logger.debug("Field scope size: {}", fieldScopes.size());

        return fieldScopes;
    }

    private String sendRequest(String complexUrl, SpiderRequest request) throws InvokeException, ResultEmptyException {
        String datas[] = ParserURLCombiner.decodeParserUrl(complexUrl);
        String url = datas[0];
        String referer = datas[1];
        String headers = datas[2];

        List<String> urls = UrlExtractor.extract(url);
        if (urls.size() != 1) {
            logger.warn("url was not formatted in parser request! >> {}", url);
            return url;
        }

        LinkNode currentLinkNode = new LinkNode(url);
        // add json headers
        if (StringUtils.isNotEmpty(headers)) {
            Map<String, String> headersMap = GsonUtils.fromJson(headers, new TypeToken<Map<String, String>>() {}.getType());
            currentLinkNode.addHeaders(headersMap);
        }
        // add referer
        if (StringUtils.isNotEmpty(referer)) {
            currentLinkNode.addHeader(Constants.HTTP_HEADER_REFERER, referer);
        }

        AbstractProcessorContext processorContext = request.getProcessorContext();
        AbstractService service = processorContext.getDefaultService();

        return ServiceUtils.invokeAsString(service, currentLinkNode, processorContext, request.getConfiguration(), request.getVisibleScope());
    }

}
