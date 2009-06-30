/**
 * Copyright (C) 2003 - 2009
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
package net.sourceforge.cilib.cooperative;

import java.util.List;

import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;

/**
 *
 *
 */
public class HybridCooperativeAlgorithm extends MultiPopulationBasedAlgorithm implements ParticipatingAlgorithm {
    private static final long serialVersionUID = 4908040536174924734L;

    /**
     * Create a new instance of {@literal HybridCooperativeAlgorithm}.
     */
    public HybridCooperativeAlgorithm() {
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public HybridCooperativeAlgorithm(HybridCooperativeAlgorithm copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    public HybridCooperativeAlgorithm getClone() {
        return new HybridCooperativeAlgorithm(this);
    }

    /**
     * {@inheritDoc}
     */
    public OptimisationSolution getBestSolution() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public OptimisationProblem getOptimisationProblem() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public List<OptimisationSolution> getSolutions() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void setOptimisationProblem(OptimisationProblem problem) {
    }

    /**
     * {@inheritDoc}
     */
    public Entity getContribution() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Fitness getContributionFitness() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void updateContributionFitness(Fitness fitness) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void algorithmIteration() {

    }

}
