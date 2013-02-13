/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.detectionstrategies;


import java.util.Set;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topologies;

public class NeighbourhoodBestSentriesDetectionStrategy<E extends SinglePopulationBasedAlgorithm> extends EnvironmentChangeDetectionStrategy<E> {
    private static final long serialVersionUID = 3598067152913033487L;

    public NeighbourhoodBestSentriesDetectionStrategy() {
        // super() is automatically called
    }

    public NeighbourhoodBestSentriesDetectionStrategy(NeighbourhoodBestSentriesDetectionStrategy<E> rhs) {
        super(rhs);
    }

    @Override
    public NeighbourhoodBestSentriesDetectionStrategy<E> getClone() {
        return new NeighbourhoodBestSentriesDetectionStrategy<E>(this);
    }

    @Override
    public boolean detect(SinglePopulationBasedAlgorithm algorithm) {
        if (algorithm.getIterations() % interval == 0) {
            Set<? extends Entity> sentries = Topologies.getNeighbourhoodBestEntities(algorithm.getTopology(), algorithm.getNeighbourhood());

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
}
