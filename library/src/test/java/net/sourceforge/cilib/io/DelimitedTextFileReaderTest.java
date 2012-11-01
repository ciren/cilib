/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.type.types.StringType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class DelimitedTextFileReaderTest {

    private static String testFilePath;

    @BeforeClass
    public static void setTestFilePath() {
        testFilePath = "library/src/test/resources/datasets/iris.data";
    }

    @Test
    public void testReading() throws IOException, CIlibIOException {
        //setup reference reader
        BufferedReader reader = new BufferedReader(new java.io.FileReader(testFilePath));

        String delimiter = "\\,";
        //setup test reader
        DelimitedTextFileReader textFileReader = new DelimitedTextFileReader();
        textFileReader.setDelimiter(delimiter);
        textFileReader.setSourceURL(testFilePath);
        textFileReader.open();

        String line = reader.readLine();
        while (line != null) {
            Assert.assertTrue(textFileReader.hasNextRow()); // as long as line is not null
                //there has to be a next row.
            List<StringType> row = textFileReader.nextRow();
            String[] tokens = line.split(delimiter);

            for (int i = 0; i < tokens.length; i++) {
                Assert.assertEquals(tokens[i], row.get(i).toString());
            }

            line = reader.readLine();
        }

        textFileReader.close();
        reader.close();
    }

}
