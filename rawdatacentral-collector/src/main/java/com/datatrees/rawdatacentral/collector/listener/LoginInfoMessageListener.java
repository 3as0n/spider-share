/**
 * This document and its contents are protected by copyright 2015 and owned by datatrees.com Inc.
 * The copying and reproduction of this document and/or its content (whether wholly or partly) or
 * any incorporation of the same into any other material in any media or format of any kind is
 * strictly prohibited. All rights are reserved.
 * Copyright (c) datatrees.com Inc. 2015
 */

package com.datatrees.rawdatacentral.collector.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.crawler.core.domain.Website;
import com.datatrees.rawdatacentral.api.RedisService;
import com.datatrees.rawdatacentral.collector.actor.Collector;
import com.datatrees.rawdatacentral.common.http.ProxyUtils;
import com.datatrees.rawdatacentral.common.http.TaskUtils;
import com.datatrees.rawdatacentral.common.utils.BeanFactoryUtils;
import com.datatrees.rawdatacentral.core.message.AbstractRocketMessageListener;
import com.datatrees.rawdatacentral.core.model.message.impl.CollectorMessage;
import com.datatrees.rawdatacentral.domain.constant.AttributeKey;
import com.datatrees.rawdatacentral.domain.enums.RedisKeyPrefixEnum;
import com.datatrees.rawdatacentral.domain.enums.TaskStageEnum;
import com.datatrees.rawdatacentral.domain.model.WebsiteOperator;
import com.datatrees.rawdatacentral.domain.mq.message.LoginMessage;
import com.datatrees.rawdatacentral.domain.operator.OperatorParam;
import com.datatrees.rawdatacentral.service.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since 2015年8月13日 下午2:27:24
 */
public class LoginInfoMessageListener extends AbstractRocketMessageListener<CollectorMessage> {

    private static final Logger  logger                = LoggerFactory.getLogger(LoginInfoMessageListener.class);
    private static final boolean setCookieFormatSwitch = PropertiesConfiguration.getInstance().getBoolean("set.cookie.format.switch", false);
    private Collector           collector;
    private StringRedisTemplate redisTemplate;
    private RedisService           redisService           = BeanFactoryUtils.getBean(RedisService.class);
    private WebsiteConfigService   websiteConfigService   = BeanFactoryUtils.getBean(WebsiteConfigService.class);
    private MonitorService         monitorService         = BeanFactoryUtils.getBean(MonitorService.class);
    private WebsiteOperatorService websiteOperatorService = BeanFactoryUtils.getBean(WebsiteOperatorService.class);
    private ClassLoaderService     classLoaderService     = BeanFactoryUtils.getBean(ClassLoaderService.class);

    public Collector getCollector() {
        return collector;
    }

    public void setCollector(Collector collector) {
        this.collector = collector;
    }

    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void process(CollectorMessage message) {
        Long taskId = message.getTaskId();
        String key = RedisKeyPrefixEnum.TASK_RUN_STAGE.getRedisKey(taskId);
        Boolean initStatus = redisTemplate.opsForValue().setIfAbsent(key, TaskStageEnum.INIT.getStatus());
        //第一次收到启动消息
        if (initStatus) {
            Website website = null;
            //30分钟内不再接受重复消息
            redisTemplate.expire(key, RedisKeyPrefixEnum.TASK_RUN_STAGE.getTimeout(), RedisKeyPrefixEnum.TASK_RUN_STAGE.getTimeUnit());
            //是否是独立运营商
            Boolean isNewOperator = StringUtils.startsWith(message.getWebsiteName(), RedisKeyPrefixEnum.WEBSITE_OPERATOR_RENAME.getPrefix());
            TaskUtils.addTaskShare(taskId, AttributeKey.IS_NEW_OPERATOR, isNewOperator + "");
            //保存第一次消息使用的原始websiteName,有别名就是独立模块
            TaskUtils.addTaskShare(taskId, AttributeKey.FIRST_VISIT_WEBSITENAME, message.getWebsiteName());
            //获取真实websiteName
            String realebsiteName = getRealWebsiteName(message.getWebsiteName());
            //后面的刷新图片验证码依赖这个
            TaskUtils.addTaskShare(taskId, AttributeKey.WEBSITE_NAME, realebsiteName);
            //保存第一次消息使用的原始websiteName,有别名就是独立模块
            redisService.saveString(RedisKeyPrefixEnum.TASK_FIRST_VISIT_WEBSITENAME, taskId, message.getWebsiteName());
            //发送任务初始化消息
            BeanFactoryUtils.getBean(MonitorService.class).initTask(taskId);
            if (isNewOperator) {
                //从新的运营商表读取配置
                WebsiteOperator websiteOperator = websiteOperatorService.getByWebsiteName(realebsiteName);
                //保存taskId对应的website,因为运营过程中用的是
                website = websiteConfigService.buildWebsite(websiteOperator);
                redisService.cache(RedisKeyPrefixEnum.TASK_WEBSITE, taskId, website);
                //初始化监控信息
                monitorService.initTask(taskId);
                //设置代理
                ProxyUtils.setProxyEnable(taskId, websiteOperator.getProxyEnable());
                //执行运营商插件初始化操作
                OperatorPluginService operatorPluginService = classLoaderService.getOperatorPluginService(realebsiteName);
                OperatorParam param = new OperatorParam();
                param.setTaskId(taskId);
                param.setWebsiteName(realebsiteName);
                operatorPluginService.init(param);

                //运营商独立部分第一次初始化后不启动爬虫
            } else {
                //这里电商,邮箱,老运营商
                website = websiteConfigService.getWebsiteByWebsiteName(message.getWebsiteName());
                //保存taskId对应的website
                redisService.cache(RedisKeyPrefixEnum.TASK_WEBSITE, taskId, website);
                //初始化监控信息
                monitorService.initTask(taskId);
                //启动爬虫
                collector.processMessage(message);
            }
            logger.info("receve login message taskId={},firstVisitWebsiteName={}", taskId, message.getWebsiteName());
        } else {
            //这里电商,邮箱,老运营商不会有第二次消息,这里只处理运营商登录成功消息
            //获取第一次消息用的websiteName
            String firstVisitWebsiteName = redisService.getString(RedisKeyPrefixEnum.TASK_FIRST_VISIT_WEBSITENAME, taskId);
            //兼容老的
            if (StringUtils.isBlank(firstVisitWebsiteName)) {
                firstVisitWebsiteName = redisService.getString(RedisKeyPrefixEnum.WEBSITE_OPERATOR_RENAME, taskId);
            }
            //是否是独立运营商
            Boolean isNewOperator = StringUtils.startsWith(firstVisitWebsiteName, RedisKeyPrefixEnum.WEBSITE_OPERATOR_RENAME.getPrefix());
            //非运营商或者老运营商,重复消息不处理
            if (!isNewOperator) {
                logger.warn("重复消息,不处理,taskId={},firstVisitWebsiteName={}", taskId, firstVisitWebsiteName);
                return;
            }
            //如果是登录成功消息就启动爬虫
            String taskStage = redisService.getString(RedisKeyPrefixEnum.TASK_RUN_STAGE.getRedisKey(taskId));
            if (StringUtils.equals(taskStage, TaskStageEnum.CRAWLER_START.getStatus())) {
                String websiteName = TaskUtils.getTaskShare(taskId, AttributeKey.WEBSITE_NAME);
                //之后运行还是数据库真实websiteName
                message.setWebsiteName(websiteName);
                collector.processMessage(message);
                logger.info("receve login message taskId={}", message.getTaskId());
            }
        }

    }

    @Override
    public CollectorMessage messageConvert(MessageExt message) {
        CollectorMessage collectorMessage = new CollectorMessage();
        String body = new String(message.getBody());
        try {
            LoginMessage loginInfo = JSON.parseObject(body, LoginMessage.class);
            if (loginInfo != null) {
                logger.info("Init logininfo:" + loginInfo);
                collectorMessage.setTaskId(loginInfo.getTaskId());
                collectorMessage.setWebsiteName(loginInfo.getWebsiteName());
                collectorMessage.setEndURL(loginInfo.getEndUrl());
                collectorMessage.setCookie(loginInfo.getCookie());
                collectorMessage.setAccountNo(loginInfo.getAccountNo());
                if (setCookieFormatSwitch && StringUtils.isNotBlank(loginInfo.getSetCookie())) {
                    if (StringUtils.isBlank(loginInfo.getCookie())) {
                        collectorMessage.setCookie(loginInfo.getSetCookie());
                    } else {
                        String cookie = collectorMessage.getCookie().endsWith(";") ? collectorMessage.getCookie() + loginInfo.getSetCookie() :
                                collectorMessage.getCookie() + ";" + loginInfo.getSetCookie();
                        collectorMessage.setCookie(cookie);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Message convert error.." + e.getMessage(), e);
        }
        return collectorMessage;
    }

    private String getRealWebsiteName(String websiteName) {
        if (StringUtils.startsWith(websiteName, RedisKeyPrefixEnum.WEBSITE_OPERATOR_RENAME.getPrefix())) {
            return RedisKeyPrefixEnum.WEBSITE_OPERATOR_RENAME.parsePostfix(websiteName);
        }
        return websiteName;
    }
}
