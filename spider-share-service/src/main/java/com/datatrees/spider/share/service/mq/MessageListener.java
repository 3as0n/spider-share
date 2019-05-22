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

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.datatrees.spider.share.common.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * mq消息处理 topic:crawler_monitor Created by zhouxinghai on 2017/10/17
 */
@Service
public class MessageListener implements MessageListenerConcurrently {

    private static final Logger logger = LoggerFactory.getLogger(MessageListener.class);

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        MessageExt messageExt = msgs.get(0);
        String topic = messageExt.getTopic();
        String tag = messageExt.getTags();
        String msgId = messageExt.getMsgId();
        String keys = messageExt.getKeys();
        long bornTimestamp = messageExt.getBornTimestamp();
        int reconsumeTimes = messageExt.getReconsumeTimes();

        String body = new String(messageExt.getBody(), DEFAULT_CHARSET);
        MessageHandler handler = getMessageHandler(topic, tag);
        if (null == handler) {
            logger.warn("no message handler found,丢弃消息,body={},topic={},tag={},msgId={},keys={}", body, topic, tag, msgId, keys);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        int maxRetry = handler.getMaxRetry();
        String bizType = handler.getTitle();
        if (reconsumeTimes > maxRetry) {
            logger.warn("{}-->失败,丢弃消息,msg={},msgId={},retry={},key={}", bizType, body, msgId, reconsumeTimes, messageExt.getKeys());
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        if (System.currentTimeMillis() - bornTimestamp > handler.getExpireTime()) {
            logger.warn("{}-->失败,已过期,丢弃消息,msg={},msgId={},retry={},key={},bornTimestamp={}", bizType, body, msgId, reconsumeTimes, messageExt.getKeys(),
                DateUtils.formatYmdhms(bornTimestamp));
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        try {
            boolean result = handler.consumeMessage(messageExt, body);
            if (!result) {
                if (reconsumeTimes < maxRetry) {
                    logger.warn("{}-->失败,稍后重试,msg={},msgId={},retry={},key={}", bizType, body, msgId, reconsumeTimes, messageExt.getKeys());
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                } else {
                    logger.warn("{}-->失败,丢弃消息,msg={},msgId={},retry={},key={}", bizType, body, msgId, reconsumeTimes, messageExt.getKeys());
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            }
            logger.info("{}-->成功,msg={},msgId={},retry={},key={}", bizType, body, msgId, reconsumeTimes, messageExt.getKeys());
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Throwable e) {
            if (reconsumeTimes > maxRetry) {
                logger.warn("{}-->失败,丢弃消息,msg={},msgId={},retry={},key={}", bizType, body, msgId, reconsumeTimes, messageExt.getKeys());
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } else {
                logger.error("{}-->失败,稍后重试,msg={},msgId={},retry={},key={}", bizType, body, msgId, reconsumeTimes, messageExt.getKeys(), e);
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }

    }

    private MessageHandler getMessageHandler(String topic, String tag) {
        Map<String, MessageHandler> map = applicationContext.getBeansOfType(MessageHandler.class);
        for (Map.Entry<String, MessageHandler> entry : map.entrySet()) {
            MessageHandler handler = entry.getValue();
            if (StringUtils.equals(handler.getTopic(), topic) && StringUtils.equals(handler.getTag(), tag)) {
                return handler;
            }
        }
        return null;
    }

}
