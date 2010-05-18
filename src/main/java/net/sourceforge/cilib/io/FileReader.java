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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class represents a {@link DataReader DataReader} object that reads its data from
 * a local file. The type of file is not restricted.
 * @param <T> The data type that will be read from the file.
 * @author andrich
 */
public abstract class FileReader<T> implements DataReader<T> {

    protected File file;
    private BufferedReader reader;
    private String previousReadLine;

    /**
     * Initialises the lineReader from a file channel constructed off a file input
     * stream.
     * @throws CIlibIOException {@inheritDoc }
     */
    @Override
    public void open() throws CIlibIOException {
        if (file == null) {
            throw new CIlibIOException("Source URL not set.");
        }
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(this.file);
            reader = new BufferedReader(new InputStreamReader(stream));
        } catch (IOException ex) {
            throw new CIlibIOException(ex);
        }
    }

    /**
     * Whether there are still lines left to be read.
     * @return true if there are lines left.
     */
    @Override
    public boolean hasNextRow() throws CIlibIOException {
        if (previousReadLine != null) {
            return true;
        }
        try {
            previousReadLine = reader.readLine();
            if (previousReadLine == null) {
                return false;
            }
        } catch (IOException ex) {
            throw new CIlibIOException(ex);
        }
        return true;
    }

    public String nextLine() {
        try {
            if (previousReadLine != null) {
                String tmp = previousReadLine;
                previousReadLine = null;
                return tmp;
            }
            return reader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Closes the lineReader (which in turn closes all wrapped channels and streams).
     */
    @Override
    public void close() throws CIlibIOException {
        try {
            reader.close();
        } catch (IOException ex) {
            throw new CIlibIOException(ex);
        } finally {
            reader = null;
        }
    }

    /**
     * Get the absolute path of the file local to the machine.
     * @return the file path.
     */
    @Override
    public String getSourceURL() {
        return file.getAbsolutePath();
    }

    /**
     * Constructs a new file using the passed parameter as the pathname.
     * @param sourceURL the location of the file.
     */
    @Override
    public void setSourceURL(String sourceURL) {
        file = new File(sourceURL);
    }

    /**
     * Gets the File instance.
     * @return the file instance (possibly null).
     */
    public File getFile() {
        return file;
    }

    /**
     * Sets the file instance.
     * @param file the new file instance.
     */
    public void setFile(File file) {
        this.file = file;
    }
}
