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
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.initialization.ParameterInclusiveInitializationStrategy;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Kristina
 */
public class ParametizedParticle extends AbstractParticle{
    private static final long serialVersionUID = 2610843008637279845L;
    protected ParametizedParticle neighbourhoodBest;
    
    private ControlParameter bestInertia;
    private ControlParameter bestSocialAcceleration;
    private ControlParameter bestCognitiveAcceleration;
    private ControlParameter bestVmax;
    
    private ControlParameter inertia;
    private ControlParameter socialAcceleration;
    private ControlParameter cognitiveAcceleration;
    private ControlParameter vmax;
    
    private ParameterInclusiveInitializationStrategy parameterInclusiveInitializationStrategy;
    private OptimisationProblem optimizationProblem;
    
    /*
     * Default Constructor sets up the particle with default values for variables
     */
    public ParametizedParticle() {
        super();
        this.getProperties().put(EntityType.Particle.BEST_POSITION, new Vector());
        this.getProperties().put(EntityType.Particle.VELOCITY, new Vector());
        inertia = new ConstantControlParameter(0.1);
        socialAcceleration = new ConstantControlParameter(0.1);
        cognitiveAcceleration = new ConstantControlParameter(0.1);
        vmax = new ConstantControlParameter(0.1);
        
        bestInertia = inertia.getClone();
        bestCognitiveAcceleration = cognitiveAcceleration.getClone();
        bestSocialAcceleration = socialAcceleration.getClone();
        bestVmax = vmax.getClone();
    }
    
    /*
     * Constructor. Copies the values for the variables tthe parameter holds.
     * @param copy The Parametized Particle whose variables need to be sued.
     */
    public ParametizedParticle(ParametizedParticle copy) {
        super(copy);
        inertia = copy.inertia;
        socialAcceleration = copy.socialAcceleration;
        cognitiveAcceleration = copy.cognitiveAcceleration;
        vmax = copy.vmax;
        
        bestInertia = copy.bestInertia;
        bestCognitiveAcceleration = copy.bestCognitiveAcceleration;
        bestSocialAcceleration = copy.bestSocialAcceleration;
        bestVmax = copy.bestVmax;
    }
    
    /**
     * Clone method returns a new instance of this particle
     */
    @Override
    public ParametizedParticle getClone() {
           return new ParametizedParticle(this);
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

        ParametizedParticle other = (ParametizedParticle) object;
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
        /*TODO: Kristina Modify*/
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
    public ParametizedParticle getNeighbourhoodBest() {
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
        bestInertia = inertia;
        bestSocialAcceleration = socialAcceleration;
        bestCognitiveAcceleration = cognitiveAcceleration;
        bestVmax = vmax;
        
        optimizationProblem = problem;
        parameterInclusiveInitializationStrategy = new ParameterInclusiveInitializationStrategy();
        parameterInclusiveInitializationStrategy.setInertia(inertia);
        parameterInclusiveInitializationStrategy.setSocialAcceleration(socialAcceleration);
        parameterInclusiveInitializationStrategy.setCognitiveAcceleration(cognitiveAcceleration);
        parameterInclusiveInitializationStrategy.setVmax(vmax);
        
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
        inertia.updateParameter(this.behavior.getPositionProvider().getInertia(this));
        socialAcceleration.updateParameter(this.behavior.getPositionProvider().getSocialAcceleration(this));
        cognitiveAcceleration.updateParameter(this.behavior.getPositionProvider().getCognitiveAcceleration(this));
        vmax.updateParameter(this.behavior.getPositionProvider().getVmax(this));
    }

    /**
     * Calculates the fitness of the particle by converting it to a standard particle with the parameters
     * added at the end and performing a normal fitness calculation on this standard particle. This method 
     * also updates the personal best value
     */
    @Override
    public void calculateFitness() {
        
        Vector solutionAndParameterCombination = (Vector) this.getCandidateSolution().getClone();
        solutionAndParameterCombination.add(Real.valueOf(inertia.getParameter()));
        solutionAndParameterCombination.add(Real.valueOf(socialAcceleration.getParameter()));
        solutionAndParameterCombination.add(Real.valueOf(cognitiveAcceleration.getParameter()));
        solutionAndParameterCombination.add(Real.valueOf(vmax.getParameter()));
        
        Vector solutionAndParameterCombinationVelocity = (Vector) this.getVelocity().getClone();
        solutionAndParameterCombinationVelocity.add(Real.valueOf(inertia.getVelocity()));
        solutionAndParameterCombinationVelocity.add(Real.valueOf(socialAcceleration.getVelocity()));
        solutionAndParameterCombinationVelocity.add(Real.valueOf(cognitiveAcceleration.getVelocity()));
        solutionAndParameterCombinationVelocity.add(Real.valueOf(vmax.getVelocity()));
        
        Vector solutionAndParameterCombinationBest = (Vector) this.getBestPosition().getClone();
        solutionAndParameterCombinationBest.add(Real.valueOf(getBestInertia().getParameter()));
        solutionAndParameterCombinationBest.add(Real.valueOf(getBestSocialAcceleration().getParameter()));
        solutionAndParameterCombinationBest.add(Real.valueOf(getBestCognitiveAcceleration().getParameter()));
        solutionAndParameterCombinationBest.add(Real.valueOf(getBestVmax().getParameter()));
        
        Particle simplifiedParticle = new StandardParticle();
        simplifiedParticle.setCandidateSolution(solutionAndParameterCombination);
        simplifiedParticle.setNeighbourhoodBest(this.getNeighbourhoodBest());
        simplifiedParticle.getProperties().put(EntityType.Particle.BEST_POSITION, solutionAndParameterCombinationBest);
        simplifiedParticle.getProperties().put(EntityType.Particle.VELOCITY, solutionAndParameterCombinationVelocity);
        Fitness fitness = getFitnessCalculator().getFitness(simplifiedParticle);
        this.getProperties().put(EntityType.FITNESS, fitness);
        
        this.personalBestUpdateStrategy.updateParametizedPersonalBest(this);
    }

    /**
     * Sets the value of the best particle within this particle's neighbourhood
     */
    @Override
    public void setNeighbourhoodBest(Particle particle) {
        neighbourhoodBest = (ParametizedParticle) particle;
    }

    /**
     * Updates the velocity of the particle and the parameters using the particle's velocity provider
     */
    @Override
    public void updateVelocity() {
        this.behavior.getVelocityProvider().setControlParameters(this);
        getProperties().put(EntityType.Particle.VELOCITY, this.behavior.getVelocityProvider().get(this));
        HashMap<String, Double> velocityHoldingParticle = this.behavior.getVelocityProvider().getControlParameterVelocity(this);
        inertia.setVelocity(velocityHoldingParticle.get("InertiaVelocity"));
        socialAcceleration.setVelocity(velocityHoldingParticle.get("SocialAccelerationVelocity"));
        cognitiveAcceleration.setVelocity(velocityHoldingParticle.get("CognitiveAccelerationVelocity"));
        vmax.setVelocity(velocityHoldingParticle.get("VmaxVelocity"));
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
        return this.behavior.getLocalGuideProvider().getInertia(this);
    }
    
    /*
     * Get the local guide for the particle's social parameter
     * @return The local guide
     */
    public ControlParameter getLocalGuideSocial() {
        return this.behavior.getLocalGuideProvider().getSocialAcceleration(this);
    }
    
    /*
     * Get the local guide for the particle's personal parameter
     * @return The local guide
     */
    public ControlParameter getLocalGuidePersonal() {
        return this.behavior.getLocalGuideProvider().getCognitiveAcceleration(this);
    }
    
    /*
     * Get the local guide for the particle's vmax parameter
     * @return The local guide
     */
    public ControlParameter getLocalGuideVmax() {
        return this.behavior.getLocalGuideProvider().getVmax(this);
    }
     
    /*
     * Get the global guide for the particle's inertia parameter
     * @return The local guide
     */
    public ControlParameter getGlobalGuideInertia() {
        return this.behavior.getGlobalGuideProvider().getInertia(this);
    }
    
    /*
     * Get the global guide for the particle's personal parameter
     * @return The local guide
     */
    public ControlParameter getGlobalGuidePersonal() {
        return this.behavior.getGlobalGuideProvider().getCognitiveAcceleration(this);
    }
    
    /*
     * Get the global guide for the particle's social parameter
     * @return The local guide
     */
    public ControlParameter getGlobalGuideSocial() {
        return this.behavior.getGlobalGuideProvider().getSocialAcceleration(this);
    }
    
    /*
     * Get the global guide for the particle's vmax parameter
     * @return The local guide
     */
    public ControlParameter getGlobalGuideVmax() {
        return this.behavior.getGlobalGuideProvider().getVmax(this);
    }
    
    /*
     * Sets the value for the inertia parameter
     * @param parameter The parameter that inertia must be set to
     */
    public void setInertia(ControlParameter parameter) {
        this.inertia = parameter.getClone();
    }
    
    /*
     * Get the inertia control parameter for the particle
     * @return The inertia parameter
     */
    public ControlParameter getInertia() {
        return inertia;
    }
    
    /*
     * Sets the value for the social acceleration parameter
     * @param parameter The parameter that social acceleration must be set to
     */
    public void setSocialAcceleration(ControlParameter parameter) {
        this.socialAcceleration = parameter.getClone();
    }
    
     /*
     * Get the social acceleration control parameter for the particle
     * @return The social acceleration parameter
     */
    public ControlParameter getSocialAcceleration() {
        return socialAcceleration;
    }
    
    /*
     * Sets the value for the cognitive acceleration parameter
     * @param parameter The parameter that cognitive acceleration must be set to
     */
    public void setCognitiveAcceleration(ControlParameter parameter) {
        this.cognitiveAcceleration = parameter.getClone();
    }
    
     /*
     * Get the cognitive acceleration control parameter for the particle
     * @return The cognitive acceleration parameter
     */
    public ControlParameter getCognitiveAcceleration() {
        return cognitiveAcceleration;
    }
    
    /*
     * Sets the value for the vmax parameter
     * @param parameter The parameter that vmax must be set to
     */
    public void setVmax(ControlParameter parameter) {
        this.vmax = parameter.getClone();
    }
    
     /*
     * Get the vmax control parameter for the particle
     * @return The vmax parameter
     */
    public ControlParameter getVmax() {
        return vmax;
    }
    
     /*
     * Get the best inertia control parameter so far for the particle
     * @return The best inertia parameter
     */
    public ControlParameter getBestInertia() {
        return bestInertia;
    }
    
    /*
     * Get the best social acceleration control parameter so far for the particle
     * @return The best social acceleration parameter
     */
    public ControlParameter getBestSocialAcceleration() {
        return bestSocialAcceleration;
    }
    
    /*
     * Get the best cognitive acceleration control parameter so far for the particle
     * @return The best cognitive acceleration parameter
     */
    public ControlParameter getBestCognitiveAcceleration() {
        return bestCognitiveAcceleration;
    }
    
    /*
     * Get the best vmax control parameter so far for the particle
     * @return The best vmax parameter
     */
    public ControlParameter getBestVmax() {
        return bestVmax;
    }
    
    /*
     * Sets the best value so far for the inertia parameter
     * @param parameter The parameter that best inertia must be set to
     */
    public void setBestInertia(ControlParameter parameter) {
        bestInertia = parameter;
    }
    
    /*
     * Sets the best value so far for the social acceleration parameter
     * @param parameter The parameter that best social acceleration must be set to
     */
    public void setBestSocialAcceleration(ControlParameter parameter) {
        bestSocialAcceleration = parameter;
    }
    
    /*
     * Sets the best value so far for the cognitive acceleration parameter
     * @param parameter The parameter that best cognitive acceleration must be set to
     */
    public void setBestCognitiveAcceleration(ControlParameter parameter) {
        bestCognitiveAcceleration = parameter;
    }
    
    /*
     * Sets the best value so far for the vmax parameter
     * @param parameter The parameter that best vmax must be set to
     */
    public void setBestVmax(ControlParameter parameter) {
        bestVmax = parameter;
    }
}
