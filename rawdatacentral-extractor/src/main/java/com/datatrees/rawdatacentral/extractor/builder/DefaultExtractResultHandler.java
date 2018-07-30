package com.datatrees.rawdatacentral.extractor.builder;

import com.datatrees.rawdatacentral.core.model.data.DefaultData;
import com.datatrees.rawdatacentral.domain.model.DefaultExtractResult;
import com.datatrees.spider.share.domain.AbstractExtractResult;
import com.datatrees.spider.share.domain.ResultType;
import com.datatrees.spider.share.service.domain.ExtractMessage;
import com.datatrees.spider.share.service.extract.ExtractResultHandler;
import org.springframework.stereotype.Component;

@Component
public class DefaultExtractResultHandler implements ExtractResultHandler {

    @Override
    public ResultType getSupportResultType() {
        return ResultType.MAILBILL;
    }

    @Override
    public AbstractExtractResult build(ExtractMessage extractMessage) {
        Object object = extractMessage.getMessageObject();
        DefaultExtractResult result = new DefaultExtractResult();
        result.setUrl(((DefaultData) object).getUrl());
        result.setUniqueSign(((DefaultData) object).getUniqueSign());
        result.setExtraInfo(((DefaultData) object).getExtraInfo());
        return result;
    }
}
