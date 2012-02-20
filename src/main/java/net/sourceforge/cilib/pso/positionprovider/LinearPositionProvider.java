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

import net.sourceforge.cilib.controlparameter.BoundedControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.particle.ParametizedParticle;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This sets the position to the velocity.
 *
 *
 */
public class LinearPositionProvider implements PositionProvider {

    private static final long serialVersionUID = -3328130784035172372L;

    public LinearPositionProvider() {
    }

    public LinearPositionProvider(LinearPositionProvider copy) {
    }

    @Override
    public LinearPositionProvider getClone() {
        return new LinearPositionProvider(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector get(Particle particle) {
        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getCandidateSolution();
        Vector.Builder builder = Vector.newBuilder();
        
        for(int n = 0; n < position.size(); n++)
            builder.addWithin(velocity.doubleValueOf(n), position.boundsOf(n));
        
        return builder.build();
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public double getInertia(ParametizedParticle particle) {
        double velocity =  particle.getInertia().getVelocity();
        BoundedControlParameter position =  (BoundedControlParameter) particle.getInertia();
        Bounds bounds = new Bounds(position.getLowerBound(), position.getUpperBound());
        return Real.valueOf(velocity, bounds).doubleValue();
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public double getSocialAcceleration(ParametizedParticle particle) {
        double velocity =  particle.getSocialAcceleration().getVelocity();
        BoundedControlParameter position =  (BoundedControlParameter) particle.getSocialAcceleration();
        Bounds bounds = new Bounds(position.getLowerBound(), position.getUpperBound());
        return Real.valueOf(velocity, bounds).doubleValue();
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public double getCognitiveAcceleration(ParametizedParticle particle) {
        double velocity =  particle.getCognitiveAcceleration().getVelocity();
        BoundedControlParameter position =  (BoundedControlParameter) particle.getCognitiveAcceleration();
        Bounds bounds = new Bounds(position.getLowerBound(), position.getUpperBound());
        return Real.valueOf(velocity, bounds).doubleValue();
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public double getVmax(ParametizedParticle particle) {
        double velocity =  particle.getVmax().getVelocity();
        BoundedControlParameter position =  (BoundedControlParameter) particle.getVmax();
        Bounds bounds = new Bounds(position.getLowerBound(), position.getUpperBound());
        return Real.valueOf(velocity, bounds).doubleValue();
    }
}
