/*
 * NaryTree.java
 * 
 * Copyright (C) 2004 - CIRG@UP 
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science 
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.type.types.container;

import java.util.ArrayList;
import java.util.List;

public class NaryTree<E extends Comparable<E>> extends AbstractTree<E> {
	private static final long serialVersionUID = -1136444941205621381L;
	
	private int degree;
	private List<NaryTree<E>> subTrees;

	public NaryTree() {	
	}
	
	public NaryTree(int degree) {
		this.key = null;
		this.degree = degree;
		this.subTrees = null;
	}
	
	public NaryTree(int degree, E element) {
		this.key = element;
		this.degree = degree;
		this.subTrees = new ArrayList<NaryTree<E>>();
		for (int i = 0; i < degree; i++)
			this.subTrees.add(new NaryTree<E>(degree));
	}
	
	public NaryTree(NaryTree<E> copy) {
		
	}
	
	public NaryTree<E> clone() {
		return new NaryTree<E>(this);
	}
	
	@SuppressWarnings("unchecked")
	public boolean addSubTree(Tree<E> subTree) {
		if (isEmpty())
			throw new UnsupportedOperationException();
		
		for (int i = 0; i < degree; i++) {
			if (!subTrees.get(i).isEmpty())
				continue;
			
			subTrees.set(i, (NaryTree<E>) subTree);
			return true;
		}
		
		return false;
	}

	/**
	 * Return the subTree with the node value of <tt>element</tt>. If
	 * such an subTree does not exist, an empty tree is returned.
	 * 
	 * @param element The element of the subTree to search for.
	 * @return The subtree if found else an empty <tt>Tree</tt> object.
	 */
	public Tree<E> getSubTree(E element) {
		for (int i = 0; i < degree; i++) {
			NaryTree<E> subTree = subTrees.get(i);
			if (subTree.isEmpty())
				continue;
			
			if (subTree.getKey().equals(element))
				return subTree;
		}
		
		return new NaryTree<E>();
	}

	public Tree<E> removeSubTree(E element) {
		if (isEmpty()) 
			throw new UnsupportedOperationException();
		
		NaryTree<E> subTree = (NaryTree<E>) getSubTree(element);
		int index = subTrees.indexOf(subTree);
		subTrees.remove(subTree);
		subTrees.add(index, new NaryTree<E>(degree));
		return subTree;
	}
	
	@Override
	public Tree<E> removeSubTree(int index) {
		NaryTree<E> subTree = (NaryTree<E>) getSubTree(index);
		return removeSubTree(subTree.getKey());
	}

	/**
	 * 
	 */
	public boolean add(E element) {
		NaryTree<E> tree = new NaryTree<E>(degree, element);
		return this.addSubTree(tree);
	}

	public void clear() {
		throw new UnsupportedOperationException("Implementation needed");
	}

	public boolean contains(E element) {
		for (int i = 0; i < degree; i++) {
			Tree<E> subTree = getSubTree(i);
			if (!subTree.isEmpty())
				if (subTree.getKey().equals(element))
					return true;
		}
		
		return false;
	}

	public boolean remove(E element) {
		throw new UnsupportedOperationException("Implementation needed");
	}

	public E remove(int index) {
		throw new UnsupportedOperationException("Implementation needed");
	}

	@Override
	public String toString() {
		throw new UnsupportedOperationException("Implementation needed");
	}

	@Override
	public Tree<E> getSubTree(int index) {
		if (isEmpty())
			throw new UnsupportedOperationException();
		
		return this.subTrees.get(index);
	}

	@Override
	public boolean isLeaf() {
		for (int i = 0; i < degree; i++)
			if (!subTrees.get(i).isEmpty())
				return false;
		
		return true;
	}

	@Override
	public void setKey(E element) {
		if (!isEmpty())
			throw new UnsupportedOperationException();
		
		this.key = element;
		this.subTrees = new ArrayList<NaryTree<E>>();
		for (int i = 0; i < degree; i++) {
			this.subTrees.add(new NaryTree<E>(degree));
		}
	}

	@Override
	public int getDegree() {
		return this.degree;
	}

}
