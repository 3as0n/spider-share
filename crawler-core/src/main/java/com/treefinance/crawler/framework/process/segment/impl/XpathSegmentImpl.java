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

package com.treefinance.crawler.framework.process.segment.impl;

import com.treefinance.crawler.framework.config.xml.segment.XpathSegment;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.expression.StandardExpression;
import com.treefinance.crawler.framework.process.segment.SegmentBase;
import com.treefinance.crawler.framework.util.xpath.XPathUtil;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.List;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 25, 2014 10:03:41 AM
 */
public class XpathSegmentImpl extends SegmentBase<XpathSegment> {

    public XpathSegmentImpl(@Nonnull XpathSegment segment) {
        super(segment);
    }

    @Override
    public List<String> splitInputContent(String content, XpathSegment segment, SpiderRequest request, SpiderResponse response) {
        if (StringUtils.isEmpty(content)) {
            return Collections.emptyList();
        }

        String xpath = segment.getXpath();

        logger.debug("Xpath: {}", xpath);

        xpath = StringUtils.trimToEmpty(xpath);

        if (!xpath.isEmpty()) {
            xpath = StandardExpression.eval(xpath, request, response);

            logger.debug("Actual xpath: {}", xpath);

            List<String> segments = XPathUtil.getXpath(xpath, content);

            logger.info("jsonpath: {}, segments size: {}", xpath, segments.size());

            return segments;
        }

        return Collections.singletonList(content);
    }

}
