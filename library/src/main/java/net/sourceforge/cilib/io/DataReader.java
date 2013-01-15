/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io;

import java.util.List;
import net.sourceforge.cilib.io.exception.CIlibIOException;

/**
 * An interface to an object that is capable of reading data from a source and
 * iterating over it in a row by row basis. This is essential as all input data
 * is assumed to be compatible with a table (which operates on rows).
 * @param <T> the type of the elements the row contains.
 */
public interface DataReader<T> {

    /**
     * Prepares the reader for reading.
     * @throws CIlibIOException a wrapper exception for any type of IO exception
     * that might occur.
     */
    void open() throws CIlibIOException;

    /**
     * Returns the next row of data as a list.
     * @return the next row of data.
     */
    T nextRow();

    /**
     * Checks whether the data source has a next row of data.
     * @return true if another row can be read.
     * @throws CIlibIOException a wrapper exception for any type of IO exception
     */
    boolean hasNextRow() throws CIlibIOException;

    /**
     * Properly terminates reading.
     * @throws CIlibIOException a wrapper exception for any type of IO exception
     */
    void close() throws CIlibIOException;

    /**
     * Gets the URL that the data is read from.
     * @return the source data URL.
     */
    String getSourceURL();

    /**
     * Sets the URL that the data is read from.
     * @param sourceURL the source data URL.
     */
    void setSourceURL(String sourceURL);

    /**
     * Gets the names of the columns as a list.
     * @return the column names.
     */
    List<String> getColumnNames();

}
