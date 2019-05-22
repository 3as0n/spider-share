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

package com.treefinance.crawler.framework.protocol;

import com.treefinance.crawler.framework.protocol.util.HeaderParser;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 13, 2014 10:53:22 AM
 */
public class ProtocolInput {

    public static final Logger log = LoggerFactory.getLogger(ProtocolInput.class);

    protected static final String URL = "ProtocolInput.URL";

    protected static final String PROXY = "ProtocolInput.PROXY";

    protected static final String HEADER = "ProtocolInput.HEADER";

    protected static final String COOKIE = "ProtocolInput.COOKIE";

    protected static final String LAST_MODIFY = "ProtocolInput.LAST_MODIFY";

    protected static final String FOLLOW_REDIRECT = "ProtocolInput.FOLLOW_REDIRECT";

    protected static final String REDIRECT_URI_ESCAPED = "ProtocolInput.REDIRECT_URI_ESCAPED";

    protected static final String COOKIE_CO_EXIST = "ProtocolInput.COOKIE_CO_EXIST";

    protected static final String ALLOW_CIRCULAR_REDIRECTS = "ProtocolInput.ALLOW_CIRCULAR_REDIRECTS";

    protected static final String STATES = "ProtocolInput.STATES";

    protected static final String POST_BODY = "ProtocolInput.POST_BODY";

    protected static final String POST_STRING_BODY = "ProtocolInput.POST_STRING_BODY";

    protected static final String REQUEST_HEADERS = "ProtocolInput.REQUEST_HEADER";

    private final Map<String, Object> context = new HashMap<String, Object>();

    private Action action = Action.GET;

    private CookieScope scope = CookieScope.REQUEST;

    public CookieScope getCookieScope() {
        return scope;
    }

    public ProtocolInput setCookieScope(CookieScope scope) {
        this.scope = scope;
        return this;
    }

    public String getUrl() {
        return getString(URL);
    }

    public ProtocolInput setUrl(String url) {

        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        context.put(URL, url);
        return this;
    }

    public String getProxy() {
        return getString(PROXY);
    }

    public ProtocolInput setProxy(String proxy) {
        put(PROXY, proxy);
        return this;
    }

    @SuppressWarnings("unchecked")
    public List<NameValuePair> getHeaders() {
        return getListWithNew(HEADER);
    }

    /**
     * add request headers the json list is key value pairs of headers
     *
     * @param jsonHeaderList
     * @return
     */
    public ProtocolInput addHeader(String jsonHeaderList) {
        List<NameValuePair> headers = HeaderParser.getHeaders(jsonHeaderList);
        List<NameValuePair> orginalHeaders = getListWithNew(HEADER);
        orginalHeaders.addAll(headers);
        return this;
    }

    /**
     * add request header
     *
     * @param key
     * @param value
     * @return
     */
    public ProtocolInput addHeader(String key, String value) {
        List<NameValuePair> orginalHeaders = getListWithNew(HEADER);
        orginalHeaders.add(new NameValuePair(key, value));
        return this;
    }

    /**
     * add request header
     *
     * @param key
     * @param value
     * @return
     */
    public ProtocolInput addHeaders(Map<String, String> headers) {
        List<NameValuePair> orginalHeaders = getListWithNew(HEADER);
        orginalHeaders.addAll(mapToList(headers));
        return this;
    }

    public ProtocolInput clearHeaders() {
        getListWithNew(HEADER).clear();
        return this;
    }

    /**
     * add post parameters
     *
     * @param key
     * @param value
     * @return
     */
    public ProtocolInput addPostRequestParam(String key, String value) {
        List<NameValuePair> orginalHeaders = getListWithNew(POST_BODY);
        orginalHeaders.add(new NameValuePair(key, URLDecoder.decode(value)));
        return this;
    }

    /**
     * get string entity as post content body
     *
     * @return
     */
    public String getPostBody() {
        return getString(POST_STRING_BODY);
    }

    /**
     * set raw string as post body this will ignore url parameters
     *
     * @param content
     * @return
     */
    public ProtocolInput setPostBody(String content) {
        put(POST_STRING_BODY, content);
        return this;
    }

    public List<NameValuePair> getPostRequestParam() {
        return getListWithNew(POST_BODY);
    }

    // 增加cookie path 的sate
    public String getCookie() {
        return getString(COOKIE);
    }

    public ProtocolInput setCookie(String cookie) {
        put(COOKIE, cookie);
        return this;
    }

    public Map<String, Cookie> getCookies() {
        return (Map)get(COOKIE);
    }

    public Action getAction() {
        return action;
    }

    public ProtocolInput setAction(Action action) {
        this.action = action;
        return this;
    }

    public long getLastModify() {
        long lastModify = getLong(LAST_MODIFY, 0);
        return lastModify;
    }

    public ProtocolInput setLastModify(long lastModified) {
        put(LAST_MODIFY, (lastModified));
        return this;
    }

    // default true
    public boolean getRedirectUriEscaped() {
        Boolean bol = (Boolean)context.get(REDIRECT_URI_ESCAPED);
        if (bol == null) {
            bol = true;
        }
        return bol;
    }

    public ProtocolInput setRedirectUriEscaped(Boolean redirectUriEscaped) {
        put(REDIRECT_URI_ESCAPED, redirectUriEscaped);
        return this;
    }

    // default true
    public boolean getCoExist() {
        Boolean bol = (Boolean)context.get(COOKIE_CO_EXIST);
        if (bol == null) {
            bol = true;
        }
        return bol;
    }

    public ProtocolInput setCoExist(Boolean coexist) {
        put(COOKIE_CO_EXIST, coexist);
        return this;
    }

    public boolean getAllowCircularRedirects() {
        Boolean bol = (Boolean)context.get(ALLOW_CIRCULAR_REDIRECTS);
        if (bol == null) {
            bol = false;
        }
        return bol;
    }

    public ProtocolInput setAllowCircularRedirects(Boolean allowCircularRedirects) {
        put(ALLOW_CIRCULAR_REDIRECTS, allowCircularRedirects);
        return this;
    }

    public boolean getFollowRedirect() {
        Boolean bol = (Boolean)context.get(FOLLOW_REDIRECT);
        if (bol == null) {
            bol = true;
        }
        return bol;
    }

    public ProtocolInput setFollowRedirect(Boolean followRedirect) {
        put(FOLLOW_REDIRECT, followRedirect);
        return this;
    }

    public HttpState getState() {
        return (HttpState)get(STATES);
    }

    public ProtocolInput setState(HttpState state) {
        put(STATES, state);
        return this;
    }

    public Header[] getRequestHeaders() {
        return (Header[])get(REQUEST_HEADERS);
    }

    public void setRequestHeaders(Header[] headers) {
        put(REQUEST_HEADERS, headers);
    }

    protected ProtocolInput put(String key, Object val) {
        context.put(key, val);
        return this;
    }

    protected Object get(String key) {
        return context.get(key);
    }

    protected String getString(String key) {
        return (String)(context.get(key));
    }

    protected String getString(String key, String defaultS) {
        String actual = (String)(context.get(key));
        if (actual == null) {
            actual = defaultS;
        }
        return actual;
    }

    protected Long getLong(String key, long defaultS) {
        Long actual = (Long)(context.get(key));
        if (actual == null) {
            actual = defaultS;
        }
        return actual;
    }

    /**
     * @param headers
     * @return
     */
    private Collection<? extends NameValuePair> mapToList(Map<String, String> headers) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        if (MapUtils.isNotEmpty(headers)) {
            for (Map.Entry<String, String> next : headers.entrySet()) {
                pairs.add(new NameValuePair(next.getKey(), next.getValue()));
            }
        }
        return pairs;
    }

    @SuppressWarnings("unchecked")
    private List getListWithNew(String key) {
        List orginalHeaders = (List)get(key);
        if (orginalHeaders == null) {
            orginalHeaders = new ArrayList<Header>();
            put(key, orginalHeaders);
        }
        return orginalHeaders;
    }

    public enum Action {
        GET,
        POST,
        POST_STRING,
        POST_FILE,
        PUT,
        DELETE;
    }

    public enum CookieScope {
        REQUEST,
        USER_SESSION,
        SESSION;

        private boolean retainQuote;

        /**
         * @return the retainQuote
         */
        public boolean isRetainQuote() {
            return retainQuote;
        }

        /**
         * @param retainQuote the retainQuote to set
         */
        public CookieScope setRetainQuote(boolean retainQuote) {
            this.retainQuote = retainQuote;
            return this;
        }
    }

}
