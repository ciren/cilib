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
package net.sourceforge.cilib.io;

import java.util.List;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for the ARFF File Reader class.
 * @author andrich
 */
public class ARFFFileReaderTest {

    private String testFilePath;
    private StandardDataTable<Type> table;
    private ARFFFileReader reader;

    @Before
    @Test
    public void testReading() throws CIlibIOException {
        testFilePath = "datasets/iris.arff";
        reader = new ARFFFileReader();
        DataTableBuilder builder = new DataTableBuilder(reader);
        builder.setSourceURL(testFilePath);
        table = (StandardDataTable<Type>) builder.buildDataTable();
        Assert.assertEquals(table.getNumRows(), 150);
        Assert.assertEquals(table.getNumColums(), 5);
        Assert.assertEquals("iris", reader.getRelationName());
    }
    
    @Test
    public void testTyping() {
        List<Type> row = table.getRow(0);
        Assert.assertEquals(Real.class, row.get(0).getClass());
        Assert.assertEquals(Real.class, row.get(1).getClass());
        Assert.assertEquals(Real.class, row.get(2).getClass());
        Assert.assertEquals(Real.class, row.get(3).getClass());
        Assert.assertEquals(Int.class, row.get(4).getClass());
    }

    @Test
    public void testNaming() {
        Assert.assertEquals("sepallength", table.getColumnName(0));
        Assert.assertEquals("sepalwidth", table.getColumnName(1));
        Assert.assertEquals("petallength", table.getColumnName(2));
        Assert.assertEquals("petalwidth", table.getColumnName(3));
        Assert.assertEquals("class", table.getColumnName(4));
    }

    @Test
    public void testNominalNaming() {
        int classColumn = 4;
        Assert.assertEquals("Iris-setosa", reader.getNominalString(classColumn, table.getRow(0).get(classColumn)));
        Assert.assertEquals("Iris-versicolor", reader.getNominalString(classColumn, table.getRow(50).get(classColumn)));
        Assert.assertEquals("Iris-virginica", reader.getNominalString(classColumn, table.getRow(100).get(classColumn)));
    }
}
