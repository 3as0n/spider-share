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

package com.treefinance.crawler.lang;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Mar 13, 2014 10:00:08 PM
 */
public enum TimeUnit {

    YEAR {
        public long toMillis(long d) {
            return x(d, C8 / C2, MAX / (C8 / C2));
        }

        public long toSeconds(long d) {
            return x(d, C8 / C3, MAX / (C8 / C3));
        }

    },
    MONTH {
        public long toMillis(long d) {
            return x(d, C7 / C2, MAX / (C7 / C2));
        }

        public long toSeconds(long d) {
            return x(d, C7 / C3, MAX / (C7 / C3));
        }

    },
    DAY {
        public long toMillis(long d) {
            return x(d, C6 / C2, MAX / (C6 / C2));
        }

        public long toSeconds(long d) {
            return x(d, C6 / C3, MAX / (C6 / C3));
        }

    },
    HOUR {
        public long toMillis(long d) {
            return x(d, C5 / C2, MAX / (C5 / C2));
        }

        public long toSeconds(long d) {
            return x(d, C5 / C3, MAX / (C5 / C3));
        }

    },
    MINUTE {
        public long toMillis(long d) {
            return x(d, C4 / C2, MAX / (C4 / C2));
        }

        public long toSeconds(long d) {
            return x(d, C4 / C3, MAX / (C4 / C3));
        }

    },
    SECOND {
        public long toMillis(long d) {
            return x(d, C3 / C2, MAX / (C3 / C2));
        }

        public long toSeconds(long d) {
            return d;
        }

    };

    // Handy constants for conversion methods
    private static final long C0 = 1L;

    private static final long C1 = C0 * 1000L;

    private static final long C2 = C1 * 1000L;

    private static final long C3 = C2 * 1000L;

    private static final long C4 = C3 * 60L;

    private static final long C5 = C4 * 60L;

    private static final long C6 = C5 * 24L;

    private static final long C7 = C6 * 30L;

    private static final long C8 = C7 * 12L;

    private static final long MAX = Long.MAX_VALUE;

    /**
     * Scale d by m, checking for overflow. This has a short name to make above code more readable.
     */
    private static long x(long d, long m, long over) {
        if (d > over)
            return Long.MAX_VALUE;
        if (d < -over)
            return Long.MIN_VALUE;
        return d * m;
    }

    /**
     * Equivalent to <tt>SECONDS.convert(duration, this)</tt>.
     * 
     * @param duration the duration
     * @return the converted duration, or <tt>Long.MIN_VALUE</tt> if conversion would negatively overflow, or
     *         <tt>Long.MAX_VALUE</tt> if it would positively overflow.
     * @see #convert
     */
    public abstract long toSeconds(long duration);

    public abstract long toMillis(long duration);

}
