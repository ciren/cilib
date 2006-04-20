/*
 * EuclideanDistanceMeasureTest.java
 * 
 * Created on Mar 7, 2006
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
package net.sourceforge.cilib.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Vector;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Edwin Peer
 * @author Gary Pampara
 */
public class EuclideanDistanceMeasureTest {
	
	private static DistanceMeasure distanceMeasure;
	
	@BeforeClass
	public static void setUp() {
		distanceMeasure = new EuclideanDistanceMeasure();		
	}
	
	@Test
	public void testVectorDistance() {
		Vector v1 = new MixedVector();
		Vector v2 = new MixedVector();
		
		v1.add(new Real(4.0));
		v1.add(new Real(3.0));
		v1.add(new Real(2.0));
		
		v2.add(new Real(2.0));
		v2.add(new Real(3.0));
		v2.add(new Real(4.0));
		
		assertEquals(Math.sqrt(8.0), distanceMeasure.distance(v1, v2));
		
		v1.add(new Real(22.0));
		
		try {
			distanceMeasure.distance(v1, v2);
			fail("Exception is not thrown????");
		}
		catch (IllegalArgumentException i) {}
	}
	
	
	@Test
	public void testCollectionDistance() {
		List<Double> l1 = new ArrayList<Double>();
		List<Double> l2 = new ArrayList<Double>();
		
		l1.add(4.0);
		l1.add(3.0);
		l1.add(2.0);
		
		l2.add(2.0);
		l2.add(3.0);
		l2.add(4.0);
		
		assertEquals(Math.sqrt(8.0), distanceMeasure.distance(l1, l2));
		
		l1.add(11.0);
		
		try {
			distanceMeasure.distance(l1, l2);
			fail("Exception is not thrown????");
		}
		catch (IllegalArgumentException i) {}
	}

}
