/*
 * Weight.java
 * 
 * Created on Jan 01, 2005
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
package net.sourceforge.cilib.neuralnetwork.generic;

/**
 * @author stefanv
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Weight<T> {
	
	T weightValue = null;
	T previousChange = null;
	
	/**
	 * @param weightValue
	 */
	public Weight(T weightValue, T change) {
		this.weightValue = weightValue;
		this.previousChange = change;
	}
	
	public Weight<T> clone(){
		Weight<T> clone = new Weight<T>(this.weightValue, this.previousChange);
		return clone;
	}
	
	/**
	 * @return Returns the weightValue.
	 */
	public T getWeightValue() {
		return weightValue;
	}
	/**
	 * @param weightValue The weightValue to set.
	 */
	public void setWeightValue(T weightValue) {
		this.weightValue = weightValue;
	}
		
	/**
	 * @return Returns the previousChange.
	 */
	public T getPreviousChange() {
		return previousChange;
	}
	
	/**
	 * @param previousChange The previousChange to set.
	 */
	public void setPreviousChange(T previousChange) {
		this.previousChange = previousChange;
	}
}
