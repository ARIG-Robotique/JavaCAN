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

import java.io.IOException;
import java.nio.ByteBuffer;

import static tel.schich.javacan.CanFrame.HEADER_LENGTH;
import static tel.schich.javacan.CanFrame.MAX_DATA_LENGTH;
import static tel.schich.javacan.CanFrame.MAX_FD_DATA_LENGTH;

public interface RawCanChannel extends CanChannel {
    int MTU = HEADER_LENGTH + MAX_DATA_LENGTH;
    int FD_MTU = HEADER_LENGTH + MAX_FD_DATA_LENGTH;

    RawCanChannel bind(CanDevice device) throws IOException;

    CanFrame read() throws IOException;
    CanFrame read(ByteBuffer buffer, int offset, int length) throws IOException;
    RawCanChannel write(CanFrame frame) throws IOException;
}
