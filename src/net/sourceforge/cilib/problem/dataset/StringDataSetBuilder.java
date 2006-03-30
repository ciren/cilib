/*
 * StringDataSetBuilder.java
 * 
 * Created on Mar 6, 2006
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
import java.util.Iterator;

public class StringDataSetBuilder extends DataSetBuilder {

	private ArrayList<String> strings;
	
	public StringDataSetBuilder() {
		this.strings = new ArrayList<String>();
		System.out.println("Building...");
	}
	
	@Override
	public void initialise() {
		
		for (Iterator<DataSet> i = this.iterator(); i.hasNext(); ) {
			DataSet dataSet = i.next();
			//System.out.println(dataSet);
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(dataSet.getInputStream()));
				String result = in.readLine();
				
				while (result != null) {
					//if (result == null) break;
					System.out.println(result);
					strings.add(result);
					
					result = in.readLine();
				}
			}
			catch (IOException ioException) {
				throw new RuntimeException(ioException);
			}
			
			System.out.println("Data set(s) initialised, proceeding...");
		}

	}
	
	public ArrayList<String> getStrings() {
		return this.strings;
	}

}
