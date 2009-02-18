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
package net.sourceforge.cilib.problem.coevolution;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblemAdapter;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Type;

/**
 * @author leo
 * This class is used by any population in a competitive coevolution algorithm. It contains the neccesary
 */
public class CompetitiveCoevolutionProblemAdapter extends OptimisationProblemAdapter {
	//Id of the poulation
	int populationID;
	//domainregistry for the population
	DomainRegistry domain;
	//the coevolution problem
	CoevolutionOptimisationProblem coevolutionProblem;
	//the current evaluation round
	int evaluationRound;
	/**
	 *
	 */
	private static final long serialVersionUID = -6940622506198881027L;
	/**
	 *
	 */
	public CompetitiveCoevolutionProblemAdapter(int populationID, DomainRegistry domain, CoevolutionOptimisationProblem coevolutionProblem) {
		evaluationRound = 0;
		this.populationID = populationID;
		this.domain = domain;
		this.coevolutionProblem = coevolutionProblem;
	}

	public CompetitiveCoevolutionProblemAdapter(CompetitiveCoevolutionProblemAdapter other){
		populationID = other.populationID;
		domain = other.domain;
		coevolutionProblem = other.coevolutionProblem;
		evaluationRound = other.evaluationRound;
	}

	public void incrementEvaluationround(){
		++evaluationRound;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Fitness calculateFitness(Type solution) {
		@SuppressWarnings("unchecked")
		Fitness fitness = coevolutionProblem.evaluateEntity(populationID, evaluationRound, (Blackboard<Enum<?>, Type>) solution);
		return fitness;
	}

	@Override
	public OptimisationProblemAdapter getClone() {
		// TODO Auto-generated method stub
		return new CompetitiveCoevolutionProblemAdapter(this);
	}

	public DomainRegistry getBehaviouralDomain() {
		// TODO Auto-generated method stub
		return domain;
	}

	public DomainRegistry getDomain() {
		// TODO Auto-generated method stub
		return domain;
	}

}
