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

import net.sourceforge.cilib.measurement.single.Fitness;
/**
 * @author mneethling
 * Abstract base class for fitness evaluation classes
 * Implement the derived classes in such a way that lower fitness is better.
 * ie. fitness should be minimised.
 */
public abstract class RNAFitness extends Fitness {
    private static final long serialVersionUID = 1060703834565988934L;

    public RNAFitness() {
    }

    public RNAFitness(String nuc) {
        this.nucleotides = nuc;
    }

    public abstract RNAFitness getClone();

    public abstract Double getRNAFitness(RNAConformation stems);

    protected String nucleotides;
}
