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

package com.treefinance.crawler.framework.context.function;

import com.treefinance.crawler.framework.consts.Constants;
import com.treefinance.crawler.framework.context.RequestUtil;
import com.treefinance.crawler.framework.context.SearchProcessorContext;
import com.treefinance.crawler.framework.process.search.URLHandler;
import com.treefinance.toolkit.util.Preconditions;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Mar 10, 2014 1:48:24 PM
 */
public class CrawlRequest extends Request {

    private CrawlRequest() {}

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getSearchTemplateId() {
        return RequestUtil.getCurrentTemplateId(this);
    }

    private void setSearchTemplateId(String searchTemplateId) {
        RequestUtil.setCurrentTemplateId(this, searchTemplateId);
    }

    public LinkNode getUrl() {
        return RequestUtil.getCurrentUrl(this);
    }

    private void setUrl(LinkNode url) {
        RequestUtil.setCurrentUrl(this, url);
    }

    public String getSearchTemplate() {
        return RequestUtil.getSearchTemplate(this);
    }

    private void setSearchTemplate(String searchTemplate) {
        RequestUtil.setSearchTemplate(this, searchTemplate);
    }

    public URLHandler getUrlHandler() {
        return RequestUtil.getURLHandler(this);
    }

    private void setUrlHandler(URLHandler handler) {
        RequestUtil.setURLHandler(this, handler);
    }

    @Override
    public String toString() {
        return "Request [url=" + getUrl().toString() + ", template=" + getSearchTemplateId() + "]";
    }

    public static class Builder {

        private LinkNode url;
        private SearchProcessorContext searchContext;
        private String templateId;
        private String seedUrl;
        private URLHandler urlHandler;

        private Builder() {}

        public Builder setUrl(LinkNode url) {
            this.url = url;
            return this;
        }

        public Builder setSearchContext(SearchProcessorContext context) {
            this.searchContext = context;
            return this;
        }

        public Builder setTemplateId(String templateId) {
            this.templateId = templateId;
            return this;
        }

        public Builder setSeedUrl(String seedUrl) {
            this.seedUrl = seedUrl;
            return this;
        }

        public Builder setUrlHandler(URLHandler urlHandler) {
            this.urlHandler = urlHandler;
            return this;
        }

        public CrawlRequest build() {
            Preconditions.notNull("searchUrl", url);
            Preconditions.notNull("searchContext", searchContext);
            Preconditions.notEmpty("templateId", templateId);

            CrawlRequest crawlRequest = new CrawlRequest();
            crawlRequest.setUrl(url);
            crawlRequest.setProcessorContext(searchContext);
            crawlRequest.setSearchTemplateId(templateId);
            crawlRequest.setSearchTemplate(seedUrl);
            crawlRequest.setUrlHandler(urlHandler);

            crawlRequest.addVisibleScope(linkNodeToMap(url));

            return crawlRequest;
        }

        private Map<String, Object> linkNodeToMap(LinkNode url) {
            Map<String, Object> map = new HashMap<>();

            map.put(Constants.PAGE_REQUEST_CONTEXT_CURRENT_URL, url.getUrl());
            map.put(Constants.PAGE_REQUEST_CONTEXT_CURRENT_DEPTH, url.getDepth() + "");
            if (StringUtils.isBlank(url.getReferer())) {
                map.put(Constants.PAGE_REQUEST_CONTEXT_REFERER_URL, url.getUrl());
            } else {
                map.put(Constants.PAGE_REQUEST_CONTEXT_REFERER_URL, url.getReferer());
            }
            map.put(Constants.PAGE_REQUEST_CONTEXT_REDIRECT_URL, url.getRedirectUrl());
            map.put(Constants.PAGE_REQUEST_CONTEXT_PAGE_TITLE, url.getPageTitle());
            map.putAll(url.getHeaders());
            map.putAll(url.getPropertys());
            return map;
        }
    }
}
