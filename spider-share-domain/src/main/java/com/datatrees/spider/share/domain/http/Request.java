package com.datatrees.spider.share.domain.http;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.datatrees.spider.share.domain.http.HttpHeadKey;
import com.datatrees.spider.share.domain.RequestType;
import com.datatrees.spider.share.domain.http.NameValue;

/**
 * 请求
 */
public class Request implements Serializable {

    /**
     * 请求ID随机,不保证重复,重复概率低
     */
    @JSONField(ordinal = 1)
    private Long                requestId;

    @JSONField(ordinal = 1)
    private Long                taskId;

    @JSONField(ordinal = 2)
    private String              websiteName;

    @JSONField(ordinal = 2)
    private boolean             isRedirect     = false;//是否重定向了

    @JSONField(ordinal = 3)
    private String              proxy;

    @JSONField(ordinal = 4)
    private String              fullUrl;

    @JSONField(ordinal = 5)
    private String              url;

    @JSONField(ordinal = 6)
    private Map<String, Object> params         = new HashMap<>();

    @JSONField(ordinal = 7)
    private String              remarkId;

    @JSONField(ordinal = 8)
    private List<NameValue>     headers        = new ArrayList<>();

    @JSONField(ordinal = 9)
    private long                requestTimestamp;

    @JSONField(ordinal = 10)
    private Map<String, String> requestCookies = new HashMap<>();

    @JSONField(ordinal = 12)
    private String              contentType    = "application/x-www-form-urlencoded; charset=UTF-8";

    @JSONField(ordinal = 12)
    private Charset             charset        = Charset.forName("ISO-8859-1");

    @JSONField(ordinal = 13)
    private RequestType         type           = RequestType.GET;

    @JSONField(ordinal = 9)
    private int                 maxRetry               = 1;

    @JSONField(ordinal = 10)
    private AtomicInteger       retry                  = new AtomicInteger(0);

    @JSONField(ordinal = 11)
    private int                 connectTimeout         = 10000;

    @JSONField(ordinal = 12)
    private int                 socketTimeout          = 20000;

    @JSONField(ordinal = 13)
    private String              requestBodyContent;

    @JSONField(ordinal = 14)
    private Boolean             proxyEnable;

    @JSONField(ordinal = 15)
    private Map<String, String> extralCookie           = new HashMap<>();

    @JSONField(ordinal = 16)
    private String              host;

    @JSONField(ordinal = 16)
    private String              protocol;

    @JSONField(ordinal = 16)
    private String              defaultResponseCharset = "UTF-8";

    @JSONField(ordinal = 16)
    private Integer             redirectCount          = 0;

    @JSONField(ordinal = 16)
    private Integer             maxRedirectCount       = 5;

    /**
     * 自动跳转
     */
    @JSONField(ordinal = 16)
    private Boolean             autoRedirect           = true;

    public Request() {
        addHead(HttpHeadKey.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:56.0) Gecko/20100101 Firefox/56.0");
    }

    public Map<String, String> getExtralCookie() {
        return extralCookie;
    }

    public void setExtralCookie(Map<String, String> extralCookie) {
        this.extralCookie = extralCookie;
    }

    public void addExtraCookie(String name, String value) {
        getExtralCookie().put(name, value);
    }

    public Boolean getProxyEnable() {
        return proxyEnable;
    }

    public void setProxyEnable(Boolean proxyEnable) {
        this.proxyEnable = proxyEnable;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getRemarkId() {
        return remarkId;
    }

    public void setRemarkId(String remarkId) {
        this.remarkId = remarkId;
    }

    public List<NameValue> getHeaders() {
        return headers;
    }

    public void setHeaders(List<NameValue> headers) {
        this.headers = headers;
    }

    public long getRequestTimestamp() {
        return requestTimestamp;
    }

    public void setRequestTimestamp(long requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }

    public Map<String, String> getRequestCookies() {
        return requestCookies;
    }

    public void setRequestCookies(Map<String, String> requestCookies) {
        this.requestCookies = requestCookies;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType requestType) {
        this.type = requestType;
    }

    public int getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    public AtomicInteger getRetry() {
        return retry;
    }

    public void setRetry(AtomicInteger retry) {
        this.retry = retry;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public String getRequestBodyContent() {
        return requestBodyContent;
    }

    public void setRequestBodyContent(String requestBodyContent) {
        this.requestBodyContent = requestBodyContent;
    }

    public boolean isRedirect() {
        return isRedirect;
    }

    public void setRedirect(boolean redirect) {
        isRedirect = redirect;
    }

    public synchronized void addHead(String name, String value) {
        if (null == name || name.trim().length() == 0 || null == value) {
            return;
        }
        if (name.equals("Set-Cookie")) {
            headers.add(new NameValue(name, value));
        } else {
            NameValue head = null;
            for (NameValue nameValue : headers) {
                if (nameValue.getName().equals(name)) {
                    head = nameValue;
                    break;
                }
            }
            if (null != head) {
                head.setValue(value);
            } else {
                headers.add(new NameValue(name, value));
            }
        }
    }

    public boolean containHeader(String name) {
        if (null != headers && !headers.isEmpty()) {
            for (NameValue header : headers) {
                if (header.getName().endsWith(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDefaultResponseCharset() {
        return defaultResponseCharset;
    }

    public void setDefaultResponseCharset(String defaultResponseCharset) {
        this.defaultResponseCharset = defaultResponseCharset;
    }

    public Boolean getAutoRedirect() {
        return autoRedirect;
    }

    public void setAutoRedirect(Boolean autoRedirect) {
        this.autoRedirect = autoRedirect;
    }

    public Integer getRedirectCount() {
        return redirectCount;
    }

    public void setRedirectCount(Integer redirectCount) {
        this.redirectCount = redirectCount;
    }

    public Integer getMaxRedirectCount() {
        return maxRedirectCount;
    }

    public void setMaxRedirectCount(Integer maxRedirectCount) {
        this.maxRedirectCount = maxRedirectCount;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}