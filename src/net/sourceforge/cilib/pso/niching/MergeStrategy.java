package net.sourceforge.cilib.pso.niching;

import java.util.Collection;

import net.sourceforge.cilib.algorithm.PopulationBasedAlgorithm;

public interface MergeStrategy<E extends PopulationBasedAlgorithm> {
	
	//public Collection<E> merge(Collection<? extends E> name);
	public void merge(Collection<? extends E> name);

}
