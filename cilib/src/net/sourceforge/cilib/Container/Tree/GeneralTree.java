/*
 * GeneralTree.java
 *
 * Created on February 17, 2003, 4:54 PM
 *
 * 
 * Copyright (C) 2003, 2004 - CIRG@UP 
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

package net.sourceforge.cilib.Container.Tree;

import java.util.ArrayList;

import net.sourceforge.cilib.Container.PreOrderVisitorDecorator;
import net.sourceforge.cilib.Container.PrePostVisitor;
import net.sourceforge.cilib.Container.Visitor;

// FIXME: The depthFirstTraversal() method could be placed into Tree itself, but it will have to become
// FIXME: an abstract class.
public class GeneralTree implements Tree {
	private Object value;
	private ArrayList<Tree> childNodes;
	
	public GeneralTree() {
		value = null;
		childNodes = new ArrayList<Tree>();
	}
	
	public GeneralTree(Object object) {
		value = object;
		childNodes = new ArrayList<Tree>();
	}
	
	/**
	 * Add a subtree to the current tree. The tree will be placed in the next available slot, ie: childNodes.size()
	 * @param generalTree The GeneralTree to attach to the current tree
	 */
	public void add(GeneralTree generalTree) {
		if (generalTree != null) {
			childNodes.add(generalTree);
		}
		else throw new RuntimeException("Cannot add a null!!!");
	}
	
	public void add(int posi,GeneralTree generalTree) {
		if (generalTree != null) {
			childNodes.add(posi,generalTree);
		}
		else throw new RuntimeException("Cannot add a null!!!");
	}
	
	
	/**
	 * Remove a subtree from the current Tree. Please note that this method could be quite a devastating
	 * operation as all sub trees associated with the subtree at index <code>index</code> will be lost.
	 * @param index The index of the subtree to remove.
	 * @return The removed object is returned to the callee
	 */
	public Object remove(int index) {
		if (index < childNodes.size()) {
			return childNodes.remove(index);
		}
		else return null;
	}
	
	/**
	 * Set the value of the current node
	 * @param value The value for the node to contain
	 */
	public void setNodeValue(Object value) {
		this.value = value;
	}

	/**
	 * Returns the value the node is currently containing
	 * @see net.sourceforge.cilib.Container.Tree.Tree#getNodeValue()
	 * @return The Object value currently contained by the Tree
	 */
	public Object getNodeValue() {
		return value;
	}

	/**
	 * Get the referance to the i-th child node to this parent node in the Tree
	 * @see net.sourceforge.cilib.Container.Tree.Tree#getSubtree(int)
	 * @param subtree The index of the subtree to return
	 * @return The referance to the required child tree node 
	 */
	public Tree getSubtree(int subtree) {
		if (subtree >= childNodes.size()) {
			throw new RuntimeException("Node index request beyound bounds - Please correct");
		}
		
		return (Tree) childNodes.get(subtree);
	}

	/**
	 * Determine if this node is a leaf node. This will only happen if the node does not have any children
	 * @see net.sourceforge.cilib.Container.Tree.Tree#isLeafNode()
	 * @return true - if the node is in fact a leaf node
	 * @return false - if the node is not a leaf node
	 */
	public boolean isLeafNode() {
		if (childNodes.size() == 0) 
			return true;
		else return false;
	}
	
	/**
	 * Test if the value being held by the Tree is null 
	 * @return True if the value has a null reference
	 * @return False if the value does not have a null reference
	 */
	public boolean isEmpty() {
		return (value == null) ? true : false;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Container.Tree.Tree#accept(net.sourceforge.cilib.Container.Visitor)
	 */
	public void accept(Visitor v) {
		PreOrderVisitorDecorator preOrder = new PreOrderVisitorDecorator(v);
		depthFirstTraversal(preOrder);
	}
	
	/**
	 * 
	 * @param visitor
	 */
	private void depthFirstTraversal(PrePostVisitor visitor) {
		if (visitor.isDone()) {
			return;
		}
		
		if (!isEmpty()) {
			visitor.preVisit(getNodeValue());
			for (int i = 0; i < childNodes.size(); ++i) {
				GeneralTree t = (GeneralTree) getSubtree(i);
				t.depthFirstTraversal(visitor);
			}
			
			visitor.postVisit(getNodeValue());
		}	
	}

	/**
	 * Get the degree of the current node
	 * @see net.sourceforge.cilib.Container.Tree.Tree#getDegree()
	 * @return The degree of the node where the degree is the number of enimating edges
	 */
	public int getDegree() {
		return childNodes.size();
	}
}
