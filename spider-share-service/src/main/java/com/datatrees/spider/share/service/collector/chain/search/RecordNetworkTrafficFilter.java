package com.datatrees.spider.share.service.collector.chain.search;

import javax.annotation.Nonnull;
import java.util.List;

import com.datatrees.common.pipeline.Response;
import com.datatrees.common.protocol.ProtocolOutput;
import com.datatrees.crawler.core.processor.common.ResponseUtil;
import com.datatrees.spider.share.service.collector.chain.Context;
import com.datatrees.spider.share.service.collector.search.SearchProcessor;
import com.datatrees.spider.share.domain.model.Task;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月29日 上午3:19:33
 */
public class RecordNetworkTrafficFilter extends ResponsesFilter {

    @Override
    protected void doInternalFilter(@Nonnull List<Response> responses, SearchProcessor searchProcessor, Context context) {
        Task task = searchProcessor.getTask();
        for (Response response : responses) {
            ProtocolOutput outPut = ResponseUtil.getProtocolResponse(response);
            if (outPut != null && outPut.getContent() != null && outPut.getContent().getContent() != null) {
                log.debug("add new networkTraffic length: {}", outPut.getContent().getContent().length);
                task.getNetworkTraffic().addAndGet(outPut.getContent().getContent().length);
            }
        }
    }

}