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

import java.util.Iterator;

import net.sourceforge.cilib.container.visitor.Visitor;
import net.sourceforge.cilib.type.types.AbstractType;

public class BinaryTree<E extends Comparable<E>> extends AbstractType implements Tree<E> {
	private static final long serialVersionUID = 3537717751647961525L;

	public BinaryTree() {
		
	}
	
	public BinaryTree(BinaryTree copy) {
		
	}
	
	public BinaryTree clone() {
		return new BinaryTree(this);
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
	
	public boolean remove(Tree<E> subtree) {
		return this.remove(subtree.getKey());
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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
