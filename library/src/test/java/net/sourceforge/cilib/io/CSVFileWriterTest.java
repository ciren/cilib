/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io;

import java.io.File;
import java.util.List;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class CSVFileWriterTest {

    private static String testFilePath;

    @BeforeClass
    public static void setTestFilePath() {
        testFilePath = "library/src/test/resources/datasets/iris.data";
    }

    @Test
    public void testWrite() throws CIlibIOException {
        DataTableBuilder dataTableBuilder = new DataTableBuilder(new DelimitedTextFileReader());
        dataTableBuilder.getDataReader().setSourceURL(testFilePath);
        dataTableBuilder.buildDataTable();
        DataTable<List<String>,List<String>> dataTable = dataTableBuilder.getDataTable();

        CSVFileWriter writer = new CSVFileWriter();
        writer.setDestinationURL("./test.csv");
        writer.open();
        writer.write(dataTable);
        writer.close();

        File file = new File("./test.csv");
        Assert.assertTrue(file.length() > 0);

        testFilePath = "./test.csv";
        dataTableBuilder = new DataTableBuilder(new DelimitedTextFileReader());
        dataTableBuilder.getDataReader().setSourceURL(testFilePath);
        dataTableBuilder.buildDataTable();
        dataTable = dataTableBuilder.getDataTable();

        Assert.assertEquals(150, dataTable.getNumRows());
        Assert.assertEquals(5, dataTable.getNumColums());

        file.delete();
    }
}
