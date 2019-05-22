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

package com.datatrees.spider.share.service.collector.chain.search;

import com.datatrees.spider.share.domain.model.Task;
import com.datatrees.spider.share.service.collector.chain.Context;
import com.datatrees.spider.share.service.collector.search.SearchProcessor;
import com.treefinance.crawler.framework.context.ResponseUtil;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.protocol.ProtocolOutput;

import javax.annotation.Nonnull;

import java.util.List;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月29日 上午3:19:33
 */
public class RecordNetworkTrafficFilter extends ResponsesFilter {

    @Override
    protected void doInternalFilter(@Nonnull List<SpiderResponse> responses, SearchProcessor searchProcessor, Context context) {
        Task task = searchProcessor.getTask();
        for (SpiderResponse response : responses) {
            ProtocolOutput outPut = ResponseUtil.getProtocolResponse(response);
            if (outPut != null && outPut.getContent() != null && outPut.getContent().getContent() != null) {
                log.debug("add new networkTraffic length: {}", outPut.getContent().getContent().length);
                task.getNetworkTraffic().addAndGet(outPut.getContent().getContent().length);
            }
        }
    }

}
