/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.TypeList;

/**
 * Class implements a DataTable, where each row is a StandardPattern. The last column
 * of the row is the classification.
 */
public class StandardPatternDataTable implements DataTable<StandardPattern, TypeList> {

    private List<StandardPattern> dataTable;
    private HashMap<Integer,String> columnNames;

    /**
     * Default constructor.
     */
    public StandardPatternDataTable() {
        dataTable = new ArrayList<StandardPattern>();
        columnNames = new HashMap<Integer,String>();
    }

    /**
     * Copy constructor.
     * @param orig the StandardPatternDataTable to copy.
     */
    public StandardPatternDataTable(StandardPatternDataTable orig) {
        dataTable = new ArrayList<StandardPattern>(orig.getNumRows());
        columnNames = new HashMap<Integer,String>();
        for (StandardPattern row : orig) {
            dataTable.add((StandardPattern)row.getClone());
        }
        int size = orig.getNumColums();
        for (int i = 0; i < size; i++) {
            this.setColumnName(i, orig.getColumnName(i));
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void addRow(StandardPattern rowData) {
        dataTable.add((StandardPattern) rowData.getClone());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void addColumn(TypeList columnData) {
        throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void addRows(List<StandardPattern> multipleRowData) {
        for (StandardPattern pattern : multipleRowData) {
            this.addRow(pattern);
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void addColumns(List<TypeList> multipleColumnData) {
        throw new UnsupportedOperationException("Not supported.");
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public StandardPattern removeRow(int index) {
        return dataTable.remove(index);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public StandardPattern getRow(int index) {
        return (StandardPattern) dataTable.get(index).getClone();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void setRow(int index, StandardPattern rowData) {
        this.dataTable.set(index, rowData);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public TypeList getColumn(int index) {
        TypeList column = new TypeList();

        if (index >= this.getNumColums()) {
            throw new IndexOutOfBoundsException("Column index: " + index + " out of bounds.");
        }
        int featureVectorSize = this.getRow(0).getVector().size();
        // if index lies in feature vector
        if (index < featureVectorSize) {
            for (StandardPattern pattern : dataTable) {
                column.add(pattern.getVector().get(index));
            }
        } else { // else index is the single classification object
            for (StandardPattern pattern : dataTable) {
                column.add(pattern.getTarget());
            }
        }
        return column;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void setColumn(int index, TypeList columnData) {
        int size = columnData.size();
        if (size != dataTable.size()) {
            throw new UnsupportedOperationException("Cannot set column that is " +
                    "of different size than table columns.");
        }
        if (index >= this.getNumColums()) {
            throw new IndexOutOfBoundsException("Column index: " + index + " out of bounds.");
        }
        int featureVectorSize = this.getRow(0).getVector().size();
        // if index lies in feature vector
        if (index < featureVectorSize) {
            for (int i = 0; i < size; i++) {
                dataTable.get(i).getVector().set(index, (Numeric) columnData.get(i));
            }
        } // else index is the classification object
        else {
            for (int i = 0; i < size; i++) {
                dataTable.get(i).setTarget(columnData.get(i));
            }
        }
    }

    /**
     * Gets the name of a column.
     * @param index the index of the column name to retrieve.
     * @return the column's name.
     */
    @Override
    public String getColumnName(int index) {
        String name = columnNames.get(index);
        if (name == null)
            return "";
        return name;
    }

    /**
     * Sets the name of a column.
     * @param index the index of the column name to set.
     * @param name the column's new name.
     */
    @Override
    public void setColumnName(int index, String name) {
        columnNames.put(index, name);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<String> getColumnNames() {
        ArrayList<String> names = new ArrayList<String>();
        for (int i = 0; i < this.getNumColums(); i++) {
            names.add(this.getColumnName(i));
        }
        return names;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void setColumnNames(List<String> names) {
        for (int i = 0; i < names.size(); i++) {
            this.setColumnName(i, names.get(i));
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int size() {
        return this.dataTable.size();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getNumRows() {
        return this.size();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int getNumColums() {
        if (this.size() == 0) {
            return 0;
        }
        return this.getRow(0).getVector().size() + 1; // add one for the class
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void clear() {
        this.dataTable.clear();
    }

    @Override
    public StandardPatternDataTable getClone() {
        return new StandardPatternDataTable(this);
    }

    @Override
    public Iterator<StandardPattern> iterator() {
        return dataTable.iterator();
    }
}
