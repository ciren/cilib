/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import net.sourceforge.cilib.io.exception.CIlibIOException;

/**
 * Class represents a {@link DataReader DataReader} object that reads its data from
 * a local file. The type of file is not restricted.
 * @param <T> The data type that will be read from the file.
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
