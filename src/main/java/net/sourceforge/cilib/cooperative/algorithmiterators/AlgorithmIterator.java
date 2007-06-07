package net.sourceforge.cilib.cooperative.algorithmiterators;

import java.util.List;
import java.util.ListIterator;

import net.sourceforge.cilib.algorithm.Algorithm;

public interface AlgorithmIterator<E extends Algorithm> extends ListIterator<E> {

	public AlgorithmIterator clone();

	public E current();

	public void setAlgorithms(List<E> algorithms);
}
