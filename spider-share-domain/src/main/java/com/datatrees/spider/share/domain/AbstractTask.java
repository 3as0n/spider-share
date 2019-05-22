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

package com.datatrees.spider.share.domain;

/**
 * Created by zhouxinghai on 2017/7/4.
 */
public abstract class AbstractTask {

    protected String websiteName;

    /**
     * 是否子任务
     */
    private boolean isSubTask = false;

    private boolean isDuplicateRemoved;

    public void setErrorCode(ErrorCode errorCode) {
        this.setErrorCode(errorCode, null);
    }

    public synchronized void setErrorCode(ErrorCode errorCode, String message) {
        if (0 == getStatus() || errorCode.getErrorCode() < getStatus()) {
            this.markStatus(errorCode.getErrorCode(), message != null ? message : errorCode.getErrorMsg());
        }
    }

    public void markStatus(int status, String message) {
        setStatus(status);
        setRemark(message);
    }

    public abstract void setRemark(String remark);

    public abstract int getStatus();

    public abstract void setStatus(int status);

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public boolean isSubTask() {
        return isSubTask;
    }

    public void setSubTask(boolean subTask) {
        isSubTask = subTask;
    }

    public boolean isDuplicateRemoved() {
        return isDuplicateRemoved;
    }

    public void setDuplicateRemoved(boolean duplicateRemoved) {
        isDuplicateRemoved = duplicateRemoved;
    }
}
