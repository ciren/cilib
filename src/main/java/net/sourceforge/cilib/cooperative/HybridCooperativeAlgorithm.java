package net.sourceforge.cilib.cooperative;

import java.util.List;

import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;

public class HybridCooperativeAlgorithm extends MultiPopulationBasedAlgorithm implements ParticipatingAlgorithm {
	private static final long serialVersionUID = 4908040536174924734L;

	public HybridCooperativeAlgorithm() {
		super();
	}
	
	public HybridCooperativeAlgorithm(HybridCooperativeAlgorithm copy) {
		
	}
	
	public HybridCooperativeAlgorithm clone() {
		return new HybridCooperativeAlgorithm(this);
	}

	public OptimisationSolution getBestSolution() {
		// TODO Auto-generated method stub
		return null;
	}

	public OptimisationProblem getOptimisationProblem() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<OptimisationSolution> getSolutions() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setOptimisationProblem(OptimisationProblem problem) {
		// TODO Auto-generated method stub

	}

	public Entity getContribution() {
		// TODO Auto-generated method stub
		return null;
	}

	public Fitness getContributionFitness() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getParticipation() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setParticipation(boolean participation) {
		// TODO Auto-generated method stub

	}

	public void updateContributionFitness(Fitness fitness) {
		// TODO Auto-generated method stub

	}

	@Override
	public void performIteration() {
		// TODO Auto-generated method stub
		
	}

	public boolean participated() {
		// TODO Auto-generated method stub
		return false;
	}

	public void participated(boolean participation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getDiameter() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPopulationSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getRadius() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Topology<? extends Entity> getTopology() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPopulationSize(int populationSize) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTopology(Topology topology) {
		// TODO Auto-generated method stub
		
	}
}
