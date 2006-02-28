/*
 * OptimisationProblemAdapter.java
 *
 * Created on January 11, 2003, 1:02 PM
 *
 * 
 * Copyright (C) 2003, 2004 - CIRG@UP 
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

package net.sourceforge.cilib.problem;

import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.type.DomainParser;

/**
 * This is a covenience class that keeps track of the number of fitness evaluations.  
 * This class can be extend instead of  implementing {@link OptimisationProblem} directly.
 * <p />
 * The contract of returning an instance of {@link  InferiorFitness} for solutions outside the 
 * problem search space is implemented by {@link #getFitness(Object solution, boolean count)} 
 * 
 * @author  Edwin Peer
 */
public abstract class OptimisationProblemAdapter implements OptimisationProblem { 
   
	protected int fitnessEvaluations;
	protected DataSetBuilder dataSetBuilder;
	 
    public OptimisationProblemAdapter() {
        fitnessEvaluations = 0;
    }
    
    protected abstract Fitness calculateFitness(Object solution);
    
    public final Fitness getFitness(Object solution, boolean count) {
    	if (count) {
    		++fitnessEvaluations;
    	}
        //if (getDomain().isInside(solution)) {
    	if (DomainParser.getInstance().isInside(solution)) {
            return calculateFitness(solution);	
        }
        else {
        	return InferiorFitness.instance();
        }
    }
    
    public final int getFitnessEvaluations() {
        return fitnessEvaluations;
    }
    
    
    public DataSetBuilder getDataSetBuilder() {
    	return this.dataSetBuilder;
    }
    
    public void setDataSetBuilder(DataSetBuilder dataSetBuilder) {
    	this.dataSetBuilder = dataSetBuilder;
    }
    
}
