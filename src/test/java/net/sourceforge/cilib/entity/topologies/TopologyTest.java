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
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.pso.PSO;

import org.junit.Test;

/**
 * 
 * @author Gary Pampara
 */
public class TopologyTest {
	
	/**
	 * Test the setter method for the IoC container
	 *
	 */
	@Test
	public void testIoCSetterMethod() {
		
		try {
			PSO p = new PSO();
			
			Method m = p.getClass().getMethod("setTopology", new Class[] {Topology.class});
			
			Topology<Individual> top1 = new LBestTopology<Individual>();
			Topology<Particle> top2 = new VonNeumannTopology<Particle>();
			Topology<Entity> top3 = new HypercubeTopology<Entity>();
			
			m.invoke(p, top1);
			assertTrue(p.getTopology() instanceof LBestTopology);
			
			m.invoke(p, top2);
			assertTrue(p.getTopology() instanceof VonNeumannTopology);
			
			m.invoke(p, top3);
			assertTrue(p.getTopology() instanceof HypercubeTopology);
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}		
	}
	
	@Test
	public void testGeneric() {
		PSO p = new PSO();
		List<Entity> list = p.getTopology().asList();
		
		assertEquals(p.getTopology().size(), list.size());
		
		for (Particle particle : p.getTopology()) {
			assertTrue(list.contains(particle));
		}
	}

}
