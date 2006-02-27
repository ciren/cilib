/*
 * DomainParserTest.java
 * 
 * Created on Jul 20, 2005
 *
 * Copyright (C) 2003, 2004 - CIRG@UP 
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
package net.sourceforge.cilib.type;

import net.sourceforge.cilib.type.DomainParser;
import net.sourceforge.cilib.type.types.Vector;
import junit.framework.TestCase;

/**
 * 
 * @author Gary Pampara
 */
public class DomainParserTest extends TestCase {
	
	private DomainParser parser;
	
	public void setUp() {
		parser = DomainParser.getInstance();
	}
	
	public void testParseReal() {
		try {
			parser.parse("R(0,INF)");
			parser.parse("R");
			assertEquals("R", parser.expandDomainString("R"));
			parser.parse("R^6");
			assertEquals("R,R,R,R,R,R", parser.expandDomainString("R^6"));
			parser.parse("R(-9,9)");
			assertEquals("R(-9.0,9.0)", parser.expandDomainString("R(-9,9)"));
			parser.parse("R(-30.0,30.0)^6");
			assertEquals("R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0)", parser.expandDomainString("R(-30.0,30.0)^6"));
		}
		catch (Exception e) {
			fail("Parser cannot handle parsing of R! " + e.getMessage());
		}
	}
	
	
	public void testParseBit() {
		try {
			parser.parse("B");
			assertEquals("B", parser.expandDomainString("B"));
			parser.parse("B^6");
			assertEquals("B,B,B,B,B,B", parser.expandDomainString("B^6"));
		}
		catch (Exception e) {
			fail("Parser cannot handle parsing of B! " + e.getMessage());
		}
	}
	

	public void testParseInteger() {
		try {
			parser.parse("Z");
			assertEquals("Z", parser.expandDomainString("Z"));
			parser.parse("Z(-1,0)");
			assertEquals("Z(-1.0,0.0)", parser.expandDomainString("Z(-1,0)"));
			parser.parse("Z(0,1)");
			assertEquals("Z(0.0,1.0)", parser.expandDomainString("Z(0,1)"));
			parser.parse("Z(-999,999)");
			assertEquals("Z(-999.0,999.0)", parser.expandDomainString("Z(-999,999)"));
			parser.parse("Z^8");
			assertEquals("Z,Z,Z,Z,Z,Z,Z,Z", parser.expandDomainString("Z^8"));
			parser.parse("Z(0,1)^10");
			assertEquals("Z(0.0,1.0),Z(0.0,1.0),Z(0.0,1.0),Z(0.0,1.0),Z(0.0,1.0),Z(0.0,1.0),Z(0.0,1.0),Z(0.0,1.0),Z(0.0,1.0),Z(0.0,1.0)", parser.expandDomainString("Z(0,1)^10"));
		}
		catch (Exception e) {
			fail("Parser cannot handle parsing of Z! " + e.getMessage());			
		}
	}
	
	
	public void testParseString() {
		try {
			parser.parse("T^5");
			assertEquals("T,T,T,T,T", parser.expandDomainString("T^5"));
		}
		catch (Exception e) {
			fail("Parser cannot handle parsing of T! " + e.getMessage());
		}
	}
	
	
	public void testParseComplexDomain() {
		try {
			parser.parse("R(-30.0,30.0)^30,B,Z^6");
			assertEquals("R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),B,Z,Z,Z,Z,Z,Z",
					parser.expandDomainString("R(-30.0,30.0)^30,B,Z^6"));
		}
		catch (Exception e) {
			fail("Parser cannot handle parsing of a complex domain! " + e.getMessage());
		}
	}

	
	/**
	 * This test determines if the parser can successfully parse and build
	 * domains that can be represented as a matrix (also regarded as second order
	 * domains).
	 */
	public void testParseMatrixDomain() {
		try {
			parser.parse("[R(-30.0,30.0)^4]^5,B,B,Z(2,5)");
			assertEquals("[R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0)],[R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0)],[R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0)],[R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0)],[R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0),R(-30.0,30.0)],B,B,Z(2.0,5.0)",
					parser.expandDomainString("[R(-30.0,30.0)^4]^5,B,B,Z(2,5)"));
		}
		catch (Exception e) {
			fail("Parser cannot handle martix representation domains! " + e.getMessage());
		}
	}
	
	
	public void testBuildMatrixDomain() {
		try {
			parser.parse("[R(-30.0,30.0)^4]^5");
			
			Vector matrix = (Vector) parser.getBuiltRepresentation();
			
			// Test the dimensionality
			assertEquals(5, matrix.getDimension());
			
			for (int i = 0; i < matrix.getDimension(); i++) {
				assertEquals(4, ((Vector)matrix.get(i)).getDimension());
			}
			
			// Test object equality
			Vector v1 = (Vector) matrix.get(0);
			Vector v2 = (Vector) matrix.get(1);
			Vector v3 = (Vector) matrix.get(2);
			Vector v4 = (Vector) matrix.get(3);
			Vector v5 = (Vector) matrix.get(4);
			
			assertTrue(assertObjectNotSame(v1, v2));
			assertTrue(assertObjectNotSame(v1, v3));
			assertTrue(assertObjectNotSame(v1, v4));
			assertTrue(assertObjectNotSame(v1, v5));
			assertTrue(assertObjectNotSame(v2, v3));
			assertTrue(assertObjectNotSame(v2, v4));
			assertTrue(assertObjectNotSame(v2, v5));
			assertTrue(assertObjectNotSame(v3, v4));
			assertTrue(assertObjectNotSame(v3, v4));
			assertTrue(assertObjectNotSame(v4, v5));
		}
		
		catch (Exception e) {
			
		}
	}
	
	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	private boolean assertObjectNotSame(Vector v1, Vector v2) {
		
		for (int i = 0; i < v1.getDimension(); i++) {
			//System.out.println("v1.get: " + v1.get(i));
			//System.out.println("v2.get: " + v2.get(i));
			//System.out.println(v1.get(i).equals(v2.get(i)));
			if (v1.get(i).equals(v2.get(i)))
				return false;
		}
		
		return true;
	}

}
