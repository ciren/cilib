/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.simulator;

import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * Create a combined view of the generated data in the normal CIlib text
 * file format.
 *
 * @TODO: This class should probably use a divide and conquer strategy to combine files as needed
 */
public class MeasurementCombiner {

    private final File file;

    MeasurementCombiner(File file) {
        this.file = file;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * This method actually creates a text file, named {@code filename}.
     */
    public void combine(List<String> descriptions, List<File> partials) {
        Preconditions.checkArgument(descriptions.size() >= 1);
        Preconditions.checkArgument(partials.size() >= 1);

        try {
            if(file.getParent() != null) {
                file.getParentFile().mkdirs();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            int columnId = 0;
            writer.write("# " + columnId++ + " - Iterations\n");
            for (String description : descriptions) {
                for (int i = 0; i < partials.size(); i++) {
                    writer.write("# " + columnId + " - " + description + " (" + i + ")\n");
                    columnId++;
                }
            }
            combineData(writer, partials);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private long countLines(File file) {
        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new FileReader(file));
            String lineRead = "";
            while ((lineRead = reader.readLine()) != null) {}
            return reader.getLineNumber();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        return 0;
    }

    /**
     * Combine the actual line data. The data is read line for line and
     * then appended one after the other and then written to the file.
     * <p>
     * The order of the files is maintained so that the data is not corrupted
     * once the writing out to the final result file is performed.
     * @param writer destination of the final output.
     * @param partials list of partial results.
     */
    private void combineData(final BufferedWriter writer, final List<File> partials) {
        List<BufferedReader> readers = Lists.newArrayListWithCapacity(partials.size());
        Collections.sort(partials, new Comparator<File>() {
            public int compare(File t, File t1) {
                return Long.compare(countLines(t1), countLines(t));
            }            
        });
        
        for (File f : partials) {
            try {
                InputStreamReader is = new InputStreamReader(new FileInputStream(f), Charset.forName("UTF-8"));
                readers.add(new BufferedReader(is));
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }

        try {
            BufferedReader checkReader = readers.get(0); // There will always be at least 1
            String checkLine = checkReader.readLine();
            String[] parts = checkLine.split(" ");
            int partsSize = parts.length;
            Entry entry = new Entry(parts[0], parts.length - 1);
            for (int i = 1; i < parts.length; i++) {
                entry.add(i - 1, parts[i]);
            }

            List<BufferedReader> loopingList = readers.subList(1, readers.size());
            while (checkLine != null) {
                for (BufferedReader reader : loopingList) {
                    try {
                        String string = reader.readLine();
                        String[] localParts = string.split(" ");
                        for (int i = 1; i < localParts.length; i++) {
                            entry.add(i - 1, localParts[i]);
                        }
                    } catch (Exception ex) {
                        for (int i = 1; i < partsSize; i++) {
                            entry.add(i - 1, "-");
                        }
                    }
                }

                writer.write(entry.toString());
                writer.newLine();
                checkLine = checkReader.readLine();

                if (checkLine != null) {
                    parts = checkLine.split(" ");
                    entry = new Entry(parts[0], parts.length - 1);
                    for (int i = 1; i < parts.length; i++) {
                        entry.add(i - 1, parts[i]);
                    }
                }
            }

            for (BufferedReader reader : readers) {
                reader.close();
            }

            for (File f : partials) {
                f.delete();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static class Entry {

        private final Multimap<Integer, String> map;
        private final String iteration;

        Entry(String iteration, int categories) {
            this.map = LinkedListMultimap.create(categories);
            this.iteration = iteration;
        }

        void add(Integer key, String value) {
            this.map.put(key, value);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append(iteration);
            for (Integer i : map.keySet()) {
                for (String s : map.get(i)) {
                    builder.append(" ").append(s);
                }
            }
            return builder.toString();
        }
    }
}
