/**
 * This document and its contents are protected by copyright 2015 and owned by datatrees.com Inc.
 * The copying and reproduction of this document and/or its content (whether wholly or partly) or
 * any incorporation of the same into any other material in any media or format of any kind is
 * strictly prohibited. All rights are reserved.
 * Copyright (c) datatrees.com Inc. 2015
 */

package com.datatrees.crawler.core.processor.common;

import com.datatrees.common.pipeline.Request;
import com.datatrees.common.pipeline.Response;
import com.treefinance.crawler.framework.expression.StandardExpression;
import com.treefinance.crawler.framework.expression.spring.SpelExpParser;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since 2015年10月21日 下午1:47:57
 */
public final class CalculateUtil {

    private CalculateUtil() {
    }

    public static Double calculate(String expression) {
        return calculate(expression, 0d, Double.class);
    }

    public static <T> T calculate(String expression, T defaultValue, Class<T> clazz) {
        return SpelExpParser.parse(expression, defaultValue, clazz);
    }

    public static <T> T calculate(String expression, Request request, Response response, T defaultValue, Class<T> clazz) {
        String exp = StandardExpression.eval(expression, request, response);
        return calculate(exp, defaultValue, clazz);
    }

    public static Double calculate(String expression, Request request, Double defaultValue) {
        return calculate(expression, request, null, defaultValue, Double.class);
    }

    public static double calculate(String expression, Request request) {
        return calculate(expression, request, 0d);
    }

}
