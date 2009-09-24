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
