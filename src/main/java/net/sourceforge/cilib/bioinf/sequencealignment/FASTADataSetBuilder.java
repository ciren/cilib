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
package net.sourceforge.cilib.bioinf.sequencealignment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import net.sourceforge.cilib.problem.dataset.DataSet;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;

/**
 * Builds the input data set that gets fed as sequences to be aligned. Input sequences must be in
 * the FASTA format.
 * @author Fabien Zablocki
 */
public class FASTADataSetBuilder extends DataSetBuilder {
    private static final long serialVersionUID = 766658455852634831L;

    private ArrayList<String> strings; // data structure that holds the input sequence

    /**
     * Create an instance of {@linkplain FASTADataSetBuilder}.
     */
    public FASTADataSetBuilder() {
        this.strings = new ArrayList<String>();
    }

    /**
     * Copy constructor. Copy the given instane.
     * @param copy The instance to copy.
     */
    public FASTADataSetBuilder(FASTADataSetBuilder copy) {
        throw new UnsupportedOperationException("'copy constructor' not implemented for '" + this.getClass().getName() + "'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FASTADataSetBuilder getClone() {
        return new FASTADataSetBuilder(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    // Used to read in the unalignmed sequences in the FASTA format.
    public void initialise() {
        for (Iterator<DataSet> i = this.iterator(); i.hasNext();) {
            DataSet dataSet = i.next();

            String temp; // buffer
            String result = ""; // hold the actual sequence

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(dataSet.getInputStream()));

                temp = new String(in.readLine());
                temp.trim();
                while (temp != null) { // all the sequences in file
                    if (temp.contains(">")) { // it is the description line
                        temp = in.readLine();

                        while (!temp.startsWith(">")) { // collect sequence without space
                            StringTokenizer st = new StringTokenizer(temp, "\u0020");
                            String p = "";

                            while (st.hasMoreElements()) {
                                String token = st.nextToken();
                                p += token;
                            }
                            result += p;
                            temp = in.readLine();

                            if (temp == null)
                                break;
                        }

                        strings.add(result); // adds sequence to the set
                        result = "";
                    }
                }
            }
            catch (IOException ioException) {
                throw new RuntimeException(ioException.getMessage());
            }
            System.out.println("Data set(s) initialization completed, aligning...");
        }
    }

    /**
     * Get the list of build {@linkplain String}s.
     * @return The list of {@linkplain String}s.
     */
    public ArrayList<String> getStrings() {
        return this.strings;
    }
}
