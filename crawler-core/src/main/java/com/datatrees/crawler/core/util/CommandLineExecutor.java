package com.datatrees.crawler.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.datatrees.common.protocol.util.CharsetUtil;
import com.google.common.collect.Maps;
import org.apache.commons.exec.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.collect.ImmutableMap.copyOf;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月6日 上午12:22:34
 */
public class CommandLineExecutor {

    private static final Logger                      log           = LoggerFactory.getLogger(CommandLineExecutor.class);
    private final        ByteArrayOutputStream       inputOut      = new ByteArrayOutputStream();
    private final        ByteArrayOutputStream       inputErrorOut = new ByteArrayOutputStream();
    private final        DefaultExecuteResultHandler handler       = new DefaultExecuteResultHandler();
    private final        Executor                    executor      = new DefaultExecutor();
    private final org.apache.commons.exec.CommandLine cl;
    private final Map<String, String> env = new ConcurrentHashMap<String, String>();
    private volatile String allInput;
    private ExecuteWatchdog executeWatchdog = new ExecuteWatchdog(1000 * 60 * 10);

    public CommandLineExecutor(String executable, String... args) {
        cl = new org.apache.commons.exec.CommandLine(executable);
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

    private Map<String, String> getMergedEnv() {
        HashMap<String, String> newEnv = Maps.newHashMap(System.getenv());
        newEnv.putAll(env);
        return newEnv;
    }

    private ByteArrayInputStream getInputStream() {
        return allInput != null ? new ByteArrayInputStream(allInput.getBytes()) : null;
    }

    public void executeAsync() throws Exception {
        final OutputStream outputStream = getOutputStream();
        executor.setWatchdog(executeWatchdog);
        executor.setStreamHandler(new PumpStreamHandler(outputStream, getOutputErrorStream(), getInputStream()));
        executor.execute(cl, getMergedEnv(), handler);
    }

    private OutputStream getOutputStream() {
        return inputOut;
    }

    private OutputStream getOutputErrorStream() {
        return inputErrorOut;
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

}