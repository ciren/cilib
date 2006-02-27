/*
 * RandomInitialiser.java
 *
 * Created on September 14, 2003, 4:11 PM
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

package net.sourceforge.cilib.PSO;

import java.util.Random;

import net.sourceforge.cilib.Problem.Domain;
import net.sourceforge.cilib.Problem.OptimisationProblem;
import net.sourceforge.cilib.Random.MersenneTwister;

/**
 *
 * @author  espeer
 */
public class RandomInitialiser implements Initialiser {
    
    public RandomInitialiser() {
        generator = new MersenneTwister();
    }
        
    public double[] getInitialPosition(OptimisationProblem problem) {
        double[] position = new double[problem.getDimension()];
        for (int i = 0; i < problem.getDimension(); ++i) {
            Domain domain = problem.getDomain(i);
            position[i] = generator.nextDouble();
            position[i] *= (domain.getUpperBound() - domain.getLowerBound());
            position[i] += domain.getLowerBound();
        }
        return position;
    }

    public double[] getInitialVelocity(OptimisationProblem problem) {
        double[] velocity = new double[problem.getDimension()];
        for (int i = 0; i < problem.getDimension(); ++i) {
            velocity[i] = 0;
        }
        return velocity;
    }

    public void setRandomGenerator(Random generator) {
        this.generator = generator;
    }

	public Random getRandomGenerator() {
		return generator;
	}
    
    private Random generator;
}
