/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.detectionstrategies;

import java.util.ArrayList;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;

/**
 * This class defines a detection strategy that uses a user-specified
 * {@link #numberOfSentries number of sentry points} and an {@link #epsilon} value to
 * detect whether a change has occurred in the environment within a number of
 * {@link #interval consecutive iterations}.
 *
 * @InProceedings{title = "Adapting Particle Swarm Optimization to Dynamic Environments",
 *                      booktitle = "Proceedings of the International Conference on
 *                      Artificial Intelligence", author = "Anthony Jack Carlisle and Gerry
 *                      V. Dozier", pages = "429--434", year = "2000", }
 * @param <E> some {@link PopulationBasedAlgorithm population based algorithm}
 */
public class RandomSentryPointsDetectionStrategy<E extends PopulationBasedAlgorithm> extends RandomSentriesDetectionStrategy<E> {
    private static final long serialVersionUID = -7908355064341601839L;

    protected ArrayList<Entity> sentries = null;

    public RandomSentryPointsDetectionStrategy() {
        // super() is automatically called
        int size = Double.valueOf(numberOfSentries.getParameter()).intValue();
        sentries = new ArrayList<Entity>(size);
    }

    public RandomSentryPointsDetectionStrategy(RandomSentryPointsDetectionStrategy<E> rhs) {
        super(rhs);
        sentries = new ArrayList<Entity>(rhs.sentries.size());

        for (Entity sentry : rhs.sentries) {
            sentries.add(sentry.getClone());
        }
    }

    @Override
    public RandomSentryPointsDetectionStrategy<E> getClone() {
        return new RandomSentryPointsDetectionStrategy<E>(this);
    }

    /**
     * After every {@link #interval} iterations, iterate through all sentry
     * points and compare their previous fitness values with their current
     * fitness values. An environment change is detected when the difference
     * between the previous and current fitness values are {@code >= epsilon}.
     * Although this detection strategy only makes use of random sentry points,
     * the entities in the population based algorithm are sent through to the
     * {@link #initialiseSentryPoints(Topology)} method to initialise the sentry
     * points.
     *
     * @param algorithm used to get hold of topology of entities and number of
     *                  iterations
     * @return          true if a change has been detected, false otherwise.
     */
    @Override
    public boolean detect(PopulationBasedAlgorithm algorithm) {
        if (sentries.isEmpty()) {
            initialiseSentryPoints(algorithm.getTopology());
        }

        if (algorithm.getIterations() % interval == 0) {
            for (Entity sentry : sentries) {
                double previousFitness = sentry.getFitness().getValue();
                sentry.calculateFitness();
                double currentFitness = sentry.getFitness().getValue();

                if(Math.abs(previousFitness - currentFitness) >=  epsilon) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method initialises the sentry points. The following steps are followed:
     * <ol>
     * <li>Randomly selects an {@link Entity} from the given {@link Topology}</li>
     * <li>Clones the selected entity a {@link #numberOfSentries specified} number of
     * times</li>
     * <li>Reinitialises each cloned entity</li>
     * <li>Evaluates each cloned entity (fitness value is calculated)</li>
     * <li>Adds each cloned entity to the list of sentry points</li>
     * </ol>
     *
     * @param topology the topology holding all the entities in the population
     * @throws an {@link IllegalStateException} when this method is called and
     *         {@link #sentries} is NOT <code>null</code>.
     */
    private void initialiseSentryPoints(Topology<? extends Entity> topology) {
        int size = Double.valueOf(numberOfSentries.getParameter()).intValue();

        Entity prototype = (Entity) new RandomSelector().on(topology).select();
        sentries.ensureCapacity(size);

        for (int i = 0; i < size; ++i) {
            Entity sentry = prototype.getClone();
            sentry.reinitialise();
            sentry.calculateFitness();
            sentries.add(sentry);
        }
    }
}
