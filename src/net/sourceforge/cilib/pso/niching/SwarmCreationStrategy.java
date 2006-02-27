package net.sourceforge.cilib.pso.niching;

import java.util.Collection;

import net.sourceforge.cilib.algorithm.PopulationBasedAlgorithm;

public interface SwarmCreationStrategy<E extends PopulationBasedAlgorithm> {
	
	public Collection<E> create(E mainSwarm, Collection<? extends E> subSwarms);

}
