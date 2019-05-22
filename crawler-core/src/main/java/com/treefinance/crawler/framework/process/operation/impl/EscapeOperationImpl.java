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

package com.treefinance.crawler.framework.process.operation.impl;

import com.treefinance.crawler.framework.config.enums.operation.escape.EscapeType;
import com.treefinance.crawler.framework.config.enums.operation.escape.HandlingType;
import com.treefinance.crawler.framework.config.xml.extractor.FieldExtractor;
import com.treefinance.crawler.framework.config.xml.operation.EscapeOperation;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.process.operation.Operation;
import org.apache.commons.lang.StringEscapeUtils;

import javax.annotation.Nonnull;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年11月19日 下午12:05:28
 */
public class EscapeOperationImpl extends Operation<EscapeOperation> {

    public EscapeOperationImpl(@Nonnull EscapeOperation operation, @Nonnull FieldExtractor extractor) {
        super(operation, extractor);
    }

    @Override
    protected boolean isSkipped(@Nonnull EscapeOperation operation, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response) {
        // invalid escape operation and skip
        boolean flag = operation.getEscapeType() == null || operation.getHandlingType() == null;
        if (flag) {
            logger.warn("Invalid escape operation and skip. 'escape-type' or 'handling-type' was null.");
        }
        return flag;
    }

    @Override
    protected Object doOperation(@Nonnull EscapeOperation operation, @Nonnull Object operatingData, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response)
        throws Exception {
        String input = (String)operatingData;

        EscapeType escapeType = operation.getEscapeType();
        HandlingType handlingType = operation.getHandlingType();

        String result;
        switch (escapeType) {
            case HTML:
                result = handleHtml(handlingType, input);
                break;
            case JAVA:
                result = handleJava(handlingType, input);
                break;
            case JS:
                result = handleJs(handlingType, input);
                break;
            default:
                result = handleXml(handlingType, input);
                break;
        }

        return result;
    }

    private String handleXml(HandlingType handlingType, String content) {
        String result;

        if (HandlingType.ESCAPE.equals(handlingType)) {
            result = StringEscapeUtils.escapeXml(content);
        } else {
            result = StringEscapeUtils.unescapeXml(content);
        }

        return result;
    }

    private String handleJs(HandlingType handlingType, String input) {
        String result;

        if (HandlingType.ESCAPE.equals(handlingType)) {
            result = StringEscapeUtils.escapeJavaScript(input);
        } else {
            result = StringEscapeUtils.unescapeJavaScript(input);
        }

        return result;
    }

    private String handleHtml(HandlingType handlingType, String content) {
        String result;

        if (HandlingType.ESCAPE.equals(handlingType)) {
            result = StringEscapeUtils.escapeHtml(content);
        } else {
            result = StringEscapeUtils.unescapeHtml(content);
        }

        return result;
    }

    private String handleJava(HandlingType handlingType, String content) {
        String result;

        if (HandlingType.ESCAPE.equals(handlingType)) {
            result = StringEscapeUtils.escapeJava(content);
        } else {
            result = StringEscapeUtils.unescapeJava(content);
        }

        return result;
    }

}
