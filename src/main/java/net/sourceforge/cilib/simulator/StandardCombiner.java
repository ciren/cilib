/**
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
package net.sourceforge.cilib.simulator;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author gpampara
 * @TODO: This class should probably use a divide and conquer startegy to combine files as needed
 */
class StandardCombiner implements MeasurementCombiner {

    @Override
    public void combine(String resultFilename, List<String> descriptions, List<File> partials) {
        Preconditions.checkArgument(descriptions.size() >= 1);
        Preconditions.checkArgument(partials.size() >= 1);

        try {
            FileWriter writer = new FileWriter(resultFilename);
            int columnId = 0;
            writer.write("# " + columnId++ + " - Iterations\n");
            for (int i = 0; i < partials.size(); i++) {
                for (String description : descriptions) {
                    writer.write("# " + columnId + " - " + description + " (" + (columnId - 1) + ")\n");
                    columnId++;
                }
            }
            writeDataLines(writer, partials);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void writeDataLines(FileWriter writer, List<File> partials) {
        List<BufferedReader> readers = Lists.newArrayListWithCapacity(partials.size());
        for (File file : partials) {
            try {
                readers.add(new BufferedReader(new FileReader(file)));
            } catch (FileNotFoundException ex) {
               throw new RuntimeException(ex);
            }
        }

        try {
            BufferedReader checkReader = readers.get(0); // There will always be at least 1
            String checkLine = checkReader.readLine();
            List<BufferedReader> loopingList = readers.subList(1, readers.size());
            while (checkLine != null) {
                StringBuilder builder = new StringBuilder();
                builder.append(checkLine);
                for (BufferedReader reader : loopingList) {
                    String string = reader.readLine();
                    builder.append(string.substring(string.indexOf(" ")));
                }
                builder.append("\n");
                writer.write(builder.toString());
                builder = new StringBuilder();
                checkLine = checkReader.readLine();
            }

            for (BufferedReader reader : readers) {
                reader.close();
            }

            for (File file : partials) {
                file.delete();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
