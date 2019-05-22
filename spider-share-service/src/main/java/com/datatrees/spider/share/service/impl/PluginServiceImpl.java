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

package com.datatrees.spider.share.service.impl;

import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.spider.share.common.utils.RedisUtils;
import com.datatrees.spider.share.common.utils.TaskUtils;
import com.datatrees.spider.share.common.utils.TemplateUtils;
import com.datatrees.spider.share.domain.PluginUpgradeResult;
import com.datatrees.spider.share.domain.RedisKeyPrefixEnum;
import com.datatrees.spider.share.service.PluginService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhouxinghai on 2017/7/6.
 */
@Service
public class PluginServiceImpl implements PluginService, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger("plugin_log");

    /**
     * 文件版本号缓存
     */
    private static LoadingCache<String, String> fileVersionCache;

    @Value("${plugin.local.store.path}")
    private String pluginPath;

    @Override
    public String savePlugin(String sassEnv, String fileName, byte[] bytes, String version) {
        if (StringUtils.isBlank(sassEnv)) {
            sassEnv = TaskUtils.getSassEnv();
            logger.info("use system sassEnv :{}", sassEnv);
        }
        if (StringUtils.isBlank(version)) {
            version = String.valueOf(System.currentTimeMillis());
            logger.info("use default version :{}", version);
        }
        RedisUtils.hset(RedisKeyPrefixEnum.PLUGIN_DATA.getRedisKey(sassEnv), fileName, bytes);
        RedisUtils.hset(RedisKeyPrefixEnum.PLUGIN_VERSION.getRedisKey(sassEnv), fileName, version);
        logger.info("uploadPlugin success fileName={},version={},sassEnv={}", fileName, version, sassEnv);
        return version;
    }

    @Override
    public PluginUpgradeResult getPluginFromRedisNew(String pluginName) throws IOException {
        String sassEnv = TaskUtils.getSassEnv();
        String version = RedisUtils.hget(RedisKeyPrefixEnum.PLUGIN_VERSION.getRedisKey(sassEnv), pluginName);
        if (StringUtils.isBlank(version)) {
            logger.error("not found plugin version from redis,plugin name is {}", pluginName);
        }
        File file = getPluginFile(pluginName, version);
        boolean forceReload = !file.exists();
        if (forceReload) {
            byte[] bytes = RedisUtils.hgetForByte(RedisKeyPrefixEnum.PLUGIN_DATA.getRedisKey(sassEnv), pluginName);
            FileUtils.writeByteArrayToFile(file, bytes, false);
        }
        PluginUpgradeResult result = new PluginUpgradeResult();
        result.setForceReload(forceReload);
        result.setFile(file);
        result.setVersion(version);
        logger.info("getPluginFromRedisNew success pluginName={},localJar={}", pluginName, file.getName());
        return result;
    }

    @Override
    public String getPluginVersionFromCache(String pluginName) throws ExecutionException {
        return fileVersionCache.get(pluginName);
    }

    @Override
    public File getPluginFile(String pluginName, String version) {
        String serverIp = System.getProperty("server.ip", "default");
        return new File(TemplateUtils.format("{}{}{}.{}", pluginPath, version, serverIp, pluginName));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!StringUtils.endsWith(pluginPath, "/")) {
            pluginPath += "/";
        }
        File file = new File(pluginPath);
        if (file.exists()) {
            file.deleteOnExit();
            file.mkdirs();
        }
        logger.info("初始化plugin目录,清理所有jar,pluginPath={}", pluginPath);

        // 默认5秒更新缓存
        int fileUpgradeInterval = PropertiesConfiguration.getInstance().getInt("plugin.file.upgrade.interval", 10);
        logger.info("cache config file_upgrade_interval={}", fileUpgradeInterval);
        fileVersionCache = CacheBuilder.newBuilder().expireAfterWrite(fileUpgradeInterval, TimeUnit.SECONDS).removalListener(notification -> {
            Object key = notification.getKey();
            logger.info("cache remove key:{}", key.toString());
        }).build(new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                PluginUpgradeResult upgradeResult = getPluginFromRedisNew(key);
                return upgradeResult.getVersion();
            }
        });
    }
}
