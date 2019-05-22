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

package com.treefinance.crawler.framework.login;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Mar 16, 2014 5:34:03 PM
 */
public class Cookie implements Serializable {

    public static final Cookie EMPTY = new Cookie();

    /**
     *
     */
    private static final long serialVersionUID = -3894508724345166182L;

    @SerializedName("username")
    private String userName;

    private String cookies;

    public Cookie() {
        super();
    }

    public Cookie(String cookies) {
        this(null, cookies);
    }

    public Cookie(String userName, String cookies) {
        this.userName = userName;
        this.cookies = cookies;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    @Override
    public String toString() {
        return "Cookie [cookies=" + cookies + ", userName=" + userName + "]";
    }

}
