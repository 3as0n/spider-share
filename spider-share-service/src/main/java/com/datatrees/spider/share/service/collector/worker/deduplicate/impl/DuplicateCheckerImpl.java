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

package com.datatrees.spider.share.service.collector.worker.deduplicate.impl;

import com.datatrees.spider.share.service.collector.worker.deduplicate.DuplicateChecker;
import com.datatrees.spider.share.service.util.UniqueKeyGenUtil;

import java.util.Set;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年8月7日 下午3:15:11
 */
public class DuplicateCheckerImpl implements DuplicateChecker {

    Set<String> existedKeySet;

    /**
     * @param existedKeySet
     */
    public DuplicateCheckerImpl(Set<String> existedKeySet) {
        super();
        this.existedKeySet = existedKeySet;
    }

    @Override
    public boolean isDuplicate(String websiteType, String seed) {
        String uniqueKey = UniqueKeyGenUtil.uniqueKeyGen(seed);
        return existedKeySet != null && existedKeySet.contains(uniqueKey);
    }

}
