/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io;

import java.util.ArrayList;
import java.util.List;

/**
 * Class reads data from a local text based file. A row is a line in the text
 * file, the line is tokenized using the regular expression delimiter, the
 * resulting tokens form the columns of the row.
 */
public class TextFileReader extends FileReader<String> {

    /**
     * Default constructor. Initialises the delimiter to be a comma, i.e. the
     * class is a csv reader.
     */
    public TextFileReader() {
    }

    /**
     * Returns the next line in the file.
     * @return a tokenized line in the file.
     */
    @Override
    public String nextRow() {
        return this.nextLine();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<String> getColumnNames() {
        return new ArrayList<String>();
    }
}
