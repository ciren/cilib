/*
 * MappingProblem.java
 *
 * Created on August 21, 2004, 11:00 AM
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

package net.sourceforge.cilib.Problem.MappingProblem;

import java.io.EOFException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;

import net.sourceforge.cilib.Domain.ComponentFactory;
import net.sourceforge.cilib.Domain.DomainComponent;
import net.sourceforge.cilib.Problem.DataSet;
import net.sourceforge.cilib.Problem.Fitness;
import net.sourceforge.cilib.Problem.OptimisationProblemAdapter;

/**
 * Abstract MappingProblem class that allows for implementing methods of
 * mapping high-dimensional data onto lower dimensions.  This class is abstract
 * and merely provides a nice skeleton, similar to the
 * OptimisationProblemAdapter.
 *
 * @author jkroon
 */
public abstract class MappingProblem extends OptimisationProblemAdapter {
	/**
	 * Make the compiler shut up!
	 */
	public void getDataSet(){ }

	/**
	 * Calculates the fitness of the given matrix.  This wraps arounds the
	 * {@see evaluateMapping} function.  It may call evaluateMapping multiple
	 * times for every call to calculateFitness depending on the dataset.
	 *
	 * @param solution The solution to evaluate.  This must conform to the
	 *		domain.
	 *
	 * @author jkroon
	 */
	protected final Fitness calculateFitness(Object solution) {
		double []matrix = (double[])solution;
		
		double [][]distmatrix = new double[numvectors][numvectors];
		double [][]outputs = new double[numvectors][D];

		performMapping(inputs, matrix, outputs);
			
		matrix = null;

		for(int a = 0; a < numvectors; a++) {
			distmatrix[a][a] = 0.0;
			for(int b = 0; b < a; b++)
				distmatrix[a][b] = distmatrix[b][a] = calcDistance(outputs[a], outputs[b]);
		}

		outputs = null;

		return evaluator.evaluateMapping(distmatrix);
	}

	/**
	 * This function is there to perform the mapping.  It <b>must not</b>
	 * alter the values in the inputs or distmatrix array.  It should place
	 * the mapped to vectoers in the outputs array.  All arrays are
	 * pre-allocated.
	 *
	 * The distmatrix is a single dimensional array - of the size specified by
	 * getMatrixSize().
	 *
	 * @param inputs The input vectors - do not alter.
	 * @param distmatrix The matrix as supplied by the Algorithm - do not alter.
	 * @param outputs Place your resulting output vectors in here.
	 *
	 * @author jkroon
	 */
	protected abstract void performMapping(double [][]inputs, double []distmatrix, double [][]outputs);

	/**
	 * This function should return the number of "doubles" required in the
	 * matrix in order to perform the mapping.
	 *
	 * @return The size of the mapping matrix.
	 * 
	 * @author jkroon
	 */
	protected abstract int getMatrixSize();

	/**
	 * Returns the DomainComponent representing this mapping.  The actual
	 * ^ depends on the mapping scheme, so your mapping scheme will need
	 * to override getMatrixSize().
	 *
	 * (-1000,1000) might not be sufficient.  Atm there is no way to
	 * alter this other than changing it here.
	 *
	 * @return An instance of DomainComponent as explained above.
	 *
	 * @author jkroon
	 */
	public final DomainComponent getDomain() {
		if(domain == null)
			domain = ComponentFactory.instance().newComponent("R(-1000,1000)^" + getMatrixSize());

		return domain;
	}

	/**
	 * Gets the value of M, the input dimension.
	 *
	 * @return The current value of M.
	 * 
	 * @author jkroon
	 */
	public final int getInputDim() {
		return M;
	}

	/**
	 * Gets the value of D, the output dimension.
	 *
	 * @return The current value of D.
	 *
	 * @author jkroon
	 */
	public final int getOutputDim() {
		return D;
	}
	
	/**
	 * This function retrieves the number of input vectors that forms
	 * part of the dataset.
	 *
	 * @author jkroon
	 */
	protected final int getNumInputVectors() {
		return numvectors;
	}

	/**
	 * This function sets the evaluator to use.
	 *
	 * @param The evaluator to use.
	 *
	 * @author jkroon
	 */
	public final void setEvaluator(MappingEvaluator evaluator) {
		this.evaluator = evaluator;
		evaluator.setMappingProblem(this);
	}
	
	/**
	 * This function calculates the distance between two vectors.  The
	 * default functions uses a linear mapping - subclasses may override
	 * this function if required.
	 *
	 * It is assumed (but not tested for) that the two vectors are of the
	 * same dimension.  The reason for not testing is efficiency.  This
	 * gets called hundreds and hundreds of times...
	 *
	 * @param v1 Vector 1.
	 * @param v2 Vector 2.
	 *
	 * @author jkroon
	 */
	protected double calcDistance(double []v1, double[]v2)
	{
		double sum = 0.0;
		for(int i = 0; i < v1.length; i++) {
			double diff = v1[i] - v2[i];
			sum += diff * diff;
		}

		return Math.sqrt(sum);
	}

	/**
	 * This method is used during initialisation by the Simulator to provide us
	 * with out DataSet.  This method loads the actual data from the DataSet.
	 *
	 * @param dataset The dataset from which to retrieve the data.
	 *
	 * @author jkroon
	 */
	public void setDataSet(DataSet dataset)
	{
		try {
			InputStream is = dataset.getInputStream();
			
			StreamTokenizer tok = new StreamTokenizer(new InputStreamReader(is));

			if(tok.nextToken() != StreamTokenizer.TT_NUMBER)
				throw new IllegalStateException("Expected an integer number as the first token in the dataset");

			numvectors = (int)tok.nval;

			if(numvectors <= 0)
				throw new IllegalStateException("Must have a positive number of vectors in input file");

			if(tok.nextToken() != StreamTokenizer.TT_NUMBER)
				throw new IllegalStateException("Expected an integer number as the second token in the dataset");

			M = (int)tok.nval;

			if(M <= 0)
				throw new IllegalStateException("Need to have a positive number as the input dimensions");

			inputs = new double[numvectors][M];
			
			if(tok.nextToken() != StreamTokenizer.TT_NUMBER)
				throw new IllegalStateException("Expected an integer number as the third token in the dataset");

			D = (int)tok.nval;

			if(D <= 0)
				throw new IllegalStateException("Need to have a positive number as the input dimensions");
				
			if(!(D <= M))
				throw new IllegalStateException("Output dimension must be less than input dimension");

			for(int i = 0; i < numvectors; i++) {
				for(int m = 0; m < M; m++) {
					int tok_ret = tok.nextToken();
					while(tok_ret != StreamTokenizer.TT_NUMBER)
					{
						switch(tok_ret) {
						case StreamTokenizer.TT_EOF:
							throw new EOFException();
						case StreamTokenizer.TT_WORD:
							throw new IllegalStateException("Only numerical input expected (line " + tok.lineno() + ")");
						}
					}
						
					inputs[i][m] = tok.nval;

				}
			}
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}

		inp_distmatrix = new double[numvectors][numvectors];
		for(int i = 0; i < numvectors; i++) {
			inp_distmatrix[i][i] = 0.0;
			for(int j = 0; j < i; j++)
				inp_distmatrix[i][j] = inp_distmatrix[j][i] = 
					calcDistance(inputs[i], inputs[j]);
		}
	}

	/**
	 * Retrieve the distance between the two given input vectors.
	 *
	 * @param i1 Index of the first vector
	 * @param i2 Index of the second vector
	 *
	 * @return the distance between the two vectors.
	 * 
	 * @author jkroon
	 */
	public final double getDistanceInputVect(int i1, int i2) {
		return inp_distmatrix[i2][i1];
	}

	private int D = -1;
	private int M = -1;
	private int numvectors = -1;
	private double inputs[][] = null;
	private double inp_distmatrix[][] = null;
	private DomainComponent domain = null;
	private MappingEvaluator evaluator = null;
}
