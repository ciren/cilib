/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.io;

import net.sourceforge.cilib.io.exception.CIlibIOException;
import java.util.List;

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
