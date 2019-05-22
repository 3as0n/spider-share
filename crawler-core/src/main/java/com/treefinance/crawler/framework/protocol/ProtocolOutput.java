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

package com.treefinance.crawler.framework.protocol;

/**
 * Simple aggregate to pass from protocol plugins both content and protocol status.
 * 
 * @author Andrzej Bialecki &lt;ab@getopt.org&gt;
 */
public class ProtocolOutput {

    public static ProtocolOutput NULL = new Null(Content.NULL);

    private Content content;

    private int statusCode;

    private Response response;

    /**
     * @param content
     * @param statusCode
     * @param response
     */
    public ProtocolOutput(Content content, int statusCode, Response response) {
        super();
        this.content = content;
        this.statusCode = statusCode;
        this.response = response;
    }

    public ProtocolOutput(Content content, int statusCode) {
        this.content = content;
        this.statusCode = statusCode;
    }

    public ProtocolOutput(Content content) {
        this.content = content;
        this.statusCode = ProtocolStatusCodes.SUCCESS;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isSuccess() {
        return this.statusCode == ProtocolStatusCodes.SUCCESS;
    }

    public boolean needRetry() {
        return this.statusCode == ProtocolStatusCodes.EXCEPTION || this.statusCode == ProtocolStatusCodes.SERVER_EXCEPTION;
    }

    public boolean isRedirector() {
        return this.statusCode == ProtocolStatusCodes.MOVED || this.statusCode == ProtocolStatusCodes.TEMP_MOVED;
    }

    /**
     * @return the response
     */
    public Response getResponse() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(Response response) {
        this.response = response;
    }

    /**
     * null instance mean dummy
     */
    private static class Null extends ProtocolOutput {

        public Null(Content content) {
            super(content);
        }
    }

}
