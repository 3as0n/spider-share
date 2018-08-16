/*
 * Copyright © 2015 - 2018 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.datatrees.spider.share.service.extract;

import javax.annotation.Resource;

import com.datatrees.common.actor.AbstractActor;
import com.datatrees.spider.share.service.domain.ExtractMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by wuminlang on 15/7/29.
 */
@Service
@Scope("prototype")
public class ExtractorActor extends AbstractActor {

    private static final Logger        logger = LoggerFactory.getLogger(ExtractorActor.class);

    @Resource
    private              ExtractWorker extractWorker;

    @Override
    public void processMessage(Object message) {
        logger.info("starting task worker for [" + message.toString() + "]");
        if (message instanceof ExtractMessage) {
            extractWorker.process((ExtractMessage) message);
        } else {
            unhandled(message);
        }
    }

    @Override
    public void processComplete(Object message) {
        if (message instanceof ExtractMessage) {
            getSender().tell(message);
        } else {
            unhandled(message);
        }

    }

}
