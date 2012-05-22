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
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Kristina
 */
public class HalkidiVazirgiannisValidityIndex extends ValidityIndex {
    CentroidHolder centroidHolder;
    
    public HalkidiVazirgiannisValidityIndex() {
        super();
        centroidHolder = new CentroidHolder();
    }
    
    public HalkidiVazirgiannisValidityIndex(HalkidiVazirgiannisValidityIndex copy) {
        super(copy);
        centroidHolder = copy.centroidHolder;
    }
    
    @Override
    public HalkidiVazirgiannisValidityIndex getClone() {
        return new HalkidiVazirgiannisValidityIndex(this);
    }
    
    @Override
    public Real getValue(Algorithm algorithm) {
        centroidHolder = (CentroidHolder) algorithm.getBestSolution().getPosition();
        double result = getScattering() + getDensityAmongClusters();
        
        return Real.valueOf(result);
    }
    
    protected double getStandardDeviation() {
        double sum = 0;
        for(ClusterCentroid centroid : centroidHolder) {
            sum += (centroid.getDataItems().size() > 0) ? getVariance(centroid.getDataItems(), centroid.toVector()) : 0;
        }
        
        return sum / (double) centroidHolder.size();
    }
    
    protected double getVariance(ArrayList<Vector> patternList, Vector pattern) {
        double finalSum = 0;
        
        for(int i = 0; i < pattern.size(); i++) {
            double sum = 0;
            for(Vector otherPattern : patternList) {
                sum += Math.pow(otherPattern.get(i).doubleValue() - pattern.get(i).doubleValue(), 2);
            }

            sum = Math.pow(sum / patternList.size(), 2);
            finalSum += sum;
        }
        
        return Math.sqrt(finalSum);
    }
    
    protected Vector getMiddlePoint(ClusterCentroid cluster1, ClusterCentroid cluster2) {
        Vector result = Vector.copyOf(cluster1.toVector()).plus(cluster2.toVector());
        return Vector.copyOf(result).divide(2.0);
    }
    
    protected double getNeighbourhoodValue(Vector pattern, Vector middlePoint) {
        if(distanceMeasure.distance(pattern, middlePoint) > getStandardDeviation()) {
            return 0;
        }
        return 1;
    }
    
    protected double getDensity(Vector middlePoint) {
        ArrayList<Vector> allPatterns = getAllPatterns();
        
        double sum = 0;
        for(Vector pattern : allPatterns) {
            sum += getNeighbourhoodValue(pattern, middlePoint);
        }
        
        return sum;
    }
    
    protected double getDensityAmongClusters() {
        double valueToMultiply = 1 / (double) ((centroidHolder.size() * (centroidHolder.size() - 1)));
        double sum = 0;
        for(ClusterCentroid centroid : centroidHolder) {
            CentroidHolder centroidHolder2 = centroidHolder.getClone();
            centroidHolder2.remove(centroid);
            
            for(ClusterCentroid centroid2 : centroidHolder2) {
               sum += (getDensity(getMiddlePoint(centroid, centroid2)) / ((double) Math.max(getDensity(centroid.toVector()), getDensity(centroid2.toVector()))));
            }
        }
        
        return valueToMultiply * sum;
    }
    
    protected ArrayList<Vector> getAllPatterns() {
        ArrayList<Vector> allPatterns = new ArrayList<Vector>();
        for(ClusterCentroid centroid : centroidHolder) {
            allPatterns.addAll(centroid.getDataItems());
        }
        
        return allPatterns;
    }
    
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
    
    protected double getScattering() {
        double sum = 0;
        
        for(ClusterCentroid centroid : centroidHolder) {
            sum += getVariance(centroid.getDataItems(), centroid.toVector()) / (double) getVariance(getAllPatterns(), getMiddlePointOfDataset());
        }
        
        return sum / (double) centroidHolder.size();
    }
    
    public void setCentroidHolder(CentroidHolder holder) {
        centroidHolder = holder;
    }
    
    public CentroidHolder getCentroidHolder() {
        return centroidHolder;
    }
}
