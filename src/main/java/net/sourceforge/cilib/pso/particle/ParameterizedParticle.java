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
package net.sourceforge.cilib.pso.particle;

import java.util.HashMap;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ParameterAdaptingPSOControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.initialization.ParameterInclusiveInitializationStrategy;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.pso.guideprovider.GuideProvider;
import net.sourceforge.cilib.pso.guideprovider.NBestGuideProvider;
import net.sourceforge.cilib.pso.guideprovider.PBestGuideProvider;
import net.sourceforge.cilib.pso.positionprovider.PositionProvider;
import net.sourceforge.cilib.pso.positionprovider.StandardPositionProvider;
import net.sourceforge.cilib.pso.velocityprovider.StandardVelocityProvider;
import net.sourceforge.cilib.pso.velocityprovider.VelocityProvider;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This class specifies a particle type which holds and develops its own parameters. It holds four
 * parameters, namely inertia, social acceleration, cognitive acceleration and vmax. These parameters
 * can either be ConstantControlParameters, in which case they will not change when the particle's
 * position changes, or they can be BoundedModifiableControlParameters, in which case they can change
 * as though they are part of the particle's position.
 * 
 * @author Kristina Georgieva
 */
public class ParameterizedParticle extends AbstractParticle{
    private static final long serialVersionUID = 2610843008637279845L;
    protected ParameterizedParticle neighbourhoodBest;
    
    /*private ControlParameter bestInertia;
    private ControlParameter bestSocialAcceleration;
    private ControlParameter bestCognitiveAcceleration;
    private ControlParameter bestVmax;*/
    
    private ParameterAdaptingPSOControlParameter inertia;
    private ParameterAdaptingPSOControlParameter socialAcceleration;
    private ParameterAdaptingPSOControlParameter cognitiveAcceleration;
    private ParameterAdaptingPSOControlParameter vmax;
    
    private ParameterInclusiveInitializationStrategy parameterInclusiveInitializationStrategy;
    private OptimisationProblem optimizationProblem;
    
    private ControlParameter entityLowerBound;
    private ControlParameter entityUpperBound;
    
    private VelocityProvider parameterVelocityProvider;
    private PositionProvider parameterPositionProvider;
    private GuideProvider  parameterLocalGuideProvider;
    private GuideProvider parameterGlobalGuideProvider;
    
    /*
     * Default Constructor sets up the particle with default values for variables
     */
    public ParameterizedParticle() {
        super();
        this.getProperties().put(EntityType.Particle.BEST_POSITION, new Vector());
        this.getProperties().put(EntityType.Particle.VELOCITY, new Vector());
        inertia = new ConstantControlParameter(0.1);
        socialAcceleration = new ConstantControlParameter(0.1);
        cognitiveAcceleration = new ConstantControlParameter(0.1);
        vmax = new ConstantControlParameter(0.1);
        
        entityLowerBound = new ConstantControlParameter(0.1);
        entityLowerBound = new ConstantControlParameter(0.9);
        
        parameterVelocityProvider = new StandardVelocityProvider();
        parameterPositionProvider = new StandardPositionProvider();
        parameterLocalGuideProvider = new PBestGuideProvider();
        parameterGlobalGuideProvider = new NBestGuideProvider();
        
    }
    
    /*
     * Constructor. Copies the values for the variables tthe parameter holds.
     * @param copy The Parametized Particle whose variables need to be sued.
     */
    public ParameterizedParticle(ParameterizedParticle copy) {
        super(copy);
        
        this.getProperties().put(EntityType.Particle.BEST_POSITION, copy.getBestPosition());
        this.getProperties().put(EntityType.Particle.VELOCITY, copy.getVelocity());
        this.neighbourhoodBest = copy.neighbourhoodBest;
        
        inertia = copy.inertia;
        socialAcceleration = copy.socialAcceleration;
        cognitiveAcceleration = copy.cognitiveAcceleration;
        vmax = copy.vmax;
        
        entityLowerBound = copy.entityLowerBound;
        entityLowerBound = copy.entityUpperBound;
        
        parameterVelocityProvider = copy.parameterVelocityProvider;
        parameterPositionProvider = copy.parameterPositionProvider;
        parameterLocalGuideProvider = copy.parameterLocalGuideProvider;
        parameterGlobalGuideProvider = copy.parameterGlobalGuideProvider;
    }
    
    /**
     * Clone method returns a new instance of this particle
     */
    @Override
    public ParameterizedParticle getClone() {
           return new ParameterizedParticle(this);
    }

    /*
     * Equals method checks if object provided is the same as the current particle
     * @param object The object to check
     */
    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if ((object == null) || (this.getClass() != object.getClass()))
            return false;

        ParameterizedParticle other = (ParameterizedParticle) object;
        return super.equals(object) &&
            (this.neighbourhoodBest == null ? true : this.neighbourhoodBest.equals(other.neighbourhoodBest));
    }

    /*
     * returns the superclass' hashcode
     * @return hashcode The hashcode tor eturn
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Gets the best personal fitness of the particle
     * @return The best fitness
     */
    
    @Override
    public Fitness getBestFitness() {
        return (Fitness) this.getProperties().get(EntityType.Particle.BEST_FITNESS);
    }

    /**
     * Gets the best personal position of the particle
     * @return The best position
     */
    @Override
    public Vector getBestPosition() {
        return (Vector) this.getProperties().get(EntityType.Particle.BEST_POSITION);
    }

    /**
     * Gets the dimension of the particle
     * @return The dimension of the particle
     */
    @Override
    public int getDimension() {
        return getPosition().size();
    }

    /**
     * Gets the best particle in this particle's neighbourhood
     * @return The neighbourhood best
     */
    @Override
    public ParameterizedParticle getNeighbourhoodBest() {
        return this.neighbourhoodBest;
    }

    /**
     * Gets the current position of the particle
     * @return The current position
     */
    @Override
    public Vector getPosition() {
        return (Vector) getCandidateSolution();
    }

    /**
     * Gets the particle's velocity vector
     * @return The particle's velocity
     */
    @Override
    public Vector getVelocity() {
        return (Vector) this.getProperties().get(EntityType.Particle.VELOCITY);
    }

    /**
     * Initializes the paricle's candidate solution, best personal position, the velocity and best personal parameters.
     */
    @Override
    public void initialise(OptimisationProblem problem) {
        inertia.setBestValue(inertia.getParameter());
        socialAcceleration.setBestValue(socialAcceleration.getParameter());
        cognitiveAcceleration.setBestValue(cognitiveAcceleration.getParameter());
        vmax.setBestValue(vmax.getParameter());
        
        optimizationProblem = problem;
        parameterInclusiveInitializationStrategy = new ParameterInclusiveInitializationStrategy();
        parameterInclusiveInitializationStrategy.setInertia(inertia);
        parameterInclusiveInitializationStrategy.setSocialAcceleration(socialAcceleration);
        parameterInclusiveInitializationStrategy.setCognitiveAcceleration(cognitiveAcceleration);
        parameterInclusiveInitializationStrategy.setVmax(vmax);
        
        parameterInclusiveInitializationStrategy.setLowerBound(entityLowerBound);
        parameterInclusiveInitializationStrategy.setUpperBound(entityUpperBound);
        
        this.getProperties().put(EntityType.CANDIDATE_SOLUTION, problem.getDomain().getBuiltRepresenation().getClone());
        this.getProperties().put(EntityType.Particle.BEST_POSITION, getPosition().getClone());
        this.getProperties().put(EntityType.Particle.VELOCITY, getPosition().getClone());

        parameterInclusiveInitializationStrategy.setEntityInitializationStrategy(this.positionInitialisationStrategy);
        parameterInclusiveInitializationStrategy.initialize(EntityType.CANDIDATE_SOLUTION, this);
        
        parameterInclusiveInitializationStrategy.setEntityInitializationStrategy(this.personalBestInitialisationStrategy);
        parameterInclusiveInitializationStrategy.initialize(EntityType.Particle.BEST_POSITION, this);
        
        parameterInclusiveInitializationStrategy.setEntityInitializationStrategy(this.velocityInitializationStrategy);
        parameterInclusiveInitializationStrategy.initialize(EntityType.Particle.VELOCITY, this);

        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
        this.getProperties().put(EntityType.Particle.BEST_FITNESS, InferiorFitness.instance());
        this.neighbourhoodBest = this;

        this.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
        
    }

    /**
     * Updates the position of the particle and its parameters
     */
    @Override
    public void updatePosition() {
        getProperties().put(EntityType.CANDIDATE_SOLUTION, this.behavior.getPositionProvider().get(this));
        
        inertia.updateParameter(parameterPositionProvider.getInertia(this));
        socialAcceleration.updateParameter(parameterPositionProvider.getSocialAcceleration(this));
        cognitiveAcceleration.updateParameter(parameterPositionProvider.getCognitiveAcceleration(this));
        vmax.updateParameter(parameterPositionProvider.getVmax(this));
        
    }

    /**
     * Calculates the fitness of the particle by converting it to a standard particle with the parameters
     * added at the end and performing a normal fitness calculation on this standard particle. This method 
     * also updates the personal best value
     */
    @Override
    public void calculateFitness() {
       
        Fitness fitness = getFitnessCalculator().getFitness(this);
        
        this.getProperties().put(EntityType.FITNESS, fitness);
        
        this.personalBestUpdateStrategy.updatePersonalBest(this);
       
    }
    

    /**
     * Sets the value of the best particle within this particle's neighbourhood
     */
    @Override
    public void setNeighbourhoodBest(Particle particle) {
        neighbourhoodBest = (ParameterizedParticle) particle;
    }

    /**
     * Updates the velocity of the particle and the parameters using the particle's velocity provider
     */
    @Override
    public void updateVelocity() {
        this.behavior.getVelocityProvider().setControlParameters(this);
        parameterVelocityProvider.setControlParameters(this);
        getProperties().put(EntityType.Particle.VELOCITY, this.behavior.getVelocityProvider().get(this));
        HashMap<String, Double> parameterVelocity = parameterVelocityProvider.getControlParameterVelocity(this);
        inertia.setVelocity(parameterVelocity.get("InertiaVelocity"));
        socialAcceleration.setVelocity(parameterVelocity.get("SocialAccelerationVelocity"));
        cognitiveAcceleration.setVelocity(parameterVelocity.get("CognitiveAccelerationVelocity"));
        vmax.setVelocity(parameterVelocity.get("VmaxVelocity"));
        
    }

    /**
     * This method is not applicable as the control parameter update is performed differently for a parametized particle.
     */
    @Override
    public void updateControlParameters() {
        
       // this.behavior.getVelocityProvider().updateControlParameters(this);
    }

    /**
     * Reinitializes the particle's position, personal best, velocity and the parameters.
     */
    @Override
    public void reinitialise() {
        this.getProperties().put(EntityType.CANDIDATE_SOLUTION, optimizationProblem.getDomain().getBuiltRepresenation().getClone());
        this.getProperties().put(EntityType.Particle.BEST_POSITION, getPosition().getClone());
        this.getProperties().put(EntityType.Particle.VELOCITY, getPosition().getClone());

        parameterInclusiveInitializationStrategy.setEntityInitializationStrategy(this.positionInitialisationStrategy);
        parameterInclusiveInitializationStrategy.initialize(EntityType.CANDIDATE_SOLUTION, this);
        
        parameterInclusiveInitializationStrategy.setEntityInitializationStrategy(this.personalBestInitialisationStrategy);
        parameterInclusiveInitializationStrategy.initialize(EntityType.Particle.BEST_POSITION, this);
        
        parameterInclusiveInitializationStrategy.setEntityInitializationStrategy(this.velocityInitializationStrategy);
        parameterInclusiveInitializationStrategy.initialize(EntityType.Particle.VELOCITY, this);

    }
    
    /*
     * Gets the local guide for the particle's inertia parameter
     * @return The local guide
     */
    public ControlParameter getLocalGuideInertia() {
        return this.parameterLocalGuideProvider.getInertia(this);
    }
    
    /*
     * Get the local guide for the particle's social parameter
     * @return The local guide
     */
    public ControlParameter getLocalGuideSocial() {
        return this.parameterLocalGuideProvider.getSocialAcceleration(this);
    }
    
    /*
     * Get the local guide for the particle's personal parameter
     * @return The local guide
     */
    public ControlParameter getLocalGuidePersonal() {
        return this.parameterLocalGuideProvider.getCognitiveAcceleration(this);
    }
    
    /*
     * Get the local guide for the particle's vmax parameter
     * @return The local guide
     */
    public ControlParameter getLocalGuideVmax() {
        return this.parameterLocalGuideProvider.getVmax(this);
    }
     
    /*
     * Get the global guide for the particle's inertia parameter
     * @return The local guide
     */
    public ControlParameter getGlobalGuideInertia() {
        return this.parameterGlobalGuideProvider.getInertia(this);
    }
    
    /*
     * Get the global guide for the particle's personal parameter
     * @return The local guide
     */
    public ControlParameter getGlobalGuidePersonal() {
        return this.parameterGlobalGuideProvider.getCognitiveAcceleration(this);
    }
    
    /*
     * Get the global guide for the particle's social parameter
     * @return The local guide
     */
    public ControlParameter getGlobalGuideSocial() {
        return this.parameterGlobalGuideProvider.getSocialAcceleration(this);
    }
    
    /*
     * Get the global guide for the particle's vmax parameter
     * @return The local guide
     */
    public ControlParameter getGlobalGuideVmax() {
        return this.parameterGlobalGuideProvider.getVmax(this);
    }
    
    /*
     * Sets the value for the inertia parameter
     * @param parameter The parameter that inertia must be set to
     */
    public void setInertia(ParameterAdaptingPSOControlParameter parameter) {
        this.inertia = (ParameterAdaptingPSOControlParameter) parameter.getClone();
    }
    
    /*
     * Get the inertia control parameter for the particle
     * @return The inertia parameter
     */
    public ParameterAdaptingPSOControlParameter getInertia() {
        return inertia;
    }
    
    /*
     * Sets the value for the social acceleration parameter
     * @param parameter The parameter that social acceleration must be set to
     */
    public void setSocialAcceleration(ParameterAdaptingPSOControlParameter parameter) {
        this.socialAcceleration = (ParameterAdaptingPSOControlParameter) parameter.getClone();
    }
    
     /*
     * Get the social acceleration control parameter for the particle
     * @return The social acceleration parameter
     */
    public ParameterAdaptingPSOControlParameter getSocialAcceleration() {
        return socialAcceleration;
    }
    
    /*
     * Sets the value for the cognitive acceleration parameter
     * @param parameter The parameter that cognitive acceleration must be set to
     */
    public void setCognitiveAcceleration(ParameterAdaptingPSOControlParameter parameter) {
        this.cognitiveAcceleration = (ParameterAdaptingPSOControlParameter) parameter.getClone();
    }
    
     /*
     * Get the cognitive acceleration control parameter for the particle
     * @return The cognitive acceleration parameter
     */
    public ParameterAdaptingPSOControlParameter getCognitiveAcceleration() {
        return cognitiveAcceleration;
    }
    
    /*
     * Sets the value for the vmax parameter
     * @param parameter The parameter that vmax must be set to
     */
    public void setVmax(ParameterAdaptingPSOControlParameter parameter) {
        this.vmax = (ParameterAdaptingPSOControlParameter) parameter.getClone();
    }
    
     /*
     * Get the vmax control parameter for the particle
     * @return The vmax parameter
     */
    public ParameterAdaptingPSOControlParameter getVmax() {
        return vmax;
    }
    
     /*
     * Get the best inertia control parameter so far for the particle
     * @return The best inertia parameter
     */
    public ParameterAdaptingPSOControlParameter getBestInertia() {
        return inertia.getBestValue();
    }
    
    /*
     * Get the best social acceleration control parameter so far for the particle
     * @return The best social acceleration parameter
     */
    public ParameterAdaptingPSOControlParameter getBestSocialAcceleration() {
        return socialAcceleration.getBestValue();
    }
    
    /*
     * Get the best cognitive acceleration control parameter so far for the particle
     * @return The best cognitive acceleration parameter
     */
    public ParameterAdaptingPSOControlParameter getBestCognitiveAcceleration() {
        return cognitiveAcceleration.getBestValue();
    }
    
    /*
     * Get the best vmax control parameter so far for the particle
     * @return The best vmax parameter
     */
    public ParameterAdaptingPSOControlParameter getBestVmax() {
        return vmax.getBestValue();
    }
    
    /*
     * Sets the best value so far for the inertia parameter
     * @param parameter The parameter that best inertia must be set to
     */
    public void setBestInertia(ParameterAdaptingPSOControlParameter parameter) {
        inertia.setBestValue(parameter.getParameter());
    }
    
    /*
     * Sets the best value so far for the social acceleration parameter
     * @param parameter The parameter that best social acceleration must be set to
     */
    public void setBestSocialAcceleration(ParameterAdaptingPSOControlParameter parameter) {
        socialAcceleration.setBestValue(parameter.getParameter());
    }
    
    /*
     * Sets the best value so far for the cognitive acceleration parameter
     * @param parameter The parameter that best cognitive acceleration must be set to
     */
    public void setBestCognitiveAcceleration(ParameterAdaptingPSOControlParameter parameter) {
        cognitiveAcceleration.setBestValue(parameter.getParameter());
    }
    
    /*
     * Sets the best value so far for the vmax parameter
     * @param parameter The parameter that best vmax must be set to
     */
    public void setBestVmax(ParameterAdaptingPSOControlParameter parameter) {
        vmax.setBestValue(parameter.getParameter());
    }
    
    /*
     * gets the lower bound for the entity's allele
     * @return The value for the lower bound
     */
    public ControlParameter getEntityLowerBound() {
        return entityLowerBound;
    }
    
    /*
     * Sets the lower bound for the entity's allele
     * @param parameterValue The value for the lower bound
     */
    public void setEntityLowerBound(double parameterValue) {
        entityLowerBound = new ConstantControlParameter(parameterValue);
    }
    
    /*
     * gets the upper bound for the entity's allele
     * @return The value for the upper bound
     */
    public ControlParameter getEntityUpperBound() {
        return entityUpperBound;
    }
    
    /*
     * Sets the upper bound for the entity's allele
     * @param parameterValue The value for the upper bound
     */
    public void setEntityUpperBound(double parameterValue) {
        entityUpperBound = new ConstantControlParameter(parameterValue);
    }
    
    /*
     * Sets the Velocity Provider to be used for the parameters. This
     * is specific to the ParameterizedParticle and therefore is added
     * to this calss and not to ParticleBehaviour.
     * 
     * @param provider The velocity provider to be used
     */
    public void setParameterVelocityProvider(VelocityProvider provider) {
        parameterVelocityProvider = provider.getClone();
    }
    
    /*
     * Sets the Position Provider to be used for the parameters. This
     * is specific to the ParameterizedParticle and therefore is added
     * to this calss and not to ParticleBehaviour.
     * 
     * @param provider The velocity provider to be used
     */
    public void setParameterPositionProvider(PositionProvider provider) {
        parameterPositionProvider = provider.getClone();
    }
    
    /*
     * Sets the Local Guide Provider to be used for the parameters. This
     * is specific to the ParameterizedParticle and therefore is added
     * to this calss and not to ParticleBehaviour.
     * 
     * @param provider The velocity provider to be used
     */
    public void setParameterLocalGuideProvider(GuideProvider provider) {
        parameterLocalGuideProvider = provider.getClone();
    }
    
    /*
     * Sets the Global Guide Provider to be used for the parameters. This
     * is specific to the ParameterizedParticle and therefore is added
     * to this calss and not to ParticleBehaviour.
     * 
     * @param provider The velocity provider to be used
     */
    public void setParameterGlobalGuideProvider(GuideProvider provider) {
        parameterGlobalGuideProvider = provider.getClone();
    }
    
    /*
     * Gets the Velocity Provider used for the parameters. This
     * is specific to the ParameterizedParticle and therefore is added
     * to this calss and not to ParticleBehaviour.
     * 
     * @return The velocity provider
     */
    public VelocityProvider getParameterVelocityProvider() {
        return parameterVelocityProvider;
    }
    
    /*
     * Gets the Position Provider used for the parameters. This
     * is specific to the ParameterizedParticle and therefore is added
     * to this calss and not to ParticleBehaviour.
     * 
     * @return The velocity provider
     */
    public PositionProvider getParameterPositionProvider() {
        return parameterPositionProvider;
    }
    
    /*
     * Gets the Local Guide Provider used for the parameters. This
     * is specific to the ParameterizedParticle and therefore is added
     * to this calss and not to ParticleBehaviour.
     * 
     * @return The velocity provider
     */
    public GuideProvider getParameterLocalGuideProvider() {
        return parameterLocalGuideProvider;
    }
    
    /*
     * Gets the Global Guide Provider used for the parameters. This
     * is specific to the ParameterizedParticle and therefore is added
     * to this calss and not to ParticleBehaviour.
     * 
     * @return The velocity provider
     */
    public GuideProvider getParameterGlobalGuideProvider() {
        return parameterGlobalGuideProvider;
    }
}
