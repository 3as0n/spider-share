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

package com.datatrees.spider.share.service.collector.worker.filter;

import com.datatrees.spider.share.service.AppCrawlerConfigService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.treefinance.crawler.framework.config.enums.BusinessType;
import com.treefinance.crawler.framework.context.AbstractProcessorContext;
import com.treefinance.crawler.framework.context.control.IBusinessTypeFilter;
import com.treefinance.saas.taskcenter.facade.request.TaskRequest;
import com.treefinance.saas.taskcenter.facade.result.TaskRO;
import com.treefinance.saas.taskcenter.facade.result.common.TaskResult;
import com.treefinance.saas.taskcenter.facade.service.TaskFacade;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Resource;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * User: yand Date: 2018/4/9
 */
@Service("businessTypeFilter")
public class BusinessTypeFilter implements IBusinessTypeFilter {

    private static final Logger logger = LoggerFactory.getLogger(BusinessTypeFilter.class);

    private final Cache<String, Boolean> localCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).expireAfterAccess(1, TimeUnit.MINUTES).softValues().build();

    @Resource
    private TaskFacade taskFacade;

    @Resource
    private AppCrawlerConfigService appCrawlerConfigService;

    @Override
    public boolean isFilter(String businessType, @Nonnull AbstractProcessorContext context) {
        Objects.requireNonNull(context);

        if (StringUtils.isBlank(businessType)) {
            return Boolean.FALSE;
        }

        Boolean filtered = isFiltered(businessType, context.getTaskId());

        logger.info("crawling-business decider >> taskId: {}, businessType: {}, filter: {}", context.getTaskId(), businessType, filtered);

        return filtered;
    }

    private Boolean isFiltered(String businessType, long taskId) {
        BusinessType type = BusinessType.getBusinessType(businessType);
        if (type == null || !type.isEnable()) {
            logger.warn("Disabled crawling-business type >>> {}, taskId: {}", businessType, taskId);
            return Boolean.FALSE;
        }

        try {
            return localCache.get(taskId + "_" + type.getCode(), () -> {
                TaskRequest taskRequest = new TaskRequest();
                taskRequest.setId(taskId);
                TaskResult<TaskRO> taskRO = taskFacade.getTaskByPrimaryKey(taskRequest);
                TaskRO data = taskRO.getData();

                logger.debug("taskRO is {}", data);

                if (data != null) {
                    String appId = data.getAppId();
                    String result = appCrawlerConfigService.getFromRedis(appId, type.getCode());

                    logger.info("appId: {}, projectCode: {}, crawlBizConfig: {} ", appId, type.getCode(), result);

                    return Boolean.FALSE.toString().equalsIgnoreCase(result);
                }

                return false;
            });
        } catch (ExecutionException e) {
            throw new UncheckedExecutionException(e);
        }
    }

}
