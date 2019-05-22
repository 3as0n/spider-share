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

package com.treefinance.crawler.framework.util;

import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.expression.StandardExpression;
import com.treefinance.crawler.framework.expression.spring.SpelExpParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年10月21日 下午1:47:57
 */
public final class CalculateUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalculateUtils.class);

    private CalculateUtils() {}

    public static Double calculate(String expression) {
        return calculate(expression, 0d, Double.class);
    }

    public static <T> T calculate(String expression, T defaultValue, Class<T> clazz) {
        return SpelExpParser.parse(expression, defaultValue, clazz);
    }

    public static <T> T calculate(String expression, SpiderRequest request, SpiderResponse response, T defaultValue, Class<T> clazz) {
        String exp = StandardExpression.eval(expression, request, response);

        LOGGER.debug("Actual calculate expression: {}", exp);

        return calculate(exp, defaultValue, clazz);
    }

    public static Double calculate(String expression, SpiderRequest request, SpiderResponse response, Double defaultValue) {
        return calculate(expression, request, response, defaultValue, Double.class);
    }

    public static double calculate(String expression, SpiderRequest request, SpiderResponse response) {
        return calculate(expression, request, response, 0d);
    }

}
