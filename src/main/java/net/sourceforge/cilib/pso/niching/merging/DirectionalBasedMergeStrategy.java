/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.pso.niching.merging;

import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.visitor.RadiusVisitor;
import net.sourceforge.cilib.pso.niching.merging.MergeStrategy;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 *
 */
public class DirectionalBasedMergeStrategy implements MergeStrategy {

    private double threshold;

    public DirectionalBasedMergeStrategy() {
        threshold = 10e-8;
    }

    public DirectionalBasedMergeStrategy(DirectionalBasedMergeStrategy copy) {
        this.threshold = copy.threshold;
    }

    @Override
    public MergeStrategy getClone() {
        return new DirectionalBasedMergeStrategy(this);
    }

     /**
     * Only subswarms moving towards the same niche will be merged into one if
     * the areas they cover in search space overlap.
     * @param algorithm; Is the niche algorithm that has to merge.
     */
    @Override
    public void merge(MultiPopulationBasedAlgorithm algorithm) {
        if (algorithm.getPopulations().size() < 2)
            return;

        DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
        RadiusVisitor radiusVisitor = new RadiusVisitor();

        for (int i = 0; i < algorithm.getPopulations().size()-1; i++) {
            PopulationBasedAlgorithm sk1 = algorithm.getPopulations().get(i);

            sk1.accept(radiusVisitor);
            double sk1Radius = radiusVisitor.getResult().doubleValue();

            for (int j = i+1; j < algorithm.getPopulations().size(); j++) {
                PopulationBasedAlgorithm sk2 = algorithm.getPopulations().get(j);

                sk2.accept(radiusVisitor);
                double sk2Radius = radiusVisitor.getResult().doubleValue();
                Vector vectorSK1 = (Vector) Topologies.getBestEntity(sk1.getTopology()).getCandidateSolution();
                Vector vectorSK2 = (Vector) Topologies.getBestEntity(sk2.getTopology()).getCandidateSolution();

                Vector normalSK1 = vectorSK1.normalize();
                Vector normalSK2 = vectorSK2.normalize();

                double distance = distanceMeasure.distance(normalSK1, normalSK2);

                double direction = normalSK1.dot(normalSK2);

                if(direction < 0) {

                    if (sk1Radius == sk2Radius && sk1Radius == 0) {
                        if (distance < getThreshold())
                            mergeSwarms(algorithm, sk1, sk2);

                        continue;
                    }

                    if (distance < getThreshold()) {
                        mergeSwarms(algorithm, sk1, sk2);
                    }
                }
            }
        }
    }

    private void mergeSwarms(final MultiPopulationBasedAlgorithm algorithm, final PopulationBasedAlgorithm k1, PopulationBasedAlgorithm k2) {
        Topology<Particle> topology = (Topology<Particle>) k1.getTopology();
        Particle neighbourhoodBest = Topologies.getBestEntity(topology);

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
     * @return the threshold
     */
    public double getThreshold() {
        return threshold;
    }

    /**
     * @param threshold the threshold to set
     */
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

}
