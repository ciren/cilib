/*
 * GeneralTree.java
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

public class GeneralTree<E extends Comparable<E>> extends AbstractTree<E> {
	private static final long serialVersionUID = 3453326928796685749L;
	
	private List<Tree<E>> subTrees;
	
	public GeneralTree() {
		key = null;
		subTrees = new ArrayList<Tree<E>>();
	}
	
	public GeneralTree(E element) {
		this();
		this.key = element;
	}
	
	public GeneralTree<E> clone() {
		return null;
	}

	public boolean addSubTree(Tree<E> subtree) {
		if (subtree == null)
			throw new IllegalArgumentException("Cannot add a null object as a child of a tree");
	
		if (getKey() == null)
			throw new IllegalStateException("Cannot add a subtree to a tree with a null for the key value");
		
		this.subTrees.add(subtree);
		return true;
	}

	/**
	 * Create an empty tree object and add it to the current tree node.
	 */
	public boolean add(E element) {
		Tree<E> subTree = new GeneralTree<E>(element);
		return addSubTree(subTree);
	}

	public void clear() {
		this.subTrees.clear();
	}

	public boolean contains(E element) {
		for (Tree<E> e : this.subTrees) {
			if (e.getKey().equals(element))
				return true;
		}

		return false;
	}

	public boolean remove(E element) {
		for (Tree<E> tree : this.subTrees) {
			if (tree.getKey().equals(this.key)) {
				this.subTrees.remove(tree);
				return true;
			}
		}
		
		return false;
	}
	
	public E remove(int index) {
		if (index >= this.subTrees.size())
			throw new IndexOutOfBoundsException("");
		
		return this.subTrees.remove(index).getKey();
	}

	public Tree<E> removeSubTree(E element) {
		throw new UnsupportedOperationException("Implement me");
	}
	
	public Tree<E> removeSubTree(int index) {
		throw new UnsupportedOperationException("Implementation needed");
	}

	public Tree<E> getSubTree(E element) {
		for (Tree<E> tree : this.subTrees) {
			if (tree.getKey().equals(element))
				return tree;
		}

		return null;
	}

	public String toString() {
		throw new UnsupportedOperationException("Implementation needed");
	}

	public Tree<E> getSubTree(int index) {
		if (isEmpty()) 
			throw new UnsupportedOperationException();
		
		return this.subTrees.get(index);
	}

	public boolean isLeaf() {
		return this.subTrees.size() == 0;
	}

	public int getDegree() {
		return this.subTrees.size();
	}
	
}