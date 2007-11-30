package net.sourceforge.cilib.cooperative.algorithmiterators;

import java.util.List;
import java.util.ListIterator;

import net.sourceforge.cilib.util.Cloneable;

public interface AlgorithmIterator<E> extends ListIterator<E>, Cloneable {

	public AlgorithmIterator<E> getClone();

	public E current();

	public void setAlgorithms(List<E> algorithms);
}
