/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.detectionstrategies;


import java.util.LinkedList;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.MOFitness;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;

/**
 * This class uses solutions from the archive to detect whether a change in the
 * environment has occurred. It should only be used for MOO problems.
 *
 */
public class MOORandomArchiveSentriesDetectionStrategy<E extends PopulationBasedAlgorithm>
        extends RandomSentriesDetectionStrategy<E> {

    /**
     * Creates a new instance of RandomMOOSentriesDetectionStrategy.
     */
    public MOORandomArchiveSentriesDetectionStrategy() {
        //super is called automatically
    }

    /**
     * Creates a copy of the provided instance.
     *
     * @param copy The instance that should be copied when creating the new
     * instance.
     */
    public MOORandomArchiveSentriesDetectionStrategy(MOORandomArchiveSentriesDetectionStrategy<E> copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MOORandomArchiveSentriesDetectionStrategy<E> getClone() {
        return new MOORandomArchiveSentriesDetectionStrategy<E>(this);
    }

    /**
     * After every {@link #interval} iteration, pick {@link #numberOfSentries a number of}
     * random entities from the given {@link Algorithm algorithm's} topology and
     * compare their previous fitness values with their current fitness values.
     * An environment change is detected when the difference between the
     * previous and current fitness values are &gt;= the specified {@link #epsilon}
     * value.
     *
     * @param algorithm used to get hold of topology of entities and number of
     * iterations
     * @return true if a change has been detected, false otherwise
     */

    @Override
    public boolean detect(E algorithm) {
        if ((AbstractAlgorithm.get().getIterations() % interval == 0) && (AbstractAlgorithm.get().getIterations() != 0)) {

            PopulationBasedAlgorithm populationBasedAlgorithm = (PopulationBasedAlgorithm) AbstractAlgorithm.getAlgorithmList().get(0);
            Problem problem = populationBasedAlgorithm.getOptimisationProblem();

            java.util.List<OptimisationSolution> currentSolutions = new LinkedList<OptimisationSolution>();
            java.util.List<OptimisationSolution> newSolutions = new LinkedList<OptimisationSolution>();

            for (OptimisationSolution solution : Archive.Provider.get()) {
                OptimisationSolution os = new OptimisationSolution(solution.getPosition(), problem.getFitness(solution.getPosition()));
                currentSolutions.add(solution);
                newSolutions.add(os);
            }

            for (int i = 0; i < numberOfSentries.getParameter(); i++) {
              if (newSolutions.size() > 0) {
                // select random sentry entity
                int random = Rand.nextInt(newSolutions.size());

                // check for change
                boolean detectedChange = false;

                MOFitness previousFitness = (MOFitness)currentSolutions.get(random).getFitness();
                MOFitness currentFitness = (MOFitness)newSolutions.get(random).getFitness();

                for (int k=0; k < previousFitness.getDimension(); k++) {
                    if (Math.abs(previousFitness.getFitness(k).getValue() -
                        currentFitness.getFitness(k).getValue()) >= epsilon) {
                	detectedChange = true;
                        break;
                    }
                }
                if (detectedChange) {
                    return true;
                }

                // remove the selected element from the all list preventing it from being selected again
                currentSolutions.remove(random);
                newSolutions.remove(random);
            } }
        }
        return false;
    }

}
