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

package org.apache.commons.httpclient.methods;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年11月5日 下午3:23:18
 */
public class CustomGetMethod extends GetMethod {

    private boolean uriEscaped;

    private boolean retainQuote;

    private boolean coexist;

    /**
     *
     */
    public CustomGetMethod() {
        super();
    }

    /**
     * @param uri
     */
    public CustomGetMethod(String uri) {
        super(uri);
    }

    public boolean isCoexist() {
        return coexist;
    }

    public CustomGetMethod setCoexist(boolean coexist) {
        this.coexist = coexist;
        return this;
    }

    public boolean isRetainQuote() {
        return retainQuote;
    }

    public CustomGetMethod setRetainQuote(boolean retainQuote) {
        this.retainQuote = retainQuote;
        return this;
    }

    /**
     * @return the uriEscaped
     */
    public boolean isUriEscaped() {
        return uriEscaped;
    }

    /**
     * @param uriEscaped the uriEscaped to set
     */
    public CustomGetMethod setUriEscaped(boolean uriEscaped) {
        this.uriEscaped = uriEscaped;
        return this;
    }

}
