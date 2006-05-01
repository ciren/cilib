/*
 * nPointCrossOver.java
 * 
 * Created on Jun 22, 2005, 11:13:50 AM
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
import java.util.Collections;
import java.util.Random;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.Vector;


/**
 * @author otter
 *
 * n-point crossover - n positions are randomly chosen and the parameter (genes) between these positions are swapped.
 * n-point means you split the parents up in n+1 parts and then interchangably combine the parts to form the offspring.
 * 
 * This cross-over operator only works on Entities who uses a vector representation.
 */
public class nPointCrossOver<E extends Entity> extends CrossOverOperator<E> {
    
    private Random random;
    private int npoint = 2;
    
    public nPointCrossOver() {
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

    	int dim = offspring.get(0).getDimension();
        if(npoint > dim-1)
            throw new RuntimeException("Usage error : The value of n, must be smaller than the dimension - 1.");
        //get the part offsets and save then within the points list.
        int pp;
        ArrayList<Integer> points = new ArrayList<Integer>();
        for(int i = 0; i < npoint; i++) {
            pp = random.nextInt(dim);
            while(points.contains(pp) || pp == 0) {
                pp = random.nextInt(dim);
            }
        	points.add(pp);        	
        }
        Collections.sort(points);
        points.add(0,0);
        points.add(dim);
        //for(int i : points) {
        //    System.out.println("sort\t"+i);
       //}
        
    	//create offpsring, by swapping parts where necessarry.
        Type tmp = null;
        for(int i = 0; i < npoint+1; i++) {
            points.get(i);
            if(i%2==0) {
                for(int j = points.get(i); j < points.get(i+1); j++) {//SWAP
                    tmp = ((Vector) offspring.get(0).get()).get(j);
                    ((Vector) offspring.get(0).get()).set(j,((Vector) offspring.get(1).get()).get(j));
                    ((Vector) offspring.get(1).get()).set(j,tmp);
                }
            }
        }

        return offspring;
    }
    
	public int getNpoint() {
		return npoint;
	}
	public void setNpoint(int npoint) {
		this.npoint = npoint;
	}
	public Random getRandom() {
		return random;
	}
	public void setRandom(Random random) {
		this.random = random;
	}    
}
