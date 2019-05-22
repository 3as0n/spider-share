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

package com.datatrees.spider.share.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuminlang on 15/7/29.
 */
public enum ExtractCode {

    EXTRACT_SUCCESS(0, "Extract success"),
    EXTRACT_CONF_FAIL(2, "No conf or conf parse fail"),
    ERROR_INPUT(4, "Extract error input"),
    EXTRACT_FAIL(6, "Extract fail code"),
    EXTRACT_STORE_FAIL(8, "Extract store failed");

    private static Map<Integer, ExtractCode> extractCodeMap = new HashMap<Integer, ExtractCode>();

    static {
        for (ExtractCode obj : values()) {
            extractCodeMap.put(obj.getCode(), obj);
        }
    }

    int code;

    String desc;

    ExtractCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ExtractCode getExtractCode(Integer value) {
        return extractCodeMap.get(value);
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return String.valueOf(this.code);
    }

}
