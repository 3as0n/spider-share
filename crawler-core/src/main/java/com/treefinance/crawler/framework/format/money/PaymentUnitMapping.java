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

package com.treefinance.crawler.framework.format.money;

import com.datatrees.common.conf.Configuration;
import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.common.util.GsonUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.reflect.TypeToken;
import com.treefinance.crawler.framework.consts.Constants;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Jerry
 * @since 01:39 2018/6/2
 */
public final class PaymentUnitMapping {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentUnitMapping.class);
    private static final LoadingCache<Configuration, Map<String, PaymentUnit>> CACHE =
        CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES).softValues().build(new CacheLoader<Configuration, Map<String, PaymentUnit>>() {
            @Override
            public Map<String, PaymentUnit> load(@Nonnull Configuration conf) throws Exception {
                Map<String, PaymentUnit> paymentMapper = new HashMap<>();
                String config = conf.get(Constants.PAYMENT_FROMAT_CONFIG, "{\"OUT\":\"支出|转出|欠款|\\\\+\",\"IN\":\"-|转入|存入|存款\"}");
                if (StringUtils.isNotEmpty(config)) {
                    Map<String, String> paymentMap = GsonUtils.fromJson(config, new TypeToken<HashMap<String, String>>() {}.getType());

                    paymentMap.forEach((key, value) -> {
                        PaymentUnit pu = null;
                        try {
                            pu = PaymentUnit.valueOf(key);
                        } catch (Exception e) {
                            LOGGER.error("payment value error", e);
                        }

                        if (pu != null && StringUtils.isNotEmpty(value)) {
                            LOGGER.debug("payment: {}, pattern: {}", pu, value);
                            paymentMapper.put(value, pu);
                        }
                    });
                }

                return paymentMapper;
            }
        });

    private PaymentUnitMapping() {}

    public static Map<String, PaymentUnit> getPaymentUnitMap(Configuration conf) {
        Configuration configuration = conf;

        if (configuration == null) {
            configuration = PropertiesConfiguration.getInstance();
        }

        Map<String, PaymentUnit> map = CACHE.getUnchecked(configuration);

        return Collections.unmodifiableMap(map);
    }
}
