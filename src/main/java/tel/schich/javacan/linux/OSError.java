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
package tel.schich.javacan.linux;

/**
 * This class represents an OS error with an ID and an error message.
 */
public class OSError {

    /**
     * Try again
     */
    public static final int EAGAIN = 11;


    public final int errorNumber;
    public final String errorMessage;

    /**
     * Creates a new instance with a given error number and message.
     * Instances of this class will be created from native code.
     * @param number the number
     * @param message the message
     */
    OSError(int number, String message) {
        this.errorNumber = number;
        this.errorMessage = message;
    }

    @Override
    public String toString() {
        return "OSError{" + "errorNumber=" + errorNumber + ", errorMessage='" + errorMessage + '\'' + '}';
    }

    private static boolean isTemporary(int errno) {
        switch (errno) {
            case EAGAIN:
                return true;
            default:
                return false;
        }
    }

    /**
     * Checks if the error suggests retrying as a solution.
     *
     * @return true if a retry might be a viable resolution of this error
     */
    public boolean mayTryAgain() {
        return isTemporary(errorNumber);
    }
}
