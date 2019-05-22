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

package com.datatrees.spider.share.service.impl;

import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.common.util.GsonUtils;
import com.datatrees.spider.share.domain.AbstractExtractResult;
import com.datatrees.spider.share.service.ResultStorage;
import com.datatrees.spider.share.service.extract.ExtractResultHandlerFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月31日 上午1:39:17
 */
@Service
public class ResultStorageImpl implements ResultStorage {

    private static final Logger logger = LoggerFactory.getLogger(ResultStorageImpl.class);

    private int remarkLength = PropertiesConfiguration.getInstance().getInt("remark.max.length", 255);

    private int urlLength = PropertiesConfiguration.getInstance().getInt("url.max.length", 512);

    @Resource
    private ExtractResultHandlerFactory extractResultHandlerFactory;

    @Override
    public void doExtractResultSave(AbstractExtractResult result) {
        try {
            if (result.getExtraInfo() != null && StringUtils.isBlank(result.getRemark())) {
                result.setRemark(StringUtils.substring(GsonUtils.toJson(result.getExtraInfo()), 0, remarkLength));
            }
            // url截断保护，避免数据太长保存失败
            if (result.getUrl() != null && result.getUrl().length() > urlLength) {
                logger.warn("url too long, sub original url： {} ", result.getUrl());
                result.setUrl(StringUtils.substring(result.getUrl(), 0, urlLength));
            }
            extractResultHandlerFactory.save(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
