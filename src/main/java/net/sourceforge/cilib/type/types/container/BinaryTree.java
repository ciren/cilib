/*
 * BinaryTree.java
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


import net.sourceforge.cilib.container.visitor.PrePostVisitor;

public class BinaryTree<E extends Comparable<E>> extends AbstractTree<E> {
	private static final long serialVersionUID = 3537717751647961525L;
	
	private BinaryTree<E> left;
	private BinaryTree<E> right;

	public BinaryTree() {
		this(null, null, null);
	}
	
	public BinaryTree(E element) {
		this(element, new BinaryTree<E>(), new BinaryTree<E>());
	}
	
	public BinaryTree(E key, BinaryTree<E> left, BinaryTree<E> right) {
		this.key = key;
		this.left = left;
		this.right = right;
	}
	
	public BinaryTree(BinaryTree<E> copy) {
		this.key = copy.key;
		this.left = copy.left.clone();
		this.right = copy.right.clone();
	}
	
	public BinaryTree<E> clone() {
		return new BinaryTree<E>(this);
	}

	@Override
	public String toString() {
		throw new UnsupportedOperationException("Not implemented");
	}

	/**
	 * Add the provided subtree to the current Tree.
	 * Addition is by default first the left branch subtree. If the
	 * left branch already has a defined tree attached, the right
	 * subtree is assigned.
	 * 
	 * @param subTree
	 * @return <tt>true</tt> if the addition was successful, <tt>false</tt> otherwise  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean addSubTree(Tree<E> subTree) {
		if (isEmpty())
			throw new UnsupportedOperationException("Cannot add a subtree to an empty tree");
		
		if (left.isEmpty()) {
			left = (BinaryTree<E>) subTree;
			return true;
		}
		if (right.isEmpty()) {
			right = (BinaryTree<E>) subTree;
			return true;
		}
		
		return false;
	}

	@Override
	public Tree<E> getSubTree(E element) {
		if (isEmpty())
			throw new UnsupportedOperationException("Cannot get a subtree from an empty tree");
		
		if (!left.isEmpty() && left.getKey().equals(element)) return left;
		if (!right.isEmpty() && right.getKey().equals(element)) return right;
		
		return new BinaryTree<E>();
	}

	@Override
	public Tree<E> getSubTree(int index) {
		if (index < 0 || index >= 2)
			throw new IndexOutOfBoundsException("BinaryTree subTree indexes of 0 or 1 are ony allowed.");
		
		if (index == 0) return left;
		
		return right;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Tree<E> removeSubTree(E element) {
		AbstractTree<E> subTreeFound = (AbstractTree<E>) this.getSubTree(element);
		
		if (subTreeFound == left) left = new BinaryTree<E>();
		if (subTreeFound == right) right = new BinaryTree<E>();
		
		return subTreeFound;
	}
	
	// TODO: Is there a nicer way of doing this method?
	public Tree<E> removeSubTree(int index) {
		AbstractTree<E> found = (AbstractTree<E>) this.getSubTree(index);
		return this.removeSubTree(found.getKey());
	}

	/**
	 * Convenience method. Defers to {@see BinaryTree#addSubtree(Tree)}
	 */
	@Override
	public boolean add(E element) {
		return this.addSubTree(new BinaryTree<E>(element));
	}

	@Override
	public void clear() {
		this.key = null;
		this.left = null;
		this.right = null;
	}

	@Override
	public boolean contains(E element) {
		return this.getSubTree(element).isEmpty();
	}

	@Override
	public boolean remove(E element) {
		return !this.removeSubTree(element).isEmpty();
	}

	@Override
	public E remove(int index) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void depthFirstTraversal(PrePostVisitor<E> visitor) {
		if (!isEmpty()) {
			visitor.preVisit(getKey());
			left.depthFirstTraversal(visitor);
			visitor.visit(getKey());
			right.depthFirstTraversal(visitor);
			visitor.postVisit(getKey());
		}
	}

	@Override
	public boolean isLeaf() {
		return left.isEmpty() && right.isEmpty();
	}

	@Override
	public int getDegree() {
		return 2;
	}

}
