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

import com.treefinance.crawler.framework.context.ResponseUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Mar 10, 2014 1:48:37 PM
 */
public class CrawlResponse extends Response {

    private CrawlResponse() {
        setStatus(1);
    }

    public static CrawlResponse build() {
        return new CrawlResponse();
    }

    public List<LinkNode> getUrls() {
        List<LinkNode> links = ResponseUtil.getResponseLinkNodes(this);
        if (links == null) {
            links = new ArrayList<LinkNode>();
            setUrls(links);
        }
        return links;
    }

    public CrawlResponse setUrls(List<LinkNode> urls) {
        ResponseUtil.setResponseLinkNodes(this, urls);
        return this;
    }

    public CrawlResponse addUrl(LinkNode url) {
        List<LinkNode> links = getUrls();
        links.add(url);
        return this;
    }

    public String info() {
        return "status:" + getStatus() + "\n" + "error info:" + getErrorMsg() + "\n" + "urls size:" + getUrls().size();
    }

}
