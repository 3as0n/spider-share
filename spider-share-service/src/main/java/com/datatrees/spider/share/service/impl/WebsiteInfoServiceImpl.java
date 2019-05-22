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

package com.datatrees.spider.share.service.impl;

import com.datatrees.spider.share.common.utils.CheckUtils;
import com.datatrees.spider.share.common.utils.TaskUtils;
import com.datatrees.spider.share.dao.WebsiteInfoDAO;
import com.datatrees.spider.share.domain.ErrorCode;
import com.datatrees.spider.share.domain.GroupEnum;
import com.datatrees.spider.share.domain.model.WebsiteInfo;
import com.datatrees.spider.share.domain.model.example.WebsiteInfoExample;
import com.datatrees.spider.share.domain.website.WebsiteConfig;
import com.datatrees.spider.share.service.WebsiteInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

/**
 * Created by zhangyanjia on 2018/3/20.
 */
@Service
public class WebsiteInfoServiceImpl implements WebsiteInfoService {

    private static final Logger logger = LoggerFactory.getLogger(WebsiteInfoServiceImpl.class);

    @Resource
    private WebsiteInfoDAO websiteInfoDAO;

    @Override
    public WebsiteInfo getByWebsiteName(String websiteName) {
        CheckUtils.checkNotBlank(websiteName, ErrorCode.EMPTY_WEBSITE_NAME);
        WebsiteInfoExample example = new WebsiteInfoExample();
        String env = TaskUtils.getSassEnv();
        example.createCriteria().andWebsiteNameEqualTo(websiteName).andEnvEqualTo(env);
        List<WebsiteInfo> list = websiteInfoDAO.selectByExample(example);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public void updateWebsiteConf(String websiteName, String searchConfig, String extractConfig) {
        WebsiteInfo websiteDb = getByWebsiteName(websiteName);
        CheckUtils.checkNotNull(websiteDb, "website not found websiteName=" + websiteName);
        WebsiteInfo websiteInfoUpdate = new WebsiteInfo();
        websiteInfoUpdate.setWebsiteId(websiteDb.getWebsiteId());
        websiteInfoUpdate.setSearchConfig(searchConfig);
        websiteInfoUpdate.setExtractorConfig(extractConfig);
        websiteInfoUpdate.setUpdatedAt(new Date());
        websiteInfoDAO.updateByPrimaryKeySelective(websiteInfoUpdate);
        logger.info("updateWebsiteInfo success websiteName={}", websiteName);
    }

    @Override
    public WebsiteConfig buildWebsiteConfig(WebsiteInfo websiteInfo) {
        CheckUtils.checkNotNull(websiteInfo, "info is null");
        WebsiteConfig config = new WebsiteConfig();
        config.setWebsiteId(websiteInfo.getWebsiteId());
        config.setWebsiteName(websiteInfo.getWebsiteName());
        config.setWebsiteType(websiteInfo.getWebsiteType().toString());
        config.setIsenabled(true);
        config.setLoginTip(websiteInfo.getLoginTip());
        config.setVerifyTip(websiteInfo.getVerifyTip());
        config.setResetType(websiteInfo.getResetType());
        config.setSmsReceiver(websiteInfo.getSmsReceiver());
        config.setSmsTemplate(websiteInfo.getSmsTemplate());
        config.setResetTip(websiteInfo.getResetTip());
        config.setResetURL(websiteInfo.getResetUrl());
        config.setInitSetting(websiteInfo.getLoginConfig());
        config.setSearchConfig(websiteInfo.getSearchConfig());
        config.setExtractorConfig(websiteInfo.getExtractorConfig());
        config.setWebsiteTitle(websiteInfo.getWebsiteTitle());
        config.setGroupCode(websiteInfo.getGroupCode());
        if (websiteInfo.getGroupCode() != null && !("".equals(websiteInfo.getGroupCode()))) {
            config.setGroupName(GroupEnum.getByGroupCode(websiteInfo.getGroupCode()).getGroupName());
        }
        return config;
    }
}
