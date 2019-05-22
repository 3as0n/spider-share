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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convenience base class for implementations of the <b>Valve</b> interface. A subclass <strong>MUST</strong> implement
 * an <code>invoke()</code> method to provide the required functionality, and <strong>MAY</strong> implement the
 * <code>Lifecycle</code> interface to provide configuration management and lifecycle support.
 * 
 * @author Craig R. McClanahan
 * @version $Revision: 467222 $ $Date: 2006-10-24 11:17:11 +0800 (Tue, 24 Oct 2006) $
 */
public abstract class ValveBase implements Valve {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    // ------------------------------------------------------ Instance Variables

    /**
     * The next Valve in the pipeline this Valve is a component of.
     */
    protected Valve next = null;

    /**
     * Return the next Valve in this pipeline, or <code>null</code> if this is the last Valve in the pipeline.
     */
    @Override
    public Valve getNext() {
        return next;
    }

    /**
     * Set the Valve that follows this one in the pipeline it is part of.
     * 
     * @param valve The new next valve
     */
    @Override
    public void setNext(Valve valve) {
        this.next = valve;
    }
}
