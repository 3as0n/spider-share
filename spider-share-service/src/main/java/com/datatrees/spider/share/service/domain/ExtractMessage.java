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

import com.datatrees.spider.share.domain.ExtractCode;
import com.datatrees.spider.share.domain.ResultType;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月28日 下午11:12:56
 */
public class ExtractMessage {

    private final SpiderTask task;

    private Object messageObject;

    private ResultType ResultType;

    private int typeId;// maybe bankid，operatorid，ecommerceid

    private ExtractCode extractCode;

    private Map<String, String> submitkeyResult = new HashMap<>();

    private List<ExtractMessage> subExtractMessageList;

    private Integer messageIndex;

    public ExtractMessage(@Nonnull SpiderTask task) {
        this(task, null);
    }

    public ExtractMessage(@Nonnull SpiderTask task, Object obj) {
        this.task = Objects.requireNonNull(task);
        this.messageObject = obj;
    }

    public SpiderTask getTask() {
        return task;
    }

    public Long getTaskId() {
        return task.getTaskId();
    }

    public Integer getProcessId() {
        return task.getProcessId();
    }

    public Integer getWebsiteId() {
        return task.getWebsiteId();
    }

    public String getWebsiteName() {
        return task.getWebsiteName();
    }

    public Integer getMessageIndex() {
        return messageIndex;
    }

    public void setMessageIndex(Integer messageIndex) {
        this.messageIndex = messageIndex;
    }

    public ResultType getResultType() {
        return ResultType;
    }

    public void setResultType(ResultType resultType) {
        ResultType = resultType;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public Object getMessageObject() {
        return messageObject;
    }

    public void setMessageObject(Object messageObject) {
        this.messageObject = messageObject;
    }

    /**
     * @return the extractCode
     */
    public ExtractCode getExtractCode() {
        return extractCode;
    }

    /**
     * @param extractCode the extractCode to set
     */
    public void setExtractCode(ExtractCode extractCode) {
        this.extractCode = extractCode;
    }

    /**
     * @return the submitkeyResult
     */
    public Map<String, String> getSubmitkeyResult() {
        return submitkeyResult;
    }

    public void addSubmitKey(String name, String value) {
        submitkeyResult.put(name, value);
    }

    public List<ExtractMessage> getSubExtractMessageList() {
        return subExtractMessageList;
    }

    public void addSubExtractMessage(ExtractMessage subExtractMessage) {
        if (subExtractMessageList == null) {
            synchronized (this) {
                if (subExtractMessageList == null) {
                    subExtractMessageList = new ArrayList<>();
                }
            }
        }
        subExtractMessageList.add(subExtractMessage);
    }

    @Override
    public String toString() {
        return "ExtractMessage [pid=" + getProcessId() + ",taskId=" + getTaskId() + ", ResultType=" + ResultType + ", typeId=" + typeId + "]";
    }

}
