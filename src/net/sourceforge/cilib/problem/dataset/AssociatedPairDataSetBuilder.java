/*
 * AssociatedPairDataSetBuilder.java
 * 
 * Created on Feb 23, 2006
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
package net.sourceforge.cilib.problem.dataset;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import net.sourceforge.cilib.container.Pair;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Vector;

/**
 * 
 * @author Gary Pampara
 * TODO: Make the key also able to be a StringType
 * TODO: specify format of file generically
 */
public class AssociatedPairDataSetBuilder extends DataSetBuilder {
	
	private ArrayList<Pair<Numeric, Vector>> keyPatternPair;
	
	/**
	 * 
	 *
	 */
	public AssociatedPairDataSetBuilder() {
		keyPatternPair = new ArrayList<Pair<Numeric, Vector>>();
	}
	

	/**
	 * 
	 */
	public void initialise() {
		BufferedReader br = new BufferedReader(new InputStreamReader(this.getDataSet(0).getInputStream()));
		
		try {
			String line = br.readLine();
			
			while (line != null) {
				addToDataSet(line);
				line = br.readLine();
			}
		}
		catch (IOException io) {
			throw new RuntimeException(io);
		}
	}
	
	/**
	 * 
	 * @param line
	 */
	private void addToDataSet(String line) {
		String [] elements = line.split(this.getDataSet(0).getPatternExpression());
		
		Vector vector = new MixedVector();
		
		for (int i = 0; i < elements.length; i++) {
			vector.add(new Real(Double.parseDouble(elements[i])));
		}
		
		keyPatternPair.add(new Pair<Numeric, Vector>(new Real(0.0), vector));
	}
	
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public Vector getPattern(int index) {
		return this.keyPatternPair.get(index).getValue();
	}
	
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public Numeric getKey(int index) {
		return this.keyPatternPair.get(index).getKey();
	}
	
	
	public void setKey(int index, Numeric key) {
		this.keyPatternPair.get(index).setKey(key);
	}


	public int getNumberOfPatterns() {
		return keyPatternPair.size();
	}

}
