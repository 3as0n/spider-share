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

package com.treefinance.crawler.framework.config.xml;

import com.treefinance.crawler.framework.config.SpiderConfig;
import com.treefinance.crawler.framework.config.annotation.Attr;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Tag;
import com.treefinance.crawler.framework.config.xml.plugin.AbstractPlugin;
import com.treefinance.crawler.framework.config.xml.plugin.JavaPlugin;
import com.treefinance.crawler.framework.config.xml.plugin.PythonPlugin;
import com.treefinance.crawler.framework.config.xml.plugin.ShellPlugin;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月7日 下午7:05:31
 */
public abstract class AbstractWebsiteConfig implements SpiderConfig {

    /**
     *
     */
    private static final long serialVersionUID = 2850518517814023140L;

    private List<AbstractPlugin> pluginList = new ArrayList<>();

    private String parentWebsiteName;

    public AbstractWebsiteConfig() {
        super();
    }

    @Tag("plugin-definition")
    public List<AbstractPlugin> getPluginList() {
        return Collections.unmodifiableList(pluginList);
    }

    @Node(value = "plugin-definition/plugin", types = {JavaPlugin.class, ShellPlugin.class, PythonPlugin.class}, registered = true)
    public void setPluginList(AbstractPlugin plugin) {
        this.pluginList.add(plugin);
    }

    @Attr("extends")
    public String getParentWebsiteName() {
        return parentWebsiteName;
    }

    @Node("@extends")
    public void setParentWebsiteName(String parentWebsiteName) {
        this.parentWebsiteName = parentWebsiteName;
    }

    public void clone(AbstractWebsiteConfig cloneFrom) {
        if (CollectionUtils.isEmpty(pluginList)) {
            this.pluginList.addAll(cloneFrom.getPluginList());
        }
    }
}
