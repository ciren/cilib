/*
 * SplitCoOperativeOptimisationAlgorithm.java
 *
 * Created on 03 August 2004, 09:27
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

package net.sourceforge.cilib.Algorithm;

/** 
 * This class implements a generalised co-operative optimisation algorithm.
 * <p />
 * Any algorithm that wishes to participate in a co-operative optimisation algorithm must implement the
 * {@link ParticipatingAlgorithm} interface. This class also implements ParticipatingAlgorithm meaning that
 * co-operative algorithms can be composed of co-operative algorithms again. 
 * <p />
 * The original co-operative PSO on which this class is based 
 *  is due to F. van den Bergh, "An Analysis of Particle Swarm Optimizers",
 *          PhD thesis, Department of Computer Science, 
 *          University of Pretoria, South Africa, 2002.
 *
 * @author  espeer
 */
public class SplitCoOperativeOptimisationAlgorithm extends CoOperativeOptimisationAlgorithm{
    
    /** Creates a new instance of SplitCoOperativeOptimisationAlgorithm */
    public SplitCoOperativeOptimisationAlgorithm() {
        updateContributionFitness = true;
    }
    
    /**
     * Determines whether this co-operative algorithm will update the fitness of participants.
     * Participants that execute later during an iteration may improve the fitness of the solution context.
     * This determines if the contribution's fitness is updated when this happens.
     * 
     * @param updateContributionFitness if true then fitness is updated, otherwise not. 
     */
    public void setUpdateContributionFitness(boolean updateContributionFitness) {
        this.updateContributionFitness = updateContributionFitness;
    }
    
    protected void performIteration() {
        ParticipatingAlgorithm participant = null;
        for (int i = 0; i < participants; ++i) {
            participant = (ParticipatingAlgorithm) optimisers[i];

            CoOperativeOptimisationProblemAdapter adapter = 
                (CoOperativeOptimisationProblemAdapter)
                    ((OptimisationAlgorithm) optimisers[i]).getOptimisationProblem();
            
            adapter.updateContext(context);

            optimisers[i].performIteration();

            for (int j = 0; j < adapter.getDimension(); ++j) {
                context[adapter.getOffset() + j] = participant.getContribution()[j];
            }
        }

        if (updateContributionFitness) {
            fitness = participant.getContributionFitness();
            for (int i = 0; i < participants - 1; ++i) {
                ((ParticipatingAlgorithm) optimisers[i]).updateContributionFitness(fitness);
            }
        }
    }

    private boolean updateContributionFitness;    
}
