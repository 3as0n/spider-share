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

package com.datatrees.spider.share.domain.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Response implements Serializable {

    @JSONField(ordinal = 1)
    private int statusCode;

    @JSONField(ordinal = 2)
    private boolean isRedirect = false;// 是否重定向了

    @JSONField(ordinal = 2)
    private String redirectUrl;

    @JSONField(ordinal = 3)
    private Request request;

    @JSONField(ordinal = 4)
    private long totalTime;

    @JSONField(ordinal = 5)
    private List<NameValue> headers = new ArrayList<>();

    @JSONField(ordinal = 6)
    private Map<String, String> responseCookies;

    @JSONField(serialize = false)
    private byte[] response;

    @JSONField(ordinal = 7)
    private Charset charset;

    @JSONField(ordinal = 8)
    private String contentType;

    public Response(Request request) {
        this.request = request;
    }

    public Response(Request request, Charset charset) {
        this.request = request;
        this.charset = charset;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getRedirectUrl() {
        if (null != request && request.isRedirect()) {
            return request.getUrl();
        }
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public boolean isRedirect() {
        return null != request && request.isRedirect();
    }

    public void setRedirect(boolean redirect) {
        isRedirect = redirect;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public List<NameValue> getHeaders() {
        return headers;
    }

    public void setHeaders(List<NameValue> headers) {
        this.headers = headers;
    }

    public List<NameValue> getHeaders(String name) {
        if (headers != null && !headers.isEmpty()) {
            return headers.stream().filter(header -> header.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public NameValue getFirstHeader(String name) {
        if (headers != null && !headers.isEmpty()) {
            return headers.stream().filter(header -> header.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        }
        return null;
    }

    public List<String> getHeaderValues(String name) {
        if (headers != null && !headers.isEmpty()) {
            return headers.stream().filter(header -> header.getName().equalsIgnoreCase(name)).map(NameValue::getValue).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public String getFirstHeaderValue(String name) {
        if (headers != null && !headers.isEmpty()) {
            return headers.stream().filter(header -> header.getName().equalsIgnoreCase(name)).findFirst().map(NameValue::getValue).orElse(null);
        }
        return null;
    }

    public void addHeader(NameValue header){
        if(headers == null){
            headers = new ArrayList<>();
        }
        headers.add(header);
    }

    public Map<String, String> getResponseCookies() {
        return responseCookies;
    }

    public void setResponseCookies(Map<String, String> responseCookies) {
        this.responseCookies = responseCookies;
    }

    public String getResponseCookie(String name) {
        if (responseCookies != null) {
            return responseCookies.get(name);
        }
        return null;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @JSONField(serialize = false)
    public byte[] getResponse() {
        return response;
    }

    public void setResponse(byte[] response) {
        this.response = response;
    }

    @JSONField(ordinal = 10)
    public String getPageContent() {
        try {
            if (null == response) {
                return "";
            }
            if (null != contentType && (contentType.contains("image") || contentType.contains("IMAGE"))) {
                return getPageContentForBase64();
            }
            if (null != charset) {
                return new String(response, charset);
            }
            return new String(response, request.getDefaultResponseCharset());
        } catch (Exception e) {
            throw new RuntimeException("getPateContent error,charsetName=UTF-8,request=" + request, e);
        }

    }

    public String getPageContent(Charset charsetName) {
        try {
            return new String(response, charsetName);
        } catch (Exception e) {
            throw new RuntimeException("getPateContent error,charsetName=" + charsetName + ",request=" + request, e);
        }
    }

    @JSONField(serialize = false)
    public String getPageContentForBase64() {
        return Base64.getEncoder().encodeToString(response);
    }

    /**
     * @deprecated use {{@link #getPageContentASJSON()} instead
     */
    @JSONField(serialize = false)
    @Deprecated
    public JSONObject getPageContentForJSON() {
        String json = getPageContent().trim();
        if ((json.startsWith("{") && json.endsWith("}")) || (json.startsWith("[") && json.endsWith("]"))) {
            return JSON.parseObject(json);
        }
        // 有的结尾带";"
        if (null != json && json.contains("(") && json.trim().contains(")")) {
            json = json.substring(json.indexOf("(") + 1, json.lastIndexOf(")"));
            return JSON.parseObject(json);
        }
        return JSON.parseObject(json);
    }

    @JSONField(serialize = false)
    public JSON getPageContentASJSON() {
        String json = getPageContent().trim();
        if (json.startsWith("{") && json.endsWith("}")) {
            return JSON.parseObject(json);
        } else if (json.startsWith("[") && json.endsWith("]")) {
            return JSON.parseArray(json);
        }
        // 有的结尾带";"
        if (json.contains("(") && json.contains(")")) {
            json = json.substring(json.indexOf("(") + 1, json.lastIndexOf(")"));
        }
        return JSON.parseObject(json);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
