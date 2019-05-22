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

package com.datatrees.spider.share.service.collector.worker;

import com.datatrees.spider.share.service.collector.actor.TaskMessage;
import com.datatrees.spider.share.service.collector.search.CrawlExecutor;
import com.datatrees.spider.share.service.collector.worker.filter.BusinessTypeFilter;
import com.datatrees.spider.share.service.dao.RedisDao;
import com.datatrees.spider.share.service.extra.SubTaskManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2016年1月18日 下午3:34:47
 */
@Service
public class CollectorWorkerFactory {

    @Resource
    private ResultDataHandler resultDataHandler;

    @Resource
    private CrawlExecutor crawlExecutor;

    @Resource
    private SubTaskManager subTaskManager;

    @Resource
    private RedisDao redisDao;

    @Resource
    private BusinessTypeFilter businessTypeFilter;

    public CollectorWorker getCollectorWorker(TaskMessage taskMessage) {

        return new CollectorWorker().setCrawlExecutor(crawlExecutor).setResultDataHandler(resultDataHandler).setSubTaskManager(subTaskManager).setRedisDao(redisDao)
            .setBusinessTypeFilter(businessTypeFilter);
    }
}
