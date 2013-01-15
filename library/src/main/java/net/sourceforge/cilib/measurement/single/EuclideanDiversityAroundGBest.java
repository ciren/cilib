/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.distancemeasure.DistanceMeasure;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;

/**
 * TODO: Complete this javadoc.
 */
public class EuclideanDiversityAroundGBest implements Measurement<Real> {

    private static final long serialVersionUID = 8221420456303029095L;

    @Override
    public EuclideanDiversityAroundGBest getClone() {
        return this;
    }

    @Override
    public Real getValue(Algorithm algorithm) {
        PopulationBasedAlgorithm populationBasedAlgorithm = (PopulationBasedAlgorithm) algorithm;

        Vector center = (Vector) algorithm.getBestSolution().getPosition();
        DistanceMeasure distance = new EuclideanDistanceMeasure();
        double diameter = 0;

        Topology<? extends Entity> topology = populationBasedAlgorithm.getTopology();
        for (Entity entity : topology) {
            diameter += distance.distance(center, (Vector) entity.getCandidateSolution());
        }

        return Real.valueOf(diameter / topology.size());
    }
}
