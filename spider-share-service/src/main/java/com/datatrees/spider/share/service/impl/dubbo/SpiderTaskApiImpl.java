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

package com.datatrees.spider.share.service.impl.dubbo;

import com.alibaba.fastjson.JSON;
import com.datatrees.common.zookeeper.ZooKeeperClient;
import com.datatrees.spider.share.api.SpiderTaskApi;
import com.datatrees.spider.share.common.share.service.ProxyService;
import com.datatrees.spider.share.common.share.service.RedisService;
import com.datatrees.spider.share.common.utils.ProcessResultUtils;
import com.datatrees.spider.share.common.utils.RedisUtils;
import com.datatrees.spider.share.common.utils.TaskUtils;
import com.datatrees.spider.share.domain.AttributeKey;
import com.datatrees.spider.share.domain.ErrorCode;
import com.datatrees.spider.share.domain.ProcessResult;
import com.datatrees.spider.share.domain.ProcessStatus;
import com.datatrees.spider.share.domain.QRStatus;
import com.datatrees.spider.share.domain.RedisKeyPrefixEnum;
import com.datatrees.spider.share.domain.directive.DirectiveRedisCode;
import com.datatrees.spider.share.domain.directive.DirectiveResult;
import com.datatrees.spider.share.domain.directive.DirectiveType;
import com.datatrees.spider.share.domain.http.HttpResult;
import com.datatrees.spider.share.domain.model.Task;
import com.datatrees.spider.share.domain.model.WebsiteConf;
import com.datatrees.spider.share.service.MonitorService;
import com.datatrees.spider.share.service.TaskService;
import com.datatrees.spider.share.service.WebsiteConfigService;
import com.datatrees.spider.share.service.extra.ActorLockEventWatcher;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class SpiderTaskApiImpl implements SpiderTaskApi {

    private static final Logger logger = LoggerFactory.getLogger(SpiderTaskApiImpl.class);

    @Resource
    private TaskService taskService;

    @Resource
    private RedisService redisService;

    @Resource
    private ProxyService proxyService;

    @Resource
    private ZooKeeperClient zooKeeperClient;

    @Resource
    private MonitorService monitorService;

    @Resource
    private WebsiteConfigService websiteConfigService;

    @Override
    public Task getByTaskId(Long taskId) {
        return taskService.getByTaskId(taskId);
    }

    @Override
    public Map<String, String> getTaskBaseInfo(Long taskId) {
        return getTaskBaseInfo(taskId, null);
    }

    @Override
    public Map<String, String> getTaskBaseInfo(Long taskId, String websiteName) {
        Map<String, String> map = TaskUtils.getTaskShares(taskId);
        map.put(AttributeKey.TASK_ID, String.valueOf(taskId));
        map.put(AttributeKey.TIMESTAMP, String.valueOf(System.currentTimeMillis()));
        return map;
    }

    @Override
    public String getTaskAccountNo(Long taskId) {
        String redisKey = RedisKeyPrefixEnum.TASK_INFO_ACCOUNT_NO.getRedisKey(taskId);
        String accountNo = RedisUtils.get(redisKey);
        return accountNo;
    }

    @Override
    public HttpResult<Boolean> importCrawlCode(String directiveId, long taskId, int type, String code, Map<String, String> extra) {
        HttpResult<Boolean> result = new HttpResult<>();
        try {
            if (null == extra) {
                extra = new HashMap<>();
            }
            if (taskId <= 0 || type < 0 || StringUtils.isAnyBlank(directiveId, code)) {
                logger.warn("invalid param taskId={},type={},directiveId={},code={},extra={}", taskId, type, directiveId, code, JSON.toJSONString(extra));
                return result.failure("参数为空或者参数不完整");
            }
            String status = DirectiveRedisCode.WAIT_SERVER_PROCESS;
            String directiveType = null;
            switch (type) {
                case 0:
                    directiveType = DirectiveType.CRAWL_SMS;
                    break;
                case 1:
                    directiveType = DirectiveType.CRAWL_CODE;
                    break;
                case 3:
                    directiveType = DirectiveType.LOGIN_SECOND_PASSWORD;
                    Long processId = Long.parseLong(extra.get("processId"));
                    ProcessResult<Object> processResult = ProcessResultUtils.queryProcessResult(processId);
                    if (StringUtils.equals(processResult.getProcessStatus(), ProcessStatus.REQUIRE_SECOND_PASSWORD)) {
                        processResult.setProcessStatus(ProcessStatus.PROCESSING);
                        ProcessResultUtils.saveProcessResult(processResult);
                        TaskUtils.addTaskShare(taskId, AttributeKey.QR_STATUS, QRStatus.WAITING);
                    }
                    break;
                default:
                    logger.warn("invalid param taskId={},type={}", taskId, type);
                    return result.failure("未知参数type");
            }

            extra.put(AttributeKey.CODE, code);
            DirectiveResult<Map<String, String>> sendDirective = new DirectiveResult<>(directiveType, taskId);
            // 保存交互指令到redis
            sendDirective.fill(status, extra);
            redisService.saveDirectiveResult(directiveId, sendDirective);
            logger.info("import success taskId={},directiveId={},code={},extra={}", taskId, directiveId, code, JSON.toJSONString(extra));
            return result.success(true);
        } catch (Exception e) {
            logger.error("import error taskId={},directiveId={},code={},extra={}", taskId, directiveId, code, JSON.toJSONString(extra), e);
            return result.failure();
        }
    }

    @Override
    public HttpResult<Boolean> cancel(long taskId, Map<String, String> extra) {
        HttpResult<Boolean> result = new HttpResult<Boolean>();
        String websiteName = TaskUtils.getTaskShare(taskId, AttributeKey.WEBSITE_NAME);
        if (StringUtils.equals(websiteName, "alipay.com") || StringUtils.equals(websiteName, "taobao.com") || StringUtils.equals(websiteName, "taobao.com.h5")) {
            logger.info("电商拒绝取消,哈哈.......taskId={},websiteName={}", taskId, websiteName);
            return result.success();
        }

        DirectiveResult<Map<String, String>> sendDirective = new DirectiveResult<>(DirectiveType.PLUGIN_LOGIN, taskId);
        String directiveId = redisService.createDirectiveId();
        sendDirective.setDirectiveId(directiveId);
        Map<String, String> directiveData = new HashMap<>();
        sendDirective.fill(DirectiveRedisCode.CANCEL, directiveData);
        redisService.saveDirectiveResult(sendDirective);

        // 清理与任务绑定的代理
        proxyService.clear(taskId);

        ActorLockEventWatcher watcher = new ActorLockEventWatcher("CollectorActor", taskId + "", null, zooKeeperClient);
        logger.info("cancel taskId={}", taskId);
        result.setData(false);
        if (watcher.cancel()) {
            logger.info("cancel task success,taskId={}", taskId);
            result.setData(true);
            result.success();
        }
        String reason = null;
        if (null != extra && extra.containsKey("reason")) {
            reason = extra.get("reason");
        }
        ErrorCode errorCode = ErrorCode.TASK_CANCEL;
        if (StringUtils.equals("timeout", reason)) {
            errorCode = ErrorCode.TASK_CANCEL_BY_SYSTEM;
        } else if (StringUtils.equals("user", reason)) {
            errorCode = ErrorCode.TASK_CANCEL_BY_USER;
        }
        monitorService.sendTaskCompleteMsg(taskId, null, errorCode.getErrorCode(), errorCode.getErrorMsg());
        return result.failure();
    }

    @Override
    public ProcessResult queryProcessResult(long processId) {
        return ProcessResultUtils.queryProcessResult(processId);
    }

    @Override
    public HttpResult<String> verifyQr(String directiveId, long taskId, Map<String, String> extra) {
        HttpResult<String> result = new HttpResult<>();
        try {
            if (taskId <= 0 || StringUtils.isBlank(directiveId)) {
                logger.warn("verifyQr invalid param taskId={},directiveId={}", taskId, directiveId);
                return result.success(DirectiveRedisCode.FAILED);
            }
            DirectiveResult<String> directiveResult = redisService.getDirectiveResult(directiveId, 2, TimeUnit.SECONDS);
            if (null == directiveResult) {
                logger.warn("verifyQr timeout taskId={},directiveId={}", taskId, directiveId);
                return result.success(DirectiveRedisCode.FAILED);
            }
            TimeUnit.MILLISECONDS.sleep(500);// 不能让前端一直轮询
            logger.info("verifyQr result taskId={},directiveId={},qrStatus={}", taskId, directiveId, directiveResult.getStatus());
            return result.success(directiveResult.getStatus());
        } catch (Exception e) {
            logger.error("verifyQr error taskId={},directiveId={}", taskId, directiveId);
            return result.success(DirectiveRedisCode.FAILED);
        }
    }

    @Override
    public List<WebsiteConf> getWebsiteConf(List<String> websiteNameList) {
        return websiteConfigService.getWebsiteConf(websiteNameList);
    }

}
