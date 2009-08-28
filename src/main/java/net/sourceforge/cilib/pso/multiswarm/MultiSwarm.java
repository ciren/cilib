/*
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
package net.sourceforge.cilib.pso.multiswarm;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;//I
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.type.DomainRegistry;

/**
 *
 */
public class MultiSwarm extends MultiPopulationBasedAlgorithm {
    private static final long serialVersionUID = -3859431217295779546L;
    protected  IterationStrategy<MultiSwarm> multiSwarmsIterationStrategy;
    public MultiSwarm() {
        super();
        this.multiSwarmsIterationStrategy = new MultiSwarmIterationStrategy();
    }

    public MultiSwarm(MultiSwarm copy) {
        super(copy);
        this.multiSwarmsIterationStrategy = copy.multiSwarmsIterationStrategy;
    }

    @Override
    public MultiSwarm getClone() {
        return new MultiSwarm(this);
    }

    /**
     * @return the sum of all the populations
     */
    public int getPopulationSize() {
        int sum = 0;
        for (PopulationBasedAlgorithm currentAlgorithm : subPopulationsAlgorithms) {
            sum += currentAlgorithm.getTopology().size();
        }

        return sum;
    }

    public void setAlgorithm(PopulationBasedAlgorithm algorithm) {
        subPopulationsAlgorithms.add(algorithm);
    }

    /**
     * initialises every population.
     *
     */
    @Override
    public void performInitialisation()    {
        OptimisationProblem problem = getOptimisationProblem();//getCoevolutionOptimisationProblem();
        for (PopulationBasedAlgorithm currentAlgorithm : subPopulationsAlgorithms) {
            currentAlgorithm.setOptimisationProblem(problem);
            currentAlgorithm.performInitialisation();
        }//for
    }

    @Override
    public OptimisationSolution getBestSolution() {
        OptimisationSolution bestSolution = subPopulationsAlgorithms.get(0).getBestSolution();
        for (PopulationBasedAlgorithm currentAlgorithm : subPopulationsAlgorithms) {
            if(bestSolution.compareTo(currentAlgorithm.getBestSolution())<0)
                bestSolution = currentAlgorithm.getBestSolution();
            }
        return bestSolution;
    }

    /**
     * Can be useful to compare how the different populations are performing.
     * @return a list of the best solution in each population.
     */
    @Override
    public List<OptimisationSolution> getSolutions() {
        List<OptimisationSolution> solutions = new ArrayList<OptimisationSolution>();
        for (PopulationBasedAlgorithm currentAlgorithm : this.getPopulations()) {
             for (OptimisationSolution solution : currentAlgorithm.getSolutions())
                 solutions.add(solution);
        }
        return solutions;
    }

    @Override
    public void algorithmIteration() {
        multiSwarmsIterationStrategy.performIteration(this);
    }

    public IterationStrategy<MultiSwarm> getMultiSwarmIterationStrategy() {
        return multiSwarmsIterationStrategy;
    }

    public void setMultiSwarmIterationStrategy(IterationStrategy<MultiSwarm> MultiSwarmIterationStrategy) {
        this.multiSwarmsIterationStrategy = MultiSwarmIterationStrategy;
    }


    /* (non-Javadoc)
     * @see net.sourceforge.cilib.Problem.OptimisationProblem#getFitnessEvaluations()
     */
    public int getFitnessEvaluations() {
        throw new UnsupportedOperationException("Implementation still required.");
    }

    public DomainRegistry getDomain() {
        throw new RuntimeException("Get domain on multiswarms still needs to be defined!");
    }

    public DomainRegistry getBehaviouralDomain() {
        throw new UnsupportedOperationException("Implementation still required.");
    }

    public DataSetBuilder getDataSetBuilder() {
        throw new UnsupportedOperationException("Implementation still required.");
    }

    public void setDataSetBuilder(DataSetBuilder dataSet) {
        throw new UnsupportedOperationException("Implementation still required.");
    }

    public void setPopulationSize(int i) {
        throw new UnsupportedOperationException("setPopulationSize() is not supported");
    }

    public double getRadius(){
        throw new UnsupportedOperationException("getRadius() is not supported");
    }

    public double getDiameter(){
        throw new UnsupportedOperationException("getDiameter() is not supported");
    }

    public void setTopology(Topology t) {
        throw new UnsupportedOperationException("setTopology() is not supported");
    }

    public Topology<? extends Entity> getTopology() {
        throw new UnsupportedOperationException("getTopology() is not supported");
    }
}
