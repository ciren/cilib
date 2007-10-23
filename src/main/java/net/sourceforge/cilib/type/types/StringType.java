/*
 * StringType.java
 * 
 * Created on Oct 18, 2005
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
package net.sourceforge.cilib.type.types;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * 
 * @author Gary Pampara
 *
 */
public class StringType extends AbstractType {
	private static final long serialVersionUID = 2946972552546398657L;
	private String string;
	
	
	public StringType() {
		string = null;
	}
	
	
	public StringType(String string) {
		this.string = string;
	}
	
	public StringType(StringType copy) {
		this.string = copy.string;
	}
	
	@Override
	public StringType clone() {
		return new StringType(this);
	}
	
	
	public String getString() {
		return this.string;
	}
	
	
	public void setString(String newString) {
		this.string = new String(newString);
	}

	
	public boolean equals(Object other) {
		if (other instanceof StringType) {
			StringType t = (StringType) other;
			return this.string.equals(t.string);
		}
		else if (other instanceof String) {
			return this.string.equals(other);
		}
		
		return false;
	}

	
	public int hashCode() {
		return this.string.hashCode();
	}
	
	
	public int getDimension() {
		return 1;
	}

	
	public void randomise() {
		throw new UnsupportedOperationException("Randomise string? Are you looking for garbage?");
	}
	
	public void reset() {
		throw new UnsupportedOperationException("Reset string? Are you wanting to make it null or something?");
	}

	
	public String toString() {
		return string;
	}

	
	public String getRepresentation() {
		return toString();
	}

	public void writeExternal(ObjectOutput oos) throws IOException {
		oos.writeUTF(this.string);
	}

	public void readExternal(ObjectInput ois) throws IOException, ClassNotFoundException {
		this.string = ois.readUTF();
		
	}

	public boolean isInsideBounds() {
		return false; // There are no bounds for the StringType.
	}

}
