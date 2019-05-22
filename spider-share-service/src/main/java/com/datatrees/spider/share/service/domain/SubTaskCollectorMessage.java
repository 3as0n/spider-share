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

package com.datatrees.spider.share.service.domain;

import com.datatrees.spider.share.domain.CollectorMessage;
import org.apache.commons.lang.BooleanUtils;

import java.util.HashMap;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月28日 下午4:14:22
 */
public class SubTaskCollectorMessage extends CollectorMessage implements SubTaskAble {

    private final Integer parentTaskId;

    private SubSeed subSeed;

    public SubTaskCollectorMessage(SpiderTask parentTask) {
        this.parentTaskId = parentTask.getProcessId();// taskLogId
        setTaskId(parentTask.getTaskId());
        setCookie(parentTask.getCookies());
        CollectorMessage collectorMessage = parentTask.getCollectorMessage();
        setEndURL(collectorMessage.getEndURL());
        setNeedDuplicate(collectorMessage.isNeedDuplicate());
        setLevel1Status(collectorMessage.isLevel1Status());
        setProperty(new HashMap<>(parentTask.getProperty()));
    }

    @Override
    public SubSeed getSubSeed() {
        return subSeed;
    }

    public void setSubSeed(SubSeed subSeed) {
        this.subSeed = subSeed;

        setWebsiteName(subSeed.getWebsiteName());
        setLoginCheckIgnore(BooleanUtils.isTrue(subSeed.getLoginCheckIgnore()));
        addProperties(subSeed);
    }

    public String getTemplateId() {
        return subSeed.getTemplateId();
    }

    @Override
    public Integer getParentTaskId() {
        return parentTaskId;
    }

    @Override
    public boolean isSynced() {
        return BooleanUtils.isTrue(subSeed.isSync());
    }

    @Override
    public boolean noStatus() {
        return subSeed != null && BooleanUtils.isTrue(subSeed.noStatus());
    }

}
