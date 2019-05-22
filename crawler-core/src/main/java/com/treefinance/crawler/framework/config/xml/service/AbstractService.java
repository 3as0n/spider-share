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

package com.treefinance.crawler.framework.config.xml.service;

import com.datatrees.common.util.json.annotation.Description;
import com.treefinance.crawler.framework.config.annotation.Attr;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.enums.ServiceType;
import com.treefinance.crawler.framework.config.xml.AbstractBeanDefinition;

import java.io.Serializable;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Jan 9, 2014 7:16:53 PM
 */
@Description(value = "serviceType", keys = {"Plugin_Service", "Task_Service"}, types = {PluginService.class, TaskHttpService.class})
public abstract class AbstractService extends AbstractBeanDefinition implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1249145375986336790L;

    private ServiceType serviceType;

    @Attr("type")
    public ServiceType getServiceType() {
        return serviceType;
    }

    @Node("@type")
    public void setServiceType(String serviceType) {
        this.serviceType = ServiceType.getServiceType(serviceType);
    }

}
