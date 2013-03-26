/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider;

import fj.P1;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Implementation of the standard / default velocity update equation.
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
     * Get the {@linkplain ControlParameter} representing the inertia weight of
     * the VelocityProvider.
     * @return Returns the inertia component <tt>ControlParameter</tt>.
     */
    public ControlParameter getInertiaWeight() {
        return inertiaWeight;
    }

    /**
     * Set the {@linkplain ControlParameter} for the inertia weight of the velocity
     * update equation.
     * @param inertiaWeight The inertiaComponent to set.
     */
    public void setInertiaWeight(ControlParameter inertiaWeight) {
        this.inertiaWeight = inertiaWeight;
    }

    /**
     * Gets the {@linkplain ControlParameter} representing the cognitive
     * component within this {@linkplain VelocityProvider}.
     * @return Returns the cognitiveComponent.
     */
    public ControlParameter getCognitiveAcceleration() {
        return cognitiveAcceleration;
    }

    /**
     * Set the cognitive component {@linkplain ControlParameter}.
     * @param cognitiveComponent The cognitiveComponent to set.
     */
    public void setCognitiveAcceleration(ControlParameter cognitiveComponent) {
        this.cognitiveAcceleration = cognitiveComponent;
    }

    /**
     * Get the {@linkplain ControlParameter} representing the social component
     * of the velocity update equation.
     * @return Returns the socialComponent.
     */
    public ControlParameter getSocialAcceleration() {
        return socialAcceleration;
    }

    /**
     * Set the {@linkplain ControlParameter} for the social component.
     * @param socialComponent The socialComponent to set.
     */
    public void setSocialAcceleration(ControlParameter socialComponent) {
        this.socialAcceleration = socialComponent;
    }
}
