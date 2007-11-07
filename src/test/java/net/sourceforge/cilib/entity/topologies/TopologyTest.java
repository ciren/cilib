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
