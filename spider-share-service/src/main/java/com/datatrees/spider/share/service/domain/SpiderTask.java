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

package com.datatrees.spider.share.service.domain;

import com.datatrees.spider.share.domain.CollectorMessage;
import com.treefinance.crawler.framework.context.SearchProcessorContext;

import javax.annotation.Nonnull;

import java.util.Map;
import java.util.Objects;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年12月20日 上午12:36:15
 */
public class SpiderTask {

    private final Integer processId;

    private final SearchProcessorContext processorContext;

    private CollectorMessage collectorMessage;

    public SpiderTask(Integer processId, @Nonnull SearchProcessorContext processorContext) {
        this.processId = processId;
        this.processorContext = Objects.requireNonNull(processorContext);
    }

    public Integer getProcessId() {
        return processId;
    }

    public SearchProcessorContext getProcessorContext() {
        return processorContext;
    }

    public Long getTaskId() {
        return processorContext.getTaskId();
    }

    public Integer getWebsiteId() {
        return processorContext.getWebsiteId();
    }

    public String getWebsiteName() {
        return processorContext.getWebsiteName();
    }

    /**
     * @return the cookies
     */
    public String getCookies() {
        return processorContext.getCookiesAsString();
    }

    /**
     * @return the property
     */
    public Map<String, Object> getProperty() {
        return processorContext.getProcessorResult();
    }

    /**
     * @return the collectorMessage
     */
    public CollectorMessage getCollectorMessage() {
        return collectorMessage;
    }

    /**
     * @param collectorMessage the collectorMessage to set
     */
    public void setCollectorMessage(CollectorMessage collectorMessage) {
        this.collectorMessage = collectorMessage;
    }

    @Override
    public String toString() {
        return "SpiderTask [pid=" + processId + ", websiteName=" + getWebsiteName() + ", property=" + getProperty() + "]";
    }

}
