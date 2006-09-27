/*
 * ES.java
 * 
 * Sep 30, 2005 5:07:44 PM
 * 
 * Copyright (C) 2003, 2004, 2005 - CIRG@UP 
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
package net.sourceforge.cilib.ec.ea;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.cilib.controlparameterupdatestrategies.ConstantUpdateStrategy;
import net.sourceforge.cilib.controlparameterupdatestrategies.ControlParameterUpdateStrategy;
import net.sourceforge.cilib.entity.Topology;

/**
 * @author otter
 * 
 * General Evolutionary Strategies algorithm
 * 
 * TODO check all die copying semantics en so aan, maak proof of concept...
 */
@Deprecated
public class ES extends EA {
	
	protected ControlParameterUpdateStrategy lambda;
	protected boolean parentsAsWell;	//usage of the next generation selection. Select best out of offspring or offspring and parents???
    
	
	public ES() {
		//init class members with suitable defaults.
		//this.prototypeIndividual = new ESIndividual();
		this.mutationProbability = new ConstantUpdateStrategy(1.0);
		this.crossProbability = new ConstantUpdateStrategy(1.0);
		this.lambda = new ConstantUpdateStrategy();
		this.parentsAsWell = false;
	}
	
	protected void performInitialisation() {
		super.performInitialisation();
		this.performFitnessEvaluation(this.population);
	}
	
	protected void performIteration() {
		//no fitness eval in beginning for population, this is enforced thanks to the way the algorithm structure is defined.
		this.performCrossOver();
		this.performMutation();
		//this.performFitnessEvaluation(this.offspring); dont need to do this is done for us by the ASMutationDecorator which must be used
		//in order to obtain the accept mutation only when succesful functionality.
		this.performNextGenerationFormation();
	}
	
    public ControlParameterUpdateStrategy getLambda() {
        return lambda;
    }
    public void setLambda(ControlParameterUpdateStrategy lambda) {
        this.lambda = lambda;
    }
    public boolean isParentsAsWell() {
        return parentsAsWell;
    }
    public void setParentsAsWell(boolean parentsAsWell) {
        this.parentsAsWell = parentsAsWell;
    }

	/**
	 * Primitive operation to evaluate the fitness of all the individuals within the supplied topology instance.
	 * @param PopoulationTopology
	 * This implementation overrides that of EA. Thus is because the genetic part must be extracted first
	 * from the representation (containing the strategy parameters as well).
	 */
    protected void performFitnessEvaluation(Topology<Individual> pop) {
        Iterator<Individual> iterator = pop.iterator();
        while (iterator.hasNext()) {
          Individual individual = iterator.next();
          //hier moet net die genetic gedeelte gepass word...
         // System.out.println("GEN"+individual.getGeneticParams());
          individual.setFitness(problem.getFitness(individual.get(), false));
        }    
    }
    
	/**
	 * Primitive operation for the cross-over step within the performIteration template method.
	 */
    protected void performCrossOver() {
        this.offspring.clear();
        Collection<Individual> parents = null;
        Collection<Individual> kiddies = null;
        
        //cross-over. Create the number(lambda) of children needed...
		for(int l=0; l < Double.valueOf(lambda.getParameter()).intValue(); l++) {
        	//select parents. The number of parents needed can be retrieved from the cross-over operator,
            //which has been either configured to local(2 parents) or global(the whole population)
            if(this.crossover.numberOffParentsNeeded() == 2) {  //local cross-over
                parents = this.parentSelector.select(this.population,this.crossover.numberOffParentsNeeded());                
            }
            else {  //global cross-over
                parents = this.population.getAll();
            }
            // based on a probabilty perform cross-over to generate the new offspring.
            if (this.random.nextDouble() < this.crossProbability.getParameter()) {
            	kiddies = this.crossover.reproduce(parents);
            	offspring.addAll(kiddies);
            }
		}    	
    }
    
	/**
	 * Primitive operation for the cross-over step within the performIteration template method.
	 */
    protected void performMutation() {
        //System.out.println(this.offspring.getSize());
        //this.printChildren();
		//as almal gemutate moet word maak dan 'n mutation rate 1.0.
		//mutation, only accepted in event of success, use the ASMutationDecorator in the configuration XML file.
        for(int i = 0; i < this.offspring.size(); i++) {
            if(random.nextDouble() < this.mutationProbability.getParameter()) {
            	this.mutator.mutate(this.offspring.get(i));
        	}
        }    	
    }

	/**
	 * Primitive operation for the cross-over step within the performIteration template method.
	 */
    protected void performNextGenerationFormation() {
        //calculate all the offspring fitnesses, so that the use of the Elitism selection for 
        //next generation selection can work correctly, take note the fitness of the initial parents have been evaluated within the initialisation.
        this.performFitnessEvaluation(this.offspring);
		
		//perform next generation selection...
        if(this.parentsAsWell) {        //add the parents to the kids... (mu+lamda)ES-selection
        	this.offspring.addAll(this.population.getAll());
        }
        //else do (mu,lamda)ES-selection
        
        //use the elistism operator, as configured within the XML file to choose the best individuals up to the size of the population.
        List<Individual> tmp = this.nextGenerationSurvivalSelector.select(this.offspring,this.populationSize); 
        this.population.setAll(tmp);    	
    }
}

