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

package com.datatrees.spider.share.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.datatrees.common.util.StringUtils;
import com.datatrees.spider.share.common.share.service.RedisService;
import com.datatrees.spider.share.common.utils.RegexpUtils;
import com.datatrees.spider.share.common.utils.TaskUtils;
import com.datatrees.spider.share.domain.AttributeKey;
import com.datatrees.spider.share.domain.TopicEnum;
import com.datatrees.spider.share.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouxinghai on 2017/5/11.
 */
@Service
public class MessageServiceImpl implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    /**
     * 默认格式格式化成JSON后发送的字符编码
     */
    private static final String DEFAULT_CHARSET_NAME = "UTF-8";

    @Resource
    private DefaultMQProducer defaultMQProducer;

    @Resource
    private RedisService redisService;

    @Override
    public boolean sendTaskLog(Long taskId, String msg, String errorDetail) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskId", taskId);
        map.put("timestamp", System.currentTimeMillis());
        map.put("msg", msg);
        map.put("errorDetail", errorDetail);
        return sendMessage(TopicEnum.TASK_LOG.getCode(), map, DEFAULT_CHARSET_NAME);
    }

    @Override
    public String sendDirective(Long taskId, String directive, String remark) {
        return sendDirective(taskId, directive, remark, null);
    }

    @Override
    public String sendDirective(Long taskId, String directive, String remark, String formType) {
        if (null == taskId || StringUtils.isBlank(directive)) {
            logger.error("invalid param taskId={},directive={}", taskId, directive);
            return null;
        }
        String directiveId = redisService.createDirectiveId();
        Map<String, Object> msg = new HashMap<String, Object>();
        msg.put(AttributeKey.TASK_ID, taskId);
        msg.put(AttributeKey.DIRECTIVE_ID, directiveId);
        msg.put(AttributeKey.DIRECTIVE, directive);
        msg.put(AttributeKey.REMARK, remark);
        msg.put(AttributeKey.FORM_TYPE, formType);
        msg.put(AttributeKey.TIMESTAMP, System.currentTimeMillis());
        sendMessage(TopicEnum.TASK_NEXT_DIRECTIVE.getCode(), msg, DEFAULT_CHARSET_NAME);
        return directiveId;
    }

    @Override
    public boolean sendTaskLog(Long taskId, String msg) {
        return sendTaskLog(taskId, msg, null);
    }

    @Override
    public boolean sendMessage(String topic, Object msg) {
        return sendMessage(topic, msg, DEFAULT_CHARSET_NAME);
    }

    @Override
    public boolean sendMessage(String topic, Object msg, String charsetName) {
        return sendMessage(topic, null, msg, charsetName);
    }

    @Override
    public boolean sendMessage(String topic, String tags, Object msg) {
        return sendMessage(topic, tags, msg, DEFAULT_CHARSET_NAME);
    }

    @Override
    public boolean sendMessage(String topic, String tags, Object msg, String charsetName) {
        if (StringUtils.isBlank(topic) || null == msg) {
            logger.error("invalid param  topic={},msg={}", topic, msg);
            return false;
        }
        if (StringUtils.isBlank(charsetName)) {
            charsetName = DEFAULT_CHARSET_NAME;
        }
        String content = JSON.toJSONString(msg);
        try {
            Message mqMessage = new Message();
            mqMessage.setTopic(topic);
            mqMessage.setBody(content.getBytes(charsetName));
            StringBuilder key = new StringBuilder(topic);
            if (StringUtils.isNotBlank(tags)) {
                key.append("_").append(tags);
            }
            String taskId = RegexpUtils.select(content, "taskId\":(\\d+)", 1);
            if (StringUtils.isNotBlank(taskId)) {
                key.append("_").append(taskId);
            } else {
                key.append("_").append(System.currentTimeMillis());
            }
            mqMessage.setKeys(key.toString());
            if (StringUtils.isNotBlank(tags)) {
                mqMessage.setTags(tags);
            }
            SendResult sendResult = defaultMQProducer.send(mqMessage);
            if (sendResult != null && SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
                logger.info("send message success topic={},tags={},content={},charsetName={},msgId={}", topic, tags, content.length() > 100 ? content.substring(0, 100) : content,
                    charsetName, sendResult.getMsgId());
                return true;
            }
        } catch (Exception e) {
            logger.info("send message error topic={},content={},charsetName={}", topic, content, charsetName, e);
        }
        logger.error("send message fail topic={},content={},charsetName={}", topic, content, charsetName);
        return false;
    }

    @Override
    public void sendLoginSuccessMessage(String topic, String tag, long taskId) {
        Map<String, String> map = TaskUtils.getTaskShares(taskId);
        String cookies = TaskUtils.getCookieString(taskId);
        map.put(AttributeKey.COOKIE, cookies);
        sendMessage(topic, tag, map, DEFAULT_CHARSET_NAME);
    }

}
