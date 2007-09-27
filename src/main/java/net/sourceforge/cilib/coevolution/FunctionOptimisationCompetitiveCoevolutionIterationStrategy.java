/*
 * FunctionOptimisationCompetitiveCoevolutionIterationStrategy.java
 * 
 * Created on 2007/04/27
 *
 * Copyright (C) 2003, 2007 - CIRG@UP 
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
package net.sourceforge.cilib.coevolution;

import java.util.List;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameterupdatestrategies.ConstantControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.pso.velocityupdatestrategies.StandardVelocityUpdate;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * exemple of concrete CompetitiveCoevolutionIterationStrategy
 * @author Julien Duhain
 */
public class FunctionOptimisationCompetitiveCoevolutionIterationStrategy extends CompetitiveCoevolutionIterationStrategy {
	private static final long serialVersionUID = 4256055145511342878L;

	public FunctionOptimisationCompetitiveCoevolutionIterationStrategy() {
		super();
	}
	
	public FunctionOptimisationCompetitiveCoevolutionIterationStrategy(FunctionOptimisationCompetitiveCoevolutionIterationStrategy copy) {
		super(copy);
	}
	
	public FunctionOptimisationCompetitiveCoevolutionIterationStrategy clone() {
		return new FunctionOptimisationCompetitiveCoevolutionIterationStrategy(this);
	}
	
	/**
	 * @param ent Entity competing
	 * @param opponents list of opponents the entity will compete against. In most cases the list contains only one element
	 * @param ca CoevolutionAlgorithm
	 *    
	 */
	public void compete(Entity ent, List<Entity> opponents, CoevolutionAlgorithm ca) {
		StandardParticle stPart = (StandardParticle)ent;
	
		Fitness entityFitness = stPart.getFitnessCalculator().getFitness(stPart.getPosition(), false);
    	if (entityFitness.compareTo(stPart.getFitnessCalculator().getFitness(stPart.getBestPosition(), false)) > 0) {
    		//stPart.setBestFitness(entityFitness);
    		stPart.getProperties().put("bestFitness", entityFitness);
    		stPart.getProperties().put("bestPosition", stPart.getPosition().clone());
    	}
		
		//stPart.setBestFitness(entityFitness);
    	stPart.getProperties().put("bestFitness", entityFitness);
		
		for(Entity entity : opponents) {
			StandardParticle opp = (StandardParticle) entity;
			Fitness opponentFitness = opp.getFitnessCalculator().getFitness(opp.getPosition(), false);
			EuclideanDistanceMeasure edm = new EuclideanDistanceMeasure();
			//System.out.println("PURE distance: " + edm.distance(stPart.getPosition(), opp.getPosition()));
			stPart.getProperties().put("distance", new Real(((Real)(stPart.getProperties().get("distance"))).getReal() + edm.distance(stPart.getPosition(), opp.getPosition())));
			//System.out.println("distance: " + stPart.getProperties().get("distance"));
			//do not update the opponent's score so that every particles compete the same numbet of times
			if(entityFitness.compareTo(opponentFitness)>0){ 
				((CoevolutionEntityScoreboard)(stPart.getProperties().get("board"))).win(opp, ca.getIterations());
			}
			else if(entityFitness.compareTo(opponentFitness)==0){
				((CoevolutionEntityScoreboard)(stPart.getProperties().get("board"))).draw(opp, ca.getIterations());
			}
			else{
				((CoevolutionEntityScoreboard)(stPart.getProperties().get("board"))).lose(opp, ca.getIterations());
			}
		}
	}
	
	public void reset(Entity e) {
		((CoevolutionEntityScoreboard)(e.getProperties().get("board"))).reset();
		((Real)e.getProperties().get("distance")).setReal(0);
	}

	/**
	 * Set the correct Entytype you want to use in your experiment 
	 * @param pba PopulationBasedAlgorithm whose entity will be set to the correct entity type
	 */
	public void setEntityType(PopulationBasedAlgorithm pba, int populationID) {
		StandardParticle sp = new StandardParticle();
		sp.getProperties().put("board", new CoevolutionEntityScoreboard());
		sp.getProperties().put("populationID", new Int(populationID));
		sp.getProperties().put("distance", new Real(0.0));
		StandardVelocityUpdate svu = new StandardVelocityUpdate();
		svu.setCognitiveAcceleration(new ConstantControlParameter(0));
		sp.setVelocityUpdateStrategy(svu);
		PSO currentAlgorithm = (PSO)pba;
		currentAlgorithm.getInitialisationStrategy().setEntityType(sp.clone());
	}
	
}
