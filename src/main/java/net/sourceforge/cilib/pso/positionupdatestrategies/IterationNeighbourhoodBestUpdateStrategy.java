/*
 * IterationNeighbourhoodBestUpdateStrategy.java
 *
 * Created on Oct 14, 2005
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
package net.sourceforge.cilib.pso.positionupdatestrategies;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * @author Gary Pampara
 */
public class IterationNeighbourhoodBestUpdateStrategy implements NeighbourhoodBestUpdateStrategy {
	private static final long serialVersionUID = 9029103734770326975L;

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.PSO.NeighbourhoodBestUpdateStrategy#getSocialBestFitness(net.sourceforge.cilib.PSO.Particle)
	 */
	public Fitness getSocialBestFitness(Particle particle) {
		return particle.getFitness();
	}

}
