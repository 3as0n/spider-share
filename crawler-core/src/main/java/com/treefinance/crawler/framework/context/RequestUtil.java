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

import com.treefinance.crawler.framework.config.xml.page.Page;
import com.treefinance.crawler.framework.consts.Constants;
import com.treefinance.crawler.framework.context.function.LinkNode;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.process.search.URLHandler;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Mar 3, 2014 8:14:36 PM
 */
public class RequestUtil {

    public static LinkNode getCurrentUrl(SpiderRequest req) {
        return (LinkNode)req.getAttribute(Constants.CURRENT_LINK_NODE);
    }

    public static void setCurrentUrl(SpiderRequest req, LinkNode node) {
        req.setAttribute(Constants.CURRENT_LINK_NODE, node);
    }

    public static Integer getRetryCount(SpiderRequest req) {
        return (Integer)req.getAttribute(Constants.CRAWLER_REQUEST_RETRY_COUNT);
    }

    public static void setRetryCount(SpiderRequest req, Integer count) {
        req.setAttribute(Constants.CRAWLER_REQUEST_RETRY_COUNT, count);
    }

    public static URLHandler getURLHandler(SpiderRequest req) {
        return (URLHandler)req.getAttribute(Constants.CRAWLER_REQUEST_URL_HANDLER);
    }

    public static void setURLHandler(SpiderRequest req, URLHandler handler) {
        req.setAttribute(Constants.CRAWLER_REQUEST_URL_HANDLER, handler);
    }

    public static String getCurrentTemplateId(SpiderRequest req) {
        return (String)req.getAttribute(Constants.CURRENT_SEARCH_TEMPLATE);
    }

    public static void setCurrentTemplateId(SpiderRequest req, String content) {
        req.setAttribute(Constants.CURRENT_SEARCH_TEMPLATE, content);
    }

    public static String getContent(SpiderRequest req) {
        return (String)req.getInput();
    }

    public static void setContent(SpiderRequest req, String content) {
        req.setInput(content);
    }

    public static String getKeyWord(SpiderRequest req) {
        return (String)req.getAttribute(Constants.CRAWLER_REQUEST_KEYWORD);
    }

    public static void setKeyWord(SpiderRequest req, String keyword) {
        req.setAttribute(Constants.CRAWLER_REQUEST_KEYWORD, keyword);
    }

    public static String getContentCharset(SpiderRequest req) {
        return (String)req.getAttribute(Constants.CRAWLER_PAGECONTENT_CHARSET);
    }

    public static void setContentCharset(SpiderRequest req, String charset) {
        req.setAttribute(Constants.CRAWLER_PAGECONTENT_CHARSET, charset);
    }

    public static String getSearchTemplate(SpiderRequest req) {
        return (String)req.getAttribute(Constants.CRAWLER_REQUEST_TEMPLATE);
    }

    public static void setSearchTemplate(SpiderRequest req, String template) {
        req.setAttribute(Constants.CRAWLER_REQUEST_TEMPLATE, template);
    }

    @Deprecated
    public static String getAttribute(SpiderRequest req, String key) {
        return (String)req.getAttribute(Constants.REQUEST_PREFIX + key);
    }

    @Deprecated
    public static void setAttribute(SpiderRequest req, String key, Object obj) {
        req.setAttribute(Constants.REQUEST_PREFIX + key, obj);
    }

    /**
     * @see #getCurrentPage(SpiderRequest)
     */
    @Deprecated
    public static Page getCurrenPage(SpiderRequest req) {
        return getCurrentPage(req);
    }

    public static Page getCurrentPage(SpiderRequest req) {
        return (Page)req.getAttribute(Constants.CURRENT_PAGE);
    }

    public static void setCurrentPage(SpiderRequest req, Page page) {
        req.setAttribute(Constants.CURRENT_PAGE, page);
    }

}
