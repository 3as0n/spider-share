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

import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.spider.share.domain.ResultType;
import com.datatrees.spider.share.service.domain.ExtractMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月30日 下午8:24:48
 */
public class StoragePathUtil {

    private static boolean useOriginalPathFlag = PropertiesConfiguration.getInstance().getBoolean("use.original.path.flag", false);

    public static String genStoragePath(int taskId, String uuid) {
        StringBuffer buffer = new StringBuffer();
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        return buffer.append(date).append("_").append(taskId).append("_").append(uuid).toString();
    }

    public static String genStoragePath(ExtractMessage extractMessage, String uniqueMd5) {
        String bucket;
        if (useOriginalPathFlag) {
            bucket = extractMessage.getProcessId() + "/" + extractMessage.getWebsiteId();
        } else {
            String date = new SimpleDateFormat("yyyyMM").format(new Date());
            ResultType resultType = extractMessage.getResultType();
            String resultTypeStr = null;
            if (resultType != null) {
                resultTypeStr = resultType.getValue();
            } else {
                resultTypeStr = "UnKnown";
            }
            bucket = resultTypeStr + "/" + date + "/" + extractMessage.getProcessId() + "/" + extractMessage.getWebsiteId();
        }
        return extractMessage.getMessageIndex() == null ? bucket + "/" + uniqueMd5 : bucket + "/" + uniqueMd5 + "_" + extractMessage.getMessageIndex();

    }
}
