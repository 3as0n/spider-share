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

package com.datatrees.spider.share.service.extra;

import com.datatrees.spider.share.common.utils.TemplateUtils;
import com.datatrees.spider.share.service.ClassLoaderService;
import com.treefinance.crawler.framework.config.xml.plugin.AbstractPlugin;
import com.treefinance.crawler.framework.config.xml.plugin.JavaPlugin;
import com.treefinance.crawler.framework.exception.ExtensionException;
import com.treefinance.crawler.framework.exception.PluginException;
import com.treefinance.crawler.framework.exception.UnsupportedExtensionException;
import com.treefinance.crawler.framework.extension.manager.PluginManager;
import com.treefinance.crawler.framework.extension.manager.WrappedExtension;
import com.treefinance.crawler.framework.extension.plugin.AbstractClientPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Created by zhouxinghai on 2017/7/6.
 */
@Service
public class RedisPluginManager implements PluginManager {

    private static final Logger logger = LoggerFactory.getLogger(RedisPluginManager.class);

    @Autowired
    private ClassLoaderService classLoaderService;

    @Override
    public <T> WrappedExtension<T> loadExtension(AbstractPlugin metadata, Class<T> extensionType, Long taskId) throws ExtensionException {
        Objects.requireNonNull(metadata);
        if (metadata instanceof JavaPlugin) {
            String fileName = metadata.getFileName();
            String mainClass = ((JavaPlugin)metadata).getMainClass();
            T plugin = loadExtension(fileName, mainClass, extensionType, taskId);
            return new WrappedExtension<>(metadata, plugin);
        }

        throw new UnsupportedExtensionException("Unsupported extension type! >>> " + metadata.getClass());
    }

    @Override
    public <T> T loadExtension(String jarName, String mainClass, Class<T> extensionType, Long taskId) throws ExtensionException {
        try {
            Class pluginClass = classLoaderService.loadPlugin(jarName, mainClass, taskId);

            if (null == pluginClass) {
                logger.error("Error loading extension class! jarName={}, mainClass={}", jarName, mainClass);
                throw new ExtensionException(TemplateUtils.format("Error loading extension class! jarName={}, mainClass={}", jarName, mainClass));
            }

            if (!extensionType.isAssignableFrom(pluginClass)) {
                logger.error("Incorrect extension type! type={}, jarName={}, mainClass={}", extensionType, jarName, mainClass);
                throw new ExtensionException("Incorrect extension type! The expected extension must be instance of " + extensionType.getName());
            }

            return extensionType.cast(pluginClass.newInstance());
        } catch (ExtensionException e) {
            throw e;
        } catch (Throwable e) {
            logger.error("Error loading extension! jarName={}, mainClass={}", jarName, mainClass, e);
            throw new ExtensionException(e);
        }
    }

    @Override
    public AbstractClientPlugin loadPlugin(String jarName, String mainClass, Long taskId) throws PluginException {
        return loadExtension(jarName, mainClass, AbstractClientPlugin.class, taskId);
    }

}
