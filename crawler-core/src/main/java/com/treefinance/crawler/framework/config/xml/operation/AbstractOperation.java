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

package com.treefinance.crawler.framework.config.xml.operation;

import com.datatrees.common.util.json.annotation.Description;
import com.treefinance.crawler.framework.config.annotation.Attr;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.enums.operation.OperationType;

import java.io.Serializable;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Feb 7, 2014 2:24:35 PM
 */
@Description(value = "type",
    keys = {"REPLACE", "XPATH", "JSONPATH", "REGEX", "PARSER", "TEMPLATE", "CODEC", "TRIM", "RETURN", "SET", "EXTRACT", "APPEND", "MATCHGROUP", "DATETIME", "TRIPLE", "MAILPARSER",
        "CALCULATE", "ESCAPE", "DECODE", "PROXYSET", "MAPPING", "SLEEP", "RETURNMATCH"},
    types = {ReplaceOperation.class, XpathOperation.class, JsonPathOperation.class, RegexOperation.class, ParserOperation.class, TemplateOperation.class, CodecOperation.class,
        TrimOperation.class, ReturnOperation.class, SetOperation.class, ExtractOperation.class, AppendOperation.class, MatchGroupOperation.class, DateTimeOperation.class,
        TripleOperation.class, MailParserOperation.class, CalculateOperation.class, EscapeOperation.class, DecodeOperation.class, ProxySetOperation.class, MappingOperation.class,
        SleepOperation.class, ReturnMatchOperation.class})
public abstract class AbstractOperation implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3764777526967983011L;

    private OperationType type;

    @Attr("type")
    public OperationType getType() {
        return type;
    }

    @Node("@type")
    public void setType(String type) {
        this.type = OperationType.getOperationType(type);
    }

}
