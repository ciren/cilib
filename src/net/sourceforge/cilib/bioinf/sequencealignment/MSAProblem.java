/*
 * MSAProblemPSO.java
 *
 * Created on Sep 14, 2004
 *
 * Copyright (C) 2003, 2004 - CIRG@UP
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
package net.sourceforge.cilib.bioinf.sequencealignment;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.MaximisationFitness;
import net.sourceforge.cilib.problem.OptimisationProblemAdapter;
import net.sourceforge.cilib.problem.dataset.StringDataSetBuilder;
import net.sourceforge.cilib.type.DomainParser;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Vector;

/**
 * This class represents the Optimization Problem to be solved for the MSA.
 *
 * @author gpampara
 * @author fzablocki
 */
public class MSAProblem extends OptimisationProblemAdapter {
	
	private DomainRegistry domainRegistry;  //will hold the domain, i.e. the solution space is defined
	private Collection<String> strings; //holds the array of input sequences
	private GapCreator gapCreator;  //interface for the fitness function
	private GapPenaltiesMethod gapPenaltyMethod;
	
	private int maxSequenceGapsAllowed;  //FOR GAPPED, AS A STANDARD, SHOULD BE SET TO 20% EXTRA OF THE LENGTH OF SEQUENCE, MIN 2
	private int smallestLength = Integer.MAX_VALUE;  //for init purpose
	private int biggestLength = Integer.MIN_VALUE; //for init purpose
	private int averageLength;  //computed average length of all the input sequences 
	private boolean areGapsAllowed;  //GAPPED (true) or non-GAPPED version (false by default)
	private int gapsArray []; //holds total num of gaps to be inserted
	private int totalGaps;
	private boolean isAggregate;
	
	public MSAProblem() {
		this.domainRegistry = new DomainRegistry();
	}
	
	protected Fitness calculateFitness(Object solution) { //solution = particule position vector
		Vector realValuedPosition = (Vector) solution;
		if (!areGapsAllowed)
		{ 
			double result = gapCreator.getFitness(strings, realValuedPosition, gapsArray);
			System.out.println("Fitness: "+ result);
			return new MaximisationFitness(new Double(result));
		}
		else
		{
			double result1 = gapCreator.getFitness(strings, realValuedPosition, gapsArray);
			double result2 = gapPenaltyMethod.getPenalty(gapCreator.getAlignment());
		//	System.out.println("Fitness for matches: "+result1);
		//	System.out.println("Fitness for gap penalties: "+result2);
			System.out.println("Fitness: "+ (result1-result2));
		
			if (isAggregate)
				return new MaximisationFitness(new Double(result1-result2));
            //CHANGE "MaximisationFitness" WITH THE NEW MO FITNESS below, with vector of fitnesses.
			else return new MaximisationFitness(new Double(result1-result2));
		}
	}

	public void setAreGapsAllowed(boolean areGapsAllowed)
	{
		this.areGapsAllowed = areGapsAllowed;
	}

	public void setGapCreator(GapCreator gapCreator) {
		this.gapCreator = gapCreator;
	}

	public void setIsAggregate(boolean isAggregate) {
		this.isAggregate = isAggregate;
	}

	public void setGapPenaltyMethod(GapPenaltiesMethod gapPenaltyMethod) {
		this.gapPenaltyMethod = gapPenaltyMethod;
	}
	
	// If gaps are allowed, make it a 20% of sequence length (in XML file). Otherwise set it to 1.
	public void setMaxSequenceGapsAllowed(int number) {
		if (number == 0)
			this.maxSequenceGapsAllowed = 1; 
		else{
			//for gapped version, it is the number of gaps allowed.
			this.maxSequenceGapsAllowed = number;
		}
	}

	public DomainRegistry getDomain() {
		if (this.domainRegistry.getDomainString() == null) {
			DomainParser parser = DomainParser.getInstance();
			
			//reads in the input data sets.
			StringDataSetBuilder stringBuilder = (StringDataSetBuilder) this.getDataSetBuilder();
			strings = stringBuilder.getStrings();
	
			for (Iterator<String> i = strings.iterator(); i.hasNext(); ) {
				String result = i.next();
				
				if (result.length() < smallestLength) smallestLength = result.length();
				if (result.length() > biggestLength) biggestLength = result.length();
			}
			
			averageLength = Math.round((float)((smallestLength + biggestLength)/2));
			System.out.println("Average Length: "+averageLength+ ", amount of sequences: "+strings.size()+".");
			
			
			/*
			 * ATTENTION:  An alignment is only valid if all the aligned sequences are of the same length!!!!
			 * MUST DO SOME PREPROCESSING. 
			 */
			 
			gapsArray = new int [strings.size()];
			
			int delta = 0;
			int c = 0;
			for (Iterator<String> i = strings.iterator(); i.hasNext(); ) 
			{
				String aSeq = i.next();
				delta = biggestLength - aSeq.length();
				if (delta > 0) gapsArray[c] = delta+maxSequenceGapsAllowed;
				else gapsArray[c] = maxSequenceGapsAllowed;
				c++;
			}
			
			for (int t = 0; t < strings.size(); t++) totalGaps+=gapsArray[t];
			
			System.out.println("Total gaps to be added: " + totalGaps+".");
			
			String rep = "";
			
			// Non-Gapped version
			if (!areGapsAllowed) { 
				rep = "Z(0.0, " + averageLength + ")^" + strings.size()*maxSequenceGapsAllowed;
			}
			
			//Gapped version
			else
			{
				//System.out.println("Strings size: " + strings.size());
				System.out.println("Recommended number of gaps per seq: " + Math.ceil(0.2 * averageLength) +".");
				
				/*OBSOLETE:
				 * I put -1 instead of 0 for the domain so that if it is -1 it means no gap to insert otherwise it is the gap position.
				 * Since there is a set amount of gaps allowed for all the sequences, it's better no to force having let's say 3 gaps per sequence, rather UP to 3.
				 */
				//representation = "Z(0.0, " + biggestLength + ")^" + strings.size()*maxSequenceGapsAllowed;
				rep = "Z(0.0, " + (biggestLength+maxSequenceGapsAllowed) + ")^" + totalGaps;
			} 
			
	    	parser.parse(rep);
	    	
	    	domainRegistry.setDomainString(rep);
	    	domainRegistry.setExpandedRepresentation(parser.expandDomainString(rep));
	    	domainRegistry.setBuiltRepresenation(parser.getBuiltRepresentation());
		}
				
		return domainRegistry;
	}

	public DomainRegistry getBehaviouralDomain() {
		return null;
	}
}
