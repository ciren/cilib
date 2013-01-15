/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import java.util.Iterator;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.distancemeasure.ChebyshevDistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;

/**
 * <p>
 * Function to calculate the diversity measure, spatial extent as defined by Blackwell
 * </p><p>
 * References:
 * </p><p><ul><li>
 * T. Blackwell, "Particle Swarms and Population DiversityOld I: Analysis",
 * Genetic and Evolutionary Computation Conference Workshop on Evolutionary Algorithms for Dynamic Optimization Problems,
 * pages 9--13, 2003.
 * </li><li>
 * AP Engelbrecht, "Fundamentals of Computational Swarm Intelligence",
 * Wiley & Sons, pages 125, 2005.
 * </li></ul></p>
 */
public class SpatialExtent implements Measurement<Real> {

    private static final long serialVersionUID = -6846992935896199456L;

    @Override
    public SpatialExtent getClone() {
        return this;
    }

    @Override
    public Real getValue(Algorithm algorithm) {

        /*PSO pso = (PSO) Algorithm.get();

        Iterator k = pso.getTopology().iterator();
        Particle particle = (Particle) k.next();
        Vector pos = (Vector) particle.getPosition();
        Vector maxVector = pos.clone();
        Vector minVector = pos.clone();
        while (k.hasNext()) {
        particle = (Particle) k.next();
        Vector position = (Vector) particle.getPosition();
        for (int j = 0; j < position.getDimension(); ++j) {
        double posValue = position.getReal(j);
        double maxValue = maxVector.getReal(j);
        double minValue = minVector.getReal(j);

        if (posValue > maxValue)
        maxVector.setReal(j,posValue);

        if (posValue < minValue)
        minVector.setReal(j,posValue);
        }
        }

        double maxDimensionalDifference = 0.0;
        for (int j = 0; j < maxVector.getDimension(); ++j) {
        double dimensionDifference = maxVector.getReal(j) - minVector.getReal(j);
        if (dimensionDifference > maxDimensionalDifference)
        maxDimensionalDifference = dimensionDifference;
        }
        return new Real(maxDimensionalDifference);*/


        PopulationBasedAlgorithm populationBasedAlgorithm = (PopulationBasedAlgorithm) algorithm;
        DistanceMeasure chebyshevDistance = new ChebyshevDistanceMeasure();
        double maxDimensionalDifference = 0.0;
        //PSO pso = (PSO) Algorithm.get();

        Iterator<? extends Entity> populationIterator_1 = populationBasedAlgorithm.getTopology().iterator();

        while (populationIterator_1.hasNext()) {
            Entity entity_1 = populationIterator_1.next();
            Vector entity_1Contents = (Vector) entity_1.getCandidateSolution();

            Iterator<? extends Entity> populationIterator_2 = populationBasedAlgorithm.getTopology().iterator();

            while (populationIterator_2.hasNext()) {
                Entity entity_2 = populationIterator_2.next();
                Vector entity_2Contents = (Vector) entity_2.getCandidateSolution();

                double dimensionalDifference = chebyshevDistance.distance(entity_1Contents, entity_2Contents);

                if (dimensionalDifference > maxDimensionalDifference) {
                    maxDimensionalDifference = dimensionalDifference;
                }
            }
        }

        return Real.valueOf(maxDimensionalDifference);
    }
}
