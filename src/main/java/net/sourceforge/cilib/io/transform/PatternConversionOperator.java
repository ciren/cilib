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
package net.sourceforge.cilib.io.transform;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.StandardDataTable;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Class implements a DataOperator that converts a <u>typed</u> data table into a data table containing
 * StandardPatternDataTable, where each row in the typed table is a pattern in the new table.
 *
 * Type checking is not done, the logical use of the operators is left up to the user.
 *
 * In order for a pattern to be constructed, the index of the class of the pattern in the typed row needs to be known.
 * Some patterns also have an entire vector as the class, hence the class length can also be set. The default class
 * index is the last item in a row, and the default length is 1.
 * @author andrich
 */
public class PatternConversionOperator extends SelectiveDataOperator {
    private Set<Integer> ignoreColumnIndices;
    private int classIndex;
    private int classLength;

    /**
     * Default constructor. Initialise class index to -1, class length to 1 and the ignore column indices set to an
     * empty set.
     */
    public PatternConversionOperator() {
        this.ignoreColumnIndices = Sets.newHashSet();
        this.classIndex = -1;
        this.classLength = 1;
    }

    /**
     * Checks whether i is in the range min to max.
     * @param i number to test.
     * @param min minimum of range.
     * @param max maximum of range (exclusive).
     * @return true if i is in range.
     */
    private boolean isInRange(int i, int min, int max) {
        if (i < min) {
            return false;
        }
        if (i >= max) {
            return false;
        }
        return true;
    }

    /**
     * Converts a StandardDataTable<Type> to a StandardPatternDataTable. Each row represents a StandardPattern, with all
     * items (barring those that are defined as part of the class or those added to the {@link #ignoreColumnIndices}
     * set) being part of the feature vector. All rows defined in the {@link #selectedItems selectedItems} list are
     * processed. If the list is empty, all rows are processed.
     * @param dataTable a StandardDataTable<Type> where each row represents a StandardPattern.
     * @return a StandardPatternDataTable.
     * @throws net.sourceforge.cilib.io.exception.CIlibIOException {@inheritDoc}
     */
    @Override
    public DataTable operate(DataTable dataTable) throws CIlibIOException {
        StandardDataTable<Type> typedTable = (StandardDataTable<Type>) dataTable;
        int rowLength = typedTable.getNumColums();

        // a negative class index indicates the class (target) is at the end of the pattern
        if (classIndex < 0) {
            classIndex = rowLength - classLength;
        }

        int classRange = classIndex + classLength;
        StandardPatternDataTable patterns = new StandardPatternDataTable();

        for (int r = 0, n = typedTable.size(); r < n; ++r) {
            if (selectedItems.isEmpty() || selectedItems.contains(r)) {
                List<Type> row = typedTable.getRow(r);
                Vector data = this.buildPatternData(row, rowLength, classRange);
                Type classification = this.buildPatternTarget(row, classRange);

                patterns.addRow(new StandardPattern(data, classification));
            }
        }
        return patterns;
    }

    /**
     * Build the data part of the pattern and ignore (or skip over) the following indices:
     * <ul>
     * <li>Those that form part of the target</li>
     * <li>Those that are in the {@link #ignoreColumnIndices} set</li>
     * </ul>
     * @return a {@link Vector} representing the data part of the pattern
     */
    private Vector buildPatternData(List<Type> row, int rowLength, int classRange) {
        Vector.Builder data = Vector.newBuilder();

        for (int i = 0; i < rowLength; ++i) {
            if (!isInRange(i, this.classIndex, classRange) && !this.ignoreColumnIndices.contains(i)) {
                data.add((Numeric) row.get(i));
            }
        }
        return data.build();
    }

    /**
     * Build the target part of the pattern. If the {@link #classLength} &gt; then we assume the target is a vector and
     * indices that fall within the class range and are inside the {@link #ignoreColumnIndices} set will be ignored.
     * @return a {@link Vector} representing the target part of the pattern or a single {@link Type} when it turns out
     * that the target size is not &gt; 1.
     */
    private Type buildPatternTarget(List<Type> row, int classRange) {
        Type classification = null;

        if (this.classLength > 1) {
            Vector.Builder target = Vector.newBuilder();

            for (int i = this.classIndex; i < classRange; ++i) {
                if (!this.ignoreColumnIndices.contains(i)) {
                    target.add((Numeric) row.get(i));
                }
            }

            Vector tmp = target.build();

            if (tmp.size() > 1) {
                classification = tmp;
            }
            else {
                classification = tmp.get(0);
            }
        }
        else {
            classification = row.get(this.classIndex);
        }
        return classification;
    }

    /**
     * The index of the feature vector's class.
     * @return the class index.
     */
    public int getClassIndex() {
        return this.classIndex;
    }

    /**
     * Sets the index in a row where the feature vector's class starts. Setting the class index to a negative value
     * indicates that the class (target) is at the end of the pattern.
     * @param classIndex the class index.
     */
    public void setClassIndex(int classIndex) {
        this.classIndex = classIndex;
    }

    /**
     * Gets the length of the class.
     * @return the class length.
     */
    public int getClassLength() {
        return this.classLength;
    }

    /**
     * Sets the length of the class.
     * @param classLength the length of the target class.
     */
    public void setClassLength(int classLength) {
        this.classLength = classLength;
    }

    /**
     * Add the specified index to the set of indices that will be ignored. The ignored indices will not form part of the
     * patterns that are created by this operator. If the ignored index is within the target (or classification) range,
     * then the ignored index will also not form part of the target vector.
     * @param ignore the index that should be ignored
     */
    public void ignoreColumnIndex(int ignore) {
        Preconditions.checkArgument(ignore >= 0, "An index cannot be negative");

        this.ignoreColumnIndices.add(ignore);
    }
}
