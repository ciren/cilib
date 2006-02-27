/*
 * AcceptSuccessMutationDecorator.java
 * 
 * Sep 30, 2005 6:05:44 PM
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
 */
package net.sourceforge.cilib.ec.mutationoperators;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.OptimisationProblem;
/**
 * @author otter
 *
 * Decorate a mutation opertor with the additional responsiblity of only
 * accepting the mutation result in the event of success. That is when
 * the result is fitter than before the mutation occured.
 */
public class ASMutationDecorator<E extends Entity> extends MutationDecorator<E> {

	//YOU NEED THE PROBLEM IN ORDER TO EVALUATE THE FITNESS.Make sure the correct problem is specified within the XML configuration file.
	private OptimisationProblem problem;
    private int successes = 0;
    
    public void mutate(E entity) {
        if (problem == null) {
            throw new RuntimeException("Usage error : No problem has been specified");
        }
        
		String id = entity.getId();
		E temp = (E) entity.clone();    			
        temp.setId(id);

        //call the component mutation operator, mutate method.
    	this.component.mutate(entity);

    	//do fitness comparison, to see if mutation was successful or not.
    	entity.setFitness(problem.getFitness(entity.get(),false));
    	temp.setFitness(problem.getFitness(temp.get(),false));
    	if(temp.compareTo(entity) > 0) {
    		entity = temp;
    	} else {
            this.successes++;   //mutation was a success, increment successes counter.
        }
	}

	public void setProblem(OptimisationProblem problem) {
		this.problem = problem;
	}
    public int getSuccesses() {
        return successes;
    }
}

