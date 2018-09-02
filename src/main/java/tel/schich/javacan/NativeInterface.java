/*
 * The MIT License
 * Copyright © 2018 Phillip Schichtel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package tel.schich.javacan;

class NativeInterface {

    public static native long resolveInterfaceName(String interfaceName);

    public static native int createRawSocket();

    public static native int bindSocket(int sock, long interfaceId, int rx, int tx);

    public static native int close(int sock);

    public static native int errno();

    public static native String errstr(int errno);

    public static native int setBlockingMode(int sock, boolean block);

    public static native int getBlockingMode(int sock);

    public static native int setTimeouts(int sock, long read, long write);

    public static native long write(int sock, byte[] buf, int offset, int len);

    public static native long read(int sock, byte[] buf, int offset, int len);

    public static native int setFilters(int sock, byte[] data);

    @Deprecated
    public static native byte[] getFilters(int sock);

    public static native int setLoopback(int sock, boolean enable);

    public static native int getLoopback(int sock);

    public static native int setReceiveOwnMessages(int sock, boolean enable);

    public static native int getReceiveOwnMessages(int sock);

    public static native int setJoinFilters(int sock, boolean enable);

    public static native int getJoinFilters(int sock);

    public static native int setAllowFDFrames(int sock, boolean enable);

    public static native int getAllowFDFrames(int sock);

    public static native int setErrorFilter(int sock, int mask);

    public static native int getErrorFilter(int sock);

    public static native short poll(int sockFD, int events, int timeout);
}
