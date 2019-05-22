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

package com.treefinance.crawler.framework.context;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * url's protocol registry.
 * 
 * @author Jerry
 * @since 10:36 2018/7/19
 */
public final class UrlProtocolRegistry {

    public static final String PROTOCOL_SEPARATOR = "://";
    private static final String DEFAULT_PROTOCOLS = "http,https,rtmp,qvod,pa,mms,rtsp,thunder,bdhb";
    private static Collection<String> PROTOCOLS = build(DEFAULT_PROTOCOLS.split(","));

    private UrlProtocolRegistry() {}

    private static ConcurrentLinkedQueue<String> build(String... protocols) {
        return Arrays.stream(protocols).map(StringUtils::trim).filter(StringUtils::isNotEmpty).collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
    }

    /**
     * register new protocols
     * 
     * @param protocols the new protocol array to add
     */
    public static void register(String... protocols) {
        if (ArrayUtils.isNotEmpty(protocols)) {
            Arrays.stream(protocols).map(StringUtils::trim).filter(protocol -> StringUtils.isNotEmpty(protocol) && !PROTOCOLS.contains(protocol)).forEach(PROTOCOLS::add);
        }
    }

    /**
     * clear old protocols and reset to new protocols
     * 
     * @param protocols the new protocol array to add
     */
    public static void reset(String... protocols) {
        if (ArrayUtils.isEmpty(protocols)) {
            throw new IllegalArgumentException("The protocols must not be empty.");
        }

        PROTOCOLS = build(protocols);
    }

    /**
     * @return unmodifiable protocol collection.
     */
    public static Collection<String> getProtocols() {
        return Collections.unmodifiableCollection(PROTOCOLS);
    }
}
