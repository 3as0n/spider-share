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

package com.treefinance.crawler.framework.boot;

import com.treefinance.crawler.framework.config.xml.page.PageExtractor;
import com.treefinance.crawler.framework.consts.Status;
import com.treefinance.crawler.framework.context.ExtractorProcessorContext;
import com.treefinance.crawler.framework.context.ResponseUtil;
import com.treefinance.crawler.framework.context.function.ExtractRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.context.function.SpiderResponseFactory;
import com.treefinance.crawler.framework.exception.ResultEmptyException;
import com.treefinance.crawler.framework.process.domain.PageExtractObject;
import com.treefinance.crawler.framework.process.extract.ExtractorSelectorHandler;
import com.treefinance.crawler.framework.process.extract.PageExtractorImpl;
import com.treefinance.toolkit.util.Preconditions;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

import java.util.List;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Mar 3, 2014 8:48:55 PM
 */
public class Extractor {

    private static final Logger log = LoggerFactory.getLogger(Extractor.class);

    /**
     * crawler request main route step 1: request via httpclient step 2: parse page content
     * 
     * @param request
     * @return
     */
    public static SpiderResponse extract(@Nonnull ExtractRequest request) {
        Preconditions.notNull("request", request);
        Preconditions.notNull("input", request.getInput());

        SpiderResponse response = SpiderResponseFactory.make();

        try {
            ExtractorProcessorContext context = (ExtractorProcessorContext)request.getProcessorContext();
            ExtractorSelectorHandler selectorHandler = new ExtractorSelectorHandler(context.getExtractorSelectors(), context.getPageExtractorMap());
            List<PageExtractor> pageExtractors = selectorHandler.select(request);

            if (CollectionUtils.isNotEmpty(pageExtractors)) {
                for (int i = 0, length = pageExtractors.size(); i < length; i++) {
                    PageExtractor pageExtractor = pageExtractors.get(i);
                    try {
                        PageExtractorImpl pageExtractorImpl = new PageExtractorImpl(pageExtractor);
                        pageExtractorImpl.invoke(request.copy(), response);
                    } catch (Exception e) {
                        if (i >= length - 1) {
                            throw e;
                        }
                        log.warn("Error invoking page extractor[{}], error: {}", pageExtractor.getId(), e.getMessage());
                        continue;
                    }

                    PageExtractObject extractObject = (PageExtractObject)response.getOutPut();
                    if (extractObject != null && extractObject.isNotEmpty()) {
                        ResponseUtil.setPageExtractor(response, pageExtractor);
                        break;
                    }
                }
            } else {
                log.warn("Empty page extractors!");
            }
        } catch (Exception e) {
            if (e instanceof ResultEmptyException) {
                log.warn("Error invoking page extractor, error: {}", e.getMessage());
            } else {
                log.error("Error invoking page extractor.", e);
            }
            response.setStatus(Status.PROCESS_EXCEPTION);
            response.setException(e);
        }
        return response;
    }

}
