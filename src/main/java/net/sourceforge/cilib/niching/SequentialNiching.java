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

import com.google.common.collect.Lists;
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
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 *
 */
public class SequentialNiching extends AbstractAlgorithm implements PopulationBasedAlgorithm {
    private PopulationBasedAlgorithm algorithm;
    private List<OptimisationSolution> solutions;
    private ContinuousFunction deratingFunction;
    private DistanceMeasure distanceMeasure;
    private ControlParameter threshold;
    
    public SequentialNiching() {
        this.algorithm = new EC();
        this.distanceMeasure = new EuclideanDistanceMeasure();
        this.deratingFunction = new PowerDeratingFunction();
        this.solutions = Lists.<OptimisationSolution>newLinkedList();
        this.threshold = ConstantControlParameter.of(0);
    }
    
    public SequentialNiching(SequentialNiching copy) {
        this.algorithm = copy.algorithm.getClone();
        this.distanceMeasure = copy.distanceMeasure;
        this.deratingFunction = copy.deratingFunction;
        this.solutions = Lists.<OptimisationSolution>newLinkedList(solutions);
        this.threshold = copy.threshold.getClone();
    }
    
    @Override
    public SequentialNiching getClone() {
        return new SequentialNiching(this);
    }

    @Override
    public void performInitialisation() {
        algorithm.performInitialisation();
        algorithm.setOptimisationProblem(optimisationProblem);
    }

    @Override
    protected void algorithmIteration() {
        algorithm.performIteration();
        
        Entity best = algorithm.getTopology().getBestEntity();
        
        ((FunctionOptimisationProblem) algorithm.getOptimisationProblem()).getFunction();
        
    }

    @Override
    public OptimisationSolution getBestSolution() {
        return algorithm.getBestSolution();
    }

    @Override
    public Iterable<OptimisationSolution> getSolutions() {
        return solutions;
    }

    @Override
    public Topology<? extends Entity> getTopology() {
        return algorithm.getTopology();
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
}
