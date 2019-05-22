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

import com.alibaba.rocketmq.common.message.MessageExt;
import com.datatrees.spider.share.domain.CollectorMessage;
import com.datatrees.spider.share.domain.LoginMessage;
import com.datatrees.spider.share.service.MonitorService;
import com.datatrees.spider.share.service.collector.CollectorMessageUtils;
import com.datatrees.spider.share.service.collector.actor.Collector;
import com.treefintech.spider.share.integration.manager.TaskPointManager;

/**
 * @author Jerry
 * @date 2019-01-26 15:24
 */
public abstract class AbstractSimpleLoginMessageHandler extends AbstractLoginMessageHandler {

    protected final Collector collector;

    public AbstractSimpleLoginMessageHandler(Collector collector, MonitorService monitorService, TaskPointManager taskPointManager) {
        super(monitorService, taskPointManager);
        this.collector = collector;
    }

    @Override
    protected boolean handle(LoginMessage loginInfo, MessageExt messageExt) {
        super.sendStartingEvent(loginInfo);

        CollectorMessage message = CollectorMessageUtils.buildCollectorMessage(loginInfo);
        message.setMsgId(messageExt.getMsgId());
        message.setBornTimestamp(messageExt.getBornTimestamp());
        // 启动爬虫
        collector.processMessage(message);
        return true;
    }

}
