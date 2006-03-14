/*
 * BinaryTree.java
 *
 * Created on January 17, 2003, 4:54 PM
 *
 * 
 * Copyright (C) 2003 - 2006 
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
 * 
 */

package net.sourceforge.cilib.container.tree;

import net.sourceforge.cilib.container.visitor.PrePostVisitor;
import net.sourceforge.cilib.container.visitor.Visitor;

/**
 * This class provides a very simple BinaryTree implementation.
 * 
 * @author Gary Pampara
 */
public class BinaryTree<E> implements Tree<E> {
	
	private BinaryTree<E> left;
	private BinaryTree<E> right;
	private E nodeValue;
	private int degree = 0;
	private static final int LEFT = 0;
	private static final int RIGHT = 1; 
	
	
	/**
	 * Create an empty <code>BinaryTree</code>
	 */
	public BinaryTree() {
		nodeValue = null;
		left = null;
		right = null;
	}
	
	/**
	 * Create a <code>BinaryTree</code> with a value of nodeValue
	 * @param nodeValue The object to be contained by the <code>BinaryTree</code>
	 */
	public BinaryTree(E nodeValue) {
		this.nodeValue = nodeValue;
		left = null;
		right = null;
	}
	
	
	/**
	 * 
	 * @param copy
	 */
	public BinaryTree(BinaryTree<E> copy) {
		this.nodeValue = copy.nodeValue;
		this.left = copy.left.clone();
		this.right = copy.right.clone();
	}
	
	
	/**
	 * 
	 */
	public BinaryTree<E> clone() {
		return new BinaryTree<E>(this);
	}
	
	
	/**
	 * Add a given <code>BinaryTree</code> to the current <code>BinaryTree</code> as a subtree, and add the
	 * subtree in the given position
	 * @param b The <code>BinaryTree</code> to add to the current node
	 * @param position The index where to add the subtree 
	 *                 - this can only be <code>BinaryTree.LEFT</code> or <code>BinaryTree.RIGHT</code>
	 */
	public void add(BinaryTree<E> b, int position) {
		if (getSubtree(position) == null) {
			if (position == LEFT) {
				left = b;
			}
			else { // This is the Right part of the BinaryTree
				right = b;
			}
			
			degree++;
		}
		else throw new RuntimeException("Error cannot insert subtree into BinaryTree, the position is occupied");
	}

	
	/**
	 * Set the value of the <code>Object</code> contained within this <code>BinaryTree</code>
	 * @param o The Object to be contained by this <code>BinaryTree</code>
	 * @see net.sourceforge.cilib.container.tree.Tree#setNodeValue(java.lang.Object)
	 */
	public void setNodeValue(E o) {
		this.nodeValue = o;		
	}

	/**
	 * Get the object contained in the <code>BinaryTree</code>
	 * @return The <code>Object</code> contained within this <code>BinaryTree</code>
	 * @see net.sourceforge.cilib.container.tree.Tree#getNodeValue()
	 */
	public E getNodeValue() {
		return nodeValue;
	}

	
	/**
	 * Get a referance to the subtree located in the position given 
	 * @param subtree The position of the subtree to get a reference to
	 * @see net.sourceforge.cilib.container.tree.Tree#getSubtree(int)
	 * @return The subtree located in the given position, otherwise a <code>null</code> is returned
	 */
	public Tree<E> getSubtree(int subtree) {
		if (subtree == LEFT)
			return getLeftSubtree();
		else if (subtree == RIGHT)
			return getRightSubtree();
		else return null;
	}
	
	
	/**
	 * Add a <code>BinaryTree</code> to the current <code>BinaryTree</code> as a subtree and place it in
	 * the "left" subtree position
	 * @param b The <code>BinaryTree</code> to add to the current <code>BinaryTree</code> node in the "left" position
	 */
	public void addLeftSubtree(BinaryTree<E> b) {
		add(b, LEFT);
	}
	
	
	/**
	 * Add a <code>BinaryTree</code> to the current <code>BinaryTree</code> as a subtree and place it in
	 * the "right" subtree position
	 * @param b The <code>BinaryTree</code> to add to the current <code>BinaryTree</code> node in the "right" position
	 */
	public void addRightSubtree(BinaryTree<E> b) {
		add(b, RIGHT);
	}
	
	
	/**
	 * Return the left <code>BinaryTree</code> subtree of the current <code>BinaryTree</code>
	 * @return The <code>BinaryTree</code> located in the left subtree
	 */
	public Tree<E> getLeftSubtree() {
		return left;
	}
	
	
    /**
     * Return the right <code>BinaryTree</code> subtree of the current <code>BinaryTree</code> 
     * @return The <code>BinaryTree</code> located in the right subtree
     */
	public Tree<E> getRightSubtree() {
		return right;
	}
	
	
	/**
	 * Remove the subtree currently located in the <code>BinaryTree</code> at the given index / position
	 * @param position The location of the subtree in regards to the current <code>BinaryTree</code>
	 * @return The <code>BinaryTree</code> located at the given index / position or <code>null</code> if it is not found
	 */
	public Tree<E> removeSubtree(int position) {
		if (this.getSubtree(position) == null)
			throw new RuntimeException("NullPointer Operation Error - Cannot remove a subtree if it does not exist");
		
		if (position == LEFT) {
			BinaryTree<E> b = left;
			degree--;
			left = null;
			return b;
		}
		else if (position == RIGHT) {
			BinaryTree<E> b = right;
			degree--;
			right = null;
			return b;
		}
		else throw new RuntimeException("Cannot index a BinaryTree with the index: " + position);
	}
	
	
	/**
	 * Remove the left subtree of the current <code>BinaryTree</code>
	 * @return The <code>BinaryTree</code> that was located in the left branch of the <code>BinaryTree</code>
	 */
	public Tree<E> removeLeftSubtree() {
		return removeSubtree(LEFT);
	}
	
	
	/**
	 * Remove the right subtree of the current <code>BinaryTree</code>
	 * @return The <code>BinaryTree</code> that was located in the right branch of the <code>BinaryTree</code>
	 */
	public Tree<E> removeRightSubtree() {
		return removeSubtree(RIGHT);
	}

	
	/**
	 * Determine if this <code>BinaryTree</code> node is a leaf node. This only happens if the node has
	 * no subtrees and the subtree links have <code>null</code> values.
	 * @see net.sourceforge.cilib.container.tree.Tree#isLeafNode()
	 * @return True if the node is a leaf node, otherwise returns false
	 */
	public boolean isLeafNode() {
		if (left == null & right == null)
			return true;
		else return false;
	}

	
	/**
	 * This method determines if the <code>Object</code> contained by this <code>BinaryTree</code> is null or
	 * not. If it is null the Tree is empty with respect to node value. 
	 * @see net.sourceforge.cilib.container.tree.Tree#isEmpty()
	 * @return true - if the Tree node does not contain an object
	 * @return false - if the Tree node does contain an object
	 */
	public boolean isEmpty() {
		return (nodeValue == null) ? true : false;
	}

	
	/**
	 * Accept a visitor into the Binary and base on the needs of the Visitor, the BinaryTree will be
	 * traversed.
	 * @param v The Visitor to be used inside the <code>BinaryTree</code> 
	 * @see net.sourceforge.cilib.container.tree.Tree#accept(net.sourceforge.cilib.container.Visitor)
	 */
	public void accept(Visitor<E> v) {
		//PrePostVisitorDecorator visitor = new PrePostVisitorDecorator(v);
		depthFirstTraversal((PrePostVisitor<E>) v);
		
	}
	
	
	/**
	 * 
	 * @param visitor
	 */
	private void depthFirstTraversal(PrePostVisitor<E> visitor) {
		if (visitor.isDone()) {
			return;
		}
		
		if (!isEmpty()) {
			visitor.preVisit(getNodeValue());
			left.depthFirstTraversal(visitor);
			visitor.visit(getNodeValue());
			right.depthFirstTraversal(visitor);
			visitor.postVisit(getNodeValue());
		}
	}

	
	/**
	 * Get the current capacity of the <code>BinaryTree</code>. The degree is the amount of subtrees
	 * that have been filled out of the 2 possible.
	 * @see net.sourceforge.cilib.container.tree.Tree#getDegree()
	 * @return The number of subtree elements that have been filled in the <code>BinaryTree</code>  
	 */
	public int getDegree() {
		return degree;
	}
}