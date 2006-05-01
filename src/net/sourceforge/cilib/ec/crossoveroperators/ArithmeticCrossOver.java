/*
 * ArithmeticCrossOver.java
 * 
 * Created on Jun 21, 2005
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
import net.sourceforge.cilib.type.types.Vector;

/**
 * @author otter
 *
 * Arithmetic cross-over. Usually used within GA where the representation consists of real valued vectors.
 * 
 * In this case the implemenation has the following semantics:
 *      check page 138 of Prof. Engelbrecht's CI book.
 *      
 * Take note: Due to the mathmetics, if the two parents are the same Entity then the resulting two offspring will be an exact
 * clone of the parents.
 */
public class ArithmeticCrossOver<E extends Entity> extends CrossOverOperator<E> {
    
    private Random random;
    
    public ArithmeticCrossOver() {
        random = new MersenneTwister();
        this.offspring = 2;
        this.parents = 2;
    }
    
    public Collection<E> reproduce(Collection<? extends E> parents) {
        
        if(parents.size() != this.numberOffParentsNeeded())
            throw new RuntimeException("Usage error : ArithmeticCrossOver needs two parents. Received " + parents.size()+ " parents");
        //clone the parents       
        ArrayList<E> offspring = new ArrayList<E>();
        for(Entity parent : parents) {
        	//try {
        		offspring.add((E)parent.clone());	
        	//}
        	//catch (CloneNotSupportedException ex) {
        	//	throw new RuntimeException("Could not clone parent Entity");
        	//}
        }
        
        double r1 = 0.0, r2 = 0.0, c1 = 0.0, c2 = 0.0;

        for(int i = 0; i < offspring.get(0).getDimension(); i++) {
            c1 = ((Vector) (offspring.get(0).get())).getReal(i);
            c2 = ((Vector) (offspring.get(1).get())).getReal(i);

            r1 = random.nextDouble();
            ((Vector) (offspring.get(0).get())).setReal(i, r1*c1 + (1.0-r1)*c2);
            r2 = random.nextDouble();
            ((Vector) (offspring.get(1).get())).setReal(i, (1.0-r2)*c1 + r2*c2);
        }
        
        return offspring;   
    }
}
