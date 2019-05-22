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

import org.apache.commons.lang.BooleanUtils;

import javax.annotation.Nonnull;

import java.util.Objects;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年12月19日 下午3:57:07
 */
public class SubTask {

    private final SpiderTask parentTask;

    private final SubSeed seed;

    private long submitAt;

    public SubTask(@Nonnull SpiderTask parentTask, @Nonnull SubSeed seed) {
        this.parentTask = Objects.requireNonNull(parentTask);
        this.seed = Objects.requireNonNull(seed);
    }

    public SpiderTask getParentTask() {
        return parentTask;
    }

    public Long getTaskId() {
        return parentTask.getTaskId();
    }

    public Integer getParentProcessId() {
        return parentTask.getProcessId();
    }

    public SubSeed getSeed() {
        return seed;
    }

    public long getSubmitAt() {
        return submitAt;
    }

    public void setSubmitAt(long submitAt) {
        this.submitAt = submitAt;
    }

    public boolean isSync() {
        return BooleanUtils.isTrue(seed.isSync());
    }

    public boolean isMutex() {
        return BooleanUtils.isTrue(seed.isMutex());
    }

    public String getUniqueKey() {
        return parentTask.getProcessId() + "_" + seed.getUniqueSuffix();
    }

    @Override
    public String toString() {
        return "SubTask [taskId=" + getTaskId() + ", parentTask=" + parentTask + ", seed=" + seed + "]";
    }
}
