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

/**
 * Method that calculates a gap score by linearly or not scale the number of gaps over all columns.
 * Found in paper from Fogel in his GA for MSA
 *
 * @author Fabien Zablocki
 */
public class GapFogel implements GapPenaltiesMethod {

    private ArrayList<String> alignment;
    private boolean verbose = false;   // default, can be set via XML
    private boolean linearScale = true;  // default, can be set via XML. If false, it justs counts the gaps.

    public void setLinearScale(boolean linearScale) {
        this.linearScale = linearScale;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public double getPenalty(ArrayList<String> alignmentList) {
        this.alignment = alignmentList;
                                                                                                //setFinalAlignment(alignment);
        //    Now modify the fitness based on the formula to penalize gaps and gap groups
        double penalty = 0.0;
        double columnPenalty = 0.0;
        int seqLength1 = alignment.get(0).length();
        int counter = 0;

        //Iterate through the columns
        for (int i = 0; i < seqLength1; i++) {
            //go through all the seqs
            for (String currentString : alignment) {
                if (currentString.charAt(i) != '-') counter++;  // counts the number of symbols (all non-gap characters in that column)
            }
            columnPenalty = alignment.size() - counter;
            if (linearScale) penalty+=columnPenalty*(1+(columnPenalty/alignment.size()));  //add a the linear scale to the column fitness
            else penalty+= columnPenalty;
            columnPenalty = 0;
            counter = 0;
        }

        //    prints the current alignment if verbose on
        if (verbose) {
            System.out.println("Penalty: "+penalty);
            for (String s : alignment) {
                System.out.println("'" + s + "'");
            }
        }
        return penalty;
    }
}
