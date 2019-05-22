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

import com.alibaba.fastjson.TypeReference;
import com.datatrees.spider.share.common.share.service.RedisService;
import com.datatrees.spider.share.dao.KeywordDAO;
import com.datatrees.spider.share.domain.model.Keyword;
import com.datatrees.spider.share.domain.model.example.KeywordExample;
import com.datatrees.spider.share.service.KeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhouxinghai on 2017/6/27.
 */
@Service
public class KeywordServiceImpl implements KeywordService {

    private static final Logger logger = LoggerFactory.getLogger(KeywordServiceImpl.class);

    @Resource
    private RedisService redisService;

    @Resource
    private KeywordDAO keywordDAO;

    @Override
    public List<Keyword> queryByWebsiteType(Integer websiteType) {
        if (null == websiteType || websiteType <= 0) {
            logger.warn("invalid param websiteType={}", websiteType);
            return null;
        }
        String key = "spider_keyword_websitetype_" + websiteType;
        List<Keyword> list = redisService.getCache(key, new TypeReference<List<Keyword>>() {});
        if (null == list || list.isEmpty()) {
            KeywordExample example = new KeywordExample();
            example.createCriteria().andWebsiteTypeEqualTo(websiteType).andIsenabledEqualTo(true);
            list = keywordDAO.selectByExample(example);
            if (null == list || list.isEmpty()) {
                redisService.cache(key, list, 1, TimeUnit.DAYS);
            }

        }
        return list;
    }
}
