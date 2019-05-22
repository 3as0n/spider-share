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

package com.datatrees.spider.share.service.plugin.qrcode;

import java.util.Map;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2016年8月31日 上午11:54:33
 */
public interface QRCodeVerification {

    public QRCodeResult confirmQRCodeStatus(Map<String, Object> params);

    public QRCodeResult verifyQRCodeStatus(Map<String, Object> params);

    public String refreshQRCode(Map<String, Object> params);

    public enum QRCodeStatus {
        WAITING,
        SCANNED,
        CONFIRMED,
        EXPIRE,
        FAILED,
        SUCCESS;
    }

    public class QRCodeResult {

        public QRCodeStatus status;

        public String remark;

        public Map<String, Object> result;

        /**
         * @param status
         * @param remark
         * @param result
         */
        public QRCodeResult(QRCodeStatus status, String remark, Map<String, Object> result) {
            super();
            this.status = status;
            this.remark = remark;
            this.result = result;
        }

        /**
         * @param status
         * @param remark
         */
        public QRCodeResult(QRCodeStatus status, String remark) {
            super();
            this.status = status;
            this.remark = remark;
        }

        /**
         *
         */
        public QRCodeResult(QRCodeStatus status) {
            this.status = status;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("QRCodeResult [status=").append(status).append(", remark=").append(remark).append(", result=").append(result).append("]");
            return builder.toString();
        }

    }

}
