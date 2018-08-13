package tel.schich.javacan;

import java.io.Closeable;
import java.io.IOException;

public class CanSocket implements Closeable {

    private final int fileDescriptor;

    public CanSocket() {
        fileDescriptor = makeFD();
    }

    private static int makeFD() {
        int fd = NativeInterface.createSocket();
        if (fd == -1) {
            throw new JavaCANException("Unable to create socket!", getLastError());
        }
        return fd;
    }

    public void bind(String interfaceName) {
        final long ifindex = NativeInterface.resolveInterfaceName(interfaceName);
        if (ifindex == 0) {
            throw new JavaCANException("Unknown interface: " + interfaceName, getLastError());
        }

        final int result = NativeInterface.bindSocket(fileDescriptor, ifindex);
        if (result == -1) {
            throw new JavaCANException("Unable to bind!", getLastError());
        }
    }

    public void setBlockingMode(boolean block) {
        if (NativeInterface.setBlockingMode(fileDescriptor, block) == -1) {
            throw new JavaCANException("Unable to set the blocking mode!", getLastError());
        }
    }

    public boolean getBlockingMode() {
        final int result = NativeInterface.getBlockingMode(fileDescriptor);
        if (result == -1) {
            throw new JavaCANException("Unable to get blocking mode!", getLastError());
        }
        return result == 1;
    }

    public boolean setTimeouts(long read, long write) {
        return NativeInterface.setTimeouts(fileDescriptor, read, write);
    }

    public void setLoopback(boolean loopback) {
        final int result = NativeInterface.setLoopback(fileDescriptor, loopback);
        if (result == -1) {
            throw new JavaCANException("Unable to set loopback state!", getLastError());
        }
    }

    public boolean getLoopback() {
        final int result = NativeInterface.getLoopback(fileDescriptor);
        if (result == -1) {
            throw new JavaCANException("Unable to get loopback state!", getLastError());
        }
        return result == 1;
    }

    public void setFilters(CanFilter... filters) {
        int[] ids = new int[filters.length];
        int[] masks = new int[filters.length];

        NativeInterface.setFilter(fileDescriptor, ids, masks);
    }

    public CanFrame read() {
        CanFrame frame = NativeInterface.read(fileDescriptor);
        if (frame == null) {
            throw new JavaCANException("Unable to read a frame!", getLastError());
        }
        return frame;
    }

    public boolean write(CanFrame frame) {
        return NativeInterface.write(fileDescriptor, frame);
    }

    public boolean shutdown() {
        return shutdown(true, true);
    }

    public boolean shutdown(boolean read, boolean write) {
        return NativeInterface.shutdown(fileDescriptor, read, write);
    }

    public void close() throws IOException {
        shutdown();
        NativeInterface.closeSocket(fileDescriptor);
    }

    private static NativeError getLastError() {
        int lastErrno = NativeInterface.errno();
        if (lastErrno == 0) {
            return null;
        }
        String lastErrstr = NativeInterface.errstr(lastErrno);

        return new NativeError(lastErrno, lastErrstr);
    }

    public static class NativeError {
        public final int errorNumber;
        public final String errorMessage;

        public NativeError(int errorNumber, String errorMessage) {
            this.errorNumber = errorNumber;
            this.errorMessage = errorMessage;
        }

        @Override
        public String toString() {
            return "NativeError{" + "errorNumber=" + errorNumber + ", errorMessage='" + errorMessage + '\'' + '}';
        }
    }
}
