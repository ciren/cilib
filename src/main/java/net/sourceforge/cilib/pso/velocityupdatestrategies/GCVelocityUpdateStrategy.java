/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.pso.velocityupdatestrategies;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>
 * An implementation of the Guaranteed Convergence PSO algorithm. The GCPSO is a simple extension
 * to the normal PSO algorithm and the modifications to the algorithm is implemented as
 * a simple {@link VelocityUpdateStrategy}
 * </p><p>
 * References:
 * </p><p><ul><li>
 * F. van den Bergh and A. Engelbrecht, "A new locally convergent particle swarm optimizer,"
 * in Proceedings of IEEE Conference on Systems, Man and Cybernetics,
 * (Hammamet, Tunisia), Oct. 2002.
 * </li><li>
 * F. van den Bergh, "An Analysis of Particle Swarm Optimizers,"
 * PhD thesis, Department of Computer Science,
 * University of Pretoria, South Africa, 2002.
 * </li></ul></p>
 *
 * @TODO: The Rho value should be a vector to hold the rho value for each dimension!
 *
 * It is very important to realise the importance of the <code>rho</code> values. <code>rho</code>
 * determines the local search size of the global best particle and depending on the domain
 * this could result in poor performance if the <code>rho</code> value is too small or too large depending
 * on the specified problem domain. For example, a <code>rho</code> value of 1.0 is not a good
 * value within problems which have a domain that spans <code>[0,1]</code>
 */
public class GCVelocityUpdateStrategy extends StandardVelocityUpdate {

    private static final long serialVersionUID = 5985694749940610522L;
    private ControlParameter rhoLowerBound;
    private ControlParameter rho;
    private int successCount;
    private int failureCount;
    private int successCountThreshold;
    private int failureCountThreshold;
    private Fitness oldFitness;
    private ControlParameter rhoExpandCoefficient;
    private ControlParameter rhoContractCoefficient;

    /**
     * Create an instance of the GC Velocity Update strategy.
     */
    public GCVelocityUpdateStrategy() {
        super();
        oldFitness = InferiorFitness.instance();

        rho = new ConstantControlParameter(1.0);
        rhoLowerBound = new ConstantControlParameter(1.0e-323);
        successCount = 0;
        failureCount = 0;
        successCountThreshold = 15;
        failureCountThreshold = 5;
        rhoExpandCoefficient = new ConstantControlParameter(1.2);
        rhoContractCoefficient = new ConstantControlParameter(0.5);

        vMax = new ConstantControlParameter(0.5); // This needs to be set dynamically - this is not valid for all problems
    }

    /**
     * Copy constructor. Copy the given instance.
     * @param copy The instance to copy.
     */
    public GCVelocityUpdateStrategy(GCVelocityUpdateStrategy copy) {
        super(copy);
        this.oldFitness = copy.oldFitness.getClone();

        this.rho = copy.rho.getClone();
        this.rhoLowerBound = copy.rhoLowerBound.getClone();
        this.successCount = copy.successCount;
        this.failureCount = copy.failureCount;
        this.successCountThreshold = copy.successCountThreshold;
        this.failureCountThreshold = copy.failureCountThreshold;
        this.rhoExpandCoefficient = copy.rhoExpandCoefficient.getClone();
        this.rhoContractCoefficient = copy.rhoContractCoefficient.getClone();

        this.vMax = copy.vMax.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GCVelocityUpdateStrategy getClone() {
        return new GCVelocityUpdateStrategy(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateVelocity(Particle particle) {
        PSO pso = (PSO) AbstractAlgorithm.get();
        final Particle globalBest = pso.getTopology().getBestEntity(new SocialBestFitnessComparator<Particle>());

        if (particle == globalBest) {
            final Vector velocity = (Vector) particle.getVelocity();
            final Vector position = (Vector) particle.getPosition();
            final Vector nBestPosition = (Vector) particle.getNeighbourhoodBest().getPosition();

            for (int i = 0; i < velocity.size(); ++i) {
                double component = -position.doubleValueOf(i) + nBestPosition.doubleValueOf(i)
                        + this.inertiaWeight.getParameter() * velocity.doubleValueOf(i)
                        + rho.getParameter() * (1 - 2 * r2.nextDouble());

                velocity.setReal(i, component);
                clamp(velocity, i);
            }

            oldFitness = particle.getFitness().getClone(); // Keep a copy of the old Fitness object - particle.calculateFitness() within the IterationStrategy resets the fitness value
            return;
        }

        super.updateVelocity(particle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateControlParameters(Particle particle) {
        // Remember NOT to reset the rho value to 1.0
        PSO pso = (PSO) AbstractAlgorithm.get();

        if (particle == pso.getTopology().getBestEntity(new SocialBestFitnessComparator<Particle>())) {
            Fitness newFitness = particle.getFitnessCalculator().getFitness(particle);

            if (!newFitness.equals(oldFitness)) {
                this.failureCount = 0;
                this.successCount++;
            } else {
                this.successCount = 0;
                this.failureCount++;
            }

            updateRho((Vector) particle.getPosition());
            return;
        }

        failureCount = 0;
        successCount = 0;
        super.updateControlParameters(particle);
    }

    /**
     * Update the <code>rho</code> value.
     * @param position
     */
    private void updateRho(Vector position) { // the Rho value is problem and dimension dependent
        double tmp = 0.0;

        Bounds component = position.boundsOf(0);
        double average = (component.getUpperBound() - component.getLowerBound()) / rhoExpandCoefficient.getParameter();

        if (successCount >= successCountThreshold) {
            tmp = rhoExpandCoefficient.getParameter() * rho.getParameter();
        }
        if (failureCount >= failureCountThreshold) {
            tmp = rhoContractCoefficient.getParameter() * rho.getParameter();
        }

        if (tmp <= rhoLowerBound.getParameter()) {
            tmp = rhoLowerBound.getParameter();
        }
        if (tmp >= average) {
            tmp = average;
        }

        rho.setParameter(tmp);
    }

    /**
     * Get the lower-bound value for <code>rho</code>.
     * @return The lower-bound value for <code>rho</code>.
     */
    public ControlParameter getRhoLowerBound() {
        return rhoLowerBound;
    }

    /**
     * Set the lower-bound value for <code>rho</code>.
     * @param rhoLowerBound The lower-bound to set.
     */
    public void setRhoLowerBound(ControlParameter rhoLowerBound) {
        this.rhoLowerBound = rhoLowerBound;
    }

    /**
     * Get the current value for <code>rho</code>.
     * @return The current value for <code>rho</code>.
     */
    public ControlParameter getRho() {
        return rho;
    }

    /**
     * Set the value for <code>rho</code>.
     * @param rho The value to set.
     */
    public void setRho(ControlParameter rho) {
        this.rho = rho;
    }

    /**
     * Get the count of success threshold.
     * @return The success threshold.
     */
    public int getSuccessCountThreshold() {
        return successCountThreshold;
    }

    /**
     * Set the threshold of success count value.
     * @param successCountThreshold The value to set.
     */
    public void setSuccessCountThreshold(int successCountThreshold) {
        this.successCountThreshold = successCountThreshold;
    }

    /**
     * Get the count of failure threshold.
     * @return The failure threshold.
     */
    public int getFailureCountThreshold() {
        return failureCountThreshold;
    }

    /**
     * Set the count of failure threshold.
     * @param failureCountThreshold The value to set.
     */
    public void setFailureCountThreshold(int failureCountThreshold) {
        this.failureCountThreshold = failureCountThreshold;
    }

    /**
     * Get the coefficient value for <code>rho</code> expansion.
     * @return The expansion coefficient value.
     */
    public ControlParameter getRhoExpandCoefficient() {
        return rhoExpandCoefficient;
    }

    /**
     * Set the value of the coefficient of expansion.
     * @param rhoExpandCoefficient The value to set.
     */
    public void setRhoExpandCoefficient(ControlParameter rhoExpandCoefficient) {
        this.rhoExpandCoefficient = rhoExpandCoefficient;
    }

    /**
     * Get the coefficient value for <code>rho</code> contraction.
     * @return The contraction coefficient value.
     */
    public ControlParameter getRhoContractCoefficient() {
        return rhoContractCoefficient;
    }

    /**
     * Set the contraction coefficient value.
     * @param rhoContractCoefficient The value to set.
     */
    public void setRhoContractCoefficient(ControlParameter rhoContractCoefficient) {
        this.rhoContractCoefficient = rhoContractCoefficient;
    }
}
