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
package net.sourceforge.cilib.pso.niching;

import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.visitor.RadiusVisitor;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * <p>
 * Merge sub-swarms if the radius of two subswarms overlap.
 * </p>
 * <p>
 * This overlap is determined by the radius of the sub-swarm. If the overlap
 * is less than a predefined threshold value, the sub-swarms will merge into
 * a single sub-swarm but having all participants within the one swarm
 * being transferred over to the other sub-swarm.
 * </p>
 * <p>
 * Upon completion of the entity migration, the empty sub-swarm is destroyed.
 * </p>
 *
 * @author gpampara
 */
public class StandardMergeStrategy implements MergeStrategy {
    private static final long serialVersionUID = 6790307057694598017L;

    private double threshold;

    public StandardMergeStrategy() {
        this.threshold  = 10e-8;
    }

    public StandardMergeStrategy(StandardMergeStrategy copy) {
        this.threshold = copy.threshold;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StandardMergeStrategy getClone() {
        return new StandardMergeStrategy(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(MultiPopulationBasedAlgorithm algorithm) {
        if (algorithm.getPopulations().size() < 2)
            return;

        DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
        RadiusVisitor radiusVisitor = new RadiusVisitor();

        for (int i = 0; i < algorithm.getPopulations().size()-1; i++) {
            PopulationBasedAlgorithm k1 = algorithm.getPopulations().get(i);

            k1.accept(radiusVisitor);
            double k1Radius = radiusVisitor.getResult().doubleValue();

            for (int j = i+1; j < algorithm.getPopulations().size(); j++) {
                PopulationBasedAlgorithm k2 = algorithm.getPopulations().get(j);

                k2.accept(radiusVisitor);
                double k2Radius = radiusVisitor.getResult().doubleValue();
                Vector vectorK1 = (Vector) k1.getTopology().getBestEntity().getCandidateSolution();
                Vector vectorK2 = (Vector) k2.getTopology().getBestEntity().getCandidateSolution();

                // Radii need to be normalized based on the size of the domain?????????
//                Vector normalK1 = vectorK1.normalize();
//                Vector normalK2 = vectorK2.normalize();
                Vector normalK1 = vectorK1;
                Vector normalK2 = vectorK2;

                double distance = distanceMeasure.distance(normalK1, normalK2);//Math.abs(normalK1.subtract(normalK2).norm());

                if (k1Radius == k2Radius && k1Radius == 0) {
                    if (distance < threshold)
                        mergeSwarms(algorithm, k1, k2);

                    continue;
                }

                if (distance < threshold) {
                    mergeSwarms(algorithm, k1, k2);
                }
            }
        }
    }

    private void mergeSwarms(final MultiPopulationBasedAlgorithm algorithm, final PopulationBasedAlgorithm k1, PopulationBasedAlgorithm k2) {
        Topology<Particle> topology = (Topology<Particle>) k1.getTopology();
        Particle neighbourhoodBest = topology.getBestEntity();

        // migrate all entities from k2 into k1
        for (int i = 0; i < k2.getTopology().size(); i++) {
            Particle p = (Particle) k2.getTopology().get(i);
            topology.add(p);

            p.setNeighbourhoodBest(neighbourhoodBest);
        }

        algorithm.getPopulations().remove(k2);
        k2 = null;
    }

    /**
     * Get the merge threshold value.
     * @return The value of the merge threshold.
     */
    public double getThreshold() {
        return threshold;
    }

    /**
     * Set the merge threshold value.
     * @param threshold The value to set.
     */
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

}
