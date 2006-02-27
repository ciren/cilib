/*
 * RNAInitialiser.java
 * 
 * Created on 2005/08/17
 *
 * Copyright (C) 2003, 2005 - CIRG@UP 
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
package net.sourceforge.cilib.bioinf.rnaprediction;

import net.sourceforge.cilib.math.random.MersenneTwister;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Type;


/**
 * @author mneethling
 *
 */
public class RNAInitialiser {

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.PSO.Initialiser#getInitialPosition(net.sourceforge.cilib.Problem.OptimisationProblem)
	 */
	public Type getInitialPosition(OptimisationProblem problem) {
		RNAConformation position = new RNAConformation();
		int rand;
		int added = 0;
		MersenneTwister r = new net.sourceforge.cilib.math.random.MersenneTwister();
		//
		if (NucleotideString.getInstance().getNucleotideString().length() == 0)
			System.out.println("RNAInitialiser.getInitialPosition(): Nucleotide String not yet initialised!");
		int percentage = StemGenerator.getInstance().getAllStems().size() / 5;
		//System.out.println("Initialising particles with "+ percentage + " stems.");
		
		for (int count = 0; count < percentage; count++ ) {	
			boolean conflicts = false;
			StemGenerator generator = StemGenerator.getInstance();
			//System.out.println("StemGenerator: " + generator.getAllStems());
			rand = r.nextInt(generator.getAllStems().size());
			RNAStem newStem = generator.getAllStems().get(rand);
			for (RNAStem stem : position) {
				if (newStem.conflictsWith(stem)) {
					conflicts = true;
					break;
				}				
			}
			if (!conflicts) {
				position.add(newStem);		
				added++;
			}
		}
		//System.out.println("Added "+ added + "stems to inital particle!");
		//should not be invalid - just to check.
		position.isInvalid();
		//System.out.println("Initial Stem: "+new String(position.getRepresentation()));
		//System.out.println();
		return position;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.PSO.Initialiser#getInitialVelocity(net.sourceforge.cilib.Problem.OptimisationProblem)
	 */
	public Type getInitialVelocity(OptimisationProblem problem) {
		MixedVector mv = new MixedVector();
		mv.add(new RNAConformation());
		mv.add(new RNAConformation());
		return mv;				
	}

}
