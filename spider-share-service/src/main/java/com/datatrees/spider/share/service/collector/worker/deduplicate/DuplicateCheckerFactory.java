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

package com.datatrees.spider.share.service.collector.worker.deduplicate;

import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.spider.share.service.collector.worker.deduplicate.impl.DuplicateCheckerImpl;
import com.treefinance.crawler.framework.context.Website;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年8月27日 下午12:56:28
 */
@Service
public class DuplicateCheckerFactory {

    private static final Logger log = LoggerFactory.getLogger(DuplicateCheckerFactory.class);

    private long duplicateCheckerWithinHours = PropertiesConfiguration.getInstance().getLong("duplicate.checker.within.hours", 48);

    private int websiteTaskCountThreshold = PropertiesConfiguration.getInstance().getInt("website.task.count.threshold", 2);
    //
    // @Resource
    // private ExtractorResultService extractorResultService;
    //
    // @Resource
    // private TaskService taskService;

    public DuplicateChecker duplicateCheckerBuild(Website website, int userId) {
        // List<Task> tasks =
        // taskService.selectWebisteTaskWithinPeriod(userId, website.getId(), new
        // Date(UnifiedSysTime.INSTANCE.getSystemTime().getTime()
        // - duplicateCheckerWithinHours * 3600 * 1000));

        Set<String> existedKeySet = null;
        // task not empty means user retry to import data, and don't do duplicate check,
        // websiteTaskCountThreshold min=2;去除当前次task
        // if (CollectionUtils.isEmpty(tasks) || tasks.size() < websiteTaskCountThreshold) {
        // log.info("try to load duplicateChecker for userId:{}, website:{}", userId, website);
        // WebsiteType type = WebsiteType.getWebsiteType(website.getWebsiteType());
        // if (type != null) {
        // switch (type) {
        // case MAIL:
        // existedKeySet = extractorResultService.getUserSuccessParsedMailKeySet(userId);
        // break;
        // case OPERATOR:
        // break;
        // case ECOMMERCE:
        // break;
        // case BANK:
        // existedKeySet = extractorResultService.getUserSuccessParsedEBankBillSet(userId);
        // break;
        // default:
        // break;
        // }
        // }
        // } else {
        // log.warn("no need to do duplicate check for websiteid:" + website.getId() + "," + "userid:" + userId +
        // ",withinHours:"
        // + duplicateCheckerWithinHours + ",size:" + tasks.size() + ",threshold:" + websiteTaskCountThreshold);
        // }

        DuplicateChecker checker = new DuplicateCheckerImpl(existedKeySet);
        return checker;
    }
}
