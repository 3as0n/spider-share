/*
 * Copyright © 2015 - 2017 杭州大树网络技术有限公司. All Rights Reserved
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

package com.treefinance.crawler.framework.process.fields;

import com.datatrees.crawler.core.domain.config.extractor.FieldExtractor;
import org.apache.commons.lang.StringUtils;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 20, 2014 6:52:53 PM
 */
public class FieldExtractResult {

    private FieldExtractor extractor;

    private Object         result;

    public FieldExtractResult() {
        super();
    }

    public FieldExtractResult(FieldExtractor extractor, Object result) {
        super();
        this.extractor = extractor;
        this.result = result;
    }

    public FieldExtractResult(Object result) {
        super();
        this.result = result;
    }

    public FieldExtractor getExtractor() {
        return extractor;
    }

    public void setExtractor(FieldExtractor extractor) {
        this.extractor = extractor;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean isNotEmpty() {
        if (result instanceof String) {
            return StringUtils.isNotEmpty((String) result);
        }
        return result != null;
    }

    public String getFieldName() {
        return getExtractor().getField();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "FieldExtractResult [extractor=" + extractor + ", result=" + result + "]";
    }

}
