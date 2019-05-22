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

package com.treefinance.crawler.support.selenium;

import org.openqa.selenium.WebDriver;

/**
 * @author Jerry
 * @since 20:32 15/11/2017
 */
public final class SeleniumCaller {

    private SeleniumCaller() {}

    public static <T> T callFirefox(String proxy, Callable<T> callable) throws Exception {
        WebDriver webDriver = null;

        try {
            webDriver = WebDriverFactory.makeFirefoxDriver(proxy);

            return callable.call(webDriver);
        } finally {
            if (webDriver != null) {
                webDriver.quit();
            }
        }
    }

    public interface Callable<T> {

        T call(WebDriver webDriver) throws Exception;
    }
}
