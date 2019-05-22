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

package com.treefinance.crawler.framework.config.xml.properties;

import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Tag;
import com.treefinance.crawler.framework.config.enums.properties.UnicodeMode;
import com.treefinance.crawler.framework.config.xml.properties.cookie.AbstractCookie;
import com.treefinance.crawler.framework.config.xml.properties.cookie.BaseCookie;
import com.treefinance.crawler.framework.config.xml.properties.cookie.CustomCookie;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Jan 10, 2014 7:29:16 PM
 */
public class Properties implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1340916732791297838L;

    private Integer waitIntervalMillis;

    private AbstractCookie cookie;

    private Proxy proxy;

    private String encoding;

    private Boolean useTaskHttp;

    private UnicodeMode unicodeMode;

    private String taskRegion;

    private Integer maxRetryCount;

    private Boolean duplicateRemoval;

    private String httpClientType;

    private Boolean redirectUriEscaped;

    private Boolean allowCircularRedirects;

    private Integer captchaCode;

    @Tag("captcha-code")
    public Integer getCaptchaCode() {
        return captchaCode;
    }

    @Node("captcha-code/text()")
    public void setCaptchaCode(Integer captchaCode) {
        this.captchaCode = captchaCode;
    }

    @Tag("redirect-uri-escaped")
    public Boolean getRedirectUriEscaped() {
        return redirectUriEscaped;
    }

    @Node("redirect-uri-escaped/text()")
    public void setRedirectUriEscaped(Boolean redirectUriEscaped) {
        this.redirectUriEscaped = redirectUriEscaped;
    }

    @Tag("use-task-http")
    public Boolean getUseTaskHttp() {
        return useTaskHttp;
    }

    @Node("use-task-http/text()")
    public void setUseTaskHttp(Boolean useTaskHttp) {
        this.useTaskHttp = useTaskHttp;
    }

    @Tag("allow-circular-redirects")
    public Boolean getAllowCircularRedirects() {
        return allowCircularRedirects;
    }

    @Node("allow-circular-redirects/text()")
    public void setAllowCircularRedirects(Boolean allowCircularRedirects) {
        this.allowCircularRedirects = allowCircularRedirects;
    }

    @Tag("http-client-type")
    public String getHttpClientType() {
        return httpClientType;
    }

    @Node("http-client-type/text()")
    public void setHttpClientType(String httpClientType) {
        this.httpClientType = httpClientType;
    }

    /**
     * @return the duplicateRemoval
     */
    @Tag("duplicate-removal")
    public Boolean getDuplicateRemoval() {
        return duplicateRemoval == null ? Boolean.TRUE : duplicateRemoval;
    }

    /**
     * @param duplicateRemoval the duplicateRemoval to set
     */
    @Node("duplicate-removal/text()")
    public void setDuplicateRemoval(Boolean duplicateRemoval) {
        this.duplicateRemoval = duplicateRemoval;
    }

    @Tag("wait-interval")
    public Integer getWaitIntervalMillis() {
        return waitIntervalMillis;
    }

    @Node("wait-interval/text()")
    public void setWaitIntervalMillis(Integer waitIntervalMillis) {
        this.waitIntervalMillis = waitIntervalMillis;
    }

    @Tag("proxy")
    public Proxy getProxy() {
        return proxy;
    }

    @Node("proxy")
    public void setProxy(Proxy proxy) {
        if (proxy != null && StringUtils.isBlank(proxy.getProxy())) {
            this.proxy = null;
        } else {
            this.proxy = proxy;
        }
    }

    @Tag("cookie")
    public AbstractCookie getCookie() {
        return cookie;
    }

    @Node(value = "cookie", types = {CustomCookie.class, BaseCookie.class})
    public void setCookie(AbstractCookie cookie) {
        this.cookie = cookie;
    }

    @Tag("encoding")
    public String getEncoding() {
        return encoding;
    }

    @Node("encoding/text()")
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Tag("unicode-mode")
    public UnicodeMode getUnicodeMode() {
        return unicodeMode;
    }

    @Node("unicode-mode/text()")
    public void setUnicodeMode(String unicodeMode) {
        this.unicodeMode = UnicodeMode.getUnicodeMode(unicodeMode);
    }

    @Tag("task-region")
    public String getTaskRegion() {
        return taskRegion;
    }

    @Node("task-region/text()")
    public void setTaskRegion(String taskRegion) {
        this.taskRegion = taskRegion;
    }

    //
    //
    // /**
    // * @return the saveNextPageNum
    // */
    // @Tag("save-next-page-num")
    // public Boolean getSaveNextPageNum() {
    // return saveNextPageNum == null ? false : saveNextPageNum;
    // }
    //
    // /**
    // * @param saveNextPageNum the saveNextPageNum to set
    // */
    // @Node("save-next-page-num/text()")
    // public void setSaveNextPageNum(Boolean saveNextPageNum) {
    // this.saveNextPageNum = saveNextPageNum;
    // }

    @Tag("max-retry-count")
    public Integer getMaxRetryCount() {
        return maxRetryCount;
    }

    @Node("max-retry-count/text()")
    public void setMaxRetryCount(Integer maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    // @Tag("rate-limit")
    // public Integer getRateLimit() {
    // return rateLimit;
    // }
    //
    // @Node("rate-limit/text()")
    // public void setRateLimit(Integer rateLimit) {
    // this.rateLimit = rateLimit;
    // }

}
