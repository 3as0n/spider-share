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

import com.treefinance.crawler.framework.config.xml.segment.SplitSegment;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.process.segment.SegmentBase;
import com.treefinance.toolkit.util.RegExp;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 25, 2014 10:03:56 AM
 */
public class SplitSegmentImpl extends SegmentBase<SplitSegment> {

    public SplitSegmentImpl(@Nonnull SplitSegment segment) {
        super(segment);
    }

    @Override
    protected List<String> splitInputContent(String content, SplitSegment segment, SpiderRequest request, SpiderResponse response) {
        if (StringUtils.isNotEmpty(content)) {
            String split = StringUtils.defaultString(segment.getSplitString());

            logger.debug("Splitter separate: {}", split);

            if (!split.isEmpty()) {
                String[] regexResult = content.split(split);
                Matcher m = RegExp.getMatcher(split, content);
                int count = 0;
                while (count < regexResult.length) {
                    if (BooleanUtils.isTrue(segment.getAppend())) {
                        if (m.find()) {
                            regexResult[count] = regexResult[count] + m.group();
                        }
                    } else {
                        if (count > 0 && m.find()) {
                            regexResult[count] = m.group() + regexResult[count];
                        }
                    }
                    count++;
                }
                return Arrays.asList(regexResult);
            }
        }

        return Collections.singletonList(content);
    }
}
