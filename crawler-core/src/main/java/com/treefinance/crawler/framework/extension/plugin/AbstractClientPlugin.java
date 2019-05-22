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

package com.treefinance.crawler.framework.extension.plugin;

import com.treefinance.crawler.framework.context.function.LinkNode;
import com.treefinance.crawler.framework.protocol.Protocol;
import com.treefinance.crawler.framework.proxy.ProxyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * abstract client plugin custom plugin should implements this as super class
 * 
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 20, 2014 10:28:20 AM
 */
public abstract class AbstractClientPlugin {

    protected Logger logger = LoggerFactory.getLogger(AbstractClientPlugin.class);

    private Protocol webClient;

    private ProxyManager proxyManager;

    public Protocol getWebClient() {
        return webClient;
    }

    public void setWebClient(Protocol webClient) {
        this.webClient = webClient;
    }

    public ProxyManager getProxyManager() {
        return proxyManager;
    }

    public void setProxyManager(ProxyManager proxyManager) {
        this.proxyManager = proxyManager;
    }

    public abstract String process(String... args) throws Exception;

    protected String getResponseByWebRequest(LinkNode linkNode) {
        try {
            return PluginHelper.requestAsString(linkNode, null);
        } catch (Exception e) {
            throw new RuntimeException("Error sending request >>> " + linkNode, e);
        }
    }

    @Deprecated
    protected String getPorxy(String cacertUrl) throws Exception {
        return this.getProxy(cacertUrl);
    }

    protected String getProxy(String url) throws Exception {
        return PluginHelper.getProxy(url);
    }

}
