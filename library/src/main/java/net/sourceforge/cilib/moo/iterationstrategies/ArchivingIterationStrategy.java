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
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.Types;

/**
 * <p>
 * A generic multi-objective {@link IterationStrategy} class that wraps another {@code IterationStrategy}
 * and is responsible for populating the {@link Archive} of Pareto optimal solutions after the execution
 * of the inner {@code IterationStrategy} class.
 * </p>
 *
 *
 * @param <E> The {@link PopulationBasedAlgorithm} that will have it's entities' positions added to
 * the archive as potential solutions.
 */
public class ArchivingIterationStrategy<E extends PopulationBasedAlgorithm> implements IterationStrategy<E> {

    private static final long serialVersionUID = 4029628616324259998L;
    private IterationStrategy<PopulationBasedAlgorithm> iterationStrategy;

    public ArchivingIterationStrategy() {
    }

    public ArchivingIterationStrategy(ArchivingIterationStrategy<E> copy) {
        this.iterationStrategy = copy.iterationStrategy.getClone();
    }

    @Override
    public ArchivingIterationStrategy<E> getClone() {
        return new ArchivingIterationStrategy<E>(this);
    }

    public void setIterationStrategy(IterationStrategy<PopulationBasedAlgorithm> iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

    public IterationStrategy<PopulationBasedAlgorithm> getIterationStrategy() {
        return this.iterationStrategy;
    }

    protected void updateArchive(Topology<? extends Entity> population) {
        Algorithm topLevelAlgorithm = AbstractAlgorithm.getAlgorithmList().get(0);
        List<OptimisationSolution> optimisationSolutions = new ArrayList<OptimisationSolution>();
        for (Entity entity : population) {
            if(Types.isInsideBounds(entity.getCandidateSolution())){
                Type solution = entity.getCandidateSolution().getClone();
                optimisationSolutions.add(new OptimisationSolution(solution,
                    topLevelAlgorithm.getOptimisationProblem().getFitness(solution)));
            }
        }
        Archive.Provider.get().addAll(optimisationSolutions);
    }

    @Override
    public void performIteration(E algorithm) {
        this.iterationStrategy.performIteration(algorithm);
        updateArchive(algorithm.getTopology());
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
