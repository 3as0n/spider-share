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

package com.treefinance.crawler.framework.config.xml.parser;

import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Tag;
import com.treefinance.crawler.framework.config.xml.AbstractBeanDefinition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Jan 9, 2014 9:28:58 PM
 */
@Tag("parser")
public class Parser extends AbstractBeanDefinition implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1249743371163334883L;

    private List<ParserPattern> patterns = new ArrayList<ParserPattern>();

    private String urlTemplate;

    private String linkUrlTemplate;

    private String headers;

    private Integer sleepSecond;

    public Parser() {
        super();
    }

    @Tag("url-template")
    public String getUrlTemplate() {
        return urlTemplate;
    }

    @Node("url-template/text()")
    public void setUrlTemplate(String urlTemplate) {
        this.urlTemplate = urlTemplate;
    }

    @Tag("patterns")
    public List<ParserPattern> getPatterns() {
        return Collections.unmodifiableList(patterns);
    }

    @Node(value = "patterns/pattern", types = {ParserPattern.class})
    public void setPatterns(ParserPattern pattern) {
        this.patterns.add(pattern);
    }

    @Tag("link-url-template")
    public String getLinkUrlTemplate() {
        return linkUrlTemplate;
    }

    @Node("link-url-template/text()")
    public void setLinkUrlTemplate(String linkUrlTemplate) {
        this.linkUrlTemplate = linkUrlTemplate;
    }

    @Tag("headers")
    public String getHeaders() {
        return headers;
    }

    @Node("headers/text()")
    public void setHeaders(String headers) {
        this.headers = headers;
    }

    @Tag("sleep-second")
    public Integer getSleepSecond() {
        return sleepSecond;
    }

    @Node("sleep-second/text()")
    public void setSleepSecond(Integer sleepSecond) {
        this.sleepSecond = sleepSecond;
    }

}
