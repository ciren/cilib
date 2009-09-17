/*
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.io;

import net.sourceforge.cilib.io.exception.CIlibIOException;
import java.util.List;

/**
 * An interface to an object that is capable of reading data from a source and
 * iterating over it in a row by row basis. This is essential as all input data
 * is assumed to be compatible with a table (which operates on rows).
 * @author andrich
 * @param <T> the type of the elements the row contains.
 */
public interface DataReader<T> {

    /**
     * Prepares the reader for reading.
     * @throws CIlibIOException a wrapper exception for any type of IO exception
     * that might occur.
     */
    public void open() throws CIlibIOException;

    /**
     * Returns the next row of data as a list.
     * @return the next row of data.
     */
    public T nextRow();

    /**
     * Checks whether the data source has a next row of data.
     * @return true if another row can be read.
     * @throws CIlibIOException a wrapper exception for any type of IO exception
     */
    public boolean hasNextRow() throws CIlibIOException;

    /**
     * Properly terminates reading.
     * @throws CIlibIOException a wrapper exception for any type of IO exception
     */
    public void close() throws CIlibIOException;

    /**
     * Gets the URL that the data is read from.
     * @return the source data URL.
     */
    public String getSourceURL();

    /**
     * Sets the URL that the data is read from.
     * @param sourceURL the source data URL.
     */
    public void setSourceURL(String sourceURL);

    /**
     * Gets the names of the columns as a list.
     * @return the column names.
     */
    public List<String> getColumnNames();

}
