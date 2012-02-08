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
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.visitor.RadiusVisitor;
import net.sourceforge.cilib.pso.niching.AbsorptionStrategy;
import net.sourceforge.cilib.pso.niching.Niche;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 *
 * @author wayne
 */
public class DirectionalBasedAbsorptionStrategy implements AbsorptionStrategy {

    private DistanceMeasure distanceMeasure;

    public DirectionalBasedAbsorptionStrategy() {
        distanceMeasure = new EuclideanDistanceMeasure();
    }

    public DirectionalBasedAbsorptionStrategy(DirectionalBasedAbsorptionStrategy copy) {
        this.distanceMeasure = copy.distanceMeasure;
    }

    /**
     * absorp a particle if, and only if the particle is contained withing the
     * radius of a subswarm and the particle moves in the same direction as the
     * global best position of the subswarm.
     * @param algorithm; Is the niche algorithm that has to absorb.
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
                Vector vec1 = (Vector) entity.getCandidateSolution();
                Vector vec2 = (Vector) pba.getTopology().getBestEntity().getCandidateSolution();
                double direction = vec1.dot(vec2);
                if (distance <= radius && direction < 0) {
                    Particle p = (Particle) entity;
                    StandardVelocityProvider velocityUpdateStrategy = new StandardVelocityProvider();
                    velocityUpdateStrategy.setSocialAcceleration(new ConstantControlParameter(0.0));
                    p.setVelocityProvider(velocityUpdateStrategy);
                    p.setNeighbourhoodBest((Particle) pba.getTopology().getBestEntity());
                    Topology<Particle> topology = (Topology<Particle>) pba.getTopology();
                    topology.add(p);
                    algorithm.getMainSwarm().getTopology().remove(entity);
                }
            }
        }
    }

    /**
     * @return the distanceMeasure
     */
    public DistanceMeasure getDistanceMeasure() {
        return distanceMeasure;
    }

    /**
     * @param distanceMeasure the distanceMeasure to set
     */
    public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

}
