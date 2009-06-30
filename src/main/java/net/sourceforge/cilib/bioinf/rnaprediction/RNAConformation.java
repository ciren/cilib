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

import java.util.Arrays;

import net.sourceforge.cilib.type.types.container.Set;

/**
 *
 *
 */
public class RNAConformation extends Set<RNAStem> {
    private static final long serialVersionUID = 5471752732539466566L;
    private char[] bracketRepresentation = null;

    /**
     * Create an instance of a {@linkplain RNAConformation}.
     */
    public RNAConformation() {
        super();
    }

    /**
     * Get the length of the current {@linkplain RNAConformation}.
     * @return The length of the {@linkplain RNAConformation}.
     */
    public int length() {
        return NucleotideString.instance.nucleotideString.length();
    }

    /**
     * Get the character representation of the current {@linkplain RNAConformation}.
     * @return The {@linkplain RNAConformation} represented by a character array.
     */
    public char[] getCharRepresentation() {
        if (bracketRepresentation == null)
            bracketRepresentation = new char[NucleotideString.instance.nucleotideString.length()];

        for (int i = 0; i < this.length(); i++) {
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

    /**
     * {@inheritDoc}
     */
    public RNAConformation getClone() {
        RNAConformation clone = new RNAConformation();
        clone.addAll(this);
        clone.bracketRepresentation = new char[NucleotideString.instance.nucleotideString.length()];
        return clone;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;

        if ((other == null) || (this.getClass() != other.getClass()))
            return false;

        RNAConformation rnaConformation = (RNAConformation) other;
        return super.equals(other) && (Arrays.equals(this.bracketRepresentation, rnaConformation.bracketRepresentation));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + super.hashCode();
        hash = 31 * hash + (this.bracketRepresentation == null ? 0 : Arrays.hashCode(this.bracketRepresentation));
        return hash;
    }

    /**
     * Determine if the current {@linkplain RNAConformation} is valid.
     * @throws RuntimeException if an invalid confirmation is created.
     */
    public void isInvalid() {
        for (RNAStem s : this) {
            for (RNAStem t : this) {
                if (s.conflictsWith(t) && s.compareTo(t)!=0) {
                    System.out.println("Stem Initialiser:");
                    System.out.println(s);
                    System.out.println("conflicts with:");
                    System.out.println(t);
                    throw new RuntimeException("Invalid conformation, Stems conflict with each other");
                }
            }
        }
    }

    /**
     * Determine if the current {@linkplain RNAConformation} contains the provided
     * {@linkplain NucleotidePair}.
     * @param pair The {@linkplain NucleotidePair} to test.
     * @return <code>true</code> if the {@linkplain RNAConformation} contains the
     *         {@linkplain NucleotidePair}, <code>false</code> otherwise.
     */
    public boolean contains(NucleotidePair pair) {
        for (RNAStem s : this) {
            if (s.contains(pair))
                return true;
        }
        return false;
    }

    /**
     * Get the number of pairs within this {@linkplain RNAConformation}.
     * @return The number of pairs.
     */
    public int getNumOfPairs() {
        int count = 0;
        for (RNAStem s : this) {
            count += s.getLength();
        }
        return count;
    }
}
