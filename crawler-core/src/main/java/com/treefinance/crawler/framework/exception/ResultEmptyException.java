/*
 * Copyright © 2015 - 2018 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.treefinance.crawler.framework.exception;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月20日 下午2:30:56
 */
public class ResultEmptyException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -3339472572748698248L;

    /**
     *
     */
    public ResultEmptyException() {
        super();
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public ResultEmptyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * @param message
     * @param cause
     */
    public ResultEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public ResultEmptyException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ResultEmptyException(Throwable cause) {
        super(cause);
    }

}
