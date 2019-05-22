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

package com.datatrees.spider.share.dao.mapper;

import com.datatrees.spider.share.domain.model.WebsiteInfo;
import com.datatrees.spider.share.domain.model.example.WebsiteInfoExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** create by system from table website_info(非运营商配置) */
public interface WebsiteInfoMapper {
    long countByExample(WebsiteInfoExample example);

    int deleteByExample(WebsiteInfoExample example);

    int deleteByPrimaryKey(Integer websiteId);

    int insertSelective(WebsiteInfo record);

    List<WebsiteInfo> selectByExample(WebsiteInfoExample example);

    WebsiteInfo selectByPrimaryKey(Integer websiteId);

    int updateByExampleSelective(@Param("record") WebsiteInfo record, @Param("example") WebsiteInfoExample example);

    int updateByPrimaryKeySelective(WebsiteInfo record);

    int batchInsertSelective(List<WebsiteInfo> records);
}