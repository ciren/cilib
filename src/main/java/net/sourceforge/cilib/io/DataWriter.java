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

/**
 * Interface for a data writer: writes a data table object to a file.
 * @author andrich
 */
public interface DataWriter {

     /**
     * Prepares the writer for writing.
     * @throws CIlibIOException a wrapper exception for any type of IO exception
     * that might occur.
     */
    public void open() throws CIlibIOException;

    /**
     * Write a data table to a URL.
     * @param dataTable the data to write.
     * @throws net.sourceforge.cilib.io.exception.CIlibIOException
     */
    public void write(DataTable dataTable) throws CIlibIOException;

    /**
     * Properly terminates writer.
     */
    public void close();

    /**
     * Gets the URL that the data is written to.
     * @return the source data URL.
     */
    public String getDestinationURL();

    /**
     * Sets the URL that the data is written to.
     * @param sourceURL the source data URL.
     */
    public void setDestinationURL(String sourceURL);
}
