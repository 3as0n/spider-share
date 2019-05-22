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

package com.treefinance.crawler.framework.process.operation;

import com.alibaba.fastjson.JSON;
import com.treefinance.crawler.framework.config.xml.operation.AbstractOperation;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jerry
 * @since 16:04 2018/5/28
 */
public class OperationEntity implements Serializable {

    private Object source;

    private Object lastData;

    private Object data;

    private AbstractOperation lastOperation;

    private List<AbstractOperation> undoOperations;

    private OperationEntity(Object data) {
        this.source = data;
        this.lastData = data;
        this.data = data;
    }

    public static OperationEntity wrap(Object content) {
        return new OperationEntity(content);
    }

    public Object getSource() {
        return source;
    }

    public Object getLastData() {
        return lastData;
    }

    public Object getData() {
        return data;
    }

    public void update(Object result, AbstractOperation operation) {
        this.lastData = this.data;
        this.data = result;
        this.lastOperation = operation;
    }

    public void update(AbstractOperation operation) {
        this.lastOperation = operation;
    }

    public boolean isEmpty() {
        return data == null;
    }

    public AbstractOperation getLastOperation() {
        return lastOperation;
    }

    public List<AbstractOperation> getUndoOperations() {
        if (undoOperations == null) {
            undoOperations = new ArrayList<>();
        }
        return undoOperations;
    }

    public <T extends AbstractOperation> void skip(T operation) {
        if (operation != null) {
            getUndoOperations().add(operation);
        }
    }

    @Override
    public String toString() {
        String msg = "Source: " + source + "\nLastData: " + lastData + "\nData: " + data + "\nLast operation: " + JSON.toJSONString(lastOperation);

        if (CollectionUtils.isNotEmpty(undoOperations)) {
            return msg + "\nSkipped operations: " + JSON.toJSONString(undoOperations);
        }

        return msg;
    }
}
