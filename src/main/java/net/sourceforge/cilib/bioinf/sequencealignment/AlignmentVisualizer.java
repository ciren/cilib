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

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.StringType;
import net.sourceforge.cilib.type.types.Type;

/**
 * This class serves as a measurement, its puspose is to display the output alignment.
 * Options include the "fullColumns" flag that also displays a star (*) at the bottom of each fully matched columns.
 *
 * @author Fabien Zablocki
 */

public class AlignmentVisualizer implements Measurement {

    private static final long serialVersionUID = -4912115455897639857L;

    private boolean fullColumns = true;  //default, can be set via XML
    private int fullyMatchedColumnCounter;

    public Type getValue(Algorithm algorithm) {
        String s = new String();
        String lineOfStars="";
        fullyMatchedColumnCounter = 0;

        ArrayList<String> as = new ArrayList<String>((((MSAProblem) algorithm.getOptimisationProblem()).getAlignmentCreator()).getAlignment());

        //checks for fully matched columns
        if (fullColumns) {
            int seqLength = as.get(0).length();

            //Iterate through the columns
            for (int i = 0; i < seqLength; i++) {
                boolean full = true;
                char c = as.get(0).charAt(i);

                for (int j = 1; j < as.size(); j++)
                    if (as.get(j).charAt(i) != c) full = false;

                if (full) {
                    lineOfStars+="*";
                    fullyMatchedColumnCounter++;
                }
                else lineOfStars+=" ";
            }
        }

        for (String string : as) {
            s += (string+"\n");
        }

        if (fullColumns)
            s+= lineOfStars+"\n[ Number of fully matched columns: "+fullyMatchedColumnCounter+" ]";
        return new StringType("\n"+s);
    }

    public void setFullColumns(boolean visualiseMatches) {
        this.fullColumns = visualiseMatches;
    }

    public AlignmentVisualizer getClone() {
        return this;
    }

    public String getDomain() {
        return "Z";
    }
}
