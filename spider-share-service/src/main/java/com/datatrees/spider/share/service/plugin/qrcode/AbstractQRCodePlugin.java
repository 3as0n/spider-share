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

package com.datatrees.spider.share.service.plugin.qrcode;

import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.common.util.GsonUtils;
import com.datatrees.spider.share.common.utils.BeanFactoryUtils;
import com.datatrees.spider.share.domain.AttributeKey;
import com.datatrees.spider.share.domain.directive.DirectiveEnum;
import com.datatrees.spider.share.domain.directive.DirectiveRedisCode;
import com.datatrees.spider.share.domain.directive.DirectiveResult;
import com.datatrees.spider.share.domain.directive.DirectiveType;
import com.datatrees.spider.share.service.MonitorService;
import com.datatrees.spider.share.service.plugin.AbstractRawdataPlugin;
import com.google.gson.reflect.TypeToken;
import com.treefinance.crawler.framework.context.AbstractProcessorContext;
import com.treefinance.crawler.framework.extension.plugin.PluginConstants;
import com.treefinance.crawler.framework.extension.plugin.PluginFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class AbstractQRCodePlugin extends AbstractRawdataPlugin implements QRCodeVerification {

    int verifyQRCodeCount = 0;

    private Logger logger = LoggerFactory.getLogger(AbstractQRCodePlugin.class);

    private MonitorService monitorService = BeanFactoryUtils.getBean(MonitorService.class);

    @Override
    public String process(String... args) throws Exception {
        Map<String, String> paramMap = (LinkedHashMap<String, String>)GsonUtils.fromJson(args[0], new TypeToken<LinkedHashMap<String, String>>() {}.getType());
        return GsonUtils.toJson(doProcess(paramMap));
    }

    public Map<String, String> doProcess(Map<String, String> paramMap) throws Exception {
        AbstractProcessorContext context = PluginFactory.getProcessorContext();
        String websiteName = context.getWebsiteName();
        Long taskId = context.getLong(AttributeKey.TASK_ID);
        logger.info("start run qrcode plugin!taskId={},websiteName={}", taskId, websiteName);
        Map<String, String> resultMap = new LinkedHashMap<String, String>();
        Map paramsMap = perpareParam(paramMap);

        // Map<String, Object> redisMap = new HashMap<String, Object>();
        if (null == taskId || StringUtils.isBlank(websiteName)) {
            logger.error("invalid params taskId or websitename is empty! taskId={},websiteName={}", taskId, websiteName);
            resultMap.put(AttributeKey.ERROR_CODE, "-1");
            resultMap.put(AttributeKey.ERROR_MESSAGE, "userId or websitename is empty");
            return resultMap;
        }
        if (!this.preQRCode(paramsMap)) {
            logger.warn("no need to do qr plugin .taskId={},websiteName={}", taskId, websiteName);
            resultMap.putAll(paramMap);
            return resultMap;
        }
        // 二维码不需要刷新,暂时不需要重试
        // 最大重试次数
        int maxRetry = 0;
        // 当前重试次数
        int retry = 0;
        monitorService.sendTaskLog(taskId, "电商-->校验二维码启动-->成功");
        getMessageService().sendTaskLog(taskId, "等待用户扫描二维码");
        // 保存交互指令到redis,让APP端轮询进入等待模式
        DirectiveResult<String> sendDirective = new DirectiveResult<>(DirectiveType.CRAWL_QR, taskId);
        sendDirective.fill(DirectiveRedisCode.WAITTING, null);
        String directiveId = null;
        do {
            String qrcodeResult = refreshQRCode(paramsMap);
            logger.info("do refresh qrcode  retry={},taskId={},websiteName={}", retry, taskId, websiteName);
            if (StringUtils.isEmpty(qrcodeResult)) {
                monitorService.sendTaskLog(taskId, "电商-->刷新二维码-->失败");
                logger.error("request qrCode error!retry={},taskId={},websiteName={}", retry, taskId, websiteName);
                continue;
            }
            monitorService.sendTaskLog(taskId, "电商-->刷新二维码-->成功");
            directiveId = getMessageService().sendDirective(taskId, DirectiveEnum.REQUIRE_QR.getCode(), qrcodeResult);
            getRedisService().saveDirectiveResult(directiveId, sendDirective);

            long statTime = System.currentTimeMillis();

            while (!isTimeOut(statTime, websiteName)) {
                TimeUnit.MILLISECONDS.sleep(500);
                QRCodeResult verifyResult = verifyQRCodeStatus(paramsMap);
                if (null == verifyResult) {
                    continue;
                }
                logger.info("taskId={},directiveId={},websiteName={},verifyResult={}", taskId, directiveId, websiteName, verifyResult.status);
                if (QRCodeStatus.SCANNED == verifyResult.status) {
                    // 已扫描,待确认
                    sendDirective.fill(DirectiveRedisCode.SCANNED, null);
                    getRedisService().saveDirectiveResult(directiveId, sendDirective);
                    logger.info("verifyQRCodeStatus taskId={},directiveId={},websiteName={},status = {}", taskId, directiveId, websiteName, DirectiveRedisCode.SCANNED);
                    monitorService.sendTaskLog(taskId, "电商-->用户已扫描,等待确认-->成功");
                }
                if (QRCodeStatus.CONFIRMED == verifyResult.status) {
                    QRCodeResult confirmResult = confirmQRCodeStatus(paramsMap);
                    if (null != confirmResult) {
                        logger.info("confirmQRCodeStatus taskId={},directiveId={},websiteName={},confirmResult = {}", taskId, directiveId, websiteName, confirmResult.status);
                        if (QRCodeStatus.SUCCESS == confirmResult.status) {
                            resultMap.put(PluginConstants.FIELD, this.postQRCode(confirmResult.result));
                            sendDirective.fill(DirectiveRedisCode.SUCCESS, null);
                            getRedisService().saveDirectiveResult(directiveId, sendDirective);
                            getMessageService().sendTaskLog(taskId, "二维码验证成功");
                            monitorService.sendTaskLog(taskId, "电商-->校验二维码-->成功");
                            return resultMap;
                        }
                    }
                }
            }
        } while (retry++ < maxRetry);
        monitorService.sendTaskLog(taskId, "电商-->校验二维码-->失败");
        getMessageService().sendTaskLog(taskId, "二维码验证失败");

        if (StringUtils.isNoneBlank(directiveId)) {
            sendDirective.fill(DirectiveRedisCode.SKIP, null);
            getRedisService().saveDirectiveResult(directiveId, sendDirective);
        }

        // redisMap.put(PluginConstants.FIELD, DirectiveRedisCode.SKIP);
        logger.info("qrcode valid fail, taskId={},directiveId={},websiteName={},status={}", taskId, directiveId, websiteName, DirectiveRedisCode.SKIP);
        // ThreadInterruptedUtil.setInterrupt(Thread.currentThread());
        // throw new ResultEmptyException("二维码验证失败");
        resultMap.put(PluginConstants.FIELD, DirectiveRedisCode.SKIP);
        return resultMap;
    }

    public Map<String, String> perpareParam(Map<String, String> parms) {
        logger.debug("default perpareParam directly return!");
        return parms;
    }

    public boolean preQRCode(Map<String, String> parms) {
        return true;
    }

    public String postQRCode(Map<String, Object> parms) {
        return "CONFIRMED";
    }

    public boolean isQRCodeNotEmpty(Map<String, String> parms) {
        return true;
    }

    protected void setSendMessageTips(Map<String, String> parms, String title, String tips) {
        if (parms != null) {
            parms.put("tips", tips);
            parms.put("title", title);
        }
    }

    protected int getMaxInterval(String websiteName) {
        return PropertiesConfiguration.getInstance().getInt(websiteName + ".qrCode.max.waittime", 2 * 60 * 1000);
    }

}
