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

package com.datatrees.spider.share.service;

import com.datatrees.spider.share.domain.PluginUpgradeResult;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * 插件管理 Created by zhouxinghai on 2017/7/6.
 */
public interface PluginService {

    /**
     * 环境变量
     * 
     * @param sassEnv
     * @param fileName
     * @param bytes
     * @param version
     * @return
     */
    String savePlugin(String sassEnv, String fileName, byte[] bytes, String version);

    /**
     * 新的从redis获取配置
     * 
     * @param pluginName
     * @return
     */
    PluginUpgradeResult getPluginFromRedisNew(String pluginName) throws IOException;

    /**
     * 获取插件版本
     * 
     * @param pluginName
     * @return
     */
    String getPluginVersionFromCache(String pluginName) throws ExecutionException;

    /**
     * 获取插件文件路径
     * 
     * @param pluginName
     * @param version
     * @return
     */
    File getPluginFile(String pluginName, String version);

}
