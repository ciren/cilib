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
 * This subclass uses a greedy algorithm to add the 'close stems' to the
 * 'currentStem' RNAStem. It will add the stems from 'closeStems' that reduces
 * the free energy of the RNAStem the most, first, until no more stems can be added.
 */
public class GreedyRNAFolder extends RNAFolder {
    /* (non-Javadoc)
     * @see net.sourceforge.cilib.BioInf.RNAFolder#refold(net.sourceforge.cilib.BioInf.RNAStem, java.util.ArrayList, java.util.ArrayList)
     */
    public GreedyRNAFolder(RNAFitness fitness) {
        GreedyRNAFolder.fitness = fitness;
    }

    /**
     * This method modifies the 'currentFolding' HashSet. HashSet is passed by reference.
     */
    public void refold(RNAConformation currentFolding, RNAConformation openStems, RNAConformation closeStems) { // TODO: Look at FAST Simulated Annealing and Very FAST Simulated Annealing
        //remove openStems
        currentFolding.removeAll(openStems);
        //System.out.println("\tRemoved "+openStems.size()+" stems from conformation");
        //do greedy addition
        int addedCounter = 0;
        //int possible = closeStems.size();
        boolean canAdd = true;
        boolean conflicts = false;
        RNAStem bestStem = null;
        double bestFitness;
        bestFitness = fitness.getRNAFitness(currentFolding).doubleValue();
        while (canAdd) {
            canAdd = false;
            bestStem = null;
            for (RNAStem tempStem : closeStems) {
                //check if this stem conflicts with any of the current stems
                conflicts = false;
                for (RNAStem currentStem : currentFolding) {
                    if (((RNAStem) tempStem).conflictsWith((RNAStem) currentStem)) {
                        conflicts = true;
                        break;
                    }
                }
                if (!conflicts) {
                    //add this stem to currentFolding
                    currentFolding.add(tempStem);
                    //check if this stem gives better fitness
                    //test if currentFolding is more fit
                    if (fitness.getRNAFitness(currentFolding).doubleValue() < bestFitness) {
                        bestFitness = fitness.getRNAFitness(currentFolding).doubleValue();
                        bestStem = (RNAStem) tempStem;
                    }
                    currentFolding.remove(tempStem);
                }
            }
            //transfer the best stem from closeStems to currentFolding
            if (bestStem != null) {
                currentFolding.add(bestStem);
                closeStems.remove(bestStem);
                canAdd = true;
                addedCounter++;
            }
        }
        //System.out.println("\tGreedy Folder added "+addedCounter+" new stems to the new position");
        //System.out.println("\tout of a possible "+possible+" stems");
        //System.out.println("Stem grew by\t"+(addedCounter-openStems.size())+"\tstems  New length is "+currentFolding.size()+ "  Fitness is "+fitness.getRNAFitness(currentFolding).doubleValue());
    }

    static RNAFitness fitness;
}
