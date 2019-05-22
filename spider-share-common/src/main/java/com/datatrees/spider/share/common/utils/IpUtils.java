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

package com.datatrees.spider.share.common.utils;

import java.net.InetAddress;

/**
 * ip管理工具 Created by zhouxinghai on 2017/5/15.
 */
public class IpUtils {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(IpUtils.class);

    /**
     * 获取本地hostname
     * 
     * @return
     */
    public static String getLocalHostName() {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            return ia.getHostName();
        } catch (Exception e) {
            logger.error("getLocalHostName error", e);
        }
        return null;
    }
}
