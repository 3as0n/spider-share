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

package com.datatrees.spider.share.service.collector.subtask;

import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.spider.share.service.collector.subtask.container.Container;
import com.datatrees.spider.share.service.collector.subtask.container.Mutex;
import com.datatrees.spider.share.service.collector.subtask.container.impl.MutexSubTaskContainer;
import com.datatrees.spider.share.service.collector.subtask.container.impl.SimpleSubTaskContainer;
import com.datatrees.spider.share.service.collector.subtask.pool.SubTaskExecutor;
import com.datatrees.spider.share.service.domain.SubTask;
import com.datatrees.spider.share.service.extra.SubTaskManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年12月21日 上午11:03:55
 */
@Service
public class MutexSupportSubTaskManager implements SubTaskManager {

    private static final Logger logger = LoggerFactory.getLogger(MutexSupportSubTaskManager.class);
    private final Map<Integer, Queue<SubTaskFuture>> syncSubTaskFutureMap = new ConcurrentHashMap<>();
    private final Map<String, SubTaskFuture> mutexSubTaskFutureMap = new ConcurrentHashMap<String, SubTaskFuture>();
    private final int maxSubTaskWaitSecond = PropertiesConfiguration.getInstance().getInt("max.subTask.wait.second", 60 * 2);
    // asyncSubTask has no mutex
    private LinkedBlockingQueue<SubTask> asyncSubTaskManagerList = new LinkedBlockingQueue<>();
    private Map<String, SubTask> syncMutexSubTaskMap = new ConcurrentHashMap<>();

    @Resource
    private SubTaskExecutor taskExecutor;

    public MutexSupportSubTaskManager() {
        super();
    }

    @PostConstruct
    public void init() {
        new AsyncSubTaskScheduleThread(taskExecutor).start();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public List<Map> getSyncedSubTaskResults(Integer taskLogId) {
        List<Map> resultsList = new ArrayList<>();
        Queue<SubTaskFuture> syncSubTaskLists = syncSubTaskFutureMap.remove(taskLogId);
        if (CollectionUtils.isNotEmpty(syncSubTaskLists)) {
            SubTaskFuture future;
            while ((future = syncSubTaskLists.poll()) != null) {
                try {
                    Container container = future.container;
                    if (container instanceof Mutex) {
                        ((Mutex)container).stopWaiting();
                        mutexSubTaskFutureMap.remove(future.mutexKey);
                    }
                    Map result;
                    try {
                        result = future.future.get(maxSubTaskWaitSecond, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        logger.warn("get sub task " + future.mutexKey + " future error:" + e.getMessage());
                        result = new HashMap();
                        if (e instanceof TimeoutException) {
                            result.put("exception", "sub task execute timeout within " + maxSubTaskWaitSecond + " seconds.");
                        }
                    }
                    if (result == null) {
                        result = new HashMap();
                        result.put("exception", "sub task result empty.");
                    }
                    result.put("subTaskKey", future.mutexKey);
                    resultsList.add(result);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return resultsList;
    }

    @Override
    public void submitSubTask(SubTask task) {
        task.setSubmitAt(System.currentTimeMillis());

        if (task.isSync()) {
            // auto submit to pool
            Queue<SubTaskFuture> queue = syncSubTaskFutureMap.computeIfAbsent(task.getParentProcessId(), k -> new LinkedBlockingQueue<>());
            if (task.isMutex()) {
                this.submitMutexSubTask(task, queue);
            } else {
                Container container = new SimpleSubTaskContainer();
                container.addSubTask(task);
                String mutexKey = task.getUniqueKey();
                logger.info("submit normal sync subtask {}, queue: {},task: {}", mutexKey, queue.hashCode(), task);
                queue.offer(new SubTaskFuture(mutexKey, container, taskExecutor.submit(container)));
            }
        } else if (task.isMutex()) {
            String mutexKey = task.getUniqueKey();

            if (syncMutexSubTaskMap.putIfAbsent(mutexKey, task) == null) {
                // add to async queue
                boolean result = asyncSubTaskManagerList.offer(task);
                logger.info("submit async & mutex {} result: {}", task, result);
            } else {
                logger.info("already contains async & mutex task for key:" + mutexKey);
            }
        } else {
            // add to async queue
            boolean result = asyncSubTaskManagerList.offer(task);
            logger.info("submit async {} result: {}", task, result);
        }
    }

    private void submitMutexSubTask(SubTask task, Queue<SubTaskFuture> queue) {
        String mutexKey = task.getUniqueKey();
        logger.info("submit mutex sync subtask " + mutexKey + " queue:" + queue.hashCode() + " ,task:" + task);

        SubTaskFuture mutexSubTaskFuture = mutexSubTaskFutureMap.computeIfAbsent(mutexKey, k -> {
            Container container = new MutexSubTaskContainer();
            SubTaskFuture subTaskFuture = new SubTaskFuture(mutexKey, container, taskExecutor.submit(container));
            queue.offer(subTaskFuture);// add to task list
            return subTaskFuture;
        });

        mutexSubTaskFuture.container.addSubTask(task);
    }

    static class SubTaskFuture {

        private String mutexKey;

        private Container container;

        private Future<Map> future;

        /**
         * @param mutexKey
         * @param container
         * @param future
         */
        public SubTaskFuture(String mutexKey, Container container, Future<Map> future) {
            super();
            this.mutexKey = mutexKey;
            this.container = container;
            this.future = future;
        }
    }

    private class AsyncSubTaskScheduleThread extends Thread {

        private final String waitingOnParentTask = "parentTask";

        private boolean shutdown = false;

        private long scheduleInterval = PropertiesConfiguration.getInstance().getLong("subtask.async.schedule.interval", 3000);

        private int subTaskCorePoolSize = PropertiesConfiguration.getInstance().getInt("subtask.core.pool.size", 50);

        private long maxSubtaskWaitingMillis = PropertiesConfiguration.getInstance().getInt("max.subtask.waiting.minutes", 5) * 60 * 1000L;

        private SubTaskExecutor subTaskExecutor;

        /**
         * @param subTaskExecutor
         */
        public AsyncSubTaskScheduleThread(SubTaskExecutor subTaskExecutor) {
            super(AsyncSubTaskScheduleThread.class.getSimpleName());
            this.subTaskExecutor = subTaskExecutor;
        }

        public void shutdown() {
            this.shutdown = true;
        }

        /**
         * Start execution.
         */
        public void run() {
            logger.info("async subTask scheduler thread started ...");
            while (!shutdown && subTaskExecutor != null) {
                try {
                    int activeCount = subTaskExecutor.getActiveCount();
                    if (activeCount < subTaskCorePoolSize) {
                        int submitCount = 0;
                        Iterator<SubTask> iterator = asyncSubTaskManagerList.iterator();
                        while (iterator.hasNext() && (submitCount + activeCount) < subTaskCorePoolSize) {
                            SubTask subTask = iterator.next();
                            if (StringUtils.isNotBlank(subTask.getSeed().getWaiting()) && !this.subTaskWaitingOnCondition(subTask)) {
                                continue;
                            }
                            if (BooleanUtils.isTrue(subTask.getSeed().isMutex())) {
                                String mutexKey = subTask.getUniqueKey();
                                syncMutexSubTaskMap.remove(mutexKey);
                                logger.info("submit async & mutex task " + subTask + " ,for mutexKey:" + mutexKey);
                            } else {
                                logger.info("submit async task" + subTask);
                            }
                            Container container = new SimpleSubTaskContainer();
                            container.addSubTask(subTask);
                            subTaskExecutor.submit(container);
                            iterator.remove();// remove subtask from queue
                            submitCount++;
                        }
                    }
                    // logger.info("active thread count " + activeCount + ", asyncSubTask need wait sleep " +
                    // scheduleInterval + "毫秒");
                    Thread.sleep(scheduleInterval);
                } catch (Exception e) {
                    logger.warn("AsyncSubTaskScheduleThread interrupted", e);
                }
            }
        }

        private boolean subTaskWaitingOnCondition(SubTask subTask) {
            String waiting = subTask.getSeed().getWaiting();
            if (waiting.equals(waitingOnParentTask)) {
                if (subTask.getSubmitAt() + maxSubtaskWaitingMillis > System.currentTimeMillis()) {
                    if (!subTask.getParentTask().getCollectorMessage().isFinish()) {
                        logger.debug(subTask + " should still waiting for ParentTask finish");
                        return false;
                    }
                } else {
                    logger.warn(subTask + " waiting for ParentTask timeout in " + maxSubtaskWaitingMillis / 1000 + "s");
                }
            } else {
                try {
                    long waitingMillis = Long.parseLong(waiting);
                    if (waitingMillis + subTask.getSubmitAt() > System.currentTimeMillis()) {
                        logger.debug(subTask + " should witing for " + waitingMillis / 1000 + " s");
                        return false;
                    }
                } catch (Exception e) {
                    logger.info(e.getMessage(), e);
                }
            }
            return true;
        }
    }
}
