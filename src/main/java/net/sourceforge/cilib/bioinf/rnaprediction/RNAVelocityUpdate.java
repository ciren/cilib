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

import java.util.ArrayList;
import java.util.Random;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.generator.KnuthSubtractive;
import net.sourceforge.cilib.pso.velocityupdatestrategies.VelocityUpdateStrategy;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author mneethling
 *
 */
public class RNAVelocityUpdate implements VelocityUpdateStrategy {
    private static final long serialVersionUID = -6682883069950387034L;

    private RNAConformation unionSet;
    private Random r;
    double openProbability = 0.5;
    double closeProbability = 0.6;
    double addRandomProbability = 0.1;

    public RNAVelocityUpdate() {
        unionSet = new RNAConformation();
        r = new KnuthSubtractive();
    }

    public RNAVelocityUpdate(RNAVelocityUpdate copy) {
        this();

        this.openProbability = copy.openProbability;
        this.closeProbability = copy.closeProbability;
        this.addRandomProbability = copy.addRandomProbability;

        this.unionSet = copy.unionSet.getClone();
    }


    public RNAVelocityUpdate getClone() {
        return new RNAVelocityUpdate(this);
    }


    /**
     *
     * @param threshold - should be in the range [0.0:1.0]
     * @return TRUE if a value sampled from range U[0.0:1.0) is less than threshold; FALSE otherwise
     *
     * The higher the threshold, the better chance of returning TRUE.
     */
    private boolean prob(double threshold) {
        if (r.nextDouble() < threshold)
            return true;

        return false;
    }

    /*
     * VelocityUpdate creates 2 sets (openStems and closeStems) by using own and
     * neighbourhood bests.
     */

    public void updateVelocity(Particle particle) {
        TypeList velocity = (TypeList) particle.getVelocity();
        RNAConformation openStems = (RNAConformation) velocity.get(0);
        RNAConformation closeStems = (RNAConformation) velocity.get(1);

        openStems.clear();
        closeStems.clear();

        RNAConformation pBest = (RNAConformation) particle.getBestPosition();
        RNAConformation nBest = (RNAConformation) (particle.getNeighbourhoodBest().getPosition());
        RNAConformation position = (RNAConformation) particle.getPosition();

        //openStems should contain some of those stems that is included in the
        //current folding but not in either of pBest or nBest. Thus add all the
        //stems in particle.position that is not in the union of pBest and nBest.
        //the amount of stems added corresponds to an inertia factor

        //closeStems should contain some of the stems that are in the union of
        //pBest and nBest but not in particle.position.
        //A random selection of stems from the pool can also be added.

        //Get the union of pBest and nBest

        unionSet.clear();
        unionSet.addAll(pBest);
        unionSet.addAll(nBest);



        for (RNAStem stem : position) {
            if (!unionSet.contains(stem)) {
                if (prob(openProbability)) { // TODO: Try link up with jan stuff?
                    openStems.add(stem);
                }
            }
        }


        for  (RNAStem stem : unionSet) {
            if (!position.contains(stem)) {
                if (prob(closeProbability)){
                    closeStems.add(stem);
                }
            }
        }

        //remove some stems from position...
        for (RNAStem o : position) {
            if (prob(openProbability)) { // TODO: inertiaUpdatreStrategy
                openStems.add(o);
            }
        }

        //add some random stems from the stem pool to closeStems
        ArrayList<RNAStem> allStems = StemGenerator.getInstance().getAllStems();
        //int num = 0;

        int index;
        int count = StemGenerator.getInstance().getAllStems().size();
        if (particle.getNeighbourhoodBest() == particle) {
            //System.out.println("I'm the best particle.");
            for (int i = 0; i < count; i++) {
                if (prob(addRandomProbability)) {
                    index = r.nextInt(allStems.size()); //get random stem to add.
                    closeStems.add(allStems.get(index));
                }
            }
            //System.out.println("Added "+num+" stems.");
        }
        else {
            //System.out.println("I'm not the best particle.");
            for (int i = 0; i < count; i++) {
                if (prob(addRandomProbability/100)) {
                    index = r.nextInt(allStems.size()); //get random stem to add.
                    closeStems.add(allStems.get(index));
                }
            }
            //System.out.println("Added "+num+" stems.");
        }
        //count = (int)((double) count * (closeProbability * 0.01));
        //System.out.println("Random stems added: "+count);
        //System.out.println("Added " + secondCounter + " random stems");
    }


    public void updateControlParameters(Particle particle) {
        // TODO Auto-generated method stub
    }


    /**
     * @param closeProbability The closeProbability to set.
     */
    public void setCloseProbability(double closeProbability) {
        this.closeProbability = closeProbability;
    }
    /**
     * @param openProbability The openProbability to set.
     */
    public void setOpenProbability(double openProbability) {
        this.openProbability = openProbability;
    }

    public void setAddRandomProbability(double addRandomProbability) {
        this.addRandomProbability = addRandomProbability;
    }

}
