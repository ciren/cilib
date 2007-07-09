package net.sourceforge.cilib.type.types.container;

import java.util.Iterator;

import net.sourceforge.cilib.container.visitor.Visitor;

public interface Structure<E> extends Iterable<E> {
	
	public boolean add(E element);
	
	public boolean addAll(Structure<E> structure);
	
	public void clear();
	
	public boolean contains(E element);
	
	public boolean isEmpty();
	
	public Iterator<E> iterator();
	
	public boolean remove(E element);
	
	public E remove(int index);
	
	public boolean removeAll(Structure<E> structure);
	
	public int size();
	
	public void accept(Visitor<E> visitor);
	
}
