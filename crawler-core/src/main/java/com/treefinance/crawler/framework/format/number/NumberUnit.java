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

package com.treefinance.crawler.framework.format.number;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Mar 31, 2014 10:18:20 AM
 */
public enum NumberUnit {

    ONE {
        @Override
        public long getProportion() {
            return 1;
        }
    },
    TEN {
        @Override
        public long getProportion() {
            return 10;
        }
    },
    HUNDRED {
        @Override
        public long getProportion() {
            return 100;
        }
    },
    /**
     * 历史原因，暂时保留
     */
    THOUDAND {
        @Override
        public long getProportion() {
            return 1000;
        }
    },
    THOUSAND {
        @Override
        public long getProportion() {
            return 1000;
        }
    },
    TEN_THOUSAND {
        @Override
        public long getProportion() {
            return 10000;
        }
    },
    HUNDRED_THOUSAND {
        @Override
        public long getProportion() {
            return 100000;
        }
    },
    MILLION {
        @Override
        public long getProportion() {
            return 1000000;
        }
    };

    public abstract long getProportion();

}
