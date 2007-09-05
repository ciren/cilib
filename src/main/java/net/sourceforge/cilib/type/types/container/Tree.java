/*
 * Tree.java
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
import net.sourceforge.cilib.container.visitor.Visitor;

public interface Tree<E extends Comparable<E>> extends Graph<E> {
	
	public E getKey();
	
	public boolean addSubTree(Tree<E> subTree);
	
	public Tree<E> getSubTree(E element);
	
	public Tree<E> getSubTree(int index);
	
	public Tree<E> removeSubTree(E element);
	
	public Tree<E> removeSubTree(int index);
	
	public boolean isLeaf();
	
	public void depthFirstTraversal(PrePostVisitor<E> visitor);
	
	public void breadthFirstTraversal(Visitor<E> visitor);
	
}
