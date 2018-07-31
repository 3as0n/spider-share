package com.datatrees.rawdatacentral.service;

import com.datatrees.rawdatacentral.api.internal.CommonPluginService;
import com.datatrees.spider.share.domain.CommonPluginParam;

public interface ClassLoaderService {

    Class loadPlugin(String pluginName, String className, Long taskId);

    CommonPluginService getCommonPluginService(String pluginName, String className, Long taskId);

    CommonPluginService getCommonPluginService(CommonPluginParam pluginParam);

}
