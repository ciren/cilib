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

import java.util.Random;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.generator.KnuthSubtractive;
import net.sourceforge.cilib.type.types.container.Vector;


/**
 * TODO: test this.
 *
 * @author engel
 */
public class LinearVelocityUpdate extends StandardVelocityUpdate {

    private static final long serialVersionUID = -1624326615681760823L;

    private Random socialRandomGenerator;
    private Random cognitiveRandomGenerator;

    /**
     * Create an instance of {@linkplain LinearVelocityUpdate}.
     */
    public LinearVelocityUpdate() {
        super();

        // Resetting the social and cognitive components is required to ensure
        // that during the velocity update process, only 1 random number is used.
        this.cognitiveAcceleration = new ConstantControlParameter();
        this.socialAcceleration = new ConstantControlParameter();

        this.cognitiveAcceleration.setParameter(1.496180);
        this.socialAcceleration.setParameter(1.496180);

        socialRandomGenerator = new KnuthSubtractive();
        cognitiveRandomGenerator = new KnuthSubtractive();
    }


    /**
     * {@inheritDoc}
     */
    public void updateVelocity(Particle particle) {
        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        Vector bestPosition = (Vector) particle.getBestPosition();
        Vector nBestPosition = (Vector) particle.getNeighbourhoodBest().getBestPosition();

        float social = socialRandomGenerator.nextFloat();
        float cognitive = cognitiveRandomGenerator.nextFloat();

        for (int i = 0; i < particle.getDimension(); ++i) {
            double tmp = inertiaWeight.getParameter()*velocity.getReal(i) +
                cognitive  * cognitiveAcceleration.getParameter() * (bestPosition.getReal(i) - position.getReal(i)) +
                social * socialAcceleration.getParameter() * (nBestPosition.getReal(i) - position.getReal(i));
            velocity.setReal(i, tmp);

            clamp(velocity, i);
        }
    }


    /**
     * Return the random number generator for the cognitive component.
     * @return Returns the random number generator for the cognitive component.
     */
    public Random getCongnitiveRandomGenerator() {
        return cognitiveRandomGenerator;
    }


    /**
     * @param congnitiveRandomGenerator The congnitiveRandomGenerator to set.
     */
    public void setCongnitiveRandomGenerator(Random congnitiveRandomGenerator) {
        this.cognitiveRandomGenerator = congnitiveRandomGenerator;
    }


    /**
     * @return Returns the socialRandomGenerator.
     */
    public Random getSocialRandomGenerator() {
        return socialRandomGenerator;
    }


    /**
     * @param socialRandomGenerator The socialRandomGenerator to set.
     */
    public void setSocialRandomGenerator(Random socialRandomGenerator) {
        this.socialRandomGenerator = socialRandomGenerator;
    }
}
