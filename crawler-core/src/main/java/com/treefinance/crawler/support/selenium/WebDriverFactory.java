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

package com.treefinance.crawler.support.selenium;

import com.datatrees.common.conf.Configuration;
import com.datatrees.common.conf.PropertiesConfiguration;
import com.treefinance.crawler.framework.protocol.http.HTTPConstants;
import org.openqa.selenium.WebDriver;

/**
 * @author Jerry
 * @since 16:10 26/12/2017
 */
public final class WebDriverFactory {

    private static final String SERVER_URL;

    private static final String USER_AGENT;

    static {
        Configuration configuration = PropertiesConfiguration.getInstance();
        SERVER_URL = configuration.get("selenium.hub.url", "http://121.43.180.135:6666/wd/hub");
        USER_AGENT = configuration.get(HTTPConstants.USER_AGENT, MockHttpHeaders.USER_AGENT);
    }

    private WebDriverFactory() {}

    public static WebDriver makeFirefoxDriver(String proxy) {
        return FirefoxDriverBuilder.newBuilder(WebDriverFactory.SERVER_URL).setUserAgent(WebDriverFactory.USER_AGENT).setProxy(proxy).build();
    }

    public static WebDriver makeFirefoxDriver(String proxy, boolean disableCSS, boolean disableImage) {
        return makeFirefoxDriver(WebDriverFactory.USER_AGENT, proxy, disableCSS, disableImage);
    }

    public static WebDriver makeFirefoxDriver(String userAgent, String proxy, boolean disableCSS, boolean disableImage) {
        return makeFirefoxDriver(WebDriverFactory.SERVER_URL, userAgent, proxy, disableCSS, disableImage);
    }

    public static WebDriver makeFirefoxDriver(String serverUrl, String userAgent, String proxy, boolean disableCSS, boolean disableImage) {
        return FirefoxDriverBuilder.newBuilder(serverUrl).setUserAgent(userAgent).setProxy(proxy).setDisableCSS(disableCSS).setDisableImage(disableImage).build();
    }
}
