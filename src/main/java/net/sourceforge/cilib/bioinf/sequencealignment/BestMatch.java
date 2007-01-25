/*
 * RulesOfFour.java
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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ListIterator;

/**
 * @author fzablocki 
 */
public class BestMatch implements ScoringMethod {
	
	/*
	 * Each matching pair of symbols adds 1 point to the fitness value.
	 * Takes the best value for each column.
	 */

	public double getScore(ArrayList<String> alignment)
	{
		int length = alignment.get(0).length();
		double fitness = 0.0;

		Hashtable<Character, Integer> hashTable = new Hashtable<Character, Integer>();

		//Iterate through the columns, one hashtable is used for each column then cleared every new iteration
		for (int i = 0; i < length; i++) {    // to go through all the char in the seq

			for (ListIterator l = alignment.listIterator(); l.hasNext(); ) {   //go through all the seqs

				String currentString = (String) l.next();   //gets the sequence as a String

				//if (i >= currentString.length()) continue; //skip if index i is longer than current seq length

				Character c = new Character(currentString.charAt(i)); //gets char at position i

				//ignores the spaces or "gaps" in the alignment, skips the rest and the loop goes to next itration
				
				/*WE MIGHT WANT TO COMMENT THAT OUT to also count the aligned the gaps, recommended.
				because we also impose a penalty on gap groups.*/
				//if (c.charValue() == ' ') continue;

				//check if hashtable already has that character
				if (hashTable.containsKey(new Character(currentString.charAt(i)))) {
					Integer count = (Integer) hashTable.get(c);//gets the # of that char in hashtable
					int tmp1 = count.intValue() + 1;//tmp1 = value+1 , just increments occurence #
					hashTable.put(c, tmp1);//put the char back along with new value in hashtable
				}
				else
					//	if it wasn't in yet, then put in hashtable with value 1
					hashTable.put(currentString.charAt(i), 1);
				}

			// Now add the best alignment value from the hashtable to the fitness
				int highest = 0;
			//	cycle through the hastable
				for (Enumeration e = hashTable.elements(); e.hasMoreElements(); ) {

					int tmp2 = ((Integer) e.nextElement()).intValue(); //gets the occurence value
					if (tmp2 > highest)   //we want to get the max value for that column
						highest = tmp2;
				}

				fitness += highest;  //add the current column highest value to the fitness

			//	Clear the hashtable
				hashTable.clear();
			}

			//System.out.println("Fitness without penalty: " + fitness);

		//	prints the current alignment
			for (ListIterator i = alignment.listIterator(); i.hasNext(); ) {
				String s = (String) i.next();
				System.out.println("'" + s + "'");
			}
			return fitness ;
		}
	}
