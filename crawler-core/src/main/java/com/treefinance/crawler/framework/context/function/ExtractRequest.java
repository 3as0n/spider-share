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

package com.treefinance.crawler.framework.context.function;

import com.treefinance.crawler.framework.context.ExtractorProcessorContext;
import com.treefinance.toolkit.util.Preconditions;

import java.util.Map;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月14日 下午5:40:15
 */
public class ExtractRequest extends Request {

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private ExtractorProcessorContext extractContext;

        private Object input;

        private Builder() {}

        public Builder setExtractContext(ExtractorProcessorContext extractContext) {
            this.extractContext = extractContext;
            return this;
        }

        public Builder setInput(Object input) {
            this.input = input;
            return this;
        }

        @SuppressWarnings("unchecked")
        public ExtractRequest build() {
            Preconditions.notNull("extractContext", extractContext);
            Preconditions.notNull("input", input);

            ExtractRequest extractRequest = new ExtractRequest();
            extractRequest.setProcessorContext(extractContext);
            extractRequest.setInput(input);

            if (input instanceof Map) {
                extractRequest.addVisibleScope((Map<String, Object>)input);
            }

            return extractRequest;
        }

    }

}
