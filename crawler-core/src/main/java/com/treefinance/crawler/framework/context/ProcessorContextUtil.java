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

import com.treefinance.crawler.framework.consts.Constants;
import com.treefinance.crawler.framework.context.function.LinkNode;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.login.Cookie;
import com.treefinance.crawler.framework.login.WebsiteAccount;
import org.apache.commons.httpclient.HttpState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Mar 3, 2014 8:14:36 PM
 */
public class ProcessorContextUtil {

    public static void setCookieString(AbstractProcessorContext context, String cookieString) {
        context.setCookies(cookieString);
    }

    public static Map<String, String> getCookieMap(AbstractProcessorContext context) {
        return context.getCookiesAsMap();
    }

    public static String getCookieString(AbstractProcessorContext context) {
        return context.getCookiesAsString();
    }

    public static void setCookieObject(AbstractProcessorContext context, Cookie cookie) {
        context.setAttribute(Constants.USERNAME, cookie.getUserName());
        context.setCookies(cookie.getCookies());
    }

    public static void setAccountKey(AbstractProcessorContext context, String accountKey) {
        context.setAttribute(Constants.ACCOUNT_KEY, accountKey);
    }

    public static String getAccountKey(AbstractProcessorContext context) {
        return (String)context.getAttribute(Constants.ACCOUNT_KEY);
    }

    public static void setAccount(AbstractProcessorContext context, WebsiteAccount account) {
        context.setAttribute(Constants.USERNAME, account.getUserName());
        context.setAttribute(Constants.PASSWORD, account.getPassword());
    }

    public static void setValue(AbstractProcessorContext context, String key, Object value) {
        context.setAttribute(key, value);
    }

    public static void addValues(AbstractProcessorContext context, Map<String, Object> values) {
        context.addAttributes(values);
    }

    public static Object getValue(AbstractProcessorContext context, String key) {
        return context.getAttribute(key);
    }

    public static void setKeyword(AbstractProcessorContext context, String keyword) {
        context.setAttribute(Constants.PAGE_REQUEST_CONTEXT_KEYWORD, keyword);
        context.setAttribute(Constants.PAGE_REQUEST_CONTEXT_ORIGINAL_KEYWORD, keyword);
    }

    public static void setTaskUnique(AbstractProcessorContext context, Object obj) {
        context.setAttribute(Constants.TASK_UNIQUE_SIGN, obj);
    }

    public static Object getTaskUnique(AbstractProcessorContext context) {
        return context.getAttribute(Constants.TASK_UNIQUE_SIGN);
    }

    public static void addThreadLocalResponse(AbstractProcessorContext context, SpiderResponse response) {
        Object responseList = context.computeThreadAttrIfAbsent(Thread.currentThread(), Constants.THREAD_LOCAL_RESPONSE, k -> new ArrayList<SpiderResponse>());
        ((List)responseList).add(response);
    }

    public static List<SpiderResponse> getThreadLocalResponseList(AbstractProcessorContext context) {
        return (List<SpiderResponse>)context.getThreadAttr(Thread.currentThread(), Constants.THREAD_LOCAL_RESPONSE);
    }

    public static void clearThreadLocalResponseList(AbstractProcessorContext context) {
        context.removeThreadAttr(Thread.currentThread(), Constants.THREAD_LOCAL_RESPONSE);
    }

    public static void addThreadLocalLinkNode(AbstractProcessorContext context, LinkNode linkNode) {
        Object linkNodeList = context.computeThreadAttrIfAbsent(Thread.currentThread(), Constants.THREAD_LOCAL_LINKNODE, k -> new ArrayList<LinkNode>());
        ((List)linkNodeList).add(linkNode);
    }

    public static List<LinkNode> getThreadLocalLinkNode(AbstractProcessorContext context) {
        return (List<LinkNode>)context.getThreadAttr(Thread.currentThread(), Constants.THREAD_LOCAL_LINKNODE);
    }

    public static void clearThreadLocalLinkNode(AbstractProcessorContext context) {
        context.removeThreadAttr(Thread.currentThread(), Constants.THREAD_LOCAL_LINKNODE);
    }

    public static void setHttpState(AbstractProcessorContext context, HttpState state) {
        context.setAttribute(Constants.HTTP_STATE, state);
    }

    public static HttpState getHttpState(AbstractProcessorContext context) {
        return (HttpState)context.getAttribute(Constants.HTTP_STATE);
    }

}
