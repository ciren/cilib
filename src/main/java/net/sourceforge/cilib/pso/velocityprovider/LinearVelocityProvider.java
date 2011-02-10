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


import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.generator.KnuthSubtractive;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.type.types.container.Vector;


/**
 * TODO: test this.
 *
 * @author engel
 */
public class LinearVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = -1624326615681760823L;

    protected ControlParameter inertiaWeight;
    protected ControlParameter socialAcceleration;
    protected ControlParameter cognitiveAcceleration;

    private RandomProvider socialRandomGenerator;
    private RandomProvider cognitiveRandomGenerator;

    /**
     * Create an instance of {@linkplain LinearVelocityProvider}.
     */
    public LinearVelocityProvider() {
        // Resetting the social and cognitive components is required to ensure
        // that during the velocity update process, only 1 random number is used.
        this.inertiaWeight = new ConstantControlParameter(0.729844);
        this.socialAcceleration = new ConstantControlParameter(1.496180);
        this.cognitiveAcceleration = new ConstantControlParameter(1.496180);

        this.socialRandomGenerator = new KnuthSubtractive();
        this.cognitiveRandomGenerator = new KnuthSubtractive();
    }

    public LinearVelocityProvider(LinearVelocityProvider copy) {
        this.inertiaWeight = copy.inertiaWeight.getClone();
        this.socialAcceleration = copy.socialAcceleration.getClone();
        this.cognitiveAcceleration = copy.cognitiveAcceleration.getClone();

        this.socialRandomGenerator = copy.socialRandomGenerator;
        this.cognitiveRandomGenerator = copy.cognitiveRandomGenerator;
    }

    @Override
    public VelocityProvider getClone() {
        return new LinearVelocityProvider(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector get(Particle particle) {
        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        Vector localGuide = (Vector) particle.getLocalGuide();
        Vector globalGuide = (Vector) particle.getGlobalGuide();

        float social = this.socialRandomGenerator.nextFloat();
        float cognitive = this.cognitiveRandomGenerator.nextFloat();

        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); ++i) {
            double value = this.inertiaWeight.getParameter()*velocity.doubleValueOf(i) +
                cognitive  * this.cognitiveAcceleration.getParameter() * (localGuide.doubleValueOf(i) - position.doubleValueOf(i)) +
                social * this.socialAcceleration.getParameter() * (globalGuide.doubleValueOf(i) - position.doubleValueOf(i));
            builder.add(value);
        }
        return builder.build();
    }

    /**
     * Return the random number generator for the cognitive component.
     * @return Returns the random number generator for the cognitive component.
     */
    public RandomProvider getCongnitiveRandomGenerator() {
        return this.cognitiveRandomGenerator;
    }

    /**
     * @param congnitiveRandomGenerator The congnitiveRandomGenerator to set.
     */
    public void setCongnitiveRandomGenerator(RandomProvider congnitiveRandomGenerator) {
        this.cognitiveRandomGenerator = congnitiveRandomGenerator;
    }

    /**
     * @return Returns the socialRandomGenerator.
     */
    public RandomProvider getSocialRandomGenerator() {
        return this.socialRandomGenerator;
    }

    /**
     * @param socialRandomGenerator The socialRandomGenerator to set.
     */
    public void setSocialRandomGenerator(RandomProvider socialRandomGenerator) {
        this.socialRandomGenerator = socialRandomGenerator;
    }

    @Override
    public void updateControlParameters(Particle particle) {
        this.inertiaWeight.updateParameter();
        this.socialAcceleration.updateParameter();
        this.cognitiveAcceleration.updateParameter();
    }
}
