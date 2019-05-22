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

package com.datatrees.spider.share.service.collector.common;

import java.util.Comparator;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月20日 上午12:33:40
 */
public class LinkNodeComparator implements Comparator<byte[]> {

    public LinkNodeComparator() {}

    private static long byte2long(byte[] b) {
        StringBuilder strValue = new StringBuilder();
        for (byte aB : b) {
            strValue.append(aB - 48);
        }
        return Long.parseLong(strValue.toString());
    }

    @Override
    public int compare(byte[] o1, byte[] o2) {

        long k1 = byte2long(o1);
        long k2 = byte2long(o2);

        if (k1 == k2) {
            return 0;
        }
        return k1 < k2 ? -1 : 1;
    }
}
