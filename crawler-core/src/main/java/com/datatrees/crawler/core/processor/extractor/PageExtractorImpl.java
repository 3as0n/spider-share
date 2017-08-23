/**
 * This document and its contents are protected by copyright 2005 and owned by datatrees.com Inc.
 * The copying and reproduction of this document and/or its content (whether wholly or partly) or
 * any incorporation of the same into any other material in any media or format of any kind is
 * strictly prohibited. All rights are reserved.
 * Copyright (c) datatrees.com Inc. 2015
 */

package com.datatrees.crawler.core.processor.extractor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import com.datatrees.common.pipeline.Request;
import com.datatrees.common.pipeline.Response;
import com.datatrees.common.util.ReflectionUtils;
import com.datatrees.crawler.core.domain.config.page.impl.PageExtractor;
import com.datatrees.crawler.core.domain.config.segment.AbstractSegment;
import com.datatrees.crawler.core.processor.Constants;
import com.datatrees.crawler.core.processor.common.Processor;
import com.datatrees.crawler.core.processor.common.ProcessorFactory;
import com.datatrees.crawler.core.processor.common.ResponseUtil;
import com.datatrees.crawler.core.processor.common.exception.ResultEmptyException;
import com.datatrees.crawler.core.processor.extractor.source.PageSourceImpl;
import com.datatrees.crawler.core.processor.segment.SegmentBase;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月14日 下午9:14:20
 */
public class PageExtractorImpl extends Processor {

    private static final Logger log = LoggerFactory.getLogger(PageExtractorImpl.class);
    private PageExtractor pageExtractor;

    /**
     * @param pageExtractor
     */
    public PageExtractorImpl(PageExtractor pageExtractor) {
        super();
        this.pageExtractor = pageExtractor;
    }

    @Override
    public void process(Request request, Response response) throws Exception {
        PageSourceImpl pageSourceImpl = new PageSourceImpl(pageExtractor.getPageSourceList());
        pageSourceImpl.invoke(request, response);

        List<AbstractSegment> segments = pageExtractor.getSegmentList();
        if (CollectionUtils.isNotEmpty(segments)) {
            // extract objs with segment
            this.extractObjectsWithSegments(segments, request, response);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void segResultConvert(Map segmentResultMap, Object segResult, AbstractSegment abstractSegment) {
        Object value = segmentResultMap.get(abstractSegment.getName());
        if (value == null) {
            segmentResultMap.put(abstractSegment.getName(), segResult);
        } else {
            if (value instanceof Collection) {
                if (segResult instanceof Collection) {
                    ((Collection) value).addAll((Collection) segResult);
                } else {
                    ((Collection) value).add(segResult);
                }
            } else {
                List newValue = new ArrayList();
                if (segResult instanceof Collection) {
                    newValue.addAll((Collection) segResult);
                } else {
                    newValue.add(segResult);
                }
                segmentResultMap.put(abstractSegment.getName(), newValue);
            }
        }

    }

    @SuppressWarnings("rawtypes")
    private void extractObjectsWithSegments(List<AbstractSegment> segments, Request req, Response resp) throws ResultEmptyException {
        log.info("extractObjectsWithSegments: segment size.." + segments.size());
        Map segmentResultMap = new HashMap();
        for (AbstractSegment abstractSegment : segments) {
            try {
                Response segResponse = Response.build();
                SegmentBase segmentBase = ProcessorFactory.getSegment(abstractSegment);

                segmentBase.invoke(req, segResponse);

                Object segResult = ResponseUtil.getSegmentsResults(segResponse);

                this.segResultConvert(segmentResultMap, segResult, abstractSegment);

            } catch (Exception e) {
                if (e instanceof ResultEmptyException) {
                    throw (ResultEmptyException) e;
                } else {
                    log.error("invoke segment processor error!", e);
                }
            }
        }

        ResponseUtil.setResponsePageExtractResultMap(resp, segmentResultMap);
    }

    @SuppressWarnings({"unused", "rawtypes", "unchecked"})
    private Object resultMapConvert(Map<String, Object> resultMap) {
        String className = null;
        try {
            if (resultMap == null || resultMap.isEmpty() || StringUtils.isBlank((className = (String) resultMap.get(Constants.SEGMENT_RESULT_CLASS_NAMES)))) {
                return resultMap;
            } else {
                // Reflect to class instance
                Object instance = ReflectionUtils.newInstance(className);
                if (instance instanceof Map) {// no need to Convert
                    resultMap.remove(Constants.SEGMENT_RESULT_CLASS_NAMES);
                    ((Map) instance).putAll(resultMap);
                    return instance;
                }
                Class userClass = (Class) instance.getClass();
                Field[] fs = userClass.getDeclaredFields();
                for (int i = 0; i < fs.length; i++) {
                    Field f = fs[i];
                    f.setAccessible(true); // set Accessible
                    int fieldModifiers = f.getModifiers();
                    if ((fieldModifiers & Modifier.FINAL) == Modifier.FINAL) {
                        continue;
                    }
                    Object value = resultMap.get(f.getName());
                    if (value instanceof Map) {// no need to Convert
                        value = resultMapConvert((Map<String, Object>) value);
                    }

                    f.set(instance, value);// set value
                    if (log.isDebugEnabled()) {
                        log.debug("set name:" + f.getName() + "\t value = " + resultMap.get(f.getName()));
                    }
                }
                return instance;
            }
        } catch (Exception e) {
            log.error(resultMap + " convert to " + className + " error " + e.getMessage());
            return resultMap;
        }
    }
}
