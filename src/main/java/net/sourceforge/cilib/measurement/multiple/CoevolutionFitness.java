	/*
	 * CoevolutionFitness.java
	 *
	 * Created on July 18, 2007, 2:45 PM
	 *
	 * 
	 * Copyright (C) 2003 - 2007 
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
package net.sourceforge.cilib.measurement.multiple;

import java.util.ListIterator;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.CoevolutionAlgorithm;
import net.sourceforge.cilib.coevolution.CoevolutionEntityScoreboard;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;

	/**
	 * measurement that prints the number of victory of the best Entity 
	 * @author  Julien Duhain
	 */
public class CoevolutionFitness implements Measurement{
	private static final long serialVersionUID = 2724661105974842752L;

	/** Creates a new instance of Fitness */
	public CoevolutionFitness() {
		
	}
	    
	public CoevolutionFitness(CoevolutionFitness copy) {
	
	}
	    
	public CoevolutionFitness clone() {
		return new CoevolutionFitness(this);
	}

	public String getDomain() {
		return "Z";
	}
	    
	public Type getValue() {
		CoevolutionAlgorithm ca = (CoevolutionAlgorithm)(Algorithm.get());
		int bestscore = 0;
		for(ListIterator it=ca.getPopulations().listIterator(); it.hasNext();){
			PopulationBasedAlgorithm currentAlgorithm = (PopulationBasedAlgorithm)it.next();
			for(int i=0; i<currentAlgorithm.getPopulationSize(); i++){
				Entity e = currentAlgorithm.getTopology().get(i);
				int score = ((CoevolutionEntityScoreboard)(e.getProperties().get("board"))).getWinCount();
				if(bestscore < score){
					bestscore = score;
				}
			}
		}
					
		return new Real(bestscore);
	}
	    
}
