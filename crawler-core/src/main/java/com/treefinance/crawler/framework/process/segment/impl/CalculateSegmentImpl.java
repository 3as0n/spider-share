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

import com.treefinance.crawler.framework.config.xml.segment.CalculateSegment;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.expression.StandardExpression;
import com.treefinance.crawler.framework.process.segment.SegmentBase;
import com.treefinance.crawler.framework.util.CalculateUtils;

import javax.annotation.Nonnull;

import java.util.LinkedList;
import java.util.List;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年9月5日 下午8:16:43
 */
public class CalculateSegmentImpl extends SegmentBase<CalculateSegment> {

    public CalculateSegmentImpl(@Nonnull CalculateSegment segment) {
        super(segment);
    }

    @Override
    protected List<String> splitInputContent(String content, CalculateSegment segment, SpiderRequest request, SpiderResponse response) {
        List<String> result = new LinkedList<>();
        String expression = segment.getExpression();
        logger.info("start calculate segment processor with expression: {}", expression);

        // 1,3,1,+ 从2开始到3(包含3)
        String[] arrays = expression.split(",");
        double start = CalculateUtils.calculate(arrays[0], request, response);
        double end = CalculateUtils.calculate(arrays[1], request, response);
        double interval = CalculateUtils.calculate(arrays[2], request, response);
        String formula = StandardExpression.eval(arrays[3], request, response);
        while (start < end) {
            start = CalculateUtils.calculate(start + formula + interval, null, Double.TYPE);
            result.add(start + "");
        }

        return result;
    }
}
