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
package net.sourceforge.cilib.problem.dataset;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;

import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This class represents a local dataset, i.e. a local file on disk that contains lines that
 * represent patterns of the dataset. It is responsible for parsing this file and building
 * up an {@link ArrayList} of {@link Pattern} objects. It makes use of a few variables to
 * correctly parse the lines in the dataset and construct {@link Pattern} objects from them.
 * The first variable that is of importance is the {@link #delimiter} variable. It specifies
 * the delimiter (actually a regular expression) that is used to split up the elements of a
 * single <i>line</i> of the dataset. The default delimiter is a whitespace character. Once
 * that is done, the {@link #beginIndex} specifies the column number/index where the
 * pattern's data begins. Likewise, the {@link #endIndex} specifies the column number/index
 * where the pattern's data ends. This index is inclusive. Lastly, the {@link #classIndex}
 * specifies the column number/index that represents the pattern's class. If the
 * {@link #classIndex} is <code>-1</code> then it means that the dataset does not have a
 * column for the class of the patterns, and in this case the filename of the dataset is
 * used as the class.
 *
 * @author Edwin Peer
 * @author Theuns Cloete
 */
public class LocalDataSet extends DataSet {
    private static final long serialVersionUID = -3482617012711168661L;

    protected String delimiter = null;
    protected int beginIndex = 0;
    protected int endIndex = 0;
    protected int classIndex = 0;

    public LocalDataSet() {
        delimiter = "\\s";
    }

    public LocalDataSet(LocalDataSet rhs) {
        super(rhs);
        identifier = rhs.identifier;
        delimiter = rhs.delimiter;
        beginIndex = rhs.beginIndex;
        endIndex = rhs.endIndex;
        classIndex = rhs.classIndex;
    }

    @Override
    public LocalDataSet getClone() {
        return new LocalDataSet(this);
    }

    /**
     * Get the contents of the file on disk as an array of bytes.
     *
     * @return the contents of the file on disk as an array of bytes
     */
    @Override
    public byte[] getData() {
        try {
            InputStream is = getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.close();

            return bos.toByteArray();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Get the contents of the file on disk as an {@link InputStream}.
     *
     * @return the contents of the file on disk as an {@link InputStream}
     */
    @Override
    public InputStream getInputStream() {
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(identifier));
            return is;
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Parse the dataset, building up a list containing all the patterns in the dataset.
     *
     * @throws IllegalArgumentException when
     *         <code>{@link #beginIndex} == {@link #endIndex}</code>.
     * @return an {@link ArrayList} of {@link Pattern}s containing all the patterns in this
     *         dataset
     */
    @Override
    public Set<Pattern<Vector>> parseDataSet() {
        Preconditions.checkArgument(beginIndex != endIndex, "beginIndex and endIndex should not be equal");
        Set<Pattern<Vector>> patterns = Sets.newHashSet();
        InputStream is = this.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        System.out.println("#Parsing " + identifier);

        try {
            // every line in a dataset represents a pattern
            String line = br.readLine();

            while (line != null) {
                patterns.add(parseLine(line));
                line = br.readLine();
            }
        }
        catch (IOException io) {
            throw new RuntimeException(io);
        }
        finally {
            try {
                br.close();
            }
            catch (IOException ioe) {
            }

            try {
                is.close();
            }
            catch (IOException ioe) {
            }
        }

        return patterns;
    }

    /**
     * Parse the given line, construct a {@link Pattern} and return it. Use the filename of
     * this dataset as the class when the {@link #classIndex} <code>== -1</code>.
     *
     * @param line a {@link String} representing one line of the {@link DataSet}
     * @return a new {@link Pattern} object of which the class and {@link Pattern#data} has
     *         been configured.
     */
    private Pattern<Vector> parseLine(String line) {
        // split the line using the 'delimiter' regular expression
        String[] elements = line.split(delimiter);
        // the elements of the split are stored inside a vector that will form the pattern
        Vector.Builder pattern = Vector.newBuilder();

        for (int i = beginIndex; i <= endIndex; ++i) {
            pattern.add(Real.valueOf(Double.valueOf(elements[i])));
        }

        String clazz = "";

        if (classIndex == -1) {
            clazz = identifier.substring(identifier.lastIndexOf("/") + 1);
        }
        else {
            clazz = elements[classIndex];
        }

        return new Pattern<Vector>(pattern.build(), clazz);
    }

    /**
     * Sets the regular expression (as a {@link String}} that should be used as delimiter to
     * split a string into the elements of the pattern.
     *
     * @param d the regular expression (as a {@link String}) that should be used as
     *        delimiter
     * @throws IllegalArgumentException when the delimiter is empty ("") or <code>null</code>
     */
    public void setDelimiter(String d) {
        Preconditions.checkArgument(d != null && !d.isEmpty(), "The delimiter cannot be null, empty or blank");

        delimiter = d;
    }

    /**
     * Sets the index where the elements of the pattern begins.
     *
     * @param bi the starting index
     * @throws IllegalArgumentException when the index is negative
     */
    public void setBeginIndex(int bi) {
        Preconditions.checkArgument(bi >= 0, "An index cannot be negative");

        beginIndex = bi;
    }

    /**
     * Sets the index where the elements of the pattern ends. This index is inclusive.
     *
     * @param ei the ending index
     * @throws IllegalArgumentException when the index is negative
     */
    public void setEndIndex(int ei) {
        Preconditions.checkArgument(ei >= 0, "An index cannot be negative");

        endIndex = ei;
    }

    /**
     * Sets the index of the column that represents the class of the pattern. If the index is
     * <code>-1</code> then the filename of the dataset will be used as the class.
     *
     * @throws IllegalArgumentException when the index is <code>&lt;-1</code>
     * @param ci the index where the class resides
     */
    public void setClassIndex(int ci) {
        Preconditions.checkArgument(ci >= -1, "The classIndex should be >= -1");

        classIndex = ci;
    }
}
