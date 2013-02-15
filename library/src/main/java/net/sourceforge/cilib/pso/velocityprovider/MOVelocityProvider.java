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

import fj.P1;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Implementation of the standard / default velocity update equation.
 *
 * @author  Edwin Peer
 */
public final class MOVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = 8204479765311251730L;

    protected ControlParameter inertiaWeight;
    protected ControlParameter socialAcceleration;
    protected ControlParameter cognitiveAcceleration;
    
    /** Creates a new instance of StandardVelocityUpdate. */
    public MOVelocityProvider() {
        this(ConstantControlParameter.of(0.729844),
            ConstantControlParameter.of(1.496180),
            ConstantControlParameter.of(1.496180));        
    }

    public MOVelocityProvider(ControlParameter inertia, ControlParameter social, ControlParameter cog) {
        this.inertiaWeight = inertia;
        this.socialAcceleration = social;
        this.cognitiveAcceleration = cog;
    }
    
    /**
     * Copy constructor.
     * @param copy The object to copy.
     */
    public MOVelocityProvider(MOVelocityProvider copy) {
        this.inertiaWeight = copy.inertiaWeight.getClone();
        this.cognitiveAcceleration = copy.cognitiveAcceleration.getClone();
        this.socialAcceleration = copy.socialAcceleration.getClone();        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MOVelocityProvider getClone() {
        return new MOVelocityProvider(this);
    }

    /**
     * Perform the velocity update for the given <tt>Particle</tt>.
     * @param particle The Particle velocity that should be updated.
     */
    @Override
    public Vector get(Particle particle) {

        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        Vector gbest = (Vector) particle.getNeighbourhoodBest().getCandidateSolution();
        Vector localGuide = (Vector) particle.getLocalGuide();
        Vector globalGuide = (Vector) particle.getGlobalGuide();

        int min = Math.min(localGuide.size(), globalGuide.size());
        int i = 0;

        for (; i < min; ++i) {
            double value = this.inertiaWeight.getParameter() * velocity.doubleValueOf(i) +
                    (localGuide.doubleValueOf(i) - position.doubleValueOf(i)) * this.cognitiveAcceleration.getParameter()* Rand.nextDouble() +
                    (globalGuide.doubleValueOf(i) - position.doubleValueOf(i)) * this.socialAcceleration.getParameter()* Rand.nextDouble();
            velocity.setReal(i, value);            
        }

        for (; i < particle.getDimension(); ++i) {
            double value = this.inertiaWeight.getParameter() * velocity.doubleValueOf(i) +
                    (localGuide.doubleValueOf(i) - position.doubleValueOf(i)) * this.cognitiveAcceleration.getParameter() * Rand.nextDouble() +
                    (gbest.doubleValueOf(i) - position.doubleValueOf(i)) * this.socialAcceleration.getParameter() * Rand.nextDouble();
            velocity.setReal(i, value);            
        }
        return velocity;
    }

        
    /**
     * Get the <code>ControlParameter</code> representing the inertia weight of
     * the VelocityProvider.
     * @return Returns the inertia component <tt>ControlParameter</tt>.
     */
    public ControlParameter getInertiaWeight() {
        return inertiaWeight;
    }

    /**
     * Set the <tt>ControlParameter</tt> for the inertia weight of the velocity
     * update equation.
     * @param inertiaComponent The inertiaComponent to set.
     */
    public void setInertiaWeight(ControlParameter inertiaWeight) {
        this.inertiaWeight = inertiaWeight;
    }

    /**
     * Gets the <tt>ControlParameter</tt> representing the cognitive component within this
     * <code>VelocityProvider</code>.
     * @return Returns the cognitiveComponent.
     */
    public ControlParameter getCognitiveAcceleration() {
        return cognitiveAcceleration;
    }

    /**
     * Set the cognitive component <code>ControlParameter</code>.
     * @param cognitiveComponent The cognitiveComponent to set.
     */
    public void setCognitiveAcceleration(ControlParameter cognitiveComponent) {
        this.cognitiveAcceleration = cognitiveComponent;
    }

    /**
     * Get the <tt>ControlParameter</tt> representing the social component of
     * the velocity update equation.
     * @return Returns the socialComponent.
     */
    public ControlParameter getSocialAcceleration() {
        return socialAcceleration;
    }

    /**
     * Set the <tt>ControlParameter</tt> for the social component.
     * @param socialComponent The socialComponent to set.
     */
    public void setSocialAcceleration(ControlParameter socialComponent) {
        this.socialAcceleration = socialComponent;
    }
}
