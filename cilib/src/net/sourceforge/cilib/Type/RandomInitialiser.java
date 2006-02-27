/*
 * RandomInitialiser.java
 * 
 * Created on Oct 19, 2004
 *
 * Copyright (C)  2004 - CIRG@UP 
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
package net.sourceforge.cilib.Type;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;


/**
 * @author espeer
 *
 */
public class RandomInitialiser extends DomainBuilder {
	
	public RandomInitialiser(Random randomiser) {
		this.randomiser = randomiser;
		reset();
	}
	
	public void reset() {
		result = new ArrayList<Type>();
		isVector = false;
	}
	
	public void beginBuildPrefixVector() {
		isVector = true;
	}
	
	public void buildBit() {
		result.add(new Bit(randomiser.nextBoolean()));
	}
	
	public void buildInt(int lower, int upper) {
		int value = 0;
        long range = ((long) upper) - ((long) lower) + 1L;
        if (range < Integer.MAX_VALUE) {
                value = (int) (randomiser.nextInt((int) range) + lower);
        }
        else {
                int count = 0;
                do {
                        if (count++ > 100) {
                                throw new RuntimeException("Failed to find a random integer value within the bounds");
                        }
                        value = randomiser.nextInt();
                } while (value < lower || value > upper);
        }

		result.add(new Int(value));
	}
	
	public void buildPostfixVector(String previous, int dimension, int slack) {
		isVector = true;
		if (slack < 0) {
			dimension -= randomiser.nextInt(-slack);
		}
		else if (slack > 0) {
			dimension += randomiser.nextInt(slack);
		}
		DomainParser parser = new DomainParser(this);
		for (int i = 1; i < dimension; ++i) {
			parser.build(previous);
		}
	}
	
	public void buildReal(double lower, double upper) {
        BigDecimal bottom = (lower == Double.NEGATIVE_INFINITY) ? new BigDecimal(- Double.MAX_VALUE) : new BigDecimal(lower);
        BigDecimal top = (upper == Double.POSITIVE_INFINITY) ? new BigDecimal(Double.MAX_VALUE) : new BigDecimal(upper);
        double value =  top.subtract(bottom).multiply(new BigDecimal(randomiser.nextDouble())).add(bottom).doubleValue();
		result.add(new Real(value));
	}
	
	public void endBuildPrefixVector() {
	}
	
	public Type getResult() {
		if (result.isEmpty()) {
			return null;
		}
		else if (!isVector) {
			return result.get(0);
		}
		else {
			boolean bits = true;
			for (Type t : result) {
				if (! (t instanceof Bit)) {
					bits = false;
					break;
				}
			}
			
			int dimension = result.size();
			Vector tmp = null;
			if (bits) {
				tmp = new BitVector(dimension);
			}
			else {
				tmp = new MixedVector(dimension);
			}
			
			for (int i = 0; i < dimension; ++i) {
				tmp.set(i, result.get(i));
			}
			
			return tmp;
		}
		
	}
	
	private ArrayList<Type> result;
	private Random randomiser;
	private boolean isVector;
}
