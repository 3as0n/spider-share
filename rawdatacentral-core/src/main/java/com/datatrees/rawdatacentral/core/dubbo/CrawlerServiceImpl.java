/**
 * This document and its contents are protected by copyright 2015 and owned by datatrees.com Inc.
 * The copying and reproduction of this document and/or its content (whether wholly or partly) or
 * any incorporation of the same into any other material in any media or format of any kind is
 * strictly prohibited. All rights are reserved.
 * <p>
 * Copyright (c) datatrees.com Inc. 2016
 */
package com.datatrees.rawdatacentral.core.dubbo;

import com.datatrees.common.util.GsonUtils;
import com.datatrees.rawdatacentral.api.CrawlerService;
import com.datatrees.rawdatacentral.core.dao.RedisDao;
import com.datatrees.rawdatacentral.core.service.WebsiteService;
import com.datatrees.rawdatacentral.domain.common.Website;
import com.datatrees.rawdatacentral.domain.model.WebsiteConf;
import com.datatrees.rawdatacentral.domain.result.HttpResult;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since 2016年11月5日 上午11:33:19
 */
@Service
public class CrawlerServiceImpl implements CrawlerService {
    private static final Logger logger = LoggerFactory.getLogger(CrawlerServiceImpl.class);
    @Resource
    private WebsiteService websiteService;

    @Resource
    private RedisDao redisDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.datatrees.rawdata.api.CrawlerService#getWebsiteConf(java.lang.String)
     */
    @Override
    public WebsiteConf getWebsiteConf(String websiteName) {
        Website website = websiteService.getCachedWebsiteByName(websiteName);
        if (website != null) {
            return website.getWebsiteConf();
        } else {
            logger.warn("no active website named {}", websiteName);
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.datatrees.rawdata.api.CrawlerService#getWebsiteConf(java.util.List)
     */
    @Override
    public List<WebsiteConf> getWebsiteConf(List<String> websiteNameList) {
        List<WebsiteConf> confList = new ArrayList<>();
        for (String websiteName : websiteNameList) {
            confList.add(getWebsiteConf(websiteName));
        }
        return confList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.datatrees.rawdata.api.CrawlerService#updateWebsiteConfig(com.datatrees.rawdata.api.model.
     * Website)
     */
    @Override
    public boolean updateWebsiteConfig(String websiteName, String searchConfigSource, String extractConfigSource) {
        logger.info("crawlerService start update webiste:" + websiteName);
        try {
            synchronized (websiteName) {
                Website website = websiteService.getWebsiteNoConfByName(websiteName);
                if (website != null) {
                    website.setSearchConfigSource(searchConfigSource);
                    website.setExtractorConfigSource(extractConfigSource);
                    if (websiteService.countWebsiteConfigByWebsiteId(website.getId()) > 0) {
                        websiteService.updateWebsiteConfig(website);
                        logger.info("update websiteConfig success,webiste:" + websiteName);
                    } else {
                        websiteService.insertWebsiteConfig(website);
                        logger.info("insert websiteConfig success,webiste:" + websiteName);
                    }
                } else {
                    logger.warn("can't find website by websiteName:" + websiteName);
                    return false;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public HttpResult<String> importStatus(long taskId, int type, String code) {
        HttpResult<String> result = new HttpResult<String>();
        String key = "verify_result_" + taskId;
        Map<String, Object> map = new HashMap<String, Object>();
        if (type == 0) {
            map.put("status", "REFRESH_LOGIN_RANDOMPASSWORD");
        } else if (type == 1) {
            map.put("status", "REFRESH_LOGIN_CODE");
        }

        if (redisDao.saveListString(key, Arrays.asList(GsonUtils.toJson(map)))) {}
        return result.failure();
    }

    @SuppressWarnings("unchecked")
    @Override
    public HttpResult<String> fetchStatus(long taskId, int type, String attrJson) {
        // HttpResult<String> result = new HttpResult<String>();
        // String key = "verify_result_" + taskId;
        // Map<String, Object> resultMap;
        // if (redisDao.saveListString(key, Arrays.asList(attrJson))) {
        // while (true) {
        // String pullResult = redisDao.pullResult("plugin_remark_" + taskId);
        // if (StringUtils.isNotBlank(pullResult)) {
        // resultMap = (Map<String, Object>) GsonUtils.fromJson(pullResult, new
        // TypeToken<HashMap<String, Object>>() {}.getType());
        // result = result.success();
        // if (StringUtils.isNotBlank((String) resultMap.get("remark"))) {
        // result.setData((String) resultMap.get("remark"));
        // }
        // return result;
        // }
        // }
        // }
        // return result.failure();
        HttpResult<String> result = new HttpResult<String>();
        String key = "verify_result_" + taskId;
        Map<String, Object> map = new HashMap<String, Object>();
        if (type == 0) {
            map.put("status", "REFRESH_LOGIN_RANDOMPASSWORD");
        } else if (type == 1) {
            map.put("status", "REFRESH_LOGIN_CODE");
        }

        if (redisDao.saveListString(key, Arrays.asList(GsonUtils.toJson(map)))) {}

        return result.failure();

    }

    @SuppressWarnings("unchecked")
    @Override
    public HttpResult<Boolean> verifyQr(long taskId, String attrJson) {
        HttpResult<Boolean> result = new HttpResult<Boolean>();
        String key = "verify_result_" + taskId;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", "VERIFY_LOGIN_QR_CODE");

        if (redisDao.saveListString(key, Arrays.asList(GsonUtils.toJson(map)))) {
            while (true) {
                key = "plugin_remark_" + taskId;
                String pullResult = redisDao.pullResult(key);

                Map<String, Object> resultMap =
                        (Map<String, Object>) GsonUtils.fromJson(pullResult, new TypeToken<HashMap<String, Object>>() {}.getType());
                String remark;
                if (resultMap != null) {
                    remark = StringUtils.defaultIfBlank((String) resultMap.get("remark"), "");
                    if (remark.equals("LOGIN_SUCCESS")) {
                        result = result.success();
                        result.setData(true);
                        return result;
                    }

                }
            }
        }

        result = result.failure();
        result.setData(false);
        return result;
    }


}
