/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.clustervalidity;

import java.util.ArrayList;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This class calculates the Dunn Validity Index that can be found in:
 * {@literal@}{Graaff11,
 *  author = {Graaff A. J. and Engelbrecht A. P.},
 *  title = {A local network neighbourhood artificial immune system},
 *  year = {2011},
 *  }
 */
public class DunnValidityIndex extends ValidityIndex{
    /*
     * Default constructor for DunnValidityIndex
     */
    public DunnValidityIndex() {
        super();
    }
    
    /*
     * Copy Constructor for DunnValidityIndex
     */
    public DunnValidityIndex(DunnValidityIndex copy) {
        super(copy);
    }
    
    /*
     * Clone method for DunnValidityIndex
     * @return new instance of HalkidiVazirgiannisValidityIndex
     */
    @Override
    public DunnValidityIndex getClone() {
        return new DunnValidityIndex(this);
    }
    
    /*
     * Calculates and returns the Dunn Validity Index
     * @param algorithm The algorithm for which the validity index is being calculated
     * @return result The result of the calculateion of the validity index
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        CentroidHolder holder = (CentroidHolder) algorithm.getBestSolution().getPosition();
        double minimum = Double.POSITIVE_INFINITY;
        CentroidHolder holder2;
        double min;
        double result;
        
        for(ClusterCentroid centroid1 : holder) {
            holder2 = holder.getClone();
            holder2.remove(centroid1);
            min = Double.POSITIVE_INFINITY;
            for(ClusterCentroid centroid2 : holder2) {
                result = getMinimumIntraclusterDistance(centroid1, centroid2) / (double) getMaximumInterclusterDistance(centroid1);
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
    
    /*
     * Calculates the smallest distance between two clusters
     * @param cluster1 One of the clusters to be compared
     * @param cluster2 The cluster cluster1 is compared against
     * @return minimumDistance the minimum distance between the two clusters
     */
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
    
    /*
     * Calculates the maximum distance between patterns within a cluster\
     * @param centroid The cluster to be checked
     */
    protected double getMaximumInterclusterDistance(ClusterCentroid centroid) {
        double maximumDistance = 0;
        ArrayList<Vector> patterns2;
        
        for(Vector pattern : centroid.getDataItems()) {
            patterns2 = (ArrayList<Vector>) centroid.getDataItems().clone();
            patterns2.remove(pattern);
            
            for(Vector pattern2 : patterns2) {
                if(distanceMeasure.distance(pattern, pattern2) > maximumDistance) {
                    maximumDistance = distanceMeasure.distance(pattern, pattern2);
                }
            }
        }
        
        return maximumDistance + Double.MIN_VALUE;
    }
}
