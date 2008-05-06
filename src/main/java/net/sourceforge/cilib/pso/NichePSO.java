/*
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.pso;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.LinearDecreasingControlParameter;
import net.sourceforge.cilib.controlparameter.RandomizingControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.pso.iterationstrategies.PerElementReinitialisation;
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
		mainSwarm.getIterationStrategy().setBoundaryConstraint(new PerElementReinitialisation());
				
		subSwarmParticle = new StandardParticle();
	}

	/**
	 * Copy constructor. Creates a copy of the given object.
	 * @param copy The instance to copy.
	 */
	public NichePSO(NichePSO copy) {
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public NichePSO getClone() {
		return new NichePSO(this);
	}

	
	/**
	 * {@inheritDoc}
	 */
	public void performInitialisation() {
		mainSwarm.initialise();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void algorithmIteration() {
		log.debug("Beginning iteration");
		log.debug("\tmainSwarm particle #: " + mainSwarm.getTopology().size());
		
		if(mainSwarm.getTopology().size() > 1)
		    mainSwarm.performIteration();
		
		this.swarmCreationStrategy.create(this);
		for (PopulationBasedAlgorithm subSwarm : this.subPopulationsAlgorithms) {   		    
			log.debug("\tsubswarm size: " + subSwarm.getTopology().size());
			subSwarm.performIteration();
		}
		
//		this.absorptionStrategy.absorb(mainSwarm, this.subPopulationsAlgorithms);
		this.mergeStrategy.merge(mainSwarm, this.subPopulationsAlgorithms);
				
		log.debug("End of iteration");
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPopulationSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Topology<? extends Entity> getTopology() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTopology(Topology<? extends Entity> topology) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	public void setOptimisationProblem(OptimisationProblem problem) {
		this.mainSwarm.setOptimisationProblem(problem);
		this.problem = problem;
	}

	/**
	 * {@inheritDoc}
	 */
	public OptimisationProblem getOptimisationProblem() {
		return this.problem;
	}

	/**
	 * {@inheritDoc}
	 */
	public OptimisationSolution getBestSolution() {
		throw new UnsupportedOperationException("Niching algorithms could possibly not have 1 correct answer. Please call getSolutions()");
	}

	/**
	 * {@inheritDoc}
	 */
	public List<OptimisationSolution> getSolutions() {
		List<OptimisationSolution> solutions = new ArrayList<OptimisationSolution>();
		
		for (PopulationBasedAlgorithm p : this.subPopulationsAlgorithms) {
			solutions.add(p.getBestSolution());
		}
		
		return solutions;
	}
	

	/**
	 * Get the current {@linkplain AbsorptionStrategy}.
	 * @return The current {@linkplain AbsorptionStrategy}.
	 */
	public AbsorptionStrategy<PSO> getAbsorptionStrategy() {
		return absorptionStrategy;
	}

	/**
	 * Set the {@linkplain AbsorptionStrategy}.
	 * @param absorptionStrategy The value to set.
	 */
	public void setAbsorptionStrategy(AbsorptionStrategy<PSO> absorptionStrategy) {
		this.absorptionStrategy = absorptionStrategy;
	}

	/**
	 * Get the current {@linkplain DistanceMeasure}.
	 * @return The current {@linkplain DistanceMeasure}.
	 */
	public DistanceMeasure getDistanceMeasure() {
		return distanceMeasure;
	}

	/**
	 * Set the {@linkplain DistanceMeasure}.
	 * @param distanceMeasure The value to set.
	 */
	public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
		this.distanceMeasure = distanceMeasure;
	}

	/**
	 * Get the main swarm {@linkplain PSO}.
	 * @return The main swarm {@linkplain PSO}.
	 */
	public PSO getMainSwarm() {
		return mainSwarm;
	}

	/**
	 * Set the main swarm {@linkplain PSO}.
	 * @param mainSwarm The value to set.
	 */
	public void setMainSwarm(PSO mainSwarm) {
		this.mainSwarm = mainSwarm;
	}

	/**
	 * Get the main swarm {@linkplain Particle}.
	 * @return The main swarm {@linkplain Particle}.
	 */
	public Particle getMainSwarmParticle() {
		return mainSwarmParticle;
	}

	/**
	 * Set the main swarm {@linkplain Particle}.
	 * @param mainSwarmParticle The value to set.
	 */
	public void setMainSwarmParticle(Particle mainSwarmParticle) {
		this.mainSwarmParticle = mainSwarmParticle;
	}

	/**
	 * Get the current {@linkplain MergeStrategy}.
	 * @return The current {@linkplain MergeStrategy}.
	 */
	public MergeStrategy<PSO> getMergeStrategy() {
		return mergeStrategy;
	}

	/**
	 * Set the {@linkplain MergeStrategy}.
	 * @param mergeStrategy The value to set.
	 */
	public void setMergeStrategy(MergeStrategy<PSO> mergeStrategy) {
		this.mergeStrategy = mergeStrategy;
	}

	/**
	 * Get the sub swarm {@linkplain Particle}.
	 * @return The sub swarm {@linkplain Particle}.
	 */
	public Particle getSubSwarmParticle() {
		return subSwarmParticle;
	}

	/**
	 * Set the sub swarm {@linkplain Particle}.
	 * @param subSwarmParticle The value to set.
	 */
	public void setSubSwarmParticle(Particle subSwarmParticle) {
		this.subSwarmParticle = subSwarmParticle;
	}

	/**
	 * Get the list of sub swarms.
	 * @return The list of sub swarms.
	 */
	public List<PopulationBasedAlgorithm> getSubSwarms() {
		return this.subPopulationsAlgorithms;
	}

	/**
	 * Set the list of sub swarms.
	 * @param subSwarms The value to set.
	 */
	public void setSubSwarms(List<PopulationBasedAlgorithm> subSwarms) {
		this.subPopulationsAlgorithms = subSwarms;
	}

	/**
	 * Get the sub swarm {@linkplain SwarmCreationStrategy}.
	 * @return The current {@linkplain SwarmCreationStrategy}. 
	 */
	public SwarmCreationStrategy<PSO> getSwarmCreationStrategy() {
		return swarmCreationStrategy;
	}

	/**
	 * Set the {@linkplain SwarmCreationStrategy}.
	 * @param swarmCreationStrategy The value to set.
	 */
	public void setSwarmCreationStrategy(SwarmCreationStrategy<PSO> swarmCreationStrategy) {
		this.swarmCreationStrategy = swarmCreationStrategy;
	}

	/**
	 * Get the maxima.
	 * @return The value of the maxima.
	 */
	public double getMaxima() {
		return maxima;
	}

	/**
	 * Set the value of the maxima.
	 * @param maxima The vaue to set.
	 */
	public void setMaxima(double maxima) {
		this.maxima = maxima;
	}
	    
	/**
	 * Get the sub swarm {@linkplain VelocityUpdateStrategy}.
	 * @return The {@linkplain VelocityUpdateStrategy} for the sub swarms.
	 */
	public GCVelocityUpdateStrategy getSubSwarmVelocityUpdateStrategy(){
		return this.subswarmVelocityUpdateStrategy;
	}
	
	/**
	 * Set the number of particles.
	 * @param particles The value to set.
	 */
	public void setParticles(int particles) {
		this.mainSwarm.getInitialisationStrategy().setEntityNumber(particles);
	}

	/**
	 * Get the number of particles.
	 * @return The number of particles.
	 */
	public int getParticles() {
		return this.mainSwarm.getInitialisationStrategy().getEntityNumber();
	}
	
}
