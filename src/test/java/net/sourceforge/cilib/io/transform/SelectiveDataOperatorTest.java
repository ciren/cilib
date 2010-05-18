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
package net.sourceforge.cilib.io.transform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author andrich
 */
public class SelectiveDataOperatorTest {

    @Test
    public void testParseSelectionString() {
        List<Integer> expected = new ArrayList<Integer>();
        List<Integer> list;

        expected.addAll(Arrays.asList(1));
        list = SelectiveDataOperator.parseSelectionString("1 : 1");
        Assert.assertEquals(expected, list);

        expected.clear();
        expected.addAll(Arrays.asList(2,3,4,5));
        list = SelectiveDataOperator.parseSelectionString("2 : 5");
        Assert.assertEquals(expected, list);
    }

}
