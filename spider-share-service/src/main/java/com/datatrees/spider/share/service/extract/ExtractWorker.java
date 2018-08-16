package com.datatrees.spider.share.service.extract;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;

import com.datatrees.crawler.core.processor.ExtractorProcessorContext;
import com.datatrees.crawler.core.processor.bean.ExtractRequest;
import com.datatrees.crawler.core.processor.common.ResponseUtil;
import com.datatrees.spider.share.domain.AbstractExtractResult;
import com.datatrees.spider.share.domain.ExtractCode;
import com.datatrees.spider.share.service.FileStoreService;
import com.datatrees.spider.share.service.ResultStorage;
import com.datatrees.spider.share.service.domain.ExtractMessage;
import com.datatrees.spider.share.service.domain.SubmitMessage;
import com.datatrees.spider.share.service.extract.impl.DefaultProcessorContextBuilder;
import com.datatrees.spider.share.service.submitter.SubmitProcessor;
import com.treefinance.crawler.framework.boot.Extractor;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.process.domain.PageExtractObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * Created by wuminlang on 15/7/29.
 */
@Service
public class ExtractWorker {

    private static final Logger logger = LoggerFactory.getLogger(ExtractWorker.class);

    @Resource
    private ResultStorage resultStorage;

    @Resource
    private SubmitProcessor submitProcessor;

    @Resource
    private DefaultProcessorContextBuilder contextBuilder;

    @Resource
    private ExtractResultHandlerFactory resultHandlerFactory;

    @Resource
    private FileStoreService fileStoreService;

    public void process(ExtractMessage extractMessage) {
        AbstractExtractResult result = resultHandlerFactory.build(extractMessage);

        long start = System.currentTimeMillis();
        // extract & submit results to redis

        this.extractAndSubmit(extractMessage, result);

        result.setDuration(System.currentTimeMillis() - start);

        logger.info("extractAndSubmit resource:" + result + ",cost time:" + result.getDuration() + "ms");

        // save result recode to mysql
        resultStorage.doExtractResultSave(result);

        extractMessage.setExtractCode(ExtractCode.getExtractCode(result.getStatus()));
    }

    private void extractAndSubmit(ExtractMessage extractMessage, AbstractExtractResult result) {
        ExtractorProcessorContext context = contextBuilder.buildExtractorProcessorContext(extractMessage);

        if (context != null) {
            try {
                // set StoragePath to context
                context.addAttribute("StoragePath", result.getStoragePath());

                ExtractRequest request = ExtractRequest.newBuilder().setInput(extractMessage.getMessageObject()).setExtractContext(context).build();

                SpiderResponse response = Extractor.extract(request);

                if (response.isError()) {
                    result.setExtractCode(ExtractCode.EXTRACT_FAIL, response.getErrorMsg());
                    return;
                }

                PageExtractObject pageExtractObject = (PageExtractObject) response.getOutPut();

                SubmitMessage submitMessage = new SubmitMessage(extractMessage, result);
                submitMessage.setPageExtractObject(pageExtractObject);

                // submit to redis & oss
                if (!submitProcessor.process(submitMessage)) {
                    result.setExtractCode(ExtractCode.EXTRACT_STORE_FAIL);
                }

                // do sub extract processing
                if (pageExtractObject != null && pageExtractObject.isNotEmpty()) {
                    try {
                        result.setResultType(StringUtils.join(pageExtractObject.keySet(), ","));
                        result.setPageExtractId(ResponseUtil.getPageExtractor(response).getId());
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }

                    this.doSubExtract(pageExtractObject.getSubExtractObject(), extractMessage);
                }
            } finally {
                fileStoreService.storeEviFile(result.getStoragePath(), extractMessage);
            }
        } else {
            result.setExtractCode(ExtractCode.EXTRACT_CONF_FAIL);
        }
    }

    private void doSubExtract(Object subExtrat, ExtractMessage extractMessage) {
        if (subExtrat instanceof Collection) {
            Object[] arrays = ((Collection) subExtrat).toArray();
            for (int i = 0; i < arrays.length; i++) {
                this.doSubExtractProcess(extractMessage, arrays[i], i + 1);
            }
        } else if (subExtrat != null) {
            this.doSubExtractProcess(extractMessage, subExtrat, 1);
        }
    }

    private void doSubExtractProcess(ExtractMessage extractMessage, Object obj, int messageIndex) {
        try {
            if (obj instanceof Map && MapUtils.isNotEmpty((Map) obj)) {
                Object messageObject = extractMessage.getMessageObject();
                if (messageObject instanceof Map) {
                    ExtractMessage subExtractMessage = new ExtractMessage();
                    Map map = (Map) BeanUtils.instantiate(messageObject.getClass());
                    map.putAll((Map) messageObject);
                    map.putAll((Map) obj);
                    subExtractMessage.setMessageObject(map);
                    subExtractMessage.setTask(extractMessage.getTask());
                    subExtractMessage.setResultType(extractMessage.getResultType());
                    subExtractMessage.setTaskLogId(extractMessage.getTaskLogId());
                    subExtractMessage.setTaskId(extractMessage.getTaskId());
                    subExtractMessage.setTypeId(extractMessage.getTypeId());
                    subExtractMessage.setWebsiteId(extractMessage.getWebsiteId());
                    subExtractMessage.setMessageIndex(messageIndex);
                    this.process(subExtractMessage);
                    // extractMessage.getSubmitkeyResult().putAll(subExtractMessage.getSubmitkeyResult());
                    extractMessage.addSubExtractMessage(subExtractMessage);
                }
            }
        } catch (Exception e) {
            logger.error("do doSubExtractProcess error " + extractMessage + e.getMessage(), e);
        }
    }

}
