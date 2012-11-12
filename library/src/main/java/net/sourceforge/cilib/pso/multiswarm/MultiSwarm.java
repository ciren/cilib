/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.multiswarm;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
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
    public void algorithmInitialisation()    {
        Problem problem = getOptimisationProblem();//getCoevolutionOptimisationProblem();
        for (PopulationBasedAlgorithm currentAlgorithm : subPopulationsAlgorithms) {
            currentAlgorithm.setOptimisationProblem(problem);
            currentAlgorithm.performInitialisation();
        }//for
    }

    @Override
    public OptimisationSolution getBestSolution() {
        OptimisationSolution bestSolution = subPopulationsAlgorithms.get(0).getBestSolution();
        for (PopulationBasedAlgorithm currentAlgorithm : subPopulationsAlgorithms) {
            if(bestSolution.compareTo(currentAlgorithm.getBestSolution())<0) {
                bestSolution = currentAlgorithm.getBestSolution();
            }
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
}
