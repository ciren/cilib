/*
 * RandomizedPostionInitialisationStrategy.java
 *
 * Created on September 22, 2003, 1:29 PM
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
 */
package net.sourceforge.cilib.pso.particle.initialisation;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.OptimisationProblem;


/**
 * 
 * @deprecated This class is a temporary measure until we can get the DomainParser
 *             working as intended. the idea is to be able to specify particle
 *             contents via the string and have the randomness applied on initialisation.
 *             A specific example is to use this new string when initialising positions
 *             with Polar coordinates for example
 * @author gpampara
 *
 */
public class RandomizedPostionInitialisationStrategy implements
		PositionInitialisationStrategy {

	public void initialise(Particle particle, OptimisationProblem problem) {
		particle.getProperties().put("position", problem.getDomain().getBuiltRepresenation().clone());
		particle.getPosition().randomise();
		
		particle.getProperties().put("bestPosition", particle.getPosition().clone());
	}

}
