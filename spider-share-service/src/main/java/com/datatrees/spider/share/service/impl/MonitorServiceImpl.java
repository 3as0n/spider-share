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
import com.datatrees.spider.share.api.SpiderTaskApi;
import com.datatrees.spider.share.common.utils.DateUtils;
import com.datatrees.spider.share.common.utils.TaskUtils;
import com.datatrees.spider.share.common.utils.TemplateUtils;
import com.datatrees.spider.share.domain.AttributeKey;
import com.datatrees.spider.share.domain.ErrorCode;
import com.datatrees.spider.share.domain.TopicEnum;
import com.datatrees.spider.share.domain.TopicTag;
import com.datatrees.spider.share.domain.http.HttpResult;
import com.datatrees.spider.share.service.MonitorService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MonitorServiceImpl implements MonitorService {

    private static final Logger logger = LoggerFactory.getLogger(MonitorServiceImpl.class);

    private static final String DEFAULT_CHARSET_NAME = "UTF-8";

    @Resource
    private SpiderTaskApi crawlerTaskService;

    @Resource
    private DefaultMQProducer defaultMQProducer;

    @Override
    public void initTask(Long taskId, String websiteName, Object userName) {
        Map<String, String> map = crawlerTaskService.getTaskBaseInfo(taskId, websiteName);
        if (null != userName) {
            map.put(AttributeKey.USERNAME, userName.toString());
        }
        sendMessage(TopicEnum.CRAWLER_MONITOR.getCode(), TopicTag.TASK_INIT.getTag(), taskId, map);
    }

    @Override
    public void sendTaskCompleteMsg(Long taskId, String websiteName, Integer errorCode, String errorMsg) {
        Map<String, Object> map = new HashMap<>();
        map.put(AttributeKey.TASK_ID, taskId);
        map.put(AttributeKey.WEBSITE_NAME, websiteName);
        map.put(AttributeKey.ERROR_CODE, errorCode);
        map.put(AttributeKey.ERROR_MSG, errorMsg);
        map.put(AttributeKey.TIMESTAMP, System.currentTimeMillis());
        sendMessage(TopicEnum.CRAWLER_MONITOR.getCode(), TopicTag.TASK_COMPLETE.getTag(), taskId, map);
    }

    @Override
    public void sendTaskLog(Long taskId, String websiteName, String msg, Integer errorCode, String errorMsg, String errorDetail) {
        Map<String, Object> map = new HashMap<>();
        map.put(AttributeKey.TASK_ID, taskId);
        if (StringUtils.isBlank(websiteName)) {
            websiteName = TaskUtils.getTaskShare(taskId, AttributeKey.WEBSITE_NAME);
        }
        map.put(AttributeKey.WEBSITE_NAME, websiteName);
        map.put(AttributeKey.TIMESTAMP, System.currentTimeMillis());
        map.put(AttributeKey.MSG, msg);
        String websiteTitle = TaskUtils.getTaskShare(taskId, AttributeKey.WEBSITE_TITLE);
        String username = TaskUtils.getTaskShare(taskId, AttributeKey.USERNAME);
        map.put(AttributeKey.WEBSITE_TITLE, websiteTitle);
        map.put(AttributeKey.USERNAME, username);
        if (null != errorCode) {
            map.put(AttributeKey.ERROR_CODE, errorCode);
        }
        if (StringUtils.isNotBlank(errorMsg)) {
            map.put(AttributeKey.ERROR_MSG, errorMsg);
        }
        if (StringUtils.isNotBlank(errorDetail)) {
            map.put(AttributeKey.ERROR_DETAIL, errorDetail);
        }
        sendMessage(TopicEnum.CRAWLER_MONITOR.getCode(), TopicTag.TASK_LOG.getTag(), taskId, map);
    }

    @Override
    public void sendTaskLog(Long taskId, String websiteName, String msg, HttpResult result) {
        sendTaskLog(taskId, websiteName, msg, result.getResponseCode(), result.getMessage(), result.getErrorDetail());
    }

    @Override
    public void sendTaskLog(Long taskId, String websiteName, String msg, ErrorCode errorCode) {
        sendTaskLog(taskId, websiteName, msg, errorCode.getErrorCode(), errorCode.getErrorMsg(), null);
    }

    @Override
    public void sendTaskLog(Long taskId, String websiteName, String msg, ErrorCode errorCode, String errorDetail) {
        sendTaskLog(taskId, websiteName, msg, errorCode.getErrorCode(), errorCode.getErrorMsg(), errorDetail);
    }

    @Override
    public void sendTaskLog(Long taskId, String websiteName, String msg) {
        sendTaskLog(taskId, websiteName, msg, null, null, null);
    }

    @Override
    public void sendTaskLog(Long taskId, String msg) {
        sendTaskLog(taskId, null, msg, null, null, null);
    }

    @Override
    public void sendTaskLog(Long taskId, String msg, ErrorCode errorCode, String errorDetail) {
        sendTaskLog(taskId, null, msg, errorCode.getErrorCode(), errorCode.getErrorMsg(), errorDetail);
    }

    @Override
    public void sendTaskLog(Long taskId, String msg, ErrorCode errorCode) {
        sendTaskLog(taskId, null, msg, errorCode.getErrorCode(), errorCode.getErrorMsg(), null);
    }

    @Override
    public void sendMethodUseTime(Long taskId, String websiteName, String key, String className, String methodName, List<Object> param, Object result, long startTime,
        long endTime) {
        Map<String, Object> map = new HashMap<>();
        map.put(AttributeKey.TIMESTAMP, System.currentTimeMillis());
        map.put(AttributeKey.TASK_ID, taskId);
        map.put(AttributeKey.WEBSITE_NAME, websiteName);
        map.put(AttributeKey.KEY, key);
        map.put(AttributeKey.CLASS_NAME, className);
        map.put(AttributeKey.METHOD_NAME, methodName);
        map.put(AttributeKey.PARAM, param);
        map.put(AttributeKey.RESULT, result);
        map.put(AttributeKey.START_TIME, startTime);
        map.put(AttributeKey.END_TIME, endTime);
        map.put(AttributeKey.REMARK, DateUtils.getUsedTime(startTime, endTime));
        if (null != result) {
            map.put(AttributeKey.RESULT_CLASS, result.getClass().getName());
        }
        sendMessage(TopicEnum.CRAWLER_MONITOR.getCode(), TopicTag.METHOD_USE_TIME.getTag(), taskId, map);
    }

    public boolean sendMessage(String topic, String tags, Long taskId, Object msg) {
        if (StringUtils.isBlank(topic) || null == msg) {
            logger.error("invalid param  topic={},msg={}", topic, msg);
            return false;
        }
        String content = JSON.toJSONString(msg);
        try {
            Message mqMessage = new Message();
            mqMessage.setTopic(topic);
            mqMessage.setBody(content.getBytes(DEFAULT_CHARSET_NAME));
            String key = TemplateUtils.format("{}.{}.{}", topic, StringUtils.isNotBlank(tags) ? tags : "", taskId);
            mqMessage.setKeys(key);
            if (StringUtils.isNotBlank(tags)) {
                mqMessage.setTags(tags);
            }
            SendResult sendResult = defaultMQProducer.send(mqMessage);
            if (sendResult != null && SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
                logger.info("send message success topic={},tags={},content={},charsetName={},msgId={}", topic, tags, content.length() > 100 ? content.substring(0, 100) : content,
                    DEFAULT_CHARSET_NAME, sendResult.getMsgId());
                return true;
            }
        } catch (Exception e) {
            logger.error("send message error topic={},content={},charsetName={}", topic, content, DEFAULT_CHARSET_NAME, e);
        }
        logger.error("send message fail topic={},content={},charsetName={}", topic, content, DEFAULT_CHARSET_NAME);
        return false;
    }

}
