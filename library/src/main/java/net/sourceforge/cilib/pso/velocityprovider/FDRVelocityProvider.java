/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider;

import java.util.Iterator;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ExponentiallyVaryingControlParameter;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Implementation of the FDR-PSO velocity update equation.
 *
 * <p>
 * BibTex entry:<br>
 * <code>
 * &#64;ARTICLE{1202264,<br>
 * title={Fitness-distance-ratio based particle swarm optimization},<br>
 * author={Peram, T. and Veeramachaneni, K. and Mohan, C.K.},<br>
 * journal={Swarm Intelligence Symposium, 2003. SIS '03. Proceedings of the 2003 IEEE},<br>
 * year={2003},<br>
 * month={April},<br>
 * pages={ 174-181}}<br>
 * </code>
 * </p>
 *
 */
public class FDRVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = -7117135203986406944L;

    private ControlParameter fdrMaximizerAcceleration;

    private StandardVelocityProvider delegate;

    public FDRVelocityProvider() {
        this.fdrMaximizerAcceleration = ConstantControlParameter.of(2);

        //TODO: recheck this inertia, the original paper has some weird values that become negative early on
        this.delegate = new StandardVelocityProvider(new ExponentiallyVaryingControlParameter(0.9, 0.4),
                                                     ConstantControlParameter.of(1),
                                                     ConstantControlParameter.of(1));
    }

    public FDRVelocityProvider(FDRVelocityProvider copy) {
        this.fdrMaximizerAcceleration = copy.fdrMaximizerAcceleration.getClone();
        this.delegate = copy.delegate.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FDRVelocityProvider getClone() {
        return new FDRVelocityProvider(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector get(Particle particle) {
        Vector position = (Vector) particle.getPosition();

        fj.data.List<Particle> topology = ((PSO) AbstractAlgorithm.get()).getTopology();

        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); ++i) {
            Particle fdrMaximizer = topology.head();
            double maxFDR = Double.NEGATIVE_INFINITY;

            for (Particle p : topology) {
                if (p != particle) {
                    Fitness f = p.getBestFitness();
                    Vector pos = (Vector) p.getBestPosition();

                    double testFDR = Math.abs(f.getValue() - particle.getFitness().getValue()) * f.compareTo(particle.getFitness()) / 
                        Math.abs(position.doubleValueOf(i) - pos.doubleValueOf(i));

                    if (testFDR > maxFDR) {
                        maxFDR = testFDR;
                        fdrMaximizer = p;
                    }
                }
            }

            Vector fdrMaximizerPosition = (Vector) fdrMaximizer.getBestPosition();
            builder.add(fdrMaximizerAcceleration.getParameter() * Rand.nextDouble()
                    * (fdrMaximizerPosition.doubleValueOf(i) - position.doubleValueOf(i)));
        }

        return delegate.get(particle).plus(builder.build());
    }

    /**
     * @return the fdrMaximizerAcceleration
     */
    public ControlParameter getFdrMaximizerAcceleration() {
        return this.fdrMaximizerAcceleration;
    }

    /**
     * @param fdrMaximizerAcceleration
     *            the fdrMaximizerAcceleration to set
     */
    public void setFdrMaximizerAcceleration(ControlParameter fdrMaximizerAcceleration) {
        this.fdrMaximizerAcceleration = fdrMaximizerAcceleration;
    }
}
