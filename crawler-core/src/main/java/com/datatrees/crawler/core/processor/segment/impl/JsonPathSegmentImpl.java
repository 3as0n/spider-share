package com.datatrees.crawler.core.processor.segment.impl;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

import com.datatrees.common.pipeline.Request;
import com.datatrees.common.pipeline.Response;
import com.datatrees.crawler.core.domain.config.segment.impl.JsonPathSegment;
import com.datatrees.crawler.core.processor.segment.SegmentBase;
import com.datatrees.crawler.core.util.json.JsonPathUtil;
import com.treefinance.crawler.framework.expression.StandardExpression;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Jerry
 * @datetime 2015-07-17 19:45
 */
public class JsonPathSegmentImpl extends SegmentBase<JsonPathSegment> {

    public JsonPathSegmentImpl(@Nonnull JsonPathSegment segment) {
        super(segment);
    }

    @Override
    protected List<String> splitInputContent(String content, JsonPathSegment segment, Request request, Response response) {
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
