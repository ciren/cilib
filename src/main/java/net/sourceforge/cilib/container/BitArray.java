/**
 * Copyright (C) 2003 - 2009
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
package net.sourceforge.cilib.container;

import net.sourceforge.cilib.util.Cloneable;


/**
 * This class represents an array of bit values and is efficient as the values
 * are packed together.
 *
 * Ideas for this class where taken from a C++ implementation.
 *
 * @author Gary Pampara
 */
public class BitArray implements Cloneable {
    private static final long serialVersionUID = 2559265464633882908L;

    private int [] bits;
    private int numberOfBits;
    private int size;

    private static final int[] BIT_MASKS = {
        0x80000000, 0x40000000, 0x20000000, 0x10000000,
        0x08000000, 0x04000000, 0x02000000, 0x01000000,
        0x00800000, 0x00400000, 0x00200000, 0x00100000,
        0x00080000, 0x00040000, 0x00020000, 0x00010000,
        0x00008000, 0x00004000, 0x00002000, 0x00001000,
        0x00000800, 0x00000400, 0x00000200, 0x00000100,
        0x00000080, 0x00000040, 0x00000020, 0x00000010,
        0x00000008, 0x00000004, 0x00000002, 0x00000001,
        };

    /**
     *  Create a <code>BitArray</code> with the initial number of bits
     *  equal to 32.
     */
    public BitArray() {
        this(32);
    }

    /**
     * Create a <code>BitArray</code> with the initial number of bits specified
     * by the parameter <code>numberOfBits</code>.
     * @param numberOfBits The number of bits to set for this {@linkplain BitArray}.
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
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public BitArray(BitArray copy) {
        numberOfBits = copy.numberOfBits;
        size = copy.size;

        bits = new int[copy.bits.length];
        System.arraycopy(copy.bits, 0, bits, 0, bits.length);
    }

    /**
     * {@inheritDoc}
     */
    public BitArray getClone() {
        return new BitArray(this);
    }

    /**
     * Return the state of the bit specified at index <code>index</code>.
     *
     * @param index The index of the specified bit.
     * @throws IndexOutOfBoundsException If the specified index value is invalid
     * @return Return the bit value at {@code index}.
     */
    public boolean get(int index) {
        if ((index > numberOfBits) | (index < 0))
            throw new IndexOutOfBoundsException("Cannot access bits that are out of scope");

        int byteValue = bits[index>>>5];
        int mask = BIT_MASKS[index%32];
        int result = byteValue & mask;

        return (result != 0);
    }

    /**
     * Sets the specified bit at index <code>index</code> to the true / on state.
     *
     * @param index The index of the target bit
     * @throws IndexOutOfBoundsException If the specified index value is invalid
     */
    public void set(int index) {
        if ((index > numberOfBits) | (index < 0))
            throw new IndexOutOfBoundsException("Cannot access bits that are out of scope");

        int pos = index >>> 5;
        bits[pos] |= BIT_MASKS[index%32];
    }

    /**
     * Clear the bit (make it false) located at a specific index.
     *
     * @param index The index of the bit to be cleared.
     * @throws IndexOutOfBoundsException If the specified index value is invalid.
     */
    public void clear(int index) {
        if ((index > numberOfBits) | (index < 0))
            throw new IndexOutOfBoundsException("Cannot access bits that are out of scope");

        int pos = index >>> 5;
        int mask = BIT_MASKS[index % 32];
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


    /**
     * Get the value of the bits between the provided indexes, {@code i} and {@code j}.
     * @param i The start index.
     * @param j The end index.
     * @return The value of the bits from indexes {@code i} to {@code j}.
     */
    public double valueOf(int i, int j) {
        double result = 0.0;
        int n = 1;

        for (int counter = i; counter < j; counter++) {
            if (this.get(counter)) {
                result += n;
            }

            n = n*2;
        }

        System.out.println("result: " + result);
        return result;
    }

}
