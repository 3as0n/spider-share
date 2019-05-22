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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.spider.share.common.share.service.ProxyService;
import com.datatrees.spider.share.common.share.service.RedisService;
import com.datatrees.spider.share.domain.GroupEnum;
import com.datatrees.spider.share.domain.RedisKeyPrefixEnum;
import com.datatrees.spider.share.domain.model.WebsiteConf;
import com.datatrees.spider.share.domain.website.WebsiteConfig;
import com.datatrees.spider.share.service.WebsiteConfigService;
import com.datatrees.spider.share.service.WebsiteHolderService;
import com.datatrees.spider.share.service.extra.SimpleProxyManager;
import com.treefinance.crawler.framework.config.factory.ParentConfigHandler;
import com.treefinance.crawler.framework.config.factory.SpiderConfigFactory;
import com.treefinance.crawler.framework.config.xml.AbstractWebsiteConfig;
import com.treefinance.crawler.framework.config.xml.ExtractorConfig;
import com.treefinance.crawler.framework.config.xml.SearchConfig;
import com.treefinance.crawler.framework.context.ExtractorProcessorContext;
import com.treefinance.crawler.framework.context.SearchProcessorContext;
import com.treefinance.crawler.framework.context.Website;
import com.treefinance.crawler.framework.extension.manager.PluginManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouxinghai on 2017/6/30.
 */
@Service
public class WebsiteConfigServiceImpl implements WebsiteConfigService {

    private static final Logger logger = LoggerFactory.getLogger(WebsiteConfigServiceImpl.class);

    private static Map<Integer, String> bankIds = new HashMap<>();

    @Resource
    private PluginManager pluginManager;

    @Resource
    private ProxyService proxyService;

    private ParentConfigHandler parentConfigHandler;

    @Resource
    private WebsiteHolderService websiteHolderService;

    @Resource
    private RedisService redisService;

    public WebsiteConfigServiceImpl() {
        parentConfigHandler = (ParentConfigHandler<AbstractWebsiteConfig>)type -> {
            String parentWebsiteName = type.getParentWebsiteName();
            if (StringUtils.isNotBlank(parentWebsiteName)) {
                logger.info("do parentConfigHandler for parentWebsiteName named: " + parentWebsiteName + " for class " + type.getClass());
                Website website = websiteHolderService.getWebsite(parentWebsiteName);
                if (website != null) {
                    if (type instanceof SearchConfig) {
                        ((SearchConfig)type).clone(website.getSearchConfig());
                    } else if (type instanceof ExtractorConfig) {
                        ((ExtractorConfig)type).clone(website.getExtractorConfig());
                    }
                }
            }
            return type;
        };
    }

    @Override
    public SearchProcessorContext getSearchProcessorContext(Long taskId, String websiteName) {
        Website website = websiteHolderService.getWebsite(websiteName);
        if (website != null) {
            SearchProcessorContext searchProcessorContext = new SearchProcessorContext(website, taskId);
            searchProcessorContext.setPluginManager(pluginManager);
            searchProcessorContext.setProxyManager(new SimpleProxyManager(taskId, website.getWebsiteName(), proxyService));
            searchProcessorContext.init();
            logger.info("getSearchProcessorContext success,taskId={},websiteName={}", taskId, websiteName);
            return searchProcessorContext;
        }
        return null;
    }

    @Override
    public ExtractorProcessorContext getExtractorProcessorContext(Long taskId, String websiteName) {
        logger.info("getExtractorProcessorContext start,taskId={},websiteName={}", taskId, websiteName);
        Website website = websiteHolderService.getWebsite(websiteName);
        if (website != null) {
            ExtractorProcessorContext extractorProcessorContext = new ExtractorProcessorContext(website, taskId);
            extractorProcessorContext.setPluginManager(pluginManager);
            extractorProcessorContext.init();
            logger.info("getExtractorProcessorContext success,taskId={},websiteName={}", taskId, websiteName);
            return extractorProcessorContext;
        }
        return null;
    }

    @Override
    public ExtractorProcessorContext getExtractorProcessorContextWithBankId(int bankId, Long taskId) {
        String websiteName = bankIds.get(bankId);
        if (StringUtils.isBlank(websiteName)) {
            logger.error("invalid bankId={},taskId={},bankIds={}", bankId, taskId, JSON.toJSONString(bankIds));
            return null;
        }
        Website website = websiteHolderService.getWebsite(websiteName);
        if (website != null) {
            ExtractorProcessorContext extractorProcessorContext = new ExtractorProcessorContext(website, taskId);
            extractorProcessorContext.setPluginManager(pluginManager);
            extractorProcessorContext.init();
            return extractorProcessorContext;
        }
        return null;
    }

    @Override
    public Website buildWebsite(WebsiteConfig websiteConfig) {
        if (websiteConfig == null) {
            return null;
        }
        Website website = new Website();
        if (StringUtils.isNotEmpty(websiteConfig.getSearchConfig())) {
            try {
                SearchConfig searchConfig = SpiderConfigFactory.build(websiteConfig.getSearchConfig(), SearchConfig.class, parentConfigHandler);
                website.setSearchConfig(searchConfig);
                website.setSearchConfigSource(websiteConfig.getSearchConfig());
            } catch (Exception e) {
                logger.error("parse searchConfig  error websiteId={},websiteName={}", websiteConfig.getWebsiteId(), websiteConfig.getWebsiteName(), e);
            }
        }
        if (StringUtils.isNotEmpty(websiteConfig.getExtractorConfig())) {
            try {
                ExtractorConfig extractorConfig = SpiderConfigFactory.build(websiteConfig.getExtractorConfig(), ExtractorConfig.class, parentConfigHandler);
                website.setExtractorConfig(extractorConfig);
                website.setExtractorConfigSource(websiteConfig.getExtractorConfig());
            } catch (Exception e) {
                logger.error("parse extractorConfig  error websiteId={},websiteName={}", websiteConfig.getWebsiteId(), websiteConfig.getWebsiteName(), e);
            }
        }
        website.setId(websiteConfig.getWebsiteId());
        website.setIsEnabled(websiteConfig.getIsenabled());
        website.setWebsiteName(websiteConfig.getWebsiteName());
        website.setWebsiteType(websiteConfig.getWebsiteType());
        website.setSearchConfigSource(websiteConfig.getSearchConfig());
        website.setExtractorConfigSource(websiteConfig.getExtractorConfig());
        if (StringUtils.isBlank(websiteConfig.getGroupCode())) {
            GroupEnum group = GroupEnum.getByWebsiteName(websiteConfig.getWebsiteName());
            if (null == group) {
                logger.warn("not found group code for webisteName={}", websiteConfig.getWebsiteName());
                // throw new RuntimeException("not found group code for webisteName=" + websiteConfig.getWebsiteName());
            } else {
                website.setGroupCode(group.getGroupCode());
                website.setGroupName(group.getGroupName());
                website.setWebsiteTitle(group.getGroupName());
            }
        } else {
            website.setGroupCode(websiteConfig.getGroupCode());
            website.setGroupName(websiteConfig.getGroupName());
            website.setWebsiteTitle(websiteConfig.getWebsiteTitle());
        }
        return website;
    }

    @Override
    public void initBankCache(Map<Integer, String> map) {
        bankIds.putAll(map);
    }

    @Override
    public WebsiteConf getWebsiteConf(String websiteName) {
        logger.info("getWebsiteConf start websiteName={}", websiteName);
        Map<String, String> map = redisService.getCache(RedisKeyPrefixEnum.WEBSITENAME_TRANSFORM_MAP, new TypeReference<Map<String, String>>() {});
        String newWebsiteName;
        if (null == map || !map.containsKey(websiteName)) {
            String property = PropertiesConfiguration.getInstance().get(RedisKeyPrefixEnum.WEBSITENAME_TRANSFORM_MAP.getRedisKey());
            map = JSON.parseObject(property, Map.class);
            redisService.cache(RedisKeyPrefixEnum.WEBSITENAME_TRANSFORM_MAP, map);
        }
        newWebsiteName = map.get(websiteName);
        if (StringUtils.isBlank(newWebsiteName)) {
            logger.warn("no this websiteName in properties, websiteName is {}", websiteName);
            return null;
        }
        WebsiteConf conf = websiteHolderService.getWebsiteConf(newWebsiteName);
        if (null != conf) {
            // 中文
            conf.setName(websiteName);
        }
        return conf;
    }

    @Override
    public List<WebsiteConf> getWebsiteConf(List<String> websiteNameList) {
        List<WebsiteConf> confList = new ArrayList<>();
        for (String websiteName : websiteNameList) {
            WebsiteConf websiteConf = getWebsiteConf(websiteName);
            if (null != websiteConf) {
                confList.add(websiteConf);
            }
        }
        return confList;
    }

}
