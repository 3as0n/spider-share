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

package com.datatrees.spider.share.service.plugin;

import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.common.util.GsonUtils;
import com.datatrees.crawler.core.processor.common.resource.DataResource;
import com.datatrees.spider.share.common.share.service.RedisService;
import com.datatrees.spider.share.common.utils.BeanFactoryUtils;
import com.datatrees.spider.share.domain.AttributeKey;
import com.datatrees.spider.share.service.MessageService;
import com.google.gson.reflect.TypeToken;
import com.treefinance.crawler.framework.context.AbstractProcessorContext;
import com.treefinance.crawler.framework.context.ProcessorContextUtil;
import com.treefinance.crawler.framework.context.ResponseUtil;
import com.treefinance.crawler.framework.context.function.LinkNode;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.extension.plugin.AbstractClientPlugin;
import com.treefinance.crawler.framework.extension.plugin.PluginFactory;
import com.treefinance.crawler.framework.util.ServiceUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2016年12月16日 下午3:22:26
 */
public abstract class AbstractRawdataPlugin extends AbstractClientPlugin {

    private Logger logger = LoggerFactory.getLogger(AbstractRawdataPlugin.class);

    private String tags;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    protected Map<String, Object> parserResponseMessage(String resultContent) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(resultContent)) {
            resultMap = (Map<String, Object>)GsonUtils.fromJson(resultContent, new TypeToken<HashMap<String, Object>>() {}.getType());
            Map<String, Object> bodyMap = (Map<String, Object>)resultMap.get("body");
            if (MapUtils.isNotEmpty(bodyMap)) {
                resultMap.putAll(bodyMap);
            }
        }
        return resultMap;
    }

    protected Map<String, Object> getResultFromApp(Map<String, ?> preParamMap) {
        DataResource gatewayService = BeanFactoryUtils.getBean(DataResource.class);
        String taskId = PluginFactory.getProcessorContext().getString(AttributeKey.TASK_ID);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskId", taskId);
        map.put("taskSequence", ProcessorContextUtil.getTaskUnique(PluginFactory.getProcessorContext()));
        Object resultMessage = gatewayService.getData(map);
        String resultContent = resultMessage != null ? (String)resultMessage : StringUtils.EMPTY;
        Map<String, Object> resultContentMap = parserResponseMessage(resultContent);
        if (MapUtils.isNotEmpty(resultContentMap)) {
            return resultContentMap;
        } else {
            return new HashMap<String, Object>();
        }
    }

    @Deprecated
    protected boolean sendMessageToApp(Map<String, ?> preParamMap) {
        String userId = ProcessorContextUtil.getAccountKey(PluginFactory.getProcessorContext());
        String websiteName = PluginFactory.getProcessorContext().getWebsiteName();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("websiteName", websiteName);
        map.put("isResultEmpty", false);
        map.put("tag", this.getTags());
        map.put("taskSequence", ProcessorContextUtil.getTaskUnique(PluginFactory.getProcessorContext()));
        map.putAll(preParamMap);
        postSendMessageToApp(map);
        // DataResource gatewayService = BeanResourceFactory.getInstance().getBean(DataResource.class);
        // return gatewayService.sendToQueue(map);
        logger.info("needn't sendMessageToApp,send result:" + GsonUtils.toJson(map));
        return true;
    }

    protected void preSendMessageToApp(Map<String, String> parms) {}

    protected void postSendMessageToApp(Map<String, Object> map) {}

    protected int getMaxInterval(String websiteName) {
        return PropertiesConfiguration.getInstance().getInt(websiteName + ".default.max.waittime", 2 * 60 * 1000);
    }

    protected boolean isTimeOut(long startTime, String websiteName) throws Exception {
        long now = System.currentTimeMillis();
        int maxInterval = getMaxInterval(websiteName);
        if (now <= startTime + maxInterval) {
            return false;
        }
        return true;
    }

    protected void saveRemarkToRedis(Map<String, ?> preParamMap) {
        String taskId = PluginFactory.getProcessorContext().getString(AttributeKey.TASK_ID);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskId", taskId);
        map.putAll(preParamMap);
        String type = (String)preParamMap.get("dubboType");
        String key = StringUtils.isBlank(type) ? "plugin_remark_" + taskId : "plugin_remark_" + type + "_" + taskId;
        getDataResource().ttlSave(key, GsonUtils.toJson(map), 10 * 60 * 1000);
    }

    protected Object sendRequest(LinkNode linkNode, ResultType resultType, Integer retries) {
        try {
            AbstractProcessorContext processorContext = PluginFactory.getProcessorContext();

            SpiderResponse newResponse = ServiceUtils.invoke(null, linkNode, processorContext, null, processorContext.getVisibleScope(), retries);
            if (resultType == ResultType.ValidCode) {
                return ResponseUtil.getProtocolResponse(newResponse).getContent().getContent();
            } else {
                return org.apache.commons.lang3.StringUtils.defaultString((String)newResponse.getOutPut());
            }

        } catch (Exception e) {
            logger.error("execute request error! " + e.getMessage(), e);
        }

        if (resultType == ResultType.ValidCode) {
            return new byte[0];
        } else {
            return org.apache.commons.lang3.StringUtils.EMPTY;
        }
    }

    /**
     * 获取redis服务
     *
     * @return
     */
    protected RedisService getRedisService() {
        return BeanFactoryUtils.getBean(RedisService.class);
    }

    /**
     * 获取消息服务
     *
     * @return
     */
    protected MessageService getMessageService() {
        return BeanFactoryUtils.getBean(MessageService.class);
    }

    /**
     * 获取taskId
     *
     * @return
     */
    protected Long getTaskId() {
        return PluginFactory.getProcessorContext().getLong(AttributeKey.TASK_ID);
    }

    /**
     * 获取DataResource
     *
     * @return
     */
    @Deprecated
    private DataResource getDataResource() {
        return BeanFactoryUtils.getBean(DataResource.class);
    }

    public enum ResultType {
        ValidCode,
        Content
    }
}
