/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.detectionstrategies;


import java.util.Set;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.HasNeighbourhood;
import net.sourceforge.cilib.algorithm.population.HasTopology;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topologies;

public class NeighbourhoodBestSentriesDetectionStrategy extends EnvironmentChangeDetectionStrategy {
    private static final long serialVersionUID = 3598067152913033487L;

    public NeighbourhoodBestSentriesDetectionStrategy() {
        // super() is automatically called
    }

    public NeighbourhoodBestSentriesDetectionStrategy(NeighbourhoodBestSentriesDetectionStrategy rhs) {
        super(rhs);
    }

    @Override
    public NeighbourhoodBestSentriesDetectionStrategy getClone() {
        return new NeighbourhoodBestSentriesDetectionStrategy(this);
    }

    @Override
    public <A extends HasTopology & Algorithm & HasNeighbourhood> boolean detect(A algorithm) {
        if (algorithm.getIterations() % interval == 0) {
            Set<? extends Entity> sentries = Topologies.getNeighbourhoodBestEntities(algorithm.getTopology(), algorithm.getNeighbourhood());

            for (Entity sentry : sentries) {
                double previousFitness = sentry.getFitness().getValue();
                sentry.updateFitness(sentry.getBehaviour().getFitnessCalculator().getFitness(sentry));
                double currentFitness = sentry.getFitness().getValue();

                if(Math.abs(previousFitness - currentFitness) >=  epsilon) {
                    return true;
                }
            }
        }
        return false;
    }
}
