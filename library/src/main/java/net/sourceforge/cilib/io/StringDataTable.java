/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 */
public class StringDataTable implements DataTable<String, String> {
    private static final long serialVersionUID = 3309485547124815669L;

    private ArrayList<String> strings;
    private String shortestString;
    private String longestString;

    public StringDataTable() {
        this.strings = new ArrayList<String>();
    }

    public StringDataTable(StringDataTable rhs) {
        strings = new ArrayList<String>();
        for (String string : rhs.strings) {
            strings.add(new String(string));
        }
        shortestString = new String(rhs.shortestString);
        longestString = new String(rhs.longestString);
    }

    @Override
    public StringDataTable getClone() {
        return new StringDataTable(this);
    }

    /*@Override
    public void initialise() {

        for (Iterator<DataSet> i = this.iterator(); i.hasNext();) {
            DataSet dataSet = i.next();

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(dataSet.getInputStream()));
                String result = in.readLine();

                while (result != null) {
                    strings.add(result);

                    result = in.readLine();
                }
            }
            catch (IOException ioException) {
                throw new RuntimeException(ioException);
            }
        }
    }*/

    public ArrayList<String> getStrings() {
        return this.strings;
    }

    public String getShortestString() {
        if (this.shortestString == null)
            calculateStringLengths();

        return this.shortestString;
    }

    public String getLongestString() {
        if (this.longestString == null)
            calculateStringLengths();

        return this.longestString;
    }

    /**
     * Iterate through all the strings and determine the length of the longest and shortest strings.
     * If the strings are all the same length, then the <tt>shortestLength</tt> will equal the
     * <tt>longestLength</tt>.
     */
    private void calculateStringLengths() {
        int shortestLength = Integer.MAX_VALUE;
        int longestLength = Integer.MIN_VALUE;

        for (String str : strings) {
            int length = str.length();

            if (length < shortestLength) {
                shortestLength = length;
                this.shortestString = str;
            }

            if (length > longestLength) {
                longestLength = length;
                this.longestString = str;
            }
        }
    }

    @Override
    public int size() {
        return this.strings.size();
    }

    @Override
    public void addRow(String rowData) {
        this.strings.add(rowData);
    }

    @Override
    public void addColumn(String columnData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addRows(List<String> multipleRowData) {
        this.strings.addAll(multipleRowData);
    }

    @Override
    public void addColumns(List<String> multipleColumnData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String removeRow(int index) {
        return this.strings.remove(index);
    }

    @Override
    public String getRow(int index) {
        return this.strings.get(index);
    }

    @Override
    public void setRow(int index, String rowData) {
        this.strings.set(index, rowData);
    }

    @Override
    public String getColumn(int index) {
        return this.strings.get(index);
    }

    @Override
    public void setColumn(int index, String columnData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getColumnName(int index) {
        return "";
    }

    @Override
    public void setColumnName(int index, String name) {
    }

    @Override
    public List<String> getColumnNames() {
        return new ArrayList();
    }

    @Override
    public void setColumnNames(List<String> names) {
    }

    @Override
    public void clear() {
        this.strings.clear();
    }

    @Override
    public int getNumRows() {
        return this.strings.size();
    }

    @Override
    public int getNumColums() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator<String> iterator() {
        return this.strings.iterator();
    }
}
