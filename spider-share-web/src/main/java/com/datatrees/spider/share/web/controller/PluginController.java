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

package com.datatrees.spider.share.web.controller;

import com.datatrees.spider.share.service.PluginService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * plugin上传接口
 * 
 * @author zhouxinghai
 * @date 2017/12/5
 */
@RestController
@RequestMapping("/plugin")
public class PluginController {

    private static final Logger logger = LoggerFactory.getLogger("plugin_log");

    @Resource
    private PluginService pluginService;

    /**
     * 上传java插件
     *
     * @param jar 上传的文件
     * @param fileName 文件名称
     * @param version 文件版本,默认当前时间戳
     * @param sassEnv 系统环境,可以指定
     * @return
     */
    @RequestMapping(value = "/uploadPlugin", method = RequestMethod.POST)
    public Object uploadPlugin(@RequestParam("file") MultipartFile jar, String fileName, String version, String sassEnv) {
        try {
            String filename = fileName;
            if (StringUtils.isBlank(filename)) {
                String originalFilename = jar.getOriginalFilename();
                logger.info("OriginalFilename: {}", originalFilename);
                int pos = originalFilename.lastIndexOf('\\');
                if (pos == -1) {
                    pos = originalFilename.lastIndexOf('/');
                }
                if (pos != -1) {
                    filename = originalFilename.substring(pos + 1);
                } else {
                    filename = originalFilename;
                }
            }
            logger.info("Filename: {}", filename);
            version = pluginService.savePlugin(sassEnv, filename, jar.getBytes(), version);
            logger.info("Upload plugin success! filename={}, version={}, sassEnv={}, originalFilename={}", fileName, version, sassEnv, jar.getOriginalFilename());
            return "upload plugin success: " + filename + ", version: " + version;
        } catch (Exception e) {
            logger.error("Upload plugin failure! filename={}, version={}, sassEnv={}, originalFilename={}", fileName, version, sassEnv, jar.getOriginalFilename(), e);
            return "上传失败. 错误： " + e.getMessage();
        }
    }

}
