/*
 * AllTest.java
 *
 * Created on May 4 2006
 *
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
package net.sourceforge.cilib.entity.comparator;

import static junit.framework.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.MinimisationFitness;

import org.junit.Test;

/**
 * 
 * @author Gary Pampara
 */
public class AscendingFitnessCompartorTest {
	
	@Test
	public void simpleDataStructure() {
		
		Fitness x [] = { 
				new MinimisationFitness(0.0),
				new MinimisationFitness(1.0),
				new MinimisationFitness(2.0),
				new MinimisationFitness(3.0)
			};
		
		for (int i = 0; i < x.length-1; i++) {
			assertEquals(1, x[i].compareTo(x[i+1]));
		}
		
		List<Fitness> l = Arrays.asList(x);
		
		Collections.sort(l, new AscendingFitnessComparator());
		
	}
	
	
	/**
	 * 
	 * @author Gary Pampara
	 *
	 */
	private class AscendingFitnessComparator implements Comparator<Fitness>{

		public int compare(Fitness f1, Fitness f2) {
			return f2.compareTo(f1);
		}
		
	}

}
