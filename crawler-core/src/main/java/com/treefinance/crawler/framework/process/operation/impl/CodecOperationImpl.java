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

package com.treefinance.crawler.framework.process.operation.impl;

import com.treefinance.crawler.framework.config.enums.operation.codec.CodecType;
import com.treefinance.crawler.framework.config.enums.operation.codec.HandlingType;
import com.treefinance.crawler.framework.config.xml.extractor.FieldExtractor;
import com.treefinance.crawler.framework.config.xml.operation.CodecOperation;
import com.treefinance.crawler.framework.context.RequestUtil;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.process.operation.Operation;
import com.treefinance.crawler.framework.util.CharsetUtil;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;

import java.io.UnsupportedEncodingException;

/**
 * handle codec operation decode/encode etc..
 * 
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Mar 27, 2014 12:30:43 PM
 */
public class CodecOperationImpl extends Operation<CodecOperation> {

    public CodecOperationImpl(@Nonnull CodecOperation operation, @Nonnull FieldExtractor extractor) {
        super(operation, extractor);
    }

    @Override
    protected boolean isSkipped(@Nonnull CodecOperation operation, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response) {
        // invalid codec operation and skip
        boolean flag = operation.getCodecType() == null || operation.getHandlingType() == null;
        if (flag) {
            logger.warn("Invalid codec operation and skip. 'codec-type' or 'handling-type' was null.");
        }
        return flag;
    }

    @Override
    protected Object doOperation(@Nonnull CodecOperation operation, @Nonnull Object operatingData, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response)
        throws Exception {
        String input = (String)operatingData;

        String charset = RequestUtil.getContentCharset(request);
        charset = StringUtils.defaultIfEmpty(charset, CharsetUtil.UTF_8_NAME);

        CodecType cdType = operation.getCodecType();
        HandlingType handlingType = operation.getHandlingType();

        String result;
        switch (cdType) {
            case MD5:
                result = handleMd5(handlingType, input, charset);
                break;
            case BASE64:
                result = handleBase64(handlingType, input, charset);
                break;
            default:
                result = handleURL(handlingType, input, charset);
                break;
        }
        return result;
    }

    private String handleURL(HandlingType handlingType, String url, String charset) throws UnsupportedEncodingException, DecoderException {
        byte[] sourceBytes = url.getBytes(charset);

        byte[] dest;
        if (HandlingType.ENCODE.equals(handlingType)) {
            dest = URLCodec.encodeUrl(null, sourceBytes);
        } else {
            dest = URLCodec.decodeUrl(sourceBytes);
        }

        return new String(dest, charset);
    }

    private String handleBase64(HandlingType handlingType, String content, String charset) throws Exception {
        byte[] sourceBytes = content.getBytes(charset);

        byte[] dest;
        if (HandlingType.ENCODE.equals(handlingType)) {
            dest = Base64.encodeBase64(sourceBytes);
        } else {
            dest = Base64.decodeBase64(sourceBytes);
        }

        return new String(dest, charset);
    }

    private String handleMd5(HandlingType handlingType, String content, String charset) throws Exception {
        String result = content;

        if (HandlingType.ENCODE.equals(handlingType)) {
            byte[] sourceBytes = content.getBytes(charset);
            result = DigestUtils.md5Hex(sourceBytes);
        }

        return result;
    }
}
