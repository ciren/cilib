/*
 * AngleModulationTest.java
 * 
 * Created on Oct 5, 2005
 *
 * Copyright (C) 2003 - 2006 
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
 *
 */
package net.sourceforge.cilib.functions.continuous;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sourceforge.cilib.functions.continuous.AngleModulation;
import net.sourceforge.cilib.functions.continuous.Rastrigin;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Vector;

public class AngleModulationTest extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite(AngleModulationTest.class);
		return suite;
	}
	
	private AngleModulation angle;
	
	
	public void setUp() {
		angle = new AngleModulation();
	}
	
	
	public void testObjectCreation() {
		assertNotNull(angle);
	}
	
	
	public void testObjectDimensionality() {
		angle.setDomain("R(-1000,1000)^4");
		Vector builtRepresentation = (Vector) angle.getDomainRegistry().getBuiltRepresenation();
		
		assertEquals(4, builtRepresentation.getDimension());
	}
	
	
	public void testSetPrecision() {
		angle.setPrecision(4);
		assertEquals(4, angle.getPrecision());
		
		angle.setPrecision(0);
		assertEquals(0, angle.getPrecision());
		
		try {
			angle.setPrecision(-1);
		}
		catch (Exception e) {
		}
		
		reset();
	}
	
	
	public void testGetPrecision() {
		assertEquals(3, angle.getPrecision());
		
		angle.setPrecision(10);
		assertEquals(10, angle.getPrecision());
		
		reset();
	}
	
	
	public void testCalcuateRequiredBits() {
		DomainRegistry registry = new DomainRegistry();
		registry.setDomainString("R(-5.12,-5.12)^30");
		
		angle.setPrecision(3);
		assertEquals(14, angle.getRequiredNumberOfBits(registry));
		
		angle.setPrecision(2);
		assertEquals(10, angle.getRequiredNumberOfBits(registry));
		
		reset();
	}
	
	
	public void testGetDecoratedFunctionDomain() {
		assertTrue(angle.getFunction() == null);
	}
	
	
	public void testSetDecoratedFunctionDomain() {
		angle.setFunction(new Rastrigin());
		assertTrue(angle.getFunction() instanceof Rastrigin);
	}
	
	
	public void testConversionToBitRepresentationLength() {
		angle.setFunction(new Rastrigin());
		
		Vector testVector = new MixedVector();
		testVector.append(new Real(0.0));
		testVector.append(new Real(1.0));
		testVector.append(new Real(1.0));
		testVector.append(new Real(0.0));
		
		//BitArray converted = angle.generateBitString(testVector);
		String converted = angle.generateBitString(testVector);
		
		assertEquals(420, converted.length());
	}
	
	
	public void testBinaryConversion() {
		String test = "1111";
		String test2 = "1010";
		
		assertEquals(15.0, this.angle.valueOf(test));
		assertEquals(15.0, this.angle.valueOf(test, 0, 4));
		assertEquals(3.0, this.angle.valueOf(test, 2));
		assertEquals(10.0, this.angle.valueOf(test2, 0, 4));
	}
	
	
	private void reset() {
		angle.setPrecision(3);
	}

}
