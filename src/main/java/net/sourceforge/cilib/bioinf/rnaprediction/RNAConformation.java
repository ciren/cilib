/*
 * RNAConformation.java
 * 
 * Created on 2005/09/07
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

import net.sourceforge.cilib.type.types.container.Set;

public class RNAConformation extends Set<RNAStem> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5471752732539466566L;

	public RNAConformation() {
		super();
	}
	
	public int length () {
		return NucleotideString.instance.nucleotideString.length();
	}
	
	public char[] getCharRepresentation () {
		if (bracketRepresentation == null)
			bracketRepresentation = new char[NucleotideString.instance.nucleotideString.length()];
		
		for (int i = 0; i < this.length();i++) {
			bracketRepresentation[i] = '.';
		}
		
		for (RNAStem s : this) {
			for (NucleotidePair p : s) {
				bracketRepresentation[p.get5primeIndex()-1] = '(';
				bracketRepresentation[p.get3primeIndex()-1] = ')';
			}
		}	
		
		return bracketRepresentation;
	}

	
	public RNAConformation clone() {
		RNAConformation clone = new RNAConformation();
		clone.addAll(this);
		clone.bracketRepresentation = new char[NucleotideString.instance.nucleotideString.length()];
		return clone;
	}
	

	char[] bracketRepresentation = null;
	StringBuffer conf;
	
	public void isInvalid() {
		for (RNAStem s : this) {
			for (RNAStem t : this) {
				if (s.conflictsWith(t) && s.compareTo(t)!=0) {
					System.out.println("Stem Initialiser:");
					System.out.println(s);
					System.out.println("conflicts with:");
					System.out.println(t);
					throw new RuntimeException ("Invalid conformation, Stems conflict with each other");
				}
			}
		}		
	}
	
	public boolean contains(NucleotidePair pair) {
		for (RNAStem s : this) {
			if (s.contains(pair))
				return true;
		}
		return false;
	}

	public int getNumOfPairs() {
		int count = 0;
		for (RNAStem s : this) {
			count += s.getLength();
		}
		return count;
	}
}
