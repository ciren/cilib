/*
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.container.visitor;

/**
 * @author Gary Pampara
 * @param <E> The type object.
 */
public abstract class Visitor<E> {
	
	/**
	 * Create the {@linkplain Visitor} instance.
	 */
	protected Visitor() {
	}
	
	/**
	 * Visit the provided object.
	 * @param o The object to visit.
	 */
	public abstract void visit(E o);
	
	/**
	 * Determine if the visitor has completed its visit operation.
	 * @return <code>true</code> if the visit operation is complete,
	 *         <code>false</code> otherwise.
	 */
	public boolean isDone() {
		return false;
	}
}
