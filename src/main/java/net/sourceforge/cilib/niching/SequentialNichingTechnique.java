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
package net.sourceforge.cilib.niching;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.ec.EC;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.visitor.TopologyVisitor;
import net.sourceforge.cilib.problem.DeratingOptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.type.types.container.Vector;

public class SequentialNichingTechnique extends AbstractAlgorithm implements PopulationBasedAlgorithm {
    private PopulationBasedAlgorithm algorithm;
    private ControlParameter threshold;
    protected List<OptimisationSolution> solutions;
    
    public SequentialNichingTechnique() {
        this.algorithm = new EC();
        this.threshold = ConstantControlParameter.of(0);
        this.solutions = Lists.<OptimisationSolution>newLinkedList();
    }
    
    public SequentialNichingTechnique(SequentialNichingTechnique copy) {
        this.algorithm = copy.algorithm.getClone();
        this.threshold = copy.threshold.getClone();
        this.solutions = Lists.<OptimisationSolution>newLinkedList(copy.solutions);
    }
    
    @Override
    public SequentialNichingTechnique getClone() {
        return new SequentialNichingTechnique(this);
    }
    
    @Override
    public void algorithmInitialisation() {
        //algorithm.setOptimisationProblem(optimisationProblem);
    }

    @Override
    protected void algorithmIteration() {
        AbstractAlgorithm alg = (AbstractAlgorithm) algorithm.getClone();
        alg.setOptimisationProblem(optimisationProblem);
        alg.performInitialisation();

        while (!alg.isFinished()) {
            alg.performIteration();
        }
        
        OptimisationSolution best = alg.getBestSolution();
        ((DeratingOptimisationProblem) optimisationProblem).addSolution((Vector) best.getPosition());
        
        if (best.getFitness().getValue() > threshold.getParameter()) {
            solutions.add(best);
        }
    }

    @Override
    public Topology<? extends Entity> getTopology() {
        return algorithm.getTopology();
    }
    
    @Override
    public OptimisationSolution getBestSolution() {
        return Collections.max(solutions);
    }

    @Override
    public Iterable<OptimisationSolution> getSolutions() {
        return solutions;
    }

    @Override
    public Object accept(TopologyVisitor visitor) {
        visitor.visit(algorithm.getTopology());
        return visitor.getResult();
    }

    @Override
    public void setInitialisationStrategy(PopulationInitialisationStrategy<? extends Entity> initialisationStrategy) {
        algorithm.setInitialisationStrategy(initialisationStrategy);
    }

    @Override
    public PopulationInitialisationStrategy getInitialisationStrategy() {
        return algorithm.getInitialisationStrategy();
    }

    @Override
    public void setOptimisationProblem(OptimisationProblem problem) {
        Preconditions.checkArgument(problem instanceof DeratingOptimisationProblem, 
                "SequentialNiching can only be used with DeratingOptimisationProblem.");
        optimisationProblem = problem;
    }

    public void setAlgorithm(PopulationBasedAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public PopulationBasedAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setThreshold(ControlParameter threshold) {
        this.threshold = threshold;
    }

    public ControlParameter getThreshold() {
        return threshold;
    }
}
