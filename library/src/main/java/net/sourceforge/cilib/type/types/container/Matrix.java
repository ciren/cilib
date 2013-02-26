/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types.container;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import fj.P1;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.Numeric;

/**
 * Representation of a Matrix. This class is immutable with the intention that
 * all operations on a Matrix will result in a new resulting Matrix.
 */
public final class Matrix implements Type {
    private static final long serialVersionUID = 7726056815026772629L;

    private final double[][] contents;

    private Matrix(int x, int y) {
        Preconditions.checkArgument(x > 0, "Zero row length does not make sense.");
        Preconditions.checkArgument(y > 0, "Zero column length does not make sense.");

        this.contents = new double[x][y];
    }

    /**
     * Determine if the {@code Matrix} is square. In other words, if the number
     * of rows and columns are the same.
     * @return {@code true} if the matrix is square, {@code false} otherwise.
     */
    public boolean isSquare() {
        Preconditions.checkState(this.contents.length >= 1);
        return (contents.length == contents[0].length) ? true : false;
    }

    /**
     * Obtain the current value within the matrix at the provided co-ordinates.
     * @param row The row to lookup, indexed from 0.
     * @param col The column to lookup, indexed from 0.
     * @return The value located at the position {@code [row][col]}.
     */
    public double valueAt(int row, int col) {
        return this.contents[row][col];
    }

    /**
     * Obtain the row vector for the given row, indexed from 0.
     * @param row The row number to obtain.
     * @return A {@code Vector} representing the row.
     */
    public Vector getRow(int row) {
        List<Double> rowList = new ArrayList<Double>(this.contents[row].length);

        for (double d : this.contents[row]) {
            rowList.add(d);
        }

        return Vector.copyOf(rowList);
    }

    /**
     * Obtain the column vector for the given column, indexed from 0.
     * @param column The column number to obtain.
     * @return A {@code Vector} representing the column.
     */
    public Vector getColumn(int column) {
        return this.transpose().getRow(column);
    }

    /**
     * Get the number of rows within the matrix.
     * @return The number of rows.
     */
    public int getRows() {
        return this.contents.length;
    }

    /**
     * Get the number of columns within the matrix.
     * @return The number of columns.
     */
    public int getColumns() {
        return this.contents[0].length;
    }

    /**
     * Apply the addition operation on the current matrix and the provided
     * matrix. The result is a new matrix.
     * @param b The matrix to add.
     * @return A new {@code Matrix} representing the result of the addition.
     */
    public final Matrix plus(final Matrix b) {
        Preconditions.checkArgument((this.getRows() == b.getRows()) && (this.getColumns() == b.getColumns()),
            "Illegal matrix dimensions for matrix addition.");

        Matrix result = new Matrix(this.getRows(), this.getColumns());
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                result.contents[i][j] = this.contents[i][j] + b.contents[i][j];
            }
        }
        return result;
    }

    /**
     * Apply the subtraction operation on the current matrix and the provided
     * matrix. The result is a new matrix.
     * @param b The matrix to subtract.
     * @return A new {@code Matrix} representing the result of the subtraction.
     */
    public final Matrix minus(final Matrix b) {
        Preconditions.checkArgument((this.getRows() == b.getRows()) && (this.getColumns() == b.getColumns()),
            "Illegal matrix dimensions for matrix subtraction.");

        Matrix result = new Matrix(this.getRows(), this.getColumns());
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                result.contents[i][j] = this.contents[i][j] - b.contents[i][j];
            }
        }
        return result;
    }

    /**
     * Multiplies every element of this matrix by the given scalar;
     * @param scalar The scalar to multiply into the matrix.
     * @return The resultant matrix.
     */
    public Matrix multiply(final double scalar) {
        return multiply(new P1<Double>() {
            @Override
            public Double _1() {
                return scalar;
            }
        });
    }

    /**
     * Multiplies every element of this matrix by the given supplier;
     * @param supplier The supplier that supplies scalar values.
     * @return The resultant matrix.
     */
    public Matrix multiply(P1<Double> supplier) {
        Matrix result = new Matrix(this.getRows(), this.getColumns());
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                result.contents[i][j] = this.contents[i][j] * supplier._1();
            }
        }
        return result;
    }

    /**
     * Perform multiplication on the current {@code Matrix} and the provided {@code Matrix}.
     * Naturally, matrix multiplication can only be performed on matrices that adhere
     * to the required matrix multiplication rules.
     * <p>
     * The result of this operation is a new immutable matrix.
     * @param b The {@code Matrix} to to multiply the current {@code Matrix} with.
     * @return A new {@code Matrix} representing the result of the multiplication.
     * @see Matrix#times(net.sourceforge.cilib.type.types.container.Matrix)
     */
    public Matrix multiply(Matrix b) {
        return this.times(b);
    }

    public Vector multiply(Vector b) {
        Matrix.Builder builder = Matrix.builder().dimensions(b.size(), 1);
        for(Numeric n : b) {
            builder.addRow(n.doubleValue());
        }
        return this.times(builder.build()).getColumn(0);
    }

    /**
     * Perform multiplication on the current {@code Matrix} and the provided {@code Matrix}.
     * Naturally, matrix multiplication can only be performed on matrices that adhere
     * to the required matrix multiplication rules.
     * <p>
     * The result of this operation is a new immutable matrix.
     * @param b The {@code Matrix} to to multiply the current {@code Matrix} with.
     * @return A new {@code Matrix} representing the result of the multiplication.
     */
    public Matrix times(Matrix b) {
        Preconditions.checkArgument(this.getColumns() == b.getRows(), "Illegal matrix dimensions for matrix multiplication.");

        Matrix result = new Matrix(this.getRows(), b.getColumns());
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < b.getColumns(); j++) {
                result.contents[i][j] = this.getRow(i).dot(b.getColumn(j));
            }
        }
        return result;
    }

    /**
     * Obtain the transposition of the current {@code Matrix} instance.
     * @return A new {@code Matrix} that is the transpose of the current {@code Matrix}.
     */
    public Matrix transpose() {
        Matrix result = new Matrix(this.getColumns(), this.getRows());
        for (int i = 0; i < this.getRows(); i++)
            for (int j = 0; j < this.getColumns(); j++)
                result.contents[j][i] = this.contents[i][j];
        return result;
    }

    /**
     * Perform a rotation on the provided matrix by the specified {@code angle}.
     * @param angle The rotation angle defined in radians.
     * @param i
     * @param j
     * @return A rotated {@code Matrix}, which is a new instance.
     */
    public Matrix rotate(double angle, int i, int j) {
        Matrix rotation = getClone();
        rotation.contents[i][i] = Math.cos(angle);
        rotation.contents[j][j] = Math.cos(angle);
        rotation.contents[i][j] = Math.sin(angle);
        rotation.contents[j][i] = -Math.sin(angle);

        return this.times(rotation);
    }

    /**
     * Obtain the determinant of the current matrix. The determinant is obtained
     * by creating a LU decomposition matrix. The determinant is then calculated by:
     * <p>
     * <pre>
     * \begin{equation}
     * \mbox{det($A$)} = |L| \dot |U|
     * \end{equation}
     * </pre>
     * where |L| = 1 and |U| is the product of the diagonal elements.
     * @return The determinant value.
     */
    public double determinant() {
        Preconditions.checkState(isSquare(), "Cannot obtain determinant of a non-square matrix");

        LUDecomposition decomposition = new LUDecomposition(this);
        return decomposition.determinant();
    }

    /**
    * Determines if the {@code Matrix} is singular.
    * A matrix is singular if its determinant is zero.
    * @return {@code true} if the matrix is singular, {@code false} otherwise.
    */
    public boolean isSingular() {
        return determinant() == 0.0;
    }

    /**
    * Remove a row and a column from the current {@code Matrix} instance.
    * @param row The index of the row to remove
    * @param col The index of the column to remove
    * @return The {@code Matrix} without the row and column
    */
    public Matrix subMatrix(int row, int col) {
        Preconditions.checkArgument((row >= 0 && row < getRows()), "Illegal row index");
        Preconditions.checkArgument((col >= 0 && col < getColumns()), "Illegal column index");
        Preconditions.checkState(getRows() > 1 && getColumns() > 1, "Cannot obtain submatrix of a 1xN or Nx1 matrix");

        Matrix sub = new Matrix(getRows() - 1, getColumns() - 1);
        int r = 0, c = 0;

        for(int i = 0; i < getRows(); i++) {
            if (i != row) {
                for (int j = 0; j < getColumns(); j++) {
                    if (j != col)
                        sub.contents[r][c++] = this.contents[i][j];
                }
                r++;
                c = 0;
            }
        }

        return sub;
    }

    /**
     * Obtain the cofactor (adjunct) of the current {@code Matrix} instance.
     * <p>
     * Please refer to <a href=http://en.wikipedia.org/wiki/Cofactor_(linear_algebra)>
     * the Wikipedia entry</a> for more information.
     * <p>
     */
    public Matrix cofactor() {
        Preconditions.checkState(isSquare(), "Cannot obtain cofactor of a non-square matrix");

        Matrix cofactor = new Matrix(getRows(), getColumns());

        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getColumns(); j++) {
                Matrix sub = subMatrix(i, j);
                double factor = sub.determinant() * Math.pow(-1, i + j);
                cofactor.contents[i][j] = factor;
            }
        }

        return cofactor;
    }
    /**
    * Obtain the inverse of the current {@code Matrix} instance if it exists.
    * @return The inverse {@code Matrix}
    */
    public Matrix inverse() {
        Preconditions.checkState(isSquare(), "Cannot obtain inverse of a non-square matrix");
        Preconditions.checkState(!isSingular(), "Cannot obtain cofactor of a singular matrix");

        Matrix adjugate = cofactor().transpose();
        return adjugate.multiply(1.0 / determinant());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Matrix getClone() {
        Matrix matrix = new Matrix(this.getRows(), this.getColumns());

        for (int i = 0; i < getRows(); i++)
            for (int j = 0; j < getColumns(); j++)
                matrix.contents[i][j] = this.contents[i][j];

        return matrix;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final Matrix other = (Matrix) obj;
        return Arrays.deepEquals(this.contents, other.contents);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getRows(); i++) {
            sb.append(getRow(i).toString() + '\n');
        }
        return sb.deleteCharAt(sb.length()-1).toString();
    }

    @Override
    public int hashCode() {
        int hash = 0;

        for (double[] array : this.contents)
            hash += Arrays.hashCode(array);

        return hash;
    }

    /**
     * Obtain a builder for {@code Matrix} instances.
     * @return A {@link Matrix.Builder} instance.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder object to help with the construction of {@link Matrix} instances.
     */
    public static final class Builder {
        private int rowNumber;
        private int colNumber;
        private List<List<Double>> rows;
        private List<DataPoint> tuples;
        private boolean identity;

        private Builder() {
            reset();
        }

        /**
         * Define the dimensions (rows and columns) that the built up {@code Matrix} will
         * contain.
         * @param rows The number of rows.
         * @param columns The number of columns.
         * @return The current {@code Builder}.
         * @throws IllegalArgumentException if {@code rows} or {@code columns} are less than 1.
         */
        public Builder dimensions(int rows, int columns) {
            Preconditions.checkArgument(rows >= 1);
            Preconditions.checkArgument(columns >= 1);

            this.rowNumber = rows;
            this.colNumber = columns;
            return this;
        }

        /**
         * Add a row vector, for inclusion in the built up {@code Matrix}.
         * @param columnValues The values for the columns.
         * @return the current {@code Builder}.
         * @throws IllegalArgumentException if the {@code columnValues} are not the same
         *         dimensions / length that is expected.
         */
        public Builder addRow(Double... columnValues) {
            Preconditions.checkArgument(columnValues.length == this.colNumber,
                "Cannot add a row with a differing column length. Expected: " + this.colNumber + ", got: " + columnValues.length);

            List<Double> rowVector = Arrays.asList(columnValues);
            this.rows.add(rowVector);
            return this;
        }

        /**
         * Add a row vector, for inclusion in the built up {@code Matrix}.
         * @param iterable The values for the columns.
         * @return the current {@code Builder}.
         * @throws IllegalArgumentException if the {@code columnValues} are not the same
         *         dimensions / length that is expected.
         */
        public Builder addRow(Iterable<Double> iterable) {
            List<Double> list = new ArrayList<Double>();
            Iterables.addAll(list, iterable);
            this.rows.add(list);
            return this;
        }

        /**
         * Define that the built up {@code Matrix} should be an identity {@code Matrix}.
         * All added row vectors or position values will be discarded if the built up {@code Matrix}
         * is to be an identity {@code Matrix}.
         * @return The current {@code Builder}.
         */
        public Builder identity() {
            Preconditions.checkState(this.rowNumber == this.colNumber, "Identity on non-sqaure matrix is not allowed.");
            this.identity = true;
            return this;
        }

        /**
         * Define that at a specific value for a defined grid point within the built
         * up {@code Matrix}.
         * <p>
         * This operation will override any values specified by a row vector addition.
         * @param row The row number, indexed from 0.
         * @param col The column number, indexed from 0.
         * @param value The value to be set at the defined location.
         * @return The current {@code Builder}.
         */
        public Builder valueAt(int row, int col, double value) {
            this.tuples.add(new DataPoint(row, col, value));
            return this;
        }

        /**
         * Reset the builder into a clean state.
         */
        private void reset() {
            this.rowNumber = 0;
            this.colNumber = 0;
            this.rows = new ArrayList<List<Double>>();
            this.tuples = new ArrayList<DataPoint>();
            this.identity = false;
        }

        /**
         * Build the {@code Matrix} instance, based on the currently defined builder.
         * @return A new immutable {@code Matrix} instance.
         */
        public Matrix build() {
            Matrix matrix = new Matrix(rowNumber, colNumber);

            if (identity) {
                for (int i = 0; i < rowNumber; i++) {
                    for (int j = 0; j < colNumber; j++) {
                        matrix.contents[i][j] = (i == j) ? 1.0 : 0.0;
                    }
                }

                return matrix;
            }

            if (this.rows.size() >= 1) {
                for (int i = 0; i < rowNumber; i++) {
                    for (int j = 0; j < colNumber; j++) {
                        matrix.contents[i][j] = this.rows.get(i).get(j);
                    }
                }
            }

            if (this.tuples.size() >= 0) {
                for (DataPoint tuple : this.tuples) {
                    matrix.contents[tuple.getX()][tuple.getY()] = tuple.getValue();
                }
            }

            reset(); // Reset the builder to the default state, so that it may be used again, if needed.

            return matrix;
        }
    }

    /**
     * Class defining a [row, col, value] tuple for building up of a {@code Matrix}.
     */
    private static final class DataPoint {
        private int x;
        private int y;
        private double value;

        public DataPoint(int x, int y, double value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    /**
     * This class is an implementation of the Doolittle / Crout algorithms.
     * <p>
     * Please refer to <a href=http://en.wikipedia.org/wiki/LU_decomposition#Doolittle_algorithm>
     * the Wikipedia entry</a> for more information.
     * <p>
     * Additional help and code was obtained from the JAMA project (which seems to
     * be stagnant) and from the commons-math.
     */
    private static final class LUDecomposition {
        private double[][] lu;

        private int m;
        private int n;
        private int pivsign;
        private int[] piv;

        private LUDecomposition(Matrix matrix) {
            m = matrix.getRows();
            n = matrix.getColumns();
            pivsign = 1;

            lu = new double[m][n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    lu[i][j] = matrix.contents[i][j];
                }
            }

            piv = new int[m];
            for (int i = 0; i < m; i++)
                piv[i] = i;

            double[] LUrowi;
            double[] LUcolj = new double[m];

            // Outer-loop
            for (int j = 0; j < n; j++) {
                // Make a copy of the j-th column.
                for (int i = 0; i < m; i++) {
                    LUcolj[i] = lu[i][j];
                }

                // Apply previous transformations
                for (int i = 0; i < m; i++) {
                    LUrowi = lu[i];

                    int kmax = Math.min(i, j);
                    double s = 0.0;
                    for (int k = 0; k < kmax; k++) {
                        s += LUrowi[k]*LUcolj[k];
                    }

                    LUcolj[i] -= s;
                    LUrowi[j] = LUcolj[i];
                }

                // Find the pivot and exchange if needed
                int p = j;
                for (int i = j+1; i < m; i++) {
                    if (Math.abs(LUcolj[i]) > Math.abs(LUcolj[p])) {
                        p = i;
                    }
                }

                if (p != j) {
                    for (int k = 0; k < n; k++) {
                        double t = lu[p][k]; lu[p][k] = lu[j][k]; lu[j][k] = t;
                    }
                    int k = piv[p]; piv[p] = piv[j]; piv[j] = k;
                    pivsign = -pivsign;
                }

                // Compute multipliers.
                if (j < m && (lu[j][j] != 0.0)) {
                    for (int i = j+1; i < m; i++) {
                        lu[i][j] /= lu[j][j];
                    }
                }
            }
        }

        public double determinant() {
            Preconditions.checkState(m == n, "Matrix must be square.");

            double d = Integer.valueOf(pivsign).doubleValue();
            for (int j = 0; j < n; j++) {
                d *= lu[j][j];
            }

            return d;
        }
    }
}
