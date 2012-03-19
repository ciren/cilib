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
package net.sourceforge.cilib.pso.positionprovider;

import net.sourceforge.cilib.controlparameter.BoundedModifiableControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.util.Vectors;

/**
 * Decorates a {@link PositionUpdateVisitor} or a {@link VelocityUpdateVisitor}
 * with random noise from any probability distribution function.
 *
 */
public class NoisyPositionProvider implements PositionProvider {

    private static final long serialVersionUID = -2665293187543545962L;
    private ProbabilityDistributionFuction distribution;
    private PositionProvider delegate;

    public NoisyPositionProvider() {
        this.distribution = new GaussianDistribution();
        this.delegate = new StandardPositionProvider();
    }

    public NoisyPositionProvider(NoisyPositionProvider rhs) {
        this.distribution = rhs.distribution;
        this.delegate = rhs.delegate.getClone();
    }

    /*
     * {@inheritDoc}
     */
    @Override
    public Vector get(Particle particle) {
        Vector position = this.delegate.get(particle);
        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); i++) {
            builder.add(this.distribution.getRandomNumber());
        }
        return Vectors.sumOf(position, builder.build());
    }

    /*
     * {@inheritDoc}
     */
    @Override
    public NoisyPositionProvider getClone() {
        return new NoisyPositionProvider(this);
    }

    public PositionProvider getDelegate() {
        return this.delegate;
    }

    public void setDelegate(PositionProvider delegate) {
        this.delegate = delegate;
    }

    public ProbabilityDistributionFuction getDistribution() {
        return this.distribution;
    }

    public void setDistribution(ProbabilityDistributionFuction distribution) {
        this.distribution = distribution;
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public double getInertia(ParameterizedParticle particle) {
        double position = this.delegate.getInertia(particle);
        double randomValue = this.distribution.getRandomNumber();
        double value = position + randomValue;
        //if(isWithinBounds(value, particle.getInertia()))
            return value;
       // return position;
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public double getSocialAcceleration(ParameterizedParticle particle) {
        double position = this.delegate.getSocialAcceleration(particle);
        double randomValue = this.distribution.getRandomNumber();
        double value = position + randomValue;
        //if(isWithinBounds(value, particle.getSocialAcceleration()))
            return value;
        //return position;
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public double getCognitiveAcceleration(ParameterizedParticle particle) {
        double position = this.delegate.getCognitiveAcceleration(particle);
        double randomValue = this.distribution.getRandomNumber();
        double value = position + randomValue;
        //if(isWithinBounds(value, particle.getCognitiveAcceleration()))
            return value;
        //return position;
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public double getVmax(ParameterizedParticle particle) {
        double position = this.delegate.getVmax(particle);
        double randomValue = this.distribution.getRandomNumber();
        double value = position + randomValue;
        //if(isWithinBounds(value, particle.getVmax()))
            return value;
        //return position;
    }
    
    public boolean isWithinBounds(double value, ControlParameter parameter) {
        if(parameter instanceof BoundedModifiableControlParameter) {
            BoundedModifiableControlParameter newParameter = (BoundedModifiableControlParameter) parameter;
            
            if((value > newParameter.getLowerBound()) && (value < newParameter.getUpperBound())) {
                return true;
            }
        } else {
            return true;
        }
        
        return false;
    }
}
