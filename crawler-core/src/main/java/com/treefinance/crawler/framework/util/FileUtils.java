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

package com.treefinance.crawler.framework.util;

import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.common.util.DateUtils;
import com.treefinance.crawler.framework.consts.Constants;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年10月8日 下午3:44:34
 */
public class FileUtils {

    private static String filePathPrfix = PropertiesConfiguration.getInstance().get(Constants.DOWNLOAD_FILE_STORE_PATH, "/tmp/file/");

    static {
        org.apache.commons.io.FileUtils.deleteQuietly(new File(filePathPrfix));
    }

    private static boolean mkDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    public static String getFileRandomPath(String siteName) {
        String timeString = DateUtils.baseFormatDateTime(new Date(), "yyyyMMdd");
        String parentPath = filePathPrfix + timeString + "/" + siteName;
        FileUtils.mkDir(parentPath);
        return parentPath + "/" + UUID.randomUUID();
    }
}
