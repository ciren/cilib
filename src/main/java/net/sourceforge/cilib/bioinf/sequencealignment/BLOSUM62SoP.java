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
 * This class implements the Sum Of Pairs scoring function that uses the BLOSUM62 substitutions matrices
 *  that give scores for proteins in a biologically meaningful manner.
 *
 *  SoP routine has been optimized for speed.
 *
 *@author Fabien Zablocki
 */
public class BLOSUM62SoP implements ScoringMethod {

    private boolean verbose = false;   //default, can be set via XML
    private boolean weight = false;    //default, can be set via XML
    private static final char [] AMINO_ACID = {'A', 'R', 'N', 'D', 'C', 'Q', 'E', 'G', 'H', 'I', 'L', 'K', 'M', 'F', 'P', 'S', 'T', 'W', 'Y', 'V', '-'};

    /* BLOSUM (BLOcks SUbstitution Matrix) [Henikoff + 1992]. This is needed because amino acids have varying properties.
     * It uses the well known BLOSUM62 (pointwise mutations in similar proteins) scoring
     * matrix (amino acid substitution) in order to rate matches.
     * BLOSUM is designed to find conserved regions of proteins.
     */

    // Substitution matrix with BLOSUM scores for all the possible pairs of protein
    private static final short [][] BLOSUM62_SUB = {// A R N . . .
                                        /*A*/ {4,   0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 0},
                                        /*R*/ {-1,  5,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 0},
                                        /*.*/ {-2,  0,  6,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 0},
                                        /*.*/ {-2, -2,  1,  6,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 0},
                                              {0,  -3, -3, -3,  9,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 0},
                                              {-1,  1,  0,  0, -3,  5,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 0},
                                              {-1,  0,  0,  2, -4,  2,  5,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 0},
                                              {0,  -2,  0, -1, -3, -2, -2,  6,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 0},
                                              {-2,  0,  1, -1, -3,  0,  0, -2,  8,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 0},
                                              {-1, -3, -3, -3, -1, -3, -3, -4, -3,  4,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 0},
                                              {-1, -2, -3, -4, -1, -2, -3, -4, -3,  2,  4,  0,  0,  0,  0,  0,  0,  0,  0,  0, 0},
                                              {-1,  2,  0, -1, -3,  1,  1, -2, -1, -3, -2,  5,  0,  0,  0,  0,  0,  0,  0,  0, 0},
                                              {-1, -1, -2, -3, -1,  0, -2, -3, -2,  1,  2, -1,  5,  0,  0,  0,  0,  0,  0,  0, 0},
                                              {-2, -3, -3, -3, -2, -3, -3, -3, -1,  0,  0, -3,  0,  6,  0,  0,  0,  0,  0,  0, 0},
                                              {-1, -2, -2, -1, -3, -1, -1, -2, -2, -3, -3, -1, -2, -4,  7,  0,  0,  0,  0,  0, 0},
                                              {1,  -1,  1,  0, -1,  0,  0,  0, -1, -2, -2,  0, -1, -2, -1,  4,  0,  0,  0,  0, 0},
                                              {0,  -1,  0, -1, -1, -1, -1, -2, -2, -1, -1, -1, -1, -2, -1,  1,  5,  0,  0,  0, 0},
                                              {-3, -3, -4, -4, -2, -2, -3, -2, -2, -3, -2, -3, -1,  1, -4, -3, -2, 11,  0,  0, 0},
                                              {-2, -2, -2, -3, -2, -1, -2, -3,  2, -1, -1, -2, -1,  3, -3, -2, -2,  2,  7,  0, 0},
                                              {0,  -3, -3, -3, -1, -2, -2, -3, -3,  3,  1, -2,  1, -1, -2, -2,  0, -3, -1,  4, 0},
                                              {-4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, -4, 1},
                                              };

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void setWeight(boolean weight) {
        this.weight = weight;
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
                        if (seq1.charAt(i) == '-' || seq2.charAt(i) == '-') continue;

                        if (seq1.charAt(i) == seq2.charAt(i)) matchC++;
                        else mismC++;

                        short pos1 = -1 , pos2 = -1;
//                        first find the corresponding letter with position in array
                        for (short p = 0; p < AMINO_ACID.length; p++) {
                            if (AMINO_ACID[p] == seq1.charAt(i)) pos1 = p;
                            if (AMINO_ACID[p] == seq2.charAt(i)) pos2 = p;
                        }

                    //    swap if bigger
                        if (pos2 > pos1) pairwiseFitness+= BLOSUM62_SUB[pos2][pos1];
                        else pairwiseFitness+= BLOSUM62_SUB[pos1][pos2];
                    }

                    fitness+=pairwiseFitness  / (1 + (matchC/(matchC+mismC)));
                    pairwiseFitness = 0;
                    matchC=0;
                    mismC=0;
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
                        //gaps will be penalized with the gap penalty method in use, even when it is a gap match they suppose to score 0 so it's fine to ignore them.
                        if (seq1.charAt(i) == '-' || seq2.charAt(i) == '-') continue;

                        short pos1 = -1 , pos2 = -1;
//                        first find the corresponding letter with position in array
                        for (short p = 0; p < AMINO_ACID.length; p++) {
                            if (AMINO_ACID[p] == seq1.charAt(i)) pos1 = p;
                            if (AMINO_ACID[p] == seq2.charAt(i)) pos2 = p;
                        }

                    //    swap if bigger
                        if (pos2 > pos1) pairwiseFitness+= BLOSUM62_SUB[pos2][pos1];
                        else pairwiseFitness+= BLOSUM62_SUB[pos1][pos2];
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
