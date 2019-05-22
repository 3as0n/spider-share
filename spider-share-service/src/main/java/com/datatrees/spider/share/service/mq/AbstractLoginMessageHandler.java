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

package com.datatrees.spider.share.service.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.datatrees.spider.share.domain.LoginMessage;
import com.datatrees.spider.share.service.MonitorService;
import com.treefintech.spider.share.integration.manager.TaskPointManager;

/**
 * @author Jerry
 * @date 2019-01-26 15:20
 */
public abstract class AbstractLoginMessageHandler extends AbstractSpiderBootMessageHandler {

    private static final String SPIDER_STARTING_POINT_CODE = "900201";
    protected final MonitorService monitorService;
    protected final TaskPointManager taskPointManager;

    public AbstractLoginMessageHandler(MonitorService monitorService, TaskPointManager taskPointManager) {
        this.monitorService = monitorService;
        this.taskPointManager = taskPointManager;
    }

    @Override
    public boolean consumeMessage(MessageExt messageExt, String msg) {
        LoginMessage loginInfo = JSON.parseObject(msg, LoginMessage.class);

        return handle(loginInfo, messageExt);
    }

    protected abstract boolean handle(LoginMessage loginInfo, MessageExt messageExt);

    protected void sendStartingEvent(LoginMessage loginInfo) {
        Long taskId = loginInfo.getTaskId();
        monitorService.sendTaskLog(taskId, loginInfo.getWebsiteName(), "爬虫-->启动-->成功");

        taskPointManager.addTaskPoint(taskId, SPIDER_STARTING_POINT_CODE);
    }

}
