package com.datatrees.rawdatacentral.service.impl;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.rawdatacentral.api.RedisService;
import com.datatrees.rawdatacentral.common.http.TaskUtils;
import com.datatrees.rawdatacentral.common.utils.CheckUtils;
import com.datatrees.rawdatacentral.common.utils.RedisUtils;
import com.datatrees.rawdatacentral.common.utils.TemplateUtils;
import com.datatrees.rawdatacentral.domain.enums.RedisKeyPrefixEnum;
import com.datatrees.rawdatacentral.domain.vo.PluginUpgradeResult;
import com.datatrees.rawdatacentral.service.PluginService;
import com.google.common.cache.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by zhouxinghai on 2017/7/6.
 */
@Service
public class PluginServiceImpl implements PluginService, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(PluginServiceImpl.class);
    /**
     * 文件版本号缓存
     */
    private static LoadingCache<String, String> fileVersionCache;
    @Resource
    private        RedisService                 redisService;
    @Value("${plugin.local.store.path}")
    private        String                       pluginPath;

    @Override
    public String savePlugin(String fileName, byte[] bytes) {
        CheckUtils.checkNotBlank(fileName, "fileName is blank");
        String md5 = DigestUtils.md5Hex(bytes);
        redisService.saveBytes(RedisKeyPrefixEnum.PLUGIN_FILE.getRedisKey(fileName), bytes);
        redisService.saveString(RedisKeyPrefixEnum.PLUGIN_FILE_MD5, fileName, md5);
        logger.info("cache plugin fileName={},md5={}", fileName, md5);
        return md5;
    }

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
    public PluginUpgradeResult getPluginFromRedis(String pluginName) throws IOException {
        String sassEnv = TaskUtils.getSassEnv();
        String version = RedisUtils.hget(RedisKeyPrefixEnum.PLUGIN_VERSION.getRedisKey(sassEnv), pluginName);
        if (StringUtils.isNotBlank(version)) {
            return getPluginFromRedisNew(pluginName);
        }
        PluginUpgradeResult result = new PluginUpgradeResult();
        version = redisService.getString(RedisKeyPrefixEnum.PLUGIN_FILE_MD5.getRedisKey(pluginName));
        if (StringUtils.isBlank(version)) {
            logger.error("没有从redis读取到插件md5,pluginName={}", pluginName);
            throw new IOException("没有从redis读取到插件:" + pluginName);
        }
        //修改策略,文件保存到本地用${md5}.jar,这样文件变化了,classLoader就变了
        File file = getPluginFile(pluginName, version);
        boolean forceReload = !file.exists();
        if (forceReload) {
            byte[] bytes = redisService.getBytes(RedisKeyPrefixEnum.PLUGIN_FILE.getRedisKey(pluginName));
            FileUtils.writeByteArrayToFile(file, bytes, false);
            logger.info("plugin已经更新,重新加载到本地,pluginName={},pluginPath={},md5={}", pluginName, pluginPath, version);
        }
        result.setForceReload(forceReload);
        result.setFile(file);
        result.setVersion(version);
        logger.info("getPluginFromRedis success pluginName={},localJar={}", pluginName, file.getName());
        return result;
    }

    @Override
    public PluginUpgradeResult getPluginFromRedisNew(String pluginName) throws IOException {
        String sassEnv = TaskUtils.getSassEnv();
        String version = RedisUtils.hget(RedisKeyPrefixEnum.PLUGIN_VERSION.getRedisKey(sassEnv), pluginName);
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

        //默认5秒更新缓存
        int file_upgrade_interval = PropertiesConfiguration.getInstance().getInt("plugin.file.upgrade.interval", 10);
        logger.info("cache config class_upgrade_interval={}", file_upgrade_interval);
        fileVersionCache = CacheBuilder.newBuilder().expireAfterWrite(file_upgrade_interval, TimeUnit.SECONDS)
                .removalListener(new RemovalListener<Object, Object>() {
                    @Override
                    public void onRemoval(RemovalNotification<Object, Object> notification) {
                        Object key = notification.getKey();
                        logger.info("cache remove key:{}", key.toString());
                    }
                }).build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        PluginUpgradeResult upgradeResult = getPluginFromRedis(key);
                        return upgradeResult.getVersion();
                    }
                });
    }
}
