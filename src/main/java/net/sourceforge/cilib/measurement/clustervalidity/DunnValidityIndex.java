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

import java.util.ArrayList;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Kristina
 */
public class DunnValidityIndex extends ValidityIndex{
    public DunnValidityIndex() {
        
    }
    
    public DunnValidityIndex(DunnValidityIndex copy) {
        
    }
    
    @Override
    public DunnValidityIndex getClone() {
        return new DunnValidityIndex(this);
    }
    
    @Override
    public Real getValue(Algorithm algorithm) {
        CentroidHolder holder = (CentroidHolder) algorithm.getBestSolution().getPosition();
        double minimum = Double.POSITIVE_INFINITY;
        for(ClusterCentroid centroid1 : holder) {
            CentroidHolder holder2 = holder.getClone();
            holder2.remove(centroid1);
            double min = Double.POSITIVE_INFINITY;
            for(ClusterCentroid centroid2 : holder2) {
                double result = getMinimumIntraclusterDistance(centroid1, centroid2) / (double) getMaximumInterclusterDistance(centroid1);
                if(result < min) {
                    min = result;
                }
            }
            
            if(min < minimum) {
                minimum = min;
            }
        }
        
        return Real.valueOf(minimum);
        
    }
    
    protected double getMinimumIntraclusterDistance(ClusterCentroid cluster1, ClusterCentroid cluster2) {
        double minimumDistance = Double.POSITIVE_INFINITY;
        for(Vector pattern1 : cluster1.getDataItems()) {
            for(Vector pattern2 : cluster2.getDataItems()) {
                if(distanceMeasure.distance(pattern1, pattern2) < minimumDistance) {
                    minimumDistance = distanceMeasure.distance(pattern1, pattern2);
                }
            }
        }
        return minimumDistance;
    }
    
    protected double getMaximumInterclusterDistance(ClusterCentroid centroid) {
        double maximumDistance = 0;
        
        for(Vector pattern : centroid.getDataItems()) {
            ArrayList<Vector> patterns2 = (ArrayList<Vector>) centroid.getDataItems().clone();
            patterns2.remove(pattern);
            
            for(Vector pattern2 : patterns2) {
                if(distanceMeasure.distance(pattern, pattern2) > maximumDistance) {
                    maximumDistance = distanceMeasure.distance(pattern, pattern2);
                }
            }
        }
        
        return maximumDistance;
    }
}
