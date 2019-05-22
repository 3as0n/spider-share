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

package com.datatrees.spider.share.service.extra;

import com.datatrees.spider.share.service.constants.Constants;
import com.datatrees.spider.share.service.constants.SubmitConstant;
import com.datatrees.spider.share.service.domain.ExtractMessage;
import com.datatrees.spider.share.service.domain.SubmitFile;
import com.datatrees.spider.share.service.oss.OssService;
import com.datatrees.spider.share.service.oss.OssServiceProvider;
import com.datatrees.spider.share.service.oss.OssUtils;
import com.datatrees.spider.share.service.util.ZipCompressUtils;
import com.treefinance.crawler.framework.download.WrappedFile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadTask.class);
    private ExtractMessage extractMessage;
    private List<String> fieldList;
    private String ossKey;

    public UploadTask(ExtractMessage extractMessage, List<String> fieldList, String ossKey) {
        this.extractMessage = extractMessage;
        this.fieldList = fieldList;
        this.ossKey = OssUtils.getObjectKey(ossKey);
        LOGGER.debug("OSS:ObjectKey: {}", this.ossKey);
    }

    @Override
    public void run() {
        LOGGER.info("start upload task! pid={},taskId={}", extractMessage.getProcessId(), extractMessage.getTaskId());
        try {
            Map<String, SubmitFile> uploadMap = this.getSubmitFiles(extractMessage.getMessageObject());
            // after upload complete remove extract message object
            if (MapUtils.isNotEmpty(uploadMap)) {
                byte[] data = ZipCompressUtils.compress(uploadMap);
                OssService service = OssServiceProvider.getDefaultService();
                service.putObject(SubmitConstant.ALIYUN_OSS_DEFAULTBUCKET, this.ossKey, data);
                LOGGER.info("upload task completed! pid: {}, ossKey: {}", extractMessage.getProcessId(), ossKey);
            } else {
                LOGGER.info("no need to upload file for message: {}", extractMessage);
            }
        } catch (Exception e) {
            LOGGER.error("upload task run failed! pid: {}, taskId: {}", extractMessage.getProcessId(), extractMessage.getTaskId(), e);
        }
    }

    private Map<String, SubmitFile> getSubmitFiles(Object result) {
        Map<String, SubmitFile> uploadFieldMap = new HashMap<>();
        if (result instanceof Map) {
            for (Map.Entry<String, Object> entry : ((Map<String, Object>)result).entrySet()) {
                try {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (value != null && (fieldList.contains(key) || key.endsWith("File"))) {
                        List<SubmitFile> fileBytesList = valueFormat(value);
                        this.setUploadFieldMap(uploadFieldMap, fileBytesList, entry.getKey());
                        // remove entry value from map
                        entry.setValue(null);
                    }
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        } else {
            LOGGER.warn("Error input only support map for upload.Input:" + result);
        }
        return uploadFieldMap;
    }

    @SuppressWarnings("unused")
    private void setUploadFieldMap(Map<String, SubmitFile> uploadMap, List<SubmitFile> fileBytesList, String field) {
        if (CollectionUtils.isNotEmpty(fileBytesList)) {
            int size = fileBytesList.size();
            if (size == 1) {
                uploadMap.put(field, fileBytesList.get(0));
            } else {
                for (int i = 0; i < size; i++) {
                    uploadMap.put(field + (i + 1), fileBytesList.get(i));
                }
            }
        }

    }

    private List<SubmitFile> valueFormat(Object obj) throws Exception {
        List<SubmitFile> result = new ArrayList<>();
        if (obj instanceof String) {
            SubmitFile file = new SubmitFile(null, ((String)obj).getBytes(Constants.DEFAULT_ENCODE_CHARSETNAME));
            result.add(file);
        } else if (obj instanceof WrappedFile) {
            WrappedFile wrappedFile = (WrappedFile)obj;
            byte[] resultArray = readFileBytes(wrappedFile);

            if (resultArray != null) {
                SubmitFile file = new SubmitFile(wrappedFile.getName(), resultArray);
                result.add(file);
            }
        } else if (obj instanceof Collection) {
            for (Object sub : (Collection)obj) {
                result.addAll(valueFormat(sub));
            }
        } else {
            SubmitFile file = new SubmitFile(null, obj.toString().getBytes(Constants.DEFAULT_ENCODE_CHARSETNAME));
            result.add(file);
        }
        return result;
    }

    private byte[] readFileBytes(WrappedFile wrappedFile) {
        byte[] fileBytes = null;
        if (wrappedFile != null) {
            try {
                fileBytes = wrappedFile.readFull();
                wrappedFile.remove();
            } catch (Exception e) {
                LOGGER.error("read wrappedFile error " + wrappedFile, e);
            }
        }
        return fileBytes;
    }
}
