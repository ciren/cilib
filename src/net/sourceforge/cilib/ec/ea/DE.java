/*
 * DE.java
 * 
 * Created on Jul 20, 2005
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
 * 
 */
package net.sourceforge.cilib.ec.ea;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.cilib.ec.crossoveroperators.DifferentialCrossOver;

/**
 * @author otter
 *
 * TODO: Cross and mutation prob (Rate) van EAOLD is nie hier van pas...
 * TODO: Mutator operator is nie van pas hier. 
 * TODO: GenerationGap and the two nextGenerationSelectors is nie van pas hier. 
 * 
 * Breek dit nou jou hierarchy, of nie? Is dit bad practice om seker velde van jou super klas nie te gebruik nie?
 */
public class DE extends EA {
	
	public DE() {
		super();
		this.crossover = new DifferentialCrossOver<Individual>();
	}

	
	/**
	 * Primitive operation to evaluate the fitness of all the individuals within the supplied topology instance.
	 * @param PopoulationTopology
	 */
    //TODO: Wat sal die beste hier wees???
/*    public void performFitnessEvaluation(PopulationTopology pop) {
    	//do nothing. This is because the template method within EA is defined so that the individuals fitness is evaluated at the beginnning
    	//of each iteration. The DE is so that fitness is enforced at the beginning of every druration due to the replacement selection to form
    	//the next generation.
    }*/  
	
	/**
	 * Primitive operation for the cross-over step within the performIteration template method.
	 */
    protected void performCrossOver() {
        //clear the offspring population...
        //this.offspring.makeEmpty();
    	this.offspring.clear();
        Collection<Individual> parents = null;
        Collection<Individual> kiddies = null;

        //for each individual within the population, do:
        Iterator<Individual> iterator = population.iterator();
        while (iterator.hasNext()) {
            Individual individual = (Individual)iterator.next();
            
            //1). Select three other individuals
            parents = this.parentSelector.select(this.population,this.crossover.numberOffParentsNeeded());
            parents.add(individual);
            //2). cross-over. Create the number of children needed...
            kiddies = this.crossover.reproduce(parents);
            
            offspring.addAll(kiddies);
            
            //reset
            parents.clear();
            kiddies.clear();
        }
    }
    
  
    
	/**
	 * Primitive operation for the mutation step within the performIteration template method.
	 */
    protected void performMutation() {
    	//leave empty. No mutation must take place.
    	//This is better than the old way of using a nomutation operator or setting the mutation rate to zero,
    	//because some calculations are done every time but no mutation takes place.
    }
    
	/**
	 * Primitive operation for the next generation formation step within the performIteration template method.
	 */
    protected void performNextGenerationFormation() {
        //TODO: Next generation...    	
        //pre-condition, current population and offspring, must be added to each other, all the fitnesses needs to be evaluated...
        //work out fitness of the current generation.
        super.performFitnessEvaluation(this.population);
        super.performFitnessEvaluation(this.offspring);
    	
        //FIXME: nextGenerationSurvivalSelector and nextGenerationOffspringSelector is not applicable here.
        // Used the nextGenerationSurvivalSelector for varaible purposes here.
        this.population.addAll(this.offspring.getAll());
        
        Collection<Individual> tmp = this.nextGenerationSurvivalSelector.select(this.population,this.offspring.size()); 
        this.population.setAll(tmp);
    }    
}
