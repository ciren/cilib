/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider.binary;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.velocityprovider.VelocityProvider;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Implementation of A quantum particle swarm optimisation
 *
 * Velocity of particle is modelled by a quantum bit (qubit)
 * <p>
 * References:
 * </p>
 * <ul><li>
 * Shuyuan Yang, Min Wang, Licheng Jiao.,
 * "A quantum particle swarm optimization" (2004).
 * IEEE Congress on evolutionary computation, vol 1, pp 320--324, 2004
 * </li></ul>
 */
public final class BinaryQuantumVelocityProvider implements VelocityProvider {

    protected ControlParameter alpha;
    protected ControlParameter beta;

    protected ControlParameter selfAcceleration;
    protected ControlParameter socialAcceleration;
    protected ControlParameter cognitiveAcceleration;

    public BinaryQuantumVelocityProvider() {
        this(ConstantControlParameter.of(0.3),
            ConstantControlParameter.of(0.7),
            ConstantControlParameter.of(0.2),
            ConstantControlParameter.of(0.2),
            ConstantControlParameter.of(0.6));
    }

    public BinaryQuantumVelocityProvider(
        ControlParameter alpha,
        ControlParameter beta,
        ControlParameter selfAcceleration,
        ControlParameter socialAcceleration,
        ControlParameter cognitiveAcceleration) {

        this.alpha = alpha;
        this.beta = beta;
        this.selfAcceleration = selfAcceleration;
        this.socialAcceleration = socialAcceleration;
        this.cognitiveAcceleration = cognitiveAcceleration;
    }

    public BinaryQuantumVelocityProvider(BinaryQuantumVelocityProvider copy) {
        this.alpha = copy.alpha.getClone();
        this.beta = copy.beta.getClone();
        this.selfAcceleration = copy.selfAcceleration.getClone();
        this.socialAcceleration = copy.socialAcceleration.getClone();
        this.cognitiveAcceleration = copy.cognitiveAcceleration.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BinaryQuantumVelocityProvider getClone() {
        return new BinaryQuantumVelocityProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        Vector velocity = (Vector) particle.getVelocity();
        Vector pbest = (Vector) particle.getLocalGuide();
        Vector gbest = (Vector) particle.getGlobalGuide();

        Vector.Builder groupBest = Vector.newBuilder();
        Vector.Builder selfBest = Vector.newBuilder();

        double alphaValue = alpha.getParameter();
        double betaValue = beta.getParameter();

        for(int k = 0; k < particle.getDimension(); k++) {
            double gbk = gbest.doubleValueOf(k);
            double pbk = pbest.doubleValueOf(k);

            double gk = (alphaValue * gbk)
                + (betaValue * (1 - gbk));

            double sk = (alphaValue * pbk)
                + (betaValue * (1 - pbk));

            groupBest.addWithin(gk, velocity.boundsOf(k));
            selfBest.addWithin(sk, velocity.boundsOf(k));
        }

        return velocity.multiply(selfAcceleration.getParameter())
            .plus(selfBest.build().multiply(cognitiveAcceleration.getParameter()))
            .plus(groupBest.build().multiply(socialAcceleration.getParameter()));
    }
    /**
     * Gets the <tt>ControlParameter</tt> representing the alpha
     * control parameter
     * <code>VelocityProvider</code>.
     * @return Returns alpha.
     */
    public ControlParameter getAlpha() {
        return alpha;
    }

    /**
     * Set the <tt>ControlParameter</tt> for alpha.
     * @param alpha
     */
    public void setAlpha(ControlParameter alpha) {
        this.alpha = alpha;
    }

    /**
     * Gets the <tt>ControlParameter</tt> representing the beta
     * control parameter
     * <code>VelocityProvider</code>.
     * @return Returns beta.
     */
    public ControlParameter getBeta() {
        return beta;
    }

    /**
     * Set the <tt>ControlParameter</tt> for beta.
     * @param beta the beta value to set.
     */
    public void setBeta(ControlParameter beta) {
        this.beta = beta;
    }

    /**
     * Gets the <tt>ControlParameter</tt> representing the acceleration
     * factor of the previous velocity
     * <code>VelocityProvider</code>.
     * @return Returns selfAcceleration.
     */
    public ControlParameter getSelfAcceleration() {
        return selfAcceleration;
    }

    /**
     * Set the <tt>ControlParameter</tt> for self acceleration.
     * @param selfAcceleration
     */
    public void setSelfAcceleration(ControlParameter selfAcceleration) {
        this.selfAcceleration = selfAcceleration;
    }

    /**
     * Gets the <tt>ControlParameter</tt> representing the acceleration
     * factor of the local best
     * <code>VelocityProvider</code>.
     * @return Returns cognitiveAcceleration.
     */
    public ControlParameter getCognitiveAcceleration() {
        return cognitiveAcceleration;
    }

    /**
     * Set the <tt>ControlParameter</tt> for cognitive acceleration
     * @param cognitiveAcceleration
     */
    public void setCognitiveAcceleration(ControlParameter cognitiveAcceleration) {
        this.cognitiveAcceleration = cognitiveAcceleration;
    }

    /**
     * Gets the <tt>ControlParameter</tt> representing the acceleration
     * factor of the global best
     * <code>VelocityProvider</code>.
     * @return Returns socialAcceleration.
     */
    public ControlParameter getSocialAcceleration() {
        return socialAcceleration;
    }

    /**
     * Set the <tt>ControlParameter</tt> for social acceleration
     * @param socialAcceleration
     */
    public void setSocialAcceleration(ControlParameter socialAcceleration) {
        this.socialAcceleration = socialAcceleration;
    }

}
