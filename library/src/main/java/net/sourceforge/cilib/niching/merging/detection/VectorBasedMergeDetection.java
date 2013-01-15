/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.merging.detection;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

public class VectorBasedMergeDetection extends MergeDetection {

    @Override
    public Boolean f(PopulationBasedAlgorithm a, PopulationBasedAlgorithm b) {
        Particle p1 = (Particle) Topologies.getBestEntity(a.getTopology(), new SocialBestFitnessComparator());
        Particle p2 = (Particle) Topologies.getBestEntity(b.getTopology(), new SocialBestFitnessComparator());
        Vector v1 = ((Vector) p1.getBestPosition()).subtract((Vector) p1.getCandidateSolution());
        Vector v2 = ((Vector) p2.getBestPosition()).subtract((Vector) p2.getCandidateSolution());

        return v1.dot(v2) > 0;
   }

}
