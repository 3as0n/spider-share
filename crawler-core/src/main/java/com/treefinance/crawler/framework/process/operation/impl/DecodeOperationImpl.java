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

package com.treefinance.crawler.framework.process.operation.impl;

import javax.annotation.Nonnull;

import com.treefinance.crawler.framework.util.CharsetUtil;
import com.treefinance.crawler.framework.config.xml.extractor.FieldExtractor;
import com.treefinance.crawler.framework.config.xml.operation.DecodeOperation;
import com.treefinance.crawler.framework.config.enums.operation.decode.DecodeType;
import com.treefinance.crawler.framework.context.RequestUtil;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.decode.impl.BasicDecoder;
import com.treefinance.crawler.framework.decode.impl.HexDecoder;
import com.treefinance.crawler.framework.decode.impl.StandardDecoder;
import com.treefinance.crawler.framework.process.operation.Operation;
import org.apache.commons.lang.StringUtils;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年11月19日 下午12:05:28
 */
public class DecodeOperationImpl extends Operation<DecodeOperation> {

    public DecodeOperationImpl(@Nonnull DecodeOperation operation, @Nonnull FieldExtractor extractor) {
        super(operation, extractor);
    }

    @Override
    protected boolean isSkipped(@Nonnull DecodeOperation operation, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response) {
        // invalid decode operation and skip
        boolean flag = operation.getDecodeType() == null;
        if (flag) {
            logger.warn("Invalid decode operation and skip. 'decode-type' was null.");
        }
        return flag;
    }

    @Override
    protected Object doOperation(@Nonnull DecodeOperation operation, @Nonnull Object operatingData, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {
        String input = (String) operatingData;

        String charset = operation.getCharset();
        if (StringUtils.isEmpty(charset)) {
            charset = RequestUtil.getContentCharset(request);
            if (StringUtils.isEmpty(charset)) {
                charset = CharsetUtil.UTF_8_NAME;
            }
        }

        DecodeType decodeType = operation.getDecodeType();

        String result;
        switch (decodeType) {
            case BASIC:
                result = new BasicDecoder().decode(input, charset);
                break;
            case HEX:
                result = new HexDecoder().decode(input, charset);
                break;
            default:
                result = new StandardDecoder().decode(input, charset);
                break;
        }

        return result;
    }
}