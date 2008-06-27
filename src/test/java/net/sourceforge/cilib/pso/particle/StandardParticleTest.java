package net.sourceforge.cilib.pso.particle;

import static org.junit.Assert.*;
import net.sourceforge.cilib.entity.Particle;

import org.junit.Test;

public class StandardParticleTest {
	
	@Test
	public void equals() {
		Particle p1 = new StandardParticle();
		Particle p2 = new StandardParticle();
		
		p1.setNeighbourhoodBest(p1);
		p2.setNeighbourhoodBest(p1);
		
		assertTrue(p1.equals(p2));
		assertTrue(p1.hashCode() == p2.hashCode());
		assertFalse(p1.equals(null));
	}
	
	@Test
	public void hashCodes() {
		Particle p1 = new StandardParticle();
		Particle p2 = new StandardParticle();
		
		assertTrue(p1.hashCode() == p2.hashCode());
	}

}
