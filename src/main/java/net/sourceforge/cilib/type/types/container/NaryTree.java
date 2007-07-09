package net.sourceforge.cilib.type.types.container;

import java.util.Iterator;

import net.sourceforge.cilib.container.visitor.Visitor;

public class NaryTree<E extends Comparable<E>> implements Tree<E> {
	private static final long serialVersionUID = -1136444941205621381L;

	public NaryTree() {
		
	}
	
	public NaryTree(NaryTree copy) {
		
	}
	
	public NaryTree<E> clone() {
		return new NaryTree<E>(this);
	}
	
	public boolean addSubtree(Tree<E> subTree) {
		// TODO Auto-generated method stub
		return false;
	}

	public E getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public Tree<E> getSubtree(E element) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean remove(Tree<E> subTree) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean addEdge(E a, E b) {
		// TODO Auto-generated method stub
		return false;
	}

	public int edges() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isConnected(E a, E b) {
		// TODO Auto-generated method stub
		return false;
	}

	public int verticies() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void accept(Visitor<E> visitor) {
		// TODO Auto-generated method stub
		
	}

	public boolean add(E element) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean addAll(Structure<E> structure) {
		// TODO Auto-generated method stub
		return false;
	}

	public void clear() {
		// TODO Auto-generated method stub
		
	}

	public boolean contains(E element) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean remove(E element) {
		// TODO Auto-generated method stub
		return false;
	}

	public E remove(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean removeAll(Structure<E> structure) {
		// TODO Auto-generated method stub
		return false;
	}

	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getDimension() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getRepresentation() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isInsideBounds() {
		// TODO Auto-generated method stub
		return false;
	}

	public void randomise() {
		// TODO Auto-generated method stub
		
	}

	public void reset() {
		// TODO Auto-generated method stub
		
	}
	
	
}
