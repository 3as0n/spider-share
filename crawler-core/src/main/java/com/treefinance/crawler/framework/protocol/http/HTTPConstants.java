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

import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.cookie.CookiePolicy;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 19, 2014 1:45:04 PM
 */
public interface HTTPConstants {

    public static final String HTTP_HEADER_SET_COOKIE = "Set-Cookie";
    public static final String HTTP_HEADER_SET_COOKIE2 = "Set-Cookie2";
    public static final String HTTP_MAX_RETRY_COUNT = "http.max.retry.count";
    public static final String HTTP_PROXY_HOST = "http.proxy.host";
    public static final String HTTP_PROXY_PORT = "http.proxy.port";
    public static final String HTTP_CONTENT_LIMIT = "http.content.limit";
    public static final String HTTP_ACCEPT = "http.accept";
    public static final String HTTP_ACCEPT_LANGUAGE = "http.accept.language";
    public static final String HTTP_ACCEPT_ENCODING = "http.accept.encoding";
    public static final String HTTP_ACCEPT_CHARSET = "http.accept.charset";
    public static final String HTTP_CONNECTION = "http.connection";
    /**
     * Defines the content of the <tt>User-Agent</tt> header used by {@link org.apache.commons.httpclient.HttpMethod
     * HTTP methods}.
     * <p>
     * This parameter expects a value of type {@link String}.
     * </p>
     */
    public static final String USER_AGENT = "http.useragent";
    /**
     * Defines the {@link HttpVersion HTTP protocol version} used by {@link org.apache.commons.httpclient.HttpMethod
     * HTTP methods} per default.
     * <p>
     * This parameter expects a value of type {@link HttpVersion}.
     * </p>
     */
    public static final String PROTOCOL_VERSION = "http.protocol.version";
    /**
     * Defines whether {@link org.apache.commons.httpclient.HttpMethod HTTP methods} should reject ambiguous
     * {@link org.apache.commons.httpclient.StatusLine HTTP status line}.
     * <p>
     * This parameter expects a value of type {@link Boolean}.
     * </p>
     */
    public static final String UNAMBIGUOUS_STATUS_LINE = "http.protocol.unambiguous-statusline";
    /**
     * Defines whether {@link org.apache.commons.httpclient.Cookie cookies} should be put on a single
     * {@link org.apache.commons.httpclient.Header response header}.
     * <p>
     * This parameter expects a value of type {@link Boolean}.
     * </p>
     */
    public static final String SINGLE_COOKIE_HEADER = "http.protocol.single-cookie-header";
    /**
     * Defines whether responses with an invalid <tt>Transfer-Encoding</tt> header should be rejected.
     * <p>
     * This parameter expects a value of type {@link Boolean}.
     * </p>
     */
    public static final String STRICT_TRANSFER_ENCODING = "http.protocol.strict-transfer-encoding";
    /**
     * Defines whether the content body sent in response to {@link org.apache.commons.httpclient.methods.HeadMethod}
     * should be rejected.
     * <p>
     * This parameter expects a value of type {@link Boolean}.
     * </p>
     */
    public static final String REJECT_HEAD_BODY = "http.protocol.reject-head-body";
    /**
     * Sets period of time in milliseconds to wait for a content body sent in response to
     * {@link org.apache.commons.httpclient.methods.HeadMethod HEAD method} from a non-compliant server. If the
     * parameter is not set or set to <tt>-1</tt> non-compliant response body check is disabled.
     * <p>
     * This parameter expects a value of type {@link Integer}.
     * </p>
     */
    public static final String HEAD_BODY_CHECK_TIMEOUT = "http.protocol.head-body-timeout";
    /**
     * <p>
     * Activates 'Expect: 100-Continue' handshake for the
     * {@link org.apache.commons.httpclient.methods.ExpectContinueMethod entity enclosing methods}. The purpose of the
     * 'Expect: 100-Continue' handshake to allow a client that is sending a request message with a request body to
     * determine if the origin server is willing to accept the request (based on the request headers) before the client
     * sends the request body.
     * </p>
     * <p>
     * The use of the 'Expect: 100-continue' handshake can result in noticable peformance improvement for entity
     * enclosing requests (such as POST and PUT) that require the target server's authentication.
     * </p>
     * <p>
     * 'Expect: 100-continue' handshake should be used with caution, as it may cause problems with HTTP servers and
     * proxies that do not support HTTP/1.1 protocol.
     * </p>
     * This parameter expects a value of type {@link Boolean}.
     */
    public static final String USE_EXPECT_CONTINUE = "http.protocol.expect-continue";
    /**
     * Defines the charset to be used when encoding {@link org.apache.commons.httpclient.Credentials}. If not defined
     * then the {@link #HTTP_ELEMENT_CHARSET} should be used.
     * <p>
     * This parameter expects a value of type {@link String}.
     * </p>
     */
    public static final String CREDENTIAL_CHARSET = "http.protocol.credential-charset";
    /**
     * Defines the charset to be used for encoding HTTP protocol elements.
     * <p>
     * This parameter expects a value of type {@link String}.
     * </p>
     */
    public static final String HTTP_ELEMENT_CHARSET = "http.protocol.element-charset";
    /**
     * Defines the charset to be used for parsing URIs.
     * <p>
     * This parameter expects a value of type {@link String}.
     * </p>
     */
    public static final String HTTP_URI_CHARSET = "http.protocol.uri-charset";
    /**
     * Defines the charset to be used for encoding content body.
     * <p>
     * This parameter expects a value of type {@link String}.
     * </p>
     */
    public static final String HTTP_CONTENT_CHARSET = "http.protocol.content-charset";
    /**
     * Defines {@link CookiePolicy cookie policy} to be used for cookie management.
     * <p>
     * This parameter expects a value of type {@link String}.
     * </p>
     */
    public static final String COOKIE_POLICY = "http.protocol.cookie-policy";
    /**
     * Defines HttpClient's behavior when a response provides more bytes than expected (specified with Content-Length,
     * for example).
     * <p>
     * Such surplus data makes the HTTP connection unreliable for keep-alive requests, as malicious response data (faked
     * headers etc.) can lead to undesired results on the next request using that connection.
     * </p>
     * <p>
     * If this parameter is set to <code>true</code>, any detection of extra input data will generate a warning in the
     * log.
     * </p>
     * <p>
     * This parameter expects a value of type {@link Boolean}.
     * </p>
     */
    public static final String WARN_EXTRA_INPUT = "http.protocol.warn-extra-input";
    /**
     * Defines the maximum number of ignorable lines before we expect a HTTP response's status code.
     * <p>
     * With HTTP/1.1 persistent connections, the problem arises that broken scripts could return a wrong Content-Length
     * (there are more bytes sent than specified).<br />
     * Unfortunately, in some cases, this is not possible after the bad response, but only before the next one. <br />
     * So, HttpClient must be able to skip those surplus lines this way.
     * </p>
     * <p>
     * Set this to 0 to disallow any garbage/empty lines before the status line.<br />
     * To specify no limit, use {@link Integer#MAX_VALUE} (default in lenient mode).
     * </p>
     * This parameter expects a value of type {@link Integer}.
     */
    public static final String STATUS_LINE_GARBAGE_LIMIT = "http.protocol.status-line-garbage-limit";
    /**
     * Sets the socket timeout (<tt>SO_TIMEOUT</tt>) in milliseconds to be used when executing the method. A timeout
     * value of zero is interpreted as an infinite timeout.
     * <p>
     * This parameter expects a value of type {@link Integer}.
     * </p>
     * 
     * @see java.net.SocketOptions#SO_TIMEOUT
     */
    public static final String SO_TIMEOUT = "http.socket.timeout";
    /**
     * Determines the timeout until a connection is etablished. A value of zero means the timeout is not used. The
     * default value is zero.
     * <p>
     * This parameter expects a value of type {@link Integer}.
     * </p>
     */
    public static final String CONNECTION_TIMEOUT = "http.connection.timeout";
    /**
     * The key used to look up the date patterns used for parsing. The String patterns are stored in a
     * {@link java.util.Collection} and must be compatible with {@link java.text.SimpleDateFormat} .
     * <p>
     * This parameter expects a value of type {@link java.util.Collection}.
     * </p>
     */
    public static final String DATE_PATTERNS = "http.dateparser.patterns";
    /**
     * Sets the method retry handler parameter.
     * <p>
     * This parameter expects a value of type {@link org.apache.commons.httpclient.HttpMethodRetryHandler}.
     * </p>
     */
    public static final String RETRY_HANDLER = "http.method.retry-handler";
    /**
     * Sets the maximum buffered response size (in bytes) that triggers no warning. Buffered responses exceeding this
     * size will trigger a warning in the log.
     * <p>
     * This parameter expects a value if type {@link Integer}.
     * </p>
     */
    public static final String BUFFER_WARN_TRIGGER_LIMIT = "http.method.response.buffer.warnlimit";
    /**
     * Sets the timeout in milliseconds used when retrieving an {@link org.apache.commons.httpclient.HttpConnection HTTP
     * connection} from the {@link org.apache.commons.httpclient.HttpConnectionManager HTTP connection manager}.
     * <p>
     * This parameter expects a value of type {@link Long}.
     * </p>
     */
    public static final String CONNECTION_MANAGER_TIMEOUT = "http.connection-manager.timeout";
    /**
     * Defines the default {@link org.apache.commons.httpclient.HttpConnectionManager HTTP connection manager} class.
     * <p>
     * This parameter expects a value of type {@link Class}.
     * </p>
     */
    public static final String CONNECTION_MANAGER_CLASS = "http.connection-manager.class";
    /**
     * Defines whether authentication should be attempted preemptively.
     * <p>
     * This parameter expects a value of type {@link Boolean}.
     * </p>
     */
    public static final String PREEMPTIVE_AUTHENTICATION = "http.authentication.preemptive";
    /**
     * Defines whether relative redirects should be rejected.
     * <p>
     * This parameter expects a value of type {@link Boolean}.
     * </p>
     */
    public static final String REJECT_RELATIVE_REDIRECT = "http.protocol.reject-relative-redirect";
    /**
     * Defines the maximum number of redirects to be followed. The limit on number of redirects is intended to prevent
     * infinite loops.
     * <p>
     * This parameter expects a value of type {@link Integer}.
     * </p>
     */
    public static final String MAX_REDIRECTS = "http.protocol.max-redirects";
    /**
     * Defines whether circular redirects (redirects to the same location) should be allowed. The HTTP spec is not
     * sufficiently clear whether circular redirects are permitted, therefore optionally they can be enabled
     * <p>
     * This parameter expects a value of type {@link Boolean}.
     * </p>
     */
    public static final String ALLOW_CIRCULAR_REDIRECTS = "http.protocol.allow-circular-redirects";
    /**
     * Defines the request headers to be sent per default with each request.
     * <p>
     * This parameter expects a value of type {@link java.util.Collection}. The collection is expected to contain
     * {@link org.apache.commons.httpclient.Header}s.
     * </p>
     */
    public static final String DEFAULT_HEADERS = "http.default-headers";
    /**
     * Defines the maximum number of connections allowed per host configuration. These values only apply to the number
     * of connections from a particular instance of HttpConnectionManager.
     * <p>
     * This parameter expects a value of type {@link java.util.Map}. The value should map instances of
     * {@link org.apache.commons.httpclient.HostConfiguration} to {@link Integer integers}. The default value can be
     * specified using {@link org.apache.commons.httpclient.HostConfiguration#ANY_HOST_CONFIGURATION}.
     * </p>
     */
    public static final String MAX_HOST_CONNECTIONS = "http.connection-manager.max-per-host";
    /**
     * Defines the maximum number of connections allowed overall. This value only applies to the number of connections
     * from a particular instance of HttpConnectionManager.
     * <p>
     * This parameter expects a value of type {@link Integer}.
     * </p>
     */
    public static final String MAX_TOTAL_CONNECTIONS = "http.connection-manager.max-total";

}
