/*
 * MockClusteringStringDataSet.java
 * 
 * Created on May 24, 2007
 *
 * Copyright (C) 2004 - CIRG@UP 
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
package net.sourceforge.cilib.problem.dataset;

public class MockClusteringStringDataSet extends MockStringDataSet {
	private static final long serialVersionUID = 5346632651777290824L;

	public MockClusteringStringDataSet() {
		patternExpression = ",\\s*Class[\\d+]|,\\s*";
		data = "7.5, 1.5,Class0\n";
		data += "7.0, 3.5, Class0\n";
		data += "6.0, 2.0, Class0\n";
		data += "5.0, 4.0, Class0\n";
		data += "5.0, 2.5, Class1\n";
		data += "1.5, 2.0, Class1\n";
		data += "2.0, 4.0, Class1\n";
		data += "2.0, 6.0, Class1\n";
		data += "2.5, 0.5, Class2\n";
		data += "3.0, 5.0, Class2\n";
		data += "3.0, 7.0, Class2\n";
		data += "3.5, 6.5, Class2\n";
		data += "3.5, 5.0, Class3\n";
		data += "4.0, 3.0, Class3\n";
		data += "3.5, 1.0, Class3\n";
		data += "6.0, 1.0, Class3\n";
	}
}
