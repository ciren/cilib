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
package net.sourceforge.cilib.Type;

import junit.framework.TestCase;

public class DomainParserTest extends TestCase {
	
	public void testParseReal() {
		DomainParser parser = DomainParser.getInstance();
		
		try {
			//System.out.println("Test1");
			//parser.parse("R(-30.0,30.0)^30");
			//System.out.println("Test2");
			//parser.parse("R^30");
			//System.out.println("Test3");
			parser.parse("R(0,INF)");
			//System.out.println("Expanded: " + parser.getExpandedRepresentation());
		}
		catch (Exception e) {
			fail();
		}
	}
	
	public void testParseBit() {
		DomainParser parser = DomainParser.getInstance();
		
		parser.parse("B^6");
		//System.out.println(parser.getExpandedRepresentation());
		//System.out.println(parser.getBuiltRepresentation());	
	}

}
