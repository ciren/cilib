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
package net.sourceforge.cilib.pso.velocityupdatestrategies;

import java.util.Iterator;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author Olusegun Olorunda
 */
public class FIPSVelocityUpdate extends StandardVelocityUpdate {
    private static final long serialVersionUID = 6391914534943249737L;

    public FIPSVelocityUpdate() {
        super();
        cognitiveAcceleration = new ConstantControlParameter(1.496180);
        socialAcceleration = new ConstantControlParameter(1.496180);
    }

    public FIPSVelocityUpdate(FIPSVelocityUpdate copy) {
        super(copy);
    }

    @Override
    public FIPSVelocityUpdate getClone() {
        return new FIPSVelocityUpdate(this);
    }

    @Override
    public void updateVelocity(Particle particle) {
        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();

        Topology<Particle> topology = ((PSO) AbstractAlgorithm.get()).getTopology();
        Iterator<Particle> swarmIterator = topology.iterator();

        while (swarmIterator.hasNext()) {
            Particle currentTarget = swarmIterator.next();
            if (currentTarget.getId() == particle.getId()) {
                break;
            }
        }

        for (int i = 0; i < particle.getDimension(); ++i) {
            double informationSum = 0.0;
            int numberOfNeighbours = 0;

            Iterator<Particle> neighborhoodIterator = topology.neighbourhood(swarmIterator);

            while (neighborhoodIterator.hasNext()) {
                Particle currentTarget = neighborhoodIterator.next();
                Vector currentTargetPosition = (Vector) currentTarget.getBestPosition();

                double randomComponent = (cognitiveAcceleration.getParameter() + socialAcceleration.getParameter())*r1.nextDouble();

                informationSum += randomComponent * (currentTargetPosition.doubleValueOf(i) - position.doubleValueOf(i));

                numberOfNeighbours++;
            }

            double value = inertiaWeight.getParameter() * (velocity.doubleValueOf(i) + (informationSum / numberOfNeighbours));

            velocity.setReal(i, value);
            clamp(velocity, i);
        }
    }
}
