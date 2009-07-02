/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.pso.velocityupdatestrategies;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.RandomizingControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * A velocity update strategy that utilizes the constriction coefficient as
 * developed by Clerc. References:
 * <ul>
 * <li>
 * Inproceedings (Clerc1999)
 * Clerc, M.
 * The swarm and the queen: towards a deterministic and adaptive particle swarm
 * optimization
 * Evolutionary Computation, 1999. CEC 99. Proceedings of the 1999 Congress on,
 * 1999, 3, -1957 Vol. 3
 * </li>
 * <li>
 * Article (Clerc2002)
 * Clerc, M. & Kennedy, J.
 * The particle swarm - explosion, stability, and convergence in a 
 * multidimensional complex space Evolutionary Computation,
 * IEEE Transactions on, 2002, 6, 58-73
 * </li>
 * </ul>
 * 
 * Note, this strategy does not the inertia control parameter.
 * Certain constraints are imposed on the other control parameters in order to
 * calculate the constriction coefficient, namely:
 * $c1r1 + c2r2 \leq 4$ , and
 * $\kappa \in [0, 1]$
 * 
 * @author andrich
 */
public class ConstrictionVelocityUpdate implements VelocityUpdateStrategy {

    protected ControlParameter socialAcceleration;
    protected ControlParameter cognitiveAcceleration;
    protected ControlParameter vMax;
    protected ControlParameter kappa;

    /**
     * Default constructor. The values given to the control parameters attempt to
     * adhere to the constraints of calculating the constriction constant, but do not
     * necessarily represent good values.
     */
    public ConstrictionVelocityUpdate() {
        socialAcceleration = new RandomizingControlParameter();
        cognitiveAcceleration = new RandomizingControlParameter();
        vMax = new ConstantControlParameter();
        kappa = new ConstantControlParameter();

        socialAcceleration.setParameter(3.0);
        cognitiveAcceleration.setParameter(3.0);
        vMax.setParameter(Double.MAX_VALUE);
        kappa.setParameter(0.1);
    }

    /**
     * Copy constructor.
     * @param orig the ConstrictionVelocityUpdate to copy.
     */
    public ConstrictionVelocityUpdate(ConstrictionVelocityUpdate orig) {
        this.socialAcceleration = new RandomizingControlParameter((RandomizingControlParameter) orig.socialAcceleration);
        this.cognitiveAcceleration = new RandomizingControlParameter((RandomizingControlParameter) orig.cognitiveAcceleration);
        this.vMax = new ConstantControlParameter((ConstantControlParameter) orig.vMax);
        this.kappa = new ConstantControlParameter((ConstantControlParameter) orig.kappa);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public VelocityUpdateStrategy getClone() {
        return new ConstrictionVelocityUpdate(this);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void updateVelocity(Particle particle) {
        assertAccelerationConstraints();

        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        Vector bestPosition = (Vector) particle.getBestPosition();
        Vector nBestPosition = (Vector) particle.getNeighbourhoodBest().getBestPosition();


        for (int i = 0; i < particle.getDimension(); ++i) {
            // calculate the constriction coefficient
            double c1 = cognitiveAcceleration.getParameter();
            double c2 = socialAcceleration.getParameter();
            // c1r1 + c2r2 has to be greater or equal to 4
            while ((c1 + c2) < 4) {
                c1 = cognitiveAcceleration.getParameter();
                c2 = socialAcceleration.getParameter();
            }
            double phi = c1 + c2;
            double constrictionCoefficient = (2 * kappa.getParameter()) /
                    Math.abs(2 - phi - Math.sqrt(phi * (phi - 4.0)));

            double value = velocity.getReal(i) +
                    (bestPosition.getReal(i) - position.getReal(i)) * c1 +
                    (nBestPosition.getReal(i) - position.getReal(i)) * c2;
            value = constrictionCoefficient * value;
            velocity.setReal(i, value);

            clamp(velocity, i);
        }
    }

    /**
     * Clamp to maximum velocity.
     * @param velocity The {@link Vector} to be clamped.
     * @param i The dimension index to be clamped
     */
    protected void clamp(Vector velocity, int i) {
        // if vMax is not set (or set as max), it is unnecessary to clamp
        if (Double.compare(vMax.getParameter(), Double.MAX_VALUE) == 0) {
            return;
        }
        if (velocity.getReal(i) < -vMax.getParameter()) {
            velocity.setReal(i, -vMax.getParameter());
        } else if (velocity.getReal(i) > vMax.getParameter()) {
            velocity.setReal(i, vMax.getParameter());
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void updateControlParameters(Particle particle) {
        this.kappa.updateParameter();
        this.cognitiveAcceleration.updateParameter();
        this.socialAcceleration.updateParameter();
        this.vMax.updateParameter();
    }

    /**
     * Ensure that values of c1 and c2 make it possible to calculate the
     * constriction coefficient.
     */
    private void assertAccelerationConstraints() {
        double c1 = ((RandomizingControlParameter) cognitiveAcceleration).getControlParameter().getParameter();
        double c2 = ((RandomizingControlParameter) socialAcceleration).getControlParameter().getParameter();
        if (c1 + c2 < 4) {
            throw new UnsupportedOperationException("Parameter constraint violation: " +
                    "The sum of the Cognitive (" + c1 + ") and Social (" + c2 + ") acceleration parameters " +
                    "has to be greater than or equal to 4.");
        }
    }

    /**
     * Get the coginitive acceleration parameter.
     * @return the cognitive acceleration {@link ControlParameter control parameter }.
     */
    public ControlParameter getCognitiveAcceleration() {
        return cognitiveAcceleration;
    }

    /**
     * Set the coginitive acceleration parameter.
     * @param cognitiveAcceleration the new cognitive acceleration {@link ControlParameter control parameter }.
     */
    public void setCognitiveAcceleration(ControlParameter cognitiveAcceleration) {
        this.cognitiveAcceleration = cognitiveAcceleration;
    }

    /**
     * Get the Kappa control parameter.
     * @return the kappa {@link ControlParameter control parameter }.
     */
    public ControlParameter getKappa() {
        return kappa;
    }

    /**
     * Set the Kappa control parameter.
     * @param kappa the new kappa {@link ControlParameter control parameter }.
     */
    public void setKappa(ControlParameter kappa) {
        this.kappa = kappa;
    }

    /**
     * Get the social acceleration parameter.
     * @return the social acceleration {@link ControlParameter control parameter }.
     */
    public ControlParameter getSocialAcceleration() {
        return socialAcceleration;
    }

    /**
     * Set the social acceleration parameter.
     * @param socialAcceleration the new social accerelation {@link ControlParameter control parameter }.
     */
    public void setSocialAcceleration(ControlParameter socialAcceleration) {
        this.socialAcceleration = socialAcceleration;
    }

    /**
     * Get the maximum velocity parameter.
     * @return the maximum velocity {@link ControlParameter control parameter }.
     */
    public ControlParameter getVMax() {
        return vMax;
    }

    /**
     * Set the maximum velocity parameter.
     * @param vMax the new maximum velocity {@link ControlParameter control parameter }.
     */
    public void setVMax(ControlParameter vMax) {
        this.vMax = vMax;
    }
}
