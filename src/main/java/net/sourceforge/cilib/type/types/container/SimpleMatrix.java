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
package net.sourceforge.cilib.type.types.container;

import java.util.Arrays;

import net.sourceforge.cilib.type.types.Real;

/**
 * Implementation of a simple matrix of doubles with the ability to perform required matrix
 * operations.
 *
 * @author Olusegun
 *
 */
public class SimpleMatrix {

    // TODO Add the rest of the matrix operations

    private int rows;
    private int columns;
    private double[][] matrix;

    /**
     * Default constructor. Necessary but parameterised constructors more useful.
     */
    public SimpleMatrix() {
        rows = 0;
        columns = 0;
        matrix = null;
    }

    /**
     * Creates a matrix with the specified number of columns and rows. All elements are initialised
     * to 0.0.
     *
     * @param rows -
     *            number of rows required
     * @param columns -
     *            number of columns required
     */
    public SimpleMatrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.matrix = new double[this.rows][this.columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = 0.0;
            }
        }
    }

    /**
     *
     * @param otherMatrix -
     *            matrix to copy
     */
    public SimpleMatrix(SimpleMatrix otherMatrix) {
        this.rows = otherMatrix.getRows();
        this.columns = otherMatrix.getColumns();
        this.matrix = new double[this.rows][this.columns];

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                this.getMatrix()[i][j] = (otherMatrix.getMatrix())[i][j];
            }
        }
    }

    /**
     * @return clone - a deep copy of this class
     */
    public SimpleMatrix getClone() {
        return new SimpleMatrix(this);
    }

    /**
     * @return true if both matrices are equal
     */
    public boolean equals(Object other) {
        if (other == this)
            return true;

        if ((other == null) || (this.getClass() != other.getClass()))
            return false;

        SimpleMatrix otherMatrix = (SimpleMatrix) other;

        if ((this.getRows() != otherMatrix.getRows())
                || (this.getColumns() != otherMatrix.getColumns()))
            return false;

        return Arrays.deepEquals(this.getMatrix(), otherMatrix.getMatrix());
    }

    /**
     * @param rhs -
     *            matrix being added to this
     * @return matrixSum - sum of this and rhs
     */
    public final SimpleMatrix plus(SimpleMatrix rhs) {
        if (this.getMatrix() == null || rhs.getMatrix() == null)
            throw new IllegalArgumentException("One or more matrices is empty.");

        if (this.getRows() != rhs.getRows() || this.getColumns() != rhs.getColumns())
            throw new UnsupportedOperationException("Cannot add matrices of different dimensions");

        final SimpleMatrix matrixSum = this.getClone();

        for (int i = 0; i < matrixSum.getRows(); i++) {
            for (int j = 0; j < matrixSum.getColumns(); j++) {
                (matrixSum.getMatrix())[i][j] = (this.getMatrix())[i][j] + (rhs.getMatrix())[i][j];
            }
        }

        return matrixSum;
    }

    /**
     * @param rhs -
     *            matrix being subtracted from this
     * @return matrixDifference - difference between this and rhs
     */
    public final SimpleMatrix subtract(SimpleMatrix rhs) {
        if (this.getMatrix() == null || rhs.getMatrix() == null)
            throw new IllegalArgumentException("One or more matrices is empty.");

        if (this.getRows() != rhs.getRows() || this.getColumns() != rhs.getColumns())
            throw new UnsupportedOperationException(
                    "Cannot subtract matrices of different dimensions");

        final SimpleMatrix matrixDifference = this.getClone();

        for (int i = 0; i < matrixDifference.getRows(); i++) {
            for (int j = 0; j < matrixDifference.getColumns(); j++) {
                (matrixDifference.getMatrix())[i][j] = (this.getMatrix())[i][j]
                        - (rhs.getMatrix())[i][j];
            }
        }

        return matrixDifference;
    }

    /**
     * @param scalar -
     *            value to scale matrix by
     * @return sacaledMatrix - the result of the scalar multiplication
     */
    public final SimpleMatrix multiply(double scalar) {
        if (this.getMatrix() == null)
            throw new IllegalArgumentException("One or more matrices is empty.");

        final SimpleMatrix scaledMatrix = this.getClone();

        for (int i = 0; i < scaledMatrix.getRows(); i++) {
            for (int j = 0; j < scaledMatrix.getColumns(); j++) {
                (scaledMatrix.getMatrix())[i][j] *= scalar;
            }
        }

        return scaledMatrix;
    }

    /**
     * @param rhs -
     *            matrix to multiply by
     * @return productMatrix - result of matrix multiplication
     */
    public final SimpleMatrix multiply(SimpleMatrix rhs) {
        if (this.getMatrix() == null || rhs.getMatrix() == null)
            throw new IllegalArgumentException("One or more matrices is empty.");

        if (this.getColumns() != rhs.getRows())
            throw new UnsupportedOperationException(
                    "Cannot multiply matrices of incompatible dimensions");

        final SimpleMatrix productMatrix = new SimpleMatrix(this.getRows(), rhs.getColumns());

        for (int i = 0; i < productMatrix.getRows(); i++) {
            for (int j = 0; j < productMatrix.getColumns(); j++) {
                for (int r = 0; r < this.getColumns(); r++) {
                    (productMatrix.getMatrix())[i][j] += (this.getMatrix())[i][r]
                            * (rhs.getMatrix()[r][j]);
                }
            }
        }

        return productMatrix;
    }

    /**
     * @param rhs -
     *            vector to multiply by
     * @return productVector - result of matrix/vector multiplication
     */
    public final Vector multiply(Vector rhs) {
        if (this.getMatrix() == null)
            throw new IllegalArgumentException("One or more matrices is empty.");

        if (this.getColumns() != rhs.getDimension())
            throw new UnsupportedOperationException(
                    "Cannot multiply matrix with a vector of incompatible dimensions");

        final Vector productVector = new Vector();

        for (int i = 0; i < this.getRows(); i++) {
            double coordinate_i = 0.0;
            for (int j = 0; j < this.getColumns(); j++) {
                coordinate_i += (this.getMatrix())[i][j] * rhs.getReal(j);
            }
            productVector.append(new Real(coordinate_i));
        }

        return productVector;
    }

    /**
     * @return transposedMatrix - the transpose of this matrix class
     */
    public SimpleMatrix getTranspose() {
        if (this.getMatrix() == null)
            throw new IllegalArgumentException("Cannot transpose a null matrix.");

        final SimpleMatrix transposedMatrix = new SimpleMatrix(this.getColumns(), this.getRows());

        for (int i = 0; i < transposedMatrix.getRows(); i++) {
            for (int j = 0; j < transposedMatrix.getColumns(); j++) {
                (transposedMatrix.getMatrix())[i][j] = (this.getMatrix())[j][i];
            }
        }

        return transposedMatrix;
    }

    /**
     * @return the determinant
     */
    public double getDeterminant() {

        /* public static double[][] reduce(double[][] x, double[][] y, int r, int c, int n)
         {
            for (int h = 0, j = 0; h < n; h++)
            {
               if (h == r)
                  continue;
               for (int i = 0, k = 0; i < n; i++)
               {
                  if (i == c)
                     continue;
                  y[j][k] = x[h][i];
                  k++;
               } // end inner loop
               j++;
            } // end outer loop
            return y;
         } // end method
      // ===================================================
          public static double det(int NMAX, double[][] x)
         {
            double ret=0;
            if (NMAX < 4)// base case
            {
               double prod1=1, prod2=1;
               for (int i = 0; i < NMAX; i++)
               {
                  for (int j = 0; j < NMAX; j++)
                  {
                     prod1 *= x[(j + i + 1) % NMAX][j];
                     prod2 *= x[(j + i + 1) % NMAX][NMAX - j - 1];
                  } // end inner loop
                  ret += prod1 - prod2;
               } // end outer loop
               return ret *- 1;
            } // end base case
            double[][] y = new double [NMAX - 1][NMAX - 1];
            for (int h = 0; h < NMAX; h++)
            {
               if (x[h][0] == 0)
                  continue;
               reduce(x, y, h, 0, NMAX);
               if (x[h][0] % 2 == 0) ret += det(NMAX - 1, y) * x[h][0];
               if (x[h][0]  %2 == 1) ret -= det(NMAX - 1, y) * x[h][0];
            } // end loop
            return ret;
         } // end method
*/

        return 0.0;
    }

    /**
     * @return matrixInverse - the inverse of this matrix class
     */
    public SimpleMatrix getInverse() {

        return null;
    }

    /**
     * @return rowEchelonForm - this matrix class after application of the Gauss algorithm
     */
    public SimpleMatrix getRowEchelonForm() {

        return null;
    }

    /**
     * @return reducedRowEchelonForm - this matrix class after application of the Gauss-Jordan
     *         algorithm
     */
    public SimpleMatrix getReducedRowEchelonForm() {

        return null;
    }

    /**
     * Reset a square matrix into the identity matrix.
     */
    public void resetToIdentity() {
        if (this.getMatrix() == null)
            throw new IllegalArgumentException(
                    "Cannot reset an empty matrix to the identity matrix.");

        if (this.getRows() != this.getColumns())
            throw new UnsupportedOperationException(
                    "Cannot reset a non-square matrix to the identity matrix.");

        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getColumns(); j++) {
                (this.getMatrix())[i][j] = 0.0;
            }
        }

        for (int i = 0; i < this.getRows(); i++) {
            (this.getMatrix())[i][i] = 1.0;
        }
    }

    /**
     * @return true is matrix is equal to its transpose
     */
    public boolean isSymmetric() {
        return this.equals(this.getTranspose());
    }

    /**
     * @return the rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * @param rows -
     *            the rows to set
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * @return the columns
     */
    public int getColumns() {
        return columns;
    }

    /**
     * @param columns -
     *            the columns to set
     */
    public void setColumns(int columns) {
        this.columns = columns;
    }

    /**
     * @return the matrix
     */
    public double[][] getMatrix() {
        return matrix;
    }

    /**
     * @param matrix -
     *            the matrix to set
     */
    public void setMatrix(double[][] matrix) {
        int firstColumnLength = matrix[0].length;
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i].length != firstColumnLength)
                throw new IllegalArgumentException(
                        "A matrix can only be created from a rectangular array.");
        }

        this.matrix = matrix;
        this.rows = matrix.length;
        this.columns = firstColumnLength;
    }

}
