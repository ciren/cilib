/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.dataset;

import java.io.InputStream;
import net.sourceforge.cilib.util.Cloneable;

/**
 * This interface provides an abstraction for accessing data sets. The underlying data set can be
 * accessed using either an <code>InputStream</code> or a <code>byte[]</code>
 */
public abstract class DataSet implements Cloneable {
    private static final long serialVersionUID = 5190227337412349440L;

    @Deprecated
    protected String patternExpression = null;

    public DataSet() {
        patternExpression = "";
    }

    public DataSet(DataSet rhs) {
        patternExpression = new String(rhs.patternExpression);
    }

    public abstract DataSet getClone();

    /**
     * Returns the data set as a byte array.
     * @return the data set as a <code>byte[]</code>
     */
    public abstract byte[] getData();

    /**
     * Returns the data set as an input stream.
     * @return the data set as a <code>InputStream</code>
     */
    public abstract InputStream getInputStream();

    /**
     * Set the regular expression that will be used to split the patterns in the provided DataSet
     * file. The format of this regular expression depends on where you are calling the method from.
     * When you specify the regular expression in a simulation XML file, the format should be a
     * standard regular expression. When you call this method directly with a regular expression in
     * double quotes (from a Java source file), then the format of the regular expression should be a
     * Java style regular expression.
     * @param regexp The regex to use
     */
    public void setPatternExpression(String regexp) {
        patternExpression = regexp;
    }

    /**
     * Get the regular expression that has been set for this DataSet.
     * @return The regular expression.
     */
    public String getPatternExpression() {
        return patternExpression;
    }
}
