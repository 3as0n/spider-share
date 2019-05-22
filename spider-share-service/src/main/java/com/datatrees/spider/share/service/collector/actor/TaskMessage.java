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

package com.datatrees.spider.share.service.collector.actor;

import com.datatrees.spider.share.domain.CollectorMessage;
import com.datatrees.spider.share.domain.ErrorCode;
import com.datatrees.spider.share.domain.http.HttpResult;
import com.datatrees.spider.share.domain.model.Task;
import com.datatrees.spider.share.domain.website.WebsiteType;
import com.datatrees.spider.share.service.domain.SubTaskAble;
import com.datatrees.spider.share.service.util.UnifiedSysTime;
import com.treefinance.crawler.framework.context.SearchProcessorContext;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月28日 下午5:47:32
 */
public class TaskMessage {

    private final Task task;

    private final SearchProcessorContext context;

    private Boolean messageSend;

    private Integer parentTaskId;

    private CollectorMessage collectorMessage;

    private String templateId;

    private String uniqueSuffix;

    private Boolean statusSend;

    public TaskMessage(final Task task, final SearchProcessorContext context) {
        this.task = Objects.requireNonNull(task);
        this.context = Objects.requireNonNull(context);
        this.messageSend = true;
        this.statusSend = true;
    }

    public Task getTask() {
        return task;
    }

    public Integer getProcessId() {
        return task.getId();
    }

    public SearchProcessorContext getContext() {
        return context;
    }

    public Long getTaskId() {
        return context.getTaskId();
    }

    public String getWebsiteName() {
        return context.getWebsiteName();
    }

    public WebsiteType getWebsiteType() {
        return WebsiteType.getWebsiteType(context.getWebsiteType());
    }

    public CollectorMessage getCollectorMessage() {
        return collectorMessage;
    }

    public void setCollectorMessage(CollectorMessage message) {
        this.collectorMessage = message;

        // set subtask parameter
        if (message instanceof SubTaskAble) {
            // 标记子任务
            task.setSubTask(true);

            SubTaskAble sub = (SubTaskAble)message;
            this.parentTaskId = sub.getParentTaskId();
            context.setAttribute("parentTaskLogId", this.parentTaskId);
            this.templateId = sub.getTemplateId();
            this.messageSend = !sub.isSynced();
            this.statusSend = !sub.noStatus();
            if (sub.getSubSeed() != null) {
                this.uniqueSuffix = sub.getSubSeed().getUniqueSuffix();
            }
        }
    }

    public String getTemplateId() {
        return templateId;
    }

    public boolean isSubTask() {
        return task.isSubTask();
    }

    public boolean isMainTask() {
        return !isSubTask();
    }

    public int getParentTaskId() {
        return parentTaskId;
    }

    public Boolean getMessageSend() {
        return messageSend;
    }

    public Boolean getStatusSend() {
        return statusSend;
    }

    public String getUniqueSuffix() {
        return uniqueSuffix;
    }

    public Set<String> getResultTagSet() {
        Set<String> resultTagSet = collectorMessage.getResultTagSet();
        if (CollectionUtils.isEmpty(resultTagSet)) {
            resultTagSet = new HashSet<>(getContext().getSearchConfig().getResultTagList());
        }
        return resultTagSet;
    }

    public void failure(ErrorCode errorCode) {
        task.setErrorCode(errorCode);
    }

    public void failure(ErrorCode errorCode, String message) {
        task.setErrorCode(errorCode, message);
    }

    public void failure(int code, String message) {
        task.markStatus(code, message);
    }

    public void completeSearch(HttpResult<Map<String, Object>> searchResult) {
        Task task = getTask();
        task.setFinishedAt(UnifiedSysTime.INSTANCE.getSystemTime());
        task.setDuration((task.getFinishedAt().getTime() - task.getStartedAt().getTime()) / 1000);
        if (searchResult.getResponseCode() != ErrorCode.TASK_INTERRUPTED_ERROR.getErrorCode()) {
            // 释放代理
            getContext().release();
        }
    }

    @Override
    public String toString() {
        return "TaskMessage [websiteName=" + getWebsiteName() + ", templateId=" + templateId + ", messageSend=" + messageSend + ", parentTaskID=" + parentTaskId + "]";
    }
}
