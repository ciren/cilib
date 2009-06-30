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
import java.util.StringTokenizer;

/**
 * A scoring function based on the overall score for the number of matched symbols
 * over all columns.
 * Each matching pair of symbols adds 1 point to the fitness value.
 * The number of matches in each column is linearly scaled(such that any column that was fully matched up was doubled).
 * Fitness found in paper from Fogel in his GA for MSA.
 *
 * @author Fabien Zablocki
 */
public class MatchFogel implements ScoringMethod {
    private boolean verbose = false;  //default, can be set via XML
    private boolean linearScale = true;  // default, can be set via XML

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void setLinearScale(boolean linearScale) {
        this.linearScale = linearScale;
    }

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
        for (int i = 0; i < seqLength; i++)    {
            try{
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
            catch(StringIndexOutOfBoundsException e) {
                e.getMessage();
                //System.out.println("i :"+i);
                }
        }


        int which2 = 0;
        for (String st : alignment)    {
            StringTokenizer st1 = new StringTokenizer(st, "*", false);
            String t="";

            while (st1.hasMoreElements()) t += st1.nextElement();
            alignment.set(which2, t);
            which2++;
        }
            /************* END ***************/

        double fitness = 0.0;
        double columnFitness = 0.0;
        int seqLength1 = alignment.get(0).length();
        char [] tmpArray;
        int counter;

        //Iterate through the columns
        for (int i = 0; i < seqLength1; i++) {
            tmpArray = new char [alignment.size()];
            counter = 0;

            //go through all the seqs
            for (String currentString : alignment) {
                tmpArray[counter] = currentString.charAt(i); //gets a entire column of chars at position i in the alignment
                counter++;
            }

            /* START comparisons*/
            int track = 0;
            for (int h1 = 0; h1 < alignment.size(); h1++) {  //exept the last
                for (int h2 = 1+track; h2 < alignment.size(); h2++) { //starts at 1, not 0
                    //MATCH
                    if(tmpArray[h1] == tmpArray[h2] &&
                    //CONSIDER GAP MATCHES AS A GAP PENALTY, so discard/ignore them with
                        !(tmpArray[h1] == '-' && tmpArray[h2]== '-')
                      ) columnFitness++;
                }
                track++;
            }
            /* END COMPARISON*/

            if (linearScale) fitness+=columnFitness*(1+(columnFitness/alignment.size()));  //add a the linear scale to the column fitness
            else fitness+= columnFitness;
            columnFitness = 0;
            tmpArray = null;
        }
        //  Fitness for matches
        return fitness;
    }
}
