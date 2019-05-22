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

package com.datatrees.spider.share.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhouxinghai on 2017/5/16.
 */
public class DateUtils {

    /**
     * 时间格式
     */
    public static final String YMDHMS = "yyyy-MM-dd HH:mm:ss";

    public static final String YMD = "yyyy-MM-dd";

    public static final String GMT = "EEE MMM ddHH:mm:ss 'GMT' yyyy";

    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

    private static ThreadLocal<Map<String, SimpleDateFormat>> sfs = new ThreadLocal<Map<String, SimpleDateFormat>>() {
        @Override
        protected Map<String, SimpleDateFormat> initialValue() {
            Map<String, SimpleDateFormat> map = new HashMap<>();
            map.put(YMDHMS, new SimpleDateFormat(YMDHMS));
            map.put(YMD, new SimpleDateFormat(YMD));
            map.put(GMT, new SimpleDateFormat("EEE MMM ddHH:mm:ss 'GMT' yyyy", Locale.US));
            return map;
        }
    };

    /**
     * 返回一个SimpleDateFormat,每个线程只会new一次pattern对应的sdf
     * 
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getDateFormat(final String pattern) {
        Map<String, SimpleDateFormat> tl = sfs.get();
        SimpleDateFormat sdf = tl.get(pattern);
        if (sdf == null) {
            logger.info("{} put new sdf of pattern {} to map", Thread.currentThread().getName(), pattern);
            sdf = new SimpleDateFormat(pattern);
            tl.put(pattern, sdf);
        }
        return sdf;
    }

    /**
     * 格式化
     * 
     * @param date 时间
     * @return
     */
    public static String format(Date date, String pattern) {
        return getDateFormat(pattern).format(date);
    }

    /**
     * 格式化成yyyy-MM-dd HH:mm:ss
     * 
     * @param date 时间
     * @return
     */
    public static String formatYmdhms(Date date) {
        return getDateFormat(YMDHMS).format(date);
    }

    /**
     * 格式化成yyyy-MM-dd HH:mm:ss
     * 
     * @param date 时间
     * @return
     */
    public static String formatYmdhms(long date) {
        return getDateFormat(YMDHMS).format(new Date(date));
    }

    /**
     * 格式化成yyyy-MM-dd
     * 
     * @param date 时间
     * @return
     */
    public static String formatYmd(Date date) {
        return getDateFormat(YMD).format(date);
    }

    /**
     * 计算耗时
     * 
     * @param start
     * @return
     */
    public static String getUsedTime(long start, long end) {
        StringBuilder sb = new StringBuilder();
        long usedTime = end - start;

        long count = TimeUnit.MILLISECONDS.toDays(usedTime);
        if (count > 0) {
            sb.append(count).append("天");
            usedTime -= TimeUnit.DAYS.toMillis(count);
        }

        count = TimeUnit.MILLISECONDS.toHours(usedTime);
        if (count > 0 || sb.length() > 0) {
            usedTime -= TimeUnit.HOURS.toMillis(count);
            sb.append(count).append("小时");
        }

        count = TimeUnit.MILLISECONDS.toMinutes(usedTime);
        if (count > 0) {
            usedTime -= TimeUnit.MINUTES.toMillis(count);
            sb.append(count).append("分");
        }

        count = TimeUnit.MILLISECONDS.toSeconds(usedTime);
        if (count > 0) {
            usedTime -= TimeUnit.SECONDS.toMillis(count);
            sb.append(count).append("秒");
        }
        if (StringUtils.isBlank(sb)) {
            sb.append(usedTime).append("毫秒");
        }
        return sb.toString();
    }

    public static Date parseYmdhms(String dateStr) {
        try {
            return getDateFormat(YMDHMS).parse(dateStr);
        } catch (Exception e) {
            logger.info("parseYmdhms error dateStr={}", dateStr, e);
            return null;
        }
    }

    public static Date parseYmdh(String dateStr) {
        try {
            return getDateFormat(YMD).parse(dateStr);
        } catch (Exception e) {
            logger.info("parseYmdhms error dateStr={}", dateStr, e);
            return null;
        }
    }

    public static Date parse(String dateStr, String pattern) {
        try {
            return getDateFormat(pattern).parse(dateStr);
        } catch (Exception e) {
            logger.info("parseYmdh error dateStr={}", dateStr, e);
            return null;
        }
    }

}
