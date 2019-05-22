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

package com.treefinance.crawler.framework.extension.plugin.impl;

import com.treefinance.crawler.framework.config.enums.PluginType;
import com.treefinance.crawler.framework.config.xml.plugin.AbstractPlugin;
import com.treefinance.crawler.framework.context.AbstractProcessorContext;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.exception.PluginInvokeException;
import com.treefinance.crawler.framework.extension.manager.WrappedExtension;
import com.treefinance.crawler.framework.extension.plugin.PluginHandler;
import com.treefinance.crawler.framework.util.CommandLineExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 19, 2014 1:21:47 PM
 */
public class CommandPluginHandler extends PluginHandler {

    private static final Logger logger = LoggerFactory.getLogger(CommandPluginHandler.class);

    public CommandPluginHandler(AbstractPlugin metadata, AbstractProcessorContext context) {
        super(metadata, context);
    }

    @Override
    protected Object invokePlugin(AbstractPlugin metadata, String args, SpiderRequest request) throws Exception {
        WrappedExtension<File> extension = getContext().loadExtension(metadata, File.class);

        File file = extension.getExtension();
        String path = file.getAbsolutePath();
        logger.info("Command: " + path + ", with args: " + args);

        return executorCommand(path, args, metadata);
    }

    private String executorCommand(String path, String args, AbstractPlugin metadata) throws PluginInvokeException {
        CommandLineExecutor executor = null;
        try {
            PluginType type = metadata.getType();
            String shell = "bash";
            if (type == PluginType.PYTHON) {
                shell = "python";
            }
            executor = new CommandLineExecutor(shell, path);
            executor.setInput(args);
            executor.execute();
            return executor.getStdOut();
        } catch (Exception e) {
            throw new PluginInvokeException("Error executing command line", e);
        } finally {
            if (executor != null)
                executor.destroy();
        }
    }
}
