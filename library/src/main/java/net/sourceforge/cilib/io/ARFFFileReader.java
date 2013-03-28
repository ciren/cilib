/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.Type;

/**
 * An implementation of an ARFF file reader according to:
 * {@code http://www.cs.waikato.ac.nz/~ml/weka/arff.html}. Currently the reader
 * does not support the DATE format as no CIlib equivalent type exists.
 */
public class ARFFFileReader extends FileReader<List<Type>> {

    private ArrayList<Type> columnTypePrototypes;
    private ArrayList<String> columnNames;
    private String relationName;
    private String headerDelimiter;
    private String dataDelimiter;
    private String commentPrefix;
    private HashMap<Integer, HashMap<String, Integer>> columnToNominalAttributesMap;

    /**
     * Initiates the reader, sets the default delimiters to a space (for header
     * fields) and comma (for data fields).
     * @throws CIlibIOException
     * {@inheritDoc}
     */
    @Override
    public void open() throws CIlibIOException {
        super.open();
        headerDelimiter = "[\\s]+";
        dataDelimiter = "\\,";
        commentPrefix = "%";
        columnTypePrototypes = new ArrayList<Type>();
        columnNames = new ArrayList<String>();
        this.processHeader();
    }

    /**
     * Reads and returns the next data row in the file as a list of type
     * {@link Type}. Each line read is assumed to be a row and the tokens parsed
     * (by using the {@link #dataDelimiter dataDelimiter}) are best-effort
     * fashion according to the data header.
     * @return a list containing the row data.
     */
    @Override
    public List<Type> nextRow() {
        String line = this.nextLine();
        String[] tokens = line.split(dataDelimiter);
        if (tokens.length != columnTypePrototypes.size()) {
            throw new UnsupportedOperationException("Error: Not all attributes specified." +
                    "Expected @attribute declaration for each column");
        }
        ArrayList<Type> row = new ArrayList<Type>();
        for (int i = 0; i < tokens.length; i++) {
            String data = tokens[i];
            row.add(this.mapTokenToType(i, data));
        }
        return row;
    }

    /**
     * Processes the header to determine the required type information.
     * @throws CIlibIOException
     * {@inheritDoc}
     */
    private void processHeader() throws CIlibIOException {
        String line = getNextLineIgnoreComments();
        String[] tokens;
        if (line.toUpperCase().startsWith("@RELATION")) {
            tokens = line.split(headerDelimiter);
            if (tokens.length < 2) {
                throw new CIlibIOException("@RELATION declaration must be followed" +
                        "by whitespace and relation name.");
            }
            relationName = tokens[1];
        }

        int columnCount = 0;
        line = getNextLineIgnoreComments();
        while (!line.equalsIgnoreCase("@DATA")) {
            tokens = line.split(headerDelimiter);
            if (!tokens[0].equalsIgnoreCase("@ATTRIBUTE")) {
                throw new CIlibIOException("Expected @ATTRIBUTE declaration, found: " + tokens[0]);
            }
            if (tokens.length < 3) {
                throw new CIlibIOException("@ATTRIBUTE declaration must be followed" +
                        "by <attribute-name> <datatype>");
            }
            String attributeName = tokens[1];
            columnNames.add(attributeName);
            Type type = getTypeData(columnCount, tokens[2]);
            columnTypePrototypes.add(type);
            line = getNextLineIgnoreComments();
            columnCount++;
        }
    }

    /**
     * Gets the next line, ignoring comment lines. As determined by the comment
     * prefix.
     * @return the next non-empty non-comment line.
     */
    private String getNextLineIgnoreComments() {
        String line = this.nextLine();
        while (line.startsWith(commentPrefix) || line.isEmpty()) {
            line = this.nextLine();
        }
        return line.trim();
    }

    /**
     * Given the datatype key, determines the datatype for a given column. Also
     * sets up the HashTables to map a nominal attribute to a corresponding integer
     * number.
     * @param columnn the column to map to a datatype.
     * @param datatype the datatype key.
     * @return a corresponding CIlib type.
     * @throws CIlibIOException
     * {@inheritDoc}
     */
    private Type getTypeData(int columnn, String datatype) throws CIlibIOException {
        if (datatype.equalsIgnoreCase("NUMERIC")) {
            return Real.valueOf(0.0);
        }
        if (datatype.equalsIgnoreCase("STRING")) {
            return new StringType("");
        }
        if (datatype.equalsIgnoreCase("DATE")) {
            throw new UnsupportedOperationException("Date format currently not supported" +
                    " in CIlib.");
        }

        //If none of the above, has to be a nominal attribute.
        if (columnToNominalAttributesMap == null) {
            columnToNominalAttributesMap = new HashMap<Integer, HashMap<String, Integer>>();
        }
        HashMap<String, Integer> nominalMap = new HashMap<String, Integer>();
        datatype = datatype.replaceAll("[{}]", "");
        String[] nominalAttributes = datatype.split("\\,");
        if (nominalAttributes.length == 0) {
            throw new CIlibIOException("Nominal attributes must be comma separated:" +
                    "{<nominal-name1>, <nominal-name2>, <nominal-name3>, ...} ");
        }
        for (int i = 0; i < nominalAttributes.length; i++) {
            String nominalAttribute = nominalAttributes[i];
            nominalMap.put(nominalAttribute, i);
        }
        columnToNominalAttributesMap.put(columnn, nominalMap);
        return Int.valueOf(0);
    }

    /**
     * Puts a token into a new object of the correct CIlib type.
     * @param index the index of the token in the row (its column).
     * @param token the token to be typed.
     * @return a new CIlib object of the correct type.
     */
    private Type mapTokenToType(int index, String token) {
        Type type = columnTypePrototypes.get(index);
        if (type instanceof Real) {
            return Real.valueOf(Double.parseDouble(token));
        }

        if (type instanceof StringType) {
            return new StringType(token);
        }

        //If none of the above, has to be a nominal attribute.
        HashMap<String, Integer> nominalMap = this.columnToNominalAttributesMap.get(index);
        return Int.valueOf(nominalMap.get(token));
    }

    /**
     * Gets the delimiter used to separate fields in the file's header.
     * @return the header delimiter.
     */
    public String getHeaderDelimiter() {
        return headerDelimiter;
    }

    /**
     * Sets the delimiter used to separate fields in the file's header.
     * @param headerDelimiter the header delimiter.
     */
    public void setHeaderDelimiter(String headerDelimiter) {
        this.headerDelimiter = headerDelimiter;
    }

    /**
     * The name of the ARFF relation (dataset).
     * @return the relation's name.
     */
    public String getRelationName() {
        return relationName;
    }

    /**
     * Sets the ARFF relation's name.
     * @param relationName the relation's name.
     */
    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    /**
     * Gets the names of the columns of the dataset.
     * @return the column's names.
     */
    @Override
    public ArrayList<String> getColumnNames() {
        return columnNames;
    }

    /**
     * Sets the names of the columns of the dataset.
     * @param columnNames the column's names.
     */
    public void setColumnNames(ArrayList<String> columnNames) {
        this.columnNames = columnNames;
    }

    /**
     * Gets the delimiter used to identify fields in the data.
     * @return the delimiter used in the data section of the file.
     */
    public String getDataDelimiter() {
        return dataDelimiter;
    }

    /**
     * Sets the delimiter used to identify fields in the data.
     * @param dataDelimiter the delimiter used in the data section of the file.
     */
    public void setDataDelimiter(String dataDelimiter) {
        this.dataDelimiter = dataDelimiter;
    }

    /**
     * For a specific nominal integer value in a nominal attribute column, this
     * method retrieves the value's corresponding nominal string representation.
     * @param column the column of the nominal attribute.
     * @param nominalKey the integer nominal key.
     * @return the string nominal value.
     */
    public String getNominalString(int column, Type nominalKey) {
        int nominalKeyInt;
        try {
            nominalKeyInt = ((Int) nominalKey).intValue();
        } catch (ClassCastException ex) {
            throw new ClassCastException("Nominal key must be CIlib Int object.");
        }
        Set<Entry<String, Integer>> nominalKeySet = this.columnToNominalAttributesMap.get(column).entrySet();
        for (Entry<String, Integer> entry : nominalKeySet) {
            if (entry.getValue().compareTo(nominalKeyInt) == 0) {
                return entry.getKey();
            }
        }
        return "Nominal Key not found.";
    }
}
