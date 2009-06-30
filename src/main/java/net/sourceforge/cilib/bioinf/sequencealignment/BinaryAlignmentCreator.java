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
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This class is responsible for creating an output alignment by populating the input alignment
 *  with gaps at positions given by the discretization of the particles positions.
 * Works with 4 bits encoding in conjonction with BinaryMSAProblem.
 *
 * @author Fabien Zablocki
 */

public class BinaryAlignmentCreator {
    private ScoringMethod theMethod;  //interface for the scoring function (BLOSUM, PAM , SumOfPairs, BestMatch)
    private ArrayList<String> align;
    private boolean justEvaluate;

    public double getFitness(Collection<String> alignment, Vector solution, int [] gapsArray) {
        //take 4 bits at a time and convert back to int
        java.util.Vector <Integer> tmpSolution = new java.util.Vector<Integer>();

        String buff = "";
        for (int k = 0; k < solution.size(); k+=4) { //experimental on 4 bits, must change to x bits to accomodata larger alignment
            for (int l = 0; l < 4; l++) {
                buff += ""+solution.get(k+l);
            }
            tmpSolution.add(new Integer(((int) buff.charAt(0)-48) *8 + ((int) buff.charAt(1)-48) *4 + ((char) buff.charAt(2)-48)*2 + ((char) buff.charAt(3)-48)*1));
            buff = "";
        }

        // Clone the ArrayList in tmp by doing a deep copy
        ArrayList<String> tmp = new ArrayList<String>();

        for (Iterator<String> l = alignment.iterator(); l.hasNext();) {
            String s = new String(l.next());
            tmp.add(s);
        }

        if (!justEvaluate) {
            // Now calculate the change in representation
            int counter = 0;  //keep track of the ith sequence
            int start = 0; // stores index of positions in vector

            // - - - - Start modify solution - - - -

            // First go through all the seqs
            for (String s : tmp) {
                int [] dummyArray = new int [gapsArray[counter]];
                int change = 0;  //keep track of how much gaps inserted for that sequence

                StringBuilder newRepresentation = new StringBuilder(s);  //copy String seq in a easy structure to modify

                // *** GAP Positions ***
                // Then go through #gaps allowed
                for (int i = 0; i < gapsArray[counter]; i++)
                    dummyArray [i] = (int) Math.round(tmpSolution.elementAt(i+start));

                /*Sort the positions in the vector so we can add gaps always from the root (original input sequence) and
                 by just incrementing position by 1 every loop execution.*/
                Arrays.sort(dummyArray);

                for (int u = 0; u < gapsArray[counter]; u++) {
                    int position = dummyArray[u];  //gets the particule positions

                    if (position > -1) {
                        position+=change; //advance

                        if (position >= newRepresentation.length()) {
                            newRepresentation.append('-');  //then append a gap at end of seq (perfect for variable seq length)
                        }
                        else { //marker is in original length range
                            newRepresentation.insert(position, '-');  //insert gap at that position
                        }

                        change++;  //inc the change counter after each addition of gaps
                    }
                }

                //*** END GAP Positions ***

                tmp.set(counter, newRepresentation.toString());  //stores the modified 'gapped' sequence
                //System.out.println("newRep: '" + newRepresentation.toString() + "'"); //display it for debug

                start += gapsArray[counter];
                counter++;

                dummyArray = null;
            }
        }
        //- - - - End modify solution - - - -
        setAlignment(tmp);

        return theMethod.getScore(tmp);
    }

    public void setScoringMethod(ScoringMethod theMethod) {
        this.theMethod = theMethod;
    }

    public ScoringMethod getTheMethod() {
        return theMethod;
    }

    public ArrayList<String> getAlignment() {
        return align;
    }

    public void setAlignment(ArrayList<String> align) {
        this.align = align;
    }

    public void setJustEvaluate(boolean justEvaluate) {
        this.justEvaluate = justEvaluate;
    }
}
