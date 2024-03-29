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

package com.treefinance.crawler.framework.config.xml.login;

import com.treefinance.crawler.framework.config.annotation.Attr;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Tag;
import com.treefinance.crawler.framework.config.enums.LoginType;
import com.treefinance.crawler.framework.config.xml.plugin.AbstractPlugin;

import java.io.Serializable;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Feb 7, 2014 11:38:49 AM
 */
public class LoginConfig implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3832213144362274287L;

    private LoginType type;

    private AbstractPlugin plugin;

    private String urlTemplate;

    private String headers;

    private LoginCheckConfig loginCheckConfig;

    @Tag("login-check")
    public LoginCheckConfig getLoginCheckConfig() {
        return loginCheckConfig;
    }

    @Node("login-check")
    public void setLoginCheckConfig(LoginCheckConfig loginCheckConfig) {
        this.loginCheckConfig = loginCheckConfig;
    }

    @Attr("login-type")
    public LoginType getType() {
        return type;
    }

    @Node("@login-type")
    public void setType(String type) {
        this.type = LoginType.getLoginType(type);
    }

    @Attr(value = "plugin-ref", referenced = true)
    public AbstractPlugin getPlugin() {
        return plugin;
    }

    @Node(value = "@plugin-ref", referenced = true)
    public void setPlugin(AbstractPlugin plugin) {
        this.plugin = plugin;
    }

    @Tag("url-template")
    public String getUrlTemplate() {
        return urlTemplate;
    }

    @Node("url-template/text()")
    public void setUrlTemplate(String urlTemplate) {
        this.urlTemplate = urlTemplate;
    }

    @Tag("headers")
    public String getHeaders() {
        return headers;
    }

    @Node("headers/text()")
    public void setHeaders(String headers) {
        this.headers = headers;
    }

}
