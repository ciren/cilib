/*
 * Bit.java
 * 
 * Created on Jul 18, 2005
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
package net.sourceforge.cilib.Type.Types;

import net.sourceforge.cilib.Random.MersenneTwister;

public class Bit extends Numeric {
	
	private boolean state;
	
	public Bit() {
		this.state = new MersenneTwister().nextBoolean();
	}
	
	public Bit(boolean state) {
		this.state = state;
	}

	public boolean equals(Object other) {
		Bit otherBit = (Bit) other;
		
		if (getBit() == otherBit.getBit())
			return true;
		
		return false;
	}

	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean getBit() {
		return state;
	}

	public void setBit(boolean value) {
		this.state = value;
	}

	public int getInt() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setInt(int value) {
		// TODO Auto-generated method stub

	}

	public double getReal() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setReal(double value) {
		// TODO Auto-generated method stub

	}

	public int compareTo(Numeric other) {
		if (state == other.getBit())
			return 0;
		else
			return state ? 1 : -1;
	}

	/**
	 * Randomly choose a new valid for the <code>Bit</code> object.
	 */
	public void randomize() {
		this.state = new MersenneTwister().nextBoolean();
	}
	
	/**
	 * 
	 */
	public String toString() {
		return "Bit[" + state + "]";
	}
}
