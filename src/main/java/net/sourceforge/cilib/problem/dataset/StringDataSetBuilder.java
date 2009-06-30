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
package net.sourceforge.cilib.problem.dataset;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Gary Pampara
 */
public class StringDataSetBuilder extends TextDataSetBuilder {
    private static final long serialVersionUID = 3309485547124815669L;

    private ArrayList<String> strings;
    private String shortestString;
    private String longestString;

    public StringDataSetBuilder() {
        this.strings = new ArrayList<String>();
    }

    public StringDataSetBuilder(StringDataSetBuilder rhs) {
        strings = new ArrayList<String>();
        for (String string : rhs.strings) {
            strings.add(new String(string));
        }
        shortestString = new String(rhs.shortestString);
        longestString = new String(rhs.longestString);
    }

    public StringDataSetBuilder getClone() {
        return new StringDataSetBuilder(this);
    }

    @Override
    public void initialise() {

        for (Iterator<DataSet> i = this.iterator(); i.hasNext();) {
            DataSet dataSet = i.next();

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(dataSet.getInputStream()));
                String result = in.readLine();

                while (result != null) {
                    strings.add(result);

                    result = in.readLine();
                }
            }
            catch (IOException ioException) {
                throw new RuntimeException(ioException);
            }
        }
    }

    public ArrayList<String> getStrings() {
        return this.strings;
    }

    @Override
    public String getShortestString() {
        if (this.shortestString == null)
            calculateStringLengths();

        return this.shortestString;
    }

    @Override
    public String getLongestString() {
        if (this.longestString == null)
            calculateStringLengths();

        return this.longestString;
    }

    /**
     * Iterate through all the strings and determine the length of the longest and shortest strings.
     * If the strings are all the same length, then the <tt>shortestLength</tt> will equal the
     * <tt>longestLength</tt>.
     */
    private void calculateStringLengths() {
        int shortestLength = Integer.MAX_VALUE;
        int longestLength = Integer.MIN_VALUE;

        for (String str : strings) {
            int length = str.length();

            if (length < shortestLength) {
                shortestLength = length;
                this.shortestString = str;
            }

            if (length > longestLength) {
                longestLength = length;
                this.longestString = str;
            }
        }
    }

    @Override
    public int size() {
        return this.strings.size();
    }

    @Override
    public String get(int index) {
        return this.strings.get(index);
    }
}
