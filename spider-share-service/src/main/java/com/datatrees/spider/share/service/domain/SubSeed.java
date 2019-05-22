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

package com.datatrees.spider.share.service.domain;

import com.treefinance.crawler.framework.proxy.Proxy;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年12月19日 下午4:25:02
 */
public class SubSeed extends HashMap<String, Object> {

    public final static String WEBSITE_NAME = "websiteName";

    public final static String UNIQUE_SUFFIX = "uniqueSuffix";

    public final static String SYNC = "sync";

    public final static String MUTEX = "mutex";

    public final static String TEMPLATEID = "templateId";// 包含多个“,”分割

    public final static String SEEDURL = "seedurl";

    public final static String WAITING = "waiting";// waiting waitingMillis or 'parentTask'

    public final static String LOGIN_CHECK_IGNORE = "loginCheckIgnore";

    public final static String NO_STATUS = "noStatus";

    public final static String PROXY_SHARED = "proxyShared";// 代理共享，父子任务持续使用代理

    public final static String PROXY = "proxy";// 代理共享，父子任务持续使用代理

    public SubSeed() {}

    public SubSeed(Map<? extends String, ?> m) {
        super(m);
    }

    public Proxy getProxy() {
        return (Proxy)this.get(PROXY);
    }

    public void setProxy(Proxy proxy) {
        this.put(PROXY, proxy);
    }

    public Boolean getProxyShared() {
        return (Boolean)this.get(PROXY_SHARED);
    }

    public void setProxyShared(Boolean proxyShared) {
        this.put(PROXY_SHARED, proxyShared);
    }

    /**
     * @return the loginCheckIgnore
     */
    public Boolean getLoginCheckIgnore() {
        return (Boolean)this.get(LOGIN_CHECK_IGNORE);
    }

    /**
     * @param loginCheckIgnore the loginCheckIgnore to set
     */
    public void setLoginCheckIgnore(Boolean loginCheckIgnore) {
        this.put(LOGIN_CHECK_IGNORE, loginCheckIgnore);
    }

    public String getWaiting() {
        return (String)this.get(WAITING);
    }

    public void setWaiting(String waiting) {
        this.put(WAITING, waiting);
    }

    /**
     * @return the seedurl
     */
    public String getSeedUrl() {
        return (String)this.get(SEEDURL);
    }

    /**
     * @param url the url to set
     */
    public void setSeedUrl(String seedurl) {
        this.put(SEEDURL, seedurl);
    }

    /**
     * @return the url
     */
    public String getTemplateId() {
        return (String)this.get(TEMPLATEID);
    }

    /**
     * @param url the url to set
     */
    public void setTemplateId(String templateId) {
        this.put(TEMPLATEID, templateId);
    }

    /**
     * @return the mutex
     */
    public Boolean isMutex() {
        return (Boolean)this.get(MUTEX);
    }

    /**
     * @param mutex the mutex to set
     */
    public void setMutex(boolean mutex) {
        this.put(MUTEX, mutex);
    }

    /**
     * @return the sync
     */
    public Boolean isSync() {
        return (Boolean)this.get(SYNC);
    }

    /**
     * @param sync the sync to set
     */
    public void setSync(boolean sync) {
        this.put(SYNC, sync);
    }

    /**
     * @return the uniqueSuffix
     */
    public String getUniqueSuffix() {
        return (String)this.get(UNIQUE_SUFFIX);
    }

    /**
     * @param uniqueSuffix the uniqueSuffix to set
     */
    public void setUniqueSuffix(String uniqueSuffix) {
        this.put(UNIQUE_SUFFIX, uniqueSuffix);
    }

    /**
     * @return the websiteName
     */
    public String getWebsiteName() {
        return (String)this.get(WEBSITE_NAME);
    }

    /**
     * @param websiteName the websiteName to set
     */
    public void setWebsiteName(String websiteName) {
        this.put(WEBSITE_NAME, websiteName);
    }

    public Boolean noStatus() {
        return (Boolean)this.get(NO_STATUS);
    }

}
