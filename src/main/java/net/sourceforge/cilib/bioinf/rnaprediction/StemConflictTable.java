/**
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
package net.sourceforge.cilib.bioinf.rnaprediction;

import net.sourceforge.cilib.container.BitArray;

/**
 * TODO: Compelte this javadoc.
 */
public final class StemConflictTable {

    private static StemConflictTable instance = null;

    private BitArray[] matrix = null;

    private int size;

    private StemConflictTable() {
    }

    public static StemConflictTable getInstance() {
        if (instance == null) {
            instance = new StemConflictTable();
        }

        return instance;
    }

    /**
     * Creates a matrix of dimensions size*size.
     * @param size The size of the matrix.
     */
    public void create(int size) {
        this.size = size;
        matrix = new BitArray[size];
        for (int i = 0; i < size; i++) {
            matrix[i] = new BitArray(size);
        }
        clearAll();
    }

    /**
     * this method will set all the bits to 0.
     */
    public void clearAll() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i].clear(j);
            }
        }
    }

    /**
     * Sets a bit (1) in the matrix.
     * @param row the row.
     * @param col The column.
     */
    public void setBit(int row, int col) {
        matrix[row].set(col);
    }

    /**
     * Returns the value of a bit.
     * @param row The row.
     * @param col The column.
     * @return the value of the bit at row and col.
     */
    public boolean get(int row, int col) {
        return matrix[row].get(col);
    }

    /**
     * Clears a specific bit (0) in the matrix.
     * @param row The row.
     * @param col The column.
     */
    public void clear(int row, int col) {
        matrix[row].clear(col);
    }

}
