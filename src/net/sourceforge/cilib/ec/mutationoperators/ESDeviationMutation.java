/*
 * ESDeviationMutation.java
 * 
 * Created on Oct 4, 2005
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
package net.sourceforge.cilib.ec.mutationoperators;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.RandomNumber;
import net.sourceforge.cilib.type.types.Vector;

/**
 * @author otter
 * 
 * Muation operator, usually used for Evolutionary Strategies(ES),
 * The entity is a vector representation consisting of n-genetic parameters
 * and one deviation paramater which is a self adapting mutation step size.
 * 
 * Mutation operators for ES are heavily representation dependent.
 * 
 * Based on Fogel[1992]
 */
public class ESDeviationMutation<E extends Entity> extends MutationOperator<E> {
    
    protected RandomNumber randomSample;
    
    public ESDeviationMutation() {
        this.randomSample = new RandomNumber();        
    }

    public void mutate(E entity) {
        /*int n = ((ESIndividual)entity).getStrategyParamOffset();
        //mutate strategy param
         ((Vector) (entity.get())).setReal(n,
        		 ((Vector) (entity.get())).getReal(n)*(Math.exp(Math.sqrt(n)*this.randomSample.getGaussian())));
        
        for(int i = 0;i < n;i++) {
            //mutate genetic param
            ((Vector) (entity.get())).setReal(i,
                    ((Vector) (entity.get())).getReal(i)+((Vector) (entity.get())).getReal(n)*this.randomSample.getGaussian());
        }*/
    	Vector behavioralParameters = (Vector) entity.getBehaviouralParameters();
    	for (int i = 0; i < behavioralParameters.getDimension(); i++) {
    		double result = behavioralParameters.getReal(i)*(Math.exp(Math.sqrt(i)*this.randomSample.getGaussian()));
    		behavioralParameters.setReal(i, result);
    	}
    	
    	Vector geneticParameters = (Vector) entity.get();
    	for (int i = 0; i < geneticParameters.getDimension(); i++) {
    		double result = geneticParameters.getReal(i) + geneticParameters.getReal(behavioralParameters.getDimension())*this.randomSample.getGaussian();
    		geneticParameters.setReal(i, result);
    	}
    }

    public void setRandomSample(RandomNumber randomSample) {
        this.randomSample = randomSample;
    }
}
