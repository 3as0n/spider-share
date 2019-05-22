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

package com.treefinance.crawler.framework.process;

import com.treefinance.crawler.framework.consts.SpiderResponseAttrs;
import com.treefinance.crawler.framework.context.function.SpiderResponse;

import java.util.List;

/**
 * @author Jerry
 * @since 20:35 2018/8/7
 */
public final class SpiderResponseHelper {

    private SpiderResponseHelper() {}

    @SuppressWarnings("unchecked")
    public static List<String> getSegmentProcessingData(SpiderResponse response) {
        return (List<String>)response.getAttribute(SpiderResponseAttrs.SEGMENT_PROCESSING_DATA);
    }

    public static void setSegmentProcessingData(SpiderResponse response, List<String> list) {
        response.setAttribute(SpiderResponseAttrs.SEGMENT_PROCESSING_DATA, list);
    }

}
