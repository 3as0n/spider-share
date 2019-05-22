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

package com.datatrees.spider.share.common.utils;

import com.datatrees.spider.share.domain.ErrorCode;
import org.apache.commons.lang3.StringUtils;

/**
 * 检查 Created by zhouxinghai on 2017/6/29.
 */
public class CheckUtils {

    public static void checkNotNull(Object param, String errorMsg) {
        if (null == param) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    public static void checkNotBlank(String param, String errorMsg) {
        if (StringUtils.isBlank(param)) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    public static void checkNotBlank(String param, ErrorCode errorCode) {
        if (StringUtils.isBlank(param)) {
            throw new IllegalArgumentException(errorCode.getErrorMsg());
        }
    }

    public static void checkNotPositiveNumber(Number number, ErrorCode errorCode) {
        if (null == number || number.longValue() <= 0) {
            throw new IllegalArgumentException(errorCode.getErrorMsg());
        }
    }

    public static void checkNotPositiveNumber(Number number, String errorMsg) {
        if (null == number || number.longValue() <= 0) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

}
