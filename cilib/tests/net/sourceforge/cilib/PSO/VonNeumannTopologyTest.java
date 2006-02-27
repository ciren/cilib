/*
 * VonNeumannTopologyTest.java
 * JUnit based test
 *
 * Created on January 21, 2003, 4:45 PM
 *
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

package net.sourceforge.cilib.PSO;

import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sourceforge.cilib.Problem.Fitness;
import net.sourceforge.cilib.Problem.OptimisationProblem;

/**
 *
 * @author espeer
 */
public class VonNeumannTopologyTest extends TestCase {
    
    public VonNeumannTopologyTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(VonNeumannTopologyTest.class);
        
        return suite;
    }
    
    public void setUp() {
    	empty = new VonNeumannTopology();
    	square = new VonNeumannTopology();
    	
    	for (int i = 0; i < 9; ++i) {
    		square.add(new DumbParticle(i + 1));
    	}
    	
    	irregular = new VonNeumannTopology();
    	
    	for (int i = 0; i < 10; ++i) {
    		irregular.add(new DumbParticle(i + 1));
    	}
    }
       
    public void testIteration() {
    	Iterator i = empty.particles();
    	assertFalse(i.hasNext());
    	
    	try {
    		i.next();
    		fail("NoSuchElementException should have been thown");
    	}
    	catch (NoSuchElementException ex) { }
    	
    	i = square.particles();
    	int count = 0;
    	while (i.hasNext()) {
    		Particle particle = (Particle) i.next();
    		assertEquals(id[count], particle.getId());
    		++count;
    	}
    	assertEquals(9, count);
    	
    	i = irregular.particles();
    	count = 0;
    	while (i.hasNext()) {
    		Particle particle = (Particle) i.next();
    		assertEquals(id[count], particle.getId());
    		++count;
    	}
    	assertEquals(10, count);
    }
    
    public void testNeighbourhoodIteration() {
    	Iterator i = square.particles();
    	Particle p = null;
    	for (int c = 0; c < 5; ++c) {
    		p = (Particle) i.next();
    	}
    	assertEquals(4, p.getId());

    	Iterator j = square.neighbourhood(i);
    	
    	int count = 0;
    	int nid[] = {4, 3, 8, 6, 2};
    	while (j.hasNext()) {
    		p = (Particle) j.next();
    		assertEquals(nid[count], p.getId());
    		++count;
    	}
    	assertEquals(5, count);
    	
    	try {
    		j.next();
    		fail("NoSuchElementException should have been thown");
    	}
    	catch (NoSuchElementException ex) { }
    
    	i = irregular.particles();
    	p = (Particle) i.next();
    	assertEquals(1, p.getId());
    	
    	j = irregular.neighbourhood(i);
    	
    	count = 0;
    	int nnid[] = {1, 10, 3, 2, 7};
    	while (j.hasNext()) {
    		p = (Particle) j.next();
    		assertEquals(nnid[count], p.getId());
    		++count;
    	}
    	assertEquals(5, count);
    	
    	for (int c = 0; c < 8; ++c) {
    		p = (Particle) i.next();
    	}
    	assertEquals(9, p.getId());
    	
    	j = irregular.neighbourhood(i);
    	
    	count = 0;
    	int nnnid[] = {9, 8, 5, 7, 6};
    	while (j.hasNext()) {
    		p = (Particle) j.next();
    		assertEquals(nnnid[count], p.getId());
    		++count;
    	}
    	assertEquals(5, count);
    	
    	p = (Particle) i.next();
    	assertEquals(10, p.getId());
    	
    	j = irregular.neighbourhood(i);
    	
    	count = 0;
    	int nnnnid[] = {10, 5, 10, 1, 10};
    	while (j.hasNext()) {
    		p = (Particle) j.next();
    		assertEquals(nnnnid[count], p.getId());
    		++count;
    	}
    	assertEquals(5, count);
    	
    	
    }
    
    private Topology empty;
    private Topology square;
    private Topology irregular;
    
    private int[] id = {1, 3, 7, 2, 4, 8, 5, 6, 9, 10};
    
    private class DumbParticle implements Particle {

    	private int id;
    	
    	public DumbParticle(int id) {
    		this.id = id;
    	}
    	
		public int getId() {
			return id;
		}
		
		public Object clone() throws CloneNotSupportedException {
			return super.clone();
		}

		public void setFitness(Fitness fitness) {
		}

		public Fitness getFitness() {
			return null;
		}

		public Fitness getBestFitness() {
			return null;
		}

		public int getDimension() {
			return 0;
		}

		public void initialise(OptimisationProblem problem, Initialiser i) {
		}

		public double[] getPosition() {
			return null;
		}

		public double[] getBestPosition() {
			return null;
		}

		public double[] getVelocity() {
			return null;
		}

		public void setNeighbourhoodBest(Particle particle) {
		}

		public Particle getNeighbourhoodBest() {
			return null;
		}

		public void move() {
		}

		public void updateVelocity(VelocityUpdate vu) {
		}

		public Particle getDecorator(Class decorator) {
			return null;
		}
    	
    }
    
}
