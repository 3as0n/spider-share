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

package com.treefinance.crawler.framework.process.operation.impl;

import com.treefinance.crawler.framework.config.xml.extractor.FieldExtractor;
import com.treefinance.crawler.framework.config.xml.operation.SleepOperation;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.process.operation.Operation;

import javax.annotation.Nonnull;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 18, 2014 2:58:34 PM
 */
public class SleepOperationImpl extends Operation<SleepOperation> {

    public SleepOperationImpl(@Nonnull SleepOperation operation, @Nonnull FieldExtractor extractor) {
        super(operation, extractor, false);
    }

    @Override
    protected boolean isSkipped(@Nonnull SleepOperation operation, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response) {
        // invalid sleep operation and skip
        boolean flag = operation.getValue() == null;
        if (flag) {
            logger.warn("invalid sleep operation and skip");
        }
        return flag;
    }

    @Override
    protected Object doOperation(@Nonnull SleepOperation operation, @Nonnull Object operatingData, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response)
        throws Exception {
        Integer sleepTime = operation.getValue();
        logger.debug("Start to Sleep: {}", sleepTime);
        Thread.sleep(sleepTime);

        return null;
    }

}
