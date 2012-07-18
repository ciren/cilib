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
