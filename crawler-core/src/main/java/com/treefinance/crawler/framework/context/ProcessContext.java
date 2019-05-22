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

import com.treefinance.crawler.framework.config.xml.plugin.AbstractPlugin;
import com.treefinance.crawler.framework.config.xml.properties.Properties;
import com.treefinance.crawler.framework.config.xml.service.AbstractService;
import com.treefinance.crawler.framework.config.xml.service.TaskHttpService;
import com.treefinance.crawler.framework.consts.Constants;
import com.treefinance.crawler.framework.extension.manager.PluginManager;
import com.treefinance.crawler.lang.SynchronizedMap;
import com.treefinance.toolkit.util.Preconditions;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jerry
 * @since 13:54 25/01/2018
 */
public abstract class ProcessContext extends DefaultCookieStore implements SpiderContext {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final Website website;

    private final Map<String, AbstractPlugin> pluginMetadataMap;

    private Map<String, Object> attributes;

    private PluginManager pluginManager;

    public ProcessContext(@Nonnull final Website website) {
        Preconditions.notNull("website", website);
        this.website = website;
        this.attributes = new SynchronizedMap<>();
        this.pluginMetadataMap = new ConcurrentHashMap<>();
    }

    public Website getWebsite() {
        return website;
    }

    public Integer getWebsiteId() {
        return website.getId();
    }

    public String getWebsiteName() {
        return website.getWebsiteName();
    }

    public String getWebsiteType() {
        return website.getWebsiteType();
    }

    @Override
    public Map<String, AbstractPlugin> getPluginMetadataMap() {
        return pluginMetadataMap;
    }

    @Override
    public void registerPlugins(List<AbstractPlugin> pluginMetadataList) {
        if (CollectionUtils.isNotEmpty(pluginMetadataList)) {
            pluginMetadataList.forEach(this::registerPlugin);
        }
    }

    @Override
    public void registerPlugin(AbstractPlugin pluginMetadata) {
        if (pluginMetadata == null) {
            return;
        }

        if (StringUtils.isEmpty(pluginMetadata.getId())) {
            logger.warn("Failed to register plugin metadata >>> The plugin id is missing!");
            return;
        }

        pluginMetadataMap.put(pluginMetadata.getId(), pluginMetadata);
    }

    @Override
    public AbstractPlugin getPluginMetadataById(@Nonnull String pluginId) {
        return pluginMetadataMap.get(pluginId);
    }

    @Override
    public void setCookies(String cookies) {
        super.setCookies(cookies);
        updateCookiesInAttributes();
    }

    @Override
    public void setCookies(@Nullable String cookies, boolean retainQuote) {
        super.setCookies(cookies, retainQuote);
        updateCookiesInAttributes();
    }

    @Override
    public void setCookies(Map<String, String> cookies) {
        super.setCookies(cookies);
        updateCookiesInAttributes();
    }

    @Override
    public void addCookies(Map<String, String> cookies) {
        super.addCookies(cookies);
        updateCookiesInAttributes();
    }

    @Override
    public void addCookies(@Nullable String[] cookies, boolean retainQuote) {
        super.addCookies(cookies, retainQuote);
        updateCookiesInAttributes();
    }

    /**
     * @return the context
     */
    @Deprecated
    public Map<String, Object> getContext() {
        return getAttributes();
    }

    /**
     * the shared fields map with the global context scope.
     *
     * @return the unmodifiable map.
     * @see #getAttributes()
     */
    public Map<String, Object> getVisibleScope() {
        return Collections.unmodifiableMap(attributes);
    }

    public Object getAttribute(String name) {
        return getAttributes().get(name);
    }

    public void setAttribute(String name, Object value) {
        logger.debug("add context attribute >> {} : {}", name, value);
        if (value == null) {
            getAttributes().remove(name);
        } else {
            getAttributes().put(name, value);
        }
    }

    public void addAttributes(Map<String, Object> attributes) {
        if (attributes != null) {
            getAttributes().putAll(attributes);
        }
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public void setPluginManager(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    public AbstractService getDefaultService() {
        AbstractService service = null;
        if (null != website && null != website.getSearchConfig() && null != website.getSearchConfig().getProperties()) {
            Properties properties = website.getSearchConfig().getProperties();
            Boolean useTaskHttp = properties.getUseTaskHttp();
            if (BooleanUtils.isTrue(useTaskHttp)) {
                service = new TaskHttpService();
                service.setServiceType("task_http");
            }
        }
        return service;
    }

    protected Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        if (attributes == null) {
            getAttributes().clear();
        } else {
            this.attributes = new SynchronizedMap<>(attributes);
        }
    }

    private void updateCookiesInAttributes() {
        setAttribute(Constants.COOKIE, this.getCookiesAsMap());
        setAttribute(Constants.COOKIE_STRING, this.getCookiesAsString());
    }

}
