package net.sourceforge.cilib.pso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.cilib.algorithm.OptimisationAlgorithm;
import net.sourceforge.cilib.algorithm.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameterupdatestrategies.ConstantUpdateStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.pso.niching.AbsorptionStrategy;
import net.sourceforge.cilib.pso.niching.FitnessDeviationCreationStrategy;
import net.sourceforge.cilib.pso.niching.GBestAbsorptionStrategy;
import net.sourceforge.cilib.pso.niching.GBestMergeStrategy;
import net.sourceforge.cilib.pso.niching.MergeStrategy;
import net.sourceforge.cilib.pso.niching.SwarmCreationStrategy;
import net.sourceforge.cilib.pso.parameterupdatestrategies.AccelerationUpdateStrategy;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.pso.velocityupdatestrategies.StandardVelocityUpdate;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * 
 * @author engel
 *
 */
public class NichePSO extends PopulationBasedAlgorithm implements OptimisationAlgorithm {
	
	private OptimisationProblem problem;
	private PSO mainSwarm;
	private Collection<PSO> subSwarms;
	private DistanceMeasure distanceMeasure;
	private MergeStrategy<PSO> mergeStrategy;
	private AbsorptionStrategy<PSO> absorptionStrategy;
	private SwarmCreationStrategy<PSO> swarmCreationStrategy;
	private Particle mainSwarmParticle;
	private Particle subSwarmParticle;

	
	/**
	 * 
	 *
	 */
	public NichePSO() {
		mainSwarm = new PSO();
		subSwarms = new ArrayList<PSO>();
		
		distanceMeasure = new EuclideanDistanceMeasure();
		mergeStrategy = new GBestMergeStrategy<PSO>();
		absorptionStrategy = new GBestAbsorptionStrategy<PSO>();
		swarmCreationStrategy = new FitnessDeviationCreationStrategy<PSO>();
		
		mainSwarmParticle = new StandardParticle();
		StandardVelocityUpdate velocityUpdate = new StandardVelocityUpdate();
		AccelerationUpdateStrategy socialAcceleration = new AccelerationUpdateStrategy();
		socialAcceleration.setParameterUpdateStrategy(new ConstantUpdateStrategy(0.0));
		velocityUpdate.setSocialAcceleration(socialAcceleration);
		mainSwarmParticle.setVelocityUpdateStrategy(velocityUpdate);
		
		subSwarmParticle = new StandardParticle();
		
		mainSwarm.setPrototypeParticle(mainSwarmParticle);
	}

	
	/**
	 * 
	 */
	protected void performInitialisation() {
		mainSwarm.initialise();
	}
	
	
	/**
	 * 
	 */
	@Override
	protected void performIteration() {
		mainSwarm.performIteration();
		
		for (Iterator<PSO> i = this.subSwarms.iterator(); i.hasNext(); ) {
			PSO subSwarm = i.next();	
			subSwarm.performIteration();
		}
		
		//this.subSwarms = this.mergeStrategy.merge(this.subSwarms);
		this.mergeStrategy.merge(this.subSwarms);
		this.absorptionStrategy.absorb(mainSwarm, subSwarms);
		
		//this.subSwarms.addAll(this.swarmCreationStrategy.create(mainSwarm, subSwarms));
		//this.swarmCreationStrategy.create(mainSwarm, subSwarms, null);
		
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
	public void setPopulationSize(int populationSize) {
		// TODO Auto-generated method stub

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

	@Override
	public double getDiameter() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public double getRadius() {
		return 0;
	}

	public void setOptimisationProblem(OptimisationProblem problem) {
		this.mainSwarm.setOptimisationProblem(problem);
		this.problem = problem;		
	}

	public OptimisationProblem getOptimisationProblem() {
		return this.problem;
	}

	public OptimisationSolution getBestSolution() {
		//return new OptimisationSolution(mainSwarm.getOptimisationProblem(), mainSwarm.getBestParticle().getPosition());
		throw new UnsupportedOperationException("Niching algorithms do not have 1 correct answer. Please call getSolutions()");
	}

	public Collection<OptimisationSolution> getSolutions() {
		Collection<OptimisationSolution> solutions = new ArrayList<OptimisationSolution>();
		
		for (Iterator<PSO> i = subSwarms.iterator(); i.hasNext(); ) {
			PSO p = i.next();
			solutions.add(p.getBestSolution());
		}
		
		return solutions;
	}
	
	

	public AbsorptionStrategy getAbsorptionStrategy() {
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

	public MergeStrategy getMergeStrategy() {
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

	public Collection<PSO> getSubSwarms() {
		return subSwarms;
	}

	public void setSubSwarms(Collection<PSO> subSwarms) {
		this.subSwarms = subSwarms;
	}

	public SwarmCreationStrategy getSwarmCreationStrategy() {
		return swarmCreationStrategy;
	}

	public void setSwarmCreationStrategy(SwarmCreationStrategy<PSO> swarmCreationStrategy) {
		this.swarmCreationStrategy = swarmCreationStrategy;
	}
	
}
