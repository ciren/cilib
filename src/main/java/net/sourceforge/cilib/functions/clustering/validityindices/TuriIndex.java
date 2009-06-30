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
package net.sourceforge.cilib.functions.clustering.validityindices;

import net.sourceforge.cilib.functions.clustering.ClusteringFitnessFunction;

/**
 * This is the Turi Validity Index as given in
 * Section 3.1.4 on page 66 of Mahamed G. H. Omran's PhD thesis, titled
 * <tt>Particle Swarm Optimization Methods for Pattern Recognition and Image Processing</tt>,
 * November 2004
 * NOTE: By default, the cluster center refers to the cluster centroid. See {@link ClusterCenterStrategy}.
 * @author Theuns Cloete
 */
public class TuriIndex extends ClusteringFitnessFunction {
    private static final long serialVersionUID = 2457356424874462741L;

//    private double c = 0.0;
//    private RandomNumber random = null;
//    private double gaussian = 0.0;

    public TuriIndex() {
        super();
//        random = new RandomNumber();
//        gaussian = random.getGaussian(2, 1);
    }

    @Override
    public double calculateFitness() {
//        gaussian = random.getGaussian(2, 1);

        return /*(c * gaussian + 1) * */(calculateAverageIntraClusterDistance() / calculateMinimumInterClusterDistance());
    }

//    public void setC(double c) {
//        this.c = c;
//    }

    @Override
    public TuriIndex getClone() {
        return new TuriIndex();
    }
}
