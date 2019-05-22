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

import com.treefinance.crawler.framework.protocol.Response;
import com.treefinance.crawler.framework.protocol.metadata.Metadata;
import com.treefinance.crawler.framework.protocol.metadata.SpellCheckedMetadata;
import org.apache.commons.httpclient.HttpState;

import java.net.URL;

/**
 * An HTTP response.
 * 
 * @author Susam Pal
 */
public class HttpResponse implements Response {

    private URL url;

    private byte[] content;

    private int code;

    private Metadata headers = new SpellCheckedMetadata();

    private String redirectUrl;

    private HttpState state;

    public HttpResponse() {
        super();
    }

    public URL getUrl() {
        return url;
    }

    public HttpResponse setUrl(URL url) {
        this.url = url;
        return this;
    }

    public int getCode() {
        return code;
    }

    public HttpResponse setCode(int code) {
        this.code = code;
        return this;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public Metadata getHeaders() {
        return headers;
    }

    public byte[] getContent() {
        return content;
    }

    public HttpResponse setContent(byte[] content) {
        this.content = content;
        return this;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public HttpResponse setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
        return this;
    }

    /**
     * @return the state
     */
    public HttpState getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(HttpState state) {
        this.state = state;
    }

}
