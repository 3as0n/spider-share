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

import com.datatrees.spider.share.service.RpcOssService;
import com.datatrees.spider.share.service.constants.SubmitConstant;
import com.datatrees.spider.share.service.oss.OssServiceProvider;
import com.datatrees.spider.share.service.oss.OssUtils;
import org.springframework.stereotype.Service;

/**
 * Created by zhangyanjia on 2017/12/15.
 */
@Service
public class RpcOssServiceImpl implements RpcOssService {

    @Override
    public void upload(String path, byte[] pageContent) {
        OssServiceProvider.getDefaultService().putObject(SubmitConstant.ALIYUN_OSS_DEFAULTBUCKET, OssUtils.getObjectKey(path), pageContent);
    }
}
