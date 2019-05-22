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

package com.datatrees.spider.share.domain;

import java.io.File;

/**
 * 插件更新结果 Created by zhouxinghai on 2017/7/7.
 */
public class PluginUpgradeResult {

    /**
     * 插件文件
     */
    private File file;

    /**
     * 是否需要重新加载
     */
    private Boolean forceReload;

    /**
     * 文件版本
     */
    private String version;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Boolean getForceReload() {
        return forceReload;
    }

    public void setForceReload(Boolean forceReload) {
        this.forceReload = forceReload;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
