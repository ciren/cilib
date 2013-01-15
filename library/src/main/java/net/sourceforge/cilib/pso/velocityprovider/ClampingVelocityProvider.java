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
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 */
public class ClampingVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = -5995116445841750100L;

    private ControlParameter vMax;
    private VelocityProvider delegate;

    public ClampingVelocityProvider() {
        this(ConstantControlParameter.of(Double.MAX_VALUE), new StandardVelocityProvider());
    }

    public ClampingVelocityProvider(ControlParameter vMax, VelocityProvider delegate) {
        this.vMax = vMax;
        this.delegate = delegate;
    }

    public ClampingVelocityProvider(ClampingVelocityProvider copy) {
        this.vMax = copy.vMax.getClone();
        this.delegate = copy.delegate.getClone();
    }

    @Override
    public ClampingVelocityProvider getClone() {
        return new ClampingVelocityProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        Vector velocity = this.delegate.get(particle);
        Vector.Builder builder = Vector.newBuilder();
        for (Numeric value : velocity) {
            builder.add(Math.min(Math.max(-vMax.getParameter(), value.doubleValue()), vMax.getParameter()));
        }
        return builder.build();
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
