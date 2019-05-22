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

package com.datatrees.spider.share.common.http;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 请求ID生成器 Created by zhouxinghai on 2017/8/31
 */
public class RequestIdUtils {

    private static final AtomicLong ID = new AtomicLong(0);

    /**
     * 生产RequestId
     * 
     * @return
     */
    public static long createId() {
        if (ID.get() >= Long.MAX_VALUE - 10000) {
            ID.set(1);
        }
        return ID.incrementAndGet();
    }

}
