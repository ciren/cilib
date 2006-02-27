/*
 * 
 * DifferentialCrossOver.java
 * 
 * Created on Jul 20, 2005
 *
 * Copyright (C) 2003, 2004, 2005 - CIRG@UP 
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
 * 
 */
package net.sourceforge.cilib.ec.crossoveroperators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.MersenneTwister;
import net.sourceforge.cilib.type.types.Vector;

/**
 * @author otter
 *
 * With this cross-over operator, there is one main parent and three other randomly chosen parents.
 * One offspring gets generated and this is offspring consists of a linear combination of the three randomly chosen parents based
 * one a probability of the crossOverFactor otherwise it inherits straight from the main parent.
 * 
 * See page 168 of prof. Engelbrecht's book: CI an introduction. 
 * 
 * This operator is usually used for DE.
 */
public class DifferentialCrossOver<E extends Entity> extends CrossOverOperator<E> {
    
    private Random random;
    private double crossOverFactor;
    private double scalingFactor;
    
    //TODO: Can also change this factors to the Rate hierarchy so that different strategies can be used. However the factors are 
    // almost always constant doubles so leave this as it is for now.
    
    public DifferentialCrossOver() {
        crossOverFactor = 0.5;
        scalingFactor = 1.0;
        random = new MersenneTwister();    
        
        this.offspring = 1;
        this.parents = 3;   //main parent + 3 other individuals.
    }    

    public Collection<E> reproduce(Collection<? extends E> parents) {
        if(parents.size() != this.numberOffParentsNeeded()+1)  
            throw new RuntimeException("Usage error : DifferentialCrossOver needs 4 parents. Received " + parents.size()+ " parents");
        
        ArrayList<E> offspring = new ArrayList<E>(); 
        //try {        //clone the main parent. 
            offspring.add((E)(((ArrayList<E>)parents).get(3)).clone());
        //} catch (CloneNotSupportedException ex) {
        //   throw new RuntimeException("Could not clone parent Entity");
        //}
        
        int i = random.nextInt(offspring.get(0).getDimension());
        for(int j = 0; j < offspring.get(0).getDimension(); j++) {
            if(random.nextDouble() < crossOverFactor || j == i) {
                ((Vector) (offspring.get(0).get())).setReal(j, 
                        ((Vector)((Entity)((ArrayList)parents).get(2)).get()).getReal(j)
                        + scalingFactor*(((Vector)((Entity)((ArrayList)parents).get(0)).get()).getReal(j) - ((Vector)((Entity)((ArrayList)parents).get(1)).get()).getReal(j)));
            }
        }
        return offspring;
    }

    public double getCrossOverFactor() {
        return crossOverFactor;
    }
    public void setCrossOverFactor(double crossOverFactor) {
        this.crossOverFactor = crossOverFactor;
    }
    public Random getRandom() {
        return random;
    }
    public void setRandom(Random random) {
        this.random = random;
    }
    public double getScalingFactor() {
        return scalingFactor;
    }
    public void setScalingFactor(double scalingFactor) {
        this.scalingFactor = scalingFactor;
    }
}
