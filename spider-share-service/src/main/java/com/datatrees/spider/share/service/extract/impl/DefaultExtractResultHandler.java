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

package com.datatrees.spider.share.service.extract.impl;

import com.datatrees.spider.share.dao.DefaultExtractResultDAO;
import com.datatrees.spider.share.domain.AbstractExtractResult;
import com.datatrees.spider.share.domain.DefaultData;
import com.datatrees.spider.share.domain.DefaultExtractResult;
import com.datatrees.spider.share.domain.ResultType;
import com.datatrees.spider.share.service.domain.ExtractMessage;
import com.datatrees.spider.share.service.extract.ExtractResultHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DefaultExtractResultHandler implements ExtractResultHandler {

    @Resource
    private DefaultExtractResultDAO defaultExtractResultDAO;

    @Override
    public ResultType getSupportResultType() {
        return ResultType.DEFAULT;
    }

    @Override
    public AbstractExtractResult build(ExtractMessage extractMessage) {
        Object object = extractMessage.getMessageObject();
        DefaultExtractResult result = new DefaultExtractResult();
        result.setUrl(((DefaultData)object).getUrl());
        result.setUniqueSign(((DefaultData)object).getUniqueSign());
        result.setExtraInfo(((DefaultData)object).getExtraInfo());
        return result;
    }

    @Override
    public Class<? extends AbstractExtractResult> getSupportResult() {
        return DefaultExtractResult.class;
    }

    @Override
    public void save(AbstractExtractResult result) {
        defaultExtractResultDAO.insert((DefaultExtractResult)result);
    }
}
