/**
 * Copyright (C) 2003 - 2008
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

package net.sourceforge.cilib.entity.topologies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.NoSuchElementException;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.pso.particle.AbstractParticle;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

import org.jmock.Mockery;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Edwin Peer
 */
public class VonNeumannTopologyTest {

    public VonNeumannTopologyTest() {
    }
    
    @BeforeClass
    public static void setUp() {
    	empty = new VonNeumannTopology<Particle>();
    	square = new VonNeumannTopology<Particle>();
    	
    	for (int i = 0; i < 9; ++i) {
    		Particle dumbParticle = new DumbParticle(String.valueOf(i + 1)); 
    		square.add(dumbParticle);
    	}
    	
    	irregular = new VonNeumannTopology<Particle>();
    	
    	for (int i = 0; i < 10; ++i) {
    		Particle dumbParticle = new DumbParticle(String.valueOf(i + 1)); 
    		irregular.add(dumbParticle);
    	}
    }
       
    @Test
    public void testIteration() {
    	Iterator<Particle> i = empty.iterator();
    	assertFalse(i.hasNext());
    	
    	try {
    		i.next();
    		fail("NoSuchElementException should have been thown");
    	}
    	catch (NoSuchElementException ex) { }
    	
    	i = square.iterator();
    	int count = 0;
    	while (i.hasNext()) {
    		DumbParticle particle = (DumbParticle) i.next();
    		assertEquals(String.valueOf(id[count]), particle.getParticleName());
    		++count;
    	}
    	assertEquals(9, count);
    	
    	i = irregular.iterator();
    	count = 0;
    	while (i.hasNext()) {
    		DumbParticle particle = (DumbParticle) i.next();
    		assertEquals(String.valueOf(id[count]), particle.getParticleName());
    		++count;
    	}
    	assertEquals(10, count);
    }
    
    @Test
    public void testNeighbourhoodIteration() {
    	Iterator<Particle> i = square.iterator();
    	DumbParticle p = null;
    	for (int c = 0; c < 5; ++c) {
    		p = (DumbParticle) i.next();
    	}
    	assertEquals("4", p.getParticleName());

    	Iterator<Particle> j = square.neighbourhood(i);
    	
    	int count = 0;
    	int nid[] = {4, 3, 8, 6, 2};
    	while (j.hasNext()) {
    		p = (DumbParticle) j.next();
    		assertEquals(String.valueOf(nid[count]), p.getParticleName());
    		++count;
    	}
    	assertEquals(5, count);
    	
    	try {
    		j.next();
    		fail("NoSuchElementException should have been thown");
    	}
    	catch (NoSuchElementException ex) { }
    
    	i = irregular.iterator();
    	p = (DumbParticle) i.next();
    	assertEquals("1", p.getParticleName());
    	
    	j = irregular.neighbourhood(i);
    	
    	count = 0;
    	int nnid[] = {1, 10, 3, 2, 7};
    	while (j.hasNext()) {
    		p = (DumbParticle) j.next();
    		assertEquals(String.valueOf(nnid[count]), p.getParticleName());
    		++count;
    	}
    	assertEquals(5, count);
    	
    	for (int c = 0; c < 8; ++c) {
    		p = (DumbParticle) i.next();
    	}
    	assertEquals("9", p.getParticleName());
    	
    	j = irregular.neighbourhood(i);
    	
    	count = 0;
    	int nnnid[] = {9, 8, 5, 7, 6};
    	while (j.hasNext()) {
    		p = (DumbParticle) j.next();
    		assertEquals(String.valueOf(nnnid[count]), p.getParticleName());
    		++count;
    	}
    	assertEquals(5, count);
    	
    	p = (DumbParticle) i.next();
    	assertEquals("10", p.getParticleName());
    	
    	j = irregular.neighbourhood(i);
    	
    	count = 0;
    	int nnnnid[] = {10, 5, 10, 1, 10};
    	while (j.hasNext()) {
    		p = (DumbParticle) j.next();
    		assertEquals(String.valueOf(nnnnid[count]), p.getParticleName());
    		++count;
    	}
    	assertEquals(5, count);
    		
    }
    
    private static Topology<Particle> empty;
    private static Topology<Particle> square;
    private static Topology<Particle> irregular;
    
    private int[] id = {1, 3, 7, 2, 4, 8, 5, 6, 9, 10};
    
    private static class DumbParticle extends AbstractParticle {
		private static final long serialVersionUID = 4273664052866515691L;
		
		private String name;
    	
    	public DumbParticle(String name) {
    		this.name = name;
    	}
    	
		public String getParticleName() {
			return name;
		}
		
		public void setId(String id) {
			this.name = id;
		}
		public DumbParticle getClone() {
			return null;
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

		public void initialise(OptimisationProblem problem) {
		}

		public Vector getPosition() {
			return null;
		}

		public Vector getBestPosition() {
			return null;
		}

		public Vector getVelocity() {
			return null;
		}

		public void updateControlParameters() {
			
		}

		public void setNeighbourhoodBest(Particle particle) {
		}

		public Particle getNeighbourhoodBest() {
			return null;
		}

		public void updatePosition() {
		}

		public void updateVelocity() {
		}		
		
		public int compareTo(Entity o) {
			// TODO Auto-generated method stub
			return 0;
		}

		public Type getCandidateSolution() {
			// TODO Auto-generated method stub
			return null;
		}

		public void setDimension(int dim) {
			// TODO Auto-generated method stub
			
		}

		public void setCandidateSolution(Type type) {
			// TODO Auto-generated method stub
			
		}

		public Type getBehaviouralParameters() {
			// TODO Auto-generated method stub
			return null;
		}

		public void setBehaviouralParameters(Type type) {
			// TODO Auto-generated method stub
			
		}

		
		public void setBestPosition(Type bestPosition) {
			// TODO Auto-generated method stub
			
		}

		public void reinitialise() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void calculateFitness(boolean count) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean equals(Object object) {
			if (this == object)
				return true;
			
			if ((object == null) || (this.getClass() != object.getClass()))
				return false;
			
			DumbParticle other = (DumbParticle) object;
			return this.name.equals(other.name);
		}

		@Override
		public int hashCode() {
			int hash = 7;
			hash = 31 * hash + (this.name == null ? 0 : this.name.hashCode());
			return hash;
		}
    	
    }
    
}
