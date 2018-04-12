package com.datatrees.crawler.core.processor.proxy;

import java.util.concurrent.atomic.AtomicInteger;

import com.treefinance.toolkit.util.RegExp;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月5日 下午11:28:49
 */
public class Proxy {

    private static final String proxyPattern = ("^([\\d]+\\.){3}[\\d]+:[\\d]+$");
    private final String host;
    private final int    port;
    private final long   timestamp;
    // 代理共享数(用于子任务),默认为1
    private AtomicInteger shareCount = new AtomicInteger(1);

    public Proxy(String host, int port) {
        this.host = host;
        this.port = port;
        timestamp = 0l;
    }

    public Proxy(String host, int port, long timestamp) {
        this.host = host;
        this.port = port;
        this.timestamp = timestamp;
    }

    public static Proxy parse(String proxy) {
        return parse(proxy, 0);
    }

    public static Proxy parse(String proxy, long timestamp) {
        Proxy result = null;
        if (RegExp.find(proxy, proxyPattern)) {
            String[] rss = proxy.split(":");
            int port = Integer.parseInt(rss[1]);
            result = new Proxy(rss[0].trim(), port, timestamp);
        }
        return result;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String format() {
        return host + ":" + port;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @return the shareCount
     */
    public AtomicInteger getShareCount() {
        return shareCount;
    }

    /**
     * @param shareCount the shareCount to set
     */
    public void setShareCount(AtomicInteger shareCount) {
        this.shareCount = shareCount;
    }

    @Override
    public String toString() {
        return "Proxy{ host = " + host + ", port = " + port + ", timestamp = " + timestamp + ", shareCount = " + shareCount.get() + "}";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((host == null) ? 0 : host.hashCode());
        result = prime * result + port;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Proxy other = (Proxy) obj;
        if (host == null) {
            if (other.host != null) return false;
        } else if (!host.equals(other.host)) return false;
        if (port != other.port) return false;
        return true;
    }
}
