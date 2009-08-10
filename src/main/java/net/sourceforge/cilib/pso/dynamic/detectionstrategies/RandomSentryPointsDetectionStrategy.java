/**
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
package net.sourceforge.cilib.pso.dynamic.detectionstrategies;

import java.util.ArrayList;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;

/**
 * This class defines a detection strategy that uses a user-specified
 * {@link #numberOfSentries number of sentry points} and an {@local #epsilon} value to
 * detect whether a change has occured in the environment within a number of
 * {@link #interval consecutive iterations}.
 *
 * @InProceedings{title = "Adapting Particle Swarm Optimization to Dynamic Environments",
 *                      booktitle = "Proceedings of the International Conference on
 *                      Artificial Intelligence", author = "Anthony Jack Carlisle and Gerry
 *                      V. Dozier", pages = "429--434", year = "2000", }
 * @param <E> some {@link PopulationBasedAlgorithm population based algorithm}
 * @author Theuns Cloete
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
     * After every {@link #interval} iterations, iterate through all sentry points
     * and compare their previous fitness values with their current fitness values. An
     * environment change is detected when the difference between the previous and current
     * fitness values are &gt;= the specified {@link #epsilon} value. Although this detection
     * strategy only makes use of random sentry points, the entities in the population based
     * algorithm are sent through to the {@link #initializeSentryPoints(Topology) method to
     * initialize the sentry points.
     *
     * @param algorithm used to get hold of topology of entities and number of iterations
     * @return true if a change has been detected, false otherwise
     */
    @Override
    public boolean detect(PopulationBasedAlgorithm algorithm) {
        if (sentries.size() == 0) {
            initializeSentryPoints(algorithm.getTopology());
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
     * This method initializes the sentry points. The following steps are followed:
     * <ol>
     * <li>Randomly selects an {@link Entity} from the given {@link Topology}</li>
     * <li>Clones the selected entity a {@link #numberOfSentries specified} number of
     * times</li>
     * <li>Reinitializes each cloned entity</li>
     * <li>Evaluates each cloned entity (fitness value is calculated)</li>
     * <li>Adds each cloned entity to the list of sentry points</li>
     * </ol>
     *
     * @param topology the topology holding all the entities in the population
     * @throws an {@link IllegalStateException} when this method is called and
     *         {@link #sentries} is NOT <code>null</code>.
     */
    private void initializeSentryPoints(Topology<? extends Entity> topology) {
        if (sentries.size() != 0)
            throw new IllegalStateException("The sentry points have already been initialized");

        int size = Double.valueOf(numberOfSentries.getParameter()).intValue();

        Entity prototype = topology.get(randomGenerator.nextInt(topology.size()));
        sentries.ensureCapacity(size);

        for (int i = 0; i < size; ++i) {
            Entity sentry = prototype.getClone();
            sentry.reinitialise();
            sentry.calculateFitness();
            sentries.add(sentry);
        }
    }
}
