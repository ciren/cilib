/**
 * Copyright (C) 2003 - 2009
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
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * A scoring function based on the highest number of matches per column.
 * Each matching pair of symbols adds 1 point to the fitness value.
 * Takes the best value for each column.
 *
 * @author Fabien Zablocki
 */
public class BestMatch implements ScoringMethod {
    private boolean verbose = false; //default, can be set via XML

    /**
     * Set the printing verbosity in this ScoringMethod.
     * TODO: This should disappear and log4j should be used instead if it is needed.
     * @param verbose The value of the switch.
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Get the score of the given alignment.
     * @param alignment The alignment to evaluate.
     * @return the alignment score.
     */
    public double getScore(ArrayList<String> alignment) {
        // prints the current raw alignment in verbose mode
        if (verbose) {
            System.out.println("Raw Alignment (no clean up):");

            for (String s : alignment) {
                System.out.println("'" + s + "'");
            }
        }
        /*************************************************************
         *  POST - PROCESSING(CLEAN UP): REMOVE ENTIRE GAPS COLUMNS  *
         *************************************************************/

        int seqLength = alignment.get(0).length();
        int count = 0;

//        Iterate through the columns
        for (int i = 0; i < seqLength; i++) {
             for (String st : alignment) {
                 if (st.charAt(i) == '-') count++; //gets char at position i
             }

             if (count == alignment.size()) { // GOT ONE, PROCEED TO CLEAN UP
                 int which = 0;
                 for (String st1 : alignment) {
                     StringBuilder stB = new StringBuilder(st1);
                     stB.setCharAt(i, '*');
                     alignment.set(which, stB.toString());
                     which++;
                 }
             }
             count = 0;
        }

        int which2 = 0;
        for (String st : alignment) {
            StringTokenizer st1 = new StringTokenizer(st, "*", false);
            String t="";
            while (st1.hasMoreElements()) t += st1.nextElement();
            alignment.set(which2, t);
            which2++;
        }
            /************* END ***************/

        int length = alignment.get(0).length();
        double fitness = 0.0;

        Hashtable<Character, Integer> hashTable = new Hashtable<Character, Integer>();

        // Iterate all the chars one column at a time, one hashtable is used for each column then cleared every new iteration
        for (int i = 0; i < length; i++) {
            //    go through all the seqs
            for (String currentString : alignment) {
                //if (i >= currentString.length()) continue; //skip if index i is longer than current seq length

                Character c = new Character(currentString.charAt(i)); //gets char at position i

                /*
                 * Ignores the gaps in the alignment, skips the rest and the loop goes to next itration
                 * because we also impose a penalty on gap groups.
                 */
                if (c.charValue() == '-') continue;

                //check if hashtable already has that character
                if (hashTable.containsKey(new Character(currentString.charAt(i)))) {
                    Integer count1 = (Integer) hashTable.get(c); //gets the # of that char in hashtable
                    int tmp1 = count1.intValue() + 1; //tmp1 = value+1 , just increments occurence #
                    hashTable.put(c, tmp1); //put the char back along with new value in hashtable
                }
                else
                    //    if it wasn't in yet, then put in hashtable with value 1
                    hashTable.put(currentString.charAt(i), 1);
            }

            // Now add the best alignment value from the hashtable to the fitness
                int highest = 0;
            //    cycle through the hastable
                for (Integer e : hashTable.values()) {
                    //we want to get the max value for that column
                    highest = Math.max(e, highest);
                }

                fitness += highest;  //add the current column highest value to the fitness

            //    Clear the hashtable
                hashTable.clear();
            }

        //  Fitness for matches
        return fitness;
    }
}
