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

import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.RandomizingControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *  The <tt>VelocityUpdateStrategy</tt> strategy for the Bare Bones PSO as
 *  defined by Kennedy.
 *
 *  TODO: get the required references
 *
 *  @author Gary Pampara
 *  @author Andries Engelbrecht
 */
public class BareBonesVelocityUpdateStrategy implements VelocityUpdateStrategy {

    private static final long serialVersionUID = -823686042197742768L;
    private ProbabilityDistributionFuction randomNumber;
    private ControlParameter cognitiveAcceleration;
    private ControlParameter socialAcceleration;

    public BareBonesVelocityUpdateStrategy() {
        randomNumber = new GaussianDistribution();

        cognitiveAcceleration = new RandomizingControlParameter();
        socialAcceleration = new RandomizingControlParameter();

        cognitiveAcceleration.setParameter(1.496180);
        socialAcceleration.setParameter(1.496180);
    }

    public BareBonesVelocityUpdateStrategy(BareBonesVelocityUpdateStrategy copy) {
        this();

        cognitiveAcceleration.setParameter(copy.cognitiveAcceleration.getParameter());
        socialAcceleration.setParameter(copy.socialAcceleration.getParameter());
    }

    public BareBonesVelocityUpdateStrategy getClone() {
        return new BareBonesVelocityUpdateStrategy(this);
    }

    public void updateVelocity(Particle particle) {
        Vector personalBestPosition = (Vector) particle.getBestPosition();
        Vector nBestPosition = (Vector) particle.getNeighbourhoodBest().getBestPosition();
        Vector velocity = (Vector) particle.getVelocity();

        for (int i = 0; i < particle.getDimension(); ++i) {
            //double tmp1 = cognitive.getParameter();
            //double tmp2 = social.getParameter();

            double sigma = Math.abs(personalBestPosition.doubleValueOf(i) - nBestPosition.doubleValueOf(i));
            //according to Kennedy
            double mean = (personalBestPosition.doubleValueOf(i) + nBestPosition.doubleValueOf(i)) / 2;
            //andries proposal: double mean = (tmp1*personalBestPosition.getReal(i) + tmp2*nBestPosition.getReal(i)) / (tmp1+tmp2);

            velocity.setReal(i, randomNumber.getRandomNumber(mean, sigma));
        }
    }

    public void updateControlParameters(Particle particle) {
        // TODO Auto-generated method stub
    }

    public ControlParameter getCognitiveAcceleration() {
        return cognitiveAcceleration;
    }

    public void setCognitiveAcceleration(ControlParameter cognitiveAcceleration) {
        this.cognitiveAcceleration = cognitiveAcceleration;
    }

    public ControlParameter getSocialAcceleration() {
        return socialAcceleration;
    }

    public void setSocialAcceleration(ControlParameter socialAcceleration) {
        this.socialAcceleration = socialAcceleration;
    }
}
