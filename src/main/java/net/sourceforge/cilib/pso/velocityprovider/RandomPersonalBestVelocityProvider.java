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
package net.sourceforge.cilib.pso.velocityprovider;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.ControlParameters;
import net.sourceforge.cilib.util.RandomProviders;
import net.sourceforge.cilib.util.Vectors;

/**
 * Implementation of the standard / default velocity update equation.
 *
 * @author  Edwin Peer
 */
public final class RandomPersonalBestVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = 8204479765311251730L;
    
    private RandomProvider random;
    protected ControlParameter inertiaWeight;
    protected ControlParameter socialAcceleration;
    protected RandomProvider r1;

    /** Creates a new instance of StandardVelocityUpdate. */
    public RandomPersonalBestVelocityProvider() {
        this.random = new MersenneTwister();
        this.inertiaWeight = new ConstantControlParameter(0.729844);
        this.socialAcceleration = new ConstantControlParameter(2.5);
        this.r1 = new MersenneTwister();
    }

    /**
     * Copy constructor.
     * @param copy The object to copy.
     */
    public RandomPersonalBestVelocityProvider(RandomPersonalBestVelocityProvider copy) {
        this.random = copy.random;
        this.inertiaWeight = copy.inertiaWeight.getClone();
        this.socialAcceleration = copy.socialAcceleration.getClone();
        this.r1 = copy.r1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RandomPersonalBestVelocityProvider getClone() {
        return new RandomPersonalBestVelocityProvider(this);
    }

    /**
     * Perform the velocity update for the given <tt>Particle</tt>.
     * @param particle The Particle velocity that should be updated.
     */
    @Override
    public Vector get(Particle particle) {
        Topology<Particle> topology = ((PSO) AbstractAlgorithm.get()).getTopology();
        Particle other = topology.get(random.nextInt(topology.size()));
        Particle toUpdate;
        
        if(particle.getBestFitness().compareTo(other.getBestFitness()) > 0) {
            toUpdate = other;
            other = particle;
        } else {
            toUpdate = particle;
        }
        
        Vector velocity = (Vector) toUpdate.getVelocity();
        Vector position = (Vector) toUpdate.getPosition();
        Vector globalGuide = (Vector) other.getBestPosition();

        Vector dampenedVelocity = Vector.copyOf(velocity).multiply(ControlParameters.supplierOf(this.inertiaWeight));
        Vector socialComponent = Vector.copyOf(globalGuide).subtract(position).multiply(ControlParameters.supplierOf(this.socialAcceleration)).multiply(RandomProviders.supplierOf(this.r1));
        return Vectors.sumOf(dampenedVelocity, socialComponent);
    }

    public ControlParameter getInertiaWeight() {
        return inertiaWeight;
    }

    public RandomProvider getR1() {
        return r1;
    }

    public void setInertiaWeight(ControlParameter inertiaWeight) {
        this.inertiaWeight = inertiaWeight;
    }

    public void setR1(RandomProvider r1) {
        this.r1 = r1;
    }

    public void setRandom(RandomProvider random) {
        this.random = random;
    }

    public void setSocialAcceleration(ControlParameter socialAcceleration) {
        this.socialAcceleration = socialAcceleration;
    }

    public ControlParameter getSocialAcceleration() {
        return socialAcceleration;
    }

    public RandomProvider getRandom() {
        return random;
    }
    
    @Override
    public void updateControlParameters(Particle particle) {
    }
}
