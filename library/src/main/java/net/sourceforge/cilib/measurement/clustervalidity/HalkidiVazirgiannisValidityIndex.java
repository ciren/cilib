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
 * This class calculates the Halkidi Vazirgiannis Validity Index that can be found in:
 * {@literal@}{Graaff11,
 *  author = {Graaff A. J. and Engelbrecht A. P.},
 *  title = {A local network neighbourhood artificial immune system},
 *  year = {2011},
 *  }
 */
public class HalkidiVazirgiannisValidityIndex extends ValidityIndex {
    CentroidHolder centroidHolder;
    
    /*
     * Default constructor for HalkidiVazirgiannisValidityIndex
     */
    public HalkidiVazirgiannisValidityIndex() {
        super();
        centroidHolder = new CentroidHolder();
    }
    
    /*
     * Copy constructor for HalkidiVazirgiannisValidityIndex
     */
    public HalkidiVazirgiannisValidityIndex(HalkidiVazirgiannisValidityIndex copy) {
        super(copy);
        centroidHolder = copy.centroidHolder;
    }
    
    /*
     * Clone method for HalkidiVazirgiannisValidityIndex
     * @return new instance of HalkidiVazirgiannisValidityIndex
     */
    @Override
    public HalkidiVazirgiannisValidityIndex getClone() {
        return new HalkidiVazirgiannisValidityIndex(this);
    }
    
    /*
     * Calculates the Halkidi Vazirgiannis Validity Index
     * @param algorithm The algorithm for which the validity index is being calculated
     * @return result The result of calculating the Validity Index
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        centroidHolder = (CentroidHolder) algorithm.getBestSolution().getPosition();
        double result = getScattering() + getDensityAmongClusters();
        return Real.valueOf(result);
    }
    
    /*
     * Calculates the standard deviation
     * @return result The standard deviation
     */
    protected double getStandardDeviation() {
        double sum = 0;
        for(ClusterCentroid centroid : centroidHolder) {
            sum += getVariance(centroid.getDataItems(), centroid.toVector());
        }
        
        return sum / (double) centroidHolder.size();
    }
    
    /*
     * Calculates the variance between a centroid and a list of patterns
     * @param patternlist The list of data patterns
     * @param pattern The centroid
     * @return variance The variance
     */
    protected double getVariance(ArrayList<Vector> patternList, Vector pattern) {
        double finalSum = 0;
        double sum;
        for(int i = 0; i < pattern.size(); i++) {
            sum = 0;
            for(Vector otherPattern : patternList) {
                sum += Math.pow(otherPattern.get(i).doubleValue() - pattern.get(i).doubleValue(), 2);
            }
            
            sum = (!patternList.isEmpty()) ? Math.pow(sum / patternList.size(), 2) : 0;
            finalSum += sum;
        }
        
        return Math.sqrt(finalSum);
    }
    
    /*
     * Calculates the middle point between two clusters
     * @param cluster1 one of the clusters to be compared
     * @param cluster2 the other cluster to be compared to cluster1
     * @return middlePoint The middle point between the two clusters
     */
    protected Vector getMiddlePoint(ClusterCentroid cluster1, ClusterCentroid cluster2) {
        Vector result = Vector.copyOf(cluster1.toVector()).plus(cluster2.toVector());
        return Vector.copyOf(result).divide(2.0);
    }
    
    /*
     * Calculates the neighbourhood value
     * @param pattern A data pattern
     * @param the middle point between two clusters
     * @return neighbourhoodValue The neighbourhood value
     */
    protected double getNeighbourhoodValue(Vector pattern, Vector middlePoint) {
        if(distanceMeasure.distance(pattern, middlePoint) > getStandardDeviation()) {
            return 0;
        }
        return 1;
    }
    
    /*
     * Calculates the density
     * @param middlePoint the middle point between two clusters
     * @return result the density
     */
    protected double getDensity(Vector middlePoint) {
        ArrayList<Vector> allPatterns = getAllPatterns();
        double sum = 0;
        for(Vector pattern : allPatterns) {
            sum += getNeighbourhoodValue(pattern, middlePoint);
        }
        
        return sum;
    }
    
    /*
     * Calculates the density among all clusters
     * @return resut The density among all clusters
     */
    protected double getDensityAmongClusters() {
        double valueToMultiply = 1 / (double) ((centroidHolder.size() * (centroidHolder.size() - 1)));
        double sum = 0;
        CentroidHolder centroidHolder2;
        for(ClusterCentroid centroid : centroidHolder) {
            centroidHolder2 = centroidHolder.getClone();
            centroidHolder2.remove(centroid);
            
            for(ClusterCentroid centroid2 : centroidHolder2) {
               sum += (getDensity(getMiddlePoint(centroid, centroid2)) / ((double) Math.max(getDensity(centroid.toVector()), getDensity(centroid2.toVector()))));
            }
        }
        
        return valueToMultiply * sum;
    }
    
    /*
     * Returns a list of all patterns in the dataset
     * @return list The list of all patterns in the dataset
     */
    protected ArrayList<Vector> getAllPatterns() {
        ArrayList<Vector> allPatterns = new ArrayList<Vector>();
        for(ClusterCentroid centroid : centroidHolder) {
            allPatterns.addAll(centroid.getDataItems());
        }
        return allPatterns;
    }
    
    /*
     * Calculates the midle point of the entire dataset
     * @return middlePoint The midle point of the entire dataset
     */
    protected Vector getMiddlePointOfDataset() {
        ArrayList<Vector> allPatterns = getAllPatterns();
        
        Vector.Builder builder = Vector.newBuilder();
        for(int i = 0; i < allPatterns.get(0).size(); i++) {
            builder.add(0);
        }
        
        Vector sum = builder.build();
        
        for(Vector pattern : allPatterns) {
            sum = Vector.copyOf(sum).plus(pattern);
        }
        
        return Vector.copyOf(sum).divide(allPatterns.size());
        
    }
    
    /*
     * Calculates the scattering of the clusters
     * @return scattering The scattering of the clusters
     */
    protected double getScattering() {
        double sum = 0;
        
        for(ClusterCentroid centroid : centroidHolder) {
            sum += getVariance(centroid.getDataItems(), centroid.toVector()) / (double) getVariance(getAllPatterns(), getMiddlePointOfDataset());
        }
        
        return sum / (double) centroidHolder.size();
    }
    
}
