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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.io.transform.DataOperator;
import net.sourceforge.cilib.io.transform.TypeConversionOperator;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.Type;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the data table builder, also illustrates a typical usage scenario.
 * @author andrich
 */
public class DataTableBuilderTest {

    private static String testFilePath;

    @BeforeClass
    public static void setTestFilePath() {
        testFilePath = "src/test/resources/datasets/iris.data";
    }

    @Test
    public void testBuildDataTable() throws CIlibIOException, IOException {
        //Example use case: Build the data table from a file.
        DataTableBuilder dataTableBuilder = new DataTableBuilder(new DelimitedTextFileReader());
        dataTableBuilder.getDataReader().setSourceURL(testFilePath);
        dataTableBuilder.buildDataTable();
        DataTable<List<StringType>, List<StringType>> dataTable = dataTableBuilder.getDataTable();

        //read file again (setup reference for comparison).
        BufferedReader reader = new BufferedReader(new java.io.FileReader(testFilePath));
        String line = reader.readLine();

        //compare lines in table to lines in file.
        int rowCount = 0;
        while (line != null) {
            List<StringType> row = dataTable.getRow(rowCount);

            String[] tokens = line.split("\\,");
            for (int i = 0; i < tokens.length; i++) {
                Assert.assertEquals(tokens[i], row.get(i).toString());
            }

            line = reader.readLine();
            rowCount++;
        }
        reader.close();
    }

    @Test
    public void testConversion() throws CIlibIOException {
        //Example use case: Build the data table and apply a data operation
        DataTableBuilder dataTableBuilder = new DataTableBuilder(new DelimitedTextFileReader());
        DataOperator operator = new TypeConversionOperator();
        dataTableBuilder.addDataOperator(operator);
        dataTableBuilder.getDataReader().setSourceURL(testFilePath);
        dataTableBuilder.buildDataTable();
        StandardDataTable<Type> dataTable = (StandardDataTable<Type>) dataTableBuilder.getDataTable();
        Assert.assertEquals(Real.class, dataTable.getRow(0).get(0).getClass());
        Assert.assertEquals(StringType.class, dataTable.getRow(0).get(4).getClass());
    }
}
