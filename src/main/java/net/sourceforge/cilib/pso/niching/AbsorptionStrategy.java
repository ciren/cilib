/*
 * AbsorptionStrategy.java
 *
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
package net.sourceforge.cilib.pso.niching;

import java.util.List;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;

/**
 * Strategy definition for the manner in which main swarm entities are absorbed
 * into subswarms.
 * @param <E> The type which is a subclass of {@linkplain PopulationBasedAlgorithm}.
 */
public interface AbsorptionStrategy<E extends PopulationBasedAlgorithm> {
	
	/**
	 * Absorb the {@linkplain Entity}s from the main swarm into the subswarm, provided
	 * the required criteria is met.
	 * @param mainSwarm The main swarm with all it's {@linkplain Entity} objects.
	 * @param subSwarms The list of the current subswarms.
	 */
	public void absorb(E mainSwarm, List<PopulationBasedAlgorithm> subSwarms);

}
