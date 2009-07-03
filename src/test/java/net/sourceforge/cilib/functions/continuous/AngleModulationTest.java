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
package net.sourceforge.cilib.functions.continuous;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.sourceforge.cilib.functions.continuous.decorators.AngleModulation;
import net.sourceforge.cilib.functions.continuous.unconstrained.Rastrigin;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.StringBasedDomainRegistry;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Test;

/**
 *
 * @author Gary Pampara
 *
 */
public class AngleModulationTest {

    @Test
    public void testObjectDimensionality() {
        AngleModulation angle = new AngleModulation();
        angle.setDomain("R(-1000,1000)^4");
        Vector builtRepresentation = (Vector) angle.getDomainRegistry().getBuiltRepresenation();

        assertEquals(4, builtRepresentation.getDimension());
    }

    @Test(expected=ArithmeticException.class)
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
        registry.setDomainString("R(-5.12,-5.12)^30");

        angle.setPrecision(3);
        assertEquals(14, angle.getRequiredNumberOfBits(registry));

        angle.setPrecision(2);
        assertEquals(10, angle.getRequiredNumberOfBits(registry));
    }

    @Test
    public void testGetDecoratedFunctionDomain() {
        AngleModulation angle = new AngleModulation();
        assertTrue(angle.getFunction() == null);
    }

    @Test
    public void testSetDecoratedFunctionDomain() {
        AngleModulation angle = new AngleModulation();
        angle.setFunction(new Rastrigin());
        assertTrue(angle.getFunction() instanceof Rastrigin);
    }

    @Test
    public void testConversionToBitRepresentationLength() {
        AngleModulation angle = new AngleModulation();
        angle.setFunction(new Rastrigin());

        Vector testVector = new Vector();
        testVector.append(new Real(0.0));
        testVector.append(new Real(1.0));
        testVector.append(new Real(1.0));
        testVector.append(new Real(0.0));

        String converted = angle.generateBitString(testVector);

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
