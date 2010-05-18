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


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.StandardDataTable;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.exception.CIlibIOException;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.type.types.Bit;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test the PatternConversionOperator
 * @author andrich
 */
public class PatternConversionOperatorTest {

    @Test
    public void testOperationSingleClass() throws CIlibIOException {
        StandardDataTable<Type> typedDataTable;
        DataTable dataTable = new StandardDataTable<StringType>();
        ArrayList<StandardPattern> referenceList = new ArrayList<StandardPattern>();

        StandardPattern referencePattern = new StandardPattern();
        Vector featureVector = new Vector();
        List<String> tokens = Arrays.asList("0","0.2","0.3E-6","TRUE","-0.5E-2","class1");
        dataTable.addRow(tokens);
        List<Type> row = new ArrayList<Type>();
        row.add(new Real(new Double(tokens.get(0))));
        featureVector.add(new Real(new Double(tokens.get(0))));
        row.add(new Real(new Double(tokens.get(1))));
        featureVector.add(new Real(new Double(tokens.get(1))));
        row.add(new Real(new Double(tokens.get(2))));
        featureVector.add(new Real(new Double(tokens.get(2))));
        row.add(new Bit(true));
        featureVector.add(new Bit(true));
        row.add(new Real(new Double(tokens.get(4))));
        featureVector.add(new Real(new Double(tokens.get(4))));
        row.add(new StringType(tokens.get(5)));
        referencePattern.setVector(featureVector);
        referencePattern.setTarget(new StringType(tokens.get(5)));
        referenceList.add(referencePattern);

        referencePattern = new StandardPattern();
        featureVector = new Vector();
        tokens = Arrays.asList("1","1.2","1.3E-6","T","-1.5E-3","class2");
        dataTable.addRow(tokens);
        row = new ArrayList<Type>();
        row.add(new Real(new Double(tokens.get(0))));
        featureVector.add(new Real(new Double(tokens.get(0))));
        row.add(new Real(new Double(tokens.get(1))));
        featureVector.add(new Real(new Double(tokens.get(1))));
        row.add(new Real(new Double(tokens.get(2))));
        featureVector.add(new Real(new Double(tokens.get(2))));
        row.add(new Bit(true));
        featureVector.add(new Bit(true));
        row.add(new Real(new Double(tokens.get(4))));
        featureVector.add(new Real(new Double(tokens.get(4))));
        row.add(new StringType(tokens.get(5)));
        referencePattern.setVector(featureVector);
        referencePattern.setTarget(new StringType(tokens.get(5)));
        referenceList.add(referencePattern);

        TypeConversionOperator operator = new TypeConversionOperator();
        typedDataTable = (StandardDataTable<Type>) operator.operate(dataTable);

        PatternConversionOperator conversionOperator = new PatternConversionOperator();
        StandardPatternDataTable patterns = (StandardPatternDataTable) conversionOperator.operate(typedDataTable);

        for (int i = 0; i < referenceList.size(); i++) {
            Assert.assertEquals(referenceList.get(i), patterns.getRow(i));
        }
    }

    @Test
    public void testOperationMultiClass() throws CIlibIOException {
        StandardDataTable<Type> typedDataTable;
        DataTable dataTable = new StandardDataTable<StringType>();
        ArrayList<StandardPattern> referenceList = new ArrayList<StandardPattern>();

        StandardPattern referencePattern = new StandardPattern();
        Vector featureVector = new Vector();
        Vector targetVector = new Vector();
        List<String> tokens = Arrays.asList("0","0.2","0.3E-6","TRUE","-0.5E-2","0.0","1.0");
        dataTable.addRow(tokens);
        List<Type> row = new ArrayList<Type>();
        row.add(new Real(new Double(tokens.get(0))));
        featureVector.add(new Real(new Double(tokens.get(0))));
        row.add(new Real(new Double(tokens.get(1))));
        featureVector.add(new Real(new Double(tokens.get(1))));
        row.add(new Real(new Double(tokens.get(2))));
        featureVector.add(new Real(new Double(tokens.get(2))));
        row.add(new Bit(true));
        featureVector.add(new Bit(true));
        row.add(new Real(new Double(tokens.get(4))));
        featureVector.add(new Real(new Double(tokens.get(4))));
        row.add(new Real(new Double(tokens.get(5))));
        targetVector.add(new Real(new Double(tokens.get(5))));
        row.add(new Real(new Double(tokens.get(6))));
        targetVector.add(new Real(new Double(tokens.get(6))));
        referencePattern.setVector(featureVector);
        referencePattern.setTarget(targetVector);
        referenceList.add(referencePattern);

        referencePattern = new StandardPattern();
        featureVector = new Vector();
        targetVector = new Vector();
        tokens = Arrays.asList("1","1.2","1.3E-6","T","-1.5E-3","1.0","1.0");
        dataTable.addRow(tokens);
        row = new ArrayList<Type>();
        row.add(new Real(new Double(tokens.get(0))));
        featureVector.add(new Real(new Double(tokens.get(0))));
        row.add(new Real(new Double(tokens.get(1))));
        featureVector.add(new Real(new Double(tokens.get(1))));
        row.add(new Real(new Double(tokens.get(2))));
        featureVector.add(new Real(new Double(tokens.get(2))));
        row.add(new Bit(true));
        featureVector.add(new Bit(true));
        row.add(new Real(new Double(tokens.get(4))));
        featureVector.add(new Real(new Double(tokens.get(4))));
        row.add(new Real(new Double(tokens.get(5))));
        targetVector.add(new Real(new Double(tokens.get(5))));
        row.add(new Real(new Double(tokens.get(6))));
        targetVector.add(new Real(new Double(tokens.get(6))));
        referencePattern.setVector(featureVector);
        referencePattern.setTarget(targetVector);
        referenceList.add(referencePattern);

        TypeConversionOperator operator = new TypeConversionOperator();
        typedDataTable = (StandardDataTable<Type>) operator.operate(dataTable);

        // test normal class index
        PatternConversionOperator conversionOperator = new PatternConversionOperator();
        conversionOperator.setClassIndex(5);
        conversionOperator.setClassLength(2);
        StandardPatternDataTable patterns1 = (StandardPatternDataTable) conversionOperator.operate(typedDataTable);

        for (int i = 0; i < referenceList.size(); i++) {
            Assert.assertEquals(referenceList.get(i), patterns1.getRow(i));
        }

        // test negative class index
        conversionOperator = new PatternConversionOperator();
        conversionOperator.setClassIndex(-1);
        conversionOperator.setClassLength(2);
        StandardPatternDataTable patterns2 = (StandardPatternDataTable) conversionOperator.operate(typedDataTable);
        for (int i = 0; i < referenceList.size(); i++) {
            Assert.assertEquals(referenceList.get(i), patterns2.getRow(i));
        }
    }

}
