package com.datatrees.rawdatacentral.service;

import com.datatrees.rawdatacentral.domain.model.WebsiteOperator;

/**
 * 运营商配置
 * Created by zhouxinghai on 2017/8/29
 */
public interface WebsiteOperatorService {

    /**
     * 获取运营商配置
     * @param websiteName
     * @return
     */
    WebsiteOperator getByWebsiteName(String websiteName);

    /**
     * 从老配置导入配置信息
     * @param config 自定义信息
     */
    void importWebsite(WebsiteOperator config);

    /**
     * 更新配置
     * @param config
     */
    void updateWebsite(WebsiteOperator config);

    /**
     * 从其他环境导入配置
     * @param websiteName
     * @param from        开发,测试,准生产,预发布
     */
    void importConfig(String websiteName, String from);

    /**
     * 从其他环境导入配置
     * @param websiteName
     * @param to          开发,测试,准生产,预发布
     */
    void exportConfig(String websiteName, String to);

    /**
     * 保存运营商配置
     */
    void saveConfig(WebsiteOperator websiteOperator);

}
