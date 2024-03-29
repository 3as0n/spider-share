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

package com.treefinance.crawler.framework.protocol.http;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * class to handle HTTP dates. Modified from FastHttpDateFormat.java in jakarta-tomcat.
 * 
 * @author John Xing
 */
public class HttpDateFormat {

    private static final SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);

    /**
     * HTTP date uses TimeZone GMT
     */
    static {
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    // HttpDate (long t) {
    // }

    // HttpDate (String s) {
    // }

    // /**
    // * Get the current date in HTTP format.
    // */
    // public static String getCurrentDate() {
    //
    // long now = System.currentTimeMillis();
    // if ((now - currentDateGenerated) > 1000) {
    // synchronized (format) {
    // if ((now - currentDateGenerated) > 1000) {
    // currentDateGenerated = now;
    // currentDate = format.format(new Date(now));
    // }
    // }
    // }
    // return currentDate;
    //
    // }

    /**
     * Get the HTTP format of the specified date.
     */
    public static String toString(Date date) {
        String string;
        synchronized (format) {
            string = format.format(date);
        }
        return string;
    }

    public static String toString(Calendar cal) {
        String string;
        synchronized (format) {
            string = format.format(cal.getTime());
        }
        return string;
    }

    public static String toString(long time) {
        String string;
        synchronized (format) {
            string = format.format(new Date(time));
        }
        return string;
    }

    public static Date toDate(String dateString) throws ParseException {
        Date date;
        synchronized (format) {
            date = format.parse(dateString);
        }
        return date;
    }

    public static long toLong(String dateString) throws ParseException {
        long time;
        synchronized (format) {
            time = format.parse(dateString).getTime();
        }
        return time;
    }

}
