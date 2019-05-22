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

package org.apache.commons.httpclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Jun 15, 2014 5:18:08 PM
 */
public class ProtocolHttpClient extends HttpClient {

    private static final Logger LOG = LoggerFactory.getLogger(HttpClient.class);

    public ProtocolHttpClient(HttpConnectionManager httpConnectionManager) {
        super(httpConnectionManager);
    }

    public int executeMethod(HostConfiguration hostconfig, final HttpMethod method, final HttpState state) throws IOException, HttpException {

        LOG.trace("enter HttpClient.executeMethod(HostConfiguration,HttpMethod,HttpState)");

        if (method == null) {
            throw new IllegalArgumentException("HttpMethod parameter may not be null");
        }
        HostConfiguration defaulthostconfig = getHostConfiguration();
        if (hostconfig == null) {
            hostconfig = defaulthostconfig;
        }
        URI uri = method.getURI();
        if (hostconfig == defaulthostconfig || uri.isAbsoluteURI()) {
            // make a deep copy of the host defaults
            hostconfig = new HostConfiguration(hostconfig);
            if (uri.isAbsoluteURI()) {
                hostconfig.setHost(uri);
            }
        }

        HttpMethodDirector methodDirector = new ProtocolHttpMethodDirector(getHttpConnectionManager(), hostconfig, getParams(), (state == null ? getState() : state));
        methodDirector.executeMethod(method);
        return method.getStatusCode();
    }
}
