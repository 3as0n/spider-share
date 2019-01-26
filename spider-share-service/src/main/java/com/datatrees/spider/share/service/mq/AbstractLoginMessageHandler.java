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

package com.datatrees.spider.share.service.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.datatrees.spider.share.domain.LoginMessage;
import com.datatrees.spider.share.service.MonitorService;
import com.treefinance.saas.taskcenter.facade.request.TaskPointRequest;
import com.treefinance.saas.taskcenter.facade.service.TaskPointFacade;
import com.treefinance.toolkit.util.net.NetUtils;

/**
 * @author Jerry
 * @date 2019-01-26 15:20
 */
public abstract class AbstractLoginMessageHandler extends AbstractSpiderBootMessageHandler {
    protected final MonitorService monitorService;
    protected final TaskPointFacade taskPointFacade;

    public AbstractLoginMessageHandler(MonitorService monitorService, TaskPointFacade taskPointFacade) {
        this.monitorService = monitorService;
        this.taskPointFacade = taskPointFacade;
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
        TaskPointRequest taskPointRequest = new TaskPointRequest();
        taskPointRequest.setTaskId(taskId);
        taskPointRequest.setType((byte)1);
        taskPointRequest.setCode("900201");
        taskPointRequest.setIp(NetUtils.getLocalHost());
        taskPointFacade.addTaskPoint(taskPointRequest);
    }

}
