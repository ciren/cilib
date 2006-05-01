/*
 * FEP.java
 * 
 * Created on Nov 7, 2005
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
package net.sourceforge.cilib.ec.ea;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.cilib.container.Pair;
import net.sourceforge.cilib.container.SortedList;
import net.sourceforge.cilib.ec.selectionoperators.TournamentSelection;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.RandomNumber;
import net.sourceforge.cilib.type.types.Vector;

/**
 *  
 * @author Gary Pampara
 *
 */
public class FEP extends EA {
	
	private RandomNumber randomNumber;
	
	/**
	 * 
	 *
	 */
	public FEP() {
		super();

		this.parentSelector = new TournamentSelection<Individual>();
		this.randomNumber = new RandomNumber();
	}
	
	
	/**
	 * Perform the initialisation of the algorithm. Create the individuals and initialise
	 * the <code>BehaviouralParameters</code> according to the FEP paper by Xin Yao.
	 */
	protected void performInitialisation() {
		super.performInitialisation();
		
		for (Iterator<Individual> i = this.population.iterator(); i.hasNext(); ) {
			Individual individual = i.next();
			Vector vector = (Vector) individual.getBehaviouralParameters();
			for (int k = 0; k < vector.getDimension(); k++) {
				vector.setReal(k, 3.0);
			}
		}
	}
	
	
	/**
	 * 
	 */
	protected void performIteration() {
		performFitnessEvaluation(population);
		
		performCrossOver(); // Create the offspring
		
		performNextGenerationFormation();
	}
	
	
	/**
	 * 
	 */
	protected void performFitnessEvaluation(Topology<Individual> pop) {
        Iterator<Individual> iterator = pop.iterator();
        while (iterator.hasNext()) {
          Individual individual = iterator.next();
          individual.setFitness(problem.getFitness(individual.get(), false));
        }
    }
	
	
	/**
	 * 
	 */
	protected void performCrossOver() {
		this.offspring.clear();
		
		for (Iterator<Individual> iterator = this.population.iterator(); iterator.hasNext(); ) {
			Individual parent = iterator.next();
			Individual offspring = null;
						
			double t = (1/(Math.sqrt(2*Math.sqrt(parent.getDimension()))));
			double tPrime = 1/(Math.sqrt(2*parent.getDimension()));
			//System.out.println("t: " + t);
			//System.out.println("tPrime: " + tPrime);
			
			offspring = parent.clone();
			
			Vector vector = (Vector) offspring.get();
			Vector parameters = (Vector) offspring.getBehaviouralParameters();
						
			for (int i = 0; i < vector.getDimension(); i++) {
				//System.out.println("cauchy: " + randomNumber.getCauchy());
				//System.out.println("normal: " + randomNumber.getNormal());
				double result = vector.getReal(i) + parameters.getReal(i)*randomNumber.getCauchy();
				vector.setReal(i, result);
				//System.out.println("After: " + vector.getReal(i));
			}
			
			for (int j = 0; j < parameters.getDimension(); j++) {
				//System.out.println("Before: " + parameters.getReal(j));
				//System.out.println(Math.exp(tPrime*randomNumber.getNormal() + t*randomNumber.getNormal()));
				double result = parameters.getReal(j)*Math.exp(tPrime*randomNumber.getNormal() + t*randomNumber.getNormal());
				parameters.setReal(j, result);
				//System.out.println("After: " + parameters.getReal(j));
			}
			
			this.offspring.add(offspring);
		}
		
		performFitnessEvaluation(this.offspring);
	}

	
	/**
	 * 
	 */
	/* This should be abstracted into a selection operator ???? */
	protected void performNextGenerationFormation() {
		SortedList<Pair<Integer, Entity>> list = new SortedList<Pair<Integer, Entity>>(new AscendingOrderComparator());
		
		Topology<Individual> unionPopulation = new StandardPopulation();
		unionPopulation.addAll(this.population.getAll());
		unionPopulation.addAll(this.offspring.getAll());
		
		// Perform the tournament
		for (Iterator<Individual> i = unionPopulation.iterator(); i.hasNext(); ) {
			Individual current = i.next();
			Pair<Integer, Entity> p = new Pair<Integer, Entity>(0, current);
			
			Collection<Individual> parents = this.parentSelector.select(unionPopulation, 10);
			
			for (Iterator<Individual> j = parents.iterator(); j.hasNext(); ) {
				Individual opponent = j.next();
				if (current.compareTo(opponent) > 0) { // Current is more fit than opponent
					p.setKey(p.getKey()+1);
				}
			}
			
			list.add(p);
		}
			
		List<Pair<Integer, Entity>> newParents = list.subList(this.populationSize, this.populationSize*2);
		List<Individual> newParentEntities = new ArrayList<Individual>();
		
		for (Iterator<Pair<Integer, Entity>> i = newParents.iterator(); i.hasNext(); ) {
			Pair<Integer, Entity> p = i.next();
			newParentEntities.add((Individual)p.getValue());
		}
		
		this.population.setAll(newParentEntities);
	}
	
	
	/**
	 * 
	 * @author Gary Pampara
	 */
	private class AscendingOrderComparator implements Comparator<Pair<Integer, Entity>> {
		
		/**
		 * 
		 * @param o1
		 * @param o2
		 * @return
		 */
		public int compare(Pair<Integer, Entity> o1, Pair<Integer, Entity> o2) {
			return o1.getKey().compareTo(o2.getKey());
		}
		
	}
	
}
