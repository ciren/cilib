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

/**
 * This class implements a technique for pairwise sequence alignment known as Dynamic Programming.
 * It always gives the best possible alignment from 2 input sequences.
 *
 * @author Fabien Zablocki
 *
 */
public class DynamicProgramming {

    private String dnaX, dnaY;
    private int[][] opt, opt2;
    int n, m, distance, counter;
    int penalty;

    private static final int GAP_PENALTY = 2;
    private static final int MISMATCH_PENALTY = 1;
    private static final int MATCH_PENALTY = 0;

    public void setSequences(String a, String b) {
        dnaX = a;
        m = dnaX.length();

        dnaY = b;
        n = dnaY.length();
    }

    public void doJob() {
        opt = new int[m+1][n+1];
        System.out.println("->Lengths of DNA segments to align: "+ m + ", "+ n+".");

        init();
        editDistance();  // Just gives the distance
        recoverAlignment();  //Display optimal alignment
        lcs();
        displayStat();  //needs LCS
    }

//    computes minimum of three values
    static int min3(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }

//     computes base values
    void init()    {
        //System.out.print("->Initializing matrix base values...");

           int v=2;
          for (int i = m-1; i >= 0; i--) {
            opt[i][n]+=v;
            v+=2;
        }

        int w=2;
        for (int i = n-1; i >= 0; i--) {
            opt[m][i]+=w;
            w+=2;
        }

        //System.out.println("done.");
    }

    void editDistance() {
        //System.out.print("->Computing optimal alignment...");

        for (int i = m-1; i >= 0; i--)
            for (int j = n-1; j >= 0; j--) {
                penalty = (dnaX.charAt(i) == dnaY.charAt(j)) ? MATCH_PENALTY : MISMATCH_PENALTY;
                opt[i][j] = min3(opt[i][j+1]+GAP_PENALTY, opt[i+1][j]+GAP_PENALTY, opt[i+1][j+1]+penalty);
            }

        //System.out.println("done.");
        System.out.println("Edit Distance: " + opt[0][0]+"\n");
    }

//    recover optimal alignment by backtracing
    void recoverAlignment() {
        System.out.println("->Recovering optimal alignment...");
        String seq1 = "", seq2 = "", consensus = "";

        int i = 0, j = 0, minimum;
        while(i < m && j < n) {
            minimum = min3(opt[i+1][j+1], opt[i+1][j], opt[i][j+1]);

            if (opt[i][j] == minimum || opt[i][j] == minimum+MISMATCH_PENALTY) {
                penalty = (dnaX.charAt(i) == dnaY.charAt(j)) ? MATCH_PENALTY : MISMATCH_PENALTY;
                //System.out.println( dnaX.charAt(m)+" "+ dnaY.charAt(n)+" "+ penalty);     // VERTICAL DISPLAY
                seq1+=dnaX.charAt(i);
                seq2+=dnaY.charAt(j);
                consensus+=penalty;
                //move diag
                i++;
                j++;
            }
            else if (opt[i][j] == minimum+GAP_PENALTY && opt[i][j] == opt[i+1][j] + GAP_PENALTY) {
                //System.out.println( dnaX.charAt(m)+" - "+ GAP_PENALTY);   // VERTICAL DISPLAY
                seq1+=dnaX.charAt(i);
                seq2+="-";
                consensus+=GAP_PENALTY;
                //advance down
                i++;
            }
            else if (opt[i][j] == minimum+GAP_PENALTY && opt[i][j] == opt[i][j+1] + GAP_PENALTY) {
                //System.out.println("- "+ dnaY.charAt(m) + " " + GAP_PENALTY);  // VERTICAL DISPLAY
                seq1+="-";
                seq2+=dnaY.charAt(j);
                consensus+=GAP_PENALTY;
                //advance right
                j++;
            }
        }

        System.out.println(seq1);
        System.out.println(seq2);
        System.out.println(consensus);
    }

//     recover LCS itself and print it to standard output
    void lcs() {
        opt2 = new int[m+1][n+1];
        counter = 0;
        System.out.println("->Computing Longest Common Sequence (LCS)...");

        // compute length of LCS and all subproblems via dynamic programming
         for (int i = m-1; i >= 0; i--) {
             for (int j = n-1; j >= 0; j--) {
                if (dnaX.charAt(i) == dnaY.charAt(j))
                   opt2[i][j] = opt2[i+1][j+1] + 1;
               else
                  opt2[i][j] = Math.max(opt2[i+1][j], opt2[i][j+1]);
            }
         }

        // recover LCS itself and print it to standard output (Longest Common Sequence)
        int i = 0, j = 0;
        while(i < m && j < n) {
            if (dnaX.charAt(i) == dnaY.charAt(j)) {
                 System.out.print(dnaX.charAt(i));
                 counter++;
                i++;
                j++;
            }
            else if (opt2[i+1][j] >= opt2[i][j+1]) i++;
            else                                   j++;
        }
        System.out.println("\nSize of LCS: "+counter);
    }

    void displayStat() {
        double percentage = (counter*100)/((n+m)/2);
        System.out.println("Percentage of similarity: "+ percentage + "%.");
    }
}
