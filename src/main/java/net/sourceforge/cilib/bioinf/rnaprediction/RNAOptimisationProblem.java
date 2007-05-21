/*
 * RNAOptimisationProblem.java
 * 
 * Created on 2005/05/25
 *
 * Copyright (C) 2003, 2005 - CIRG@UP 
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
package net.sourceforge.cilib.bioinf.rnaprediction;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.problem.OptimisationProblemAdapter;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.type.DomainRegistry;


/**
 * @author mneethling
 *
 */
public class RNAOptimisationProblem extends OptimisationProblemAdapter {
	private static final long serialVersionUID = -2995246748207035373L;
	
	private RNAFitness fit;
	protected int fitnessEvaluations;
	private DataSetBuilder dataSetBuilder;
	
	public RNAOptimisationProblem(RNAOptimisationProblem copy) {
		
	}
	
	public RNAOptimisationProblem clone() {
		return new RNAOptimisationProblem(this);
	}
	
	/**
	 * @return Returns the fit.
	 */
	public RNAFitness getFitnessCalculator() {
		return fit;
	}
	/**
	 * @param fit The fit to set.
	 */
	public void setFitnessCalculator(RNAFitness fit) {
		this.fit = fit;
	}
	
	protected Fitness calculateFitness(Object solution) {
		//System.out.println("object type of solution: " + solution.getClass().getName());
		return new MinimisationFitness(fit.getRNAFitness((RNAConformation) solution));
	}
	
	public void setDataSetBuilder(DataSetBuilder dataSetBuilder) {
		this.dataSetBuilder = dataSetBuilder;
	}
	
	public DataSetBuilder getDataSetBuilder() {
		return this.dataSetBuilder;
	}
	
	public DomainRegistry getDomain() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public DomainRegistry getBehaviouralDomain() {
		// TODO Auto-generated method stub
		return null;
	}
}
