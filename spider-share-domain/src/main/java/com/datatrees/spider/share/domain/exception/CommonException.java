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

package com.datatrees.spider.share.domain.exception;

import com.datatrees.spider.share.domain.ErrorCode;

/**
 * 通用异常 Created by zhouxinghai on 2017/8/2
 */
public class CommonException extends RuntimeException {

    /**
     * 错误代码
     */
    private int errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    public CommonException() {
        super();
    }

    public CommonException(int errorCode, String errorMsg) {
        super("{\"errorCode\":" + errorCode + ",\"errorMsg\":" + errorMsg + "}");
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public CommonException(String errorMsg) {
        super("{\"errorMsg\":" + errorMsg + "}");
        this.errorMsg = errorMsg;
    }

    public CommonException(int errorCode, String errorMsg, Throwable cause) {
        super("{\"errorCode\":" + errorCode + ",\"errorMsg\":" + errorMsg + "}", cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public CommonException(ErrorCode errorcode, Throwable cause) {
        super("{\"errorCode\":" + errorcode.getErrorCode() + ",\"errorMsg\":" + errorcode.getErrorMsg() + "}", cause);
        this.errorCode = errorcode.getErrorCode();
        this.errorMsg = errorcode.getErrorMsg();
    }

    public CommonException(ErrorCode errorcode) {
        super("{\"errorCode\":" + errorcode.getErrorCode() + ",\"errorMsg\":" + errorcode.getErrorMsg() + "}");
        this.errorCode = errorcode.getErrorCode();
        this.errorMsg = errorcode.getErrorMsg();
    }

}
