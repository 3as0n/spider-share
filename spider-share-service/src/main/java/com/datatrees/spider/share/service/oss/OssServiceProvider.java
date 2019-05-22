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

package com.datatrees.spider.share.service.oss;

import com.datatrees.spider.share.service.constants.SubmitConstant;

public class OssServiceProvider {

    private OssServiceProvider() {}

    /**
     * 获取默认的{@link OssService}
     * 
     * @return
     */
    public static OssService getDefaultService() {
        return OssServiceHolder.service;
    }

    /**
     * 获取{@link OssService}
     * 
     * @param endpoint OSS服务的Endpoint
     * @param accessKeyId 阿里云用户accessKeyId
     * @param accessKeySecret 阿里云用户accessKeySecret
     * @return
     */
    public static OssService getService(String endpoint, String accessKeyId, String accessKeySecret) {
        return new OssService(endpoint, accessKeyId, accessKeySecret);
    }

    private static class OssServiceHolder {

        private static OssService service = new OssService(SubmitConstant.ALIYUN_OSS_ENDPOINT, SubmitConstant.ALIYUN_OSS_ACCESSID, SubmitConstant.ALIYUN_OSS_ACCESSSECRET);
    }
}
