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
 * developed by Clerc.
 * <p>References:
 * <ul>
 * <li>
 * <pre>
{@literal @}INPROCEEDINGS{785513,
title={The swarm and the queen: towards a deterministic and adaptive particle swarm optimization},
author={Clerc, M.},
booktitle={Evolutionary Computation, 1999. CEC 99. Proceedings of the 1999 Congress on},
year={1999},
month={},
volume={3},
number={},
pages={-1957 Vol. 3},
abstract={A very simple particle swarm optimization iterative algorithm is presented, with just
one equation and one social/confidence parameter. We define a “no-hope” convergence criterion and
a “rehope” method so that, from time to time, the swarm re-initializes its position, according to
some gradient estimations of the objective function and to the previous re-initialization (it means
it has a kind of very rudimentary memory). We then study two different cases, a quite “easy” one
(the Alpine function) and a “difficult” one (the Banana function), but both just in dimension two.
The process is improved by taking into account the swarm gravity center (the “queen”) and the results
are good enough so that it is certainly worthwhile trying the method on more complex problems},
keywords={adaptive systems, deterministic algorithms, evolutionary computation, iterative methodsAlpine function,
Banana function, adaptive particle swarm optimization, gradient estimations, no-hope convergence criterion,
objective function, queen, re-initialization, rehope method, rudimentary memory, simple particle swarm
optimization iterative algorithm, social/confidence parameter, swarm gravity center},
doi={10.1109/CEC.1999.785513},
ISSN={}, }
</pre>
 * </li>
 * <li>
 * <pre>
{@literal @}ARTICLE{985692,
title={The particle swarm - explosion, stability, and convergence in a multidimensional complex space},
author={Clerc, M. and Kennedy, J.},
journal={Evolutionary Computation, IEEE Transactions on},
year={2002},
month={Feb},
volume={6},
number={1},
pages={58-73},
abstract={The particle swarm is an algorithm for finding optimal regions of complex search spaces
through the interaction of individuals in a population of particles. This paper analyzes a particle's
trajectory as it moves in discrete time (the algebraic view), then progresses to the view of it in
continuous time (the analytical view). A five-dimensional depiction is developed, which describes
the system completely. These analyses lead to a generalized model of the algorithm, containing a set
of coefficients to control the system's convergence tendencies. Some results of the particle swarm optimizer,
implementing modifications derived from the analysis, suggest methods for altering the original algorithm
in ways that eliminate problems and increase the ability of the particle swarm to find optima of some well-studied
test functions },
keywords={convergence of numerical methods, genetic algorithms, numerical stability, search problemsconvergence,
evolutionary computation, multidimensional complex space, optimization, particle swarm, particle trajectory,
search spaces, stability},
doi={10.1109/4235.985692},
ISSN={1089-778X}, }
</pre>
 * </li>
 * </ul>
 *
 * <p>
 * Note, this strategy does not the inertia control parameter.
 * Certain constraints are imposed on the other control parameters in order to
 * calculate the constriction coefficient, namely:
 * $c1r1 + c2r2 \leq 4$ , and
 * $\kappa \in [0, 1]$
 * 
 * @author andrich
 */
public class ConstrictionVelocityUpdate implements VelocityUpdateStrategy {
    private static final long serialVersionUID = -4470110903487138758L;

    private ControlParameter socialAcceleration;
    private ControlParameter cognitiveAcceleration;
    private ControlParameter vMax;
    private ControlParameter kappa;

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
     * @param copy the ConstrictionVelocityUpdate to copy.
     */
    public ConstrictionVelocityUpdate(ConstrictionVelocityUpdate copy) {
        this.socialAcceleration = copy.socialAcceleration.getClone();
        this.cognitiveAcceleration = copy.cognitiveAcceleration.getClone();
        this.vMax = copy.vMax.getClone();
        this.kappa = copy.kappa.getClone();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ConstrictionVelocityUpdate getClone() {
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
