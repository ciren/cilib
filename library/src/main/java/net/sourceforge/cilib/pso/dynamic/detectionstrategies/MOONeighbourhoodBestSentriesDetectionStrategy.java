/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.detectionstrategies;


import java.util.Set;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.MOFitness;

public class MOONeighbourhoodBestSentriesDetectionStrategy<E extends PopulationBasedAlgorithm> extends EnvironmentChangeDetectionStrategy<E> {
    private static final long serialVersionUID = 3598067152913033487L;

    public MOONeighbourhoodBestSentriesDetectionStrategy() {
        // super() is automatically called
    }

    public MOONeighbourhoodBestSentriesDetectionStrategy(MOONeighbourhoodBestSentriesDetectionStrategy<E> rhs) {
        super(rhs);
    }

    @Override
    public MOONeighbourhoodBestSentriesDetectionStrategy<E> getClone() {
        return new MOONeighbourhoodBestSentriesDetectionStrategy<E>(this);
    }

    @Override
    public boolean detect(PopulationBasedAlgorithm algorithm) {
        if (algorithm.getIterations() % interval == 0) {
            Set<? extends Entity> sentries = Topologies.getNeighbourhoodBestEntities(algorithm.getTopology());

            boolean detectedChange = false;
            for (Entity sentry : sentries) {
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
        }
            }

        return false;
    }
}
