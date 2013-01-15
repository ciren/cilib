/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import net.sourceforge.cilib.io.exception.CIlibIOException;

/**
 * Abstract data writer that writes data to a local file.
 */
public abstract class FileWriter implements DataWriter {

    protected File file;
    protected ByteBuffer outputBuffer;
    protected FileChannel outputChannel;
    private int outputBufferSize;

    /**
     * Default constructor. Initialises the output buffer to a default size.
     */
    public FileWriter() {
        outputBufferSize = 32 * IOUtils.Block.MEGABYTE.size();
    }

    /**
     * Opens the output channel for writing.
     * @throws net.sourceforge.cilib.io.exception.CIlibIOException {@inheritDoc }
     */
    @Override
    public void open() throws CIlibIOException {
        try {
            FileOutputStream stream = new FileOutputStream(file);
            outputChannel = stream.getChannel();
            outputBuffer = ByteBuffer.allocate(outputBufferSize);
        } catch (IOException ex) {
            throw new CIlibIOException(ex);
        }
    }

    /**
     * Closes the output channel that was used for writing.
     */
    @Override
    public void close() {
        try {
            outputChannel.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets the destination URL that the data is written to as the absolute
     * path of the wrapped file.
     * @return the destination URL.
     */
    @Override
    public String getDestinationURL() {
        return file.getAbsolutePath();
    }

    /**
     * Sets the destination URL that the data is written to, constructs the
     * wrapped file from the URL.
     * @param destinationURL
     */
    @Override
    public void setDestinationURL(String destinationURL) {
        this.file = new File(destinationURL);
    }

    /**
     * Gets the wrapped File object.
     * @return the file object.
     */
    public File getFile() {
        return file;
    }

    /**
     * Sets the wrapped File object.
     * @param file the file object.
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Gets the output buffer that data is written to.
     * @return the output buffer.
     */
    public ByteBuffer getOutputBuffer() {
        return outputBuffer;
    }

    /**
     * Sets the output buffer that data is written to.
     * @param outputBuffer the output buffer.
     */
    public void setOutputBuffer(ByteBuffer outputBuffer) {
        this.outputBuffer = outputBuffer;
    }

    /**
     * Gets the size of the output buffer used.
     * @return the size of the output buffer.
     */
    public int getOutputBufferSize() {
        return outputBufferSize;
    }

    /**
     * Sets the size of the output buffer used.
     * @param outputBufferSize the size of the output buffer.
     */
    public void setOutputBufferSize(int outputBufferSize) {
        this.outputBufferSize = outputBufferSize;
    }

    /**
     * Gets the output channel used in conjunction with the output buffer to write
     * data.
     * @return the output channel.
     */
    public FileChannel getOutputChannel() {
        return outputChannel;
    }

    /**
     * Sets the output channel used in conjunction with the output buffer to write
     * data.
     * @param outputChannel the output channel.
     */
    public void setOutputChannel(FileChannel outputChannel) {
        this.outputChannel = outputChannel;
    }
}
