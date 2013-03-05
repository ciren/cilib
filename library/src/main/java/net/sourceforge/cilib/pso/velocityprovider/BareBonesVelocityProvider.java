/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.math.random.GaussianDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * The <tt>VelocityProvider</tt> for the Bare Bones PSO as adapted by Kennedy.
 * J. Kennedy, “Bare bones particle swarms”, in Proceedings of the IEEE Swarm Intelligence Symposium, pp. 80-87, 2003.
 */
public class BareBonesVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = -5028807853700576434L;

    private ProbabilityDistributionFunction distribution;
    private ControlParameter exploitProbability;

    public BareBonesVelocityProvider() {
        this.distribution = new GaussianDistribution();
        this.exploitProbability = ConstantControlParameter.of(0.5);
    }

    public BareBonesVelocityProvider(BareBonesVelocityProvider copy) {
        this.distribution = copy.distribution;
        this.exploitProbability = copy.exploitProbability.getClone();
    }

    /**
     * {@inheritDoc}
     */
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
            if (Rand.nextDouble() < exploitProbability.getParameter()) {
                builder.add(localGuide.doubleValueOf(i));
            } else {
                double sigma = Math.abs(localGuide.doubleValueOf(i) - globalGuide.doubleValueOf(i));
                double mean = (localGuide.doubleValueOf(i) + globalGuide.doubleValueOf(i)) / 2;
                //double tmp1 = cognitive.getParameter();
                //double tmp2 = social.getParameter();
                //andries proposal: double mean = (tmp1*personalBestPosition.getReal(i) + tmp2*nBestPosition.getReal(i)) / (tmp1+tmp2);

                builder.add(this.distribution.getRandomNumber(mean, sigma));
            }
        }

        return builder.build();
    }

    public void setExploitProbability(ControlParameter exploitProbability) {
        this.exploitProbability = exploitProbability;
    }

    public ControlParameter getExploitProbability() {
        return exploitProbability;
    }

    public void setDistribution(ProbabilityDistributionFunction pdf) {
        this.distribution = pdf;
    }

    public ProbabilityDistributionFunction getDistribution() {
        return this.distribution;
    }
}
