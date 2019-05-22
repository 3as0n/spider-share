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

package com.treefinance.crawler.framework.config.xml;

import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Path;
import com.treefinance.crawler.framework.config.annotation.Tag;
import com.treefinance.crawler.framework.config.xml.extractor.ExtractorSelector;
import com.treefinance.crawler.framework.config.xml.page.PageExtractor;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Feb 7, 2014 2:10:32 PM
 */
@Tag("config")
@Path("/config")
public class ExtractorConfig extends AbstractWebsiteConfig {

    /**
     *
     */
    private static final long serialVersionUID = 7882181962225972799L;

    private List<ExtractorSelector> extractorSelectors = new ArrayList<>();

    private List<PageExtractor> pageExtractors = new ArrayList<>();

    public ExtractorConfig() {
        super();
    }

    @Tag("page-extractor-definition")
    public List<PageExtractor> getPageExtractors() {
        return Collections.unmodifiableList(pageExtractors);
    }

    @Node(value = "page-extractor-definition/page-extractor", registered = true)
    public void setPageExtractors(PageExtractor pageExtractor) {
        this.pageExtractors.add(pageExtractor);
    }

    @Tag("extrator-selectors")
    public List<ExtractorSelector> getExtractorSelectors() {
        return Collections.unmodifiableList(extractorSelectors);
    }

    @Node("extrator-selectors/selector")
    public void setExtractorSelectors(ExtractorSelector extractorSelector) {
        this.extractorSelectors.add(extractorSelector);
    }

    public void clone(ExtractorConfig cloneFrom) {
        super.clone(cloneFrom);
        if (CollectionUtils.isEmpty(extractorSelectors)) {
            this.extractorSelectors.addAll(cloneFrom.getExtractorSelectors());
        }
        if (CollectionUtils.isEmpty(pageExtractors)) {
            this.pageExtractors.addAll(cloneFrom.getPageExtractors());
        }
    }
}
