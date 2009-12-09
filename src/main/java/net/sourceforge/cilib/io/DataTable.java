/*
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.io;

import java.util.List;
import net.sourceforge.cilib.util.Cloneable;

/**
 * An interface representing a data table.
 * @author andrich
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
     * Adds mulltiple columns to the table.
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
     * @return
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
