/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io.exception;

/**
 * Class represent an Exception that occurs during data I/O in CIlib. As the data
 * source is unknown, exceptions might range from simple IOExceptions to SAXExceptions
 * or SQL Exceptions, therefore the super class is simply Exception.
 */
public class CIlibIOException extends Exception {

    public CIlibIOException(Throwable cause) {
        super(cause);
    }

    public CIlibIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public CIlibIOException(String message) {
        super(message);
    }

    public CIlibIOException() {
    }
}
