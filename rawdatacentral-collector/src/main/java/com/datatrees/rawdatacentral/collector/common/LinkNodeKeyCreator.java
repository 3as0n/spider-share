package com.datatrees.rawdatacentral.collector.common;

import com.datatrees.crawler.core.processor.bean.LinkNode;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryKeyCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月20日 上午12:55:21
 */
public class LinkNodeKeyCreator implements SecondaryKeyCreator {

    private static final Logger log = LoggerFactory.getLogger(LinkNodeKeyCreator.class);

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sleepycat.je.SecondaryMultiKeyCreator#createSecondaryKeys(com.sleepycat.je.SecondaryDatabase
     * , com.sleepycat.je.DatabaseEntry, com.sleepycat.je.DatabaseEntry, java.util.Set)
     */
    @Override
    public boolean createSecondaryKey(SecondaryDatabase secondaryDb, DatabaseEntry key, DatabaseEntry data, DatabaseEntry result) {
        LinkNodeTupleBinding linkNodeBinding = new LinkNodeTupleBinding();
        LinkNode linkNode = (LinkNode) linkNodeBinding.entryToObject(data);
        try {

            String keys = (linkNode.getUrl() + "#" + linkNode.getRetryCount());
            result.setData(keys.getBytes("UTF-8"));
        } catch (Exception ex) {
            log.error("Error occurs when set key data");
            log.error(ex.toString(), ex);
        }
        return true;
    }

}
