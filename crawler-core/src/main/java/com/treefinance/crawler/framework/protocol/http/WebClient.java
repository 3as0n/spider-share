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

package com.treefinance.crawler.framework.protocol.http;

import com.datatrees.common.conf.Configuration;
import com.datatrees.common.util.ReflectionUtils;
import com.google.common.net.HttpHeaders;
import com.treefinance.crawler.framework.protocol.NameValuePair;
import com.treefinance.crawler.framework.protocol.ProtocolInput;
import com.treefinance.crawler.framework.protocol.ProtocolInput.Action;
import com.treefinance.crawler.framework.protocol.ProtocolInput.CookieScope;
import com.treefinance.crawler.framework.protocol.Response;
import com.treefinance.crawler.framework.protocol.https.EasySSLProtocolSocketFactory;
import com.treefinance.crawler.framework.protocol.metadata.Metadata;
import com.treefinance.crawler.framework.protocol.util.UrlUtils;
import com.treefinance.crawler.framework.util.CharsetUtil;
import com.treefinance.crawler.framework.util.CookiesFormatter;
import com.treefinance.crawler.framework.util.UrlExtractor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.CustomHttpState;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.ProtocolHttpClient;
import org.apache.commons.httpclient.ProxyHost;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.CustomGetMethod;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HostParams;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is a protocol plugin that configures an HTTP client for Basic, Digest and NTLM authentication schemes for
 * web server as well as proxy server. It takes care of HTTPS protocol as well as cookies in a single fetch session.
 * 
 * @author Susam Pal
 */
public class WebClient extends HttpBase {

    private static final Logger LOG = LoggerFactory.getLogger(WebClient.class);

    private MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();

    // Since the Configuration has not yet been set,
    // then an unconfigured client is returned.
    private HttpClient client = new ProtocolHttpClient(connectionManager);

    private int maxThreadsTotal = 400;

    private int maxThreadsPerHost = 200;

    private int maxRedirect = 10;

    private String proxyUsername;

    private String proxyPassword;

    private String proxyRealm;

    private String agentHost;

    /**
     * Constructs this plugin.
     */
    public WebClient() {
        super();
    }

    /**
     * Returns an authentication scope for the specified <code>host</code>, <code>port</code>, <code>realm</code> and
     * <code>scheme</code>.
     * 
     * @param host Host name or address.
     * @param port Port number.
     * @param realm Authentication realm.
     * @param scheme Authentication scheme.
     */
    private static AuthScope getAuthScope(String host, int port, String realm, String scheme) {

        if (host.length() == 0) {
            host = null;
        }

        if (port < 0) {
            port = -1;
        }

        if (realm.length() == 0) {
            realm = null;
        }

        if (scheme.length() == 0) {
            scheme = null;
        }

        return new AuthScope(host, port, realm, scheme);
    }

    /**
     * Returns an authentication scope for the specified <code>host</code>, <code>port</code> and <code>realm</code>.
     * 
     * @param host Host name or address.
     * @param port Port number.
     * @param realm Authentication realm.
     */
    private static AuthScope getAuthScope(String host, int port, String realm) {
        return getAuthScope(host, port, realm, "");
    }

    protected static String[] split(String data, String splitChar) {
        if (StringUtils.isNotEmpty(data) && StringUtils.isNotEmpty(splitChar)) {
            return StringUtils.split(data, splitChar);
        }
        return null;
    }

    /**
     * Returns the configured HTTP client.
     * 
     * @return HTTP client
     */
    public HttpClient getClient() {
        return client;
    }

    /**
     * Reads the configuration from the Nutch configuration files and sets the configuration.
     * 
     * @param conf Configuration
     */
    @Override
    public void setConf(Configuration conf) {
        super.setConf(conf);
        this.maxThreadsTotal = conf.getInt(HTTPConstants.MAX_TOTAL_CONNECTIONS, maxThreadsTotal);
        this.maxThreadsPerHost = conf.getInt(HTTPConstants.MAX_HOST_CONNECTIONS, maxThreadsPerHost);
        this.maxRedirect = conf.getInt(HTTPConstants.MAX_REDIRECTS, maxRedirect);
        this.proxyUsername = conf.get("http.proxy.username", "");
        this.proxyPassword = conf.get("http.proxy.password", "");
        this.proxyRealm = conf.get("http.proxy.realm", "");

        agentHost = conf.get("http.agent.host", "");
        configureClient();
    }

    protected byte[] getResponseContent(String url, HttpMethod method, Metadata headers, int code) throws IOException {
        byte[] content = null;

        int contentLength = Integer.MAX_VALUE;
        String contentLengthString = headers.get(Response.CONTENT_LENGTH);
        if (contentLengthString != null) {
            try {
                contentLength = Integer.parseInt(contentLengthString.trim());
                if (contentLength == 0) {
                    contentLength = getMaxContent();
                }
            } catch (NumberFormatException ex) {
                // ignore
            }
        }

        // limit size
        if (getMaxContent() >= 0 && contentLength > getMaxContent()) {
            contentLength = getMaxContent();
        }

        // always read content. Sometimes content is useful to find a cause
        // for error.
        InputStream in = null;
        try {
            in = method.getResponseBodyAsStream();
            byte[] buffer = new byte[HttpBase.BUFFER_SIZE];
            int bufferFilled = 0;
            int totalRead = 0;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            while ((bufferFilled = in.read(buffer, 0, buffer.length)) != -1 && totalRead + bufferFilled <= contentLength) {
                totalRead += bufferFilled;
                out.write(buffer, 0, bufferFilled);
            }

            // use read length if response header doesn't content length
            if (contentLengthString == null) {
                headers.set(Response.CONTENT_LENGTH, String.valueOf(totalRead));
            }
            content = out.toByteArray();
        } catch (Exception e) {
            if (code == 200) {
                throw new IOException(e.toString());
            }
            // for codes other than 200 OK, we are fine with empty content
        } finally {
            IOUtils.closeQuietly(in);
        }

        // Extract gzip, x-gzip and deflate content
        if (content != null) {
            // check if we have to uncompress it
            String contentEncoding = headers.get(Response.CONTENT_ENCODING);
            if ("gzip".equals(contentEncoding) || "x-gzip".equals(contentEncoding)) {
                content = processGzipEncoded(content, url);
            } else if ("deflate".equals(contentEncoding)) {
                content = processDeflateEncoded(content, url);
            }
        }
        return content;
    }

    protected HostConfiguration getHostConfiguration(String proxy) {
        HostConfiguration configuration = new HostConfiguration();
        ProxyHost host = null;
        if (StringUtils.isNotEmpty(proxy)) {

            if (!proxy.startsWith("http") && !proxy.startsWith("https")) {
                proxy = "http://" + proxy;
            }
            try {
                URI u = new URI(proxy);
                proxy = u.getHost() + u.getPath();
                int port = u.getPort();
                host = new ProxyHost(proxy, port);
            } catch (Exception e) {
                LOG.error("parse proxy server error! " + proxy, e);
            }
        }
        configuration.setProxyHost(host);
        return configuration;
    }

    /**
     * get response from input s
     */
    @Override
    Response getResponse(ProtocolInput input) throws IOException {
        // set up
        Action action = input.getAction();
        CookieScope scope = input.getCookieScope();
        String url = input.getUrl();
        HttpMethod method = getMethodByAction(action, url, input);

        if (url.contains(URLSEPARATOR)) {
            LOG.debug("get header by url...");
            String headerData = StringUtils.substringAfter(url, URLSEPARATOR);
            input.addHeader(headerData);
        }

        HostConfiguration hostConfiguration = getHostConfiguration(input.getProxy());
        fillMethodWithParameter(input, method);
        HttpResponse response = new HttpResponse();

        boolean needManualHandlerRedirect = input.getFollowRedirect();
        // request
        int MAX_REDIRECT_COUTN = 3;
        String orgianlUrl = null;
        for (int i = 0; i < MAX_REDIRECT_COUTN; i++) {
            try {
                // post 302
                if (orgianlUrl != null) {
                    String refer = method.getURI().toString();
                    method = new CustomGetMethod(orgianlUrl).setUriEscaped(input.getRedirectUriEscaped());
                    method.addRequestHeader(HttpHeaders.REFERER, refer);
                    // reset cookie string
                    try {
                        Metadata headers = response.getHeaders();
                        String[] setCookies = headers.getValues(HttpHeaders.SET_COOKIE);
                        String[] setCookies2 = headers.getValues(HttpHeaders.SET_COOKIE2);
                        setCookies = ArrayUtils.addAll(setCookies, setCookies2);

                        final Map<String, String> map = CookiesFormatter.parseAsMap(input.getCookie(), scope.isRetainQuote());
                        Map<String, String> cookieMap = new HashMap<>(map);
                        cookieMap.putAll(CookiesFormatter.parseAsMap(setCookies, scope.isRetainQuote()));
                        String cookieString = CookiesFormatter.toCookiesString(cookieMap);
                        LOG.info("redirect input reset cookie string " + cookieString);
                        input.setCookie(cookieString);
                    } catch (Exception e) {
                        LOG.warn("redirect input reset cookie string error " + e.getMessage());
                    }
                    fillMethodWithParameter(input, method);
                    // clear header except cookie
                    for (String name : response.getHeaders().names()) {
                        if (!name.startsWith(HttpHeaders.SET_COOKIE)) {
                            response.getHeaders().remove(name);
                        }
                    }
                }
                HttpState state = getHttpState(scope, input);
                Metadata headers = response.getHeaders();

                // state = new HttpState();
                int code = getClient().executeMethod(hostConfiguration, method, state);

                // set real request headers
                input.setRequestHeaders(method.getRequestHeaders());

                if (input.getFollowRedirect()) {
                    response.setRedirectUrl(method.getURI().toString());
                }
                printState(state);
                response.setCode(code);
                Header[] heads = method.getResponseHeaders();

                for (int j = 0; j < heads.length; j++) {
                    headers.add(heads[j].getName(), heads[j].getValue());
                }

                // 302 cookie append, 兼容老逻辑
                if (state != null && CookieScope.SESSION != scope) {
                    Cookie[] cks = state.getCookies();
                    for (Cookie cookie : cks) {
                        headers.add(HttpHeaders.SET_COOKIE, CookiesFormatter.toFullString(cookie));
                    }
                }

                // Limit download size
                byte[] content = getResponseContent(url, method, headers, code);
                response.setContent(content);
                response.setState(state);

                if (needManualHandlerRedirect && isRedirectNeeded(method)) {

                    String redirectUrl = processRedirectResponse(method);
                    if (StringUtils.isNotEmpty(redirectUrl)) {
                        orgianlUrl = redirectUrl;
                    } else {
                        break;
                    }

                } else {
                    break;
                }

            } finally {
                method.releaseConnection();
            }
        }

        return response;
    }

    /**
     * connectionParams Configures the HTTP client
     */
    private void configureClient() {

        // Set up an HTTPS socket factory that accepts self-signed certs.
        ProtocolSocketFactory factory = new EasySSLProtocolSocketFactory();
        Protocol https = new Protocol("https", factory, 443);
        Protocol.registerProtocol("https", https);

        HttpConnectionManagerParams connectionParams = connectionManager.getParams();
        connectionParams.setConnectionTimeout(connectionTimeout);
        connectionParams.setSoTimeout(socketTimeout);
        connectionParams.setSendBufferSize(BUFFER_SIZE);
        connectionParams.setReceiveBufferSize(BUFFER_SIZE);
        connectionParams.setMaxTotalConnections(maxThreadsTotal);
        connectionParams.setDefaultMaxConnectionsPerHost(maxThreadsPerHost);
        // connectionParams.setStaleCheckingEnabled(false);

        // executeMethod(HttpMethod) seems to ignore the connection timeout on
        // the connection manager.
        // set it explicitly on the HttpClient.
        client.getParams().setConnectionManagerTimeout(connectionManagerTimeout);
        client.getParams().setIntParameter(HTTPConstants.MAX_REDIRECTS, maxRedirect);
        client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        // set retry handler
        setRetryHandler();

        HostConfiguration hostConf = client.getHostConfiguration();
        ArrayList<Header> headers = new ArrayList<Header>();
        // Set the User Agent in the header
        // headers.add(new Header("User-Agent", userAgent));
        // prefer English
        headers.add(new Header(HttpHeaders.ACCEPT_LANGUAGE, acceptLanguage));
        // prefer UTF-8
        headers.add(new Header(HttpHeaders.ACCEPT_CHARSET, acceptCharset));
        // prefer understandable formats
        // headers.add(new Header("Accept", accept));
        // accept gzipped content
        headers.add(new Header(HttpHeaders.ACCEPT_ENCODING, acceptEncoding));
        // set Connection
        headers.add(new Header(HttpHeaders.CONNECTION, connection));

        client.getParams().setParameter(HostParams.DEFAULT_HEADERS, headers);

        List<String> patterns = new ArrayList<>(
            Arrays.asList(DateUtil.PATTERN_RFC1123, DateUtil.PATTERN_RFC1036, DateUtil.PATTERN_ASCTIME, "EEE, dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MMM-yyyy HH-mm-ss z",
                "EEE, dd MMM yy HH:mm:ss z", "EEE dd-MMM-yyyy HH:mm:ss z", "EEE dd MMM yyyy HH:mm:ss z", "EEE dd-MMM-yyyy HH-mm-ss z", "EEE dd-MMM-yy HH:mm:ss z",
                "EEE dd MMM yy HH:mm:ss z", "EEE,dd-MMM-yy HH:mm:ss z", "EEE,dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MM-yyyy HH:mm:ss z", "EEE MMM dd HH:mm:ss Z yyyy"));

        client.getParams().setParameter(HttpMethodParams.DATE_PATTERNS, patterns);
        // HTTP proxy server details
        if (useProxy) {
            hostConf.setProxy(proxyHost, proxyPort);
            if (proxyUsername.length() > 0) {
                AuthScope proxyAuthScope = getAuthScope(this.proxyHost, this.proxyPort, this.proxyRealm);

                NTCredentials proxyCredentials = new NTCredentials(this.proxyUsername, this.proxyPassword, agentHost, this.proxyRealm);
                client.getState().setProxyCredentials(proxyAuthScope, proxyCredentials);
            }
        }

    }

    /**
     *
     */
    private void setRetryHandler() {
        String retryHandlerClass = conf.get(HttpMethodParams.RETRY_HANDLER);
        HttpMethodRetryHandler retryHandler = null;
        if (StringUtils.isNotEmpty(retryHandlerClass)) {
            try {
                retryHandler = ReflectionUtils.newInstance(retryHandlerClass);
                LOG.debug("find customer retry handler.." + retryHandlerClass);
            } catch (Exception e) {
                LOG.error("load retry handler class error!", e);
            }
        } else {
            int retryCount = conf.getInt(HTTPConstants.HTTP_MAX_RETRY_COUNT, 3);
            retryHandler = new CustomRetryHandler(retryCount);
        }
        if (retryHandler != null) {
            client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, retryHandler);
        }
    }

    private <T extends EntityEnclosingMethod> void setPostBody(String postData, T method) throws UnsupportedEncodingException {
        if (postData != null) {
            try {
                method.setRequestEntity(new StringRequestEntity(postData, null, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                try {
                    method.setRequestEntity(new StringRequestEntity(postData, null, CharsetUtil.getDefaultCharsetName()));
                } catch (UnsupportedEncodingException e1) {
                    throw e1;
                }
                LOG.warn("set postbody " + postData + " error,UnsupportedEncoding.");
            }
        }
    }

    /**
     * @param action
     * @return
     * @exception UnsupportedEncodingException
     * @exception Exception
     */
    private HttpMethod getMethodByAction(Action action, String url, ProtocolInput input) throws UnsupportedEncodingException {
        HttpMethod method = null;
        Action result = action;
        if (url.contains(URLSPLIT)) {
            LOG.debug("find post pattern " + url);
            result = Action.POST;
        }
        switch (result) {
            case GET:
                try {
                    if (!url.contains("%")) {
                        url = new org.apache.commons.httpclient.URI(url, false, "UTF-8").toString();
                    }
                } catch (Exception e) {
                    LOG.error("url escaped error", e);
                }
                method =
                    new CustomGetMethod(url).setUriEscaped(input.getRedirectUriEscaped()).setCoexist(input.getCoExist()).setRetainQuote(input.getCookieScope().isRetainQuote());
                break;
            case POST:
                String postUrl = StringUtils.substringBefore(url, URLSPLIT);
                String postData = StringUtils.substringAfter(url, URLSPLIT);
                method = new PostMethod(postUrl);
                this.setPostBody(postData, (PostMethod)method);
                break;
            case POST_STRING:
                method = new PostMethod(url);
                this.setPostBody(input.getPostBody(), (PostMethod)method);
                break;
            case PUT:
                method = new PutMethod(url);
                this.setPostBody(input.getPostBody(), (PutMethod)method);
                break;
            case DELETE:
                method = new DeleteMethod(url);
                break;
            default:
                method = new CustomGetMethod(url).setUriEscaped(input.getRedirectUriEscaped());
                break;
        }
        return method;
    }

    private String getRedirectURLInRefreshHeader(Header refreshHeader) {
        if (refreshHeader != null) {
            List<String> textUrls = UrlExtractor.extract(refreshHeader.getValue());
            if (CollectionUtils.isNotEmpty(textUrls)) {
                return textUrls.get(0);
            }
        }
        return null;
    }

    private String processRedirectResponse(final HttpMethod method) {
        // get the location header to find out where to redirect to
        Header locationHeader = method.getResponseHeader(HttpHeaders.LOCATION);
        if (locationHeader == null) {
            locationHeader = method.getResponseHeader("location");
        }
        Header refreshHeader = method.getResponseHeader(HttpHeaders.REFRESH);
        if (refreshHeader == null) {
            refreshHeader = method.getResponseHeader("refresh");
        }
        if (locationHeader == null && refreshHeader == null) {
            // got a redirect response, but no location header
            LOG.error("Received redirect response " + method.getStatusCode() + " but no location header");
            return null;
        }
        String result = null;
        try {
            if (locationHeader != null) {
                result = UrlUtils.resolveUrl(method.getURI().toString(), locationHeader.getValue());
            }

            if (result == null) {
                result = getRedirectURLInRefreshHeader(refreshHeader);
            }
        } catch (Exception e) {
            LOG.error("handler redirect error!", e);
        }
        return result;
    }

    /**
     * Tests if the {@link HttpMethod method} requires a redirect to another location.
     *
     * @param method HTTP method
     * @return boolean <tt>true</tt> if a retry is needed, <tt>false</tt> otherwise.
     */
    private boolean isRedirectNeeded(final HttpMethod method) {
        switch (method.getStatusCode()) {
            case HttpStatus.SC_MOVED_TEMPORARILY:
            case HttpStatus.SC_MOVED_PERMANENTLY:
            case HttpStatus.SC_SEE_OTHER:
            case HttpStatus.SC_TEMPORARY_REDIRECT:
                LOG.debug("Redirect required");
                return true;
            default:
                return false;
        } // end of switch
    }

    /**
     * @param state
     */
    private void printState(HttpState state) {
        if (LOG.isDebugEnabled() && state != null) {
            Cookie[] cks = state.getCookies();
            LOG.debug("cookie received..." + cks.length);
            for (Cookie cookie : cks) {
                LOG.debug("cookie:\t" + cookie.getName() + " value: " + cookie.getValue());
            }
        }
    }

    /**
     * @param input
     * @param method
     */
    private void fillMethodWithParameter(ProtocolInput input, HttpMethod method) {
        // add header
        boolean hasContentType = false;
        List<NameValuePair> headers = input.getHeaders();
        if (CollectionUtils.isNotEmpty(headers)) {
            for (NameValuePair hd : headers) {
                method.addRequestHeader(hd.getName(), hd.getValue());
                if (method instanceof PostMethod && HttpHeaders.CONTENT_TYPE.equals(hd.getName())) {
                    hasContentType = true;
                }
            }
        }

        if (method.getRequestHeader(HttpHeaders.ACCEPT) == null) {
            // add default Accept
            method.addRequestHeader(HttpHeaders.ACCEPT, accept);
        }

        if (method.getRequestHeader(HttpHeaders.USER_AGENT) == null) {
            // add default user agent
            method.addRequestHeader(HttpHeaders.USER_AGENT, userAgent);
        }

        if (!(method instanceof EntityEnclosingMethod)) {
            method.setFollowRedirects(input.getFollowRedirect());
        }

        long lastModified = input.getLastModify();
        // set custom parameter... to do

        if (lastModified > 0) {
            method.setRequestHeader(HttpHeaders.IF_MODIFIED_SINCE, HttpDateFormat.toString(lastModified));
        }
        // set default post content type
        if (method instanceof PostMethod && !hasContentType) {
            method.addRequestHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8");
        }

        // Set HTTP parameters
        HttpMethodParams params = method.getParams();
        if (this.getUseHttp11()) {
            params.setVersion(HttpVersion.HTTP_1_1);
        } else {
            params.setVersion(HttpVersion.HTTP_1_0);
        }
        // set SINGLE_COOKIE_HEADER true
        params.setBooleanParameter(HttpMethodParams.SINGLE_COOKIE_HEADER, true);

        if (StringUtils.isNotEmpty(input.getCookie()) && (input.getState() == null || ArrayUtils.isEmpty(input.getState().getCookies()))) {
            method.setRequestHeader(HttpHeaders.COOKIE, input.getCookie());
        }

        params.setBooleanParameter(HttpClientParams.ALLOW_CIRCULAR_REDIRECTS, input.getAllowCircularRedirects());
    }

    /**
     * create httpstate to hold cookie info
     *
     * @param scope
     * @return
     */
    private HttpState getHttpState(CookieScope scope, ProtocolInput input) {
        HttpState state = input.getState();
        if (state == null) {
            if (StringUtils.isBlank(input.getCookie()) && scope == CookieScope.SESSION) {
                state = getClient().getState();
            } else {
                if (BooleanUtils.isTrue(scope.isRetainQuote())) {
                    state = new CustomHttpState();
                } else {
                    state = new HttpState();
                }
            }
            // input.setState(state);
        }
        return state;
    }

}
