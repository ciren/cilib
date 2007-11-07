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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import net.sourceforge.cilib.functions.continuous.decorators.AngleModulation;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Gary Pampara
 *
 */
public class AngleModulationTest {

	private static AngleModulation angle;
	
	@BeforeClass
	public static void setUp() {
		angle = new AngleModulation();
	}
	
	@Before
	public void resetAngleModulationObject() {
		angle.setPrecision(3);
	}
	
	
	@Test
	public void testObjectCreation() {
		assertNotNull(angle);
	}
	
	@Test
	public void testObjectDimensionality() {
		angle.setDomain("R(-1000,1000)^4");
		Vector builtRepresentation = (Vector) angle.getDomainRegistry().getBuiltRepresenation();
		
		assertEquals(4, builtRepresentation.getDimension());
	}
	
	@Test
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
		
		//reset();
	}
	
	@Test
	public void testGetPrecision() {
		assertEquals(3, angle.getPrecision());
		
		angle.setPrecision(10);
		assertEquals(10, angle.getPrecision());
		
		//reset();
	}
	
	@Test
	public void testCalcuateRequiredBits() {
		DomainRegistry registry = new DomainRegistry();
		registry.setDomainString("R(-5.12,-5.12)^30");
		
		angle.setPrecision(3);
		assertEquals(14, angle.getRequiredNumberOfBits(registry));
		
		angle.setPrecision(2);
		assertEquals(10, angle.getRequiredNumberOfBits(registry));
		
		//reset();
	}
	
	@Test
	public void testGetDecoratedFunctionDomain() {
		assertTrue(angle.getFunction() == null);
	}
	
	@Test
	public void testSetDecoratedFunctionDomain() {
		angle.setFunction(new Rastrigin());
		assertTrue(angle.getFunction() instanceof Rastrigin);
	}
	
	@Test
	public void testConversionToBitRepresentationLength() {
		angle.setFunction(new Rastrigin());
		
		Vector testVector = new Vector();
		testVector.append(new Real(0.0));
		testVector.append(new Real(1.0));
		testVector.append(new Real(1.0));
		testVector.append(new Real(0.0));
		
		//BitArray converted = angle.generateBitString(testVector);
		String converted = angle.generateBitString(testVector);
		
		assertEquals(420, converted.length());
	}
	
	@Test
	public void testBinaryConversion() {
		String test = "1111";
		String test2 = "1010";
		
		assertEquals(15.0, angle.valueOf(test), Double.MIN_NORMAL);
		assertEquals(15.0, angle.valueOf(test, 0, 4), Double.MIN_NORMAL);
		assertEquals(3.0, angle.valueOf(test, 2), Double.MIN_NORMAL);
		assertEquals(10.0, angle.valueOf(test2, 0, 4), Double.MIN_NORMAL);
	}

}
