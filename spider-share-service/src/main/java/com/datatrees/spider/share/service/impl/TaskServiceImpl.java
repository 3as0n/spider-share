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

import com.alibaba.fastjson.JSON;
import com.datatrees.spider.share.dao.TaskDAO;
import com.datatrees.spider.share.domain.model.Task;
import com.datatrees.spider.share.domain.model.example.TaskExample;
import com.datatrees.spider.share.service.TaskService;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月30日 上午12:09:08
 */
@Service
public class TaskServiceImpl implements TaskService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Resource
    private TaskDAO taskDAO;

    @Override
    public int insertTask(Task task) {
        return taskDAO.insertSelective(task);
    }

    @Override
    public void updateTask(Task task) {
        try {
            taskDAO.updateByPrimaryKeySelective(task);
        } catch (Exception e) {
            logger.error("updateTask error task={}", JSON.toJSONString(task), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Date selectNow() {
        return taskDAO.selectNow();
    }

    @Override
    public Task getByTaskId(Long taskId) {
        TaskExample example = new TaskExample();
        example.createCriteria().andTaskidEqualTo(taskId);
        List<Task> list = taskDAO.selectByExample(example);
        return list.isEmpty() ? null : list.get(0);
    }

}
