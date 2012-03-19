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
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

/**
 * This is the normal position update as described by Kennedy and Eberhart.
 * @reference paper
 *
 *
 */
public class StandardPositionProvider implements PositionProvider {

    private static final long serialVersionUID = 5547754413670196513L;

    /**
     * Create an new instance of {@code StandardPositionProvider}.
     */
    public StandardPositionProvider() {
    }

    /**
     * Copy constructor. Copy the provided instance.
     * @param copy The instance to copy.
     */
    public StandardPositionProvider(StandardPositionProvider copy) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StandardPositionProvider getClone() {
        return new StandardPositionProvider(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector get(Particle particle) {
        Vector position = (Vector) particle.getPosition();
        Vector velocity = (Vector) particle.getVelocity();
        return Vectors.sumOf(position, velocity);
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public double getInertia(ParameterizedParticle particle) {
        double position = particle.getInertia().getParameter();
        double velocity = particle.getInertia().getVelocity();
        double value = position + velocity;
        
        //if(isWithinBounds(value, particle.getInertia()))
            return value;
        //return position;
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public double getSocialAcceleration(ParameterizedParticle particle) {
        double position = particle.getSocialAcceleration().getParameter();
        double velocity = particle.getSocialAcceleration().getVelocity();
        double value = position + velocity;
        //if(isWithinBounds(value, particle.getSocialAcceleration()))
            return value;
        //return position;
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public double getCognitiveAcceleration(ParameterizedParticle particle) {
        double position = particle.getCognitiveAcceleration().getParameter();
        double velocity = particle.getCognitiveAcceleration().getVelocity();
        double value = position + velocity;
        //if(isWithinBounds(value, particle.getCognitiveAcceleration()))
            return value;
        //return position;
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public double getVmax(ParameterizedParticle particle) {
        double position = particle.getVmax().getParameter();
        double velocity = particle.getVmax().getVelocity();
        double value = position + velocity;
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
