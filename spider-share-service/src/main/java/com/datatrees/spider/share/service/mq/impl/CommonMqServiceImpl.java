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

package com.datatrees.spider.share.service.mq.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.datatrees.spider.share.common.share.service.RedisService;
import com.datatrees.spider.share.common.utils.RedisUtils;
import com.datatrees.spider.share.common.utils.TaskUtils;
import com.datatrees.spider.share.domain.AttributeKey;
import com.datatrees.spider.share.domain.CollectorMessage;
import com.datatrees.spider.share.domain.LoginMessage;
import com.datatrees.spider.share.domain.RedisKeyPrefixEnum;
import com.datatrees.spider.share.domain.StepEnum;
import com.datatrees.spider.share.service.MonitorService;
import com.datatrees.spider.share.service.WebsiteHolderService;
import com.datatrees.spider.share.service.collector.actor.Collector;
import com.datatrees.spider.share.service.mq.CommonMqService;
import com.treefinance.crawler.exception.UnsupportedWebsiteException;
import com.treefinance.crawler.framework.context.Website;
import com.treefintech.spider.share.integration.manager.TaskPointManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.datatrees.spider.share.service.collector.CollectorMessageUtils.buildCollectorMessage;

@Service
public class CommonMqServiceImpl implements CommonMqService {
    private static final Logger logger = LoggerFactory.getLogger(CommonMqServiceImpl.class);
    private static final String SPIDER_STARTING_POINT_CODE = "900201";

    @Resource
    private Collector collector;

    @Resource
    private RedisService redisService;

    @Resource
    private MonitorService monitorService;

    @Resource
    private WebsiteHolderService websiteHolderService;

    @Resource
    private TaskPointManager taskPointManager;

    @Override
    public boolean consumeMessage(MessageExt messageExt, String msg) {
        LoginMessage loginInfo = JSON.parseObject(msg, LoginMessage.class);
        Long taskId = loginInfo.getTaskId();
        TaskUtils.addStep(taskId, StepEnum.REC_INIT_MSG);
        boolean initStatus = TaskUtils.isDev() || RedisUtils.setnx(RedisKeyPrefixEnum.TASK_RUN_COUNT.getRedisKey(taskId), "0", RedisKeyPrefixEnum.TASK_RUN_COUNT.toSeconds());
        // 第一次收到启动消息
        if (initStatus) {
            TaskUtils.addStep(taskId, StepEnum.INIT);
            String websiteName = loginInfo.getWebsiteName();
            // 这里电商,邮箱,老运营商
            Website website = websiteHolderService.getWebsite(websiteName);
            if (website == null) {
                throw new UnsupportedWebsiteException("Unsupported website: " + websiteName);
            }
            redisService.cache(RedisKeyPrefixEnum.TASK_WEBSITE, taskId, website);
            // 缓存task基本信息
            TaskUtils.initTaskShare(taskId, loginInfo.getWebsiteName());
            TaskUtils.addTaskShare(taskId, AttributeKey.USERNAME, loginInfo.getAccountNo());
            TaskUtils.addTaskShare(taskId, AttributeKey.GROUP_CODE, website.getGroupCode());
            TaskUtils.addTaskShare(taskId, AttributeKey.GROUP_NAME, website.getGroupName());
            TaskUtils.addTaskShare(taskId, AttributeKey.WEBSITE_TITLE, website.getWebsiteTitle());
            TaskUtils.addTaskShare(taskId, AttributeKey.WEBSITE_TYPE, website.getWebsiteType());

            // 初始化监控信息
            monitorService.initTask(taskId, websiteName, loginInfo.getAccountNo());
            TaskUtils.addStep(taskId, StepEnum.INIT_SUCCESS);
            monitorService.sendTaskLog(taskId, websiteName, "爬虫-->启动-->成功");
            taskPointManager.addTaskPoint(taskId, SPIDER_STARTING_POINT_CODE);

            CollectorMessage message = buildCollectorMessage(loginInfo);
            message.setMsgId(messageExt.getMsgId());
            message.setBornTimestamp(messageExt.getBornTimestamp());
            // 启动爬虫
            collector.processMessage(message);
            return true;
        }
        logger.warn("重复消息,不处理,taskId={},websiteName={}", taskId, loginInfo.getWebsiteName());
        return true;
    }
}
