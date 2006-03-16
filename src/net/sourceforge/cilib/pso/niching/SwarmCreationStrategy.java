package net.sourceforge.cilib.pso.niching;

import java.util.Collection;
import java.util.List;

import net.sourceforge.cilib.pso.PSO;

public interface SwarmCreationStrategy<E> {
	
	//public Collection<E> create(E mainSwarm, Collection<E> subSwarms);
	
	
	public void create(E mainSwarm, Collection<PSO> subSwarms,List<Double> [] mainSwarmParticleFitnesses);
}
