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

package com.treefinance.crawler.framework.protocol;

import com.datatrees.common.conf.Configuration;
import com.datatrees.common.conf.ConfigurationBase;
import com.datatrees.common.conf.ConfigurationWapper;
import com.datatrees.common.conf.DefaultConfiguration;
import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.common.util.StringUtils;
import com.treefinance.crawler.framework.protocol.http.HTTPConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.security.Security;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Mar 7, 2014 7:57:52 PM
 */
public class WebClientUtil {

    private static final Logger log = LoggerFactory.getLogger(WebClientUtil.class);

    private static final Map<String, Protocol> PROTOCOL_MAP = new ConcurrentHashMap<>();

    private static final String SERVICE_PREFIX = "service";

    private static final String FILE_CLIENT_PREFIX = "file";

    static {
        try {
            String cacertsPath = PropertiesConfiguration.getInstance().get("custom.net.ssl.trustStore", "cacerts/cacerts");
            if (StringUtils.isNotBlank(cacertsPath)) {
                log.info("cacerts path: {}", cacertsPath);
                File file = getResourceFile(cacertsPath);
                if (file != null && file.exists()) {
                    log.info("set custom ssl trustStore path: {}", file.getAbsolutePath());
                    System.setProperty("javax.net.ssl.trustStore", file.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            log.warn("Error setting 'javax.net.ssl.trustStore' into system property", e);
        }

        try {
            log.info("set jdk.tls.disabledAlgorithms empty.");
            Security.setProperty("jdk.tls.disabledAlgorithms", "");
        } catch (Exception e) {
            log.warn("Error setting 'jdk.tls.disabledAlgorithms' into system property", e);
        }

        try {
            log.info("set jdk.certpath.disabledAlgorithms empty.");
            Security.setProperty("jdk.certpath.disabledAlgorithms", "");
        } catch (Exception e) {
            log.warn("Error setting 'jdk.certpath.disabledAlgorithms' into system property", e);
        }
    }

    private static File getResourceFile(String relativePath) {
        URL resource = ClassLoader.getSystemClassLoader().getResource(relativePath);
        if (resource != null) {
            log.info("resource path: {}", resource);
            try {
                return new File(resource.toURI());
            } catch (Exception e) {
                log.warn(e.getMessage());
            }
        }

        resource = ClassLoader.getSystemClassLoader().getResource("");
        if (resource != null) {
            String classPath = resource.getPath();
            log.info("classpath: {}", classPath);
            return new File(classPath + relativePath);
        }

        return null;
    }

    private static Protocol getWebClient(Configuration conf) {
        try {
            return ProtocolFactory.getProtocol(ProtocolFactory.ProtocolType.HTTP, conf);
        } catch (Exception e) {
            log.error("get client error", e);
        }
        return null;
    }

    public static Protocol getWebClient(String prefixName) {
        if (StringUtils.isBlank(prefixName)) {
            return getWebClient();
        } else {
            return PROTOCOL_MAP.computeIfAbsent(prefixName, s -> {
                Configuration conf = getHTTPConfiguration(prefixName);
                return getWebClient(conf);
            });
        }
    }

    private static ConfigurationBase getHTTPConfiguration(final String prefixName) {
        Configuration conf = PropertiesConfiguration.getInstance();

        ConfigurationBase wrappedConfig = wrap(conf);

        setLong(wrappedConfig, conf, HTTPConstants.CONNECTION_TIMEOUT, prefixName, TimeUnit.SECONDS.toMillis(5));
        setLong(wrappedConfig, conf, HTTPConstants.SO_TIMEOUT, prefixName, TimeUnit.SECONDS.toMillis(15));
        setLong(wrappedConfig, conf, HTTPConstants.CONNECTION_MANAGER_TIMEOUT, prefixName, TimeUnit.SECONDS.toMillis(300));
        setLong(wrappedConfig, conf, HTTPConstants.HTTP_CONTENT_LIMIT, prefixName, 4 * 1024 * 1024);
        setInt(wrappedConfig, conf, HTTPConstants.MAX_HOST_CONNECTIONS, prefixName, 100);
        setInt(wrappedConfig, conf, HTTPConstants.MAX_TOTAL_CONNECTIONS, prefixName, 100);

        return wrappedConfig;
    }

    private static void setLong(ConfigurationBase config, Configuration from, String configName, String prefix, long defaultValue) {
        long value = from.getLong(prefix + "." + configName, defaultValue);
        config.setLong(configName, value);
    }

    private static void setInt(ConfigurationBase config, Configuration from, String configName, String prefix, int defaultValue) {
        int value = from.getInt(prefix + "." + configName, defaultValue);
        config.setInt(configName, value);
    }

    private static ConfigurationBase wrap(Configuration conf) {
        DefaultConfiguration newConfiguration = new DefaultConfiguration();
        return new ConfigurationWapper(newConfiguration, conf);
    }

    public static Protocol getWebClient() {
        return getWebClient(PropertiesConfiguration.getInstance());
    }

    public static Protocol getServiceClient() {
        return getWebClient(SERVICE_PREFIX);
    }

    public static Protocol getFileClient() {
        return getWebClient(FILE_CLIENT_PREFIX);
    }

}
