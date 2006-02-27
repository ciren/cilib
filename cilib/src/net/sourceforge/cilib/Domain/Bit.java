/*
 * Bit.java 
 * 
 * Created on Apr 17, 2004
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
 *
 */
package net.sourceforge.cilib.Domain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.BitSet;
import java.util.Random;

/**
 * @author espeer
 */
public class Bit extends Quantitative {

    public static final String PREFIX = "B";
    
    public Bit(String representation) {
        if (! representation.equals(PREFIX)) {
            throw new IllegalArgumentException();
        }
    }
    
    public Number getLowerBound() {
        return new Byte((byte) 0);
    }

    public Number getUpperBound() {
        return new Byte((byte) 1);
    }

    public String getRepresentation() {
        return PREFIX;
    }

    protected boolean isInside(Object item, int offset) {
    	if (offset == -1) {
    		if (item instanceof Byte) {
    			byte tmp = ((Byte) item).byteValue();
    			if (tmp == 0 || tmp == 1) {
    				return true;
    			}
    		}
    		return false;
    	}
    	else if (item instanceof BitSet) {
    		BitSet tmp = (BitSet) item;
    		if (offset < ((tmp instanceof SizeHackedBitSet) ? tmp.size() : tmp.length() - 1)) {
    			return true;
    		}
    		else {
    			throw new ArrayIndexOutOfBoundsException();
    		}
    	}
    	else {
    		return false;
    	}
    }
    
    protected void serialise(ObjectOutputStream oos, Object item, int offset) throws IOException {
    	if (offset == -1) {
    		oos.writeByte(((Byte) item).byteValue());
    	}
    	else {
    		if (((BitSet) item).get(offset)) {
    			oos.writeByte((byte) 1);
    		}
    		else {
    			oos.writeByte((byte) 0);
    		}
    	}
    }
    
    public Object deserialise(ObjectInputStream ois) throws IOException {
    	return new Byte(ois.readByte());
    }
    
    protected Object deserialiseGroup(ObjectInputStream ois) throws IOException {
    	BitSet tmp = new SizeHackedBitSet();
    	tmp.set(0, ois.readByte() == 1);
    	tmp.set(1, true);
    	return tmp;
    }
	
    public Object getRandom(Random randomiser) {
    	if (randomiser.nextBoolean()) {
    		return new Byte((byte) 1);
    	}
    	else {
    		return new Byte((byte) 0);
    	}
    }

	protected Object getRandomGroup(Random randomiser) {
		BitSet tmp = new SizeHackedBitSet();
		tmp.set(0, randomiser.nextBoolean());
		tmp.set(1, true); // ugly hack to make SizeHackedBitSet.size() return something we can use
		return tmp;
	}
	
	protected Object mergeGroups(Object _a, Object _b) {
		BitSet a = (BitSet) _a;
		BitSet b = (BitSet) _b;
		
		BitSet tmp = (BitSet) a.clone();
		
		int i = a.size();
		for (int j = 0; j < b.size(); ++i, ++j) {
			tmp.set(i, b.get(j));
		}
		
		tmp.set(i, true); // setting the sentinel bit again
		
		return tmp;
	}
	
	/* SizeHackedBitSet implements an ugly hack so that the actual number of bits that have been stored in the bitset
	 * can be obtained.
	 * 
	 * The basic idea is to use a sentinel bit at the end of the bit string that marks the last position. BitSet.length() returns
	 * the index of the rightmost true bit.  The contract is to set the (last+1)^th bit to true thus causing BitSet.length() to return 
	 * the length of the bit string plus one.
	 * 
	 * length() is overridden to make it behave as it would have without this hack
	 * size() has the new behaviour of returning the length of the original bit string provided bit n+1 is set to true 
	 * 
	 * Note, like java.util.BitSet this class is not thread safe without external synchronization.
	 */
	private class SizeHackedBitSet extends BitSet {
		
		/*
		 * This makes length() do what it is supposed to according to the Java API docs 
		 */
		public int length() {
			int sentinel = super.length() - 1;
			set(sentinel, false);
			int actualLength = super.length();
			set(sentinel, true);
			return actualLength;
		}
		
		/*
		 * This size() method returns the actual length of the bit string (if the sentinel has been properly set) instead of the
		 * actual storage used. The length of the bit string is more useful to CILib than the storage used. Note that the sentinel 
		 * bit is not included in the length. 
		 */
		public int size() {
			return super.length() - 1;
		}
	}
 
 	/**
 	 * Accepts a set of instances of the Bit variation.  This function
 	 * will only get called with the B case of the domain, B^?? is
 	 * handled by the protected variation for use by Compound.
 	 *
 	 * @param dv The DomainVisitor to use.
 	 * @param instances The Bit instances, will get passed down as Object[]
 	 * 		for the generic case.
 	 *
 	 * @author jkroon
 	 */
 	public void acceptDomainVisitor(DomainVisitor dv, Object []instances) {
 		Byte[] bitInstances = new Byte[instances.length];
 		for (int i = 0; i < instances.length; ++i) {
 			bitInstances[i] = (Byte) instances[i];
 		}
 
 		dv.visitBit(bitInstances);
 	}
 
 	/**
 	 * Accepts a set of instances of the byte[] variation.  This gets passed
 	 * down as Object[] for genericness.
 	 *
 	 * @param dv The DomainVisitor to use.
 	 * @param instances The byte[] instances to use.
 	 *
 	 * @author jkroon
 	 */
 	protected void acceptGroupedDomainVisitor(DomainVisitor dv, Object []instances) {
 		BitSet[] bsInstances = new BitSet[instances.length];
 		for (int i = 0; i < instances.length; ++i) {
 	 		bsInstances[i] = (BitSet) instances[i];
 		}
 
 		dv.visitBitSet(bsInstances);
 	}
}
