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

package net.sourceforge.cilib.pso.niching.enhanced;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.visitor.RadiusVisitor;
import net.sourceforge.cilib.pso.niching.AbsorptionStrategy;
import net.sourceforge.cilib.pso.niching.Niche;
import net.sourceforge.cilib.pso.velocityprovider.LinearVelocityProvider;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 *
 */
public class EuclideanDiversityAbsorptionStrategy implements AbsorptionStrategy {

    private DistanceMeasure distanceMeasure;
    private double threshold;

    public EuclideanDiversityAbsorptionStrategy() {
        distanceMeasure = new EuclideanDistanceMeasure();
        threshold = 0.1;
    }

    public EuclideanDiversityAbsorptionStrategy(EuclideanDiversityAbsorptionStrategy copy) {
        this.distanceMeasure = copy.distanceMeasure;
        this.threshold = copy.threshold;
    }

    /**
     * a particle is absorbed into a subswarm if the particle is within the
     * radius of the subswarm and the diversity of the subswarm is below a given
     * threshold
     * @param algorithm. The niche algorithm to be absorb.
     */
    @Override
    public void absorb(Niche algorithm) {
        for (PopulationBasedAlgorithm pba : algorithm.getPopulations()) {
            RadiusVisitor radiusVisitor = new RadiusVisitor();
            pba.accept(radiusVisitor);

            double radius = radiusVisitor.getResult().doubleValue();
            double diversity = calculateDiversity(pba);

            Topology<? extends Entity> mainSwarmTopology = algorithm.getMainSwarm().getTopology();
            for (int i = 0; i < mainSwarmTopology.size(); i++) {
                Entity entity = mainSwarmTopology.get(i);
                double distance = distanceMeasure.distance(entity.getCandidateSolution(), Topologies.getBestEntity(pba.getTopology()).getCandidateSolution());
                if ((distance <= radius) && (diversity < threshold)) {
                    Particle p = (Particle) entity;
                    p.setVelocityProvider(new LinearVelocityProvider());
                    p.setNeighbourhoodBest((Particle) Topologies.getBestEntity(pba.getTopology()));
                    Topology<Particle> topology = (Topology<Particle>) pba.getTopology();
                    topology.add(p);
                    algorithm.getMainSwarm().getTopology().remove(entity);
                }
            }
        }
    }

    private double calculateDiversity(PopulationBasedAlgorithm pba){
        Topology<Particle> topology = (Topology<Particle>) pba.getTopology();
        double sum = 0.0;
        Vector best = (Vector) Topologies.getBestEntity(topology).getBestPosition();
        for(Particle p: topology){
            sum += distanceMeasure.distance(p.getPosition(), best);
        }
        double size = topology.size() * 1.0;
        return sum/size;
    }

}
