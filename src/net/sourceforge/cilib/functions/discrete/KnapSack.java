/*
 * KnapSack.java
 * 
 * Created on Dec 8, 2005
 *
 * Copyright (C) 2003 - 2006 
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
package net.sourceforge.cilib.functions.discrete;

import java.util.ArrayList;
import java.util.Random;

import net.sourceforge.cilib.functions.DiscreteFunction;
import net.sourceforge.cilib.math.random.MersenneTwister;
import net.sourceforge.cilib.type.types.Vector;

/**
 * Perform the knapsack problem optimisation.
 * 
 * @author Gary Pampara
 */
public class KnapSack extends DiscreteFunction {
	
	private int capacity;
	private int numberOfObjects;
	private ArrayList<Double> weights;
	private ArrayList<Double> values;
	
	
	public KnapSack() {
		weights = new ArrayList<Double>();
		values = new ArrayList<Double>();
	}
	
	
	public Object getMinimum() {
		return new Double(0);
	}
	
	
	public Object getMaximum() {
		return new Double(this.capacity);
	}
	
	

	/**
	 * 
	 */
	public double evaluate(Vector x) {
		if (weights.size() == 0 && values.size() == 0) {
			randomInitialise();
		}
		else {
			if (weights.size() == 0) {
				weights = values;
			}
			else if (values.size() == 0) {
				values = weights;
			}
		}
		
		double knapsackValue = 0;
		double weightSum = 0;
		
		for (int i = 0; i < this.numberOfObjects; i++) {
			weightSum += x.getInt(i)*weights.get(i);
		}
		
		//System.out.println("WeightSum: " + weightSum);
		
		if (weightSum <= this.capacity) { // weightSum does not violate constraint
			// All is ok.... now calculate the fitness
			for (int i = 0; i < this.numberOfObjects; i++) {
				knapsackValue += x.getInt(i)*values.get(i);
			}
			
			//System.out.println("knapsackValue: " + knapsackValue);
			return knapsackValue;
		}
		else {
			return -1;			
		}		
	}
	
	
	private void randomInitialise() {
		Random random = new MersenneTwister();
		
		for (int i = 0; i < this.numberOfObjects; i++) {
			int number = 0;
			
			while (number == 0)
				number = random.nextInt(this.numberOfObjects);
			
			weights.add(i, Integer.valueOf(number).doubleValue());
			
			number = 0;
			while (number == 0)
				number = random.nextInt(this.numberOfObjects);
			
			values.add(i, Integer.valueOf(number).doubleValue());
		}
	}

		
	/**
	 * @return Returns the capacity.
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * @param capacity The capacity to set.
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * @return Returns the numberOfObjects.
	 */
	public int getNumberOfObjects() {
		return numberOfObjects;
	}

	/**
	 * @param numberOfObjects The numberOfObjects to set.
	 */
	public void setNumberOfObjects(int numberOfObjects) {
		this.numberOfObjects = numberOfObjects;
		this.weights.ensureCapacity(numberOfObjects);
		this.values.ensureCapacity(numberOfObjects);
	}


	/**
	 * @return Returns the value.
	 */
	public ArrayList<Double> getValue() {
		return values;
	}


	/**
	 * @param value The value to set.
	 */
	public void setValue(ArrayList<Double> value) {
		this.values = value;
	}

	
	/**
	 * Helper method. This method enables in input of a comma-separated string, 
	 * containing the values for the different object types.
	 * 
	 * @param valueString A comma separated string defining the values for
	 *                    each selection bucket.
	 */
	public void setValue(String valueString) {
		String [] values = valueString.split(",");
		this.values.clear();
		
		for (int i = 0; i < values.length; i++) {
			this.values.add(Double.valueOf(values[i]));
		}
	}


	/**
	 * @return Returns the weight.
	 */
	public ArrayList<Double> getWeight() {
		return weights;
	}


	/**
	 * @param weight The weight to set.
	 */
	public void setWeight(ArrayList<Double> weight) {
		this.weights = weight;
	}
	
	
	/**
	 * Helper method. This method enables in input of a comma-separated string, 
	 * containing the weight values for the different object types.
	 * 
	 * @param weightString A comma separated string defining the weight value for
	 *                     each selection bucket.
	 */
	public void setWeight(String weightString) {
		String [] values = weightString.split(",");
		this.weights.clear();
		
		for (int i = 0; i < values.length; i++) {
			this.weights.add(Double.valueOf(values[i]));
		}
	}

}
