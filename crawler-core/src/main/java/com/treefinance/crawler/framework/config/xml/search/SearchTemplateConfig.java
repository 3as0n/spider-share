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

package com.treefinance.crawler.framework.config.xml.search;

import com.treefinance.crawler.framework.config.annotation.Attr;
import com.treefinance.crawler.framework.config.annotation.ChildTag;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Tag;
import com.treefinance.crawler.framework.config.enums.BusinessType;
import com.treefinance.crawler.framework.config.enums.SearchType;
import com.treefinance.crawler.framework.config.xml.AbstractBeanDefinition;
import com.treefinance.crawler.framework.config.xml.plugin.AbstractPlugin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Feb 7, 2014 5:05:45 PM
 */
@Tag("search-template")
public class SearchTemplateConfig extends AbstractBeanDefinition implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -7796504114576595157L;

    // attribute
    private SearchType type;

    private Boolean autoStart;

    private Integer maxDepth;

    private Integer threadCount;

    private Integer waitIntervalMillis;

    private AbstractPlugin plugin;

    private Float weight;

    // 爬取业务标识
    private BusinessType businessType;

    // child nodes
    private Request request;

    private List<SearchSequenceUnit> searchSequence = new ArrayList<>();

    private List<String> resultTagList = new ArrayList<>();

    public SearchTemplateConfig() {
        super();
    }

    @Attr("type")
    public SearchType getType() {
        return type;
    }

    @Node("@type")
    public void setType(String type) {
        this.type = SearchType.getSearchType(type);
    }

    @Attr("auto-start")
    public Boolean getAutoStart() {
        return autoStart == null ? Boolean.TRUE : autoStart;
    }

    @Node("@auto-start")
    public void setAutoStart(Boolean autoStart) {
        this.autoStart = autoStart;
    }

    @Attr("max-depth")
    public Integer getMaxDepth() {
        return maxDepth;
    }

    @Node("@max-depth")
    public void setMaxDepth(Integer maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Attr("thread-count")
    public Integer getThreadCount() {
        return threadCount;
    }

    @Node("@thread-count")
    public void setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
    }

    @Attr("wait-interval")
    public Integer getWaitIntervalMillis() {
        return waitIntervalMillis;
    }

    @Node("@wait-interval")
    public void setWaitIntervalMillis(Integer waitIntervalMillis) {
        this.waitIntervalMillis = waitIntervalMillis;
    }

    @Attr("weight")
    public Float getWeight() {
        return weight;
    }

    @Node("@weight")
    public void setWeight(Float weight) {
        this.weight = weight;
    }

    @Attr(value = "plugin-ref", referenced = true)
    public AbstractPlugin getPlugin() {
        return plugin;
    }

    @Node(value = "@plugin-ref", referenced = true)
    public void setPlugin(AbstractPlugin plugin) {
        this.plugin = plugin;
    }

    @Tag("request")
    public Request getRequest() {
        return request;
    }

    @Node("request")
    public void setRequest(Request request) {
        this.request = request;
    }

    @Tag("page-sequence")
    public List<SearchSequenceUnit> getSearchSequence() {
        return Collections.unmodifiableList(searchSequence);
    }

    @Node("page-sequence/page")
    public void setSearchSequence(SearchSequenceUnit searchSequenceUnit) {
        this.searchSequence.add(searchSequenceUnit);
    }

    @ChildTag("result-tag-list/result-tag")
    public List<String> getResultTagList() {
        return Collections.unmodifiableList(resultTagList);
    }

    @Node("result-tag-list/result-tag/text()")
    public void setResultTagList(String resultTag) {
        this.resultTagList.add(resultTag);
    }

    @Attr("business-type")
    public BusinessType getBusinessType() {
        return businessType;
    }

    @Node("@business-type")
    public void setBusinessType(String businessType) {
        this.businessType = BusinessType.getBusinessType(businessType);
    }

    @Override
    public String toString() {
        return "SearchTemplateConfig [id=" + getId() + " ,type=" + type + ", maxDepth=" + maxDepth + ", autoStart=" + autoStart + ", businessType=" + businessType + "]";
    }

}
