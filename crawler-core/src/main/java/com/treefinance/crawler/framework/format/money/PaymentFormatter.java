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

package com.treefinance.crawler.framework.format.money;

import com.treefinance.crawler.framework.format.ConfigurableFormatter;
import com.treefinance.crawler.framework.format.FormatConfig;
import com.treefinance.toolkit.util.RegExp;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jerry
 * @since 00:45 2018/6/2
 */
public class PaymentFormatter extends ConfigurableFormatter<Number> {

    private static final String NEGATE_FLAG = "~";

    private static final String PAYMENT_NUM_REGEX = "([\\.,\\d]+)";

    @Override
    protected Number toFormat(@Nonnull String value, @Nonnull FormatConfig config) throws Exception {
        String numPart = RegExp.group(value, PAYMENT_NUM_REGEX, 1);
        // replace ,to
        boolean needNegate = false;
        String actualPattern = config.trimmedPattern();
        if (StringUtils.isNotEmpty(actualPattern) && actualPattern.startsWith(NEGATE_FLAG)) {
            actualPattern = actualPattern.replaceFirst(NEGATE_FLAG, "");
            needNegate = true;
        }

        Map<String, PaymentUnit> paymentMapper = buildPaymentMapping(actualPattern);
        PaymentUnit unit = findPaymentUnit(paymentMapper, value);

        if (unit == null) {
            paymentMapper = PaymentUnitMapping.getPaymentUnitMap(getConf());
            unit = findPaymentUnit(paymentMapper, value);
        }

        if (unit == null) {
            logger.debug("can't find current payment conf! input: {}, numPart: {}, pattern: {}", value, numPart, actualPattern);
            unit = PaymentUnit.OUT;
        }
        // save to .00
        String temp = unit.getValue() + numPart;

        Number result = DecimalFormat.getInstance().parse(temp);

        if (needNegate) {
            result = 0 - result.doubleValue();
        }

        return result;
    }

    private PaymentUnit findPaymentUnit(Map<String, PaymentUnit> paymentMapper, String value) {
        PaymentUnit result = null;
        if (MapUtils.isNotEmpty(paymentMapper) && StringUtils.isNotEmpty(value)) {
            String val = value.toLowerCase();
            for (Map.Entry<String, PaymentUnit> entry : paymentMapper.entrySet()) {
                String pattern = entry.getKey();
                if (RegExp.find(val, pattern.toLowerCase())) {
                    result = entry.getValue();
                    logger.debug("find payment {} unit: {}", val, result);
                    break;
                }
            }
        }
        return result;
    }

    private Map<String, PaymentUnit> buildPaymentMapping(String pattern) {
        if (StringUtils.isNotEmpty(pattern)) {
            String[] values = pattern.split("###");

            if (values.length == 2) {
                Map<String, PaymentUnit> map = new HashMap<>();
                map.put(values[0], PaymentUnit.IN);
                map.put(values[1], PaymentUnit.OUT);
                return map;
            }
        }
        return Collections.emptyMap();
    }
}
