/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io;

import java.util.List;
import net.sourceforge.cilib.util.Cloneable;

/**
 * An interface representing a data table.
 * @param <T> A type that represents a single row.
 * @param <E> A type that represents a column. Note, not necessarily the same type as the row.
 */
public interface DataTable<T, E> extends Cloneable, Iterable<T> {

    /**
     * Adds a row to the table.
     * @param rowData a collection containing the row's data.
     */
    void addRow(T rowData);

    /**
     * Adds a column to the table.
     * @param columnData a collection containing the column's data.
     */
    void addColumn(E columnData);

    /**
     * Adds multiple rows to the table.
     * @param multipleRowData List containing multiple rows' data.
     */
    void addRows(List<T> multipleRowData);

    /**
     * Adds multiple columns to the table.
     * @param multipleColumnData List containing multiple columns' data.
     */
    void addColumns(List<E> multipleColumnData);

    /**
     * Removes a row from the table and returns it.
     * @param index the row that must be removed.
     * @return the row that was removed.
     */
    T removeRow(int index);

    /**
     * Returns a row in the table.
     * @param index the index of the row to retrieve.
     * @return the requested row.
     */
    T getRow(int index);

    /**
     * Sets a row in the data table.
     * @param index the row to be set.
     * @param rowData the new row data.
     */
    void setRow(int index, T rowData);

    /**
     * Returns a column in the table.
     * @param index the column to retrieve.
     * @return the column data.
     */
    E getColumn(int index);

    /**
     * Sets a column in the table
     * @param index the column to set
     * @param columnData the column data
     */
    void setColumn(int index, E columnData);

    /**
     * Gets the name of the column, returns an empty string if none is specified.
     * @param index the column which is to be named.
     * @return      the column name.
     */
    String getColumnName(int index);

    /**
     * Sets the name of the column.
     * @param index the column which is to be named.
     * @param name the new name of the column.
     */
    void setColumnName(int index, String name);

    /**
     * Gets the columns names as a list.
     * @return the column names.
     */
    List<String> getColumnNames();

    /**
     * Sets the column names.
     * @param names the new column names.
     */
    void setColumnNames(List<String> names);

    /**
     * Clears the data table.
     */
    void clear();

    /**
     * Gets the size of the table.
     * @return the size of the table.
     */
    int size();

    /**
     * Gets the number of rows in the table.
     * @return the number of rows in the table.
     */
    int getNumRows();

    /**
     * Gets the number of columns in the table.
     * @return the number of columns.
     */
    int getNumColums();

    /**
     * Make deep copy of the table.
     */
    @Override
    Object getClone();
}
