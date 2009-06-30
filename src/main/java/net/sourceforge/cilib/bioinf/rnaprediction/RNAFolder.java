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
package net.sourceforge.cilib.bioinf.rnaprediction;


/**
 * @author mneethling
 * This is an abstract class (Strategy design pattern) that takes care of 'folding'
 * a given conformation into a new/different conformation. Several methods to do
 * this is possible, the simplest being a greedy algorithm that first adds stems
 * that has the greatest effect on the fitness of the particle. Another option is
 * to use an optimising algorithm (like PSO) to do a local search to find the best
 * configuration for adding the new stems.
 */
public abstract class RNAFolder {
    /**
     *
     * @param currentfolding - This is a input/output parameter (passed by reference.
     * The new folding is returned in this parameter.
     * @param openStems - The stems to remove from the conformation.
     * @param closeStems - The stems to attempt to add to the conformation.
     */
    public abstract void refold(RNAConformation currentfolding,
            RNAConformation openStems,
            RNAConformation closeStems);

    /**
     * Does a general refold by removing the openStems and then adding as many
     * close stems as possible.
     * @param currentfolding This is a input/output parameter (passed by reference.
     * The new folding is returned in this parameter.
     * @param openStems The stems to remove from the conformation.
     * @param closeStems The stems to attempt to add to the conformation.
     */
    /*
    public void fastRefold(RNAConformation currentfolding,
            RNAConformation openStems,
            RNAConformation closeStems) {

        //        remove openStems
        currentfolding.removeAll(openStems);
        boolean conflicts = false;

        for (RNAStem tempStem : closeStems) {
            //check if this stem conflicts with any of the current stems
            conflicts = false;
            for (RNAStem currentStem : currentfolding) {
                if (((RNAStem)tempStem).conflictsWith((RNAStem)currentStem)) {
                    conflicts = true;
                    break;
                }
            }
        }
    }
    */

}
