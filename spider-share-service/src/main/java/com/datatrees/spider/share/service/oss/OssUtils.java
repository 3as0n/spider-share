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

package com.datatrees.spider.share.service.oss;

import static com.datatrees.spider.share.service.constants.SubmitConstant.ALIYUN_OSS_OBJECT_PATH_ROOT;

/**
 * @author Jerry
 * @since 11:19 21/06/2017
 */
public final class OssUtils {

    public static final String SEPARATE = "/";

    private OssUtils() {}

    public static String getObjectKey(String key) {
        if (key.startsWith(ALIYUN_OSS_OBJECT_PATH_ROOT)) {
            return key;
        }

        if (key.startsWith(SEPARATE)) {
            return ALIYUN_OSS_OBJECT_PATH_ROOT + key;
        }

        return ALIYUN_OSS_OBJECT_PATH_ROOT + SEPARATE + key;
    }
}
