/*
 * GapGroupGapCreator.java
 *
 * Created on Sep 21, 2004
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

import net.sourceforge.cilib.type.types.Vector;

/**
 * This class implements the second fitness function which represents the gapped version.
 *
 * @author gpampara
 * @author fzablocki
 */
public class GapGroupGapCreator implements GapCreator {
	
	private ScoringMethod theMethod;  //interface for the scoring function
	private ArrayList<String> align;
	
	public void setScoringMethod(ScoringMethod theMethod) {
		this.theMethod = theMethod;
	}
	public double getFitness(Collection<String> alignment, Vector solution, int gapsArray []) {

		Vector tmpSolution = solution.clone();
		
		// Clone the ArrayList in tmp by doing a deep copy
		ArrayList<String> tmp = new ArrayList<String>();
		
		for (Iterator<String> l = alignment.iterator(); l.hasNext(); )
		{
			String s = new String(l.next()); // FIXME: This might break - check
			tmp.add(s);
		}

		// Now calculate the change in representation
		int counter = 0;  //keep track of the ith sequence 
		int start = 0; // stores index of positions in vector
		
		/*for (int j = 0; j < solution.size(); j++)  
		{   
			System.out.println (tmpSolution.getInt(j));  	
		}*/
		
		//System.out.println(" - - - - Start modify solution - - - - ");
		
		//go through all the seqs
		for (ListIterator l = tmp.listIterator(); l.hasNext(); )
		{ 
			int [] dummyArray = new int [gapsArray[counter]];
			int change = 0;  //keep track of how much gaps inserted for that sequence
			
			String s = (String) l.next();  //make that seq a String
			StringBuffer newRepresentation = new StringBuffer(s);  //copy String seq in a easy structure to modify
			
			//System.out.println("*** GAP Positions ***");
			
			//go through #gaps allowed
			for (int i = 0; i < gapsArray[counter]; i++)  
			{   
				dummyArray [i] = tmpSolution.getInt(i+start);  
				//System.out.println("Array elements: "+dummyArray [i]+ " at " +i);	
			}
			
			/*Sort the positions in the vector so we can add gaps always from the root and
			 by just incrementing position by 1 every loop execution.*/
			Arrays.sort( dummyArray );
			
			for (int u = 0; u < gapsArray[counter]; u++)  
			{
				//int position = (int) Math.round(tmpSolution.getReal(i));  //rounds the particule positions
				int position = dummyArray[u];  //gets the particule positions
				//System.out.println("Position without change: "+ position);
				
				if (position > -1)  //-1 means no gaps to be inserted
				{
					position+=change; //advance 
					//System.out.println("Position with change: "+ position);

					if (position >= newRepresentation.length())  //if marker if bigger than length
					{
						newRepresentation.append('-');  //then append a gap at end of seq (perfect for variable seq length)
					}
					else  //marker is in length range
					{
						newRepresentation.insert(position, '-');  //insert gap at that position
						//System.out.println("Inserted ' ' at "+ position );
					}
				
					change++;  //inc the change counter after each addition of gaps
				}
			}
			
			//System.out.println("\n*** END GAP Positions ***");

			tmp.set(counter, newRepresentation.toString());  //stores the modified 'gapped' seq
			//System.out.println("newRep: '" + newRepresentation.toString() + "'"); //display it
			
			start += gapsArray[counter];
			counter++; 
			
			dummyArray = null;
		}
		//System.out.println("- - - - End modify solution - - - -");
		setAlignment(tmp);
		return theMethod.getScore(tmp);
	}
	
	public ArrayList<String> getAlignment() {
		return align;
	}
	public void setAlignment(ArrayList<String> align) {
		this.align = align;
	}
	
	
	
}