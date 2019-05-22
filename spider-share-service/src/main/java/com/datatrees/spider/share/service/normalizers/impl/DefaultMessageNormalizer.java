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

package com.datatrees.spider.share.service.normalizers.impl;

import com.datatrees.spider.share.domain.DefaultData;
import com.datatrees.spider.share.domain.ResultType;
import com.datatrees.spider.share.service.domain.ExtractMessage;
import com.datatrees.spider.share.service.normalizers.MessageNormalizer;
import com.treefinance.crawler.framework.process.domain.ExtractObject;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月31日 上午11:50:36
 */
@Service
public class DefaultMessageNormalizer implements MessageNormalizer {

    @Override
    public boolean normalize(ExtractMessage message) {
        Object object = message.getMessageObject();
        if (object instanceof DefaultData) {
            message.setResultType(ResultType.DEFAULT);
            message.setTypeId(message.getWebsiteId());
            return true;
        } else if (object instanceof ExtractObject && DefaultData.class.getSimpleName().equals(((ExtractObject)object).getResultClass())) {
            DefaultData defaultData = new DefaultData();
            defaultData.putAll((Map)object);

            message.setResultType(ResultType.DEFAULT);
            message.setTypeId(message.getWebsiteId());
            message.setMessageObject(defaultData);
            return true;
        } else {
            return false;
        }
    }

}
