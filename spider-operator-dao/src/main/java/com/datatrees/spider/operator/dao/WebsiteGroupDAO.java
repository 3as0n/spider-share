/*
 * Copyright © 2015 - 2018 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.datatrees.spider.operator.dao;

import javax.annotation.Resource;
import java.util.List;

import com.datatrees.spider.operator.dao.mapper.WebsiteGroupMapper;
import com.datatrees.spider.operator.domain.model.WebsiteGroup;
import org.apache.ibatis.annotations.Param;

/** create by system from table website_group(运营商分组) */
@Resource
public interface WebsiteGroupDAO extends WebsiteGroupMapper {

    /**
     * 获取最大权重运营商
     * @param groupCode
     * @return
     */
    WebsiteGroup queryMaxWeightWebsite(String groupCode);

    /**
     * 更新分组状态(启用/禁用)
     * @param websiteName
     * @param enable
     * @return
     */
    int updateEnable(@Param("websiteName") String websiteName, @Param("enable") Integer enable);

    /**
     * @param enable
     * @param operatorType
     * @param groupCode
     * @return
     */
    List<String> queryWebsiteNameList(@Param("enable") String enable, @Param("operatorType") String operatorType,
            @Param("groupCode") String groupCode);
}