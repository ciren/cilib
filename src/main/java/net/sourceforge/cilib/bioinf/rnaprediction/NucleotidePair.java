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
package net.sourceforge.cilib.bioinf.rnaprediction;

import java.security.InvalidParameterException;

/**
 *
 * @author mneethling
 *    This class represents a binding between 2 nucleotides. It contains the base
 *    pair symbols and index into nucleotide string (starting from 1) for each.
 */

public class NucleotidePair implements Comparable<NucleotidePair> {

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + base3prime;
        result = prime * result + base5prime;
        result = prime * result + index3prime;
        result = prime * result + index5prime;
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final NucleotidePair other = (NucleotidePair) obj;
        if (base3prime != other.base3prime)
            return false;
        if (base5prime != other.base5prime)
            return false;
        if (index3prime != other.index3prime)
            return false;
        if (index5prime != other.index5prime)
            return false;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new String(index5prime+" "+base5prime+base3prime+" "+index3prime);
    }

    public NucleotidePair(int index5prime, char base5prime, int index3prime, char base3prime) {
        //Check bases contain at least 3 nucs between them
        if (index3prime-index5prime < 4)
            throw new InvalidParameterException("There should be at least 3 bases between index5prime and index3prime.");

        //TODO check for valid bases (canBind function)

        this.index5prime = index5prime;
        this.base5prime = base5prime;
        this.index3prime = index3prime;
        this.base3prime = base3prime;
    }

    public NucleotidePair(int index5prime, int index3prime) {
        this.index5prime = index5prime;
        this.index3prime = index3prime;
        this.base5prime = NucleotideString.getInstance().getNucleotideString().charAt(index5prime-1);
        this.base3prime = NucleotideString.getInstance().getNucleotideString().charAt(index3prime-1);
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(NucleotidePair o) {
        if (index5prime == o.index5prime) {
            if (index3prime == o.index3prime)
                return 0;
            else
                return this.index3prime - o.index3prime;
        }
        else {
            return this.index5prime - o.index5prime;
        }
    }

    public int get5primeIndex() {
        return index5prime;
    }

    public int get3primeIndex() {
        return index3prime;
    }

    public char get5primeBase() {
        return base5prime;
    }

    public char get3primeBase() {
        return base3prime;
    }

    /**
     * Returns true if the pairs contains the same base.
     * @param other
     * @return
     */
    public boolean shareBase(NucleotidePair other) {
        if (index5prime == other.index5prime || index5prime == other.index3prime)
            return true;
        if (index3prime == other.index5prime || index3prime == other.index3prime)
            return true;
        return false;
    }

    /**
     * Returns true if this pair is 'inside' or 'internal' pair of other pair.
     * ie i` &lt; i &lt; j &lt; j` where i,j is this pair and i`,j` is other pair
     * @param other
     * @return
     */
    public boolean containedInside(NucleotidePair other) {
        if (other.index5prime < index5prime)
            if (index3prime < other.index3prime)
                return true;
        return false;
    }

    /**
     * Returns true if other pair is 'inside' or 'internal' to this pair.
     * ie i &lt; i` &lt; j` &lt; j where i,j is this pair and i`,j` is other pair
     * @param other
     * @return
     */
    public boolean contains(NucleotidePair other) {
        if (index5prime < other.index5prime)
            if (other.index3prime < index3prime)
                return true;
        return false;
    }

    /**
     * Returns true if the base pairs form a pseudoknot formation.
     * Returns false if i &lt; j &lt; i` &lt; j` or i` &lt; j` &lt; i &lt; j
     * @param other
     * @return
     */
    public boolean isPseudoknot(NucleotidePair other) {
        /* because index5prime < index3prime alwys, we only need to check
         * that both of this pair's indexes are smaller than other's 5 prime.
         */
        if (index5prime < other.index5prime && index3prime < other.index5prime)
            return false;
        if (other.index5prime < index5prime && other.index3prime < index5prime)
            return false;
        return true;
    }

    private int index5prime, index3prime;
    private char base5prime, base3prime;
}
