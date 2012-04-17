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
package net.sourceforge.cilib.functions.continuous.decorators;

import net.sourceforge.cilib.functions.continuous.unconstrained.Rastrigin;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import static org.junit.Assert.assertEquals;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;

import org.junit.Test;

/**
 *
 */
public class AngleModulationTest {

    @Test
    public void testObjectDimensionality() {
        AngleModulation angle = new AngleModulation();
        Vector builtRepresentation = (Vector) angle.getDomain().getBuiltRepresenation();

        assertEquals(4, builtRepresentation.size());
    }

    @Test(expected = ArithmeticException.class)
    public void testSetPrecision() {
        AngleModulation angle = new AngleModulation();
        angle.setPrecision(4);
        assertEquals(4, angle.getPrecision());

        angle.setPrecision(0);
        assertEquals(0, angle.getPrecision());

        angle.setPrecision(-1);
    }

    @Test
    public void testCalcuateRequiredBits() {
        AngleModulation angle = new AngleModulation();

        DomainRegistry registry = new StringBasedDomainRegistry();
        registry.setDomainString("R(-5.12:5.12)^30");

        angle.setPrecision(3);
        assertEquals(14, angle.getRequiredNumberOfBits(registry));

        angle.setPrecision(2);
        assertEquals(10, angle.getRequiredNumberOfBits(registry));
    }

    @Test
    public void testSetDecoratedFunctionDomain() {
        AngleModulation angle = new AngleModulation();
        FunctionMinimisationProblem delegate = new FunctionMinimisationProblem();
        delegate.setDomain("R(-5.12:5.12)^30");
        delegate.setFunction(new Rastrigin());
        angle.setProblem(delegate);
        Assert.assertTrue(angle.getProblem().getFunction() instanceof Rastrigin);
    }

    /**
     * This test uses Rastrigin which has the default domain of R(-5.12, 5.12)^30
     *
     * Converting this to bits results in the expected number being:
     */
    @Test
    public void testConversionToBitRepresentationLength() {
        AngleModulation angle = new AngleModulation();
        FunctionMinimisationProblem delegate = new FunctionMinimisationProblem();
        delegate.setDomain("R(-5.12:5.12)^30");
        delegate.setFunction(new Rastrigin());
        angle.setProblem(delegate);

        Vector testVector = new Vector();
        testVector.add(Real.valueOf(0.0));
        testVector.add(Real.valueOf(1.0));
        testVector.add(Real.valueOf(1.0));
        testVector.add(Real.valueOf(0.0));

        String converted = angle.generateBitString(testVector, 14);

        assertEquals(420, converted.length());
    }

    @Test
    public void testBinaryConversion() {
        AngleModulation angle = new AngleModulation();

        String test = "1111";
        String test2 = "1010";

        assertEquals(15.0, angle.valueOf(test), Double.MIN_NORMAL);
        assertEquals(15.0, angle.valueOf(test, 0, 4), Double.MIN_NORMAL);
        assertEquals(3.0, angle.valueOf(test, 2), Double.MIN_NORMAL);
        assertEquals(10.0, angle.valueOf(test2, 0, 4), Double.MIN_NORMAL);
    }
}
