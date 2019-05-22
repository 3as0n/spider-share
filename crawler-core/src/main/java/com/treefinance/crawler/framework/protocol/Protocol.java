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

package com.treefinance.crawler.framework.protocol;

import com.datatrees.common.conf.Configurable;

/** A retriever of url content. Implemented by protocol extensions. */
public interface Protocol extends Configurable {

    /*
     * Returns the {@link Content} for a fetchlist entry.
     */
    ProtocolOutput getProtocolOutput(String url);

    ProtocolOutput getProtocolOutput(String url, long lastModified);

    ProtocolOutput getProtocolOutput(ProtocolInput input);
}
