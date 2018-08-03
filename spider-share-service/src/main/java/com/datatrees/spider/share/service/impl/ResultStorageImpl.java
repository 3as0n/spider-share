/**
 * This document and its contents are protected by copyright 2015 and owned by datatrees.com Inc.
 * The copying and reproduction of this document and/or its content (whether wholly or partly) or
 * any incorporation of the same into any other material in any media or format of any kind is
 * strictly prohibited. All rights are reserved.
 * Copyright (c) datatrees.com Inc. 2015
 */

package com.datatrees.spider.share.service.impl;

import javax.annotation.Resource;

import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.common.util.GsonUtils;
import com.datatrees.spider.share.service.ResultStorage;
import com.datatrees.spider.share.domain.AbstractExtractResult;
import com.datatrees.spider.share.service.extract.ExtractResultHandlerFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月31日 上午1:39:17
 */
@Service
public class ResultStorageImpl implements ResultStorage {

    private static final Logger                      logger       = LoggerFactory.getLogger(ResultStorageImpl.class);

    private              int                         remarkLength = PropertiesConfiguration.getInstance().getInt("remark.max.length", 255);

    private              int                         urlLength    = PropertiesConfiguration.getInstance().getInt("url.max.length", 512);

    @Resource
    private              ExtractResultHandlerFactory extractResultHandlerFactory;

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