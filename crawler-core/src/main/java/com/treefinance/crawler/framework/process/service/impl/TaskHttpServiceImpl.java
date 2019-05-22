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

package com.treefinance.crawler.framework.process.service.impl;

import com.datatrees.spider.share.common.http.TaskHttpClient;
import com.datatrees.spider.share.common.utils.CollectionUtils;
import com.datatrees.spider.share.common.utils.TaskUtils;
import com.datatrees.spider.share.domain.RequestType;
import com.treefinance.crawler.framework.config.xml.properties.Properties;
import com.treefinance.crawler.framework.config.xml.service.TaskHttpService;
import com.treefinance.crawler.framework.consts.Status;
import com.treefinance.crawler.framework.context.ProcessorContextUtil;
import com.treefinance.crawler.framework.context.RequestUtil;
import com.treefinance.crawler.framework.context.SearchProcessorContext;
import com.treefinance.crawler.framework.context.function.LinkNode;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.process.service.ServiceBase;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

public class TaskHttpServiceImpl extends ServiceBase<TaskHttpService> {

    public TaskHttpServiceImpl(@Nonnull TaskHttpService service) {
        super(service);
    }

    @Override
    public void process(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {
        SearchProcessorContext context = (SearchProcessorContext)request.getProcessorContext();
        Long taskId = context.getTaskId();
        String websiteName = context.getWebsiteName();
        Properties properties = context.getSearchConfig().getProperties();
        String encoding = null;
        if (null != properties) {
            encoding = properties.getEncoding();
        }

        LinkNode linkNode = RequestUtil.getCurrentUrl(request);
        String linkUrl = linkNode.getUrl();
        String referer = linkNode.getReferer();

        if (null == taskId || StringUtils.isBlank(websiteName)) {
            logger.error("invalid param,taskId={},websiteName={},linkUrl={}", taskId, websiteName, linkUrl);
            return;
        }
        if (StringUtils.isBlank(linkUrl)) {
            logger.error("url is blan,taskId={},websiteName={},linkUrl={}", taskId, websiteName, linkUrl);
            return;
        }
        RequestType requestType = StringUtils.contains(linkUrl, "\"") ? RequestType.POST : RequestType.GET;
        String url = StringUtils.substringBefore(linkUrl, "\"");
        String requestBody = StringUtils.substringAfter(linkUrl, "\"");

        TaskHttpClient client = TaskHttpClient.create(taskId, websiteName, requestType, true).setReferer(referer).setUrl(url);
        if (CollectionUtils.isNotEmpty(context.getDefaultHeader())) {
            client.addHeaders(context.getDefaultHeader());
        }
        if (CollectionUtils.isNotEmpty(linkNode.getHeaders())) {
            client.addHeaders(linkNode.getHeaders());
        }
        if (StringUtils.isNotBlank(requestBody)) {
            client.setRequestBody(requestBody);
        }
        if (StringUtils.isNotBlank(encoding)) {
            client.setDefaultResponseCharset(encoding);
        }
        com.datatrees.spider.share.domain.http.Response invoke = client.invoke();
        logger.info("task http service do taskId={},websiteName={},linkUrl={},status={}", taskId, websiteName, linkUrl, invoke.getStatusCode());

        String content = invoke.getPageContent();
        response.setOutPut(content);

        response.setStatus(Status.VISIT_SUCCESS);
        RequestUtil.setContent(request, content);

        ProcessorContextUtil.addThreadLocalLinkNode(context, linkNode);
        ProcessorContextUtil.addThreadLocalResponse(context, response);

        String cookieString = TaskUtils.getCookieString(taskId);
        context.setCookies(cookieString);

    }

}
