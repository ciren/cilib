/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
