/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * The velocity vector is normalised whenever the current velocity vector norm exceeds a user-defined vMax.
 * As opposed to the standard velocity clamping, the direction of the velocity vector is maintained.
 */
public class NormalisedClampingVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = -5995116445841750100L;

    private ControlParameter vMax;
    private VelocityProvider delegate;

    public NormalisedClampingVelocityProvider() {
        this(ConstantControlParameter.of(Double.MAX_VALUE), new StandardVelocityProvider());
    }

    public NormalisedClampingVelocityProvider(ControlParameter vMax, VelocityProvider delegate) {
        this.vMax = vMax;
        this.delegate = delegate;
    }

    public NormalisedClampingVelocityProvider(NormalisedClampingVelocityProvider copy) {
        this.vMax = copy.vMax.getClone();
        this.delegate = copy.delegate.getClone();
    }

    @Override
    public NormalisedClampingVelocityProvider getClone() {
        return new NormalisedClampingVelocityProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        Vector velocity = (Vector) this.delegate.get(particle);
        if(velocity.norm() > vMax.getParameter()) return velocity.normalize();
        return velocity;
    }

    public void setVMax(ControlParameter vMax) {
        this.vMax = vMax;
    }

    public ControlParameter getVMax() {
        return this.vMax;
    }

    public void setDelegate(VelocityProvider delegate) {
        this.delegate = delegate;
    }

    public VelocityProvider getDelegate() {
        return this.delegate;
    }
}
