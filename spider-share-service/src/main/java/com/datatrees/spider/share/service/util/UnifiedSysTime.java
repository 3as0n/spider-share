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

package com.datatrees.spider.share.service.util;

import com.datatrees.spider.share.common.utils.BeanFactoryUtils;
import com.datatrees.spider.share.common.utils.DateUtils;
import com.datatrees.spider.share.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月28日 下午6:05:50
 */
public enum UnifiedSysTime {
    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(UnifiedSysTime.class);

    private static final long JET_LAG;

    private static TaskService service = (TaskService)BeanFactoryUtils.getBean(TaskService.class);

    static {
        Date dbTime = service.selectNow();
        Date sysTime = new Date();
        JET_LAG = dbTime.getTime() - sysTime.getTime();
        logger.info("dbTime={},sysTime={},JET_LAG={}", DateUtils.formatYmdhms(dbTime), DateUtils.formatYmdhms(sysTime), JET_LAG);
    }

    public Date getSystemTime() {
        return new Date(System.currentTimeMillis() + JET_LAG);
    }

}
