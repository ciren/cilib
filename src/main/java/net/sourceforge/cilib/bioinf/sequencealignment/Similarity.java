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
 *    SIMILARITY SCORING FUNCTION
 *
 * Score all possible pairwise combinations in column
 * Requires (N * (N-1))/2 comparisons for N sequences
 * Total score = sum of score for each column
 *
 * SoP routine has been optimized for speed.
 *
 * SHOULD BE USED WITHOUT ADDED GAP PENALTY METHOD (weight2 = 0) BECAUSE IT ALREADY HAS A GAP PENALTY
 * OR
 * SET THE GAP_PENALTY TO 0 and use a gap penalty method
 *
 * @author fzablocki
 */
public class Similarity implements ScoringMethod {
//    Change those in xml configuration if you want different scores, but these are already the standard.
    private int match = 2;  //reward for a match
    private int mismatch = 0; // no reward
    private int gapPenalty = -1;  //  penalty for a gap, even when (-,-) which is not considered as a match

    private boolean verbose = false;  //default, can be set via XML
    private boolean weight = false;   //default, can be set via XML

    public void setWeight(boolean weight) {
        this.weight = weight;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void setMatch(int match) {
        this.match = match;
    }

    public void setMismatch(int mismatch) {
        this.mismatch = mismatch;
    }

    public void setGapPenalty(int gapPenalty) {
        this.gapPenalty = gapPenalty;
    }

    public double getScore(ArrayList<String> alignment)    {
        //    prints the current alignment in verbose mode
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

        //    Iterate through the columns
        for (int i = 0; i < seqLength; i++)    {
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
            String t = "";
            while (st1.hasMoreElements()) t += st1.nextElement();
            alignment.set(which2, t);
            which2++;
        }
            /************* END ***************/

        double fitness = 0.0;
        double pairwiseFitness = 0.0;
        int seqLength1 = alignment.get(0).length();

        if (weight) {
            double matchC = 0;
            double mismC = 0;

            //go through all the seqs (rows) SUM OF PAIRS (N * (N-1) /2)
            for (int j = 0; j < alignment.size()-1; j++) {
                for (int k = j+1; k < alignment.size(); k++) {
                    String seq1 = (String) alignment.get(j);   //gets the first sequence as a String
                    String seq2 = (String) alignment.get(k);   //gets the second sequence

                //    go through all columns, pairwise conmparison
                    for (int i = 0; i < seqLength1; i++) {
                        //                    MATCH
                        if (seq1.charAt(i) == seq2.charAt(i) &&
                            //CONSIDER GAP MATCHES AS A GAP PENALTY, so discard them with
                            !(seq1.charAt(i) == '-' && seq2.charAt(i) == '-')) {
                            pairwiseFitness+=match;
                            matchC++;
                        }
                    //    MISMATCH
                        else {
                            if (!(seq1.charAt(i) == '-' || seq2.charAt(i) == '-')) {
                                pairwiseFitness+=mismatch;
                                mismC++;
                            }
                            //GAP_PENALTY
                            else
                                pairwiseFitness+= gapPenalty;
                        }
                    }

                    fitness+=pairwiseFitness / (1 + (matchC/(matchC+mismC)));
                    matchC=0;
                    mismC=0;
                    pairwiseFitness = 0;
                }
            }
        }
        else {
//            go through all the seqs (rows) SUM OF PAIRS (N * (N-1) /2)
            for (int j = 0; j < alignment.size()-1; j++) {
                for (int k = j+1; k < alignment.size(); k++) {
                    String seq1 = (String) alignment.get(j);   //gets the first sequence as a String
                    String seq2 = (String) alignment.get(k);   //gets the second sequence

                //    go through all columns, pairwise conmparison
                    for (int i = 0; i < seqLength1; i++) {
                        //                    MATCH
                        if (seq1.charAt(i) == seq2.charAt(i) &&
                            //CONSIDER GAP MATCHES AS A GAP PENALTY, so discard them with
                            !(seq1.charAt(i) == '-' && seq2.charAt(i) == '-'))
                            pairwiseFitness+=match;
                    //    MISMATCH
                        else {
                            if(!(seq1.charAt(i) == '-' || seq2.charAt(i) == '-'))
                                pairwiseFitness+=mismatch;
                            //GAP_PENALTY
                            else
                                pairwiseFitness+= gapPenalty;
                        }
                    }

                    fitness+=pairwiseFitness;
                    pairwiseFitness = 0;
                }
            }
        }
        //     Fitness for matches:
        return fitness;
    }
}
