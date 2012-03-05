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
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.niching.Niche;
import net.sourceforge.cilib.pso.velocityprovider.LinearVelocityProvider;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 *
 */
public class ScatterMergeStrategy extends MergeStrategy {

    private double threshold;

    public ScatterMergeStrategy() {
        this.threshold = 10e-8;
    }

    /**
     * the particles of one of the subswarms that are merged are reinitialized
     * and inserted back into the main swarm.
     * @param algorithm; The niche algorithm that is merged
     */
    //@Override
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
                Vector vectorK1 = (Vector) Topologies.getBestEntity(k1.getTopology()).getCandidateSolution();
                Vector vectorK2 = (Vector) Topologies.getBestEntity(k2.getTopology()).getCandidateSolution();
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
        Particle neighbourhoodBest = Topologies.getBestEntity(topology);

        for (int i = 0; i < k2.getTopology().size(); i++) {
            Particle p = (Particle) k2.getTopology().get(i).getClone();
            Particle s = p.getClone();
            topology.add(p);

            p.setNeighbourhoodBest(neighbourhoodBest);
            s.reinitialise();
            s.calculateFitness();
            s.setNeighbourhoodBest(s.getClone());
            s.setVelocityProvider(new LinearVelocityProvider());
            ((PSO) ((Niche) algorithm).getMainSwarm()).getTopology().add(s);
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

    @Override
    public PopulationBasedAlgorithm f(PopulationBasedAlgorithm pop1, PopulationBasedAlgorithm pop2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
