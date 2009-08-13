/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.problem.dataset;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import net.sourceforge.cilib.type.types.container.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.AfterClass;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Theuns Cloete
 */
public class LocalDataSetTest {

    private static LocalDataSet dataSet;

    @BeforeClass
    public static void setUpClass() throws Exception {
        dataSet = new LocalDataSet();
        dataSet.setIdentifier(LocalDataSet.class.getResource("local.data.set.csv").getFile());
        dataSet.setDelimiter(",");
        dataSet.setBeginIndex(0);
        dataSet.setEndIndex(2);
        dataSet.setClassIndex(3);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        dataSet = null;
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testGetData() {
        byte [] byteArray = dataSet.getData();

        assertThat(byteArray, notNullValue());
        assertThat(byteArray.length, is(146));

        assertThat(byteArray[0], is((byte) '1'));
        assertThat(byteArray[1], is((byte) ','));
        assertThat(byteArray[2], is((byte) '2'));
        assertThat(byteArray[3], is((byte) ','));
        assertThat(byteArray[4], is((byte) '3'));
        assertThat(byteArray[5], is((byte) ','));
        assertThat(byteArray[6], is((byte) 'o'));
        assertThat(byteArray[7], is((byte) 'n'));
        assertThat(byteArray[8], is((byte) 'e'));
        assertThat(byteArray[9], is((byte) '\n'));

        assertThat(byteArray[117], is((byte) '2'));
        assertThat(byteArray[118], is((byte) '8'));
        assertThat(byteArray[119], is((byte) ','));
        assertThat(byteArray[120], is((byte) '2'));
        assertThat(byteArray[121], is((byte) '9'));
        assertThat(byteArray[122], is((byte) ','));
        assertThat(byteArray[123], is((byte) '3'));
        assertThat(byteArray[124], is((byte) '0'));
        assertThat(byteArray[125], is((byte) ','));
        assertThat(byteArray[126], is((byte) 't'));
        assertThat(byteArray[127], is((byte) 'e'));
        assertThat(byteArray[128], is((byte) 'n'));
        assertThat(byteArray[129], is((byte) '\n'));

        assertThat(byteArray[130], is((byte) '1'));
        assertThat(byteArray[131], is((byte) ','));
        assertThat(byteArray[132], is((byte) '2'));
        assertThat(byteArray[133], is((byte) ','));
        assertThat(byteArray[134], is((byte) '3'));
        assertThat(byteArray[135], is((byte) ','));
        assertThat(byteArray[136], is((byte) 'a'));
        assertThat(byteArray[137], is((byte) 'n'));
        assertThat(byteArray[138], is((byte) 'o'));
        assertThat(byteArray[139], is((byte) 't'));
        assertThat(byteArray[140], is((byte) 'h'));
        assertThat(byteArray[141], is((byte) 'e'));
        assertThat(byteArray[142], is((byte) 'r'));
        assertThat(byteArray[143], is((byte) 'O'));
        assertThat(byteArray[144], is((byte) 'n'));
        assertThat(byteArray[145], is((byte) 'e'));

        assertThat(byteArray[146], is((byte) '\0'));
    }

    @Test
    public void testGetInputStream() throws IOException {
        InputStream is = dataSet.getInputStream();

        try {
            assertThat(is, notNullValue());
            assertThat(is.read(), is((int) '1'));
            assertThat(is.read(), is((int) ','));
            assertThat(is.read(), is((int) '2'));
            assertThat(is.read(), is((int) ','));
            assertThat(is.read(), is((int) '3'));
            assertThat(is.read(), is((int) ','));
            assertThat(is.read(), is((int) 'o'));
            assertThat(is.read(), is((int) 'n'));
            assertThat(is.read(), is((int) 'e'));
            assertThat(is.read(), is((int) '\n'));

            assertThat(is.skip(107), is(107L));
            assertThat(is.read(), is((int) '2'));
            assertThat(is.read(), is((int) '8'));
            assertThat(is.read(), is((int) ','));
            assertThat(is.read(), is((int) '2'));
            assertThat(is.read(), is((int) '9'));
            assertThat(is.read(), is((int) ','));
            assertThat(is.read(), is((int) '3'));
            assertThat(is.read(), is((int) '0'));
            assertThat(is.read(), is((int) ','));
            assertThat(is.read(), is((int) 't'));
            assertThat(is.read(), is((int) 'e'));
            assertThat(is.read(), is((int) 'n'));
            assertThat(is.read(), is((int) '\n'));

            assertThat(is.read(), is((int) '1'));
            assertThat(is.read(), is((int) ','));
            assertThat(is.read(), is((int) '2'));
            assertThat(is.read(), is((int) ','));
            assertThat(is.read(), is((int) '3'));
            assertThat(is.read(), is((int) ','));
            assertThat(is.read(), is((int) 'a'));
            assertThat(is.read(), is((int) 'n'));
            assertThat(is.read(), is((int) 'o'));
            assertThat(is.read(), is((int) 't'));
            assertThat(is.read(), is((int) 'h'));
            assertThat(is.read(), is((int) 'e'));
            assertThat(is.read(), is((int) 'r'));
            assertThat(is.read(), is((int) 'O'));
            assertThat(is.read(), is((int) 'n'));
            assertThat(is.read(), is((int) 'e'));

            assertThat(is.read(), is(-1));
        }
        finally {
            is.close();
        }
    }

    @Test
    public void testParseDataSet() {
        Set<Pattern<Vector>> result = dataSet.parseDataSet();

        assertThat(result, notNullValue());
        assertThat(result.size(), is(11));
        assertThat(result, hasItem(new Pattern<Vector>(Vector.of(1, 2, 3), "one")));
        assertThat(result, hasItem(new Pattern<Vector>(Vector.of(4, 5, 6), "two")));
        assertThat(result, hasItem(new Pattern<Vector>(Vector.of(7, 8, 9), "three")));
        assertThat(result, hasItem(new Pattern<Vector>(Vector.of(10, 11, 12), "four")));
        assertThat(result, hasItem(new Pattern<Vector>(Vector.of(13, 14, 15), "five")));
        assertThat(result, hasItem(new Pattern<Vector>(Vector.of(16, 17, 18), "six")));
        assertThat(result, hasItem(new Pattern<Vector>(Vector.of(19, 20, 21), "seven")));
        assertThat(result, hasItem(new Pattern<Vector>(Vector.of(22, 23, 24), "eight")));
        assertThat(result, hasItem(new Pattern<Vector>(Vector.of(25, 26, 27), "nine")));
        assertThat(result, hasItem(new Pattern<Vector>(Vector.of(28, 29, 30), "ten")));
        assertThat(result, hasItem(new Pattern<Vector>(Vector.of(1, 2, 3), "anotherOne")));
        assertThat(result, not(hasItem(new Pattern<Vector>(Vector.of(31, 32, 33), "blah"))));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetDelimiterCommaNull() {
        LocalDataSet instance = new LocalDataSet();

        instance.setDelimiter(",");
        assertThat(instance.delimiter, is(","));
        instance.setDelimiter(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetDelimiterTabBlank() {
        LocalDataSet instance = new LocalDataSet();

        instance.setDelimiter("\t");
        assertThat(instance.delimiter, is("\t"));
        instance.setDelimiter("");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetBeginIndexZeroNegative() {
        LocalDataSet instance = new LocalDataSet();

        instance.setBeginIndex(0);
        assertThat(instance.beginIndex, is(0));
        instance.setBeginIndex(-5);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testEndIndexTwoNegative() {
        LocalDataSet instance = new LocalDataSet();

        instance.setEndIndex(2);
        assertThat(instance.endIndex, is(2));
        instance.setEndIndex(-5);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetClassIndex() {
        LocalDataSet instance = new LocalDataSet();

        instance.setClassIndex(-1);
        assertThat(instance.classIndex, is(-1));
        instance.setClassIndex(0);
        assertThat(instance.classIndex, is(0));
        instance.setClassIndex(1);
        assertThat(instance.classIndex, is(1));
        instance.setClassIndex(-2);
    }
}
