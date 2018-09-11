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

package com.datatrees.spider.share.service.normalizers;

import javax.annotation.Resource;
import java.util.Collection;

import com.datatrees.spider.share.service.domain.ExtractMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月31日 上午11:53:47
 */
@Component
public class MessageNormalizerFactory {

    private static final Logger             logger = LoggerFactory.getLogger(MessageNormalizerFactory.class);

    @Resource
    private              ApplicationContext context;

    public boolean normalize(ExtractMessage message) {
        Collection<MessageNormalizer> normalizers = context.getBeansOfType(MessageNormalizer.class).values();
        for (MessageNormalizer messageNormalizer : normalizers) {
            try {
                if (messageNormalizer.normalize(message)) {
                    return true;
                }
            } catch (Exception e) {
                logger.error("Data {} normalizer error ", message, e);
            }
        }
        return false;
    }

}
