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

package com.datatrees.spider.operator.service.extract;

import javax.annotation.Resource;

import com.datatrees.spider.operator.domain.OperatorData;
import com.datatrees.spider.operator.dao.OperatorExtractResultDAO;
import com.datatrees.spider.operator.domain.model.OperatorExtractResult;
import com.datatrees.spider.share.domain.AbstractExtractResult;
import com.datatrees.spider.share.domain.ResultType;
import com.datatrees.spider.share.service.domain.ExtractMessage;
import com.datatrees.spider.share.service.extract.ExtractResultHandler;
import org.springframework.stereotype.Component;

@Component
public class OperatorExtractResultHandler implements ExtractResultHandler {

    @Resource
    private OperatorExtractResultDAO operatorExtractResultDao;

    @Override
    public ResultType getSupportResultType() {
        return ResultType.OPERATOR;
    }

    @Override
    public AbstractExtractResult build(ExtractMessage extractMessage) {
        Object object = extractMessage.getMessageObject();
        OperatorExtractResult result = new OperatorExtractResult();
        result.setOperatorId(extractMessage.getTypeId());
        result.setUrl(((OperatorData) object).getUrl());
        result.setUniqueSign(((OperatorData) object).getUniqueSign());
        result.setExtraInfo(((OperatorData) object).getExtraInfo());
        return result;
    }

    @Override
    public Class<? extends AbstractExtractResult> getSupportResult() {
        return OperatorExtractResult.class;
    }

    @Override
    public void save(AbstractExtractResult result) {
        operatorExtractResultDao.insert((OperatorExtractResult) result);
    }
}
