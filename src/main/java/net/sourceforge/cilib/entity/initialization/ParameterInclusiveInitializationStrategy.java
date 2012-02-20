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
package net.sourceforge.cilib.entity.initialization;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.pso.particle.ParametizedParticle;

/**
 *
 * @author Kristina
 */
public class ParameterInclusiveInitializationStrategy<P extends ParametizedParticle> implements InitializationStrategy<ParametizedParticle> {
    private static final long serialVersionUID = -7926839076670354209L;
    private ProbabilityDistributionFuction random;
    
    private InitializationStrategy entityInitializationStrategy;
    
    private ControlParameter inertia;
    private ControlParameter socialAcceleration;
    private ControlParameter cognitiveAcceleration;
    private ControlParameter vmax;
    
    private ControlParameter lowerBoundInertia;
    private ControlParameter upperBoundInertia;
    private ControlParameter lowerBoundSocial;
    private ControlParameter upperBoundSocial;
    private ControlParameter lowerBoundPersonal;
    private ControlParameter upperBoundPersonal;
    private ControlParameter lowerBoundVmax;
    private ControlParameter upperBoundVmax;
    private ControlParameter lowerBound;
    private ControlParameter upperBound;
    
    private final double INERTIA_DEFAULT = 0.1;
    private final double SOCIAL_DEFAULT = 0.1;
    private final double PERSONAL_DEFAULT = 0.1;
    private final double VMAX_DEFAULT = 0.1;
    private final int PARAMETER_COUNT = 4;

     /*
     * Default Constructor initializes values to defaults
     */
    public ParameterInclusiveInitializationStrategy() {
        /* TODO edit constant values */
        this.random = new UniformDistribution();
        this.entityInitializationStrategy = new RandomBoundedInitializationStrategy<Entity>();
        
        this.inertia = new ConstantControlParameter(INERTIA_DEFAULT);
        this.socialAcceleration = new ConstantControlParameter(SOCIAL_DEFAULT);
        this.cognitiveAcceleration = new ConstantControlParameter(PERSONAL_DEFAULT);
        this.vmax = new ConstantControlParameter(VMAX_DEFAULT);
        
        this.lowerBoundInertia = new ConstantControlParameter(0.1);
        this.upperBoundInertia = new ConstantControlParameter(0.9);
        this.lowerBoundSocial = new ConstantControlParameter(0.1);
        this.upperBoundSocial = new ConstantControlParameter(0.9);
        this.lowerBoundPersonal = new ConstantControlParameter(0.1);
        this.upperBoundPersonal = new ConstantControlParameter(0.9);
        this.lowerBoundVmax = new ConstantControlParameter(0.1);
        this.upperBoundVmax = new ConstantControlParameter(0.9);
        this.lowerBound = new ConstantControlParameter(0.1);
        this.upperBound = new ConstantControlParameter(0.9);
    }

     /*
     * Alternative Constructor initializes values to those held by the copy
     * @param copy a copy of a ParameterInclusiveInitializationStrategy
     */
    public ParameterInclusiveInitializationStrategy(ParameterInclusiveInitializationStrategy copy) {
        this.random = copy.random;
        this.entityInitializationStrategy = copy.entityInitializationStrategy;
        this.inertia = copy.inertia;
        this.socialAcceleration = copy.socialAcceleration;
        this.cognitiveAcceleration = copy.cognitiveAcceleration;
        this.vmax = copy.vmax;
        
        lowerBoundInertia = copy.lowerBoundInertia;
        upperBoundInertia = copy.upperBoundInertia;
        lowerBoundPersonal = copy.lowerBoundPersonal;
        upperBoundPersonal = copy.upperBoundPersonal;
        lowerBoundSocial = copy.lowerBoundSocial;
        upperBoundSocial = copy.upperBoundSocial;
        lowerBoundVmax = copy.lowerBoundVmax;
        upperBoundVmax = copy.upperBoundVmax;
        lowerBound = copy.lowerBound;
        upperBound = copy.upperBound;
    }

    /*
     * Clone method returns an instance of this class
     */
    @Override
    public ParameterInclusiveInitializationStrategy getClone() {
        return new ParameterInclusiveInitializationStrategy(this);
    }

     /*
     * Initialize the complex Particle with n dimensions representing the position and m dimensions representing the parameters
     * @param key
     * @param particle the particle to be initialized
     */
    @Override
    public void initialize(Enum<?> key, ParametizedParticle particle) {
        //set bounds if there are any
      if(entityInitializationStrategy instanceof RandomBoundedInitializationStrategy) {
          RandomBoundedInitializationStrategy newStrategy = (RandomBoundedInitializationStrategy) entityInitializationStrategy;
          newStrategy.setLowerBound(lowerBound);
          newStrategy.setUpperBound(upperBound);
          entityInitializationStrategy = newStrategy.getClone();
      }  
      
      entityInitializationStrategy.initialize(key, particle);
      
     
      
      initializeParameters();
      
      particle.setInertia(inertia);
      particle.setSocialAcceleration(socialAcceleration);
      particle.setCognitiveAcceleration(cognitiveAcceleration);
      particle.setVmax(vmax);
      
    }
    
    /*
     * Initialize the particle's parameters
     */
    private void initializeParameters() {
        if(!inertia.wasSetByUser())
           inertia.updateParameter(random.getRandomNumber(lowerBoundInertia.getParameter(), upperBoundInertia.getParameter()));
        
        if(!socialAcceleration.wasSetByUser())
           socialAcceleration.updateParameter(random.getRandomNumber(lowerBoundSocial.getParameter(), upperBoundSocial.getParameter()));
        
        if(!cognitiveAcceleration.wasSetByUser())
           cognitiveAcceleration.updateParameter(random.getRandomNumber(lowerBoundPersonal.getParameter(), upperBoundPersonal.getParameter()));
        
        if(!vmax.wasSetByUser())
           vmax.updateParameter(random.getRandomNumber(lowerBoundVmax.getParameter(), upperBoundVmax.getParameter()));
           
    }
    
    /*
     * Get the lower bound of the domain for initializing a Particle
     */
    
    public InitializationStrategy getEntityInitializationStrategy() {
        return entityInitializationStrategy;
    }

    /*
     * Sets teh initialization strategy that will be used to initialize the candidate solution, 
     * best position or velocity value of the particle
     * @param initializationStrategy The initialization strategy
     */
    public void setEntityInitializationStrategy(InitializationStrategy initializationStrategy) {
        this.entityInitializationStrategy = initializationStrategy;
    }
    
    /*
     * Sets the value of the inertia control parameter. The purpose of this method is to set the 
     * parameter to either a ConstantControlParameter of a BoundedModifiableControlParameter.
     * @param parameter The parameter inertia mst be set to
     */
    public void setInertia(ControlParameter parameter) {
        this.inertia = parameter.getClone();
    }
    
    /*
     * Gets the current value for the inertia control parameter
     * @return The inertia control parameter
     */
    public ControlParameter getInertia() {
        return inertia;
    }
    
    /*
     * Sets the value of the social acceleration control parameter. The purpose of this method is to set the 
     * parameter to either a ConstantControlParameter of a BoundedModifiableControlParameter.
     * @param parameter The parameter social acceleration mst be set to
     */
    public void setSocialAcceleration(ControlParameter parameter) {
        this.socialAcceleration = parameter.getClone();
    }
    
    /*
     * Gets the current value for the social acceleration control parameter
     * @return The social acceleration control parameter
     */
    public ControlParameter getSocialAcceleration() {
        return socialAcceleration;
    }
    
    /*
     * Sets the value of the cognitive acceleration control parameter. The purpose of this method is to set the 
     * parameter to either a ConstantControlParameter of a BoundedModifiableControlParameter.
     * @param parameter The parameter cognitive acceleration mst be set to
     */
    public void setCognitiveAcceleration(ControlParameter parameter) {
        this.cognitiveAcceleration = parameter.getClone();
    }
    
    /*
     * Gets the current value for the cognitive acceleration control parameter
     * @return The cognitive acceleration control parameter
     */
    public ControlParameter getPersonal() {
        return cognitiveAcceleration;
    }
    
    /*
     * Sets the value of the vmax control parameter. The purpose of this method is to set the 
     * parameter to either a ConstantControlParameter of a BoundedModifiableControlParameter.
     * @param parameter The parameter vmax mst be set to
     */
    public void setVmax(ControlParameter parameter) {
        this.vmax = parameter.getClone();
    }
    
    /*
     * Gets the current value for the vmax control parameter
     * @return The vmax control parameter
     */
    public ControlParameter getVmax() {
        return vmax;
    }
    
    /*
     * Gets the lower bound of the entity initialization strategy
     * @return The lower bound of the entity initialization strategy
     */
    public ControlParameter getLowerBound() {
        return lowerBound;
    }
    
    /*
     * Gets the lower upper of the entity initialization strategy
     * @return The upper bound of the entity initialization strategy
     */
    public ControlParameter getUpperBound() {
        return upperBound;
    }
    
    /*
     * Sets the lower bound of the entity initialization strategy
     * @return The lower bound of the entity initialization strategy
     */
    public void setLowerBound(ControlParameter parameter) {
        lowerBound = parameter;
    }
    
    /*
     * Sets the upper bound of the entity initialization strategy
     * @return The upper bound of the entity initialization strategy
     */
    public void setUpperBound(ControlParameter parameter) {
        upperBound = parameter;
    }
    
    /*
     * Gets the lower bound of the inertia control parameter
     * @return The lower bound of the inertia control parameter
     */
    public ControlParameter getLowerBoundInertia() {
        return lowerBoundInertia;
    }
    
    /*
     * Gets the upper bound of the inertia control parameter
     * @return The upper bound of the inertia control parameter
     */
    public ControlParameter getUpperBoundInertia() {
        return upperBoundInertia;
    }
    
    /*
     * Gets the lower bound of the social acceleration control parameter
     * @return The lower bound of the social acceleration control parameter
     */
    public ControlParameter getLowerBoundSocialAcceleration() {
        return lowerBoundSocial;
    }
    
    /*
     * Gets the upper bound of the social acceleration control parameter
     * @return The upper bound of the social acceleration control parameter
     */
     public ControlParameter getUpperBoundSocialAcceleration() {
        return upperBoundSocial;
    }
    
     /*
     * Gets the lower bound of the cognitive acceleration cotnrol parameter
     * @return The lower bound of the cognitive acceleration control parameter
     */
    public ControlParameter getLowerBoundCognitiveAcceleration() {
        return lowerBoundPersonal;
    }
    
    /*
     * Gets the upper bound of the cognitive acceleration cotnrol parameter
     * @return The upper bound of the cognitive acceleration control parameter
     */
     public ControlParameter getUpperBoundCognitiveAcceleration() {
        return upperBoundPersonal;
    }
    
     /*
     * Gets the lower bound of the vmax control parameter
     * @return The lower bound of the vmax control parameter
     */
    public ControlParameter getLowerBoundVmax() {
        return lowerBoundVmax;
    }
    
    /*
     * Gets the upper bound of the vmax control parameter
     * @return The upper bound of the vmax control parameter
     */
     public ControlParameter getUpperBoundVmax() {
        return upperBoundVmax;
    }
     
     /*
     * Sets the lower bound of the inertia control parameter
     * @param bound The lower bound inertia is to be set to
     */
     public void setLowerBoundInertia(ControlParameter bound) {
         this.lowerBoundInertia = bound;
     }
     
     /*
     * Sets the upper bound of the inertia control parameter
     * @param upper The lower bound inertia is to be set to
     */
     public void setUpperBoundInertia(ControlParameter bound) {
         this.upperBoundInertia = bound;
     }
      
     /*
     * Sets the lower bound of the social acceleration control parameter
     * @param bound The lower bound social acceleration is to be set to
     */
     public void setLowerBoundSocialAcceleration(ControlParameter bound) {
         this.lowerBoundSocial = bound;
     }
     
     /*
     * Sets the upper bound of the social acceleration control parameter
     * @param upper The lower bound social acceleration is to be set to
     */
     public void setUpperBoundSocialAcceleration(ControlParameter bound) {
         this.upperBoundSocial = bound;
     }
     
     /*
     * Sets the lower bound of the cognitive acceleration control parameter
     * @param bound The lower bound cognitive acceleration is to be set to
     */
     public void setLowerBoundCognitiveAcceleration(ControlParameter bound) {
         this.lowerBoundPersonal = bound;
     }
     
     /*
     * Sets the lower upper of the cognitive acceleration control parameter
     * @param upper The lower bound cognitive acceleration is to be set to
     */
     public void setUpperBoundCognitiveAcceleration(ControlParameter bound) {
         this.upperBoundPersonal = bound;
     }
     
     /*
     * Sets the lower bound of the vmax control parameter
     * @param bound The lower bound vmax is to be set to
     */
     public void setLowerBoundVmax(ControlParameter bound) {
         this.lowerBoundVmax = bound;
     }
     
     /*
     * Sets the upper bound of the vmax control parameter
     * @param bound The lower bound vmax is to be set to
     */
     public void setUpperBoundVmax(ControlParameter bound) {
         this.upperBoundVmax = bound;
     }
}
