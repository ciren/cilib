/*
 * CandidateSolution.java
 *
 * Created on January 17, 2008, 4:54 PM
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
 */
package net.sourceforge.cilib.entity;

import java.io.Serializable;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Interface to describe the notion of a <code>CandidateSoution</code>. In general
 * a <code>CandidateSolution</code> contains a {@linkplain Type} to describe the
 * solution the <code>CandidateSolution</code> represents together with its
 * associated {@linkplain Fitness} value.
 */
public interface CandidateSolution extends Serializable, Cloneable {

	/**
	 * Get the contents of the <code>CandidateSoltion</code>. i.e.: The
	 * potential solution.
	 * @return A {@linkplain Type} representing the solution.
	 */
	public Type getContents();

	/**
	 * Set the solution that the <code>CandidateSolution</code> represents.
	 * @param contents The potential solution to set.
	 */
	public void setContents(Type contents);

	/**
	 * Obtain the {@linkplain Fitness} of the current <code>CandidateSolution</code>.
	 * @return The current {@linkplain Fitness} value.
	 */
	public Fitness getFitness();

}
