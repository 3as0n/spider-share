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

package com.treefinance.crawler.framework.config.xml.plugin;

import com.datatrees.common.util.json.annotation.Description;
import com.treefinance.crawler.framework.config.annotation.Attr;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Tag;
import com.treefinance.crawler.framework.config.enums.PluginPhase;
import com.treefinance.crawler.framework.config.enums.PluginType;
import com.treefinance.crawler.framework.config.xml.AbstractBeanDefinition;

import java.io.Serializable;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Feb 7, 2014 11:13:43 AM
 */
@Description(value = "type", keys = {"JAVA", "SHELL", "PYTHON"}, types = {JavaPlugin.class, ShellPlugin.class, PythonPlugin.class})
public abstract class AbstractPlugin extends AbstractBeanDefinition implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -721450264346621071L;

    private PluginType type;

    private PluginPhase phase;

    private String extraConfig;

    private String fileName;

    @Attr("file-type")
    public PluginType getType() {
        return type;
    }

    @Node("@file-type")
    public void setType(String type) {
        this.type = PluginType.getPluginType(type);
    }

    @Attr("phase")
    public PluginPhase getPhase() {
        return phase;
    }

    @Node("@phase")
    public void setPhase(String phase) {
        this.phase = PluginPhase.getPluginPhase(phase);
    }

    @Tag("extra-config")
    public String getExtraConfig() {
        return extraConfig;
    }

    @Node("extra-config/text()")
    public void setExtraConfig(String extraConfig) {
        this.extraConfig = extraConfig;
    }

    @Attr("file-name")
    public String getFileName() {
        return fileName;
    }

    @Node("@file-name")
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
