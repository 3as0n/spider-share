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

package com.treefinance.crawler.framework.config.xml.search;

import com.treefinance.crawler.framework.config.annotation.ChildTag;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Feb 8, 2014 5:09:54 PM
 */
public class Request implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 228030808365581545L;

    private Integer maxPages;

    private String noResultPattern;

    private String blockPattern;

    private String lastPagePattern;

    private List<String> searchTemplateList;

    // add in vtboss1.0.1
    private String reVisitPattern;

    private Boolean fullSearchSign = true;

    private Integer maxExecuteMinutes;

    private Integer visitTimeOut;

    private String defaultHeader;

    public Request() {
        super();
        searchTemplateList = new ArrayList<String>();
    }

    @Tag("visit-time-out")
    public Integer getVisitTimeOut() {
        return visitTimeOut;
    }

    @Node("visit-time-out/text()")
    public void setVisitTimeOut(Integer visitTimeOut) {
        this.visitTimeOut = visitTimeOut;
    }

    @Tag("max-page")
    public Integer getMaxPages() {
        return maxPages;
    }

    @Node("max-page/text()")
    public void setMaxPages(Integer maxPages) {
        this.maxPages = maxPages;
    }

    @Tag("no-search-results-pattern")
    public String getNoResultPattern() {
        return noResultPattern;
    }

    @Node("no-search-results-pattern/text()")
    public void setNoResultPattern(String noResultPattern) {
        this.noResultPattern = noResultPattern;
    }

    @Tag("block-pattern")
    public String getBlockPattern() {
        return blockPattern;
    }

    @Node("block-pattern/text()")
    public void setBlockPattern(String blockPattern) {
        this.blockPattern = blockPattern;
    }

    @Tag("last-page-pattern")
    public String getLastPagePattern() {
        return lastPagePattern;
    }

    @Node("last-page-pattern/text()")
    public void setLastPagePattern(String lastPagePattern) {
        this.lastPagePattern = lastPagePattern;
    }

    @ChildTag("url-templates/url-template")
    public List<String> getSearchTemplateList() {
        return Collections.unmodifiableList(searchTemplateList);
    }

    @Node("url-templates/url-template/text()")
    public void setSearchTemplateList(String searchTemplate) {
        this.searchTemplateList.add(searchTemplate);
    }

    @Tag("re-visit-pattern")
    public String getReVisitPattern() {
        return reVisitPattern;
    }

    @Node("re-visit-pattern/text()")
    public void setReVisitPattern(String reVisitPattern) {
        this.reVisitPattern = reVisitPattern;
    }

    @Tag("full-search-sign")
    public Boolean getFullSearchSign() {
        return fullSearchSign == null ? Boolean.TRUE : fullSearchSign;
    }

    @Node("full-search-sign/text()")
    public void setFullSearchSign(Boolean fullSearchSign) {
        this.fullSearchSign = fullSearchSign;
    }

    @Tag("max-execute-minutes")
    public Integer getMaxExecuteMinutes() {
        return maxExecuteMinutes;
    }

    @Node("max-execute-minutes/text()")
    public void setMaxExecuteMinutes(Integer maxExecuteMinutes) {
        this.maxExecuteMinutes = maxExecuteMinutes;
    }

    @Tag("default-header")
    public String getDefaultHeader() {
        return defaultHeader;
    }

    @Node("default-header/text()")
    public void setDefaultHeader(String defaultHeader) {
        this.defaultHeader = defaultHeader;
    }

}
