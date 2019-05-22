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

package com.datatrees.spider.share.domain.model;

import com.datatrees.spider.share.domain.AbstractExtractResult;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月27日 下午7:02:27
 */
public class EcommerceExtractResult extends AbstractExtractResult {

    private int ecommerceId;

    /**
     * @return the ecommerceId
     */
    public int getEcommerceId() {
        return ecommerceId;
    }

    /**
     * @param ecommerceId the ecommerceId to set
     */
    public void setEcommerceId(int ecommerceId) {
        this.ecommerceId = ecommerceId;
    }

}
