/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single.diversity;

import java.util.Iterator;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * This extends the concept of diversity around the population center by evaluating the average distance
 * around each entity in the population, i.e. each population entity is used as a center, and then
 * calculating the average over all these distances.
 *
 */
public class AverageDiversityAroundAllEntities extends Diversity {

    private static final long serialVersionUID = 898972772053582980L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        PopulationBasedAlgorithm populationBasedAlgorithm = (PopulationBasedAlgorithm) algorithm;
        int numberOfEntities = populationBasedAlgorithm.getTopology().size();

        Iterator<? extends Entity> populationCenterIterator = populationBasedAlgorithm.getTopology().iterator();

        double totalDistanceSum = 0.0;

        while (populationCenterIterator.hasNext()) {
            Vector currentCenter = (Vector) (((Entity) populationCenterIterator.next()).getCandidateSolution());
            Iterator<? extends Entity> populationIterator = populationBasedAlgorithm.getTopology().iterator();
            double currentDistanceSum = 0.0;

            while (populationIterator.hasNext()) {
                Vector currentEntityPosition = (Vector) (((Entity) populationIterator.next()).getCandidateSolution());
                currentDistanceSum += distanceMeasure.distance(currentCenter, currentEntityPosition);
            }

            totalDistanceSum += currentDistanceSum / numberOfEntities;
        }

        totalDistanceSum /= numberOfEntities;
        totalDistanceSum /= normalisationParameter.getNormalisationParameter(populationBasedAlgorithm);

        return Real.valueOf(totalDistanceSum);
    }
}
