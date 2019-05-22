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

// JDK imports

import com.treefinance.crawler.framework.protocol.metadata.HttpHeaders;
import com.treefinance.crawler.framework.protocol.metadata.Metadata;

import java.net.URL;

/**
 * A response inteface. Makes all protocols model HTTP.
 */
public interface Response extends HttpHeaders {

    /** Returns the URL used to retrieve this response. */
    public URL getUrl();

    /** Returns the response code. */
    public int getCode();

    /** Returns the value of a named header. */
    public String getHeader(String name);

    /** Returns all the headers. */
    public Metadata getHeaders();

    /** Returns the full content of the response. */
    public byte[] getContent();

}
