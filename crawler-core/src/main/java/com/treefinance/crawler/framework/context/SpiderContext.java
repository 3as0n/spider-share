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

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Map;

/**
 * @author Jerry
 * @since 13:54 25/01/2018
 */
public interface SpiderContext extends CookieStore {

    Map<String, AbstractPlugin> getPluginMetadataMap();

    /**
     * register plugins's metadata
     * 
     * @param pluginMetadataList a list of plugin metadata {@link AbstractPlugin}
     */
    void registerPlugins(final List<AbstractPlugin> pluginMetadataList);

    /**
     * register plugin's metadata
     * 
     * @param pluginMetadata plugin metadata {@link AbstractPlugin}
     */
    void registerPlugin(final AbstractPlugin pluginMetadata);

    AbstractPlugin getPluginMetadataById(@Nonnull final String pluginId);

}
