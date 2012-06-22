/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.clustering;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.AbstractCooperativeIterationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.clustering.iterationstrategies.CooperativeDataClusteringPSOIterationStrategy;
import net.sourceforge.cilib.clustering.iterationstrategies.SinglePopulationDataClusteringIterationStrategy;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Kristina
 */
public class CooperativePSO extends MultiPopulationBasedAlgorithm{
    private IterationStrategy<CooperativePSO> iterationStrategy;
    private int interval;
    OptimisationSolution bestSolution;
    
    public CooperativePSO() {
        iterationStrategy = new CooperativeDataClusteringPSOIterationStrategy();
        interval = 0;
        bestSolution = new OptimisationSolution(Vector.of(Double.POSITIVE_INFINITY), new MinimisationFitness(Double.POSITIVE_INFINITY));
       
    }
    
    public CooperativePSO(CooperativePSO copy) {
        iterationStrategy = copy.iterationStrategy;
        interval = copy.interval;
        bestSolution = copy.bestSolution;
    }
    
    @Override
    public CooperativePSO getClone() {
        return new CooperativePSO(this);
    }

    @Override
    protected void algorithmIteration() {
        iterationStrategy.performIteration(this);
        ClusterParticle particle = ((AbstractCooperativeIterationStrategy) iterationStrategy).getContextParticle();
        bestSolution = new OptimisationSolution(particle.getPosition(), particle.getFitness());
        
        if(((SinglePopulationDataClusteringIterationStrategy) ((DataClusteringPSO) subPopulationsAlgorithms.get(0)).getIterationStrategy()).getWindow().hasSlid()) {
            System.out.println("\n" + getBestSolution().getPosition().toString());
        }
        
        if(getIterations() == ((MeasuredStoppingCondition) getStoppingConditions().get(0)).getTarget() - 1)
            System.out.println("\n" + getBestSolution().getPosition().toString());
    }

    @Override
    public OptimisationSolution getBestSolution() {
        return bestSolution;
    }

    @Override
    public Iterable<OptimisationSolution> getSolutions() {
        List<OptimisationSolution> solutions = new ArrayList<OptimisationSolution>();
        for (PopulationBasedAlgorithm currentAlgorithm : this.getPopulations()) {
             for (OptimisationSolution solution : currentAlgorithm.getSolutions())
                 solutions.add(solution);
        }
        return solutions;
    }
    
    @Override
    public void performInitialisation()    {
        OptimisationProblem problem = getOptimisationProblem().getClone();//getCoevolutionOptimisationProblem();
       
        for (PopulationBasedAlgorithm currentAlgorithm : subPopulationsAlgorithms) {
            //((DataClusteringPSO) currentAlgorithm).setDimensions(interval);
            currentAlgorithm.setOptimisationProblem(problem);
            currentAlgorithm.performInitialisation();
            
        }
        
    }
    
    public void setIterationStrategy(IterationStrategy strategy) {
        iterationStrategy = strategy;
    }
    
}
