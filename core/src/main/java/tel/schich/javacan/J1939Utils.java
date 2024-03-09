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

/**
 * This class provides utility functions to work with J1939 messages
 */
public class J1939Utils {
    private J1939Utils() {
    }

    /**
     * Extracts the PGN from a raw EFF CAN ID.
     *
     * @param canId the raw EFF CAN ID
     * @return the PGN
     */
    public static int parameterGroupNumberFromRawAddress(int canId) {
        return (canId >>> 8) & 0b0011_1111_1111_1111_1111;
    }

    /**
     * Extracts the source address from a raw EFF CAN ID.
     *
     * @param canId the raw EFF CAN ID
     * @return the source address
     */
    public static byte sourceAddressFromRawAddress(int canId) {
        return (byte)(canId & 0b1111_1111);
    }

    /**
     * Extracts the priority from a raw EFF CAN ID.
     *
     * @param canId the raw EFF CAN ID
     * @return the priority
     */
    public static byte priorityFromRawAddress(int canId) {
        return (byte)((canId >>> 26) & 0b0111);
    }
}
