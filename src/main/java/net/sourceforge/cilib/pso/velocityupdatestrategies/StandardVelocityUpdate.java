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

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.RandomizingControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.type.types.container.Vector;


/**
 * Implementation of the standard / default velocity update equation.
 *
 * @author  Edwin Peer
 */
public class StandardVelocityUpdate implements VelocityUpdateStrategy {
    private static final long serialVersionUID = 8204479765311251730L;

    protected ControlParameter inertiaWeight;
    protected ControlParameter socialAcceleration;
    protected ControlParameter cognitiveAcceleration;
    protected ControlParameter vMax;

    /** Creates a new instance of StandardVelocityUpdate. */
    public StandardVelocityUpdate() {
        inertiaWeight = new ConstantControlParameter();
        cognitiveAcceleration = new RandomizingControlParameter();
        socialAcceleration = new RandomizingControlParameter();
        vMax = new ConstantControlParameter();

        inertiaWeight.setParameter(0.729844);
        cognitiveAcceleration.setParameter(1.496180);
        socialAcceleration.setParameter(1.496180);
        vMax.setParameter(Double.MAX_VALUE);
    }


    /**
     * Copy constructor.
     * @param copy The object to copy.
     */
    public StandardVelocityUpdate(StandardVelocityUpdate copy) {
        this.inertiaWeight = copy.inertiaWeight.getClone();
        this.cognitiveAcceleration = copy.cognitiveAcceleration.getClone();
        this.socialAcceleration = copy.socialAcceleration.getClone();
        this.vMax = copy.vMax.getClone();
    }


    /**
     * {@inheritDoc}
     */
    public StandardVelocityUpdate getClone() {
        return new StandardVelocityUpdate(this);
    }


    /**
     * Perform the velocity update for the given <tt>Particle</tt>.
     * @param particle The Particle velocity that should be updated.
     */
    public void updateVelocity(Particle particle) {
        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        Vector bestPosition = (Vector) particle.getBestPosition();
        Vector nBestPosition = (Vector) particle.getNeighbourhoodBest().getBestPosition();

        for (int i = 0; i < particle.getDimension(); ++i) {
            double value = inertiaWeight.getParameter()*velocity.getReal(i) +
                (bestPosition.getReal(i) - position.getReal(i)) * cognitiveAcceleration.getParameter() +
                (nBestPosition.getReal(i) - position.getReal(i)) * socialAcceleration.getParameter();
            velocity.setReal(i, value);

            clamp(velocity, i);
        }
    }


    /**
     * Update the associated <tt>ControlParameter</tt>s for the <tt>VelocityUpdateStrategy</tt>.
     * {@inheritDoc}
     */
    public void updateControlParameters(Particle particle) {
        this.inertiaWeight.updateParameter();
        this.cognitiveAcceleration.updateParameter();
        this.socialAcceleration.updateParameter();
        this.vMax.updateParameter();
    }


    /**
     * TODO: Need to have a VMax strategy.
     * @param velocity The {@link Vector} to be clamped.
     * @param i The dimension index to be clamped
     */
    protected void clamp(Vector velocity, int i) {
        if (velocity.getReal(i) < -vMax.getParameter())
            velocity.setReal(i, -vMax.getParameter());
        else if (velocity.getReal(i) > vMax.getParameter())
            velocity.setReal(i, vMax.getParameter());
    }


    /**
     * Gets the <tt>ControlParameter</tt> representing the cognitive component within this
     * <code>VelocityUpdateStrategy</code>.
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
     * Get the <code>ControlParameter</code> representing the inerti weight of the VelocityUpdateStrategy.
     * @return Returns the inertia component <tt>ControlParameter</tt>.
     */
    public ControlParameter getInertiaWeight() {
        return inertiaWeight;
    }

    /**
     * Set the <tt>ControlParameter</tt> for the inertia weight of the velocity update equation.
     * @param inertiaComponent The inertiaComponent to set.
     */
    public void setInertiaWeight(ControlParameter inertiaWeight) {
        this.inertiaWeight = inertiaWeight;
    }

    /**
     * Get the <tt>ControlParameter</tt> representing the social component of the velocity update equation.
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

    /**
     * Get the <code>ControlParameter</code> representing the <tt>vMax</tt> component.
     * @return The <code>ControlParameter</code> for the vMax.
     */
    public ControlParameter getVMax() {
        return vMax;
    }

    /**
     * Set the <tt>ControlParameter</tt> for the vMax parameter.
     * @param max The <tt>ControlParameter</tt> to set.
     */
    public void setVMax(ControlParameter max) {
        vMax = max;
    }

}
