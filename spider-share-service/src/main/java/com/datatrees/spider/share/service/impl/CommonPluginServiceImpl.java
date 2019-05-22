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
import com.datatrees.spider.share.common.http.ProxyUtils;
import com.datatrees.spider.share.common.share.service.ProxyService;
import com.datatrees.spider.share.common.share.service.RedisService;
import com.datatrees.spider.share.common.utils.BackRedisUtils;
import com.datatrees.spider.share.common.utils.BeanFactoryUtils;
import com.datatrees.spider.share.common.utils.DateUtils;
import com.datatrees.spider.share.common.utils.RedisUtils;
import com.datatrees.spider.share.common.utils.TaskUtils;
import com.datatrees.spider.share.common.utils.TemplateUtils;
import com.datatrees.spider.share.domain.AttributeKey;
import com.datatrees.spider.share.domain.CommonPluginParam;
import com.datatrees.spider.share.domain.ErrorCode;
import com.datatrees.spider.share.domain.FormType;
import com.datatrees.spider.share.domain.LoginMessage;
import com.datatrees.spider.share.domain.RedisKeyPrefixEnum;
import com.datatrees.spider.share.domain.StepEnum;
import com.datatrees.spider.share.domain.TopicTag;
import com.datatrees.spider.share.domain.http.Cookie;
import com.datatrees.spider.share.domain.http.HttpResult;
import com.datatrees.spider.share.domain.model.WebsiteInfo;
import com.datatrees.spider.share.service.ClassLoaderService;
import com.datatrees.spider.share.service.CommonPluginService;
import com.datatrees.spider.share.service.MessageService;
import com.datatrees.spider.share.service.MonitorService;
import com.datatrees.spider.share.service.WebsiteHolderService;
import com.datatrees.spider.share.service.WebsiteInfoService;
import com.datatrees.spider.share.service.plugin.QRPlugin;
import com.treefinance.crawler.framework.context.Website;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class CommonPluginServiceImpl implements CommonPluginService {

    private static final Logger logger = LoggerFactory.getLogger(CommonPluginServiceImpl.class);

    @Resource
    private ClassLoaderService classLoaderService;

    @Resource
    private RedisService redisService;

    @Resource
    private WebsiteHolderService websiteHolderService;

    @Resource
    private MonitorService monitorService;

    @Autowired
    private ProxyService proxyService;

    @Resource
    private WebsiteInfoService websiteInfoService;

    @Override
    public HttpResult<Object> init(CommonPluginParam param) {
        HttpResult<Object> result = null;
        try {
            Long taskId = param.getTaskId();
            String websiteName = param.getWebsiteName();
            String username = param.getUsername();
            String formType = param.getFormType();
            if (StringUtils.equals(FormType.LOGIN, formType)) {
                TaskUtils.addStep(param.getTaskId(), StepEnum.REC_INIT_MSG);
                // 清理共享信息
                RedisUtils.del(RedisKeyPrefixEnum.TASK_COOKIE.getRedisKey(taskId));
                RedisUtils.del(RedisKeyPrefixEnum.TASK_SHARE.getRedisKey(taskId));

                // 清理与任务绑定的代理
                proxyService.clear(taskId);

                try {
                    BackRedisUtils.del(RedisKeyPrefixEnum.TASK_REQUEST.getRedisKey(taskId));
                    BackRedisUtils.del(RedisKeyPrefixEnum.TASK_PAGE_CONTENT.getRedisKey(taskId));
                } catch (Throwable e) {
                    logger.error("delete task info from back redis error taskId={}", taskId, e);
                }
                RedisUtils.del(RedisKeyPrefixEnum.TASK_CONTEXT.getRedisKey(taskId));
                RedisUtils.del(RedisKeyPrefixEnum.TASK_WEBSITE.getRedisKey(taskId));

                // 这里电商,邮箱,老运营商
                Website website = websiteHolderService.getWebsite(websiteName);
                redisService.cache(RedisKeyPrefixEnum.TASK_WEBSITE, taskId, website);
                // 缓存task基本信息
                TaskUtils.initTaskShare(taskId, websiteName);
                if (StringUtils.isNotBlank(username)) {
                    TaskUtils.addTaskShare(taskId, AttributeKey.USERNAME, username);
                }
                if (null != param.getMobile()) {
                    TaskUtils.addTaskShare(taskId, AttributeKey.MOBILE, param.getMobile().toString());
                }
                TaskUtils.addTaskShare(taskId, AttributeKey.GROUP_CODE, website.getGroupCode());
                TaskUtils.addTaskShare(taskId, AttributeKey.GROUP_NAME, website.getGroupName());
                TaskUtils.addTaskShare(taskId, AttributeKey.WEBSITE_TITLE, website.getWebsiteTitle());
                TaskUtils.addTaskShare(taskId, AttributeKey.WEBSITE_TYPE, website.getWebsiteType());
                TaskUtils.addTaskShare(taskId, AttributeKey.WEBSITE_NAME, websiteName);

                // 设置代理
                ProxyUtils.setProxyEnable(taskId, param.isProxyEnable());
                ProxyUtils.queryIpLocale(taskId, param.getUserIp());

                // 记录登陆开始时间
                TaskUtils.addTaskShare(taskId, RedisKeyPrefixEnum.START_TIMESTAMP.getRedisKey(param.getFormType()), System.currentTimeMillis() + "");
                // 初始化监控信息
                monitorService.initTask(taskId, websiteName, username);

                if (null != param.getExtral() && !param.getExtral().isEmpty()) {
                    for (Map.Entry<String, Object> entry : param.getExtral().entrySet()) {
                        TaskUtils.addTaskShare(taskId, entry.getKey(), String.valueOf(entry.getValue()));
                    }
                }

                // 执行运营商插件初始化操作
                // 运营商独立部分第一次初始化后不启动爬虫
                result = classLoaderService.getCommonPluginService(param).init(param);
                // 爬虫状态
                if (!result.getStatus()) {
                    TaskUtils.addStep(taskId, StepEnum.INIT_FAIL);
                    monitorService.sendTaskLog(taskId, websiteName, TemplateUtils.format("{}-->初始化-->失败", param.getActionName()));
                    logger.warn("{}-->初始化-->失败", param.getActionName());
                    return result;
                }
                TaskUtils.addStep(taskId, StepEnum.INIT_SUCCESS);
                monitorService.sendTaskLog(taskId, websiteName, TemplateUtils.format("{}-->初始化-->成功", param.getActionName()));
                logger.info("{}-->初始化-->成功", param.getActionName());
                return result.success();
            }
            TaskUtils.addTaskShare(taskId, AttributeKey.WEBSITE_NAME, websiteName);
            result = classLoaderService.getCommonPluginService(param).init(param);
            if (!result.getStatus()) {
                monitorService.sendTaskLog(taskId, websiteName, TemplateUtils.format("{}-->初始化-->失败", param.getActionName()));
                logger.warn("{}-->初始化-->失败", param.getActionName());
                return result;
            }
            monitorService.sendTaskLog(taskId, websiteName, TemplateUtils.format("{}-->初始化-->成功", param.getActionName()));
            logger.info("{}-->初始化-->成功", param.getActionName());
            return result;
        } catch (Throwable e) {
            logger.error("init error,param={}", JSON.toJSONString(param), e);
            return new HttpResult<>().failure(ErrorCode.SYS_ERROR);
        }
    }

    @Override
    public HttpResult<Object> refeshPicCode(CommonPluginParam param) {
        try {
            return classLoaderService.getCommonPluginService(param).refeshPicCode(param);
        } catch (Throwable e) {
            logger.error("refeshPicCode error,param={}", JSON.toJSONString(param), e);
            return new HttpResult<>().failure(ErrorCode.SYS_ERROR);
        }
    }

    @Override
    public HttpResult<Object> refeshSmsCode(CommonPluginParam param) {
        try {
            WebsiteInfo websiteInfo = websiteInfoService.getByWebsiteName(param.getWebsiteName());
            // 刷新短信间隔时间
            int sendSmsInterval = websiteInfo.getSmsInterval();
            Long taskId = param.getTaskId();
            String latestSendSmsTime = TaskUtils.getTaskShare(taskId, AttributeKey.LATEST_SEND_SMS_TIME);
            if (StringUtils.isNoneBlank(latestSendSmsTime) && sendSmsInterval > 0) {
                long endTime = Long.parseLong(latestSendSmsTime) + TimeUnit.SECONDS.toMillis(sendSmsInterval);
                if (System.currentTimeMillis() < endTime) {
                    try {
                        logger.info("刷新短信有间隔时间限制,latestSendSmsTime={},将等待{}秒", DateUtils.formatYmdhms(Long.parseLong(latestSendSmsTime)),
                            DateUtils.getUsedTime(System.currentTimeMillis(), endTime));
                        TimeUnit.MILLISECONDS.sleep(endTime - System.currentTimeMillis());
                    } catch (InterruptedException e) {
                        throw new RuntimeException("refeshSmsCode error", e);
                    }
                }
            }
            HttpResult<Object> result = classLoaderService.getCommonPluginService(param).refeshSmsCode(param);
            if (result.getStatus()) {
                TaskUtils.addTaskShare(taskId, AttributeKey.LATEST_SEND_SMS_TIME, System.currentTimeMillis() + "");
            }
            String log = TemplateUtils.format("{}-->发送短信验证码-->{}", param.getActionName(), result.getStatus() ? "成功" : "失败");
            monitorService.sendTaskLog(taskId, param.getWebsiteName(), log, result);
            return result;
        } catch (Throwable e) {
            logger.error("refeshSmsCode error,param={}", JSON.toJSONString(param), e);
            return new HttpResult<>().failure(ErrorCode.SYS_ERROR);
        }
    }

    @Override
    public HttpResult<Object> submit(CommonPluginParam param) {
        long startTime = System.currentTimeMillis();
        HttpResult<Object> result = null;
        try {
            Long taskId = param.getTaskId();
            result = classLoaderService.getCommonPluginService(param).submit(param);
            if (result.isAsync()) {
                logger.info("login is async,taskId={},websiteName={}", param.getTaskId(), param.getWebsiteName());
                return result;
            }
            if (null != result && result.getStatus()) {
                if (StringUtils.equals(FormType.LOGIN, param.getFormType())) {
                    if (StringUtils.isNotBlank(param.getUsername())) {
                        TaskUtils.addTaskShare(taskId, AttributeKey.USERNAME, param.getUsername());
                    }
                    if (null != param.getMobile()) {
                        TaskUtils.addTaskShare(taskId, AttributeKey.MOBILE, param.getMobile().toString());
                    }
                    // 登录成功
                    if (StringUtils.isNoneBlank(param.getPassword())) {
                        TaskUtils.addTaskShare(taskId, AttributeKey.PASSWORD, param.getPassword());
                    }
                    if (StringUtils.isNoneBlank(param.getIdCard())) {
                        TaskUtils.addTaskShare(taskId, AttributeKey.ID_CARD, param.getIdCard());
                    }
                    if (StringUtils.isNoneBlank(param.getRealName())) {
                        TaskUtils.addTaskShare(taskId, AttributeKey.REAL_NAME, param.getRealName());
                    }
                } else if (StringUtils.isNoneBlank(param.getSmsCode())) {
                    TaskUtils.addTaskShare(taskId, RedisKeyPrefixEnum.TASK_SMS_CODE.getRedisKey(param.getFormType()), param.getSmsCode());
                }
            }
            if (!result.getStatus() && StringUtils.equals(FormType.LOGIN, param.getFormType())) {
                BeanFactoryUtils.getBean(MessageService.class).sendTaskLog(taskId, "登陆失败");
            }
            TaskUtils.addTaskShare(taskId, RedisKeyPrefixEnum.FINISH_TIMESTAMP.getRedisKey(param.getFormType()), System.currentTimeMillis() + "");
            TaskUtils.addTaskShare(taskId, RedisKeyPrefixEnum.SUBMIT_RESULT.getRedisKey(param.getFormType()), JSON.toJSONString(result));
            // 腾讯企业邮箱h5和新浪邮箱h5时不再发一次mq，防止task_log中记录错误的数据
            if (result.getData() != null) {
                String str = result.getData().toString();
                if ((str.contains("directive=login_fail") || str.contains("directive=require_picture") || str.contains("directive=require_picture_again"))
                    && str.contains("information")) {
                    return result;
                }
            }
            String log = TemplateUtils.format("{}-->校验-->{}", param.getActionName(), result.getStatus() ? "成功" : "失败");
            monitorService.sendTaskLog(taskId, param.getWebsiteName(), log, result);
            return result;
        } catch (Throwable e) {
            logger.error("submit error,param={}", JSON.toJSONString(param), e);
            return new HttpResult<>().failure(ErrorCode.SYS_ERROR);
        } finally {
            monitorService.sendMethodUseTime(param.getTaskId(), param.getWebsiteName(), param.getFormType(), this.getClass().getName(), "submit", Arrays.asList(param), result,
                startTime, System.currentTimeMillis());
        }
    }

    @Override
    public HttpResult<Object> validatePicCode(CommonPluginParam param) {
        try {
            return classLoaderService.getCommonPluginService(param).validatePicCode(param);
        } catch (Throwable e) {
            logger.error("validatePicCode error,param={}", JSON.toJSONString(param), e);
            return new HttpResult<>().failure(ErrorCode.SYS_ERROR);
        }
    }

    @Override
    public HttpResult<Object> defineProcess(CommonPluginParam param) {
        try {
            return classLoaderService.getCommonPluginService(param).defineProcess(param);
        } catch (Throwable e) {
            logger.error("defineProcess error,param={}", JSON.toJSONString(param), e);
            return new HttpResult<>().failure(ErrorCode.SYS_ERROR);
        }
    }

    @Override
    public void sendLoginSuccessMsg(String topic, LoginMessage loginMessage) {
        sendLoginSuccessMsg(topic, loginMessage, null);
    }

    @Override
    public void sendLoginSuccessMsg(String topic, LoginMessage loginMessage, List<Cookie> cookies) {
        Map<String, Object> map = new HashMap<>();
        map.put(AttributeKey.END_URL, loginMessage.getEndUrl());
        map.put(AttributeKey.TASK_ID, loginMessage.getTaskId());
        map.put(AttributeKey.WEBSITE_NAME, loginMessage.getWebsiteName());
        map.put(AttributeKey.ACCOUNT_NO, loginMessage.getAccountNo());
        map.put(AttributeKey.COOKIE, loginMessage.getCookie());
        if (null != loginMessage.getGroupCode()) {
            map.put(AttributeKey.GROUP_CODE, loginMessage.getGroupCode());
        }
        if (StringUtils.isNotBlank(loginMessage.getGroupName())) {
            map.put(AttributeKey.GROUP_NAME, loginMessage.getGroupName());
        }
        if (null != cookies && !cookies.isEmpty()) {
            TaskUtils.saveCookie(loginMessage.getTaskId(), cookies);
            String cookieString = TaskUtils.getCookieString(cookies);
            map.put(AttributeKey.COOKIE, cookieString);
        }
        BeanFactoryUtils.getBean(MessageService.class).sendMessage(topic, TopicTag.LOGIN_INFO.getTag(), map);
    }

    @Override
    public HttpResult<Object> refeshQRCode(CommonPluginParam param) {
        try {
            return ((QRPlugin)(classLoaderService.getCommonPluginService(param))).refeshQRCode(param);
        } catch (Throwable e) {
            logger.error("refeshQRCode error,param={}", JSON.toJSONString(param), e);
            return new HttpResult<>().failure(ErrorCode.SYS_ERROR);
        }
    }

    @Override
    public HttpResult<Object> queryQRStatus(CommonPluginParam param) {
        try {
            return ((QRPlugin)(classLoaderService.getCommonPluginService(param))).queryQRStatus(param);
        } catch (Throwable e) {
            logger.error("queryQRStatus error,param={}", JSON.toJSONString(param), e);
            return new HttpResult<>().failure(ErrorCode.SYS_ERROR);
        }
    }

}
