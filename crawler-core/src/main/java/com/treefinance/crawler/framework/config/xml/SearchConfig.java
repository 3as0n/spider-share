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

import com.treefinance.crawler.framework.config.annotation.ChildTag;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Path;
import com.treefinance.crawler.framework.config.annotation.Tag;
import com.treefinance.crawler.framework.config.xml.filter.UrlFilter;
import com.treefinance.crawler.framework.config.xml.login.LoginConfig;
import com.treefinance.crawler.framework.config.xml.page.Page;
import com.treefinance.crawler.framework.config.xml.parser.Parser;
import com.treefinance.crawler.framework.config.xml.properties.Properties;
import com.treefinance.crawler.framework.config.xml.search.SearchTemplateConfig;
import com.treefinance.crawler.framework.config.xml.service.AbstractService;
import com.treefinance.crawler.framework.config.xml.service.PluginService;
import com.treefinance.crawler.framework.config.xml.service.TaskHttpService;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Jan 9, 2014 3:14:15 PM
 */
@Tag("config")
@Path("/config")
public class SearchConfig extends AbstractWebsiteConfig {

    /**
     *
     */
    private static final long serialVersionUID = -3594853402134944912L;

    private List<String> protocolTypeList = new ArrayList<>();

    private List<UrlFilter> urlFilterList = new ArrayList<>();

    private Properties properties;

    private List<AbstractService> serviceList = new ArrayList<>();

    private List<Parser> parserList = new ArrayList<>();

    private List<Page> pageList = new ArrayList<>();

    private List<SearchTemplateConfig> searchTemplateConfigList = new ArrayList<>();

    private LoginConfig loginConfig;

    private List<String> resultTagList = new ArrayList<>();

    public SearchConfig() {
        super();
    }

    @ChildTag("result-tag-list/result-tag")
    public List<String> getResultTagList() {
        return Collections.unmodifiableList(resultTagList);
    }

    @Node("result-tag-list/result-tag/text()")
    public void setResultTagList(String resultTag) {
        this.resultTagList.add(resultTag);
    }

    @ChildTag("protocol-type-list/protocol-type")
    public List<String> getProtocolTypeList() {
        return Collections.unmodifiableList(protocolTypeList);
    }

    @Node("protocol-type-list/protocol-type/text()")
    public void setProtocolTypeList(String protocolTypeList) {
        this.protocolTypeList.add(protocolTypeList);
    }

    @Tag("url-filters")
    public List<UrlFilter> getUrlFilterList() {
        return Collections.unmodifiableList(urlFilterList);
    }

    @Node("url-filters/url-filter")
    public void setUrlFilterList(UrlFilter urlFilter) {
        this.urlFilterList.add(urlFilter);
    }

    @Tag("properties")
    public Properties getProperties() {
        return properties;
    }

    @Node("properties")
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Tag("service-definition")
    public List<AbstractService> getServiceList() {
        return Collections.unmodifiableList(serviceList);
    }

    @Node(value = "service-definition/service", types = {PluginService.class, TaskHttpService.class}, registered = true)
    public void setServiceList(AbstractService service) {
        this.serviceList.add(service);
    }

    @Tag("parser-definition")
    public List<Parser> getParserList() {
        return Collections.unmodifiableList(parserList);
    }

    @Node(value = "parser-definition/parser", registered = true)
    public void setParserList(Parser parser) {
        this.parserList.add(parser);
    }

    @Tag("page-definition")
    public List<Page> getPageList() {
        return Collections.unmodifiableList(pageList);
    }

    @Node(value = "page-definition/page", registered = true)
    public void setPageList(Page page) {
        this.pageList.add(page);
    }

    @Tag("search")
    public List<SearchTemplateConfig> getSearchTemplateConfigList() {
        return Collections.unmodifiableList(searchTemplateConfigList);
    }

    @Node("search/search-template")
    public void setSearchTemplateConfigList(SearchTemplateConfig searchTemplateConfig) {
        this.searchTemplateConfigList.add(searchTemplateConfig);
    }

    @Tag("login")
    public LoginConfig getLoginConfig() {
        return loginConfig;
    }

    @Node("login")
    public void setLoginConfig(LoginConfig loginConfig) {
        this.loginConfig = loginConfig;
    }

    public void clone(SearchConfig cloneFrom) {
        super.clone(cloneFrom);
        if (CollectionUtils.isEmpty(protocolTypeList)) {
            this.protocolTypeList.addAll(cloneFrom.getProtocolTypeList());
        }
        if (CollectionUtils.isEmpty(urlFilterList)) {
            this.urlFilterList.addAll(cloneFrom.getUrlFilterList());
        }
        if (properties == null) {
            this.properties = cloneFrom.getProperties();
        }
        if (CollectionUtils.isEmpty(serviceList)) {
            this.serviceList.addAll(cloneFrom.getServiceList());
        }
        if (CollectionUtils.isEmpty(parserList)) {
            this.parserList.addAll(cloneFrom.getParserList());
        }
        if (CollectionUtils.isEmpty(pageList)) {
            this.pageList.addAll(cloneFrom.getPageList());
        }
        if (CollectionUtils.isEmpty(searchTemplateConfigList)) {
            this.searchTemplateConfigList.addAll(cloneFrom.getSearchTemplateConfigList());
        }
        if (loginConfig == null) {
            this.loginConfig = cloneFrom.getLoginConfig();
        }
        if (CollectionUtils.isEmpty(resultTagList)) {
            this.resultTagList.addAll(cloneFrom.getResultTagList());
        }
    }

}
