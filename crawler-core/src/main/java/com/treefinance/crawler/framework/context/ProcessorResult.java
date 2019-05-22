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

package com.treefinance.crawler.framework.context;

import com.treefinance.crawler.lang.SynchronizedMap;

import java.util.Map;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年8月3日 下午1:52:39
 */
public class ProcessorResult<K, V> extends SynchronizedMap<K, V> {

    public ProcessorResult() {}

    public ProcessorResult(Map<K, V> m) {
        super(m);
    }
}
