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

import com.treefinance.crawler.exception.UnexpectedException;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Objects;

/**
 * @author Jerry
 * @since 23:15 26/11/2017
 */
public abstract class RemoteWebDriverBuilder<T extends RemoteWebDriverBuilder> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final DesiredCapabilities capabilities;

    private String remoteUrl;

    private HttpCommandExecutor executor;

    private String proxy;

    private String userAgent;

    private boolean disableCSS;

    private boolean disableImage;

    RemoteWebDriverBuilder(DesiredCapabilities capabilities, String remoteUrl) {
        this.capabilities = Objects.requireNonNull(capabilities);
        this.remoteUrl = Objects.requireNonNull(remoteUrl);
        try {
            this.executor = new HttpCommandExecutor(new URL(this.remoteUrl));
        } catch (Throwable e) {
            throw new UnexpectedException("Unexpected exception when building command executor.", e);
        }
    }

    public T setProxy(String proxy) {
        this.proxy = proxy;
        return (T)this;
    }

    public T setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return (T)this;
    }

    public T setDisableCSS(boolean disableCSS) {
        this.disableCSS = disableCSS;
        return (T)this;
    }

    public T setDisableImage(boolean disableImage) {
        this.disableImage = disableImage;
        return (T)this;
    }

    public final RemoteWebDriver build() {
        if (StringUtils.isEmpty(remoteUrl)) {
            throw new IllegalArgumentException("selenium server url must not be empty.");
        }

        logger.info(">>>>>> build remote web driver, type: {}", capabilities.getBrowserName());
        logger.info(">>>>>> 设置User-Agent: {}", userAgent);
        settingUserAgent(capabilities, userAgent);

        if (StringUtils.isNotEmpty(proxy) && !(proxy.contains("localhost") || proxy.contains("127.0.0.1"))) {
            logger.info(">>>>>> 设置proxy: {}", proxy);
            settingProxy(capabilities, proxy);
        }

        settingDefault(capabilities, disableCSS, disableImage);

        logger.info(">>>>>> Desired capabilities: {}", capabilities);

        return new RemoteWebDriver(this.executor, capabilities);
    }

    protected abstract void settingDefault(DesiredCapabilities capabilities, boolean disableCSS, boolean disableImage);

    protected abstract void settingUserAgent(DesiredCapabilities capabilities, String userAgent);

    protected abstract void settingProxy(DesiredCapabilities capabilities, String proxy);
}
