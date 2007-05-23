package net.sourceforge.cilib.cooperative.populationiterators;

import java.util.List;
import java.util.ListIterator;

import net.sourceforge.cilib.algorithm.Algorithm;

public interface PopulationIterator<E extends Algorithm> extends ListIterator<E> {
	
	public PopulationIterator clone();
	
	public E current();
	
	public void setPopulations(List<E> participants);
	
}
