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

package com.treefinance.crawler.framework.process.domain;

import com.treefinance.crawler.framework.context.function.LinkNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jerry
 * @since 15:20 2018/8/21
 */
public class ClassifiedExtractResult {

    private final Map<String, LinkNode> linkNodes = new HashMap<>();

    private final List<Object> segments = new ArrayList<>();

    public ClassifiedExtractResult(PageExtractObject extractObject) {
        Collection<Object> segmentResult = extractObject.values();
        for (Object obj : segmentResult) {
            if (obj instanceof Collection) {
                for (Object item : ((Collection)obj)) {
                    classify(item);
                }
            } else {
                classify(obj);
            }
        }
    }

    public Map<String, LinkNode> getLinkNodes() {
        return linkNodes;
    }

    public List<Object> getSegments() {
        return segments;
    }

    private void classify(Object obj) {
        if (obj instanceof LinkNode) {
            linkNodes.put(((LinkNode)obj).getUrl(), (LinkNode)obj);
        } else {
            segments.add(obj);
        }
    }
}
