package net.sourceforge.cilib.pso.niching;

import java.util.Collection;

import net.sourceforge.cilib.algorithm.PopulationBasedAlgorithm;

public interface AbsorptionStrategy<E extends PopulationBasedAlgorithm> {
	
	public void absorb(E mainSwarm, Collection<? extends E> subSwarms);

}
