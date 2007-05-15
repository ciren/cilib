/*
 * BLOSUMScoringProtein.java
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
 * This class implements a scoring function to align
 * protein in a biologically meaningful manner.
 * BLOSUM (BLOcks SUbstitution Matrix) [Henikoff + 1992]. This is needed because amino acids have varying properties.
 * It uses the well known BLOSUM62 (pointwise mutations in similar proteins) scoring
 * matrix (amino acid substitution) in order to rate matches. 
 * BLOSUM is designed to find conserved regions of proteins.
 * 
 *@author fzablocki
 */
public class BLOSUMScoringProtein implements ScoringMethod {
	
	private static final char letters [] = {'A','R','N','D','C','Q','E','G','H','I','L','K','M','F','P','S','T','W','Y','V'};
	private static final int sub [][] = {    // A R N . . .
										/*A*/ { 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
										/*R*/ {-1, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
										/*.*/ {-2, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
										/*.*/ {-2,-2, 1, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
											  { 0,-3,-3,-3, 9, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
											  {-1, 1, 0, 0,-3, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
											  {-1, 0, 0, 2,-4, 2, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
											  { 0,-2, 0,-1,-3,-2,-2, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
											  {-2, 0, 1,-1,-3, 0, 0,-2, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
											  {-1,-3,-3,-3,-1,-3,-3,-4,-3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
											  {-1,-2,-3,-4,-1,-2,-3,-4,-3, 2, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
											  {-1, 2, 0,-1,-3, 1, 1,-2,-1,-3,-2, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0},
											  {-1,-1,-2,-3,-1, 0,-2,-3,-2, 1, 2,-1, 5, 0, 0, 0, 0, 0, 0, 0, 0},
											  {-2,-3,-3,-3,-2,-3,-3,-3,-1, 0, 0,-3, 0, 6, 0, 0, 0, 0, 0, 0, 0},
											  {-1,-2,-2,-1,-3,-1,-1,-2,-2,-3,-3,-1,-2,-4, 7, 0, 0, 0, 0, 0, 0},
											  { 1,-1, 1, 0,-1, 0, 0, 0,-1,-2,-2, 0,-1,-2,-1, 4, 0, 0, 0, 0, 0},
											  { 0,-1, 0,-1,-1,-1,-1,-2,-2,-1,-1,-1,-1,-2,-1, 1, 5, 0, 0, 0, 0},
											  {-3,-3,-4,-4,-2,-2,-3,-2,-2,-3,-2,-3,-1, 1,-4,-3,-2,11, 0, 0, 0},
											  {-2,-2,-2,-3,-2,-1,-2,-3, 2,-1,-1,-2,-1, 3,-3,-2,-2, 2, 7, 0, 0},
											  { 0,-3,-3,-3,-1,-2,-2,-3,-3, 3, 1,-2, 1,-1,-2,-2, 0,-3,-1, 4, 0},
											  };

	private int lookup(char prot1, char prot2)
	{
		int pos1 =0 , pos2 = 0;
		
		//first find the corresponding letter with position in array
		for(int i = 0; i < letters.length; i++ )
		{
			if (letters[i] == prot1) pos1 = i; 
		    if (letters[i] == prot2) pos2 = i;
		}
		
		//if gaps, ignore and return neutral score: 0
		if (prot1 == '-' || prot2 == '-') return 0;
		
		//swap if bigger
		if( pos2 > pos1)
		{
			//System.out.println(" -->Swapped: "+pos2 +" with "+pos1);
			//System.out.println(prot1 +" and "+prot2+ " score: "+sub[pos2][pos1]);
			return sub[pos2][pos1];
		}
		//System.out.println(prot1 +" and "+prot2+ " Positions1 and 2: "+pos1+" "+pos2+" score: "+sub[pos1][pos2]);
		return sub[pos1][pos2];
	}
	
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
						columnFitness+=lookup(tmpArray[h1].charValue(), tmpArray[h2].charValue());
				}
				track++;
			}
			
			fitness+=columnFitness;
			tmpArray = null;
			
			//System.out.println("Fitness for column: "+i+" "+ columnFitness);
			//System.out.println("Fitness so far: " + fitness);
		}
		
//		prints the current alignment
		for (ListIterator i = alignment.listIterator(); i.hasNext(); ) {
			String s = (String) i.next();
			System.out.println("'" + s + "'");
		}
		return fitness /*- gapPenaltyMethod.getPenalty(alignment)*/ ;
	}
}

