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

import com.treefinance.crawler.framework.format.CommonFormatter;
import com.treefinance.crawler.framework.format.FormatConfig;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;

/**
 * @author Jerry
 * @since 00:45 2018/6/2
 */
public class CurrencyPaymentFormatter extends CommonFormatter<String> {

    private final CurrencyFormatter currencyFormat = new CurrencyFormatter();

    private final PaymentFormatter paymentFormat = new PaymentFormatter();

    @Override
    protected String toFormat(@Nonnull String value, @Nonnull FormatConfig config) throws Exception {
        String pattern = config.getPattern();
        String paymentPattern = null;
        String currencyPattern = null;
        if (StringUtils.isNotEmpty(pattern)) {
            String[] patterns = pattern.split("``");
            paymentPattern = patterns[0];
            if (patterns.length > 1) {
                currencyPattern = patterns[1];
            }
        }
        String currency = currencyFormat.format(value, config.withPattern(currencyPattern));
        Number number = paymentFormat.format(value, config.withPattern(paymentPattern));
        String result = null;
        if (number != null && currency != null) {
            result = number + " " + currency;
        }
        return result;
    }

}
