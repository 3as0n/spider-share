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

package com.datatrees.spider.share.service.impl;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.datatrees.common.util.GsonUtils;
import com.datatrees.crawler.core.processor.common.resource.DataResource;
import com.datatrees.spider.share.domain.ResultMessage;
import com.datatrees.spider.share.service.dao.RedisDao;
import com.datatrees.spider.share.service.message.MessageFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class GatewayServiceImpl implements DataResource {

    private static final Logger logger = LoggerFactory.getLogger(GatewayServiceImpl.class);

    @Resource
    private RedisDao redisDao;

    @Resource
    private MessageFactory messageFactory;

    @Resource
    private DefaultMQProducer defaultMQProducer;

    @Override
    public Object getData(Map<String, Object> parameters) {
        try {
            String result = redisDao.getStringFromList(genRedisKey(parameters));
            if (StringUtils.isNotEmpty(result)) {
                logger.debug("obtain result sucess!");
                return result;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean sendToQueue(Map<String, Object> parameters) {
        try {
            String taskId = String.valueOf(parameters.get("taskId"));
            String websiteName = String.valueOf(parameters.get("websiteName"));
            String status = String.valueOf(parameters.get("status"));
            String isEmpty = String.valueOf(parameters.get("isResultEmpty"));
            String remark = String.valueOf(parameters.get("remark"));
            String tag = String.valueOf(parameters.get("tag"));

            ResultMessage resultMessage = new ResultMessage();
            resultMessage.putAll(parameters);
            resultMessage.setRemark(remark);
            resultMessage.setTaskId(Long.parseLong(taskId));
            resultMessage.setWebsiteName(websiteName);
            resultMessage.setResultEmpty(Boolean.valueOf(isEmpty));
            resultMessage.setStatus(status);
            if (logger.isDebugEnabled()) {
                logger.debug("send to queue:" + GsonUtils.toJson(resultMessage));
            }
            Message mqMessage = messageFactory.getMessage("rawData_result_status", tag, GsonUtils.toJson(resultMessage), taskId);
            SendResult sendResult = defaultMQProducer.send(mqMessage);
            logger.info("send result message:" + mqMessage + "result:" + sendResult);
            if (sendResult != null && SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
                return true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean ttlSave(String key, String value, long timeOut) {
        try {
            redisDao.getRedisTemplate().opsForValue().set(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        redisDao.getRedisTemplate().expire(key, timeOut, TimeUnit.SECONDS);
        return true;
    }

    @Override
    public boolean ttlPush(String key, String value, long timeOut) {
        try {
            Long result = redisDao.getRedisTemplate().opsForList().rightPushAll(key, value);
            redisDao.getRedisTemplate().expire(key, timeOut, TimeUnit.SECONDS);
            return result != null ? true : false;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean clearData(Map<String, Object> parameters) {
        try {
            String key = genRedisKey(parameters);
            logger.info("init clear redis data key :" + key);
            redisDao.deleteKey(key);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    private String genRedisKey(Map<String, Object> parameters) {
        StringBuilder sb = new StringBuilder();
        String taskId = String.valueOf(parameters.get("taskId"));
        return sb.append("verify_result_").append(taskId).toString();
    }

}
