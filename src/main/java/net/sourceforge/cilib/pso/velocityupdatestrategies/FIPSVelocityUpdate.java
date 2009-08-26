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
package net.sourceforge.cilib.pso.velocityupdatestrategies;

import java.util.Iterator;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.RandomizingControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author Olusegun Olorunda
 */
public class FIPSVelocityUpdate extends StandardVelocityUpdate {
    private static final long serialVersionUID = 6391914534943249737L;

    private ControlParameter randomComponent;

    public FIPSVelocityUpdate() {
        super();
        cognitiveAcceleration = new ConstantControlParameter(1.496180);
        socialAcceleration = new ConstantControlParameter(1.496180);
        randomComponent = new RandomizingControlParameter();
    }

    public FIPSVelocityUpdate(FIPSVelocityUpdate copy) {
        super(copy);
        this.randomComponent = copy.randomComponent.getClone();
    }

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
                Particle currentTarget = (Particle) neighborhoodIterator.next();
                Vector currentTargetPosition = (Vector) currentTarget.getBestPosition();

                randomComponent.setParameter(cognitiveAcceleration.getParameter() + socialAcceleration.getParameter());

                informationSum += randomComponent.getParameter() * (currentTargetPosition.getReal(i) - position.getReal(i));

                numberOfNeighbours++;
            }

            double value = inertiaWeight.getParameter() * (velocity.getReal(i) + (informationSum / numberOfNeighbours));

            velocity.setReal(i, value);
            // clamp(velocity, i);
        }
    }

    /**
     * @return the randomComponent
     */
    public ControlParameter getRandomComponent() {
        return randomComponent;
    }

    /**
     * @param randomComponent the randomComponent to set
     */
    public void setRandomComponent(ControlParameter randomComponent) {
        this.randomComponent = randomComponent;
    }

    @Override
    public void updateControlParameters(Particle particle) {
        super.updateControlParameters(particle);
        randomComponent.updateParameter();
    }
}
