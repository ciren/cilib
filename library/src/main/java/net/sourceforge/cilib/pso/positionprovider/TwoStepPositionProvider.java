/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.positionprovider;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

/**
 * Implementation of the Two Step position update strategy.
 * <p>
 * Reference:
 * <p>
 * <ul><li>
 * B.B. Thompson, R.J. Marks, M.A. El-Sharkawi, W.J. Fox, and R.T. Miyamoto.
 * "Inversion of Neural Network Underwater Acoustic Model for Estimation of
 * Bottom Parameters using Modified Particle Swarm Optimizer." In Proceedings of
 * the International Joint Conference on Neural Networks, page 1306, 2003.
 * </li></ul>
 */
public class TwoStepPositionProvider implements PositionProvider {
    protected ControlParameter beta;

    /**
     * Create an new instance of {@code TwoStepPositionProvider}.
     */
    public TwoStepPositionProvider() {
        this.beta = ConstantControlParameter.of(0.5);
    }

    /**
     * Copy constructor. Copy the provided instance.
     * @param copy The instance to copy.
     */
    public TwoStepPositionProvider(TwoStepPositionProvider copy) {
        this.beta = copy.beta.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TwoStepPositionProvider getClone() {
        return new TwoStepPositionProvider(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector get(Particle particle) {
        Vector velocity = (Vector) particle.getVelocity();
        double beta = this.beta.getParameter();

        Vector position = (Vector) particle.getPosition();
        Vector position1 = position.plus(velocity);
        Vector position2 = position.plus(velocity.multiply(beta));

        Problem problem = AbstractAlgorithm.get().getOptimisationProblem();

        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); i++) {
            Vector x = Vector.of(position.get(i));
            Vector x1 = Vector.of(position1.get(i));
            Vector x2 = Vector.of(position2.get(i));

            double fx = problem.getFitness(x).getValue();
            double f1 = problem.getFitness(x1).getValue() - fx;
            double f2 = problem.getFitness(x2).getValue() - fx;

            builder.add(Math.max(-f1 / beta, -f2 / beta));
        }
        return builder.build();
    }

    /**
     * Set the {@code beta} {@link ControlParameter} used to control the step
     * size of the gradient.
     *
     * @param beta the {@code beta} control parameter.
     */
    public void setBeta(ControlParameter beta) {
        this.beta = beta;
    }
}
