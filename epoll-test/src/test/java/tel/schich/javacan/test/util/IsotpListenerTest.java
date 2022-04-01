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
package tel.schich.javacan.test.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tel.schich.javacan.CanChannels;
import tel.schich.javacan.IsotpCanChannel;
import tel.schich.javacan.IsotpSocketAddress;
import tel.schich.javacan.platform.linux.epoll.EPollSelector;
import tel.schich.javacan.test.CanTestHelper;
import tel.schich.javacan.util.CanUtils;
import tel.schich.javacan.util.IsotpListener;
import tel.schich.javacan.util.MessageHandler;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static tel.schich.javacan.IsotpAddress.*;

class IsotpListenerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(IsotpListenerTest.class);

    @Test
    void testBroker() throws Exception {
        ThreadFactory threadFactory = r -> {
            Thread t = new Thread(r);
            t.setName("broker-test-" + t.getName());
            return t;
        };

        IsotpSocketAddress addra = IsotpSocketAddress.isotpAddress(SFF_ECU_REQUEST_BASE + DESTINATION_ECU_1);
        IsotpSocketAddress addrb = IsotpSocketAddress.isotpAddress(SFF_ECU_RESPONSE_BASE + DESTINATION_ECU_1);

        try (IsotpListener broker = new IsotpListener(threadFactory, EPollSelector.open(), Duration.ofSeconds(5))) {
            try (IsotpCanChannel a = CanChannels.newIsotpChannel()) {
                try (IsotpCanChannel b = CanChannels.newIsotpChannel()) {
                    final ByteBuffer buf = IsotpCanChannel.allocateSufficientMemory();
                    final Lock lock = new ReentrantLock();
                    final Condition condition = lock.newCondition();

                    a.bind(CanTestHelper.CAN_INTERFACE, addra, addrb);
                    b.bind(CanTestHelper.CAN_INTERFACE, addrb, addra);

                    broker.addChannel(a, new PingPing(lock, condition, buf));
                    broker.addChannel(b, new PingPing(lock, condition, buf));

                    buf.put(randomByte());
                    buf.flip();
                    a.write(buf);

                    try {
                        lock.lock();
                        assertTrue(condition.await(20, TimeUnit.SECONDS), "The backing CAN socket should be fast enough to reach 4096 bytes within 20 seconds of ping-pong");
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }
    }

    private static byte randomByte() {
        return (byte) (Math.random() * 255);
    }

    private static final class PingPing implements MessageHandler {
        private final Lock lock;
        private final Condition condition;
        private final ByteBuffer buf;

        public PingPing(Lock lock, Condition condition, ByteBuffer buf) {
            this.lock = lock;
            this.condition = condition;
            this.buf = buf;
        }

        @Override
        public void handle(IsotpCanChannel ch, ByteBuffer buffer) {
            int length = buffer.remaining();
            if (length % 200 == 0) {
                LOGGER.debug(String.format("(%04d) -> %08X#%s%n", length, ch.getTxAddress().getId(), CanUtils.hexDump(buffer)));
            }
            buf.clear();
            buf.put(buffer);
            buf.put(randomByte());
            buf.flip();
            try {
                ch.write(buf);
            } catch (Exception e) {
                Assertions.assertEquals(IllegalArgumentException.class, e.getClass());
                LOGGER.debug("Failed to send message", e);
                lock.lock();
                try {
                    condition.signalAll();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
