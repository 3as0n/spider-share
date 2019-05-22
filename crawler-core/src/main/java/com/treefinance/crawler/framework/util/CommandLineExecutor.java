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

package com.treefinance.crawler.framework.util;

import com.google.common.collect.Maps;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.collect.ImmutableMap.copyOf;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月6日 上午12:22:34
 */
public class CommandLineExecutor {

    private static final Logger log = LoggerFactory.getLogger(CommandLineExecutor.class);

    private final ByteArrayOutputStream inputOut = new ByteArrayOutputStream();

    private final ByteArrayOutputStream inputErrorOut = new ByteArrayOutputStream();

    private final DefaultExecuteResultHandler handler = new DefaultExecuteResultHandler();

    private final Executor executor = new DefaultExecutor();

    private final CommandLine cl;

    private final Map<String, String> env = new ConcurrentHashMap<String, String>();

    private volatile String allInput;

    private ExecuteWatchdog executeWatchdog = new ExecuteWatchdog(1000 * 60 * 10);

    public CommandLineExecutor(String executable, String... args) {
        cl = new CommandLine(executable);
        cl.addArguments(args, false);
    }

    public void execute() throws Exception {
        executeAsync();
        waitFor();
        if (handler.getException() != null) {
            throw handler.getException();
        }
    }

    public void setEnvironmentVariable(String name, String value) {
        if (name == null) {
            throw new IllegalArgumentException("Cannot have a null environment variable name!");
        }
        if (value == null) {
            throw new IllegalArgumentException("Cannot have a null value for environment variable " + name);
        }
        env.put(name, value);
    }

    public Map<String, String> getEnvironment() {
        return copyOf(env);
    }

    public void executeAsync() throws Exception {
        final OutputStream outputStream = getOutputStream();
        executor.setWatchdog(executeWatchdog);
        executor.setStreamHandler(new PumpStreamHandler(outputStream, getOutputErrorStream(), getInputStream()));
        executor.execute(cl, getMergedEnv(), handler);
    }

    public int destroy() {
        executeWatchdog.destroyProcess();
        if (!isRunning()) {
            return getExitCode();
        }
        int exitCode = -1;
        executor.setExitValue(exitCode);
        return exitCode;
    }

    public void waitFor() throws InterruptedException {
        handler.waitFor();
    }

    public boolean isRunning() {
        return !handler.hasResult();
    }

    public int getExitCode() {
        if (isRunning()) {
            throw new IllegalStateException("Cannot get exit code before executing command line: " + cl);
        }
        return handler.getExitValue();
    }

    public String getStdOut() {
        return getStdOut(CharsetUtil.DEFAULT);
    }

    public String getStdOut(String encode) {
        if (isRunning()) {
            throw new IllegalStateException("Cannot get output before executing command line: " + cl);
        }

        Charset charset = CharsetUtil.getCharset(encode);
        if (charset != null) {
            return new String(inputOut.toByteArray(), charset);
        }
        return new String(inputOut.toByteArray());
    }

    public String getStdError() {
        return getStdError(CharsetUtil.DEFAULT);
    }

    public String getStdError(String encode) {
        if (isRunning()) {
            throw new IllegalStateException("Cannot get output before executing command line: " + cl);
        }

        Charset charset = CharsetUtil.getCharset(encode);
        if (charset != null) {
            return new String(inputErrorOut.toByteArray(), charset);
        }
        return new String(inputErrorOut.toByteArray());
    }

    public void setInput(String allInput) {
        this.allInput = allInput;
    }

    public void setWorkingDirectory(File workingDirectory) {
        executor.setWorkingDirectory(workingDirectory);
    }

    @Override
    public String toString() {
        return cl.toString() + "[ " + env + "]";
    }

    private Map<String, String> getMergedEnv() {
        HashMap<String, String> newEnv = Maps.newHashMap(System.getenv());
        newEnv.putAll(env);
        return newEnv;
    }

    private ByteArrayInputStream getInputStream() {
        return allInput != null ? new ByteArrayInputStream(allInput.getBytes()) : null;
    }

    private OutputStream getOutputStream() {
        return inputOut;
    }

    private OutputStream getOutputErrorStream() {
        return inputErrorOut;
    }

}
