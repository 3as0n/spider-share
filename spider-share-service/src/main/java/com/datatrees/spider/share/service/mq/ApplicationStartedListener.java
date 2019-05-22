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

package com.datatrees.spider.share.service.mq;

import com.treefinance.crawler.framework.context.control.BusinessTypeDecider;
import com.treefinance.crawler.framework.context.control.IBusinessTypeFilter;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Jerry
 * @since 21:57 2018/5/9
 */
@Component
@Order(1)
public class ApplicationStartedListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartedListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOGGER.info("Application context completed!");

        ApplicationContext applicationContext = event.getApplicationContext();
        Map<String, IBusinessTypeFilter> beans = applicationContext.getBeansOfType(IBusinessTypeFilter.class);
        if (MapUtils.isNotEmpty(beans)) {
            LOGGER.info("Registering crawling-business filters into decider.");
            BusinessTypeDecider.registerFilters(beans.values());
        }
    }

}
