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
public class DaviesBouldinValidityIndex extends ValidityIndex{
    
    @Override
    public Real getValue(Algorithm algorithm) {
        CentroidHolder holder = (CentroidHolder) algorithm.getBestSolution().getPosition();
        double maximum = 0;
        for(ClusterCentroid centroid1 : holder) {
            CentroidHolder holder2 = holder.getClone();
            holder2.remove(centroid1);
            for(ClusterCentroid centroid2 : holder2) {
                double euclideanDistance = distanceMeasure.distance(centroid1, centroid2);
                double result = ((0.5 * getMaximumInterclusterDistance(centroid1)) + (0.5 * getMaximumInterclusterDistance(centroid2))) / (double) euclideanDistance;
                
                if(result > maximum) {
                    maximum = result;
                }
            }
        }
        
        maximum /= holder.size();
        
        return Real.valueOf(maximum);
    }
    
    protected double getMaximumInterclusterDistance(ClusterCentroid centroid) {
        double result = 0;
        for(Vector pattern : centroid.getDataItems()) { 
            result += distanceMeasure.distance(centroid.toVector(), pattern);
        }
        
        result = 2 * (result / centroid.getDataItems().size());
        
        return result;
    }
}
