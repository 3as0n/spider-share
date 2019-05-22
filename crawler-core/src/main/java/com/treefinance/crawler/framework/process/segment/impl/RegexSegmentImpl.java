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

package com.treefinance.crawler.framework.process.segment.impl;

import com.treefinance.crawler.framework.config.xml.segment.RegexSegment;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.process.segment.SegmentBase;
import com.treefinance.toolkit.util.RegExp;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.List;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 25, 2014 10:03:15 AM
 */
public class RegexSegmentImpl extends SegmentBase<RegexSegment> {

    public RegexSegmentImpl(@Nonnull RegexSegment segment) {
        super(segment);
    }

    @Override
    public List<String> splitInputContent(String content, RegexSegment segment, SpiderRequest request, SpiderResponse response) {
        if (StringUtils.isEmpty(content)) {
            return Collections.emptyList();
        }

        String regex = StringUtils.defaultString(segment.getRegex());

        logger.debug("RegExp pattern: {}, group: {}", regex, segment.getGroupIndex());

        if (!regex.isEmpty()) {
            return RegExp.findAll(content, regex, segment.getGroupIndex());
        }

        return Collections.singletonList(content);
    }

}
