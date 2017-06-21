/**
 * This document and its contents are protected by copyright 2015 and owned by datatrees.com Inc.
 * The copying and reproduction of this document and/or its content (whether wholly or partly) or
 * any incorporation of the same into any other material in any media or format of any kind is
 * strictly prohibited. All rights are reserved.
 * Copyright (c) datatrees.com Inc. 2015
 */
package com.datatrees.rawdatacentral.collector.listener;

import com.alibaba.rocketmq.common.message.MessageExt;
import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.common.util.GsonUtils;
import com.datatrees.rawdatacentral.collector.actor.Collector;
import com.datatrees.rawdatacentral.core.message.AbstractRocketMessageListener;
import com.datatrees.rawdatacentral.core.model.message.impl.CollectorMessage;
import com.datatrees.rawdatacentral.domain.mq.message.LoginMessage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since 2015年8月13日 下午2:27:24
 */
public class LoginInfoMessageListener extends AbstractRocketMessageListener<CollectorMessage> {

    private static final Logger  log                   = LoggerFactory.getLogger(LoginInfoMessageListener.class);

    private static final boolean setCookieFormatSwitch = PropertiesConfiguration.getInstance()
        .getBoolean("set.cookie.format.switch", false);

    private Collector            collector;

    private StringRedisTemplate  redisTemplate;

    public Collector getCollector() {
        return collector;
    }

    public void setCollector(Collector collector) {
        this.collector = collector;
    }

    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /*
             * (non-Javadoc)
             * @see
             * AbstractRocketMessageListener#process(java.lang.Object)
             */
    @Override
    public void process(CollectorMessage message) {
        if (message.getTaskId() > 0) {
            String redisKey = "run_count:" + message.getTaskId();
            if (!redisTemplate.hasKey(redisKey)) {
                redisTemplate.opsForValue().setIfAbsent(redisKey, "0");
            }
            Long totalRun = redisTemplate.opsForValue().increment(redisKey, 1);
            message.setTotalRun(totalRun);
            log.info("receve login message taskId={},totalRun={}", message.getTaskId(), totalRun);
            collector.processMessage(message);
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * AbstractRocketMessageListener#messageConvert(com.alibaba
     * .rocketmq.common.message.Message)
     */
    @Override
    public CollectorMessage messageConvert(MessageExt message) {
        CollectorMessage collectorMessage = new CollectorMessage();
        String body = new String(message.getBody());
        try {
            LoginMessage loginInfo = (LoginMessage) GsonUtils.fromJson(body, LoginMessage.class);
            if (loginInfo != null) {
                log.info("Init logininfo:" + loginInfo);
                collectorMessage.setTaskId(loginInfo.getTaskId());
                collectorMessage.setWebsiteName(loginInfo.getWebsiteName());
                collectorMessage.setEndURL(loginInfo.getEndUrl());
                collectorMessage.setCookie(loginInfo.getCookie());
                collectorMessage.setAccountNo(loginInfo.getAccountNo());
                if (setCookieFormatSwitch && StringUtils.isNotBlank(loginInfo.getSetCookie())) {
                    if (StringUtils.isBlank(loginInfo.getCookie())) {
                        collectorMessage.setCookie(loginInfo.getSetCookie());
                    } else {
                        String cookie = collectorMessage.getCookie().endsWith(";")
                            ? collectorMessage.getCookie() + loginInfo.getSetCookie()
                            : collectorMessage.getCookie() + ";" + loginInfo.getSetCookie();
                        collectorMessage.setCookie(cookie);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Message convert error.." + e.getMessage(), e);
        }
        return collectorMessage;
    }
}
