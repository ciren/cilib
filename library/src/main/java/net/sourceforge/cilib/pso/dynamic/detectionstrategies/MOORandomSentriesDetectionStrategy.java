/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.detectionstrategies;

import java.util.List;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.MOFitness;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;

/**
 * This class is similar to {@linkplain RandomSentriesDetectionStrategy}, but
 * uses MOFitness to compare sentries' current and previous values. This is
 * necessary for cases * where a higher-level algorithm handles the archive and
 * therefore the archive is not handled on the sub-algorithm level.
 *
 */
public class MOORandomSentriesDetectionStrategy<E extends PopulationBasedAlgorithm>
        extends RandomSentriesDetectionStrategy<E> {

    private static final long serialVersionUID = 4572728741093545926L;

    /**
     * Creates a new instance of RandomMOOSentriesDetectionStrategy.
     */
    public MOORandomSentriesDetectionStrategy() {
        //super is called automatically
    }

    /**
     * Creates a copy of the provided instance.
     * @param copy The instance that should be copied when creating the new
     * instance.
     */
    public MOORandomSentriesDetectionStrategy(MOORandomSentriesDetectionStrategy<E> copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MOORandomSentriesDetectionStrategy<E> getClone() {
        return new MOORandomSentriesDetectionStrategy<E>(this);
    }

    /**
     * After every {@link #interval} iteration, pick {@link #numberOfSentries a number of}
     * random entities from the given {@link Algorithm algorithm's} topology and
     * compare their previous fitness values with their current fitness values.
     * An environment change is detected when the difference between the
     * previous and current fitness values are &gt;= the specified {@link #epsilon}
     * value.
     * @param algorithm used to get hold of topology of entities and number of
     * iterations
     * @return true if a change has been detected, false otherwise
     */
    @Override
    public boolean detect(E algorithm) {
        int iterations = AbstractAlgorithm.get().getIterations();
        //System.out.println("iteration here in detection " + iterations);
    	if ((AbstractAlgorithm.get().getIterations() % interval == 0) && (AbstractAlgorithm.get().getIterations() != 0)) {
            List<? extends Entity> all = algorithm.getTopology();

            for (int i = 0; i < numberOfSentries.getParameter(); i++) {
                // select random sentry entity
                int random = Rand.nextInt(all.size());
                StandardParticle sentry = (StandardParticle)all.get(random);

                // check for change
                //double previousFitness = sentry.getFitness().getValue();

                boolean detectedChange = false;

                if (sentry.getFitness().getClass().getName().matches("MinimisationFitness")) {
                    Fitness previousFitness = sentry.getFitness();
                    sentry.calculateFitness();
                    Fitness currentFitness = sentry.getFitness();

                    if (Math.abs(previousFitness.getValue() - currentFitness.getValue()) >= epsilon) {
                        detectedChange = true;

                    }
                }
                else if (sentry.getFitness().getClass().getName().matches("StandardMOFitness")) {
                        MOFitness previousFitness = (MOFitness)sentry.getFitness();
                        sentry.calculateFitness();
                        MOFitness currentFitness = (MOFitness)sentry.getFitness();

                        for (int k=0; k < previousFitness.getDimension(); k++)
                            if (Math.abs(previousFitness.getFitness(k).getValue() -
                		currentFitness.getFitness(k).getValue()) >= epsilon) {
                		detectedChange = true;
                                break;
                            }
                }
                if (detectedChange) {
                    System.out.println("Detected a change");
                    return true;
                }

                /*System.out.println(sentry.getFitness().getClass().getName());
                MOFitness previousFitness = (MOFitness)sentry.getFitness();
                sentry.calculateFitness();
                MOFitness currentFitness = (MOFitness)sentry.getFitness();

                for (int k=0; k < previousFitness.getDimension(); k++)
                	if (Math.abs(previousFitness.getFitness(k).getValue() -
                			currentFitness.getFitness(k).getValue()) >= epsilon) {
                		return true;
                }*/

                // remove the selected element from the all list preventing it from being selected again
                all.remove(random);
            }
        }
        return false;
    }

}
