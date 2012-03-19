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
package net.sourceforge.cilib.type.types;

import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 *
 */
public class StringTypeTest {

    @Test
    public void testClone() {
        StringType t = new StringType("test string");
        StringType clone = t.getClone();
        Assert.assertTrue(t.getString().equals(clone.getString()));
        Assert.assertEquals(t.getString(), clone.getString());
    }

    @Test
    public void testDimensionality() {
        StringType s = new StringType("This is a StringType");
        Assert.assertEquals(1, Types.dimensionOf(s));
    }

    @Test
    public void whitespaceReplacement() {
        StringType type = new StringType("This is a string with whitespace");
        Assert.assertThat(type.toString(), is(equalTo("This_is_a_string_with_whitespace")));
    }

}
