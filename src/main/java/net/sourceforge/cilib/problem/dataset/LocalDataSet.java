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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import net.sourceforge.cilib.problem.dataset.ClusterableDataSet.Pattern;
import net.sourceforge.cilib.type.types.Real;
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

    protected String fileName = null;
    protected String delimiter = null;
    protected int beginIndex = 0;
    protected int endIndex = 0;
    protected int classIndex = 0;

    public LocalDataSet() {
        super();
        fileName = "<not set>";
        delimiter = "\\s";
    }

    public LocalDataSet(LocalDataSet rhs) {
        super(rhs);
        fileName = new String(rhs.fileName);
    }

    @Override
    public LocalDataSet getClone() {
        return new LocalDataSet(this);
    }

    /**
     * Set the name of the file that represents this dataset on disk.
     *
     * @param fileName the name of the file
     */
    public void setFile(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Get the name of the file that represents this dataset on disk.
     *
     * @return the name of the file
     */
    public String getFile() {
        return fileName;
    }

    /**
     * Get the contents of the file on disk as an array of bytes.
     *
     * @return the contents of the file on disk as an array of bytes
     */
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
    public InputStream getInputStream() {
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(fileName));
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
    public ArrayList<Pattern> parseDataSet() {
        if (beginIndex == endIndex)
            throw new IllegalArgumentException("The begin and end should not be equal");

        ArrayList<Pattern> patterns = new ArrayList<Pattern>();
        BufferedReader br = new BufferedReader(new InputStreamReader(getInputStream()));

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
    private Pattern parseLine(String line) {
        // split the line using the 'delimiter' regular expression
        String[] elements = line.split(delimiter);
        // the elements of the split are stored inside a vector that will form the pattern
        Vector pattern = new Vector(endIndex - beginIndex + 1);

        for (int i = beginIndex; i <= endIndex; i++) {
            pattern.add(new Real(Double.valueOf(elements[i])));
        }

        String clazz = "";

        if (classIndex == -1) {
            clazz = fileName.substring(fileName.lastIndexOf("/") + 1);
        }
        else {
            clazz = elements[classIndex];
        }

        return new Pattern(clazz, pattern);
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
        if (d.equals("") || d == null) {
            throw new IllegalArgumentException("The delimiter cannot be empty");
        }

        delimiter = d;
    }

    /**
     * Sets the index where the elements of the pattern begins.
     *
     * @param bi the starting index
     * @throws IllegalArgumentException when the index is negative
     */
    public void setBeginIndex(int bi) {
        if (bi < 0) {
            throw new IllegalArgumentException("An index cannot be negative");
        }

        beginIndex = bi;
    }

    /**
     * Sets the index where the elements of the pattern ends. This index is inclusive.
     *
     * @param ei the ending index
     * @throws IllegalArgumentException when the index is negative
     */
    public void setEndIndex(int ei) {
        if (ei < 0) {
            throw new IllegalArgumentException("An index cannot be negative");
        }

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
        if (ci < -1) {
            throw new IllegalArgumentException("The classIndex should be >= -1");
        }

        classIndex = ci;
    }
}
