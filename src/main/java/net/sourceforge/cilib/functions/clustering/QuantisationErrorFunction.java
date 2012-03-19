/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.functions.clustering;

/**
 * This class makes use of the helper/member functions defined and implemented in
 * {@linkplain ClusteringFitnessFunction} to calculate the Quantisation Error of a particular
 * clustering in the <tt>calculateFitness</tt> method. See: <br/>
 * @PhDThesis{ omran2004thesis, title = "Particle Swarm Optimization Methods for Pattern Recognition
 *             and Image Processing", author = "Mahamed G.H. Omran", institution = "University Of
 *             Pretoria", school = "Computer Science", year = "2004", month = nov, address =
 *             "Pretoria, South Africa", note = "Supervisor: A. P. Engelbrecht", }
 * NOTE: By default, the cluster center refers to the cluster centroid. See {@link ClusterCenterStrategy}.
 */
public class QuantisationErrorFunction extends ClusteringFitnessFunction {
    private static final long serialVersionUID = -7008338250315442786L;

    public QuantisationErrorFunction() {
        super();
    }

    @Override
    public double calculateFitness() {
        return calculateQuantisationError();
    }
}
