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

package com.treefinance.crawler.framework.context.pipeline;

import java.util.ArrayList;
import java.util.List;

/**
 * Standard implementation of a processing <b>Pipeline</b> that will invoke a series of Valves that have been configured
 * to be called in order. This implementation can be used for any type of Container. <b>IMPLEMENTATION WARNING</b> -
 * This implementation assumes that no calls to <code>addValve()</code> or <code>removeValve</code> are allowed while a
 * request is currently being processed. Otherwise, the mechanism by which per-thread state is maintained will need to
 * be modified.
 * 
 * @author Craig R. McClanahan
 */
public class StandardPipeline implements Pipeline {

    /**
     * The first valve associated with this Pipeline.
     */
    protected Valve first = null;

    /**
     * The last valve associated with this Pipeline.
     */
    protected Valve last = null;

    public void addValve(Valve valve) {
        Valve old = last;
        last = valve;

        if (old == null) {
            first = valve;
        } else {
            old.setNext(valve);
        }
    }

    public Valve[] getValves() {
        List<Valve> valveList = new ArrayList<>();

        Valve current = first;
        while (current != null) {
            valveList.add(current);
            current = current.getNext();
        }

        return valveList.toArray(new Valve[valveList.size()]);
    }

    public void removeValve(Valve valve) {
        if (first == valve) {
            first = first.getNext();
            if (first == null) {
                last = null;
            }
            return;
        }

        Valve current = first;
        while (current != null) {
            if (current.getNext() == valve) {
                current.setNext(valve.getNext());
                if (current.getNext() == null) {
                    last = current;
                }
                break;
            }

            current = current.getNext();
        }
    }

    public Valve getFirst() {
        return first;
    }
}
