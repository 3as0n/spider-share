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

package com.datatrees.spider.share.service.collector.bdb.env;

import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.spider.share.service.collector.common.CollectorConstants;
import com.datatrees.spider.share.service.collector.common.LinkNodeComparator;
import com.datatrees.spider.share.service.collector.common.LinkNodeKeyCreator;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.EnvironmentLockedException;
import com.sleepycat.je.EnvironmentMutableConfig;
import com.sleepycat.je.SecondaryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月19日 下午8:14:10
 */
public class BDBEnvironmentContext {

    private static final Logger log = LoggerFactory.getLogger(BDBEnvironmentContext.class);

    private final static String homePath = CollectorConstants.COLLECTOR_TEMP_DIR;

    private static final int LOG_FILE_SIZE = 128 * 1024 * 1024;

    private static final int MAX_ENTRIES = PropertiesConfiguration.getInstance().getInt("bdb.node.max.entries", 1024);

    private static final int MAX_DUP_TREE_ENTRIES = PropertiesConfiguration.getInstance().getInt("bdb.node.max.duptree.entries", 2048);

    static {
        log.info("Init Home Path : start..." + homePath);
        initHomePath(homePath);
    }

    protected com.sleepycat.je.Environment env = null;

    protected DatabaseConfig dbConfig = null;

    protected SecondaryConfig sdbConfig = null;

    protected String folderName = null;

    private File envHome = null;

    private EnvironmentConfig envConfig = null;

    private EnvironmentMutableConfig envMutableConfig = null;

    public BDBEnvironmentContext(String folderName) throws Exception {
        initEnv(folderName);
        initDBConfig();
        initSecondaryDB();
        this.folderName = folderName;
    }

    private static void initHomePath(String homePath) {
        File hPath = new File(homePath);
        if (hPath.exists()) {
            destroyEnv(hPath);
        }
        hPath.mkdirs();
        log.info("Init Home Path end ...");
    }

    /**
     * cleanTempFile
     * 
     * @param folder
     */
    public static void destroyEnv(File folder) {
        log.info("clear file : " + folder.getName());
        try {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        destroyEnv(file);
                    }
                    if (!file.delete()) {
                        log.info("This file  has been deleted.." + file.getName());
                    }
                }
            }
            if (!folder.delete()) {
                log.info("This folder  has been deleted..." + folder.getName());
            }
        } catch (Exception e) {
            log.error("Catch exception when clean temp bdb file .", e);
        }
    }

    /**
     * initEnv
     * 
     * @exception DatabaseException
     * @exception EnvironmentLockedException
     */
    public void initEnv(String folderName) throws Exception {
        envHome = new File(homePath + folderName);
        log.info("Create environment home : " + homePath + folderName);
        if (envHome.exists()) {
            destroyEnv(envHome);
        }
        try {
            envHome.mkdirs();
        } catch (Exception e) {
            log.error("BDB envHome.mkdirs()  error  : " + e.getMessage(), e);
            throw e;
        }
        envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);
        envConfig.setTransactional(true);
        envConfig.setConfigParam(EnvironmentConfig.LOG_FILE_MAX, "" + LOG_FILE_SIZE);

        envMutableConfig = new EnvironmentMutableConfig();
        envMutableConfig.setCacheSize(16 * 1024 * 1024);

        log.info("BDB init enviroment");
        try {
            env = new com.sleepycat.je.Environment(envHome, envConfig);
            env.setMutableConfig(envMutableConfig);
        } catch (EnvironmentLockedException e) {
            log.error("BDB env init error ,EnvironmentLockedException : " + e.getMessage(), e);
            throw e;
        } catch (DatabaseException e) {
            log.error("BDB env init error , DatabaseException: " + e.getMessage(), e);
            throw e;
        }
    }

    public Environment getEnv() {
        return env;
    }

    public File getEnvHome() {
        return envHome;
    }

    /**
     * initDBConfig
     */
    private void initDBConfig() {
        log.info("BDB init database config");
        dbConfig = new DatabaseConfig();
        dbConfig.setAllowCreate(true);
        dbConfig.setNodeMaxEntries(MAX_ENTRIES);
        dbConfig.setNodeMaxDupTreeEntries(MAX_DUP_TREE_ENTRIES);
        dbConfig.setBtreeComparator(LinkNodeComparator.class);
    }

    /**
     * initSecondaryDB
     */
    private void initSecondaryDB() {
        try {
            log.info("BDB init secondary db config");
            sdbConfig = new SecondaryConfig();
            sdbConfig.setAllowCreate(true);
            sdbConfig.setSortedDuplicates(false);
            sdbConfig.setKeyCreator(new LinkNodeKeyCreator());
        } catch (Exception e) {
            log.error("BDB init SecondaryDB error , DatabaseException : " + e.getMessage());
        }
    }

}
