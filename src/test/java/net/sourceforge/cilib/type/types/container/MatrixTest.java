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

package net.sourceforge.cilib.type.types.container;

import net.sourceforge.cilib.util.Vectors;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 *
 */
public class MatrixTest {

    @Test(expected=IllegalArgumentException.class)
    public void constructionZeroRow() {
        Matrix.builder().dimensions(0, 1).build();
    }

    @Test(expected=IllegalArgumentException.class)
    public void constructionZeroColumn() {
        Matrix.builder().dimensions(1, 0).build();
    }

    @Test
    public void square() {
        Matrix a = Matrix.builder().dimensions(2, 2).build();
        Assert.assertTrue(a.isSquare());
    }

    @Test
    public void notSquare() {
        Matrix a = Matrix.builder().dimensions(3, 4).build();
        Assert.assertFalse(a.isSquare());
    }

    @Test
    public void valueAt() {
        Matrix a = Matrix.builder().dimensions(1, 2)
            .addRow(1.0, 2.0)
            .build();

        Assert.assertThat(a.valueAt(0, 0), is(1.0));
        Assert.assertThat(a.valueAt(0, 1), is(2.0));
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void invalidValueOf() {
        Matrix a = Matrix.builder().dimensions(1, 1).build();
        a.valueAt(1, 2);
    }

    @Test
    public void getRow() {
        Matrix a = Matrix.builder().dimensions(2, 2)
            .addRow(1.0, 1.0)
            .addRow(2.0, 2.0)
            .build();

        Vector row = a.getRow(0);

        Assert.assertThat(row.getReal(0), is(1.0));
        Assert.assertThat(row.getReal(1), is(1.0));
    }

    @Test
    public void rowNumber() {
        Matrix a = Matrix.builder().dimensions(4, 5).build();
        Assert.assertThat(a.getRows(), is(4));
    }

    @Test
    public void columnNumber() {
        Matrix a = Matrix.builder().dimensions(5, 8).build();
        Assert.assertThat(a.getColumns(), is(8));
    }

    @Test
    public void addition() {
        Matrix a = Matrix.builder().dimensions(2, 2)
            .addRow(1.0, 2.0)
            .addRow(3.0, 4.0)
            .build();
        Matrix b = Matrix.builder().dimensions(2, 2)
            .addRow(1.0, 2.0)
            .addRow(3.0, 4.0)
            .build();

        Matrix c = a.plus(b);

        Assert.assertThat(c.getRow(0), equalTo(Vectors.create(2.0, 4.0)));
        Assert.assertThat(c.getRow(1), equalTo(Vectors.create(6.0, 8.0)));
    }

    @Test(expected=IllegalArgumentException.class)
    public void invalidAddition() {
        Matrix a = Matrix.builder().dimensions(3, 2).build();
        Matrix b = Matrix.builder().dimensions(1, 2).build();
        a.plus(b);
    }

    @Test
    public void subtraction() {
        Matrix a = Matrix.builder().dimensions(2, 2)
            .addRow(2.0, 4.0)
            .addRow(6.0, 8.0)
            .build();
        Matrix b = Matrix.builder().dimensions(2, 2)
            .addRow(1.0, 2.0)
            .addRow(3.0, 4.0)
            .build();

        Matrix c = a.minus(b);

        Assert.assertThat(c.getRow(0), equalTo(Vectors.create(1.0, 2.0)));
        Assert.assertThat(c.getRow(1), equalTo(Vectors.create(3.0, 4.0)));
    }

    @Test(expected=IllegalArgumentException.class)
    public void invalidSubtraction() {
        Matrix a = Matrix.builder().dimensions(2, 2).build();
        Matrix b = Matrix.builder().dimensions(2, 3).build();
        a.minus(b);
    }

    @Test
    public void multiplication() {
        Matrix a = Matrix.builder().dimensions(2, 2)
            .addRow(2.0, 4.0)
            .addRow(6.0, 8.0)
            .build();
        Matrix b = Matrix.builder().dimensions(2, 2)
            .addRow(1.0, 2.0)
            .addRow(3.0, 4.0)
            .build();

        Matrix c = a.times(b);

        Assert.assertThat(c.getRow(0), equalTo(Vectors.create(14.0, 20.0)));
        Assert.assertThat(c.getRow(1), equalTo(Vectors.create(30.0, 44.0)));
    }

    @Test
    public void squareTranspose() {
        Matrix a = Matrix.builder().dimensions(2, 2)
            .addRow(2.0, 4.0)
            .addRow(6.0, 8.0)
            .build();

        Matrix c = a.transpose();

        Assert.assertThat(c.getRow(0), equalTo(Vectors.create(2.0, 6.0)));
        Assert.assertThat(c.getRow(1), equalTo(Vectors.create(4.0, 8.0)));
    }

    @Test
    public void transposeRowVector() {
        Matrix a = Matrix.builder().dimensions(1, 2)
            .addRow(2.0, 4.0)
            .build();

        Matrix c = a.transpose();

        Assert.assertThat(c.getRows(), is(2));
        Assert.assertThat(c.getColumns(), is(1));
    }

    @Test
    public void transposeColumnVector() {
        Matrix a = Matrix.builder().dimensions(2, 1)
            .addRow(2.0)
            .addRow(4.0)
            .build();

        Matrix c = a.transpose();

        Assert.assertThat(c.getRows(), is(1));
        Assert.assertThat(c.getColumns(), is(2));
    }

    @Test
    public void identity() {
        Matrix identity = Matrix.builder().dimensions(4, 4).identity().build();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == j) Assert.assertThat(identity.valueAt(i, j), is(1.0));
                else Assert.assertThat(identity.valueAt(i, j), is(0.0));
            }
        }
    }

    @Test
    public void rotation() {
        double angle = Math.PI / 4.0;
        Matrix matrix = Matrix.builder().dimensions(2, 2).addRow(1.0, 1.0).addRow(1.0, 1.0).build();
        Matrix result = matrix.rotate(angle, 1, 0);

        Assert.assertThat(result.valueAt(0, 0), is(1.414213562373095));
        Assert.assertThat(result.valueAt(0, 1), is(1.1102230246251565E-16));
        Assert.assertThat(result.valueAt(1, 0), is(1.414213562373095));
        Assert.assertThat(result.valueAt(1, 1), is(1.1102230246251565E-16));
    }

    @Test
    public void determinant() {
        Matrix matrix = Matrix.builder().dimensions(3, 3).addRow(2.0, 1.0, 0.0).addRow(1.0, 2.0, -1.0).addRow(3.0, 2.0, 1.0).build();
        double value = matrix.determinant();
        Assert.assertEquals(4.0, value, 0.00001);
    }

    @Test(expected=IllegalStateException.class)
    public void invalidIdentity() {
        Matrix.builder().dimensions(2, 5).identity().build();
    }

    @Test
    public void uniquePositionSetting() {
        Matrix a = Matrix.builder().dimensions(2, 2)
            .valueAt(0, 0, 3.0)
            .build();

        Assert.assertThat(a.valueAt(0, 0), is(3.0));
        Assert.assertThat(a.valueAt(0, 1), is(0.0));
        Assert.assertThat(a.valueAt(1, 0), is(0.0));
        Assert.assertThat(a.valueAt(1, 1), is(0.0));
    }

    @Test
    public void equal() {
        Matrix.Builder builder = Matrix.builder();
        Matrix a = builder.dimensions(1, 1).valueAt(0, 0, 2.0).build();
        Matrix b = builder.dimensions(1, 1).valueAt(0, 0, 2.0).build();

        Assert.assertTrue(a.equals(b));
    }

    @Test
    public void hash() {
        Matrix.Builder builder = Matrix.builder();
        Matrix a = builder.dimensions(1, 1).valueAt(0, 0, 2.0).build();
        Matrix b = builder.dimensions(1, 1).valueAt(0, 0, 2.0).build();

        Assert.assertTrue(a.hashCode() == b.hashCode());
    }

}
