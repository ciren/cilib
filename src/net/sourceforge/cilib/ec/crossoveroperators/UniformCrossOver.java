/*
 * UniformCrossOver.java
 * 
 * Created on Jun 22, 2005, 10:52:19 AM
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
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.Vector;

/**
 * @author otter
 *
 * UniformCrossOver - Creates two offspring, which is a random combination of the two parents supplied. 
 * It does not matter what the representation of the entities are, as long as it is in a form of a vector.
 */
public class UniformCrossOver<E extends Entity> extends CrossOverOperator<E> {
    
    private Random random;
    private double Cprob = 0.5; //default value, equal chance
    
    public UniformCrossOver() {
        random = new MersenneTwister();
        this.offspring = 2;
        this.parents = 2;
    }
    
    public Collection<E> reproduce(Collection<? extends E> parents) {
        if(parents.size() != this.numberOffParentsNeeded())
            throw new RuntimeException("Usage error : UniformCrossOver needs two parents. Received " + parents.size()+ " parents");
        //clone the parents
        ArrayList<E> offspring = new ArrayList<E>();
        for(E parent : parents) {
        	//try {
        		offspring.add((E)parent.clone());	
        	//}
        	//catch (CloneNotSupportedException ex) {
        	//	throw new RuntimeException("Could not clone parent Entity");
        	//}
        }

    	//create offpsring, by swapping where necessarry.
        Type ttmp = null;
    	for(int i = 0; i < offspring.get(0).getDimension(); i++) {
    		if(random.nextDouble() < Cprob) { //SWAP
                ttmp = ((Vector) offspring.get(0).get()).get(i);
                ((Vector) offspring.get(0).get()).set(i,((Vector) offspring.get(1).get()).get(i));
                ((Vector) offspring.get(1).get()).set(i,ttmp);
    		}
    	}
        return offspring;
    }
    
	public double getCprob() {
		return Cprob;
	}
	public void setCprob(double cprob) {
		Cprob = cprob;
	}
	public Random getRandom() {
		return random;
	}
	public void setRandom(Random random) {
		this.random = random;
	}
}
