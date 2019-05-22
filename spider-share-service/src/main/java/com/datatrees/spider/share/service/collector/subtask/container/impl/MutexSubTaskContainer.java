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

package com.datatrees.spider.share.service.collector.subtask.container.impl;

import com.datatrees.spider.share.service.collector.subtask.container.Container;
import com.datatrees.spider.share.service.collector.subtask.container.Mutex;
import com.datatrees.spider.share.service.domain.SubTask;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年12月21日 上午11:12:57
 */
public class MutexSubTaskContainer implements Mutex, Container {

    private Queue<SubTask> exclusiveList;

    private boolean waiting = true;// thread wait for new subtask add

    /**
     *
     */
    public MutexSubTaskContainer() {
        this(new LinkedBlockingQueue<SubTask>(), true);
    }

    /**
     * @param exclusiveList
     * @param waiting
     */
    public MutexSubTaskContainer(Queue<SubTask> exclusiveList, boolean waiting) {
        super();
        this.exclusiveList = exclusiveList;
        this.waiting = waiting;
    }

    /*
     * (non-Javadoc)
     *
     * @see Container#isMutex()
     */
    @Override
    public boolean isMutex() {
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see Mutex#stopWaiting()
     */
    @Override
    public void stopWaiting() {
        waiting = false;
    }

    /**
     * @return the exclusiveList
     */
    public Queue<SubTask> getExclusiveList() {
        return exclusiveList;
    }

    /**
     * @param exclusiveList the exclusiveList to set
     */
    public void setExclusiveList(Queue<SubTask> exclusiveList) {
        this.exclusiveList = exclusiveList;
    }

    /**
     * @return the waiting
     */
    public boolean isWaiting() {
        return waiting;
    }

    /**
     * @param waiting the waiting to set
     */
    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    /*
     * (non-Javadoc)
     *
     * @see Container#getSubTask()
     */
    @Override
    public SubTask popSubTask() {
        return exclusiveList.poll();
    }

    /*
     * (non-Javadoc)
     *
     * @see Mutex#waiting()
     */
    @Override
    public boolean waiting() {
        return waiting;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * Container#addSubTask(com.datatrees.spider
     * .core.model.subtask.SubTask)
     */
    @Override
    public void addSubTask(SubTask subTask) {
        exclusiveList.offer(subTask);
    }

}
