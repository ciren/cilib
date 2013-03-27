/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.moo.iterationstrategies;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.RespondingMultiPopulationCriterionBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.pso.dynamic.HigherLevelAllDynamicIterationStrategy;
import net.sourceforge.cilib.pso.dynamic.HigherLevelDynamicIterationStrategy;
import net.sourceforge.cilib.type.types.Type;

/**
 * This class enabled a higher-level {@link Algorithm} to control the iterations
 * of the sub-algorithms. This will enable a higher-level algorithm, such as
 * VEPSO, to let all swarms respond to a change if a change has been detected in
 * the environment of any of the swarms.
 *
 * @param <E>   The {@link PopulationBasedAlgorithm} that will have its
 *              {@link Entity}' positions added to the archive as potential solutions.
 */
public class HigherLevelArchivingIterationStrategy<E extends PopulationBasedAlgorithm> implements IterationStrategy<E> {

    private HigherLevelDynamicIterationStrategy iterationStrategy;

    /**
     * Creates a new instance of HigherLevelArchivingIterationStrategy.
     */
    public HigherLevelArchivingIterationStrategy() {
        this.iterationStrategy = new HigherLevelAllDynamicIterationStrategy();
    }

    /**
     * Creates a copy of provided instance.
     * @param copy Instance to copy.
     */
    public HigherLevelArchivingIterationStrategy(HigherLevelArchivingIterationStrategy<E> copy) {
        this.iterationStrategy = copy.iterationStrategy.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HigherLevelArchivingIterationStrategy<E> getClone() {
        return new HigherLevelArchivingIterationStrategy<E>(this);
    }

    public void setIterationStrategy(HigherLevelDynamicIterationStrategy iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

    public HigherLevelDynamicIterationStrategy getIterationStrategy() {
        return this.iterationStrategy;
    }

    protected void updateArchive(Topology<? extends Entity> population) {
        Algorithm topLevelAlgorithm = AbstractAlgorithm.getAlgorithmList().get(0);
        List<OptimisationSolution> optimisationSolutions = new ArrayList<OptimisationSolution>();
        for (Entity entity : population) {
            Type solution = entity.getCandidateSolution().getClone();
            optimisationSolutions.add(new OptimisationSolution(solution,
                    topLevelAlgorithm.getOptimisationProblem().getFitness(solution)));
        }
        Archive.Provider.get().addAll(optimisationSolutions);
    }

    /**
     * Performs an iteration of the algorithm:
     *  - Firstly, an iteration of the higher level algorithm is performed;
     *  - Secondly, the archive is updated.
     * @param algorithm The higher level {@link Algorithm}, e.g. VEPSO.
     */
    @Override
    public void performIteration(E algorithm) {
        this.getIterationStrategy().performIteration(algorithm);

        RespondingMultiPopulationCriterionBasedAlgorithm higherLevelAlgorithm =
        	(RespondingMultiPopulationCriterionBasedAlgorithm)AbstractAlgorithm.getAlgorithmList().get(0);

        for (PopulationBasedAlgorithm popAlg: higherLevelAlgorithm.getPopulations())
        	updateArchive(popAlg.getTopology());
    }

    public void setArchive(Archive archive) {
        Archive.Provider.set(archive);
    }

    public Archive getArchive() {
        return Archive.Provider.get();
    }

    @Override
    public BoundaryConstraint getBoundaryConstraint() {
        return this.iterationStrategy.getBoundaryConstraint();
    }

    @Override
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
