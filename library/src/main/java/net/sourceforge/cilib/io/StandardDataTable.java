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
import net.sourceforge.cilib.util.Cloneable;

/**
 * Class represents the standard implementation of the {@link DataTable DataTable}
 * where the underlying datastructures used are ArrayLists and rows are represented
 * as a List of type T. Both the row and column type is the same.
 * @param <T> type of elements contained in a row.
 */
public class StandardDataTable<T extends Cloneable> implements DataTable<List<T>, List<T>> {

    private List<List<T>> dataTable;
    private HashMap<Integer, String> columnNames;

    public StandardDataTable() {
        dataTable = new ArrayList<List<T>>();
        columnNames = new HashMap<Integer, String>();
    }

    /**
     * Copy constructor.
     * @param orig the datatable to copy.
     */
    public StandardDataTable(StandardDataTable<T> orig) {
        dataTable = new ArrayList<List<T>>(orig.getNumRows());
        for (List<T> row : orig) {
            ArrayList newRow = new ArrayList(row.size());
            for (T t : row) {
                newRow.add(t.getClone());
            }
            dataTable.add(newRow);
        }
        columnNames = new HashMap<Integer, String>();
        int size = orig.getNumColums();
        for (int i = 0; i < size; i++) {
            this.setColumnName(i, orig.getColumnName(i));
        }
    }

    /**
     * Add a row to the table. The row data is added into a new list, thereby
     * maintaining control of the row data structure (which is an ArrayList).
     * @param rowData A list representing a row.
     */
    @Override
    public void addRow(List<T> rowData) {
        ArrayList<T> row = new ArrayList<T>(rowData.size());
        row.addAll(rowData);
        dataTable.add(row);
    }

    /**
     * Add a column to the table. The column data is added unto the end of the
     * existing lists.
     * @param columnData A list representing a row.
     */
    @Override
    public void addColumn(List<T> columnData) {
        int size = columnData.size();

        if (dataTable.size() == 0) {
            for (int i = 0; i < size; i++) {
                ArrayList<T> row = new ArrayList<T>();
                row.add(columnData.get(i));
                this.dataTable.add(row);
            }
        } else if (size != dataTable.size()) {
            throw new UnsupportedOperationException("Cannot add column that is " +
                    "of different size than table columns.");
        } else {
            for (int i = 0; i < size; i++) {
                dataTable.get(i).add(columnData.get(i));
            }
        }
    }

    /**
     * Add multiple rows to the table. Calls the {@link #addRow(java.util.List) addRow}
     * method for each of the multiple rows.
     * @param multipleRowData a list of lists representing multiple rows.
     */
    @Override
    public void addRows(List<List<T>> multipleRowData) {
        for (List<T> row : multipleRowData) {
            this.addRow(row);
        }
    }

    /**
     * Add multiple columns to the table. Calls the {@link #addColumn(java.util.List) addColumn}
     * method for each of the multiple columns.
     * @param multipleColumnData a list of lists representing multiple columns.
     */
    @Override
    public void addColumns(List<List<T>> multipleColumnData) {
        for (List<T> column : multipleColumnData) {
            this.addColumn(column);
        }
    }

    /**
     * Removes and returns a row from the table.
     * @param index the index of the row to remove.
     * @return the row that has been removed.
     */
    @Override
    public List<T> removeRow(int index) {
        return dataTable.remove(index);
    }

    /**
     * Gets a row from the table.
     * @param index the required row.
     * @return a new list containing the row elements.
     */
    @Override
    public List<T> getRow(int index) {
        return this.dataTable.get(index);
    }

    /**
     * Sets a row in the datatable.
     * @param index the index of the row to set.
     * @param rowData the new row's data.
     */
    @Override
    public void setRow(int index, List<T> rowData) {
        this.dataTable.set(index, rowData);
    }

    /**
     * Gets a column from the datatable as a new ArrayList.
     * @param index the index of the column to retrieve.
     * @return a new ArrayList containing the column elements.
     */
    @Override
    public List<T> getColumn(int index) {
        ArrayList<T> column = new ArrayList<T>(dataTable.size());
        for (List<T> row : dataTable) {
            column.add(row.get(index));
        }
        return column;
    }

    /**
     * Sets a column in the datatable. The elements are set individually in each row.
     * @param index the index of the column to set.
     * @param columnData the new column data.
     */
    @Override
    public void setColumn(int index, List<T> columnData) {
        int size = columnData.size();
        if (size != dataTable.size()) {
            throw new UnsupportedOperationException("Cannot set column that is " +
                    "of different size than table columns.");
        }
        for (int i = 0; i < size; i++) {
            List<T> row = dataTable.get(i);
            row.set(index, columnData.get(i));
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
        if (name == null) {
            return "";
        }
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
     * Gets the number of rows in the table.
     * @return the number of rows in the table.
     */
    @Override
    public int size() {
        return dataTable.size();
    }

    /**
     * Gets the number of rows in the table.
     * @return the number of rows in the table.
     */
    @Override
    public int getNumRows() {
        return dataTable.size();
    }

    /**
     * Gets the number of columns in the table.
     * @return the number of columns in the table.
     */
    @Override
    public int getNumColums() {
        if (dataTable.isEmpty()) {
            return 0;
        }
        return dataTable.get(0).size();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Iterator<List<T>> iterator() {
        return dataTable.iterator();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void clear() {
        this.dataTable.clear();
        this.columnNames.clear();
    }

    @Override
    public Object getClone() {
        return new StandardDataTable<T>(this);
    }
}
