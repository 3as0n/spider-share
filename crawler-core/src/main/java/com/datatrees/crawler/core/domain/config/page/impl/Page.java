/**
 * This document and its contents are protected by copyright 2015 and owned by datatrees.com Inc.
 * The copying and reproduction of this document and/or its content (whether wholly or partly) or
 * any incorporation of the same into any other material in any media or format of any kind is
 * strictly prohibited. All rights are reserved.
 * Copyright (c) datatrees.com Inc. 2015
 */

package com.datatrees.crawler.core.domain.config.page.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.datatrees.crawler.core.domain.config.page.AbstractPage;
import com.datatrees.crawler.core.domain.config.page.PageContentExtractor;
import com.datatrees.crawler.core.domain.config.page.ReplaceMent;
import com.datatrees.crawler.core.domain.config.service.AbstractService;
import com.datatrees.crawler.core.util.xml.annotation.Attr;
import com.datatrees.crawler.core.util.xml.annotation.Node;
import com.datatrees.crawler.core.util.xml.annotation.Tag;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Feb 7, 2014 2:02:36 PM
 */
@Tag("page")
public class Page extends AbstractPage {

    private String               path;
    private String               contentRegex;
    private String               pageNumRegex;
    private String               contentPageRegex;
    private AbstractService      service;
    private String               pageTitleRegex;
    private Boolean              urlExtract;
    private List<ReplaceMent>    replaceMentList;
    private PageContentExtractor pageContentExtractor;
    private Boolean              redirectUrlAdd;
    private Integer              maxPageCount;
    private Boolean              responseCheck;
    private String               failedCodePattern;
    private String               pageFailedPattern;
    /* config for wbsite trigger temporary blockade */
    private String               pageRetryPattern;
    private Integer              retrySleepSecond;/* unit:s */
    private RetryMode            retryMode;

    public Page() {
        super();
        replaceMentList = new ArrayList<ReplaceMent>();
    }

    @Attr("page-failed-pattern")
    public String getPageFailedPattern() {
        return pageFailedPattern;
    }

    @Node("@page-failed-pattern")
    public void setPageFailedPattern(String pageFailedPattern) {
        this.pageFailedPattern = pageFailedPattern;
    }

    @Attr("retry-sleep-second")
    public Integer getRetrySleepSecond() {
        return retrySleepSecond;
    }

    @Node("@retry-sleep-second")
    public void setRetrySleepSecond(Integer retrySleepSecond) {
        this.retrySleepSecond = retrySleepSecond;
    }

    @Attr("retry-mode")
    public RetryMode getRetryMode() {
        return retryMode;
    }

    @Node("@retry-mode")
    public void setRetryMode(String retryMode) {
        this.retryMode = RetryMode.getRetryMode(retryMode);
    }

    @Attr("page-retry-pattern")
    public String getPageRetryPattern() {
        return pageRetryPattern;
    }

    @Node("@page-retry-pattern")
    public void setPageRetryPattern(String pageRetryPattern) {
        this.pageRetryPattern = pageRetryPattern;
    }

    @Attr("failed-code-pattern")
    public String getFailedCodePattern() {
        return failedCodePattern;
    }

    @Node("@failed-code-pattern")
    public void setFailedCodePattern(String failedCodePattern) {
        this.failedCodePattern = failedCodePattern;
    }

    @Attr("response-check")
    public Boolean getResponseCheck() {
        return responseCheck;
    }

    @Node("@response-check")
    public void setResponseCheck(Boolean responseCheck) {
        this.responseCheck = responseCheck;
    }

    @Attr("redirect-url-add")
    public Boolean getRedirectUrlAdd() {
        return redirectUrlAdd;
    }

    @Node("@redirect-url-add")
    public void setRedirectUrlAdd(Boolean redirectUrlAdd) {
        this.redirectUrlAdd = redirectUrlAdd;
    }

    @Attr("url-extract")
    public Boolean getUrlExtract() {
        return urlExtract;
    }

    @Node("@url-extract")
    public void setUrlExtract(Boolean urlExtract) {
        this.urlExtract = urlExtract;
    }

    @Attr("max-page-count")
    public Integer getMaxPageCount() {
        return maxPageCount;
    }

    @Node("@max-page-count")
    public void setMaxPageCount(Integer maxPageCount) {
        this.maxPageCount = maxPageCount;
    }

    @Attr("content-regex")
    public String getContentRegex() {
        return contentRegex;
    }

    @Node("@content-regex")
    public void setContentRegex(String contentRegex) {
        this.contentRegex = contentRegex;
    }

    @Attr("path")
    public String getPath() {
        return path;
    }

    @Node("@path")
    public void setPath(String path) {
        this.path = path;
    }

    @Attr("page-num-regex")
    public String getPageNumRegex() {
        return pageNumRegex;
    }

    @Node("@page-num-regex")
    public void setPageNumRegex(String pageNumRegex) {
        this.pageNumRegex = pageNumRegex;
    }

    @Attr(value = "service-ref", referenced = true)
    public AbstractService getService() {
        return service;
    }

    @Node(value = "@service-ref", referenced = true)
    public void setService(AbstractService service) {
        this.service = service;
    }

    @Attr("content-page-regex")
    public String getContentPageRegex() {
        return contentPageRegex;
    }

    @Node("@content-page-regex")
    public void setContentPageRegex(String contentPageRegex) {
        this.contentPageRegex = contentPageRegex;
    }

    @Attr("page-title-regex")
    public String getPageTitleRegex() {
        return pageTitleRegex;
    }

    @Node("@page-title-regex")
    public void setPageTitleRegex(String pageTitleRegex) {
        this.pageTitleRegex = pageTitleRegex;
    }

    @Tag("replaces")
    public List<ReplaceMent> getReplaceMentList() {
        return Collections.unmodifiableList(replaceMentList);
    }

    @Node("replaces/replace")
    public void setReplaceMentList(ReplaceMent replaceMent) {
        this.replaceMentList.add(replaceMent);
    }

    @Tag("regex")
    public PageContentExtractor getPageContentExtractor() {
        return pageContentExtractor;
    }

    @Node("regex")
    public void setPageContentExtractor(PageContentExtractor pageContentExtractor) {
        this.pageContentExtractor = pageContentExtractor;
    }

}
