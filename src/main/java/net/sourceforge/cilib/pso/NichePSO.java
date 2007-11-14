/*
 * NichePSO.java
 *
 * Created on 13 May 2006
 *
 * Copyright (C) 2003 - 2006 
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
 *
 */
package net.sourceforge.cilib.pso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.LinearDecreasingControlParameter;
import net.sourceforge.cilib.controlparameter.RandomizingControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.pso.niching.AbsorptionStrategy;
import net.sourceforge.cilib.pso.niching.FitnessDeviationCreationStrategy;
import net.sourceforge.cilib.pso.niching.GBestAbsorptionStrategy;
import net.sourceforge.cilib.pso.niching.GBestMergeStrategy;
import net.sourceforge.cilib.pso.niching.MergeStrategy;
import net.sourceforge.cilib.pso.niching.SwarmCreationStrategy;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.pso.particle.initialisation.DomainPercentageVelocityInitialisationStrategy;
import net.sourceforge.cilib.pso.velocityupdatestrategies.GCVelocityUpdateStrategy;
import net.sourceforge.cilib.pso.velocityupdatestrategies.StandardVelocityUpdate;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

import org.apache.log4j.Logger;

/**
 * Implementation of the NichePSO algorithm.
 * 
 * @author Andries Engelbrecht
 * @author Gary Pampara
 */
public class NichePSO extends MultiPopulationBasedAlgorithm {
	private static final long serialVersionUID = 2056933096612146989L;

	private static Logger log = Logger.getLogger(NichePSO.class);
	
	private OptimisationProblem problem;
	private PSO mainSwarm;
	private DistanceMeasure distanceMeasure;
	private MergeStrategy<PSO> mergeStrategy;
	private AbsorptionStrategy<PSO> absorptionStrategy;
	private SwarmCreationStrategy<PSO> swarmCreationStrategy;
	private Particle mainSwarmParticle;
	private Particle subSwarmParticle;
	private double maxima = 1.0d;
	private GCVelocityUpdateStrategy subswarmVelocityUpdateStrategy = null;	
	
	/**
	 * 
	 *
	 */
	public NichePSO() {
		super();
		
		if(subswarmVelocityUpdateStrategy == null)
		    subswarmVelocityUpdateStrategy = new GCVelocityUpdateStrategy();
				
		mainSwarm = new PSO();
		
		distanceMeasure = new EuclideanDistanceMeasure();
		mergeStrategy = new GBestMergeStrategy<PSO>();
		absorptionStrategy = new GBestAbsorptionStrategy<PSO>();
		swarmCreationStrategy = new FitnessDeviationCreationStrategy<PSO>();
		
		mainSwarmParticle = new StandardParticle();
				
		RandomizingControlParameter socialAcceleration = new RandomizingControlParameter();
		socialAcceleration.setControlParameter(new ConstantControlParameter(0.0));
		((StandardVelocityUpdate) mainSwarmParticle.getVelocityUpdateStrategy()).setSocialAcceleration(socialAcceleration);
		
		RandomizingControlParameter cognitiveAcceleration = new RandomizingControlParameter();
		cognitiveAcceleration.setControlParameter(new ConstantControlParameter(1.2));
		((StandardVelocityUpdate) mainSwarmParticle.getVelocityUpdateStrategy()).setCognitiveAcceleration(cognitiveAcceleration);
		
		LinearDecreasingControlParameter inertia = new LinearDecreasingControlParameter();
		inertia.setUpperBound(0.7);
		inertia.setLowerBound(0.2);
		((StandardVelocityUpdate) mainSwarmParticle.getVelocityUpdateStrategy()).setInertiaWeight(inertia);
		
		((StandardVelocityUpdate) mainSwarmParticle.getVelocityUpdateStrategy()).setVMax(new ConstantControlParameter(0.5));
		
		mainSwarmParticle.setVelocityInitialisationStrategy(new DomainPercentageVelocityInitialisationStrategy());
		mainSwarm.getInitialisationStrategy().setEntityType(mainSwarmParticle);
		mainSwarm.addStoppingCondition(new MaximumIterations(Integer.MAX_VALUE));
				
		subSwarmParticle = new StandardParticle();
	}
	
	public NichePSO(NichePSO copy) {
		
	}
	
	public NichePSO clone() {
		return new NichePSO(this);
	}

	
	/**
	 * 
	 */
	public void performInitialisation() {
		mainSwarm.initialise();
	}
	
	
	/**
	 * 
	 */
	@Override
	public void algorithmIteration() {
		log.debug("Beginning iteration");
		log.debug("\tmainSwarm particle #: " + mainSwarm.getTopology().size());
		
		if(mainSwarm.getTopology().size() > 1)
		    mainSwarm.performIteration();
		
		for (Iterator<PopulationBasedAlgorithm> i = this.subPopulationsAlgorithms.iterator(); i.hasNext(); )
		{   		    
			PopulationBasedAlgorithm subSwarm = i.next();
						
			log.debug("\tsubswarm size: " + subSwarm.getTopology().size());
			subSwarm.performIteration();
			
		}
		
		this.mergeStrategy.merge(mainSwarm, this.subPopulationsAlgorithms);
		this.absorptionStrategy.absorb(mainSwarm, this.subPopulationsAlgorithms);
		this.swarmCreationStrategy.create(this);
		
		log.debug("End of iteration");
	}

	
	/**
	 * 
	 */
	@Override
	public int getPopulationSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Topology<? extends Entity> getTopology() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTopology(Topology<? extends Entity> topology) {
		// TODO Auto-generated method stub

	}

	public void setOptimisationProblem(OptimisationProblem problem) {
		this.mainSwarm.setOptimisationProblem(problem);
		this.problem = problem;
	}

	public OptimisationProblem getOptimisationProblem() {
		return this.problem;
	}

	public OptimisationSolution getBestSolution() {
		throw new UnsupportedOperationException("Niching algorithms could possibly not have 1 correct answer. Please call getSolutions()");
	}

	public List<OptimisationSolution> getSolutions() {
		List<OptimisationSolution> solutions = new ArrayList<OptimisationSolution>();
		
		for (Iterator<PopulationBasedAlgorithm> i = this.subPopulationsAlgorithms.iterator(); i.hasNext(); ) {
			PopulationBasedAlgorithm p = i.next();
			solutions.add(p.getBestSolution());
		}
		
		return solutions;
	}
	
	

	public AbsorptionStrategy<PSO> getAbsorptionStrategy() {
		return absorptionStrategy;
	}

	public void setAbsorptionStrategy(AbsorptionStrategy<PSO> absorptionStrategy) {
		this.absorptionStrategy = absorptionStrategy;
	}

	public DistanceMeasure getDistanceMeasure() {
		return distanceMeasure;
	}

	public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
		this.distanceMeasure = distanceMeasure;
	}

	public PSO getMainSwarm() {
		return mainSwarm;
	}

	public void setMainSwarm(PSO mainSwarm) {
		this.mainSwarm = mainSwarm;
	}

	public Particle getMainSwarmParticle() {
		return mainSwarmParticle;
	}

	public void setMainSwarmParticle(Particle mainSwarmParticle) {
		this.mainSwarmParticle = mainSwarmParticle;
	}

	public MergeStrategy<PSO> getMergeStrategy() {
		return mergeStrategy;
	}

	public void setMergeStrategy(MergeStrategy<PSO> mergeStrategy) {
		this.mergeStrategy = mergeStrategy;
	}

	public Particle getSubSwarmParticle() {
		return subSwarmParticle;
	}

	public void setSubSwarmParticle(Particle subSwarmParticle) {
		this.subSwarmParticle = subSwarmParticle;
	}

	public List<PopulationBasedAlgorithm> getSubSwarms() {
		return this.subPopulationsAlgorithms;
	}

	public void setSubSwarms(List<PopulationBasedAlgorithm> subSwarms) {
		this.subPopulationsAlgorithms = subSwarms;
	}

	public SwarmCreationStrategy<PSO> getSwarmCreationStrategy() {
		return swarmCreationStrategy;
	}

	public void setSwarmCreationStrategy(SwarmCreationStrategy<PSO> swarmCreationStrategy) {
		this.swarmCreationStrategy = swarmCreationStrategy;
	}
	
	public Entity getContribution() {
		throw new UnsupportedOperationException("If you want to use this, you will have to implement it yourself");
	}

	public Fitness getContributionFitness() {
		throw new UnsupportedOperationException("If you want to use this, you will have to implement it yourself");
	}

	public void updateContributionFitness(Fitness fitness) {
		throw new UnsupportedOperationException("If you want to use this, you will have to implement it yourself");
	}
	
	public double getMaxima() {
		return maxima;
	}

	public void setMaxima(double maxima) {
		this.maxima = maxima;
	}
	    
	public GCVelocityUpdateStrategy getSubSwarmVelocityUpdateStrategy(){
		return this.subswarmVelocityUpdateStrategy;
	}
	
	public void setParticles(int particles) {
		this.mainSwarm.getInitialisationStrategy().setEntityNumber(particles);
	    
	}

	public int getParticles() {
		return this.mainSwarm.getInitialisationStrategy().getEntityNumber();
	}
	
}
