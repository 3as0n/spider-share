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

package com.datatrees.spider.share.service.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年8月7日 下午3:54:59
 */
public class UniqueKeyGenUtil {

    private static final Logger logger = LoggerFactory.getLogger(UniqueKeyGenUtil.class);

    public static String uniqueKeyGen(String seed) {
        String uniqueMd5 = null;
        if (seed == null) {
            logger.info("Empty uniqueSign, use uuid to generate uniqueMd5.");
            uniqueMd5 = UUID.randomUUID().toString().replace("-", "");
        } else {
            uniqueMd5 = DigestUtils.md5Hex(seed);
        }
        return uniqueMd5;
    }
}
