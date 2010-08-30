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

import java.util.Arrays;

import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.StandardDataTable;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.type.types.Bit;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Test the PatternConversionOperator
 * @author andrich
 */
public class PatternConversionOperatorTest {
    @Test
    public void testOperateSingleClass() throws CIlibIOException {
        TypeConversionOperator typeConverter = new TypeConversionOperator();
        DataTable dataTable = new StandardDataTable<StringType>();

        dataTable.addRow(Arrays.asList("0", "0.2", "0.3E-6", "TRUE", "-0.5E-2", "class1"));
        dataTable.addRow(Arrays.asList("1", "1.2", "1.3E-6", "T", "-1.5E-3", "class2"));

        StandardDataTable<Type> typedDataTable = (StandardDataTable<Type>) typeConverter.operate(dataTable);
        PatternConversionOperator patternConverter = new PatternConversionOperator();
        StandardPatternDataTable patterns = (StandardPatternDataTable) patternConverter.operate(typedDataTable);

        assertThat(patterns.getRow(0).getVector().size(), is(5));
        assertThat(patterns.getRow(0).getVector(), equalTo(Vector.of(Real.valueOf(0.0), Real.valueOf(0.2), Real.valueOf(0.3E-6), Bit.valueOf(true), Real.valueOf(-0.5E-2))));
        assertThat(patterns.getRow(0).getTarget(), instanceOf(StringType.class));
        assertThat(patterns.getRow(0).getTarget().toString(), is("class1"));

        assertThat(patterns.getRow(1).getVector().size(), is(5));
        assertThat(patterns.getRow(1).getVector(), equalTo(Vector.of(Real.valueOf(1.0), Real.valueOf(1.2), Real.valueOf(1.3E-6), Bit.valueOf(true), Real.valueOf(-1.5E-3))));
        assertThat(patterns.getRow(1).getTarget(), instanceOf(StringType.class));
        assertThat(patterns.getRow(1).getTarget().toString(), is("class2"));
    }

    @Test
    public void testOperateMultiClassPositiveIndex() throws CIlibIOException {
        TypeConversionOperator typeConverter = new TypeConversionOperator();
        DataTable dataTable = new StandardDataTable<StringType>();

        dataTable.addRow(Arrays.asList("0", "0.2", "0.3E-6", "TRUE", "-0.5E-2", "0.0", "1.0"));
        dataTable.addRow(Arrays.asList("1", "1.2", "1.3E-6", "T", "-1.5E-3", "1.0", "1.0"));

        StandardDataTable<Type> typedDataTable = (StandardDataTable<Type>) typeConverter.operate(dataTable);
        PatternConversionOperator patternConverter = new PatternConversionOperator();

        // test normal class index
        patternConverter.setClassIndex(5);
        patternConverter.setClassLength(2);

        StandardPatternDataTable patterns = (StandardPatternDataTable) patternConverter.operate(typedDataTable);

        assertThat(patterns.getRow(0).getVector().size(), is(5));
        assertThat(patterns.getRow(0).getVector(), equalTo(Vector.of(Real.valueOf(0.0), Real.valueOf(0.2), Real.valueOf(0.3E-6), Bit.valueOf(true), Real.valueOf(-0.5E-2))));
        assertThat(patterns.getRow(0).getTarget(), instanceOf(Vector.class));
        assertThat(patterns.getRow(0).getTarget().toString(), is("[0.0,1.0]"));

        assertThat(patterns.getRow(1).getVector().size(), is(5));
        assertThat(patterns.getRow(1).getVector(), equalTo(Vector.of(Real.valueOf(1.0), Real.valueOf(1.2), Real.valueOf(1.3E-6), Bit.valueOf(true), Real.valueOf(-1.5E-3))));
        assertThat(patterns.getRow(1).getTarget(), instanceOf(Vector.class));
        assertThat(patterns.getRow(1).getTarget().toString(), is("[1.0,1.0]"));
    }

    @Test
    public void testOperateMultiClassNegativeIndex() throws CIlibIOException {
        DataTable dataTable = new StandardDataTable<StringType>();

        dataTable.addRow(Arrays.asList("0", "0.2", "0.3E-6", "TRUE", "-0.5E-2", "0.0", "1.0"));
        dataTable.addRow(Arrays.asList("1", "1.2", "1.3E-6", "T", "-1.5E-3", "1.0", "1.0"));

        TypeConversionOperator typeConverter = new TypeConversionOperator();
        StandardDataTable<Type> typedDataTable = (StandardDataTable<Type>) typeConverter.operate(dataTable);
        PatternConversionOperator patternConverter = new PatternConversionOperator();

        patternConverter.setClassIndex(-1);
        patternConverter.setClassLength(2);

        StandardPatternDataTable patterns = (StandardPatternDataTable) patternConverter.operate(typedDataTable);

        assertThat(patterns.getRow(0).getVector().size(), is(5));
        assertThat(patterns.getRow(0).getVector(), equalTo(Vector.of(Real.valueOf(0.0), Real.valueOf(0.2), Real.valueOf(0.3E-6), Bit.valueOf(true), Real.valueOf(-0.5E-2))));
        assertThat(patterns.getRow(0).getTarget(), instanceOf(Vector.class));
        assertThat(patterns.getRow(0).getTarget().toString(), is("[0.0,1.0]"));

        assertThat(patterns.getRow(1).getVector().size(), is(5));
        assertThat(patterns.getRow(1).getVector(), equalTo(Vector.of(Real.valueOf(1.0), Real.valueOf(1.2), Real.valueOf(1.3E-6), Bit.valueOf(true), Real.valueOf(-1.5E-3))));
        assertThat(patterns.getRow(1).getTarget(), instanceOf(Vector.class));
        assertThat(patterns.getRow(1).getTarget().toString(), is("[1.0,1.0]"));
    }

    @Test
    public void testOperateIgnoreOneIndex() throws CIlibIOException {
        DataTable dataTable = new StandardDataTable<StringType>();

        dataTable.addRow(Arrays.asList("0.1", "0.2", "0.3", "0.4", "0.5", "class1"));
        dataTable.addRow(Arrays.asList("1.0", "2.0", "3.0", "4.0", "5.0", "class2"));

        TypeConversionOperator typeConverter = new TypeConversionOperator();
        PatternConversionOperator patternConverter = new PatternConversionOperator();

        patternConverter.ignoreColumnIndex(2);

        StandardDataTable<Type> typedDataTable = (StandardDataTable<Type>) typeConverter.operate(dataTable);
        StandardPatternDataTable patterns = (StandardPatternDataTable) patternConverter.operate(typedDataTable);

        assertThat(patterns.getRow(0).getVector().size(), is(4));
        assertThat(patterns.getRow(0).getVector(), not(equalTo(Vector.of(0.1, 0.2, 0.3, 0.4, 0.5))));
        assertThat(patterns.getRow(0).getVector(), equalTo(Vector.of(0.1, 0.2, 0.4, 0.5)));
        assertThat(patterns.getRow(0).getTarget(), instanceOf(StringType.class));
        assertThat(patterns.getRow(0).getTarget().toString(), is("class1"));

        assertThat(patterns.getRow(1).getVector().size(), is(4));
        assertThat(patterns.getRow(1).getVector(), not(equalTo(Vector.of(1.0, 2.0, 3.0, 4.0, 5.0))));
        assertThat(patterns.getRow(1).getVector(), equalTo(Vector.of(1.0, 2.0, 4.0, 5.0)));
        assertThat(patterns.getRow(1).getTarget(), instanceOf(StringType.class));
        assertThat(patterns.getRow(1).getTarget().toString(), is("class2"));
    }

    @Test
    public void testOperateIgnoreTwoIndices() throws CIlibIOException {
        DataTable dataTable = new StandardDataTable<StringType>();

        dataTable.addRow(Arrays.asList("0.1", "0.2", "0.3", "0.4", "0.5", "class1"));
        dataTable.addRow(Arrays.asList("1.0", "2.0", "3.0", "4.0", "5.0", "class2"));

        TypeConversionOperator typeConverter = new TypeConversionOperator();
        PatternConversionOperator patternConverter = new PatternConversionOperator();

        patternConverter.ignoreColumnIndex(1);
        patternConverter.ignoreColumnIndex(3);

        StandardDataTable<Type> typedDataTable = (StandardDataTable<Type>) typeConverter.operate(dataTable);
        StandardPatternDataTable patterns = (StandardPatternDataTable) patternConverter.operate(typedDataTable);

        assertThat(patterns.getRow(0).getVector().size(), is(3));
        assertThat(patterns.getRow(0).getVector(), not(equalTo(Vector.of(0.1, 0.2, 0.3, 0.4, 0.5))));
        assertThat(patterns.getRow(0).getVector(), equalTo(Vector.of(0.1, 0.3, 0.5)));
        assertThat(patterns.getRow(0).getTarget(), instanceOf(StringType.class));
        assertThat(patterns.getRow(0).getTarget().toString(), is("class1"));

        assertThat(patterns.getRow(1).getVector().size(), is(3));
        assertThat(patterns.getRow(1).getVector(), not(equalTo(Vector.of(1.0, 2.0, 3.0, 4.0, 5.0))));
        assertThat(patterns.getRow(1).getVector(), equalTo(Vector.of(1.0, 3.0, 5.0)));
        assertThat(patterns.getRow(1).getTarget(), instanceOf(StringType.class));
        assertThat(patterns.getRow(1).getTarget().toString(), is("class2"));
    }

    @Test
    public void testOperateIgnoreThreeIndices() throws CIlibIOException {
        DataTable dataTable = new StandardDataTable<StringType>();

        dataTable.addRow(Arrays.asList("0.1", "0.2", "0.3", "0.4", "0.5", "101", "102", "103"));
        dataTable.addRow(Arrays.asList("1.0", "2.0", "3.0", "4.0", "5.0", "201", "202", "203"));

        TypeConversionOperator typeConverter = new TypeConversionOperator();
        PatternConversionOperator patternConverter = new PatternConversionOperator();

        patternConverter.setClassIndex(5);
        patternConverter.setClassLength(3);
        patternConverter.ignoreColumnIndex(2);
        patternConverter.ignoreColumnIndex(6);

        StandardDataTable<Type> typedDataTable = (StandardDataTable<Type>) typeConverter.operate(dataTable);
        StandardPatternDataTable patterns = (StandardPatternDataTable) patternConverter.operate(typedDataTable);

        assertThat(patterns.getRow(0).getVector().size(), is(4));
        assertThat(patterns.getRow(0).getVector(), not(equalTo(Vector.of(0.1, 0.2, 0.3, 0.4, 0.5))));
        assertThat(patterns.getRow(0).getVector(), equalTo(Vector.of(0.1, 0.2, 0.4, 0.5)));
        assertThat(patterns.getRow(0).getTarget(), instanceOf(Vector.class));
        assertThat(patterns.getRow(0).getTarget().toString(), is("[101.0,103.0]"));

        assertThat(patterns.getRow(1).getVector().size(), is(4));
        assertThat(patterns.getRow(1).getVector(), not(equalTo(Vector.of(1.0, 2.0, 3.0, 4.0, 5.0))));
        assertThat(patterns.getRow(1).getVector(), equalTo(Vector.of(1.0, 2.0, 4.0, 5.0)));
        assertThat(patterns.getRow(1).getTarget(), instanceOf(Vector.class));
        assertThat(patterns.getRow(1).getTarget().toString(), is("[201.0,203.0]"));
    }

    @Test
    public void testOperateIgnoreOnlyClassIndex() throws CIlibIOException {
        DataTable dataTable = new StandardDataTable<StringType>();

        dataTable.addRow(Arrays.asList("0.1", "0.2", "0.3", "0.4", "0.5", "class1"));
        dataTable.addRow(Arrays.asList("1.0", "2.0", "3.0", "4.0", "5.0", "class2"));

        TypeConversionOperator typeConverter = new TypeConversionOperator();
        PatternConversionOperator patternConverter = new PatternConversionOperator();

        patternConverter.ignoreColumnIndex(5);

        StandardDataTable<Type> typedDataTable = (StandardDataTable<Type>) typeConverter.operate(dataTable);
        StandardPatternDataTable patterns = (StandardPatternDataTable) patternConverter.operate(typedDataTable);

        assertThat(patterns.getRow(0).getVector().size(), is(5));
        assertThat(patterns.getRow(0).getVector(), equalTo(Vector.of(0.1, 0.2, 0.3, 0.4, 0.5)));
        assertThat(patterns.getRow(0).getTarget(), instanceOf(StringType.class));
        assertThat(patterns.getRow(0).getTarget().toString(), is("class1"));

        assertThat(patterns.getRow(1).getVector().size(), is(5));
        assertThat(patterns.getRow(1).getVector(), equalTo(Vector.of(1.0, 2.0, 3.0, 4.0, 5.0)));
        assertThat(patterns.getRow(1).getTarget(), instanceOf(StringType.class));
        assertThat(patterns.getRow(1).getTarget().toString(), is("class2"));
    }
}
