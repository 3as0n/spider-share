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

package com.datatrees.spider.share.service.plugin.login;

import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.common.util.GsonUtils;
import com.datatrees.spider.share.common.utils.BeanFactoryUtils;
import com.datatrees.spider.share.domain.AttributeKey;
import com.datatrees.spider.share.domain.ErrorCode;
import com.datatrees.spider.share.domain.directive.DirectiveEnum;
import com.datatrees.spider.share.domain.directive.DirectiveResult;
import com.datatrees.spider.share.service.MonitorService;
import com.datatrees.spider.share.service.plugin.AbstractRawdataPlugin;
import com.google.gson.reflect.TypeToken;
import com.treefinance.crawler.framework.context.AbstractProcessorContext;
import com.treefinance.crawler.framework.exception.ResultEmptyException;
import com.treefinance.crawler.framework.extension.plugin.PluginConstants;
import com.treefinance.crawler.framework.extension.plugin.PluginFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public abstract class AbstractSMSPlugin extends AbstractRawdataPlugin {

    private Logger logger = LoggerFactory.getLogger(AbstractSMSPlugin.class);

    private MonitorService monitorService = BeanFactoryUtils.getBean(MonitorService.class);

    public Map<String, String> doProcess(Map<String, String> paramMap) throws Exception {
        AbstractProcessorContext context = PluginFactory.getProcessorContext();
        String websiteName = context.getWebsiteName();
        Long taskId = context.getLong(AttributeKey.TASK_ID);
        logger.info("start run sms plugin!taskId={},websiteName={}", taskId, websiteName);

        Map<String, String> resultMap = new LinkedHashMap<String, String>();
        Map<String, String> paramsMap = perpareParam(paramMap);

        paramMap.put("taskId", taskId.toString());

        if (StringUtils.isBlank(websiteName)) {
            logger.error("sms plugin's taskId or websitename is empty! taskId={},websiteName={}", taskId, websiteName);
            resultMap.put(AttributeKey.ERROR_CODE, "-1");
            // 貌似没用
            resultMap.put(AttributeKey.ERROR_MESSAGE, "taskId or websitename is empty");
            return resultMap;
        }
        if (!preSMSCode(paramsMap)) {
            logger.warn("no need to send sms ,taskId={},websiteName={}", taskId, websiteName);
            resultMap.putAll(paramMap);
            return resultMap;
        }
        monitorService.sendTaskLog(taskId, "详单-->校验短信启动-->成功");

        // 支付宝或者淘宝短信取消
        if (StringUtils.equals("alipay.com", websiteName) || StringUtils.equals("taobao.com", websiteName)) {
            logger.warn("还未破解短信,taskId={},websiteName={}", taskId, websiteName);
            monitorService.sendTaskLog(taskId, "支付宝/淘宝目前短信还未破解,暂不支持,忽略错误,继续跑");
            return resultMap;
        }

        // 当前重试次数
        int retry = 0;
        // 用户输入短信验证码次数
        int inputSmsCount = 0;
        boolean hasSms = false;
        // 5分钟超时
        long maxInterval = TimeUnit.MINUTES.toMillis(5) + System.currentTimeMillis();
        do {
            boolean flag = requestSMSCode(paramsMap);
            if (!flag) {
                monitorService.sendTaskLog(taskId, "详单-->发送短信验证码-->失败");

                logger.error("plugin request smsCode error!taskId={},websiteName={}", taskId, websiteName);
                TimeUnit.SECONDS.sleep(60);
                continue;
            }
            hasSms = true;
            logger.info("plugin request smsCode success!taskId={},websiteName={}", taskId, websiteName);
            // 发送任务日志
            getMessageService().sendTaskLog(taskId, "等待用户输入短信验证码");
            monitorService.sendTaskLog(taskId, "详单-->发送短信验证码-->成功");
            // 发送MQ指令
            Map<String, String> data = new HashMap<String, String>();
            data.put(AttributeKey.REMARK, StringUtils.EMPTY);
            preSendMessageToApp(data);
            String directiveId = getMessageService().sendDirective(taskId, DirectiveEnum.REQUIRE_SMS.getCode(), GsonUtils.toJson(data));
            // 保存状态到redis
            // 等待APP处理完成,并通过dubbo将数据写入redis
            DirectiveResult<Map<String, Object>> receiveDirective = getRedisService().getDirectiveResult(directiveId, getMaxInterval(websiteName), TimeUnit.MILLISECONDS);
            if (null == receiveDirective) {
                monitorService.sendTaskLog(taskId, "详单-->等待用户输入短信验证码-->失败", ErrorCode.VALIDATE_SMS_TIMEOUT, "用户2分钟没有输入短信验证码!");
                logger.error("wait user input smscode timeout,taskId={},websiteName={},directiveId={}", taskId, websiteName, directiveId);
                continue;
            }
            if (null == receiveDirective.getData() || !receiveDirective.getData().containsKey(AttributeKey.CODE)) {
                logger.error("invalid receiveDirective,taskId={},websiteName={},directiveId={},receiveDirective={}", taskId, websiteName, directiveId,
                    GsonUtils.toJson(receiveDirective));
                continue;
            }
            inputSmsCount++;
            PluginFactory.getProcessorContext().addProcessorResult("smsCodeCount", inputSmsCount);
            // 返回不为空就"认为是正确",实际大概就是短信验证码不为空,就返回短信验证码,诡异的代码,踩坑了......
            String inputCode = receiveDirective.getData().get(AttributeKey.CODE).toString();
            if (vaildSMSCode(paramsMap, inputCode)) {
                logger.info("code vaild success! taskId={},websiteName={},code={},retry={}", taskId, websiteName, receiveDirective.getData(), retry);
                // 将结果返回给插件调用的地方,作为field的值,一般返回的就是短信验证码,有的和短信验证码一起验证,有的会设置not-empty=true属性
                resultMap.put(PluginConstants.FIELD, inputCode);
                getMessageService().sendTaskLog(taskId, "短信验证码校验成功");
                monitorService.sendTaskLog(taskId, "详单-->校验短信验证码-->成功");
                return resultMap;
            }
            monitorService.sendTaskLog(taskId, "详单-->校验短信验证码-->失败");
            logger.error("code vaild failed! taskId={},websiteName={},code={},retry={},inputSmsCount={}", taskId, websiteName, inputCode, retry, inputSmsCount);

        } while (System.currentTimeMillis() < maxInterval);
        if (hasSms) {
            getMessageService().sendTaskLog(taskId, inputSmsCount == 0 ? "短信验证码校验超时" : "短信验证码校验失败");
            throw new ResultEmptyException("get sms code error,inputSmsCount:" + inputSmsCount);
        }
        return resultMap;
    }

    public Map<String, String> perpareParam(Map<String, String> parms) {
        logger.debug("default perpareParam directly return!");
        return parms;
    }

    public boolean preSMSCode(Map<String, String> parms) {
        return true;
    }

    public String postSMSCode(Map<String, String> parms, String resultCode) {
        return resultCode;
    }

    public boolean isSmsCodeNotEmpty(Map<String, String> parms) {
        return true;
    }

    public abstract boolean requestSMSCode(Map<String, String> parms);

    public abstract boolean vaildSMSCode(Map<String, String> parms, String smsCode);

    @Override
    public String process(String... args) throws Exception {
        Map<String, String> paramMap = (LinkedHashMap<String, String>)GsonUtils.fromJson(args[0], new TypeToken<LinkedHashMap<String, String>>() {}.getType());
        return GsonUtils.toJson(doProcess(paramMap));
    }

    protected void setSendMessageTips(Map<String, String> parms, String title, String tips) {
        if (parms != null) {
            parms.put("tips", tips);
            parms.put("title", title);
        }
    }

    @Override
    protected int getMaxInterval(String websiteName) {
        return PropertiesConfiguration.getInstance().getInt(websiteName + ".smsCode.max.waittime", 2 * 60 * 1000);
    }

}
