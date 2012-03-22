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
package net.sourceforge.cilib.pso.velocityprovider;

import java.util.HashMap;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * A velocity update strategy that utilizes the constriction coefficient as
 * developed by M. Clerc.
 * <p>References:
 * <ul>
 * <li>
 * <pre>
{@literal @}@INPROCEEDINGS{870279,
title={Comparing inertia weights and constriction factors in particle swarm optimization},
author={Eberhart, R.C. and Shi, Y.},
booktitle={Evolutionary Computation, 2000. Proceedings of the 2000 Congress on},
year={2000},
month={},
volume={1},
number={},
pages={84-88 vol.1},
abstract={The performance of particle swarm optimization using an inertia weight is compared
with performance using a constriction factor. Five benchmark functions are used for the
comparison. It is concluded that the best approach is to use the constriction factor while
limiting the maximum velocity Vmax to the dynamic range of the variable Xmax on each
dimension. This approach provides performance on the benchmark functions superior to any
other published results known by the authors},
keywords={evolutionary computationbenchmark functions, constriction factors, inertia weights, particle swarm optimization},
doi={10.1109/CEC.2000.870279},
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
 * Note, this strategy does not use the inertia control parameter.
 * Certain constraints are imposed on the other control parameters in order to
 * calculate the constriction coefficient, namely:
 * $c1 + c2 \leq 4$ , and
 * $\kappa \in [0, 1]$
 *
 */
public class ConstrictionVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = -4470110903487138758L;

    private ControlParameter socialAcceleration;
    private ControlParameter cognitiveAcceleration;
    protected RandomProvider r1;
    protected RandomProvider r2;

    private ControlParameter kappa;
    private ControlParameter constrictionCoefficient;

    /**
     * Default constructor. The values given to the control parameters attempt to
     * adhere to the constraints of calculating the constriction constant, but do not
     * necessarily represent good values.
     */
    public ConstrictionVelocityProvider() {
        this.socialAcceleration = ConstantControlParameter.of(2.05);
        this.cognitiveAcceleration = ConstantControlParameter.of(2.05);
        this.r1 = new MersenneTwister();
        this.r2 = new MersenneTwister();

        this.kappa = ConstantControlParameter.of(1.0);
        this.constrictionCoefficient = null;
    }

    /**
     * Copy constructor.
     * @param copy the ConstrictionVelocityProvider to copy.
     */
    public ConstrictionVelocityProvider(ConstrictionVelocityProvider copy) {
        this.socialAcceleration = copy.socialAcceleration.getClone();
        this.cognitiveAcceleration = copy.cognitiveAcceleration.getClone();
        this.r1 = copy.r1;
        this.r2 = copy.r2;
        this.kappa = copy.kappa.getClone();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public ConstrictionVelocityProvider getClone() {
        return new ConstrictionVelocityProvider(this);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Vector get(Particle particle) {

        // lazy construction (necessary to do this after user has set c1 and c2, and to only do it once per particle).
        if (this.constrictionCoefficient == null) {
            calculateConstrictionCoefficient();
        }

        Vector velocity = (Vector) particle.getVelocity();
        Vector position = (Vector) particle.getPosition();
        Vector localGuide = (Vector) particle.getLocalGuide();
        Vector globalGuide = (Vector) particle.getGlobalGuide();

        Vector.Builder builder = Vector.newBuilder();
        for (int i = 0; i < particle.getDimension(); ++i) {
            double value = this.constrictionCoefficient.getParameter() * (velocity.doubleValueOf(i)
                    + (localGuide.doubleValueOf(i) - position.doubleValueOf(i)) * this.cognitiveAcceleration.getParameter() * this.r1.nextDouble()
                    + (globalGuide.doubleValueOf(i) - position.doubleValueOf(i)) * this.socialAcceleration.getParameter() * this.r2.nextDouble());
            builder.add(value);
        }
        return builder.build();
    }

    /**
     * Calculate the constriction coefficient as well as the
     * maximum acceleration.
     */
    private void calculateConstrictionCoefficient() {
        double c1 = this.cognitiveAcceleration.getParameter();
        double c2 = this.socialAcceleration.getParameter();

        double phi = c1 + c2;
        if (phi < 4.0) {
            throw new UnsupportedOperationException("Parameter constraint violation: "
                + "The sum of the Cognitive (" + c1 + ") and Social (" + c2 + ") acceleration parameters "
                + "has to be greater than or equal to 4.");
        }
        double chi;
        chi = (2 * this.kappa.getParameter()) / Math.abs(2 - phi - Math.sqrt(phi * (phi - 4.0)));

        this.constrictionCoefficient = ConstantControlParameter.of(chi);
    }

    /**
     * Get the Kappa control parameter.
     * @return the kappa {@link ControlParameter control parameter }.
     */
    public ControlParameter getKappa() {
        return this.kappa;
    }

    /**
     * Set the Kappa control parameter.
     * @param kappa the new kappa {@link ControlParameter control parameter }.
     */
    public void setKappa(ControlParameter kappa) {
        this.kappa = kappa;
    }

    /**
     * Get the cognitive acceleration parameter.
     * @return the cognitive acceleration {@link ControlParameter control parameter }.
     */
    public ControlParameter getCognitiveAcceleration() {
        return this.cognitiveAcceleration;
    }

    /**
     * Set the cognitive acceleration parameter.
     * @param cognitiveAcceleration the new cognitive accerelation {@link ControlParameter control parameter }.
     */
    public void setCognitiveAcceleration(ControlParameter cognitiveAcceleration) {
        this.cognitiveAcceleration = cognitiveAcceleration;
    }

    /**
     * Get the social acceleration parameter.
     * @return the social acceleration {@link ControlParameter control parameter }.
     */
    public ControlParameter getSocialAcceleration() {
        return this.socialAcceleration;
    }

    /**
     * Set the social acceleration parameter.
     * @param socialAcceleration the new social accerelation {@link ControlParameter control parameter }.
     */
    public void setSocialAcceleration(ControlParameter socialAcceleration) {
        this.socialAcceleration = socialAcceleration;
    }

    /**
     * Gets the constriction coefficient.
     * @return the constriction coefficient  {@link ControlParameter control parameter }.
     */
    public ControlParameter getConstrictionCoefficient() {
        return this.constrictionCoefficient;
    }

    /**
     * Sets the constriction coefficient.
     * @param constrictionCoefficient the new constriction coefficient  {@link ControlParameter control parameter }.
     */
    public void setConstrictionCoefficient(ControlParameter constrictionCoefficient) {
        this.constrictionCoefficient = constrictionCoefficient;
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public void setControlParameters(ParameterizedParticle particle) {
        socialAcceleration = particle.getSocialAcceleration();
        cognitiveAcceleration = particle.getCognitiveAcceleration();
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public HashMap<String, Double> getControlParameterVelocity(ParameterizedParticle particle) {
        HashMap<String, Double> parameterVelocity = new HashMap<String, Double>();
        
        if (this.constrictionCoefficient == null) {
            calculateConstrictionCoefficient();
        }

        double velocity = particle.getSocialAcceleration().getVelocity();
        double position =  particle.getSocialAcceleration().getParameter();
        ControlParameter localGuide = particle.getLocalGuideSocial();
        ControlParameter globalGuide = particle.getGlobalGuideSocial();

        double value = this.constrictionCoefficient.getParameter() * (velocity
                + (localGuide.getParameter() - position) * this.cognitiveAcceleration.getParameter() * this.r1.nextDouble()
                + (globalGuide.getParameter() - position) * this.socialAcceleration.getParameter() * this.r2.nextDouble());
        
        parameterVelocity.put("SocialAccelerationVelocity", value);
        
        velocity = particle.getCognitiveAcceleration().getVelocity();
        position =  particle.getCognitiveAcceleration().getParameter();
        localGuide = particle.getLocalGuidePersonal();
        globalGuide = particle.getGlobalGuidePersonal();

        value = this.constrictionCoefficient.getParameter() * (velocity
                + (localGuide.getParameter() - position) * this.cognitiveAcceleration.getParameter() * this.r1.nextDouble()
                + (globalGuide.getParameter() - position) * this.socialAcceleration.getParameter() * this.r2.nextDouble());
        
        parameterVelocity.put("CognitiveAccelerationVelocity", value);
        
        velocity = particle.getInertia().getVelocity();
        position =  particle.getInertia().getParameter();
        localGuide = particle.getLocalGuideInertia();
        globalGuide = particle.getGlobalGuideInertia();

        value = this.constrictionCoefficient.getParameter() * (velocity
                + (localGuide.getParameter() - position) * this.cognitiveAcceleration.getParameter() * this.r1.nextDouble()
                + (globalGuide.getParameter() - position) * this.socialAcceleration.getParameter() * this.r2.nextDouble());
        
        parameterVelocity.put("InertiaVelocity", value);
        
        velocity = particle.getVmax().getVelocity();
        position =  particle.getVmax().getParameter();
        localGuide = particle.getLocalGuideVmax();
        globalGuide = particle.getGlobalGuideVmax();

        value = this.constrictionCoefficient.getParameter() * (velocity
                + (localGuide.getParameter() - position) * this.cognitiveAcceleration.getParameter() * this.r1.nextDouble()
                + (globalGuide.getParameter() - position) * this.socialAcceleration.getParameter() * this.r2.nextDouble());
        
        parameterVelocity.put("VmaxVelocity", value);
        
        return parameterVelocity;
    }
}
