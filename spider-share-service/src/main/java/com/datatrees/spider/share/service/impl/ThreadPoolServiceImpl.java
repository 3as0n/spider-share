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

package com.datatrees.spider.share.service.impl;

import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.spider.share.service.ThreadPoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ThreadPoolServiceImpl implements ThreadPoolService {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolServiceImpl.class);

    private ThreadPoolExecutor mailLoginExecutors;

    @Override
    public ThreadPoolExecutor getMailLoginExecutors() {
        int corePoolSize = PropertiesConfiguration.getInstance().getInt("mail.login.thread.min", 10);
        int maximumPoolSize = PropertiesConfiguration.getInstance().getInt("mail.login.thread.max", 100);
        if (null == mailLoginExecutors) {
            mailLoginExecutors = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(300), new ThreadFactory() {
                private AtomicInteger count = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    String threadName = "mail_login_thread_" + count.addAndGet(1);
                    t.setName(threadName);
                    logger.info("create mail login thread :{}", threadName);
                    return t;
                }
            });

        }
        return mailLoginExecutors;
    }

}
