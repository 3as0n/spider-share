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

package com.datatrees.spider.share.service.collector.actor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.common.util.GsonUtils;
import com.datatrees.common.util.PatternUtils;
import com.datatrees.common.util.ThreadInterruptedUtil;
import com.datatrees.common.zookeeper.ZooKeeperClient;
import com.datatrees.common.zookeeper.watcher.AbstractLockerWatcher;
import com.datatrees.spider.share.common.utils.IpUtils;
import com.datatrees.spider.share.common.utils.RedisUtils;
import com.datatrees.spider.share.common.utils.TaskUtils;
import com.datatrees.spider.share.common.utils.WebsiteUtils;
import com.datatrees.spider.share.domain.AttributeKey;
import com.datatrees.spider.share.domain.CollectorMessage;
import com.datatrees.spider.share.domain.ErrorCode;
import com.datatrees.spider.share.domain.RedisKeyPrefixEnum;
import com.datatrees.spider.share.domain.ResultMessage;
import com.datatrees.spider.share.domain.directive.DirectiveEnum;
import com.datatrees.spider.share.domain.exception.LoginTimeOutException;
import com.datatrees.spider.share.domain.http.HttpResult;
import com.datatrees.spider.share.domain.model.Task;
import com.datatrees.spider.share.service.MessageService;
import com.datatrees.spider.share.service.MonitorService;
import com.datatrees.spider.share.service.TaskService;
import com.datatrees.spider.share.service.WebsiteConfigService;
import com.datatrees.spider.share.service.collector.worker.CollectorWorker;
import com.datatrees.spider.share.service.collector.worker.CollectorWorkerFactory;
import com.datatrees.spider.share.service.constants.SubmitConstant;
import com.datatrees.spider.share.service.dao.RedisDao;
import com.datatrees.spider.share.service.domain.SubmitFile;
import com.datatrees.spider.share.service.extra.ActorLockEventWatcher;
import com.datatrees.spider.share.service.message.MessageFactory;
import com.datatrees.spider.share.service.oss.OssServiceProvider;
import com.datatrees.spider.share.service.oss.OssUtils;
import com.datatrees.spider.share.service.util.RedisKeyUtils;
import com.datatrees.spider.share.service.util.UnifiedSysTime;
import com.datatrees.spider.share.service.util.ZipCompressUtils;
import com.datatrees.spider.share.service.util.operator.OperatorUtils;
import com.treefinance.crawler.framework.config.enums.LoginType;
import com.treefinance.crawler.framework.context.ProcessorContextUtil;
import com.treefinance.crawler.framework.context.ProcessorResult;
import com.treefinance.crawler.framework.context.SearchProcessorContext;
import com.treefinance.toolkit.util.Preconditions;
import com.treefinance.toolkit.util.json.Jackson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.annotation.Resource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since 2015年12月22日 下午7:35:45
 */
@Service
public class Collector {

    private static final Logger logger = LoggerFactory.getLogger(Collector.class);

    private static final int START_MSG_LENGTH_LIMIT = PropertiesConfiguration.getInstance().getInt("default.startMsgJson.length.threshold", 20000);

    private static final String duplicateRemovedResultKeys = PropertiesConfiguration.getInstance().get("duplicate.removed.result.keys", "bankbill");

    private static final String mqStatusTags = PropertiesConfiguration.getInstance().get("core.mq.status.tags", "bankbill,ecommerce,operator");

    private static final String mqMessageSendTagPattern = PropertiesConfiguration.getInstance().get("core.mq.message.sendTag.pattern", "opinionDetect|webDetect|businessLicense");

    @Resource
    private WebsiteConfigService websiteConfigService;

    @Resource
    private TaskService taskService;

    @Resource
    private MessageFactory messageFactory;

    @Resource
    private DefaultMQProducer defaultMQProducer;

    @Resource
    private ZooKeeperClient zookeeperClient;

    @Resource
    private CollectorWorkerFactory collectorWorkerFactory;

    @Resource
    private RedisDao redisDao;

    @Resource
    private MessageService messageService;

    @Resource
    private MonitorService monitorService;

    public ProcessorResult processMessage(CollectorMessage message) {
        // 初始化,生成上下文,保存task
        TaskMessage taskMessage = this.initTask(message);
        Task task = taskMessage.getTask();

        // zookeeper做去重处理,后来的线程活着,ThreadInterruptedUtil.isInterrupted(Thread.currentThread())手动判断,并停止
        AbstractLockerWatcher watcher = null;
        try {
            watcher = this.actorLockWatchInit(taskMessage);

            CollectorWorker collectorWorker = collectorWorkerFactory.getCollectorWorker(taskMessage);

            SearchProcessorContext context = taskMessage.getContext();
            boolean needLogin = context.needLogin();
            LoginType loginType = context.getLoginConfig() != null ? context.getLoginConfig().getType() : null;
            logger.info("start process taskId={},needLogin={},loginType={},websiteName={}", taskMessage.getTaskId(), needLogin, loginType, context.getWebsiteName());

            HttpResult<Boolean> loginResult = new HttpResult<>();
            if (needLogin) {
                loginResult = collectorWorker.doLogin(taskMessage);
                if (!loginResult.getStatus()) {
                    taskMessage.failure(loginResult.getResponseCode(), loginResult.getMessage());
                }
            }

            boolean loginStatus = !needLogin || loginResult.getStatus();
            if (taskMessage.isMainTask() && !context.needInteractive()) {
                messageService.sendTaskLog(taskMessage.getTaskId(), loginStatus ? "登陆成功" : "登陆失败");
            }

            if (loginStatus) {
                if (taskMessage.isMainTask()) {
                    messageService.sendTaskLog(taskMessage.getTaskId(), "开始抓取");
                }
                HttpResult<Map<String, Object>> searchResult = collectorWorker.doSearch(taskMessage);
                if (!searchResult.getStatus()) {
                    taskMessage.failure(searchResult.getResponseCode(), searchResult.getMessage());
                } else if (ThreadInterruptedUtil.isInterrupted(Thread.currentThread())) {
                    logger.warn("Thread interrupt before result send to queue. threadId={},taskId={},websiteName={}", Thread.currentThread().getId(), taskMessage.getTaskId(),
                        taskMessage.getWebsiteName());
                    taskMessage.failure(ErrorCode.TASK_INTERRUPTED_ERROR);
                } else {
                    Map submitkeyResult = searchResult.getData();

                    if (taskMessage.isSubTask() && MapUtils.isEmpty(submitkeyResult)) {
                        logger.info("skip send mq message threadId={},taskId={},websiteName={},parent={}", Thread.currentThread().getId(), taskMessage.getTaskId(),
                            taskMessage.getWebsiteName(), taskMessage.getParentTaskId());
                    } else {
                        Set<String> resultTagSet = collectorWorker.getResultTagSet();
                        if (CollectionUtils.isEmpty(resultTagSet)) {
                            resultTagSet = taskMessage.getResultTagSet();
                        }

                        this.sendResult(taskMessage, submitkeyResult, resultTagSet);
                    }
                }
            }
        } catch (Throwable e) {
            logger.error("processMessage error taskId={}", message.getTaskId(), e);
            if (e instanceof LoginTimeOutException) {
                taskMessage.failure(ErrorCode.LOGIN_TIMEOUT_ERROR, ErrorCode.LOGIN_TIMEOUT_ERROR.getErrorMsg() + " " + e.getMessage());
            } else if (ThreadInterruptedUtil.isInterrupted(Thread.currentThread())) {
                taskMessage.failure(ErrorCode.TASK_INTERRUPTED_ERROR);
            } else {
                taskMessage.failure(ErrorCode.UNKNOWN_REASON, e.toString());
            }
        } finally {
            if (null != watcher) {
                this.actorLockWatchRelease(watcher);
            }
            if (taskMessage.isMainTask()) {
                String logMsg;
                switch (taskMessage.getTask().getStatus()) {
                    case 0:
                        logMsg = "抓取成功";
                        break;
                    case 306:
                        logMsg = "抓取中断";
                        break;
                    case 308:
                        logMsg = "登陆超时";
                        break;
                    default:
                        logMsg = "抓取失败";
                        break;
                }
                messageService.sendTaskLog(task.getTaskId(), logMsg, task.getRemark());
                if (task.getStatus() != 0) {
                    String newRemark = null;
                    try {
                        if (WebsiteUtils.isOperator(task.getWebsiteName())) {
                            newRemark = OperatorUtils.getRemarkForTaskFail(task.getTaskId());
                        }
                    } catch (Exception e) {
                        logger.error("更新remark失败，taskId={}", task.getTaskId(), e);
                    }
                    messageService.sendDirective(task.getTaskId(), DirectiveEnum.TASK_FAIL.getCode(), StringUtils.defaultString(newRemark));
                }
            }
            logger.info("task complete pid={}, taskId={},isSubTask={},status={},remark={},websiteName={}", task.getId(), task.getTaskId(), task.isSubTask(), task.getStatus(),
                task.getRemark(), task.getWebsiteName());
        }
        message.setFinish(true);
        this.messageComplement(taskMessage, message);
        taskService.updateTask(task);
        if (taskMessage.isMainTask()) {
            TaskUtils.addTaskShare(task.getTaskId(), RedisKeyPrefixEnum.FINISH_TIMESTAMP.getRedisKey(AttributeKey.CRAWLER), System.currentTimeMillis() + "");
            monitorService.sendTaskCompleteMsg(task.getTaskId(), task.getWebsiteName(), task.getStatus(), task.getRemark());
        }
        return taskMessage.getContext().getProcessorResult();
    }

    @Nonnull
    private TaskMessage initTask(CollectorMessage message) {
        Preconditions.notNull("taskId", message.getTaskId());
        this.clearStatus(message.getTaskId());

        SearchProcessorContext context = createSearchProcessContext(message);

        Task task = makeTask(context);

        // set task unique sign
        ProcessorContextUtil.setTaskUnique(context, task.getId());

        TaskMessage taskMessage = new TaskMessage(task, context);
        taskMessage.setCollectorMessage(message);

        return taskMessage;
    }

    private Task makeTask(SearchProcessorContext context) {
        Task task = new Task();
        task.setTaskId(context.getTaskId());
        task.setWebsiteId(context.getWebsiteId());
        task.setWebsiteName(context.getWebsiteName());
        task.setNodeName(IpUtils.getLocalHostName());
        task.setOpenUrlCount(new AtomicInteger(0));
        task.setOpenPageCount(new AtomicInteger(0));
        task.setRequestFailedCount(new AtomicInteger(0));
        task.setRetryCount(new AtomicInteger(0));
        task.setFilteredCount(new AtomicInteger(0));
        task.setNetworkTraffic(new AtomicLong(0));
        task.setStatus(0);
        task.setStartedAt(UnifiedSysTime.INSTANCE.getSystemTime());
        task.setCreatedAt(UnifiedSysTime.INSTANCE.getSystemTime());
        taskService.insertTask(task);

        logger.info("create new task. id: {}, taskId: {}", task.getId(), task.getTaskId());

        return task;
    }

    /**
     * 初始化爬虫搜索过程的上下文环境。
     */
    private SearchProcessorContext createSearchProcessContext(CollectorMessage message) {
        SearchProcessorContext context = websiteConfigService.getSearchProcessorContext(message.getTaskId(), message.getWebsiteName());
        context.setLoginCheckIgnore(message.isLoginCheckIgnore());
        context.setAttribute(AttributeKey.TASK_ID, message.getTaskId());
        context.setAttribute(AttributeKey.ACCOUNT_KEY, message.getTaskId() + "");
        // context.set(AttributeKey.ACCOUNT_NO, message.getAccountNo());
        // init cookie
        context.setCookies(message.getCookie());
        context.setAttribute(AttributeKey.END_URL, message.getEndURL());

        Map<String, String> shares = TaskUtils.getTaskShares(message.getTaskId());
        if (null != shares && !shares.isEmpty()) {
            logger.info("Add shared fields into context attributes: {}", Jackson.toJSONString(shares));
            shares.forEach(context::setAttribute);
        }

        context.addAttributes(message.getProperty());
        // set status level 1 status
        context.addStatusAttr(ResultMessage.LEVAL_1_STATUS, message.isLevel1Status());

        return context;
    }

    /**
     * 历史状态清理
     */
    private void clearStatus(Long taskId) {
        // todo 这里会有问题,待处理
        String key = "verify_result_" + taskId;
        String getKey = "plugin_remark_" + taskId;
        redisDao.deleteKey(key);
        redisDao.deleteKey(getKey);
        logger.info("do verify_result and plugin_remark  data clear for taskId: {}", taskId);
    }

    private AbstractLockerWatcher actorLockWatchInit(TaskMessage taskMessage) {
        String templateId = taskMessage.getTemplateId();
        String uniqueSuffix = taskMessage.getUniqueSuffix();
        String serialNum = taskMessage.getCollectorMessage().getSerialNum();
        Long taskId = taskMessage.getTaskId();
        String path = Long.toString(taskId);
        path = StringUtils.isNotBlank(templateId) ? path + "_" + templateId : path;
        path = StringUtils.isNotBlank(uniqueSuffix) ? path + "_" + uniqueSuffix : path;
        path = StringUtils.isNotBlank(serialNum) ? path + "_" + serialNum : path;
        AbstractLockerWatcher watcher = new ActorLockEventWatcher("CollectorActor", path, Thread.currentThread(), zookeeperClient);
        zookeeperClient.registerWatcher(watcher);
        if (watcher.init()) {
            logger.info("Get actorLock begin to start ...");
        } else {
            logger.info("Lost actorLock,waiting for interrupt...");
        }
        return watcher;
    }

    private void actorLockWatchRelease(AbstractLockerWatcher watcher) {
        if (watcher != null) {
            zookeeperClient.unregisterWatcher(watcher);
            ThreadInterruptedUtil.clearInterrupted(Thread.currentThread());
            watcher.unLock();
        } else {
            logger.warn("watcher is empty ...");
        }
    }

    private void messageComplement(TaskMessage taskMessage, CollectorMessage message) {
        try {
            if (StringUtils.isBlank(taskMessage.getTask().getResultMessage())) {
                taskMessage.getTask().setResultMessage(GsonUtils.toJson(taskMessage.getContext().getProcessorResult()));
            }
            // reset message result cookie
            message.setCookie(taskMessage.getContext().getCookiesAsString());
            Map<String, Object> map = new HashMap<>();
            map.put("resultMsg", RedisUtils.hgetAll(RedisKeyPrefixEnum.TASK_RESULT.getRedisKey(taskMessage.getTaskId())));
            map.put("startMsg", message);

            String startMsgJson = GsonUtils.toJson(message);

            if (startMsgJson.length() > START_MSG_LENGTH_LIMIT) {
                try {
                    String path = "task/" + taskMessage.getTask().getTaskId() + "/" + taskMessage.getTask().getWebsiteId() + "/" + taskMessage.getTask().getId();

                    SubmitFile file = new SubmitFile("startMsg.json", startMsgJson.getBytes());
                    Map<String, SubmitFile> uploadMap = new HashMap<>();
                    uploadMap.put(file.getFileName(), file);
                    byte[] data = ZipCompressUtils.compress(uploadMap);
                    OssServiceProvider.getDefaultService().putObject(SubmitConstant.ALIYUN_OSS_DEFAULTBUCKET, OssUtils.getObjectKey(path), data);

                    map.put("startMsgOSSPath", path);
                } catch (Exception e) {
                    logger.warn("Error uploading startMsg.json", e);
                }
            }
            taskMessage.getTask().setResultMessage(JSON.toJSONString(map, SerializerFeature.WriteDateUseDateFormat));
        } catch (Exception e) {
            logger.error("messageComplement error: {}", e.getMessage(), e);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void sendResult(TaskMessage taskMessage, Map submitkeyResult, Set<String> resultTagSet) {
        logger.info("Starting send result. submit-key: {}, result-tag: {}", submitkeyResult, resultTagSet);
        Task task = taskMessage.getTask();
        ResultMessage resultMessage = new ResultMessage();
        resultMessage.setRemark(GsonUtils.toJson(task));
        resultMessage.setTaskId(task.getTaskId());
        resultMessage.setWebsiteName(taskMessage.getWebsiteName());
        resultMessage.setWebsiteType(taskMessage.getWebsiteType().getType());
        Set<String> notEmptyTag = new HashSet<String>();
        if (submitkeyResult != null) {
            resultMessage.setStatus("SUCCESS");
        } else {
            resultMessage.setStatus("FAIL");
        }

        boolean needSendToMQ = false;
        // build result message
        ProcessorResult<String, Object> result = taskMessage.getContext().getProcessorResult();
        // the same rule as redis key ,init all the possible key
        for (String tag : resultTagSet) {
            if (submitkeyResult != null || PatternUtils.match(mqMessageSendTagPattern, tag)) {
                needSendToMQ = true;
            }
            String keys = PropertiesConfiguration.getInstance().get("core.mq.tag." + tag + ".keys");
            if (StringUtils.isNotEmpty(keys)) {
                for (String key : keys.split(",")) {
                    result.put(key, RedisKeyUtils.genRedisKey(task.getTaskId(), task.getId(), key));
                }
            }
        }
        if (needSendToMQ) {
            if (submitkeyResult != null)
                result.putAll(submitkeyResult);
            result.putAll(taskMessage.getCollectorMessage().getSendBack());
            result.put("taskId", task.getTaskId());
            result.put("websiteName", resultMessage.getWebsiteName());
            result.put("websiteType", resultMessage.getWebsiteType());
            result.put("status", resultMessage.getStatus());
            result.put("statusCode", task.getStatus());

            if (taskMessage.getMessageSend()) {
                List<Message> mqMessages = messageFactory.getMessage(result, "" + taskMessage.getTask().getId());
                for (Message mqMessage : mqMessages) {
                    try {
                        notEmptyTag.add(mqMessage.getTags());
                        SendResult sendResult = defaultMQProducer.send(mqMessage);
                        logger.info("send message: {}, result: {}", mqMessage, sendResult);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        task.setErrorCode(ErrorCode.RESULT_SEND_ERROR);
                    }
                }
            } else {
                logger.warn("{} no need to submit result: {}", taskMessage, submitkeyResult);
            }
            task.setResultMessage(GsonUtils.toJson(result));
        }

        this.sendTaskStatus(taskMessage, resultMessage, resultTagSet, notEmptyTag);
    }

    private void sendTaskStatus(TaskMessage taskMessage, ResultMessage resultMessage, Set<String> resultTagSet, Set<String> notEmptyTag) {
        if (taskMessage.getMessageSend() && taskMessage.getStatusSend()) {
            Task task = taskMessage.getTask();
            for (String key : resultTagSet) {
                if (mqStatusTags.contains(key)) {
                    ResultMessage keyResult = new ResultMessage();
                    keyResult.putAll(resultMessage);
                    if (duplicateRemovedResultKeys.contains(key)) {
                        keyResult.setResultEmpty(!task.isDuplicateRemoved() && !notEmptyTag.contains(key));
                    } else {
                        keyResult.setResultEmpty(!notEmptyTag.contains(key));
                    }
                    try {
                        Message mqMessage = messageFactory.getMessage("rawData_result_status", key, GsonUtils.toJson(keyResult), "" + taskMessage.getTask().getId());
                        SendResult sendResult = defaultMQProducer.send(mqMessage);
                        logger.info("send result message: {}, result: {}", mqMessage, sendResult);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        task.setErrorCode(ErrorCode.RESULT_SEND_ERROR);
                    }
                } else {
                    logger.warn("{} no need to send status key: {}, resultMessage: {}", taskMessage, key, resultMessage);
                }
            }
        } else {
            logger.warn("{} no need to send status: {}, resultMessage: {}", taskMessage, resultTagSet, resultMessage);
        }
    }

}
