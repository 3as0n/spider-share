/*
 * Copyright © 2015 - 2018 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.treefinance.crawler.framework.config.enums.page;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Feb 7, 2014 10:34:34 AM
 */
public enum RetryMode {
    RETRY("retry"),
    REQUEUE("requeue"),
    PROXY_RETRY("proxy_retry"),
    PROXY_REQUEUE("proxy_requeue");

    private static Map<String, RetryMode> retryModeMap = new HashMap<String, RetryMode>();

    static {
        for (RetryMode obj : values()) {
            retryModeMap.put(obj.getValue(), obj);
        }
    }

    private final String value;

    RetryMode(String value) {
        this.value = value;
    }

    public static RetryMode getRetryMode(String value) {
        return retryModeMap.get(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}