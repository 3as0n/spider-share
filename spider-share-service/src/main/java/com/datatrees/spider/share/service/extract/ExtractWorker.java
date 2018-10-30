/*
 * Copyright © 2015 - 2018 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.datatrees.spider.share.service.extract;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;

import com.treefinance.crawler.framework.context.ExtractorProcessorContext;
import com.treefinance.crawler.framework.context.function.ExtractRequest;
import com.treefinance.crawler.framework.context.ResponseUtil;
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
                context.setAttribute("StoragePath", result.getStoragePath());

                ExtractRequest request = ExtractRequest.newBuilder().setInput(extractMessage.getMessageObject()).setExtractContext(context).build();

                SpiderResponse response = Extractor.extract(request);

                if (response.isError()) {
                    result.setExtractCode(ExtractCode.EXTRACT_FAIL, response.getErrorMsg());
                    return;
                }

                PageExtractObject pageExtractObject = (PageExtractObject) response.getOutPut();

                boolean notEmpty = pageExtractObject != null && pageExtractObject.isNotEmpty();
                if (notEmpty) {
                    try {
                        result.setResultType(StringUtils.join(pageExtractObject.keySet(), ","));
                        result.setPageExtractId(ResponseUtil.getPageExtractor(response).getId());
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }

                SubmitMessage submitMessage = new SubmitMessage(extractMessage, result);
                submitMessage.setPageExtractObject(pageExtractObject);

                // submit to redis & oss
                if (!submitProcessor.process(submitMessage)) {
                    result.setExtractCode(ExtractCode.EXTRACT_STORE_FAIL);
                }

                // do sub extract processing
                if (notEmpty) {
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
                    Map map = (Map) BeanUtils.instantiate(messageObject.getClass());
                    map.putAll((Map) messageObject);
                    map.putAll((Map) obj);
                    ExtractMessage subExtractMessage = new ExtractMessage(extractMessage.getTask(), map);
                    subExtractMessage.setResultType(extractMessage.getResultType());
                    subExtractMessage.setTypeId(extractMessage.getTypeId());
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
