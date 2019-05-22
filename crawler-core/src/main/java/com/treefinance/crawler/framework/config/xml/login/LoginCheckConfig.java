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

package com.treefinance.crawler.framework.config.xml.login;

import com.treefinance.crawler.framework.config.annotation.Attr;
import com.treefinance.crawler.framework.config.annotation.ChildTag;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Tag;
import com.treefinance.crawler.framework.config.xml.segment.AbstractSegment;
import com.treefinance.crawler.framework.config.xml.segment.BaseSegment;
import com.treefinance.crawler.framework.config.xml.segment.CalculateSegment;
import com.treefinance.crawler.framework.config.xml.segment.JsonPathSegment;
import com.treefinance.crawler.framework.config.xml.segment.RegexSegment;
import com.treefinance.crawler.framework.config.xml.segment.SplitSegment;
import com.treefinance.crawler.framework.config.xml.segment.XpathSegment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月30日 下午2:06:42
 */
public class LoginCheckConfig {

    private String successPattern;

    private String failPattern;

    private Integer checkInterval;// unit m

    private String checkUrl;// check url

    private String headers;

    private List<AbstractSegment> segmentList;

    /**
     *
     */
    public LoginCheckConfig() {
        super();
        segmentList = new ArrayList<AbstractSegment>();
    }

    @ChildTag("object-segment")
    public List<AbstractSegment> getSegmentList() {
        return Collections.unmodifiableList(segmentList);
    }

    @Node(value = "object-segment", types = {XpathSegment.class, JsonPathSegment.class, RegexSegment.class, SplitSegment.class, CalculateSegment.class, BaseSegment.class})
    public void setSegmentList(AbstractSegment segment) {
        this.segmentList.add(segment);
    }

    @Attr("success-pattern")
    public String getSuccessPattern() {
        return successPattern;
    }

    @Node("@success-pattern")
    public void setSuccessPattern(String successPattern) {
        this.successPattern = successPattern;
    }

    @Attr("fail-pattern")
    public String getFailPattern() {
        return failPattern;
    }

    @Node("@fail-pattern")
    public void setFailPattern(String failPattern) {
        this.failPattern = failPattern;
    }

    @Attr("check-interval")
    public Integer getCheckInterval() {
        return checkInterval;
    }

    @Node("@check-interval")
    public void setCheckInterval(Integer checkInterval) {
        this.checkInterval = checkInterval;
    }

    @Tag("check-url")
    public String getCheckUrl() {
        return checkUrl;
    }

    @Node("check-url/text()")
    public void setCheckUrl(String checkUrl) {
        this.checkUrl = checkUrl;
    }

    @Tag("headers")
    public String getHeaders() {
        return headers;
    }

    @Node("headers/text()")
    public void setHeaders(String headers) {
        this.headers = headers;
    }
}
