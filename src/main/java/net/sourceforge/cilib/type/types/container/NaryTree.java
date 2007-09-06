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

import java.util.Iterator;
import java.util.List;

public class NaryTree<E extends Comparable<E>> extends AbstractTree<E> {
	private static final long serialVersionUID = -1136444941205621381L;
	
	private int degree;
	private List<Tree<E>> nodes;

	public NaryTree() {
		
	}
	
	public NaryTree(NaryTree<E> copy) {
		
	}
	
	public NaryTree<E> clone() {
		return new NaryTree<E>(this);
	}
	
	public boolean addSubTree(Tree<E> subTree) {
		if (subTree == null)
			throw new IllegalArgumentException("Cannot add a null object as a child of a tree");
	
		if (getKey() == null)
			throw new IllegalStateException("Cannot add a subtree to a tree with a null for the key value");
		
		if (this.nodes.size() < degree) {
			this.nodes.add(subTree);
			return true;
		}
		
		return false;
	}

	public Tree<E> getSubTree(E element) {
		// TODO Auto-generated method stub
		return null;
	}

	public Tree<E> removeSubTree(E element) {
		throw new UnsupportedOperationException("Implementation is needed");
	}
	
	@Override
	public Tree<E> removeSubTree(int index) {
		throw new UnsupportedOperationException("Implementation is needed");
	}

	public int edges() {
		// TODO Auto-generated method stub
		return 0;
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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tree<E> getSubTree(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isLeaf() {
		return this.nodes.size() == 0;
	}
	
}
