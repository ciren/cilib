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
 * This class calculates the Davies Bouldin Validity Index that can be found in:
 * {@literal@}{Graaff11,
 *  author = {Graaff A. J. and Engelbrecht A. P.},
 *  title = {A local network neighbourhood artificial immune system},
 *  year = {2011},
 *  }
 */
public class DaviesBouldinValidityIndex extends ValidityIndex{
    /*
     * Default constructor for DaviesBouldinValidityIndex
     */
    public DaviesBouldinValidityIndex() {
        super();
    }
    
    /*
     * Copy constructor for DaviesBouldinValidityIndex
     * @param copy The DaviesBouldinValidityIndex to be copied
     */
    public DaviesBouldinValidityIndex(DaviesBouldinValidityIndex copy) {
        super(copy);
    }
    
    /*
     * Clone method for DaviesBouldinValidityIndex
     * @return new instance of HalkidiVazirgiannisValidityIndex
     */
    @Override
    public DaviesBouldinValidityIndex getClone() {
        return new DaviesBouldinValidityIndex(this);
    }
    
    /*
     * Returns the result of the DaviesBouldinValidityIndex calculation
     * 
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        CentroidHolder holder = (CentroidHolder) algorithm.getBestSolution().getPosition();
        double maximum = 0;
        for(ClusterCentroid centroid1 : holder) {
            CentroidHolder holder2 = holder.getClone();
            holder2.remove(centroid1);
            for(ClusterCentroid centroid2 : holder2) {
                double euclideanDistance = distanceMeasure.distance(centroid1, centroid2);
                double maxInterClusterDistanceC1 = (centroid1.getDataItemDistances().length > 0) ? getMaximumInterclusterDistance(centroid1) : 0;
                double maxInterClusterDistanceC2 = (centroid2.getDataItemDistances().length > 0) ? getMaximumInterclusterDistance(centroid2) : 0;
                double result = ((0.5 * maxInterClusterDistanceC1) + (0.5 * maxInterClusterDistanceC2)) / (double) euclideanDistance;
                
                if(result > maximum) {
                    maximum = result;
                }
            }
        }
        
        maximum /= holder.size();
        
        return Real.valueOf(maximum);
    }
    
    /*
     * Returns the maximum distance between 2 patterns within the same cluster
     * @param centroid The cluster to be checked
     * @return result The maximum distance
     */
    protected double getMaximumInterclusterDistance(ClusterCentroid centroid) {
        double result = 0;
        for(Vector pattern : centroid.getDataItems()) { 
            result += distanceMeasure.distance(centroid.toVector(), pattern);
        }
        
        result = 2 * (result / centroid.getDataItems().size());
        
        return result;
    }
}
