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
package net.sourceforge.cilib.io;

import java.util.List;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Wiehann Matthysen
 */
public class VectorReaderTest {

    @Test
    public void testReading() throws CIlibIOException {
        VectorReader reader = new VectorReader(Vector.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 5);
        DataTableBuilder builder = new DataTableBuilder(reader);
        StandardDataTable<Type> table = (StandardDataTable<Type>) builder.buildDataTable();
        Assert.assertEquals(table.getNumRows(), 2);
        Assert.assertEquals(table.getNumColums(), 5);

        List<Type> row1 = table.getRow(0);
        Assert.assertEquals(Real.class, row1.get(0).getClass());
        Assert.assertEquals(Real.class, row1.get(1).getClass());
        Assert.assertEquals(Real.class, row1.get(2).getClass());
        Assert.assertEquals(Real.class, row1.get(3).getClass());
        Assert.assertEquals(Real.class, row1.get(4).getClass());

        List<Type> row2 = table.getRow(1);
        Assert.assertEquals(Real.class, row2.get(0).getClass());
        Assert.assertEquals(Real.class, row2.get(1).getClass());
        Assert.assertEquals(Real.class, row2.get(2).getClass());
        Assert.assertEquals(Real.class, row2.get(3).getClass());
        Assert.assertEquals(Real.class, row2.get(4).getClass());

        List<String> columnNames = table.getColumnNames();
        Assert.assertEquals("Column", columnNames.get(0));
        Assert.assertEquals("Column", columnNames.get(1));
        Assert.assertEquals("Column", columnNames.get(2));
        Assert.assertEquals("Column", columnNames.get(3));
        Assert.assertEquals("Column", columnNames.get(4));
    }
}
