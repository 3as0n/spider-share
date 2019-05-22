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

package com.treefinance.crawler.framework.context.function;

import org.apache.commons.collections.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 20, 2014 9:11:19 PM
 */
public class LinkNode {

    private int depth;

    private String url;

    private String referer;

    private String baseUrl;

    private String pageTitle;

    private String redirectUrl;

    /** page id */
    private String pId = "";

    private int retryCount = 0;

    private Map<String, Object> property = new HashMap<String, Object>();

    private Map<String, String> headers = new HashMap<String, String>();

    /** page number */
    private int pNum = -1;

    private boolean isRemoved = false;

    private boolean isHosting = false;

    private String anchorText;

    /* get from parser */
    private boolean isFromParser;

    private boolean needRequeue;

    public LinkNode(String url) {
        this(0, url, null);
    }

    public LinkNode(int depth, String url, String referer) {
        super();
        setDepth(depth).setUrl(url).setReferer(referer);
    }

    public LinkNode(String url, int depth) {
        this(depth, url, null);
    }

    /**
     * @return the needRequeue
     */
    public boolean isNeedRequeue() {
        return needRequeue;
    }

    /**
     * @param needRequeue the needRequeue to set
     */
    public void setNeedRequeue(boolean needRequeue) {
        this.needRequeue = needRequeue;
    }

    /**
     * @return the isFromParser
     */
    public boolean isFromParser() {
        return isFromParser;
    }

    /**
     * @param isFromParser the isFromParser to set
     */
    public void setFromParser(boolean isFromParser) {
        this.isFromParser = isFromParser;
    }

    /**
     * @return the isRemoved
     */
    public boolean isRemoved() {
        return isRemoved;
    }

    /**
     * @param isRemoved the isRemoved to set
     */
    public void setRemoved(boolean isRemoved) {
        this.isRemoved = isRemoved;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public LinkNode setRetryCount(int retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    public void increaseRetryCount() {
        retryCount++;
    }

    public int getDepth() {
        return depth;
    }

    public LinkNode setDepth(int depth) {
        this.depth = depth;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public LinkNode setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getReferer() {
        return referer;
    }

    public LinkNode setReferer(String referer) {
        this.referer = referer;
        return this;
    }

    public LinkNode addProperty(String key, Object val) {
        property.put(key, val);
        return this;
    }

    public LinkNode addPropertys(Map<String, Object> params) {
        property.putAll(params);
        return this;
    }

    public Object getProperty(String key) {
        return property.get(key);
    }

    public Object getStringProperty(String key) {
        Object val = property.get(key);
        if (val != null) {
            val = String.valueOf(val);
        }
        return val;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public LinkNode setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
        return this;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public LinkNode setBaseUrl(String base) {
        this.baseUrl = base;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public LinkNode addHeader(String key, String val) {
        headers.put(key, val);
        return this;
    }

    public LinkNode addHeaders(Map<String, String> headersMap) {
        headers.putAll(headersMap);
        return this;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public LinkNode setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
        return this;
    }

    public String getpId() {
        return pId;
    }

    public LinkNode setpId(String pId) {
        this.pId = pId;
        return this;
    }

    public int getpNum() {
        return pNum;
    }

    public void setpNum(int pNum) {
        this.pNum = pNum;
    }

    public Map<String, Object> getPropertys() {
        return MapUtils.unmodifiableMap(property);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + depth;
        result = prime * result + ((referer == null) ? 0 : referer.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    /**
     * @return the anchorText
     */
    public String getAnchorText() {
        return anchorText;
    }

    /**
     * @param anchorText the anchorText to set
     */
    public LinkNode setAnchorText(String anchorText) {
        this.anchorText = anchorText;
        return this;
    }

    /**
     * @return the isHosting
     */
    public boolean isHosting() {
        return isHosting;
    }

    /**
     * @param isHosting the isHosting to set
     */
    public void setHosting(boolean isHosting) {
        this.isHosting = isHosting;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LinkNode other = (LinkNode)obj;
        if (depth != other.depth)
            return false;
        if (referer == null) {
            if (other.referer != null)
                return false;
        } else if (!referer.equals(other.referer))
            return false;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "LinkNode [depth=" + getDepth() + ", retryCount=" + getRetryCount() + ", url=" + getUrl() + ", referer=" + getReferer() + "]";
    }

}
