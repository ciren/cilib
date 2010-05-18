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

import net.sourceforge.cilib.io.exception.CIlibIOException;
import java.io.IOException;
import java.util.List;

/**
 * Implements a Data Writer that writes a datatable to a local CSV file.
 * @author andrich
 */
public class CSVFileWriter extends FileWriter {

    private String delimiter;
    private boolean writeColumnNames;

    /**
     * Default constructor. Sets the delimiter used to seperate fields to the
     * default (comma).
     */
    public CSVFileWriter() {
        delimiter = ",";
    }

    /**
     * Writes the datatable, writing each row as new line, with data fields seperated
     * by a {@link #delimiter delimiter}.
     * @param dataTable the datatable to write.
     * @throws net.sourceforge.cilib.io.exception.CIlibIOException {@inheritDoc }
     */
    @Override
    public void write(DataTable dataTable) throws CIlibIOException {
        try {
            int size;
            if (writeColumnNames) {
                List<String> names = dataTable.getColumnNames();
                size = names.size();
                for (int i = 0; i < size - 1; i++) {
                    String name = names.get(i);
                    IOUtils.writeStrings(outputBuffer, "\"", name, "\"", delimiter);
                }
                String name = names.get(size - 1);
                IOUtils.writeLine(outputBuffer, "\"" + name + "\"");
                outputBuffer.flip();
                outputChannel.write(outputBuffer);
                outputBuffer.clear();
            }
            int rows = dataTable.getNumRows();
            for (int i = 0; i < rows; i++) {
                List<Object> row = (List<Object>) dataTable.getRow(i);
                size = row.size();
                for (int j = 0; j < size - 1; j++) {
                    Object object = row.get(j);
                    IOUtils.writeStrings(outputBuffer, object.toString(), delimiter);
                }
                Object object = row.get(size - 1);
                IOUtils.writeLine(outputBuffer, object.toString());
            }
            outputBuffer.flip();
            outputChannel.write(outputBuffer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets the delimiter used to seperate data fields.
     * @return the data field delimiter.
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * Sets the delimiter used to seperate data fields.
     * @param delimiter the data field delimiter.
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * Gets writeColumnNames flag: whether column names will be written along
     * with data.
     * @return writeColumnNames flag.
     */
    public boolean isWriteColumnNames() {
        return writeColumnNames;
    }

    /**
     * Sets writeColumnNames flag: whether column names will be written along
     * with data.
     * @param writeColumnNames wether to write column names.
     */
    public void setWriteColumnNames(boolean writeColumnNames) {
        this.writeColumnNames = writeColumnNames;
    }
}
