/*
 * Copyright © 2015 - 2018 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.datatrees.spider.share.service.collector.subtask.container.impl;

import com.datatrees.spider.share.service.collector.subtask.container.Container;
import com.datatrees.spider.share.service.domain.SubTask;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年12月21日 上午11:21:30
 */
public class SimpleSubTaskContainer implements Container {

    private SubTask subTask;

    /**
     *
     */
    public SimpleSubTaskContainer() {
        super();
    }

    /**
     * @param subTask the subTask to set
     */
    public void setSubTask(SubTask subTask) {
        this.subTask = subTask;
    }

    /*
     * (non-Javadoc)
     *
     * @see Container#getSubTask()
     */
    @Override
    public SubTask popSubTask() {
        return subTask;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * Container#addSubTask(com.datatrees.rawdatacentral
     * .core.model.subtask.SubTask)
     */
    @Override
    public void addSubTask(SubTask subTask) {
        this.subTask = subTask;
    }

}
