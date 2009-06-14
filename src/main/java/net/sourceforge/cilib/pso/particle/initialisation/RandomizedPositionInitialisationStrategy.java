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
package net.sourceforge.cilib.pso.particle.initialisation;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.problem.OptimisationProblem;


/**
 * @deprecated This class is a temporary measure until we can get the DomainParser
 *             working as intended. the idea is to be able to specify particle
 *             contents via the string and have the randomness applied on initialisation.
 *             A specific example is to use this new string when initialising positions
 *             with Polar coordinates for example
 * @author gpampara
 *
 */
public class RandomizedPositionInitialisationStrategy implements
        PositionInitialisationStrategy {
    private static final long serialVersionUID = -47429588645761362L;

    private Random random;

    public RandomizedPositionInitialisationStrategy() {
        this.random = new MersenneTwister();
    }

    public RandomizedPositionInitialisationStrategy getClone() {
        return new RandomizedPositionInitialisationStrategy();
    }

    public void initialise(Particle particle, OptimisationProblem problem) {
        particle.setCandidateSolution(problem.getDomain().getBuiltRepresenation().getClone());
        particle.getPosition().randomize(random);

        particle.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getPosition().getClone());
    }

}
