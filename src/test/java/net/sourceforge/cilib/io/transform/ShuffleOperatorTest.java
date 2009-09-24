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

import java.util.List;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.DataTableBuilder;
import net.sourceforge.cilib.io.DelimitedTextFileReader;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.type.types.StringType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author andrich
 */
public class ShuffleOperatorTest {

    private static String testFilePath;

    @BeforeClass
    public static void setTestFilePath() {
        testFilePath = "datasets/iris.data";
    }

    @Test
    public void TestShuffle() throws CIlibIOException {
        DataTableBuilder dataTableBuilder = new DataTableBuilder(new DelimitedTextFileReader());
        dataTableBuilder.getDataReader().setSourceURL(testFilePath);
        dataTableBuilder.buildDataTable();
        DataTable<List<StringType>, List<StringType>> dataTable = dataTableBuilder.getDataTable();
        DataTable<List<StringType>, List<StringType>> reference = (DataTable<List<StringType>, List<StringType>>) dataTable.getClone();
        
        ShuffleOperator operator = new ShuffleOperator();
        operator.operate(dataTable);

        for (int i = 0; i < dataTable.size(); i++) {
            Assert.assertNotSame(reference.getRow(i), dataTable.getRow(i));
        }
    }

}
