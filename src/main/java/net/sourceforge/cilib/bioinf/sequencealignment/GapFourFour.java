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
 * Method that penalises gaps.
 *
 * Penalisation is done according to the following formula:
 *            gap groups*4 + total amount of gaps*0.4
 *
 * @author Fabien Zablocki
 */
public class GapFourFour implements GapPenaltiesMethod {

    private boolean verbose = false;
    private ArrayList<String> finalAlignment;

    public void setVerbose(boolean verbose)    {
        this.verbose = verbose;
    }

    public ArrayList<String> getFinalAlignment() {
        return finalAlignment;
    }

    @SuppressWarnings("unchecked")
    private void setFinalAlignment(ArrayList<String> s) {
        finalAlignment = (ArrayList<String>) s.clone();
    }

    public double getPenalty(ArrayList<String> alignment) {
        /*************************************************************
         *  POST - PROCESSING(CLEAN UP): REMOVE ENTIRE GAPS COLUMNS  *
         *************************************************************/

        int seqLength = alignment.get(0).length();
        int count = 0;

        //Iterate through the columns
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
        for (String st : alignment)    {
            StringTokenizer st1 = new StringTokenizer(st, "*", false);
            String t="";
            while (st1.hasMoreElements()) t += st1.nextElement();
            alignment.set(which2, t);
            which2++;
        }
            /************* END ***************/
        setFinalAlignment(alignment);
        //    Now modify the fitness based on the formula to penalise gaps and gap groups
        int totalNumberGaps = 0;
        int gapGroups = 0;

        for (String s : alignment) {
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '-')    {
                    totalNumberGaps++;
                }
            }

            boolean flag = false;
            for (int i = 0; i < s.length()-1; i++) {
                if(s.charAt(i) == '-') {
                    while(i < s.length()-1)    {
                        if (s.charAt(++i) == '-') flag = true;
                        else break;
                    }
                    if (flag) gapGroups++;
                }
                flag = false;
            }
        }

        double gapPenalty = gapGroups*4 + (totalNumberGaps*0.4);

        //    prints the current alignment if verbose on
        if (verbose) {
            System.out.println("Gap Groups: "+gapGroups);
            System.out.println("TotalNumberGaps: "+totalNumberGaps);
            System.out.println("Penalty: "+gapPenalty);

            for (String s : alignment) {
                System.out.println("'" + s + "'");
            }
        }
        return gapPenalty;
    }
}
