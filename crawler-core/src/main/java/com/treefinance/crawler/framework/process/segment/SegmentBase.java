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

package com.treefinance.crawler.framework.process.segment;

import com.treefinance.crawler.framework.config.enums.fields.ResultType;
import com.treefinance.crawler.framework.config.xml.extractor.FieldExtractor;
import com.treefinance.crawler.framework.config.xml.segment.AbstractSegment;
import com.treefinance.crawler.framework.consts.Constants;
import com.treefinance.crawler.framework.context.AbstractProcessorContext;
import com.treefinance.crawler.framework.context.RequestUtil;
import com.treefinance.crawler.framework.context.ResponseUtil;
import com.treefinance.crawler.framework.context.SearchProcessorContext;
import com.treefinance.crawler.framework.context.control.BusinessTypeDecider;
import com.treefinance.crawler.framework.context.function.LinkNode;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.context.function.SpiderResponseFactory;
import com.treefinance.crawler.framework.context.pipeline.FailureSkipProcessorValve;
import com.treefinance.crawler.framework.exception.FormatException;
import com.treefinance.crawler.framework.exception.ResultEmptyException;
import com.treefinance.crawler.framework.expression.StandardExpression;
import com.treefinance.crawler.framework.format.Formatter;
import com.treefinance.crawler.framework.process.ProcessorFactory;
import com.treefinance.crawler.framework.process.SpiderRequestHelper;
import com.treefinance.crawler.framework.process.SpiderResponseHelper;
import com.treefinance.crawler.framework.process.domain.ExtractObject;
import com.treefinance.crawler.framework.process.domain.SegmentExtractObject;
import com.treefinance.crawler.framework.process.fields.FieldExtractResultSet;
import com.treefinance.crawler.framework.process.fields.FieldExtractorPipeline;
import com.treefinance.crawler.framework.process.operation.impl.ParserURLCombiner;
import com.treefinance.crawler.framework.protocol.util.HeaderParser;
import com.treefinance.crawler.framework.protocol.util.UrlUtils;
import com.treefinance.crawler.framework.util.FieldUtils;
import com.treefinance.crawler.framework.util.LogUtils;
import com.treefinance.crawler.framework.util.ObjectUtils;
import com.treefinance.toolkit.util.RegExp;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @param <>
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 24, 2014 5:19:42 PM
 */
public abstract class SegmentBase<T extends AbstractSegment> extends FailureSkipProcessorValve {

    private final T segment;
    private List<String> splits = null;
    private AbstractProcessorContext context;

    public SegmentBase(@Nonnull T segment) {
        this.segment = Objects.requireNonNull(segment);
    }

    @Override
    public final void process(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {
        List<String> splits = getSplits(request, response);

        if (CollectionUtils.isEmpty(splits)) {
            if (Boolean.TRUE.equals(segment.getNotEmpty())) {
                throw new ResultEmptyException(segment + " result should not be Empty!");
            }
        } else {
            logger.info("{} begin process, split size: {}", segment, splits.size());

            if (Boolean.TRUE.equals(segment.getIsReverse())) {
                Collections.reverse(splits);
            }

            List<ExtractObject> resultList = doProcess(splits, request, response);

            Object result = adaptResult(resultList);
            if (result != null) {
                response.setOutPut(result);
            }
        }

        if (SpiderRequestHelper.isKeepSegmentProcessingData(request)) {
            SpiderResponseHelper.setSegmentProcessingData(response, splits);
        }
    }

    public T getSegment() {
        return segment;
    }

    @Override
    protected void initial(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) {
        this.context = Objects.requireNonNull(request.getProcessorContext());
        String sourceId = segment.getSourceId();
        if (StringUtils.isNotEmpty(sourceId)) {
            Object result = FieldUtils.getSourceFieldValue(sourceId, request, response);
            logger.info("Will use source input instead of stdin for segment processor. segment: {}, sourceId: {}, input: {}", segment.getName(), sourceId,
                LogUtils.abbreviate(result));
            // TODO: 2018/8/21 由于历史配置不严谨暂时采用兼容做法，解析结果不可控
            if (result != null) {
                logger.info("Segment source input is available! segment: {}, sourceId: {}", segment.getName(), sourceId);
                request.setInput(result.toString());
            }
        }
    }

    @Override
    protected boolean isSkipped(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) {
        if (ObjectUtils.isNullOrEmptyString(request.getInput())) {
            logger.warn("Empty input content used for segment processing and skip. segment: {}, taskId: {}", segment.getName(), context.getTaskId());
            return true;
        }

        String businessType = segment.getBusinessType();
        if (!BusinessTypeDecider.support(businessType, context)) {
            logger.warn("Business forbidden in segment processing and skip. segment: {}, businessType: {}, taskId: {}", segment.getName(), businessType, context.getTaskId());
            return true;
        }

        return false;
    }

    protected abstract List<String> splitInputContent(String content, T segment, SpiderRequest request, SpiderResponse response);

    private Object adaptResult(List<ExtractObject> resultList) {
        if (CollectionUtils.isNotEmpty(resultList)) {
            // result merge
            if (Boolean.TRUE.equals(segment.getMerge())) {
                CollectionUtils.filter(resultList, new UniquePredicate());
            }

            // get pop return
            if (Boolean.TRUE.equals(segment.getPopReturn())) {
                // result reset
                return resultList.get(0);
            }
        } else if (Boolean.TRUE.equals(segment.getPopReturn())) {
            // result reset
            return null;
        }

        return resultList;
    }

    private List<ExtractObject> doProcess(List<String> splits, SpiderRequest request, SpiderResponse response) throws Exception {
        List<ExtractObject> resultList = new ArrayList<>();

        Integer maxCycles = segment.getMaxCycles();

        logger.debug("Segment-break pattern: {}", segment.getBreakPattern());
        String breakPattern = StandardExpression.eval(segment.getBreakPattern(), request, response);
        logger.debug("Segment-dis-contains pattern: {}", segment.getDisContains());
        String disContains = StandardExpression.eval(segment.getDisContains(), request, response);
        logger.debug("Segment-contains pattern: {}", segment.getContains());
        String contains = StandardExpression.eval(segment.getContains(), request, response);

        for (String split : splits) {
            if (matches(split, breakPattern, segment.getBreakPatternFlag(), false)) {
                logger.warn("Break segment content processing with the given break pattern: {}", breakPattern);
                break;
            }
            if (matches(split, disContains, segment.getDisContainsFlag(), false)) {
                logger.warn("Skip segment content processing with the given dis-contains pattern: {}", disContains);
                continue;
            }
            if (matches(split, contains, segment.getContainsFlag(), true)) {
                logger.warn("Skip segment content processing with the mismatch contains pattern: {}", contains);
                continue;
            }

            ExtractObject extractObject = new SegmentExtractObject(segment.getName(), segment.getResultClass());

            // extract field
            FieldExtractResultSet fieldExtractResultSet = processFields(split, request, response, extractObject);
            // extract segment object
            processChildSegments(split, fieldExtractResultSet, request, extractObject);

            addExtractObject(resultList, extractObject, request, response);

            if (maxCycles > 0 && resultList.size() >= maxCycles) {
                logger.warn("Break segment content processing with reaching the limit cycles{} reach the maxCycles: {},total-size: {}", segment, maxCycles, splits.size());
                break;
            }
        }

        if (Boolean.TRUE.equals(segment.getNotEmpty()) && resultList.isEmpty()) {
            throw new ResultEmptyException("Segment processing result must not be Empty! - segment: " + segment);
        }

        return resultList;
    }

    private void processChildSegments(String split, FieldExtractResultSet fieldExtractResultSet, SpiderRequest request, ExtractObject extractObject) throws ResultEmptyException {
        Map<String, Object> fieldExtractResultMap = null;
        if (fieldExtractResultSet != null) {
            fieldExtractResultMap = fieldExtractResultSet.resultMap();
        }
        List<AbstractSegment> segments = segment.getSegmentList();
        for (AbstractSegment childSegment : segments) {
            if (BooleanUtils.isTrue(childSegment.getStandBy()) && extractObject.isValid(childSegment.getName())) {
                logger.info("Skip segment processor with flag 'stand-by'. segment: {}", childSegment);
                continue;
            }

            Object segResultList;
            try {
                request.setInput(split);
                request.addLocalScope(fieldExtractResultMap);

                SpiderResponse segResponse = SpiderResponseFactory.make();

                SegmentBase segmentBase = ProcessorFactory.getSegment(childSegment);
                segmentBase.invoke(request, segResponse);

                segResultList = segResponse.getOutPut();

            } catch (ResultEmptyException e) {
                throw e;
            } catch (Exception e) {
                logger.error("invoke segment processor error!", e);
                segResultList = null;
            }

            extractObject.setFieldExtractValue(childSegment.getName(), segResultList);
        }
    }

    private FieldExtractResultSet processFields(String split, SpiderRequest request, SpiderResponse response, ExtractObject extractObject) throws ResultEmptyException {
        List<FieldExtractor> fieldExtractorList = segment.getFieldExtractorList();
        FieldExtractorPipeline pipeline = new FieldExtractorPipeline(fieldExtractorList, context);

        request.setInput(split);
        pipeline.invokeQuietly(request, response);

        // get response result
        FieldExtractResultSet fieldExtractResultSet = ResponseUtil.removeFieldExtractResultSet(response);
        if (fieldExtractResultSet != null) {
            fieldExtractResultSet.forEach((id, fieldExtractResult) -> extractObject.setFieldExtractResult(fieldExtractResult));
        }

        return fieldExtractResultSet;
    }

    @SuppressWarnings("unchecked")
    private void addExtractObject(@Nonnull List<ExtractObject> resultList, ExtractObject extractObject, SpiderRequest request, SpiderResponse response) {
        extractObject.consumeWithFlatObjects(object -> {
            String resultClass = object.getResultClass();
            ResultType resultType = ResultType.getResultType(resultClass);
            if (resultType != null) {
                // 可格式化类型，扁平化对象处理
                ExtractObject flatExtractObject = wrapWithFlatExtractObject(object, resultType, request, response);
                if (flatExtractObject != null) {
                    resultList.add(flatExtractObject);
                }
            } else if (StringUtils.isNotEmpty(resultClass)) {
                try {
                    if (resultClass.equals(LinkNode.class.getSimpleName()) || resultClass.equals(LinkNode.class.getName())) {
                        if (request.getProcessorContext() instanceof SearchProcessorContext) {
                            LinkNode current = RequestUtil.getCurrentUrl(request);
                            if (current != null) {
                                String baseURL = (StringUtils.isNotEmpty(current.getBaseUrl()) ? current.getBaseUrl() : current.getUrl());
                                addLinkNodes(resultList, object, baseURL, current.getUrl());
                            }
                        }
                        return;
                    } else if (resultClass.contains(".")) {
                        Class<?> clazz = Class.forName(resultClass);
                        Object instance = clazz.newInstance();

                        if (instance instanceof Map) {
                            ((Map)instance).putAll(object);
                        } else {
                            Field[] fields = clazz.getDeclaredFields();
                            for (Field field : fields) {
                                Object value = object.get(field.getName());
                                if (value != null) {
                                    field.setAccessible(true);
                                    field.set(instance, value);
                                    logger.debug("set name: {}\t value = {}", field.getName(), value);
                                }
                            }
                        }
                        resultList.add(object.withFlatField(instance));
                        return;
                    }
                } catch (ClassNotFoundException e) {
                    logger.warn("Can not recognize segment result class: {}", resultClass);
                } catch (Exception e) {
                    logger.error("{} convert to {} error.", extractObject, resultClass, e);
                }
                resultList.add(object);
            } else {
                resultList.add(object);
            }
        });
    }

    private void addLinkNodes(List<ExtractObject> resultList, ExtractObject extractObject, String baseURL, String referer) {
        HashMap<String, Object> map = new HashMap<>(extractObject);
        Object urlObj = map.remove(Constants.CRAWLER_URL_FIELD);
        if (urlObj instanceof String) {
            logger.debug("normal string url {}", urlObj);
            String url = (String)urlObj;
            String resolvedUrl = UrlUtils.resolveUrl(baseURL, url);
            if (!resolvedUrl.isEmpty()) {
                logger.debug("parse url to link node: {}", resolvedUrl);
                LinkNode tmp = new LinkNode(resolvedUrl);
                tmp.setFromParser(true);
                tmp.addPropertys(map);
                tmp.setReferer(referer);
                resultList.add(extractObject.withFlatField(tmp));
            }
        } else if (urlObj instanceof List) {
            Map<String, ExtractObject> linkNodes = new HashMap<>();
            List<String> urlsList = (List<String>)urlObj;
            logger.debug("segment url size... {} {}", urlsList.size(), baseURL);
            for (String url : urlsList) {
                String[] pairs = ParserURLCombiner.decodeParserUrl(url);
                String u = pairs[0];
                String refer = pairs[1];
                String headers = pairs[2];
                String resolvedUrl = UrlUtils.resolveUrl(baseURL, u);
                if (!resolvedUrl.isEmpty()) {
                    logger.debug("parse url to link node: {}", resolvedUrl);
                    LinkNode tmp = new LinkNode(resolvedUrl);
                    tmp.setFromParser(true);
                    tmp.addPropertys(map);
                    tmp.setReferer(StringUtils.defaultIfEmpty(refer, referer));
                    if (StringUtils.isNotEmpty(headers)) {
                        tmp.addHeaders(HeaderParser.getHeaderMaps(headers));
                    }
                    linkNodes.put(resolvedUrl, extractObject.withFlatField(tmp));
                }
            }
            resultList.addAll(linkNodes.values());
        } else if (urlObj == null) {
            logger.warn("Not found field 'url' to make link node. base-url: {}", baseURL);
        }
    }

    private ExtractObject wrapWithFlatExtractObject(ExtractObject extractObject, ResultType resultType, SpiderRequest request, SpiderResponse response) {
        if (extractObject.size() == 1) {
            // convert to basic result type
            Collection<Object> values = extractObject.values();
            Object value = values.toArray()[0];
            if (value == null) {
                logger.warn("Empty segment extract object. result-type: {}, value-type: {}", resultType);
            } else {
                Formatter formatter = ProcessorFactory.getFormatter(resultType, request.getConfiguration());
                if (formatter.supportResultType(value)) {
                    logger.info("The field value was matching with the segment's result type and return directly. result-type: {}, value: {}", resultType, value);
                    return extractObject.withFlatField(value);
                } else if (value instanceof String) {
                    try {
                        value = formatter.format((String)value, null, request, response);
                        if (value != null) {
                            return extractObject.withFlatField(value);
                        }
                    } catch (FormatException e) {
                        logger.error("Error formatting segment's extract value. result-type: {}, value: {}", resultType, value, e);
                    }
                } else {
                    logger.error("Unsupported segment extract value type. result-type: {}, value-type: {}, value: {}", resultType, value.getClass(), value);
                }
            }
        } else {
            logger.error("Invalid segment extract object. result-type: {}, extractObject: {}", resultType, extractObject.fieldNames());
        }

        return null;
    }

    @Nonnull
    private List<String> getSplits(SpiderRequest request, SpiderResponse response) {
        if (splits == null) {
            splits = splitInputContent((String)request.getInput(), segment, request, response);
            if (splits == null) {
                splits = Collections.emptyList();
            }
        }
        return splits;
    }

    private boolean matches(String content, String pattern, Integer patternFlag, boolean reverse) {
        if (StringUtils.isBlank(pattern)) {
            return false;
        }

        return reverse ^ RegExp.find(content, pattern, patternFlag != null ? patternFlag : 0);
    }

}
