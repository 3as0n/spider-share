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

package com.treefinance.crawler.framework.consts;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Mar 12, 2014 3:29:12 PM
 */
public final class Status {

    public static final int PROCESS_EXCEPTION = -2005;

    public static final int BLOCKED = -2004;

    public static final int NO_SEARCH_RESULT = -2003;

    public static final int FILTERED = -2002;

    public static final int LAST_PAGE = -2000;

    public static final int VISIT_SUCCESS = 1;

    public static final int VISIT_REDIRECT = 2;

    public static final int VISIT_ERROR = 3;

    public static final int NO_PROXY = 4;

    public static final int REQUEUE = 1999;

    private Status() {}

    public static boolean success(int code) {
        return code == VISIT_SUCCESS;
    }

    public static boolean serverError(int code) {
        return code == VISIT_ERROR;
    }

    public static boolean blocked(int code) {
        return code == BLOCKED;
    }

    public static String format(int code) {
        String result;
        switch (code) {
            case BLOCKED:
                result = "BLOCKED";
                break;
            case NO_SEARCH_RESULT:
                result = "SEARCH RESULT NOT FOUND";
                break;
            case LAST_PAGE:
                result = "LAST SEARCH PAGE";
                break;
            case VISIT_ERROR:
                result = "VISIT PAGE ERROR";
                break;
            default:
                result = "SUCCESS";
                break;
        }
        return result;
    }
}
