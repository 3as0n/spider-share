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

import com.datatrees.spider.share.domain.model.Task;
import com.datatrees.spider.share.domain.model.example.TaskExample;

import java.util.List;

/** create by system from table t_tasklog(task log info) */
public interface TaskMapper {

    long countByExample(TaskExample example);

    int deleteByPrimaryKey(Integer id);

    int insertSelective(Task record);

    List<Task> selectByExample(TaskExample example);

    Task selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Task record);
}