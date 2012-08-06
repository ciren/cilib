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
package net.sourceforge.cilib.measurement.multiple;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Kristina
 */
public class ClusterCentroids implements Measurement<Vector>{
    int dimension;
    
    public ClusterCentroids() {
        dimension = 0;
    }
    
    public ClusterCentroids(ClusterCentroids copy) {
        dimension = copy.dimension;
    }
    
    @Override
    public ClusterCentroids getClone() {
        return new ClusterCentroids(this);
    }
    
    @Override
    public String getDomain() {
        return "T";
    }

    @Override
    public Vector getValue(Algorithm algorithm) {
        //return (CentroidHolder) algorithm.getBestSolution().getPosition();
        CentroidHolder holder = (CentroidHolder) algorithm.getBestSolution().getPosition();
        
        return holder.get(dimension).toVector();
    }
    
    public void setDimension(int dim) {
        dimension = dim;
    }
    
}
