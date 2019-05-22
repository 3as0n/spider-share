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

package com.datatrees.spider.share.service.impl;

import com.alibaba.rocketmq.common.ThreadFactoryImpl;
import com.datatrees.spider.share.service.FileStoreService;
import com.datatrees.spider.share.service.constants.SubmitConstant;
import com.datatrees.spider.share.service.domain.ExtractMessage;
import com.datatrees.spider.share.service.extra.UploadTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class FileStoreServiceImpl implements FileStoreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileStoreServiceImpl.class);

    private final ExecutorService threadPool = new ThreadPoolExecutor(5, 20, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new ThreadFactoryImpl("SubmitProcessor_"));

    private final List<String> needUploadList;

    public FileStoreServiceImpl() {
        needUploadList = Arrays.asList(SubmitConstant.SUBMITTER_NEEDUPLOAD_KEY.split(" *, *"));
    }

    @Override
    public void storeEviFile(String storePath, ExtractMessage extractMessage) {
        try {
            LOGGER.info("trigger upload task : {}", storePath);
            UploadTask task = new UploadTask(extractMessage, needUploadList, storePath);

            threadPool.submit(task);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
