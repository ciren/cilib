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
package net.sourceforge.cilib.coevolution;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.coevolution.CoevolutionOptimisationProblem;
import net.sourceforge.cilib.problem.coevolution.CompetitiveCoevolutionProblemAdapter;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.type.DomainRegistry;



/**
 * @author Julien Duhain
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CoevolutionAlgorithm extends MultiPopulationBasedAlgorithm {
    private static final long serialVersionUID = -3859431217295779546L;
    protected CoevolutionIterationStrategy coevolutionIterationStrategy;
    public CoevolutionAlgorithm() {
        super();
    }

    public CoevolutionAlgorithm(CoevolutionAlgorithm copy) {
        super(copy);
        this.coevolutionIterationStrategy = copy.coevolutionIterationStrategy;
    }

    @Override
    public CoevolutionAlgorithm getClone() {
        return new CoevolutionAlgorithm(this);
    }

    /**
     * @return the sum of all the populations
     */
    public int getPopulationSize() {
        int sum = 0;
        for (PopulationBasedAlgorithm currentAlgorithm : subPopulationsAlgorithms) {
            sum += currentAlgorithm.getPopulationSize();
        }

        return sum;
    }

    public void setAlgorithm(PopulationBasedAlgorithm algorithm) {
        subPopulationsAlgorithms.add(algorithm);
    }

    /**
     * @return optimization problem cast to type CoevolutionOptimizationProblem
     */
    public CoevolutionOptimisationProblem getCoevolutionOptimisationProblem(){
        return (CoevolutionOptimisationProblem)optimisationProblem;
    }
    /**
     * initialises every population.
     *
     */
    public void performInitialisation()    {
        CoevolutionOptimisationProblem problem = getCoevolutionOptimisationProblem();
        if(problem.getAmountSubPopulations() != subPopulationsAlgorithms.size())
            throw new RuntimeException("The amount of sub populations specified do not match the amount required by the current problem");

        int populationID = 1;
        for (PopulationBasedAlgorithm currentAlgorithm : subPopulationsAlgorithms) {
            //coevolutionIterationStrategy.setEntityType(currentAlgorithm, populationID);
            problem.initializeEntities(currentAlgorithm, populationID);
            currentAlgorithm.setOptimisationProblem(new CompetitiveCoevolutionProblemAdapter(populationID, problem.getSubPopulationDomain(populationID), problem));
            currentAlgorithm.performInitialisation();
            populationID++;
        }
    }

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
    public List<OptimisationSolution> getSolutions() {
        List<OptimisationSolution> solutions = new ArrayList<OptimisationSolution>();
        for (PopulationBasedAlgorithm currentAlgorithm : this.getPopulations()) {
             solutions.addAll(currentAlgorithm.getSolutions());
        }
        return solutions;
    }

    @Override
    public void algorithmIteration() {
        coevolutionIterationStrategy.performIteration(this);
    }

    public CoevolutionIterationStrategy getCoevolutionIterationStrategy() {
        return coevolutionIterationStrategy;
    }

    public void setCoevolutionIterationStrategy(CoevolutionIterationStrategy coevolutionIterationStrategy) {
        this.coevolutionIterationStrategy = coevolutionIterationStrategy;
    }


    /* (non-Javadoc)
     * @see net.sourceforge.cilib.Problem.OptimisationProblem#getFitnessEvaluations()
     */
    public int getFitnessEvaluations() {
        // TODO Auto-generated method stub
        return 0;
    }

    public DomainRegistry getDomain() {
        throw new RuntimeException("Get domain on Coevolution still needs to be defined!");
    }

    public DomainRegistry getBehaviouralDomain() {
        // TODO Auto-generated method stub
        return null;
    }

    public DataSetBuilder getDataSetBuilder() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setDataSetBuilder(DataSetBuilder dataSet) {
        // TODO Auto-generated method stub

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

    @SuppressWarnings("unchecked")
    public void setTopology(Topology t) {
        throw new UnsupportedOperationException("setTopology() is not supported");
    }

    public Topology<? extends Entity> getTopology() {
        throw new UnsupportedOperationException("getTopology() is not supported");
    }

    @Override
    public void setOptimisationProblem(OptimisationProblem problem) {
        // TODO Auto-generated method stub
        if(!(problem instanceof CoevolutionOptimisationProblem))
            throw new RuntimeException("Co-evolutionaty algorithms can only optimize problems that impliment the CoevolutionOptimisationProblem interface");
        super.setOptimisationProblem(problem);
    }
}
