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

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipException;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Mar 28, 2014 2:05:16 PM
 */
public class CustomInflaterInputStream extends InflaterInputStream {

    /**
     * Decompressor for this stream.
     */
    protected Inflater inf;

    /**
     * Input buffer for decompression.
     */
    protected byte[] buf;

    /**
     * Length of input buffer.
     */
    protected int len;

    boolean usesDefaultInflater = false;

    private boolean closed = false;

    // this flag is set to true after EOF has reached
    private boolean reachEOF = false;

    private byte[] singleByteBuf = new byte[1];

    private byte[] b = new byte[512];

    /**
     * Creates a new input stream with the specified decompressor and buffer size.
     * 
     * @param in the input stream
     * @param inf the decompressor ("inflater")
     * @param size the input buffer size
     * @exception IllegalArgumentException if size is <= 0
     */
    public CustomInflaterInputStream(InputStream in, Inflater inf, int size) {
        super(in);
        if (in == null || inf == null) {
            throw new NullPointerException();
        } else if (size <= 0) {
            throw new IllegalArgumentException("buffer size <= 0");
        }
        this.inf = inf;
        buf = new byte[size];
    }

    /**
     * Creates a new input stream with the specified decompressor and a default buffer size.
     * 
     * @param in the input stream
     * @param inf the decompressor ("inflater")
     */
    public CustomInflaterInputStream(InputStream in, Inflater inf) {
        this(in, inf, 512);
    }

    /**
     * Creates a new input stream with a default decompressor and buffer size.
     * 
     * @param in the input stream
     */
    public CustomInflaterInputStream(InputStream in) {
        this(in, new Inflater());
        usesDefaultInflater = true;
    }

    /**
     * Reads a byte of uncompressed data. This method will block until enough input is available for decompression.
     *
     * @return the byte read, or -1 if end of compressed input is reached
     * @exception IOException if an I/O error has occurred
     */
    @Override
    public int read() throws IOException {
        ensureOpen();
        return read(singleByteBuf, 0, 1) == -1 ? -1 : singleByteBuf[0] & 0xff;
    }

    /**
     * Reads uncompressed data into an array of bytes. If <code>len</code> is not zero, the method will block until some
     * input can be decompressed; otherwise, no bytes are read and <code>0</code> is returned.
     *
     * @param b the buffer into which the data is read
     * @param off the start offset in the destination array <code>b</code>
     * @param len the maximum number of bytes read
     * @return the actual number of bytes read, or -1 if the end of the compressed input is reached or a preset
     *         dictionary is needed
     * @exception NullPointerException If <code>b</code> is <code>null</code>.
     * @exception IndexOutOfBoundsException If <code>off</code> is negative, <code>len</code> is negative, or
     *            <code>len</code> is greater than <code>b.length - off</code>
     * @exception ZipException if a ZIP format error has occurred
     * @exception IOException if an I/O error has occurred
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        ensureOpen();
        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }
        try {
            int n;
            while ((n = inf.inflate(b, off, len)) == 0) {
                if (inf.finished() || inf.needsDictionary()) {
                    reachEOF = true;
                    return -1;
                }
                if (inf.needsInput()) {
                    fill();
                }
            }
            return n;
        } catch (DataFormatException e) {
            String s = e.getMessage();
            // ignore error
            // throw new ZipException(s != null ? s : "Invalid ZLIB data format");
        }
        return 0;
    }

    /**
     * Returns 0 after EOF has been reached, otherwise always return 1.
     * <p>
     * Programs should not count on this method to return the actual number of bytes that could be read without
     * blocking.
     *
     * @return 1 before EOF and 0 after EOF.
     * @exception IOException if an I/O error occurs.
     */
    @Override
    public int available() throws IOException {
        ensureOpen();
        if (reachEOF) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * Skips specified number of bytes of uncompressed data.
     *
     * @param n the number of bytes to skip
     * @return the actual number of bytes skipped.
     * @exception IOException if an I/O error has occurred
     * @exception IllegalArgumentException if n < 0
     */
    @Override
    public long skip(long n) throws IOException {
        if (n < 0) {
            throw new IllegalArgumentException("negative skip length");
        }
        ensureOpen();
        int max = (int)Math.min(n, Integer.MAX_VALUE);
        int total = 0;
        while (total < max) {
            int len = max - total;
            if (len > b.length) {
                len = b.length;
            }
            len = read(b, 0, len);
            if (len == -1) {
                reachEOF = true;
                break;
            }
            total += len;
        }
        return total;
    }

    /**
     * Closes this input stream and releases any system resources associated with the stream.
     *
     * @exception IOException if an I/O error has occurred
     */
    @Override
    public void close() throws IOException {
        if (!closed) {
            if (usesDefaultInflater) {
                inf.end();
            }
            in.close();
            closed = true;
        }
    }

    /**
     * Tests if this input stream supports the <code>mark</code> and <code>reset</code> methods. The
     * <code>markSupported</code> method of <code>InflaterInputStream</code> returns <code>false</code>.
     *
     * @return a <code>boolean</code> indicating if this stream type supports the <code>mark</code> and
     *         <code>reset</code> methods.
     * @see InputStream#mark(int)
     * @see InputStream#reset()
     */
    @Override
    public boolean markSupported() {
        return false;
    }

    /**
     * Marks the current position in this input stream.
     * <p>
     * The <code>mark</code> method of <code>InflaterInputStream</code> does nothing.
     *
     * @param readlimit the maximum limit of bytes that can be read before the mark position becomes invalid.
     * @see InputStream#reset()
     */
    @Override
    public synchronized void mark(int readlimit) {}

    /**
     * Repositions this stream to the position at the time the <code>mark</code> method was last called on this input
     * stream.
     * <p>
     * The method <code>reset</code> for class <code>InflaterInputStream</code> does nothing except throw an
     * <code>IOException</code>.
     *
     * @exception IOException if this method is invoked.
     * @see InputStream#mark(int)
     * @see IOException
     */
    @Override
    public synchronized void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }

    /**
     * Fills input buffer with more data to decompress.
     *
     * @exception IOException if an I/O error has occurred
     */
    @Override
    protected void fill() throws IOException {
        ensureOpen();
        len = in.read(buf, 0, buf.length);
        if (len == -1) {
            throw new EOFException("Unexpected end of ZLIB input stream");
        }
        inf.setInput(buf, 0, len);
    }

    /**
     * Check to make sure that this stream has not been closed
     */
    private void ensureOpen() throws IOException {
        if (closed) {
            throw new IOException("Stream closed");
        }
    }
}
