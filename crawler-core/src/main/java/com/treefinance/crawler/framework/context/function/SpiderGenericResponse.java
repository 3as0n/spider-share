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

package com.treefinance.crawler.framework.context.function;

import com.treefinance.crawler.framework.consts.Status;
import com.treefinance.crawler.framework.protocol.ProtocolStatusCodes;
import com.treefinance.crawler.lang.AtomicAttributes;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Jerry
 * @since 20:53 2018/7/23
 */
public class SpiderGenericResponse extends AtomicAttributes implements SpiderResponse {

    private int status;

    private Object output;

    private Exception exception;

    private String errorMsg;

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public Object getOutPut() {
        return output;
    }

    @Override
    public void setOutPut(Object output) {
        this.output = output;
    }

    @Override
    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public String getErrorMsg() {
        if (this.errorMsg == null) {
            return this.exception == null ? StringUtils.EMPTY : this.exception.getMessage();
        }

        return this.errorMsg;
    }

    @Override
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public boolean isError() {
        return this.status == Status.PROCESS_EXCEPTION || this.status == ProtocolStatusCodes.EXCEPTION || this.status == ProtocolStatusCodes.SERVER_EXCEPTION;
    }

    @Override
    public void clear() {
        this.status = 0;
        this.output = null;
        this.exception = null;
        this.errorMsg = null;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE).append("status", status).append("output", output).append("attributes", attributes())
            .append("exception", exception).append("errorMsg", errorMsg).toString();
    }
}