/*
 * BitArray.java
 *
 * Created on Jun 23, 2004
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
 */
package net.sourceforge.cilib.Container;

/**
 * This class represents an array of bit values and is efficient as the values
 * are packed together.
 *
 * Ideas for this class where taken from a C++ implementation.
 *
 * @author gpampara
 */
public class BitArray {
	private int [] bits;
	private int numberOfBits;
	private int size;

	private final static int[] bitMasks = {
		0x80000000, 0x40000000, 0x20000000, 0x10000000,
		0x08000000, 0x04000000, 0x02000000, 0x01000000,
		0x00800000, 0x00400000, 0x00200000, 0x00100000,
		0x00080000, 0x00040000, 0x00020000, 0x00010000,
		0x00008000, 0x00004000, 0x00002000, 0x00001000,
		0x00000800, 0x00000400, 0x00000200, 0x00000100,
		0x00000080, 0x00000040, 0x00000020, 0x00000010,
		0x00000008, 0x00000004, 0x00000002, 0x00000001 };

	/**
	 *  Create a <code>BitArray</code> with the initial number of bits 
	 *  equal to 32
	 */
	public BitArray() {
		this(32);
	}

	/**
	 * Create a <code>BitArray</code> with the initial number of bits specified
	 * by the parameter <code>numberOfBits</code>
	 */
	public BitArray(int numberOfBits) {
		if (numberOfBits < 32)
			this.numberOfBits = 32;
		else
			this.numberOfBits = numberOfBits;

		//System.out.println("numberOfBits: " + numberOfBits);
		size = (this.numberOfBits >>> 5) + 1;
		//System.out.println("size: " + size);
		bits = new int[size];
		//System.out.println("length: " + bits.length);
	}

	/**
	 * Return the state of the bit specified at index <code>index</code>
	 *
	 * @param index The index of the sprecified bit
	 * @throws IndexOutOfBoundsException If the specified index value is invalid
	 */
	public boolean get(int index) throws IndexOutOfBoundsException {
		if ((index > numberOfBits) | (index < 0))
			throw new IndexOutOfBoundsException("Cannot access bits that are out of scope");

		int _byte = bits[index>>>5];
		int mask = bitMasks[index%32];
		int result = _byte & mask;

		return (result != 0);
	}

	/**
	 * Sets the specified bit at index <code>index</code> to the true / on state.
	 *
	 * @param index The index of the target bit
	 * @throws IndexOutOfBoundsException If the specified index value is invalid
	 */
	public void set(int index) throws IndexOutOfBoundsException {
		if ((index > numberOfBits) | (index < 0))
			throw new IndexOutOfBoundsException("Cannot access bits that are out of scope");

		int pos = index >>> 5;
		bits[pos] |= bitMasks[index%32];
	}

	/**
	 * Clear the bit (make it false) located at a specific index
	 *
	 * @param index The index of the bit to be cleared
	 * @throws IndexOutOfBoundsException If the specified index value is invalid
	 */
	public void clear(int index) throws IndexOutOfBoundsException {
		if ((index > numberOfBits) | (index < 0))
			throw new IndexOutOfBoundsException("Cannot access bits that are out of scope");

		int pos = index >>> 5;
		int mask = bitMasks[index % 32];
		int isolatedByte = bits[pos] & mask;

		// Now XOR to clear the bit
		bits[pos] ^= isolatedByte;
	}

	/**
	 * Returns the actual size used by the <code>BitArray</code>
	 * The result returned will be the size + 1.
	 *
	 * @return The size of the array (n-1) (in bytes) of the internal array
	 */
	public int size() {
		return size;
	}

	/**
	 * This method returns the number of bits currently represented by the
	 * <code>BitArray</code>. This is the "length" of the <code>BitArray</code>
	 *
	 * @return The number of bits represented by the <code>BitArray</code>
	 */
	public int length() {
		return numberOfBits;
	}
}
