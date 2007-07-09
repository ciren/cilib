package net.sourceforge.cilib.type.types.container;

public interface Tree<E extends Comparable<E>> extends Graph<E> {
	
	public E getKey();
	
	public boolean addSubtree(Tree<E> subTree);
	
	public Tree<E> getSubtree(E element);
	
	public boolean remove(Tree<E> subTree);
	
}
