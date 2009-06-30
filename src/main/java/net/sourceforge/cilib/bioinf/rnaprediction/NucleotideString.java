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

/**
 * @author marais
 * Class which holds the global nucleotide string copy we are folding.
 */
public final class NucleotideString {

    private NucleotideString() {
    }

    public static NucleotideString getInstance() {
        if (instance != null) {
            return instance;
        }
        else {
            instance = new NucleotideString();
            return instance;
        }
    }

    public void setNucleotideString(String nucs) {
        nucleotideString = nucs;
    }

    public String getNucleotideString() {
        return nucleotideString;
    }

    public void setKnowStructure(int[] struct) {
        this.knownStructure = struct;
        knownConf = new RNAConformation();
        RNAStem tempStem = new RNAStem();
        for (int i = 0; i < struct.length; i++) {
            if (struct[i] != 0) {
                if (i < struct[i]) {
                    tempStem.add(new NucleotidePair(i+1, struct[i]));
                }
            }
        }
        knownConf.add(tempStem);
    }

    public int[] getKnownStructure() {
        return knownStructure;
    }

    public RNAConformation getKnowConf() {
        return knownConf;
    }

    public String nucleotideString = "";
    public int[] knownStructure = null;
    static NucleotideString instance = null;
    public RNAConformation knownConf = null;
}
