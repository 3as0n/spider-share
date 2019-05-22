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

import akka.dispatch.Future;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.datatrees.common.actor.WrappedActorRef;
import com.datatrees.spider.share.service.collector.actor.TaskMessage;
import com.datatrees.spider.share.service.collector.common.CollectorConstants;
import com.datatrees.spider.share.service.domain.ExtractMessage;
import com.datatrees.spider.share.service.domain.SpiderTask;
import com.datatrees.spider.share.service.normalizers.MessageNormalizerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月31日 上午10:45:33
 */
@Service
public class ResultDataHandler {

    private static final Logger log = LoggerFactory.getLogger(ResultDataHandler.class);

    @Resource
    private MessageNormalizerFactory messageNormalizerFactory;

    @Resource
    private WrappedActorRef extractorActorRef;

    public List<Future<Object>> resultListHandler(List<Object> objs, TaskMessage taskMessage) {
        List<Future<Object>> futureList = new ArrayList<>();

        SpiderTask task = new SpiderTask(taskMessage.getProcessId(), taskMessage.getContext());
        task.setCollectorMessage(taskMessage.getCollectorMessage());

        for (Object obj : objs) {
            ExtractMessage message = new ExtractMessage(task, obj);

            try {
                boolean result = messageNormalizerFactory.normalize(message);
                if (result) {
                    Future<Object> future = Patterns.ask(extractorActorRef.getActorRef(), message, new Timeout(CollectorConstants.EXTRACT_ACTOR_TIMEOUT));
                    futureList.add(future);
                } else {
                    log.warn("message normalize failed, message:" + message + ", obj:" + obj);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return futureList;
    }
}
