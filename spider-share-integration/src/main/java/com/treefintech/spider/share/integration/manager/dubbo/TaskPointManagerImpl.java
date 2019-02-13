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

package com.treefintech.spider.share.integration.manager.dubbo;

import com.treefinance.saas.taskcenter.facade.request.TaskPointRequest;
import com.treefinance.saas.taskcenter.facade.service.TaskPointFacade;
import com.treefinance.toolkit.util.net.NetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Jerry
 * @date 2019-01-26 17:30
 */
@Component
public class TaskPointManagerImpl implements TaskPointManager {

    private static final byte SYSTEM_POINT_FLAG = (byte) 1;
    private final TaskPointFacade taskPointFacade;

    @Autowired
    public TaskPointManagerImpl(TaskPointFacade taskPointFacade) {
        this.taskPointFacade = taskPointFacade;
    }

    @Override
    public void addTaskPoint(Long taskId, String pointCode) {
        TaskPointRequest taskPointRequest = new TaskPointRequest();
        taskPointRequest.setTaskId(taskId);
        taskPointRequest.setType(SYSTEM_POINT_FLAG);
        taskPointRequest.setCode(pointCode);
        taskPointRequest.setIp(NetUtils.getLocalHost());
        taskPointFacade.addTaskPoint(taskPointRequest);
    }
}
