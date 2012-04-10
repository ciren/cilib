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
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.functions.activation.Sigmoid;
import net.sourceforge.cilib.math.random.CauchyDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This class calculates the new velocity of the particle using the Coherence 
 * Velocity method described by Hendtlass. The aim of this method of calculating
 * velocity is to improve diversity by soreading particles according to the 
 * optimality of solutions found.
 * 
 * NOTE: This class is only usable if velocities are initialized to not be Zero.
 * Hendtlass has managed to divide by Zero, again.
 */
public class CoherenceVelocityProvider implements VelocityProvider {

    private static final long serialVersionUID = -9051938755796130230L;
    private ControlParameter scalingFactor;
    private ProbabilityDistributionFuction randomNumber;
    private Sigmoid sigmoid;
    private VelocityProvider delegate;
    Topology topology;

    /**
     * Create an instance of {@linkplain CoherenceVelocityProvider}.
     */
    public CoherenceVelocityProvider() {
        this.scalingFactor = ConstantControlParameter.of(1.0);
        this.randomNumber = new CauchyDistribution();
        this.sigmoid = new Sigmoid();
        this.delegate = new StandardVelocityProvider();
    }

    /**
     * Copy constructor. Create a copy of the given instance.
     * @param copy The instance to copy.
     */
    public CoherenceVelocityProvider(CoherenceVelocityProvider copy) {
        this.scalingFactor = copy.scalingFactor.getClone();
        this.randomNumber = copy.randomNumber;
        this.sigmoid = new Sigmoid();
        this.sigmoid.setOffset(copy.sigmoid.getOffset());
        this.sigmoid.setSteepness(copy.sigmoid.getSteepness());
        this.delegate = copy.delegate.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CoherenceVelocityProvider getClone() {
        return new CoherenceVelocityProvider(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector get(Particle particle) {
        PSO pso = (PSO) AbstractAlgorithm.get();
        topology = pso.getTopology();
        double sigmoidResult = getSigmoidResult();
        int dimension = 0;
        double value = 0;
        
        Vector velocity = (Vector) particle.getVelocity();
        Vector.Builder resultingVelocity = Vector.newBuilder();
        
        for(Numeric velocityValue : velocity) {
            value = this.scalingFactor.getParameter() * sigmoidResult * getParticleVelocityForDimension(dimension) * randomNumber.getRandomNumber();
            resultingVelocity.add(value);
        }
        
        return resultingVelocity.build();
    }

    public double getAverageParticleVelocity() {
        double average = 0;
        
        for(int i = 0; i < topology.size(); i++) {
            Particle particle = (Particle) topology.get(i);
            Vector velocity = (Vector) particle.getVelocity();
            average += velocity.norm();
        }
        return average/topology.size();
    }
    
    /*
     * Calculates and returns the swarm center velocity as the norm of the average 
     * velocity of the swarm.
     * @return The swarm centre velocity
     */
    public double getSwarmCenterVelocity() {
        Particle dummyParticle = (Particle) topology.get(0);
        Vector dummyVelocity = (Vector) dummyParticle.getClone().getVelocity();
        Vector sum = dummyVelocity.multiply(0);
        for(int i = 0; i < topology.size(); i++) {
            Particle particle = (Particle) topology.get(i);
            Vector velocity = (Vector) particle.getVelocity();
            sum = sum.plus(velocity);
        }
        sum = sum.divide(topology.size());
        
        return sum.norm();
    }

    /*
     * Calulates and returns the swarm coherence as the swarm center velocity 
     * divided by the average particle velocity.
     * Possibility of division by Zero. Velocities must be initialized to not 
     * be Zero
     * @return The swarm coherence
     */
    private double getSwarmCoherence() {
        return getSwarmCenterVelocity()/getAverageParticleVelocity();
    }

    /*
     * Calculates the average velocity for a certain dimension
     * @param dimension The dimension for which the average should be calculated
     * @return The average velocity for that dimension
     */
    private double getParticleVelocityForDimension(int dimension) {
       double average = 0;
       double dimensions = 0;
       
       for(int i = 0; i < topology.size(); i++) {
           Particle particle = (Particle) topology.get(i);
           dimensions = particle.getVelocity().size();
           if(dimension < dimensions) {
               Vector velocity = (Vector) particle.getVelocity();
               average += velocity.get(dimension).doubleValue();
           }
       }
       return average/topology.size();
    }
    
    /*
     * Gets the result of applying the sigmoid function to the swarm coherence
     * @return The result of applying the sigmoid function
     */
    private double getSigmoidResult() {
        return this.sigmoid.apply(getSwarmCoherence());
    }
    
    @Override
    public void setControlParameters(ParameterizedParticle particle) {
        //do nothing
    }
    
    /*
     * 
     */
    @Override
    public HashMap<String, Double> getControlParameterVelocity(ParameterizedParticle particle) {
        HashMap<String, Double> parameterVelocity = new HashMap<String, Double>();
        PSO pso = (PSO) AbstractAlgorithm.get();
        topology = pso.getTopology().getClone();
        topology.clear();
        Particle standardParticle;
        
        for(Particle swarmParticle : pso.getTopology()) {
            standardParticle = new StandardParticle();
            ParameterizedParticle parameterizedParticle = (ParameterizedParticle) swarmParticle;
            standardParticle.getProperties().put(EntityType.Particle.VELOCITY, Vector.of(parameterizedParticle.getInertia().getVelocity()));
            topology.add(standardParticle);
        }
        
        int dimension = 0;
        double value = 0;
        double resultingVelocity = 0;
        
        //double velocity = particle.getInertia().getVelocity();
        double sigmoidResult = getSigmoidResult();
        value = this.scalingFactor.getParameter() * sigmoidResult * getParticleVelocityForDimension(dimension) * randomNumber.getRandomNumber();
        parameterVelocity.put("InertiaVelocity", value);
        
        //Social Acceleration
        topology.clear();
        
        for(Particle swarmParticle : pso.getTopology()) {
            standardParticle = new StandardParticle();
            ParameterizedParticle parameterizedParticle = (ParameterizedParticle) swarmParticle;
            standardParticle.getProperties().put(EntityType.Particle.VELOCITY, Vector.of(parameterizedParticle.getSocialAcceleration().getVelocity()));
            topology.add(standardParticle);
        }
        
        dimension = 0;
        value = 0;
        resultingVelocity = 0;
        
        //velocity = particle.getSocialAcceleration().getVelocity();
        sigmoidResult = getSigmoidResult();
        value = this.scalingFactor.getParameter() * sigmoidResult * getParticleVelocityForDimension(dimension) * randomNumber.getRandomNumber();
        parameterVelocity.put("SocialAccelerationVelocity", value);
        
        //cognitive acceleration
        topology.clear();
        
        for(Particle swarmParticle : pso.getTopology()) {
            standardParticle = new StandardParticle();
            ParameterizedParticle parameterizedParticle = (ParameterizedParticle) swarmParticle;
            standardParticle.getProperties().put(EntityType.Particle.VELOCITY, Vector.of(parameterizedParticle.getCognitiveAcceleration().getVelocity()));
            topology.add(standardParticle);
        }
        
        dimension = 0;
        value = 0;
        resultingVelocity = 0;
        
        //velocity = particle.getCognitiveAcceleration().getVelocity();
        sigmoidResult = getSigmoidResult();
        value = this.scalingFactor.getParameter() * sigmoidResult * getParticleVelocityForDimension(dimension) * randomNumber.getRandomNumber();
        parameterVelocity.put("CognitiveAccelerationVelocity", value);
        
        //get vmax
        topology.clear();
        
        for(Particle swarmParticle : pso.getTopology()) {
            standardParticle = new StandardParticle();
            ParameterizedParticle parameterizedParticle = (ParameterizedParticle) swarmParticle;
            standardParticle.getProperties().put(EntityType.Particle.VELOCITY, Vector.of(parameterizedParticle.getVmax().getVelocity()));
            topology.add(standardParticle);
        }
        
        dimension = 0;
        value = 0;
        resultingVelocity = 0;
        
        //velocity = particle.getVmax().getVelocity();
        sigmoidResult = getSigmoidResult();
        value = this.scalingFactor.getParameter() * sigmoidResult * getParticleVelocityForDimension(dimension) * randomNumber.getRandomNumber();
        parameterVelocity.put("VmaxVelocity", value);
        
        return parameterVelocity;
    }
}
