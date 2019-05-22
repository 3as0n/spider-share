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

package com.treefinance.crawler.framework.process.extract;

import com.treefinance.crawler.framework.config.xml.page.PageExtractor;
import com.treefinance.crawler.framework.config.xml.segment.AbstractSegment;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.context.function.SpiderResponseFactory;
import com.treefinance.crawler.framework.context.pipeline.ProcessorInvokerAdapter;
import com.treefinance.crawler.framework.exception.ResultEmptyException;
import com.treefinance.crawler.framework.process.ProcessorFactory;
import com.treefinance.crawler.framework.process.domain.PageExtractObject;
import com.treefinance.crawler.framework.process.segment.SegmentBase;
import com.treefinance.toolkit.util.Preconditions;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Objects;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月14日 下午9:14:20
 */
public class PageExtractorImpl extends ProcessorInvokerAdapter {

    private final PageExtractor pageExtractor;

    public PageExtractorImpl(@Nonnull PageExtractor pageExtractor) {
        this.pageExtractor = Objects.requireNonNull(pageExtractor);
    }

    @Override
    public void process(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {
        Preconditions.notNull("input", request.getInput());
        List<AbstractSegment> segments = pageExtractor.getSegmentList();
        logger.info("segments size[{}] in page-extractor: {}", segments.size(), pageExtractor.getId());
        if (CollectionUtils.isEmpty(segments)) {
            logger.warn("Empty segment processor and skip page extractor. page-extractor: {}", pageExtractor.getId());
            return;
        }

        // processing tag 'page-source'
        PageSourceImpl pageSourceImpl = new PageSourceImpl(pageExtractor.getPageSourceList());
        pageSourceImpl.invoke(request, response);
        // the result of page-source handler.
        String pageContent = (String)response.getOutPut();

        logger.info("processing segment for page-extractor: {}, segment-size: {}", pageExtractor.getId(), segments.size());
        PageExtractObject extractObject = new PageExtractObject();
        for (AbstractSegment segment : segments) {
            if (BooleanUtils.isTrue(segment.getStandBy()) && extractObject.isValid(segment.getName())) {
                logger.info("Skip segment processor with flag 'stand-by'. segment: {}", segment);
                continue;
            }

            try {
                request.setInput(pageContent);

                SpiderResponse segResponse = SpiderResponseFactory.make();

                SegmentBase segmentBase = ProcessorFactory.getSegment(segment);
                segmentBase.invoke(request, segResponse);

                Object segResult = segResponse.getOutPut();

                extractObject.setFieldExtractValue(segment.getName(), segResult);
            } catch (ResultEmptyException e) {
                throw e;
            } catch (Exception e) {
                logger.error("invoke segment processor error!", e);
            } finally {
                request.clear();
            }
        }

        // set the result of segment processor.
        response.setOutPut(extractObject);
    }

}
