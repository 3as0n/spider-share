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

public class BooleanUtils {

    /**
     * 是否正数
     * 
     * @param number
     * @return
     */
    public static Boolean isPositiveNumber(Number number) {
        if (null == number) {
            return false;
        }
        return number.doubleValue() > 0;
    }

    /**
     * 是否非正数
     * 
     * @param number
     * @return
     */
    public static Boolean isNotPositiveNumber(Number number) {
        return !isPositiveNumber(number);
    }
}
