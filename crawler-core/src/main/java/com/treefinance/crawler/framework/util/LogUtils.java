package com.treefinance.crawler.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Jerry
 * @since 20:09 2018/8/29
 */
public final class LogUtils {

    private LogUtils() {}

    public static Object abbreviate(Object value) {
        return value instanceof String ? StringUtils.abbreviate((String)value, 256) : value;
    }
}
