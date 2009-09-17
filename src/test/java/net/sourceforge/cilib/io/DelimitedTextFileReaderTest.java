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
 * @author andrich
 */
public class DelimitedTextFileReaderTest {

    private static String testFilePath;

    @BeforeClass
    public static void setTestFilePath() {
        testFilePath = "datasets/iris.data";
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
