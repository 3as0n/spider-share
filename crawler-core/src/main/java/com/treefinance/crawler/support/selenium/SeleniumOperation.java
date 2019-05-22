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

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author Jerry
 * @since 15:11 12/12/2017
 */
public abstract class SeleniumOperation {

    private final WebDriver webDriver;

    public SeleniumOperation(WebDriver webDriver) {
        this.webDriver = Objects.requireNonNull(webDriver);
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    protected WebElement awaitElementLocated(By by, long timeout) {
        return waitUtil(ExpectedConditions.presenceOfElementLocated(by), timeout);
    }

    protected Boolean awaitElementStaleness(WebElement element, long timeout) {
        return waitUtil(ExpectedConditions.stalenessOf(element), timeout);
    }

    protected <V> V waitUtil(Function<WebDriver, V> function, long timeout) {
        return SeleniumHelper.waitUtil(webDriver, function, timeout);
    }

    protected <V> V waitUtil(Function<WebDriver, V> function, long timeout, long sleepMillis) {
        return SeleniumHelper.waitUtil(webDriver, function, timeout, sleepMillis);
    }

    /**
     * 执行JavaScript方法
     * 
     * @param script 待执行待script
     * @param args script需要的参数
     * @return script执行结果
     */
    protected Object eval(String script, Object... args) {
        return SeleniumHelper.evalScript(webDriver, script, args);
    }

    /**
     * 根据class name 查找页面元素
     * 
     * @param className the class name of element that to find
     * @return {@link WebElement}
     */
    protected WebElement findElementByClass(String className) {
        return SeleniumHelper.findElementByClass(webDriver, className);
    }

    /**
     * 根据id查找页面元素
     * 
     * @param id the id of element that to find
     * @return {@link WebElement}
     */
    protected WebElement findElementById(String id) {
        return SeleniumHelper.findElementById(webDriver, id);
    }

    /**
     * 当前页面定向到指定URL的页面
     * 
     * @param url 重定向的URL
     */
    protected void navigateTo(String url) {
        SeleniumHelper.navigateTo(webDriver, url);
    }

    /**
     * 刷新当前页面
     */
    protected void refreshPage() {
        SeleniumHelper.refreshPage(webDriver);
    }

    protected WebDriver.Options manage() {
        return webDriver.manage();
    }

    protected Actions actions() {
        return new Actions(webDriver);
    }
}
