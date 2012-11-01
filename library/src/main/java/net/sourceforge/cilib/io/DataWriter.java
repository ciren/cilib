/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io;

import net.sourceforge.cilib.io.exception.CIlibIOException;

/**
 * Interface for a data writer: writes a data table object to a file.
 */
public interface DataWriter {

     /**
     * Prepares the writer for writing.
     * @throws CIlibIOException a wrapper exception for any type of IO exception
     * that might occur.
     */
    void open() throws CIlibIOException;

    /**
     * Write a data table to a URL.
     * @param dataTable the data to write.
     * @throws net.sourceforge.cilib.io.exception.CIlibIOException
     */
    void write(DataTable dataTable) throws CIlibIOException;

    /**
     * Properly terminates writer.
     */
    void close();

    /**
     * Gets the URL that the data is written to.
     * @return the source data URL.
     */
    String getDestinationURL();

    /**
     * Sets the URL that the data is written to.
     * @param sourceURL the source data URL.
     */
    void setDestinationURL(String sourceURL);
}
