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

package com.treefinance.crawler.framework.context;

import com.treefinance.crawler.framework.config.xml.ExtractorConfig;
import com.treefinance.crawler.framework.config.xml.extractor.ExtractorSelector;
import com.treefinance.crawler.framework.config.xml.page.PageExtractor;
import com.treefinance.toolkit.util.Preconditions;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Mar 7, 2014 2:14:19 PM
 */
public class ExtractorProcessorContext extends AbstractProcessorContext {

    private final Map<String, PageExtractor> pageExtractorMap = new HashMap<>();

    public ExtractorProcessorContext(Website website, Long taskId) {
        super(website, taskId);

        Preconditions.notNull("extractor-config", website.getExtractorConfig());
    }

    @Override
    public void init() {
        List<PageExtractor> PageExtractorList = getExtractorConfig().getPageExtractors();
        if (CollectionUtils.isNotEmpty(PageExtractorList)) {
            for (PageExtractor p : PageExtractorList) {
                pageExtractorMap.put(p.getId(), p);
            }
        }

        // init plugin
        registerPlugins(getExtractorConfig().getPluginList());
    }

    public ExtractorConfig getExtractorConfig() {
        return getWebsite().getExtractorConfig();
    }

    public List<ExtractorSelector> getExtractorSelectors() {
        return getExtractorConfig().getExtractorSelectors();
    }

    /**
     * @return the pageExtractorMap
     */
    public Map<String, PageExtractor> getPageExtractorMap() {
        return Collections.unmodifiableMap(pageExtractorMap);
    }

}
