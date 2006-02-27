/*
 * NaryTree.java
 *
 * Created on January 17, 2003, 4:54 PM
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

import net.sourceforge.cilib.Container.Visitor;

public class NaryTree implements Tree {
	private Object nodeValue;
	private ArrayList subtrees;
	private int degree;
	
	public NaryTree(int degree) {
		this.degree = degree;
		this.nodeValue = null;
		this.subtrees = new ArrayList();
	}
	
	public NaryTree(int degree, Object value) {
		this.degree = degree;
		this.nodeValue = value;
		this.subtrees = new ArrayList();	
	}
	
	public void add(NaryTree tree) {
		if (isEmpty()) {
			throw new RuntimeException("Invalid - cannot add a child to a tree with no value for the node");
		}
		
		if (subtrees.size() < degree) {
			subtrees.add(tree);
		}
		else throw new RuntimeException("Cannot add subtree, the NaryTree of size: " + degree + "is full");
	}
	
	public void add(int index, NaryTree tree) {
		if (isEmpty()) {
			throw new RuntimeException("Invalid - cannot add a child to a tree with no value for the node");
		}
		
		if (subtrees.size() > degree) {
			throw new RuntimeException("Cannot add subtree, the NaryTree of size: " + degree + "is full");
		}
		
		if (!getSubtree(index).isEmpty()) {
			throw new RuntimeException("Cannot add a subtree to replace a valid subtree, delete the subtree first");
		}
		
		subtrees.add(index, tree);	
	}
	
	public Object remove(int index) {
		if (index < subtrees.size())
			return subtrees.remove(index);
		else return null;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Container.Tree.Tree#setNodeValue(java.lang.Object)
	 */
	public void setNodeValue(Object value) {
		this.nodeValue = value;		
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Container.Tree.Tree#getNodeValue()
	 */
	public Object getNodeValue() {
		return nodeValue;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Container.Tree.Tree#getSubtree(int)
	 */
	public Tree getSubtree(int subtree) {
		if (isEmpty())
			throw new RuntimeException("Invalid operation - tree must have a value to have subtrees");
		
		if (subtree < degree) {
			return (Tree) subtrees.get(subtree);
		}
		else return null;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Container.Tree.Tree#isLeafNode()
	 */
	public boolean isLeafNode() {
		if (subtrees.size() == 0)
			return true;
		else return false;
	}
	
	/**
	 * 
	 */
	public boolean isEmpty() {
		return (nodeValue == null) ? true : false;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Container.Tree.Tree#accept(net.sourceforge.cilib.Container.Visitor)
	 */
	public void accept(Visitor v) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Container.Tree.Tree#getDegree()
	 */
	public int getDegree() {
		return subtrees.size();
	}
}
