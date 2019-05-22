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

package com.datatrees.spider.share.domain;

import java.io.Serializable;

public class ProcessStatus implements Serializable {

    public static final String PROCESSING = "PROCESSING";

    public static final String REQUIRE_SECOND_PASSWORD = "REQUIRE_SECOND_PASSWORD";

    public static final String REQUIRE_SMS = "REQUIRE_SMS";

    public static final String REQUIRE_PIC = "REQUIRE_PIC";

    public static final String SUCCESS = "SUCCESS";

    public static final String FAIL = "FAIL";

}
