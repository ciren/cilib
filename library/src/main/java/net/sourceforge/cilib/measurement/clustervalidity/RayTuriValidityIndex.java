/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.clustervalidity;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This class calculates the Ray Tury Validity Index that can be found in:
 * {@literal@}{Graaff11,
 *  author = {Graaff A. J. and Engelbrecht A. P.},
 *  title = {A local network neighbourhood artificial immune system},
 *  year = {2011},
 *  }
 */
public class RayTuriValidityIndex extends ValidityIndex {
    /*
     * Default constructor for RayTuriValidityIndex
     */
    public RayTuriValidityIndex() {
        super();
    }
    
    /*
     * Copy constructor for RayTuriValidityIndex
     */
    public RayTuriValidityIndex(RayTuriValidityIndex copy) {
        super(copy);
    }
    
    /*
     * Calculates the Ray Turi Validity Index
     * @param algorithm The algorithm for which the validity index is being calculated
     * @return result The validity index value
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        CentroidHolder holder = (CentroidHolder) algorithm.getBestSolution().getPosition();
        double result = getaverageClusterDistance(holder) / (double) getInterClusterDistance(holder);
        
        return Real.valueOf(result);
    }
    
    /*
     * Calculates the average distance between all centroid and their patterns
     * @param centroidHolder The set of centroids
     * @return distace The average distance
     */
    protected double getaverageClusterDistance(CentroidHolder centroidHolder) {
        double sum = 0;
        double numberOfPatterns = 0;
        for(ClusterCentroid centroid :centroidHolder) {
            for(Vector pattern : centroid.getDataItems()) {
                sum += distanceMeasure.distance(pattern, centroid.toVector());
                numberOfPatterns++;
            }
        }
        
        sum /= (double) numberOfPatterns;
        
        return sum;
    }
    
    /*
     * Calculates the minimum distance between all centroids
     * @param centoidHolder The set of centroids to be checked
     * @return minimumDistance the smallest distance between clusters
     */
    protected double getInterClusterDistance(CentroidHolder centroidHolder) {
        double minimum = Double.POSITIVE_INFINITY;
        CentroidHolder centroidHolder2;
        double distance;
        for(ClusterCentroid centroid : centroidHolder) {
            centroidHolder2 = centroidHolder.getClone();
            centroidHolder2.remove(centroid);
            
            for(ClusterCentroid centroid2 : centroidHolder2) {
                distance = distanceMeasure.distance(centroid.toVector(), centroid2.toVector());
                if(distance < minimum) {
                    minimum = distance;
                }
            }
        }
        
        return minimum;
    }

    /*
     * Clone method for RayTuriValidityIndex
     */
    @Override
    public Measurement<Real> getClone() {
        return new RayTuriValidityIndex(this);
    }
}
