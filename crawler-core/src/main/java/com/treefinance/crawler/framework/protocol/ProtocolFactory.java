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

package com.treefinance.crawler.framework.protocol;

import com.datatrees.common.conf.Configuration;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.treefinance.crawler.framework.protocol.http.WebClient;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Creates and caches {@link Protocol} plugins. Protocol plugins should define the attribute "protocolName" with the
 * name of the protocol that they implement. Configuration object is used for caching. Cache key is constructed from
 * appending protocol name (eg. http) to constant
 */
public class ProtocolFactory {

    private static final Cache<Key, Protocol> cache = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES).softValues().build();

    public static Protocol getProtocol(ProtocolType type, Configuration conf) {

        Key key = new Key(conf, type.name());

        try {
            return cache.get(key, () -> createProtocol(type, conf));
        } catch (ExecutionException e) {
            throw new UncheckedExecutionException("获取protocol失败！", e);
        }
    }

    private static Protocol createProtocol(ProtocolType type, Configuration conf) {
        if (ProtocolType.HTTP.equals(type)) {
            Protocol protocol = new WebClient();
            protocol.setConf(conf);
            return protocol;
        }
        throw new UnsupportedOperationException("Unsupported protocol type: " + type);
    }

    public enum ProtocolType {
        HTTP,
        FTP,
        FILE,
        SFTP;
    }

    private static class Key {

        private final Configuration conf;

        private final String type;

        public Key(Configuration conf, String type) {
            this.conf = conf;
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            Key key = (Key)o;

            if (conf != null ? !conf.equals(key.conf) : key.conf != null)
                return false;
            return type.equals(key.type);
        }

        @Override
        public int hashCode() {
            int result = conf != null ? conf.hashCode() : 0;
            result = 31 * result + type.hashCode();
            return result;
        }
    }
}
