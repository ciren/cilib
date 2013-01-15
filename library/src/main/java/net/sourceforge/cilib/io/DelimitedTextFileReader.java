/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.type.types.StringType;

/**
 * Class reads data from a local text based file. A row is a line in the text file,
 * the line is tokenized using the regular expression delimiter {@link #delimiter delimiter},
 * the resulting tokens form the columns of the row.
 */
public class DelimitedTextFileReader extends FileReader<List<StringType>> {

    private String delimiter;

    /** Default constructor. Initialises the delimiter to be a comma, i.e.
     * the class is a csv reader.
     */
    public DelimitedTextFileReader() {
        delimiter = "\\,";
    }

    /**
     * Returns the next line in the file.
     * @return a tokenized line in the file.
     */
    @Override
    public List<StringType> nextRow() {
        try {
            this.hasNextRow();
            String line = this.nextLine();
            String[] tokens = line.split(delimiter);
            List<StringType> result = new ArrayList<StringType>(tokens.length);
            for (String token : tokens) {
                result.add(new StringType(token));
            }
            return result;
        } catch (CIlibIOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the regular expression used to tokenize lines.
     * @return the delimiting regular expression.
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * Sets the regular expression used to tokenize lines.
     * @param delimiter the new delimiting regular expression.
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<String> getColumnNames() {
        return new ArrayList<String>();
    }


}
