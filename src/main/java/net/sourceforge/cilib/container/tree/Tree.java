/*
 * Tree.java
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

import net.sourceforge.cilib.container.visitor.Visitor;

/**
 * 
 * @author Gary Pampara
 */
public interface Tree<E> {
	
	/**
	 * 
	 * @return
	 */
	public Tree<E> clone();
	
	/**
	 * 
	 * @param o
	 */
	public void setNodeValue(E o);
	
	
	/**
	 * 
	 * @return
	 */
	public E getNodeValue();
	
	
	/**
	 * 
	 * @param subtree
	 * @return
	 */
	public Tree<E> getSubtree(int subtree);
	
	
	/**
	 * 
	 * @return
	 */
	public boolean isLeafNode();
	
	
	/**
	 * 
	 * @return
	 */
	public boolean isEmpty();
	
	
	/**
	 * 
	 * @param v
	 */
	public void accept(Visitor<E> v);
	
	
	/**
	 * 
	 * @return
	 */
	public int getDegree();
}
