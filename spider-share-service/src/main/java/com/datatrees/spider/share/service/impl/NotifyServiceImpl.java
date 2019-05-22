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
import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.notify.async.body.mail.MailBody;
import com.datatrees.notify.async.body.mail.MailEnum;
import com.datatrees.notify.async.body.sms.SmsEnum;
import com.datatrees.notify.async.body.wechat.WeChatBody;
import com.datatrees.notify.async.body.wechat.WeChatEnum;
import com.datatrees.notify.async.body.wechat.message.TXTMessage;
import com.datatrees.notify.async.check.PremiseChecker;
import com.datatrees.notify.async.util.BeanUtil;
import com.datatrees.notify.sms.bean.SmsResult;
import com.datatrees.notify.sms.newservice.SmsNewService;
import com.datatrees.notify.sms.newservice.entity.message.SmsMessage;
import com.datatrees.spider.share.service.NotifyService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.List;

@Service
public class NotifyServiceImpl implements NotifyService {

    private static final Logger logger = LoggerFactory.getLogger(NotifyServiceImpl.class);

    /**
     * 预警业务类型
     */
    private static final String BUSINESS = "monitor";

    /**
     * 预警topic
     */
    private static final String TOPIC_NOTIFY = "mq_topic_notify";

    @Resource
    private SmsNewService smsNewService;

    @Resource
    private DefaultMQProducer defaultMQProducer;

    @Override
    public Boolean sendMonitorEmail(String subject, String body) {
        String emailReceiver = PropertiesConfiguration.getInstance().get("monitor.email.receiver");
        if (StringUtils.isBlank(emailReceiver)) {
            logger.warn("monitor.email.receiver is blank");
            return false;
        }
        List<String> receiver = Arrays.asList(emailReceiver.split(","));
        MailBody msg = buildMailBody(receiver, subject, body);
        try {
            String msgId = sendNotifyMessage(msg);
            if (null != msgId) {
                logger.info("send email msg success receiver={},subject={},body={},msgId={}", JSON.toJSONString(receiver), subject, body, msgId);
                return true;
            } else {
                logger.warn("send email msg fail receiver={},subject={},body={}", JSON.toJSONString(receiver), subject, body);
                return false;
            }
        } catch (Throwable e) {
            logger.error("send email msg  error receiver={},subject={}", JSON.toJSONString(receiver), subject, e);
            return false;
        }
    }

    @Override
    public Boolean sendMonitorWeChat(String body) {
        String wechatReceiver = PropertiesConfiguration.getInstance().get("monitor.wechat.receiver");
        if (StringUtils.isBlank(wechatReceiver)) {
            logger.warn("monitor.wechat.receiver is blank");
            return false;
        }
        List<String> receiver = Arrays.asList(wechatReceiver.trim().split(","));
        WeChatBody msg = buildWeChatBody(receiver, body);
        try {
            String msgId = sendNotifyMessage(msg);
            if (null != msgId) {
                logger.info("send wechat msg success receiver={},subject={},body={},msgId={}", JSON.toJSONString(receiver), body, msgId);
                return true;
            } else {
                logger.warn("send wechat msg fail receiver={},subject={},body={}", JSON.toJSONString(receiver), body);
                return false;
            }
        } catch (Throwable e) {
            logger.error("send wechat msg  error receiver={},body={}", JSON.toJSONString(receiver), body, e);
            return false;
        }
    }

    @Override
    public Boolean sendMonitorSms(String body) {
        String smsReceiver = PropertiesConfiguration.getInstance().get("monitor.sms.receiver");
        if (StringUtils.isBlank(smsReceiver)) {
            logger.warn("monitor.sms.receiver is blank");
            return false;
        }
        SmsResult result = null;
        try {
            SmsMessage message = buildSmsMessage(Arrays.asList(smsReceiver.trim().split(",")), body);
            result = smsNewService.sendMessage(message);
            if (null == result || 1 != result.getCode()) {
                logger.error("sendSms fail,mobile={},sms={},result={},message={}", smsReceiver, body, JSON.toJSONString(result), JSON.toJSONString(message));
                return false;
            }
            logger.info("resu sms success mobile={},sms={},result={}", smsReceiver, body, JSON.toJSONString(result));
            return true;
        } catch (Throwable e) {
            logger.error("resu sms msg  error mobile={},sms={},result={}", smsReceiver, body, JSON.toJSONString(result), e);
            return false;
        }
    }

    private String sendNotifyMessage(Object body) {
        try {
            PremiseChecker.check(body);
            Message msg = new Message(TOPIC_NOTIFY, null, null, BeanUtil.objectToByte(body));
            SendResult sendResult = defaultMQProducer.send(msg);
            if (null != sendResult && sendResult.getSendStatus() == SendStatus.SEND_OK) {
                return sendResult.getMsgId();
            } else {
                return null;
            }
        } catch (Throwable e) {
            logger.error("sendMonitorEmailUseMQ error body={}", JSON.toJSONString(body), e);
            return null;
        }
    }

    private SmsMessage buildSmsMessage(List<String> receiver, String body) {
        SmsMessage message = new SmsMessage();
        message.setContent(body);
        message.setMobileList(receiver);
        message.setSerialNumber("crawler.monitor.sms." + System.currentTimeMillis());
        String smsChannel = PropertiesConfiguration.getInstance().get("monitor.sms.channel");
        message.setSmsEnum(SmsEnum.get(smsChannel));
        return message;
    }

    private WeChatBody buildWeChatBody(List<String> receiver, String body) {
        TXTMessage message = new TXTMessage();
        message.setMessage(body);
        WeChatBody weChatBody = new WeChatBody();
        // 爬虫:1000007 nginx监控:60
        weChatBody.setAgentId("1000007");
        weChatBody.setWeChatEnum(WeChatEnum.DASHU_AN_APP_TXT);
        weChatBody.setMessage(message);
        weChatBody.setWxUserList(receiver);
        return weChatBody;
    }

    private MailBody buildMailBody(List<String> receiver, String subject, String body) {
        MailBody mailBody = new MailBody();
        mailBody.setMailEnum(MailEnum.HTML_MAIL);
        mailBody.setBusiness(BUSINESS);
        mailBody.setToList(receiver);
        mailBody.setSubject(subject);
        mailBody.setBody(body);
        return mailBody;
    }
}
