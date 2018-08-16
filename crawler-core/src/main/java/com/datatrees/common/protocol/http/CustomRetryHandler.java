/*
 * Copyright © 2015 - 2018 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.datatrees.common.protocol.http;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpMethod;

/**
 * @author Jerry
 * @since 14:52 2018/4/11
 */
public class CustomRetryHandler extends DefaultHttpMethodRetryHandler {

    public CustomRetryHandler(int retryCount) {
        super(retryCount, false);
    }

    public CustomRetryHandler() {
    }

    @Override
    public boolean retryMethod(HttpMethod method, IOException exception, int executionCount) {
        if (super.retryMethod(method, exception, executionCount)) {
            // Do not retry if proxy is set
            HostConfiguration conf = method.getHostConfiguration();
            return conf == null || conf.getProxyHost() == null;
        }
        return false;
    }
}
