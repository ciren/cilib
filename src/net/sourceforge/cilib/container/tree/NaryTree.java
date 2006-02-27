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

package net.sourceforge.cilib.container.tree;

import java.util.ArrayList;
import java.util.Iterator;

import net.sourceforge.cilib.container.visitor.Visitor;

/**
 * 
 * @author Gary Pampara
 */
public class NaryTree<E> implements Tree<E> {
	private E nodeValue;
	private ArrayList<Tree<E>> subtrees;
	private int degree;
	
	
	/**
	 * 
	 * @param degree
	 */
	public NaryTree(int degree) {
		this.degree = degree;
		this.nodeValue = null;
		this.subtrees = new ArrayList<Tree<E>>();
	}
	
	
	/**
	 * 
	 * @param degree
	 * @param value
	 */
	public NaryTree(int degree, E value) {
		this.degree = degree;
		this.nodeValue = value;
		this.subtrees = new ArrayList<Tree<E>>();	
	}
	
	
	/**
	 * 
	 * @param copy
	 */
	public NaryTree(NaryTree<E> copy) {
		this(copy.degree, copy.nodeValue);
		
		for (Iterator<Tree<E>> i = copy.subtrees.iterator(); i.hasNext(); ) {
			this.subtrees.add(i.next().clone());
		}
	}
	
	
	public NaryTree<E> clone() {
		return new NaryTree<E>(this);
	}
	
	
	/**
	 * 
	 * @param tree
	 */
	public void add(NaryTree<E> tree) {
		if (isEmpty()) {
			throw new RuntimeException("Invalid - cannot add a child to a tree with no value for the node");
		}
		
		if (subtrees.size() < degree) {
			subtrees.add(tree);
		}
		else throw new RuntimeException("Cannot add subtree, the NaryTree of size: " + degree + "is full");
	}
	
	
	/**
	 * 
	 * @param index
	 * @param tree
	 */
	public void add(int index, NaryTree<E> tree) {
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
	
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public Tree<E> remove(int index) {
		if (index < subtrees.size())
			return subtrees.remove(index);
		else return null;
	}

	
	/** 
	 * @see net.sourceforge.cilib.container.tree.Tree#setNodeValue(java.lang.Object)
	 */
	public void setNodeValue(E value) {
		this.nodeValue = value;		
	}

	
	/**
	 * @see net.sourceforge.cilib.container.tree.Tree#getNodeValue()
	 */
	public E getNodeValue() {
		return nodeValue;
	}

	
	/**
	 * @see net.sourceforge.cilib.container.tree.Tree#getSubtree(int)
	 */
	public Tree<E> getSubtree(int subtree) {
		if (isEmpty())
			throw new RuntimeException("Invalid operation - tree must have a value to have subtrees");
		
		if (subtree < degree) {
			return (Tree<E>) subtrees.get(subtree);
		}
		else return null;
	}

	
	/**
	 * @see net.sourceforge.cilib.container.tree.Tree#isLeafNode()
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

	
	/**
	 * @see net.sourceforge.cilib.container.tree.Tree#accept(net.sourceforge.cilib.container.Visitor)
	 */
	public void accept(Visitor v) {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * @see net.sourceforge.cilib.container.tree.Tree#getDegree()
	 */
	public int getDegree() {
		return subtrees.size();
	}
}
