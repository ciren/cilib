/*
 * SequenceShiftGapCreator.java
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
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ListIterator;

import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Vector;

/**
 * This class implements the first fitness function which represents the no gaps version.
 *
 * @author gpampara
 * @author fzablocki
 */
public class SequenceShiftGapCreator implements GapCreator {
												
	private ArrayList<String> align;
																			//never used
	public double getFitness(Collection<String> alignment, Vector solution, int sequenceGapsAllowed []) {

		Vector tmpSolution = solution.clone();
		ArrayList<String> tmp = new ArrayList<String>();// will hold all the sequences

		/* Clone the ArrayList in tmp by doing a deep copy */
		for (Iterator<String> i = alignment.iterator(); i.hasNext(); )
		{
			String s = new String(i.next());
			tmp.add(s);
		}

		/*
		 * Now perform the needed changes to the temporary cloned data based on the position vector
		 * by placing "gap" characters in front on the specified sequence.
		 *
		 * Creates the alignment according to the rounded particle positions for that solution.
		 * Sort of mapping between an alignment and the corresponding particules.
		 * It basically puts a number of gaps (determined by the rounded particle positions) in
		 * front of the sequences.
		 */

		Vector position = new MixedVector(tmpSolution.getDimension());  //new double array with length of tmpSolution
		//System.out.println("*** GAP Positions ***");
		for (int i = 0; i < tmpSolution.getDimension(); i++)// all the seqs
		{
			position.add(new Real(Math.round(tmpSolution.getReal(i)))); // FIXME //copy all values, after rounding, in position
			//System.out.println(position.getReal(i)+ " ");

			String tmpStr = (String) tmp.get(i);
			for (int j = 0; j < position.getReal(i); j++)
				tmpStr = "-" + tmpStr; //prepend gaps

			tmp.set(i, tmpStr);
		}
		//System.out.println("*** END GAP Positions ***");

		int longest = 0;

		//gets the longest sequence
		for (ListIterator l = tmp.listIterator(); l.hasNext(); )
		{
			String string = (String) l.next();
			if (string.length() > longest)
				longest = string.length();
		}

		/* Determine (column-wise) the most dominating character and add the sum of that character's
		 * occurrences in the column to the final fitness value (a hash table structure is used to store
		 * the association between the characters and the current count of the specific characters).
		 * In the event of a gap character being located within a column, the gap is regarded as a
		 * character which adds a total fitness of 0 to the column fitness.
		 **/

		double fitness = 0.0;
		Hashtable<Character, Integer> set = new Hashtable<Character, Integer>();

		// Iterate through the columns
		for (int i = 0; i < longest; i++)  // to go through all the char in the seq
		{
			for (ListIterator l = tmp.listIterator(); l.hasNext(); ) //go through all the seqs
			{
				String currentString = (String) l.next();  //gets the sequence as a String

				if (i >= currentString.length()) continue; //skip if index i is longer than current seq length

				Character c = new Character(currentString.charAt(i)); //gets char at position i

				//ignores the spaces or "gaps" in the alignment, skips the rest and the loop goes to next
				if (c.charValue() == ' ' || c.charValue() == '-' ) continue;

				//check if hashtable already has that character
				if (set.containsKey(currentString.charAt(i)))
				{
					Integer count = (Integer) set.get(c); //gets the # of that char in hashtable
					int tmp1 = count.intValue() + 1; //tmp1 = value+1 , just increments occurence #
					set.put(c, tmp1);  //put the char back along with new value in hashtable
				}
				else
					//if it wasn't in yet, then put in hashtable with position 1
					set.put(currentString.charAt(i), 1);
			}

			// Now add the best alignment value in the hashtable to the fitness
			int highest = 0;
			//cycle through the hastable
			for (Enumeration e = set.elements(); e.hasMoreElements(); )
			{
				int tmp2 = ((Integer) e.nextElement()).intValue();  //gets the occurence value

				if (tmp2 > highest)  //we want to get the max value for that column
					highest = tmp2;
			}

			fitness += highest;  //add the current column highest value to the fitness

			// Clear the hashtable
			set.clear();
		}

		//System.out.println("Current Fitness: " + fitness);

		//prints the current alignment
		for (ListIterator i = tmp.listIterator(); i.hasNext(); )
		{
			String s = (String) i.next();
			System.out.println(s);
		}
		setAlignment(tmp);
		return fitness;
	}
	
	public ArrayList<String> getAlignment() {
		return align;
	}
	public void setAlignment(ArrayList<String> align) {
		this.align = align;
	}
}