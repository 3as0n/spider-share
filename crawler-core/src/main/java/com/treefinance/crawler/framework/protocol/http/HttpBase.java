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

package com.treefinance.crawler.framework.protocol.http;

// JDK imports

import com.datatrees.common.conf.Configuration;
import com.google.common.net.HttpHeaders;
import com.treefinance.crawler.framework.consts.Constants;
import com.treefinance.crawler.framework.protocol.Content;
import com.treefinance.crawler.framework.protocol.Protocol;
import com.treefinance.crawler.framework.protocol.ProtocolInput;
import com.treefinance.crawler.framework.protocol.ProtocolOutput;
import com.treefinance.crawler.framework.protocol.ProtocolStatusCodes;
import com.treefinance.crawler.framework.protocol.Response;
import com.treefinance.crawler.framework.util.DeflateUtils;
import com.treefinance.crawler.framework.util.GZIPUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

// crawler-commons imports

public abstract class HttpBase implements Protocol {

    public static final int BUFFER_SIZE = 8 * 1024;

    private static final byte[] EMPTY_CONTENT = new byte[0];

    /** The default logger */
    private final static Logger logger = LoggerFactory.getLogger(HttpBase.class);

    /** The proxy hostname. */
    protected String proxyHost = null;

    /** The proxy port. */
    protected int proxyPort = 8080;

    /** Indicates if a proxy is used */
    protected boolean useProxy = false;

    /** The network timeout in millisecond default 3s */
    protected int connectionTimeout = (int)TimeUnit.SECONDS.toMillis(3);

    /** The network read data timeout in 7s */
    protected int socketTimeout = (int)TimeUnit.SECONDS.toMillis(7);

    /** the timeout in milliseconds used when retrieving an http connection from pool */
    protected int connectionManagerTimeout = (int)TimeUnit.SECONDS.toMillis(120);

    /** The length limit for downloaded content, in bytes. */
    protected int maxContent = 8 * 1024 * 1024; // max length 4M

    /** The 'User-Agent' request header */
    protected String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:29.0) Gecko/20100101 Firefox/29.0";

    /** The "Accept-Language" request header value. */
    protected String acceptLanguage = "en-us,en-gb,en;q=0.7,*;q=0.3";

    /** The "Accept" request header value. */
    protected String accept = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";

    /** The "Accept-Encoding" request header value. */
    protected String acceptEncoding = "x-gzip, gzip, deflate";

    /** The "Connection" request header value. */
    protected String connection = "keep-alive";

    /** The "Accept-Charset" request header value. */
    protected String acceptCharset = "utf-8,ISO-8859-1;q=0.7,*;q=0.7";

    /** The nutch configuration */
    protected Configuration conf = null;

    protected String URLSPLIT;

    protected String URLSEPARATOR;

    /** Do we use HTTP/1.1? */
    protected boolean useHttp11 = true;

    /** Creates a new instance of HttpBase */
    public HttpBase() {}

    // Inherited Javadoc
    public Configuration getConf() {
        return this.conf;
    }

    // Inherited Javadoc
    public void setConf(Configuration conf) {
        this.conf = conf;

        this.proxyHost = conf.get(HTTPConstants.HTTP_PROXY_HOST);
        this.proxyPort = conf.getInt(HTTPConstants.HTTP_PROXY_PORT, 8080);
        this.connectionTimeout = conf.getInt(HTTPConstants.CONNECTION_TIMEOUT, connectionTimeout);
        this.socketTimeout = conf.getInt(HTTPConstants.SO_TIMEOUT, socketTimeout);
        this.connectionManagerTimeout = conf.getInt(HTTPConstants.CONNECTION_MANAGER_TIMEOUT, connectionManagerTimeout);
        this.maxContent = conf.getInt(HTTPConstants.HTTP_CONTENT_LIMIT, maxContent);
        this.userAgent = conf.get(HTTPConstants.USER_AGENT, userAgent);
        this.acceptLanguage = conf.get(HTTPConstants.HTTP_ACCEPT_LANGUAGE, acceptLanguage);
        this.accept = conf.get(HTTPConstants.HTTP_ACCEPT, accept);
        this.acceptCharset = conf.get(HTTPConstants.HTTP_ACCEPT_CHARSET, acceptCharset);
        this.acceptEncoding = conf.get(HTTPConstants.HTTP_ACCEPT_ENCODING, acceptEncoding);
        this.connection = conf.get(HTTPConstants.HTTP_CONNECTION, connection);

        this.useHttp11 = conf.getBoolean("http.useHttp11", useHttp11);
        this.useProxy = StringUtils.isNotEmpty(proxyHost);
        this.URLSPLIT = conf.get("url.split", "\"");
        this.URLSEPARATOR = conf.get("url.SEPARATOR", "`");
        logConf();
    }

    public ProtocolOutput getProtocolOutput(String url) {
        return getProtocolOutput(url, 0);
    }

    public ProtocolOutput getProtocolOutput(ProtocolInput input) {
        Response response = null;
        try {
            URL u = new URL(input.getUrl());
            response = getResponse(input); // make a request
            int code = response.getCode();
            byte[] content = response.getContent();
            if (input.getFollowRedirect()) {
                String orignal = input.getUrl();
                orignal = extractPostUrlBase(orignal);
                String redirect = ((HttpResponse)response).getRedirectUrl();
                if (redirect != null && !redirect.equalsIgnoreCase(orignal)) {
                    logger.info("find redirect url soure " + orignal + " redirect to " + redirect);
                    u = new URL(redirect);
                    response.getHeaders().set(Constants.REDIRECT_URL, redirect);
                }
            }

            Content c = new Content(u.toString(), input.getUrl(), (content == null ? EMPTY_CONTENT : content), response.getHeader("Content-Type"), response.getHeaders());
            c.setResponseCode(code);

            if (code == 200) { // got a good response
                return new ProtocolOutput(c, ProtocolStatusCodes.SUCCESS, response); // return it
            } else if (code >= 300 && code < 400) { // handle redirect
                String location = response.getHeader(HttpHeaders.LOCATION);
                // some broken servers, such as MS IIS, use lowercase header name...
                if (location == null)
                    location = response.getHeader("location");
                if (location == null)
                    location = "";
                u = new URL(u, location);
                c.setUrl(u.toString());
                int protocolStatusCode;
                switch (code) {
                    case 300: // multiple choices, preferred value in Location
                        protocolStatusCode = ProtocolStatusCodes.MOVED;
                        break;
                    case 301: // moved permanently
                    case 305: // use proxy (Location is URL of proxy)
                        protocolStatusCode = ProtocolStatusCodes.MOVED;
                        break;
                    case 302: // found (temporarily moved)
                    case 303: // see other (redirect after POST)
                    case 307: // temporary redirect
                        protocolStatusCode = ProtocolStatusCodes.TEMP_MOVED;
                        break;
                    case 304: // not modified
                        protocolStatusCode = ProtocolStatusCodes.NOTMODIFIED;
                        break;
                    default:
                        protocolStatusCode = ProtocolStatusCodes.MOVED;
                }
                // handle this in the higher layer.
                return new ProtocolOutput(c, protocolStatusCode, response);
            } else if (code == 400) { // bad request, mark as GONE
                logger.trace("400 Bad request: " + u);

                return new ProtocolOutput(c, ProtocolStatusCodes.GONE, response);
            } else if (code == 401) { // requires authorization, but no valid auth provided.
                logger.trace("401 Authentication Required");
                return new ProtocolOutput(c, ProtocolStatusCodes.ACCESS_DENIED, response);
            } else if (code == 404) {
                return new ProtocolOutput(c, ProtocolStatusCodes.NOTFOUND, response);
            } else if (code == 410) { // permanently GONE
                return new ProtocolOutput(c, ProtocolStatusCodes.GONE, response);
            } else {
                return new ProtocolOutput(c, ProtocolStatusCodes.SERVER_EXCEPTION, response);
            }
        } catch (Exception e) {
            logger.error("Failed with the following error: " + e);
            return new ProtocolOutput(null, ProtocolStatusCodes.EXCEPTION, response);
        }
    }

    public ProtocolOutput getProtocolOutput(String url, long lastModified) {
        return getProtocolOutput(new ProtocolInput().setUrl(url).setLastModify(lastModified));
    }

    /*
     * -------------------------- * </implementation:Protocol> * --------------------------
     */
    public String getProxyHost() {
        return proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public boolean useProxy() {
        return useProxy;
    }

    public int getMaxContent() {
        return maxContent;
    }

    public String getUserAgent() {
        return userAgent;
    }

    /**
     * Value of "Accept-Language" request header sent by Nutch.
     *
     * @return The value of the header "Accept-Language" header.
     */
    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    public String getAccept() {
        return accept;
    }

    public boolean getUseHttp11() {
        return useHttp11;
    }

    public byte[] processGzipEncoded(byte[] compressed, String url) throws IOException {

        logger.trace("uncompressing....");

        byte[] content;
        if (getMaxContent() >= 0) {
            content = GZIPUtils.unzipBestEffort(compressed, getMaxContent());
        } else {
            content = GZIPUtils.unzipBestEffort(compressed);
        }

        // if (content == null) throw new IOException("unzipBestEffort returned null");
        //
        // LOGGER.trace("fetched " + compressed.length +
        // " bytes of compressed content (expanded to "
        // + content.length + " bytes) from " + url);

        return content;
    }

    public byte[] processDeflateEncoded(byte[] compressed, String url) throws IOException {

        logger.trace("inflating....");

        byte[] content = DeflateUtils.inflateBestEffort(compressed, getMaxContent());

        if (content == null)
            throw new IOException("inflateBestEffort returned null");

        logger.trace("fetched " + compressed.length + " bytes of compressed content (expanded to " + content.length + " bytes) from " + url);

        return content;
    }

    protected void logConf() {
        logger.debug("http.proxy.host = " + proxyHost);
        logger.debug("http.proxy.port = " + proxyPort);
        logger.debug("http.connection.timeout = " + connectionTimeout);
        logger.debug("http.socket.timeout = " + socketTimeout);
        logger.debug("http.connection.managertimeout = " + connectionManagerTimeout);
        logger.debug("http.content.limit = " + maxContent);
        logger.debug("http.agent = " + userAgent);
        logger.debug("http.accept.language = " + acceptLanguage);
        logger.debug("http.accept = " + accept);
    }

    abstract Response getResponse(ProtocolInput input) throws IOException;

    /**
     * @param orignal
     * @return
     */
    private String extractPostUrlBase(String orignal) {

        String result = orignal;
        if (orignal.contains(URLSPLIT)) {
            result = orignal.substring(0, orignal.indexOf(URLSPLIT));
        }
        return result;
    }

}
