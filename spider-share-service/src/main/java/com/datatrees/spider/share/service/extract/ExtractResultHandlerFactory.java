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

package com.datatrees.spider.share.service.extract;

import com.datatrees.spider.share.domain.AbstractExtractResult;
import com.datatrees.spider.share.service.domain.ExtractMessage;
import com.datatrees.spider.share.service.util.StoragePathUtil;
import com.datatrees.spider.share.service.util.UniqueKeyGenUtil;
import com.treefinance.crawler.exception.UnexpectedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Resource;

import java.util.Collection;

@Service
public class ExtractResultHandlerFactory {

    @Resource
    private ApplicationContext context;

    @Nonnull
    public AbstractExtractResult build(ExtractMessage extractMessage) {
        AbstractExtractResult result = null;
        Collection<ExtractResultHandler> handlers = context.getBeansOfType(ExtractResultHandler.class).values();
        for (ExtractResultHandler handler : handlers) {
            if (handler.getSupportResultType() == extractMessage.getResultType()) {
                result = handler.build(extractMessage);
                break;
            }
        }

        if (result == null) {
            throw new UnexpectedException("Initial extract result failure!");
        }

        result.setUniqueMd5(UniqueKeyGenUtil.uniqueKeyGen(result.getUniqueSign()));
        result.setTaskId(extractMessage.getProcessId());
        result.setWebsiteId(extractMessage.getWebsiteId());
        result.setStoragePath(StoragePathUtil.genStoragePath(extractMessage, result.getUniqueMd5()));
        return result;
    }

    public void save(AbstractExtractResult result) {
        Collection<ExtractResultHandler> handlers = context.getBeansOfType(ExtractResultHandler.class).values();
        for (ExtractResultHandler handler : handlers) {
            if (StringUtils.equals(result.getClass().getName(), handler.getSupportResult().getName())) {
                handler.save(result);
                break;
            }

        }
    }
}
