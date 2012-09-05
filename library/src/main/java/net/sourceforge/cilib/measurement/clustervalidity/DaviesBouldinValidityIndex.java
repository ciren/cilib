/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
        CentroidHolder holder2;
        double maxInterClusterDistanceC1;
        double euclideanDistance;
        double maxInterClusterDistanceC2;
        double result;
        
        for(ClusterCentroid centroid1 : holder) {
            holder2 = holder.getClone();
            holder2.remove(centroid1);
            for(ClusterCentroid centroid2 : holder2) {
                euclideanDistance = distanceMeasure.distance(centroid1, centroid2);
                maxInterClusterDistanceC1 = (centroid1.getDataItemDistances().length > 0) ? getMaximumInterclusterDistance(centroid1) : 0;
                maxInterClusterDistanceC2 = (centroid2.getDataItemDistances().length > 0) ? getMaximumInterclusterDistance(centroid2) : 0;
                result = ((0.5 * maxInterClusterDistanceC1) + (0.5 * maxInterClusterDistanceC2)) / (double) euclideanDistance;
                
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
