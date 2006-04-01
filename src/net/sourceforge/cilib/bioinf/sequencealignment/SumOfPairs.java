/*
 * SumOfPairs.java
 *
 * Created on Mar 16, 2006
 *
 * Copyright (C) 2003, 2004, 2005, 2006 - CIRG@UP
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

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * @author fzablocki 
 */
public class SumOfPairs implements ScoringMethod 
{
	/*    SUM OF PAIRS SCORING FUNCTION
	 * 
	 * Score all possible pairwise combinations in column
	 * Requires (N * (N-1))/2 comparisons for N sequences
	 * Total score = sum of score for each column
	 */
	//GapPenaltiesMethod gapPenaltyMethod;
	//ArrayList<String> align = null;
	
//	Change those if you want different score but these are already the standard.
	private final int MATCH = 2;
	private final int MISMATCH = -1;
	private final int GAP_PENALTY = -2;
	
	/*public void setGapPenaltyMethod(GapPenaltiesMethod gapMethod) {
		this.gapPenaltyMethod = gapMethod;
	}*/
	
	public double getScore(ArrayList<String> alignment)
	{
		double fitness = 0.0;
		double columnFitness = 0.0;
		int seqLength = alignment.get(0).length(); 
		Character tmpArray [];
		int counter;
		
		//Iterate through the columns
		for (int i = 0; i < seqLength; i++)
		{ 
			 tmpArray = new Character [alignment.size()];
			 counter = 0;

			 //go through all the seqs
			for (ListIterator l = alignment.listIterator(); l.hasNext(); )
			{   
				String currentString = (String) l.next();   //gets the sequence as a String

				tmpArray[counter] = new Character ( currentString.charAt(i)); //gets char at position i
				//System.out.print(tmpArray[counter]);
				counter++;	
			}	
			
			/* START comparisons*/
			int track = 0;
			for (int h1 = 0; h1 < alignment.size() ; h1++)  //exept the last
			{
				for (int h2 = 1+track; h2 < alignment.size() ; h2++) //starts at 1, not 0
				{
					//MATCH
					if( tmpArray[h1].charValue() == tmpArray[h2].charValue())
					{
						columnFitness+=MATCH;
						//System.out.println("MATCH: "+tmpArray[h1].charValue() +" vs "+ tmpArray[h2].charValue());
					}
					//MISMATCH
					else 
					{	
						if( ! (tmpArray[h1].charValue() == '-' || tmpArray[h2].charValue()== '-'))
						{
							columnFitness+=MISMATCH;
							//System.out.println("MISMATCH: "+tmpArray[h1].charValue() +" vs "+ tmpArray[h2].charValue());
						}
						//GAP_PENALTY
						else
						{ 
							columnFitness+= GAP_PENALTY;
							//System.out.println("GAP_PENALTY: "+tmpArray[h1].charValue() +" vs "+ tmpArray[h2].charValue());
						}
					}
				}
				track++;
			}
			
			fitness+=columnFitness;
			tmpArray = null;
			
			//System.out.println("Fitness for column: "+i+" "+ columnFitness);
			//System.out.println("Fitness so far: " + fitness);
		}
		
		//prints the current alignment
		for (ListIterator j = alignment.listIterator(); j.hasNext(); )
		{
			String s = (String) j.next();
			System.out.println("'" + s + "'");
		}
		
		//System.out.println("Fitness without penalty: " + fitness);
		return fitness;
	}
}
