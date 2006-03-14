package net.sourceforge.cilib.pso.niching;

import java.util.Collection;

public interface SwarmCreationStrategy<E> {
	
	public Collection<E> create(E mainSwarm, Collection<E> subSwarms);

}
