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
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

/**
 * Implementation of the standard / default velocity update equation.
 */
public final class StandardVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = 8204479765311251730L;

    protected ControlParameter inertiaWeight;
    protected ControlParameter socialAcceleration;
    protected ControlParameter cognitiveAcceleration;

    /** Creates a new instance of StandardVelocityUpdate. */
    public StandardVelocityProvider() {
        this(ConstantControlParameter.of(0.729844),
            ConstantControlParameter.of(1.496180),
            ConstantControlParameter.of(1.496180));
    }

    public StandardVelocityProvider(ControlParameter inertia, ControlParameter social, ControlParameter cog) {
        this.inertiaWeight = inertia;
        this.socialAcceleration = social;
        this.cognitiveAcceleration = cog;
    }

    /**
     * Copy constructor.
     * @param copy The object to copy.
     */
    public StandardVelocityProvider(StandardVelocityProvider copy) {
        this.inertiaWeight = copy.inertiaWeight.getClone();
        this.cognitiveAcceleration = copy.cognitiveAcceleration.getClone();
        this.socialAcceleration = copy.socialAcceleration.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StandardVelocityProvider getClone() {
        return new StandardVelocityProvider(this);
    }

    private static P1<Number> random() {
        return new P1<Number>() {
            @Override
            public Number _1() {
                return Rand.nextDouble();
            }
        };
    }

    private static P1<Number> cp(final ControlParameter r) {
        return new P1<Number>() {
            @Override
            public Number _1() {
                return r.getParameter();
            }
        };
    }

    /**
     * Perform the velocity update for the given {@linkplain Particle}.
     * @param particle The {@linkplain Particle} velocity that should be updated.
     */
    @Override
    public Vector get(Particle particle) {
        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        Vector localGuide = (Vector) particle.getLocalGuide();
        Vector globalGuide = (Vector) particle.getGlobalGuide();

        Vector dampenedVelocity = Vector.copyOf(velocity).multiply(inertiaWeight.getParameter());
        Vector cognitiveComponent = Vector.copyOf(localGuide).subtract(position).multiply(cp(cognitiveAcceleration)).multiply(random());
        Vector socialComponent = Vector.copyOf(globalGuide).subtract(position).multiply(cp(socialAcceleration)).multiply(random());
        return Vectors.sumOf(dampenedVelocity, cognitiveComponent, socialComponent);
    }

    /**
     * Get the {@linkplain ControlParameter} representing the inertia weight of
     * the {@linkplain VelocityProvider}.
     * @return the inertia component {@linkplain ControlParameter}.
     */
    public ControlParameter getInertiaWeight() {
        return inertiaWeight;
    }

    /**
     * Set the {@linkplain ControlParameter} for the inertia weight of the
     * velocity update equation.
     * @param inertiaWeight The inertiaWeight to set.
     */
    public void setInertiaWeight(ControlParameter inertiaWeight) {
        this.inertiaWeight = inertiaWeight;
    }

    /**
     * Gets the {@linkplain ControlParameter} representing the cognitive
     * component within this {@link VelocityProvider}.
     * @return the cognitiveComponent.
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
     * @return the socialComponent.
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
