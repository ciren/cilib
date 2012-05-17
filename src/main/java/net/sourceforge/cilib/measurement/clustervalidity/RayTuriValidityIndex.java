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
package net.sourceforge.cilib.measurement.clustervalidity;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Kristina
 */
public class RayTuriValidityIndex extends ValidityIndex {
    @Override
    public Real getValue(Algorithm algorithm) {
        CentroidHolder holder = (CentroidHolder) algorithm.getBestSolution().getPosition();
        double result = getIntraclusterDistance(holder) / (double) getInterClusterDistance(holder);
        
        return Real.valueOf(result);
    }
    
    protected double getIntraclusterDistance(CentroidHolder centroidHolder) {
        double sum = 0;
        double numberOfPatrterns = 0;
        for(ClusterCentroid centroid :centroidHolder) {
            for(Vector pattern : centroid.getDataItems()) {
                sum += distanceMeasure.distance(pattern, centroid.toVector());
                numberOfPatrterns++;
            }
        }
        
        sum /= (double) numberOfPatrterns;
        
        return sum;
    }
    
    protected double getInterClusterDistance(CentroidHolder centroidHolder) {
        double minimum = Double.POSITIVE_INFINITY;
        for(ClusterCentroid centroid : centroidHolder) {
            CentroidHolder centroidHolder2 = centroidHolder.getClone();
            centroidHolder2.remove(centroid);
            
            for(ClusterCentroid centroid2 : centroidHolder2) {
                double distance = distanceMeasure.distance(centroid.toVector(), centroid2.toVector());
                if(distance < minimum) {
                    minimum = distance;
                }
            }
        }
        
        return minimum;
    }
}
