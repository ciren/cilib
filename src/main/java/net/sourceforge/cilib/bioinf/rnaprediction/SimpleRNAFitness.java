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
 * @author mneethling
 * This fitness class calculates the fitness of the conformation by adding
 * up the number of contacts in the conformation and subtracting that number from
 * the length of the nucleotide string. Thus, fitness should be minimised.
 */
public class SimpleRNAFitness extends RNAFitness{
    private static final long serialVersionUID = -666765613654971498L;

    public SimpleRNAFitness() {
        nucleotides = NucleotideString.getInstance().getNucleotideString();
    }

    public SimpleRNAFitness(SimpleRNAFitness copy) {
    }

    public SimpleRNAFitness getClone() {
        return new SimpleRNAFitness(this);
    }

    /* (non-Javadoc)
     * @see net.sourceforge.cilib.BioInf.RNAFitness#getRNAFitness(java.util.Collection)
     */
    public Double getRNAFitness(RNAConformation stems) {
        //String currentNucleotides = StemGenerator.getInstance().getNucleotides();
        //iterate over the stems and get their lengths
        int length = 0;
        for (RNAStem stem : stems) {
            length += stem.getLength()*2;
        }
        return new Double(NucleotideString.getInstance().getNucleotideString().length() - length);
    }

}
