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

import com.datatrees.spider.share.common.http.ProxyUtils;
import com.google.common.collect.ImmutableList;
import com.treefinance.crawler.framework.config.enums.LoginType;
import com.treefinance.crawler.framework.config.enums.SearchType;
import com.treefinance.crawler.framework.config.enums.properties.Scope;
import com.treefinance.crawler.framework.config.enums.properties.UnicodeMode;
import com.treefinance.crawler.framework.config.xml.SearchConfig;
import com.treefinance.crawler.framework.config.xml.login.LoginConfig;
import com.treefinance.crawler.framework.config.xml.page.Page;
import com.treefinance.crawler.framework.config.xml.properties.Properties;
import com.treefinance.crawler.framework.config.xml.properties.Proxy;
import com.treefinance.crawler.framework.config.xml.properties.cookie.AbstractCookie;
import com.treefinance.crawler.framework.config.xml.search.SearchSequenceUnit;
import com.treefinance.crawler.framework.config.xml.search.SearchTemplateConfig;
import com.treefinance.crawler.framework.context.function.LinkNode;
import com.treefinance.crawler.framework.decode.Decoder;
import com.treefinance.crawler.framework.decode.DecoderFactory;
import com.treefinance.crawler.framework.exception.NoProxyException;
import com.treefinance.crawler.framework.login.Cookie;
import com.treefinance.crawler.framework.login.LoginResource;
import com.treefinance.crawler.framework.login.WebsiteAccount;
import com.treefinance.crawler.framework.proxy.ProxyManager;
import com.treefinance.toolkit.util.Preconditions;
import com.treefinance.toolkit.util.RegExp;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Mar 7, 2014 2:14:19 PM
 */
public class SearchProcessorContext extends AbstractProcessorContext {

    private final Map<SearchTemplateConfig, Map<Integer, List<SearchSequenceUnit>>> depthPageMap = new HashMap<>();

    private final Map<SearchTemplateConfig, Map<String, SearchSequenceUnit>> pathPageMap = new HashMap<>();

    private final Map<String, SearchTemplateConfig> searchTemplateConfigMap = new HashMap<>();

    private final Map<SearchType, List<SearchTemplateConfig>> searchTemplateConfigListMap = new HashMap<>();

    // page id ===> page
    private final Map<String, Page> pageMap = new HashMap<>();

    private ProxyManager proxyManager;

    private LoginResource loginResource;

    private Proxy proxyConf;

    private AbstractCookie cookieConf;

    private Map<String, String> defaultHeader = new HashMap<>();

    private Map<Page, Integer> pageVisitCountMap = new HashMap<>();

    private boolean loginCheckIgnore;

    public SearchProcessorContext(Website website, Long taskId) {
        super(website, taskId);

        Preconditions.notNull("search-config", website.getSearchConfig());
    }

    public void release() {
        // proxy release
        if (proxyManager != null && proxyConf != null && proxyConf.getScope().equals(Scope.SESSION)) {
            try {
                // mark sessionproxy
                addProcessorResult("sessionProxy", "" + getProxy());
                proxyManager.release();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     *
     */
    public void init() {
        // init search template map
        List<SearchTemplateConfig> searchTemplateConfigs = getSearchConfig().getSearchTemplateConfigList();
        for (SearchTemplateConfig searchTemplateConfig : searchTemplateConfigs) {
            searchTemplateConfigMap.put(searchTemplateConfig.getId(), searchTemplateConfig);
            SearchType taskType = searchTemplateConfig.getType();

            List<SearchTemplateConfig> configList = searchTemplateConfigListMap.computeIfAbsent(taskType, searchType -> new ArrayList<>());
            configList.add(searchTemplateConfig);

            Map<String, SearchSequenceUnit> pathPageMaps = new HashMap<>();
            Map<Integer, List<SearchSequenceUnit>> depthPageMaps = new HashMap<>();
            pathPageMap.put(searchTemplateConfig, pathPageMaps);
            depthPageMap.put(searchTemplateConfig, depthPageMaps);

            List<SearchSequenceUnit> searchSequenceUnits = searchTemplateConfig.getSearchSequence();

            for (SearchSequenceUnit searchSequenceUnit : searchSequenceUnits) {
                int depth = searchSequenceUnit.getDepth();
                Page pg = searchSequenceUnit.getPage();
                String path = (null == pg) ? null : pg.getPath();

                if (StringUtils.isNotEmpty(path)) {
                    pathPageMaps.put(path, searchSequenceUnit);
                }

                List<SearchSequenceUnit> pages = depthPageMaps.computeIfAbsent(depth, k -> new ArrayList<>());
                pages.add(searchSequenceUnit);
            }
        }

        // init page Map
        List<Page> pageList = getSearchConfig().getPageList();
        if (CollectionUtils.isNotEmpty(pageList)) {
            for (Page p : pageList) {
                pageMap.put(p.getId(), p);
            }
        }

        // init plugin
        registerPlugins(getSearchConfig().getPluginList());

        Properties searchProperties = getSearchProperties();
        if (searchProperties != null) {
            try {
                // init cookie conf
                cookieConf = searchProperties.getCookie().clone();
            } catch (Exception e) {
                // ignore
            }
            // init proxy
            proxyConf = searchProperties.getProxy();
        }
    }

    public AbstractCookie getCookieConf() {
        return cookieConf;
    }

    public ProxyManager getProxyManager() {
        return proxyManager;
    }

    /**
     * wrapper for proxy manager with scope states
     * 
     * @param proxyManager
     */
    public void setProxyManager(ProxyManager proxyManager) {
        this.proxyManager = proxyManager;
    }

    public Page getPage(String pid) {
        return pageMap.get(pid);
    }

    public Page getPageDefinition(LinkNode url, String templateId) {
        Page page = null;
        SearchTemplateConfig stc = getSearchTemplateConfig(templateId);
        if (stc != null) {
            if (CollectionUtils.isNotEmpty(stc.getSearchSequence())) {
                Map<String, SearchSequenceUnit> urlPathMap = pathPageMap.get(stc);
                if (MapUtils.isNotEmpty(urlPathMap)) {
                    for (Map.Entry<String, SearchSequenceUnit> entry : urlPathMap.entrySet()) {
                        if (StringUtils.isNotEmpty(entry.getKey()) && RegExp.find(url.getUrl(), entry.getKey())) {
                            page = entry.getValue().getPage();
                            break;
                        }
                    }
                }
                if (page == null) {
                    List<SearchSequenceUnit> pageList = depthPageMap.get(stc).get(url.getDepth());
                    if (CollectionUtils.isNotEmpty(pageList)) {
                        for (SearchSequenceUnit sequence : pageList) {
                            Page tempPage = sequence.getPage();
                            if (tempPage.getService() == null) {
                                page = tempPage;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (page == null) {
            page = Page.NULL;
        }
        return this.pageVisitCountCheck(page);
    }

    public void adjustUrlDepth(LinkNode curr, String templateId, int parent) {
        String url = curr.getUrl();
        int result = parent + 1;
        SearchTemplateConfig stc = getSearchTemplateConfig(templateId);
        if (stc != null) {
            // just for keyword search
            if (CollectionUtils.isNotEmpty(stc.getSearchSequence())) {
                Map<String, SearchSequenceUnit> urlPathMap = pathPageMap.get(stc);
                for (Map.Entry<String, SearchSequenceUnit> entry : urlPathMap.entrySet()) {
                    if (RegExp.find(url, entry.getKey())) {
                        SearchSequenceUnit unit = entry.getValue();
                        result = unit.getDepth();
                        curr.setpId(unit.getPage().getId());
                        break;
                    }
                }

                if (StringUtils.isEmpty(curr.getpId())) {
                    List<SearchSequenceUnit> units = depthPageMap.get(stc).get(result);
                    if (CollectionUtils.isNotEmpty(units)) {
                        curr.setpId(units.get(0).getPage().getId());
                    }
                }
            }
        } else {
            logger.warn("not find SearchTemplateConfig for id:" + templateId);
        }
        curr.setDepth(result);
    }

    public boolean needProxy() {
        return proxyConf != null || supportProxy();
    }

    public boolean needProxyByUrl(String url) {
        if (supportProxy()) {
            return true;
        }

        if (null == proxyConf || StringUtils.isEmpty(url)) {
            return false;
        }

        String pattern = proxyConf.getPattern();

        logger.debug("Proxy-conf >>> pattern: {}, proxy: {}, url: {}", pattern, proxyConf.getProxy(), url);

        if (StringUtils.isEmpty(pattern)) {
            // If not, will maintain the original logic
            return StringUtils.isNotBlank(proxyConf.getProxy());
        }

        try {
            return RegExp.find(url, pattern);
        } catch (Exception e) {
            logger.error("Unexpected exception!", e);
        }
        return false;
    }

    public com.treefinance.crawler.framework.proxy.Proxy getProxy(String url) throws Exception {
        return getProxy(url, false);
    }

    public com.treefinance.crawler.framework.proxy.Proxy getProxy(String url, boolean strict) throws Exception {
        if (needProxyByUrl(url)) {
            com.treefinance.crawler.framework.proxy.Proxy proxy = getProxy();

            if (proxy == null) {
                if (strict)
                    throw new NoProxyException("Not found available proxy in remote server! >>> " + url);

                logger.warn("Not found available proxy in remote server! >>> " + url);
            }

            return proxy;
        }
        return null;
    }

    public com.treefinance.crawler.framework.proxy.Proxy getProxy() throws Exception {
        return getProxyManager().getProxy();
    }

    public SearchTemplateConfig getSearchTemplateConfig(String id) {
        return searchTemplateConfigMap.get(id);
    }

    /**
     * @see #isRedirectUriEscaped() ()
     */
    @Deprecated
    public Boolean getRedirectUriEscaped() {
        return isRedirectUriEscaped();
    }

    /**
     * @see #isAllowCircularRedirects()
     */
    @Deprecated
    public Boolean getAllowCircularRedirects() {
        return isAllowCircularRedirects();
    }

    public LoginConfig getLoginConfig() {
        return getSearchConfig().getLoginConfig();
    }

    public boolean needLogin() {
        return !isLoginCheckIgnore() && getLoginConfig() != null && getLoginConfig().getType() != LoginType.NONE;
    }

    /**
     * 目前当LoginType为plugin时，一般都需要前后端交互
     *
     * @return
     */
    public boolean needInteractive() {
        return needLogin() && getLoginConfig().getType() == LoginType.PLUGIN;
    }

    /**
     * @return the defaultHeader
     */
    public Map<String, String> getDefaultHeader() {
        return defaultHeader;
    }

    /**
     * @return the loginCheckIgnore
     */
    public boolean isLoginCheckIgnore() {
        return loginCheckIgnore;
    }

    /**
     * @param loginCheckIgnore the loginCheckIgnore to set
     */
    public void setLoginCheckIgnore(boolean loginCheckIgnore) {
        this.loginCheckIgnore = loginCheckIgnore;
    }

    /**
     * @return search config
     */
    public SearchConfig getSearchConfig() {
        return getWebsite().getSearchConfig();
    }

    /**
     * @return common properties for searching
     */
    public Properties getSearchProperties() {
        return getSearchConfig() != null ? getSearchConfig().getProperties() : null;
    }

    /**
     * @return immutable list of search template configuration.
     */
    public List<SearchTemplateConfig> getSearchTemplates() {
        SearchConfig searchConfig = getSearchConfig();
        if (searchConfig != null) {
            return ImmutableList.copyOf(searchConfig.getSearchTemplateConfigList());
        }

        return Collections.emptyList();
    }

    public UnicodeMode getUnicodeMode() {
        Properties properties = getSearchProperties();
        return properties != null ? properties.getUnicodeMode() : null;
    }

    public boolean isRedirectUriEscaped() {
        Properties properties = this.getSearchProperties();
        return properties != null && (properties.getRedirectUriEscaped() == null || properties.getRedirectUriEscaped());
    }

    public boolean isAllowCircularRedirects() {
        Properties properties = this.getSearchProperties();
        return properties != null && (properties.getAllowCircularRedirects() == null || properties.getAllowCircularRedirects());
    }

    public String getHttpClientType() {
        Properties properties = this.getSearchProperties();
        if (properties != null) {
            return properties.getHttpClientType();
        }
        return null;
    }

    public LoginResource getLoginResource() {
        return loginResource;
    }

    public void setLoginResource(LoginResource loginResource) {
        this.loginResource = loginResource;
    }

    public String getLoginAccountKey() {
        return ProcessorContextUtil.getAccountKey(this);
    }

    public WebsiteAccount getLoginAccount() {
        if (loginResource == null) {
            return null;
        }

        return loginResource.getAccount(getLoginAccountKey());
    }

    public Cookie getLoginCookies() {
        if (loginResource == null) {
            return null;
        }

        return loginResource.getCookie(getLoginAccountKey());
    }

    public Decoder getUnicodeDecoder() {
        UnicodeMode unicodeMode = this.getUnicodeMode();
        if (unicodeMode != null) {
            return DecoderFactory.getDecoder(unicodeMode);
        }
        return null;
    }

    @Override
    public void setCookies(String cookies) {
        if (getCookieConf() != null) {
            super.setCookies(cookies, getCookieConf().getRetainQuote());
        } else {
            super.setCookies(cookies);
        }
    }

    private Page pageVisitCountCheck(Page page) {
        Integer count = pageVisitCountMap.get(page);
        if (count == null) {
            count = 1;
            pageVisitCountMap.put(page, count);
        } else {
            pageVisitCountMap.put(page, ++count);
        }
        if (page.getMaxPageCount() != null && page.getMaxPageCount() < count - 1) {
            logger.info("page " + page + " reach the max visitCount " + page.getMaxPageCount() + ", return empty");
            return null;
        }
        return page;
    }

    private boolean supportProxy() {
        return taskId != null && ProxyUtils.getProxyEnable(taskId);
    }
}
