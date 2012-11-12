/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *  The <tt>VelocityProvider</tt> for the Bare Bones PSO as defined by Kennedy.
 *
 *  TODO: get the required references
 *
 */
public class BareBonesVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = -823686042197742768L;
    
    protected ProbabilityDistributionFunction randomDistribution;

    public BareBonesVelocityProvider() {
        this.randomDistribution = new GaussianDistribution();
    }

    public BareBonesVelocityProvider(BareBonesVelocityProvider copy) {
        this.randomDistribution = copy.randomDistribution;
    }

    @Override
    public BareBonesVelocityProvider getClone() {
        return new BareBonesVelocityProvider(this);
    }

    @Override
    public Vector get(Particle particle) {
        Vector localGuide = (Vector) particle.getLocalGuide();
        Vector globalGuide = (Vector) particle.getGlobalGuide();

        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); ++i) {
            //double tmp1 = cognitive.getParameter();
            //double tmp2 = social.getParameter();

            double sigma = Math.abs(localGuide.doubleValueOf(i) - globalGuide.doubleValueOf(i));
            //according to Kennedy
            double mean = (localGuide.doubleValueOf(i) + globalGuide.doubleValueOf(i)) / 2;
            //andries proposal: double mean = (tmp1*personalBestPosition.getReal(i) + tmp2*nBestPosition.getReal(i)) / (tmp1+tmp2);

            builder.add(this.randomDistribution.getRandomNumber(mean, sigma));
        }
        return builder.build();
    }

    public ProbabilityDistributionFunction getRandomDistribution() {
        return this.randomDistribution;
    }

    public void setRandomDistribution(ProbabilityDistributionFunction pdf) {
        this.randomDistribution = pdf;
    }
}
