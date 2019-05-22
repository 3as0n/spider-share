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

package com.treefinance.crawler.framework.config.xml.page;

import com.treefinance.crawler.framework.config.annotation.ChildTag;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.xml.AbstractBeanDefinition;
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
 * @since 2015年7月14日 上午11:43:48
 */
public abstract class AbstractPage extends AbstractBeanDefinition {

    private List<AbstractSegment> segmentList = new ArrayList<>();

    @ChildTag("object-segment")
    public List<AbstractSegment> getSegmentList() {
        return Collections.unmodifiableList(segmentList);
    }

    @Node(value = "object-segment", types = {XpathSegment.class, JsonPathSegment.class, RegexSegment.class, SplitSegment.class, CalculateSegment.class, BaseSegment.class})
    public void setSegmentList(AbstractSegment segment) {
        this.segmentList.add(segment);
    }

}
