/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types.container;

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

        Assert.assertThat(row.doubleValueOf(0), is(1.0));
        Assert.assertThat(row.doubleValueOf(1), is(1.0));
    }

    @Test
    public void getColumn() {
        Matrix a = Matrix.builder().dimensions(2, 2)
            .addRow(1.0, 1.0)
            .addRow(2.0, 2.0)
            .build();

        Vector row = a.getColumn(0);

        Assert.assertThat(row.doubleValueOf(0), is(1.0));
        Assert.assertThat(row.doubleValueOf(1), is(2.0));
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

        Assert.assertThat(c.getRow(0), equalTo(Vector.of(2.0, 4.0)));
        Assert.assertThat(c.getRow(1), equalTo(Vector.of(6.0, 8.0)));
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

        Assert.assertThat(c.getRow(0), equalTo(Vector.of(1.0, 2.0)));
        Assert.assertThat(c.getRow(1), equalTo(Vector.of(3.0, 4.0)));
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

        Assert.assertThat(c.getRow(0), equalTo(Vector.of(14.0, 20.0)));
        Assert.assertThat(c.getRow(1), equalTo(Vector.of(30.0, 44.0)));
    }

    @Test
    public void matrixVectorMultiplication() {
        Matrix a = Matrix.builder().dimensions(3, 3)
            .addRow(2.0, 0.0, 0.0)
            .addRow(0.0, 3.0, 0.0)
            .addRow(0.0, 0.0, 4.0)
            .build();

        Vector v = Vector.of(7.0, 6.0, 3.0);
        Assert.assertThat(a.multiply(v), equalTo(Vector.of(14.0, 18.0, 12.0)));
    }

    @Test
    public void squareTranspose() {
        Matrix a = Matrix.builder().dimensions(2, 2)
            .addRow(2.0, 4.0)
            .addRow(6.0, 8.0)
            .build();

        Matrix c = a.transpose();

        Assert.assertThat(c.getRow(0), equalTo(Vector.of(2.0, 6.0)));
        Assert.assertThat(c.getRow(1), equalTo(Vector.of(4.0, 8.0)));
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

    @Test
    public void singular() {
        Matrix a = Matrix.builder().dimensions(2, 2)
            .addRow(2.0, 1.0)
            .addRow(2.0, 1.0)
            .build();

        Assert.assertTrue(a.isSingular());

        Matrix b = Matrix.builder().dimensions(5, 5).identity().build();

        Assert.assertTrue(!b.isSingular());
    }

    @Test
    public void subMatrix() {
        Matrix a = Matrix.builder().dimensions(2, 2)
            .addRow(1.0, 2.0)
            .addRow(3.0, 4.0)
            .build();

        Matrix b = a.subMatrix(0,0);

        Assert.assertEquals(b.getRows(), 1);
        Assert.assertThat(b.valueAt(0,0), is(4.0));

        a = Matrix.builder().dimensions(3, 3)
            .addRow(1.0, 2.0, 3.0)
            .addRow(4.0, 5.0, 6.0)
            .addRow(7.0, 8.0, 9.0)
            .build();

        b = a.subMatrix(1,1);

        Assert.assertThat(b.getRows(), is(2));
        Assert.assertThat(b.getRow(0), equalTo(Vector.of(1.0, 3.0)));
        Assert.assertThat(b.getRow(1), equalTo(Vector.of(7.0, 9.0)));
    }

    @Test(expected=IllegalArgumentException.class)
    public void illegalSubMatrixIndicies() {
        Matrix a = Matrix.builder().dimensions(2, 2).build().subMatrix(-1, 5);
    }

    @Test(expected=IllegalStateException.class)
    public void illegalSubMatrixSize() {
        Matrix a = Matrix.builder().dimensions(1, 1).build().subMatrix(0, 0);
    }

    @Test
    public void cofactor() {
        Matrix a = Matrix.builder().dimensions(2, 2)
            .addRow(1.0, 2.0)
            .addRow(3.0, 4.0)
            .build();

        Matrix b = a.cofactor();

        Assert.assertThat(b.getRow(0), equalTo(Vector.of(4.0, -3.0)));
        Assert.assertThat(b.getRow(1), equalTo(Vector.of(-2.0, 1)));

        Matrix c = Matrix.builder().dimensions(3, 3)
            .addRow(1.0, 2.0, 3.0)
            .addRow(4.0, 5.0, 6.0)
            .addRow(7.0, 8.0, 9.0)
            .build();

        Matrix d = c.cofactor();

        double e = 0.000001;
        Assert.assertEquals(d.valueAt(0,1), 6.0, e);
        Assert.assertEquals(d.valueAt(0,2), -3.0, e);
        Assert.assertEquals(d.valueAt(1,1), -12.0, e);
        Assert.assertEquals(d.valueAt(1,2), 6.0, e);
        Assert.assertEquals(d.valueAt(2,1), 6.0, e);
        Assert.assertEquals(d.valueAt(2,2), -3.0, e);
    }

    @Test(expected=IllegalStateException.class)
    public void invalidCofactor() {
        Matrix a = Matrix.builder().dimensions(2, 3).build().cofactor();
    }

    @Test
    public void inverse() {
        Matrix a = Matrix.builder().dimensions(2, 2).identity().build();
        Matrix b = a.inverse();

        double e = 0.000001;
        Assert.assertEquals(b.valueAt(0,0), 1.0, e);
        Assert.assertEquals(b.valueAt(1,0), 0.0, e);

        a = Matrix.builder().dimensions(2, 2)
            .addRow(4.0, 1.0)
            .addRow(2.0, -1.0)
            .build();

        b = a.inverse();

        Assert.assertEquals(b.valueAt(0,0), 1.0 / 6, e);
        Assert.assertEquals(b.valueAt(1,1), -4.0 / 6, e);

        a = Matrix.builder().dimensions(3, 3)
            .addRow(1.0, 2.0, 3.0)
            .addRow(4.0, 5.0, 6.0)
            .addRow(7.0, 8.0, 10.0)
            .build();

        b = a.inverse();

        Assert.assertEquals(b.valueAt(1,1), 11.0 / 3, e);
        Assert.assertEquals(b.valueAt(1,2), -6.0 / 3, e);
        Assert.assertEquals(b.valueAt(2,0), 3 / 3, e);
    }

    @Test(expected=IllegalStateException.class)
    public void invalidSingularInverse() {
        Matrix a = Matrix.builder().dimensions(3, 3)
            .addRow(1.0, 2.0, 3.0)
            .addRow(4.0, 5.0, 6.0)
            .addRow(7.0, 8.0, 9.0)
            .build().inverse();
    }

    @Test(expected=IllegalStateException.class)
    public void invalidNonSquareInverse() {
        Matrix a = Matrix.builder().dimensions(1, 2)
            .addRow(1.0, 2.0)
            .build().inverse();
    }

}
