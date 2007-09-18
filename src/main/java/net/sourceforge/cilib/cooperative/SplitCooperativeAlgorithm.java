/*
 * SplitCooperativeAglorithm.java
 *
 * Created on January 24, 2003, 11:44 AM
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
package net.sourceforge.cilib.cooperative;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.cooperative.contributionupdatestrategies.ContributionUpdateStrategy;
import net.sourceforge.cilib.cooperative.contributionupdatestrategies.StandardContributionUpdateStrategy;
import net.sourceforge.cilib.cooperative.fitnessupdatestrategies.FitnessUpdateStrategy;
import net.sourceforge.cilib.cooperative.fitnessupdatestrategies.StandardFitnessUpdateStrategy;
import net.sourceforge.cilib.cooperative.splitstrategies.PerfectSplitStrategy;
import net.sourceforge.cilib.cooperative.splitstrategies.SplitStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.problem.CooperativeOptimisationProblemAdapter;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationSolution;

/**
 * TODO test this class This class forms that basis for any co-operative optimisation
 * implementations. Any algorithm that wishes to participate in a co-operative optimisation
 * algorithm must implement the {@link ParticipatingAlgorithm} interface. This class also implements
 * {@link ParticipatingAlgorithm} meaning that co-operative algorithms can be composed of
 * co-operative algorithms again.
 * @author Edwin Peer
 * @author Theuns Cloete
 */
public class SplitCooperativeAlgorithm extends MultiPopulationBasedAlgorithm implements ParticipatingAlgorithm {
	private static final long serialVersionUID = 2287798336228462889L;

	protected CooperativeEntity context = null;
	protected SplitStrategy splitStrategy = null;
	// protected PopulationIterator populationIterator = null;
	protected FitnessUpdateStrategy fitnessUpdateStrategy = null;
	protected ContributionUpdateStrategy contributionUpdateStrategy = null;

	/**
	 * Creates a new instance of a SplitCooperativeAglorithm
	 */
	public SplitCooperativeAlgorithm() {
		super();
		context = new CooperativeEntity();
		splitStrategy = new PerfectSplitStrategy();
		fitnessUpdateStrategy = new StandardFitnessUpdateStrategy();
		contributionUpdateStrategy = new StandardContributionUpdateStrategy();
	}

	public SplitCooperativeAlgorithm(SplitCooperativeAlgorithm copy) {
		super(copy);
		context = copy.context.clone();
		splitStrategy = copy.splitStrategy;
		fitnessUpdateStrategy = copy.fitnessUpdateStrategy;
		contributionUpdateStrategy = copy.contributionUpdateStrategy;
	}

	@Override
	public SplitCooperativeAlgorithm clone() {
		return new SplitCooperativeAlgorithm(this);
	}

	@Override
	public void reset() {
		super.reset();
		context.reset();
	}

	@Override
	public OptimisationSolution getBestSolution() {
		return new OptimisationSolution(optimisationProblem, context.getContents().clone());
	}

	@Override
	public List<OptimisationSolution> getSolutions() {
		ArrayList<OptimisationSolution> solutions = new ArrayList<OptimisationSolution>(1);
		solutions.add(getBestSolution());
		return solutions;
	}

	public CooperativeEntity getContext() {
		return context;
	}

	public void setContext(CooperativeEntity c) {
		context = c;
	}

	@Override
	public void addPopulationBasedAlgorithm(PopulationBasedAlgorithm algorithm) {
		if (!(algorithm instanceof ParticipatingAlgorithm))
			throw new IllegalArgumentException("The given Algorithm is not a ParticipatingAlgorithm");
		subPopulationsAlgorithms.add(algorithm);
	}

	public int getNumberOfParticipants() {
		return subPopulationsAlgorithms.size();
	}

	public SplitStrategy getSplitStrategy() {
		return splitStrategy;
	}

	public void setSplitStrategy(SplitStrategy split) {
		splitStrategy = split;
	}

	public ContributionUpdateStrategy getContributionUpdateStrategy() {
		return contributionUpdateStrategy;
	}

	public void setContributionUpdateStrategy(ContributionUpdateStrategy contributionUpdate) {
		contributionUpdateStrategy = contributionUpdate;
	}

	public Entity getContribution() {
		return context;
	}

	public Fitness getContributionFitness() {
		return context.getFitness();
	}

	/**
	 * The purpose of this method should not be confused with the ContributionUpdateStrategy. This
	 * method sets the fitness for the context of the cooperating algorithm, i.e. the fitness for all
	 * cooperating algorithms as a whole.
	 */
	public void updateContributionFitness(Fitness fitness) {
		context.setFitness(fitness);
	}

/*	public Iterator<Algorithm> iterator() {
		if(populationIterator == null)
			throw new InitialisationException("The PopulationIterator has not been initialised yet.");
		return populationIterator.clone();
	}

	public Iterator getPopulationtIterator() {
		return populationIterator;
	}

	public void setPopulationIterator(PopulationIterator iterator) {
		if(subPopulationsAlgorithms == null)
			throw new InitialisationException("The populations (ArrayList<Algorithms>) have not been initialised yet.");
		populationIterator = iterator;
		populationIterator.setPopulations(subPopulationsAlgorithms);
	}
*/
	public FitnessUpdateStrategy getFitnessUpdateStrategy() {
		return fitnessUpdateStrategy;
	}

	public void setFitnessUpdateStrategy(FitnessUpdateStrategy fitnessUpdate) {
		this.fitnessUpdateStrategy = fitnessUpdate;
	}

	// @Initialiser
	// QUESTION are initialisations (or initialisers) still deprecated? Should we use @Initialiser
	// here instead?
	@Override
	public void performInitialisation() {
		context.setContents(optimisationProblem.getDomain().getBuiltRepresenation().clone());
		splitStrategy.split(optimisationProblem, context, subPopulationsAlgorithms);
		context.reset();
		for (Algorithm participant : subPopulationsAlgorithms) {
			participant.performInitialisation();
			context.append(((ParticipatingAlgorithm) participant).getContribution());
		}
	}

	@Override
	public void performUninitialisation() {
		// DataSetBuilder dataset = optimisationProblem.getDataSetBuilder();
		// ((ClusterableDataSet)dataset).assign((MixedVector)context.get());
		// dataset.uninitialise((MixedVector)context.get());
	}

	@Override
	public void algorithmIteration() {
		for (Algorithm population : this) {
			population.performIteration();
			CooperativeOptimisationProblemAdapter participantProblem = (CooperativeOptimisationProblemAdapter) population.getOptimisationProblem();
			participantProblem.updateContext(context);
			ParticipatingAlgorithm participantAlgorithm = (ParticipatingAlgorithm) population;
			contributionUpdateStrategy.updateContribution(participantAlgorithm.getContribution(), 0, context, participantProblem.getOffset(), participantProblem.getDimension());
			fitnessUpdateStrategy.updateFitness(optimisationProblem, context);
		}
	}

	@Override
	public int getPopulationSize() {
		return subPopulationsAlgorithms.size();
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
	public void setTopology(Topology<? extends Entity> topology) {
		// TODO Auto-generated method stub
	}
}
