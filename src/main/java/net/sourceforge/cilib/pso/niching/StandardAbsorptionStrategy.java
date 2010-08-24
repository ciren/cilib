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
package net.sourceforge.cilib.pso.niching;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.visitor.RadiusVisitor;
import net.sourceforge.cilib.pso.velocityprovider.GCVelocityProvider;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * <p>
 * Standard absorption strategy for NichePSO.
 * </p>
 * <p>
 * Absorption is a process that occurs when an entity from the main swarm wonders
 * into the radius of the sub-swarm. When this occurs, the entity is removed from
 * the main swarm and is incorporated into the sub-swarm.
 * </p>
 * @author gpampara
 */
public class StandardAbsorptionStrategy implements AbsorptionStrategy {

    private DistanceMeasure distanceMeasure;

    public StandardAbsorptionStrategy() {
        this.distanceMeasure = new EuclideanDistanceMeasure();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void absorb(Niche algorithm) {
        for (PopulationBasedAlgorithm pba : algorithm.getPopulations()) {
            RadiusVisitor radiusVisitor = new RadiusVisitor();
            pba.accept(radiusVisitor);

            double radius = radiusVisitor.getResult().doubleValue();

            Topology<? extends Entity> mainSwarmTopology = algorithm.getMainSwarm().getTopology();
            for (int i = 0; i < mainSwarmTopology.size(); i++) {
                Entity entity = mainSwarmTopology.get(i);
                double distance = distanceMeasure.distance(entity.getCandidateSolution(), pba.getTopology().getBestEntity().getCandidateSolution());
                if (distance <= radius) {
                    Particle p = (Particle) entity;
                    p.setVelocityProvider(new GCVelocityProvider());
                    p.setNeighbourhoodBest((Particle) pba.getTopology().getBestEntity());
                    Topology<Particle> topology = (Topology<Particle>) pba.getTopology();
                    topology.add(p);
                    algorithm.getMainSwarm().getTopology().remove(entity);
                }
            }
        }
    }

}
