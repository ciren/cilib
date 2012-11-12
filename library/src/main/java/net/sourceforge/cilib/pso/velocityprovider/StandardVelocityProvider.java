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
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

/**
 * Implementation of the standard / default velocity update equation.
 *
 */
public final class StandardVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = 8204479765311251730L;
    
    protected ControlParameter inertiaWeight;
    protected ControlParameter socialAcceleration;
    protected ControlParameter cognitiveAcceleration;
    protected RandomProvider r1;
    protected RandomProvider r2;

    /** Creates a new instance of StandardVelocityUpdate. */
    public StandardVelocityProvider() {
        this(ConstantControlParameter.of(0.729844),
            ConstantControlParameter.of(1.496180),
            ConstantControlParameter.of(1.496180),
            new MersenneTwister(),
            new MersenneTwister());
    }

    public StandardVelocityProvider(ControlParameter inertia, ControlParameter social, ControlParameter cog,
            RandomProvider r1, RandomProvider r2) {
        this.inertiaWeight = inertia;
        this.socialAcceleration = social;
        this.cognitiveAcceleration = cog;
        this.r1 = r1;
        this.r2 = r2;
    }

    /**
     * Copy constructor.
     * @param copy The object to copy.
     */
    public StandardVelocityProvider(StandardVelocityProvider copy) {
        this.inertiaWeight = copy.inertiaWeight.getClone();
        this.cognitiveAcceleration = copy.cognitiveAcceleration.getClone();
        this.socialAcceleration = copy.socialAcceleration.getClone();
        this.r1 = copy.r1;
        this.r2 = copy.r2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StandardVelocityProvider getClone() {
        return new StandardVelocityProvider(this);
    }
    
    private static P1<Number> random(final RandomProvider r) {
        return new P1<Number>() {
            @Override
            public Number _1() {
                return r.nextDouble();
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
     * Perform the velocity update for the given <tt>Particle</tt>.
     * @param particle The Particle velocity that should be updated.
     */
    @Override
    public Vector get(Particle particle) {
        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        Vector localGuide = (Vector) particle.getLocalGuide();
        Vector globalGuide = (Vector) particle.getGlobalGuide();

        Vector dampenedVelocity = Vector.copyOf(velocity).multiply(inertiaWeight.getParameter());
        Vector cognitiveComponent = Vector.copyOf(localGuide).subtract(position).multiply(cp(cognitiveAcceleration)).multiply(random(r1));        
        Vector socialComponent = Vector.copyOf(globalGuide).subtract(position).multiply(cp(socialAcceleration)).multiply(random(r2));
        return Vectors.sumOf(dampenedVelocity, cognitiveComponent, socialComponent);
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

    public RandomProvider getR1() {
        return r1;
    }

    public void setR1(RandomProvider r1) {
        this.r1 = r1;
    }

    public RandomProvider getR2() {
        return r2;
    }

    public void setR2(RandomProvider r2) {
        this.r2 = r2;
    }
}
