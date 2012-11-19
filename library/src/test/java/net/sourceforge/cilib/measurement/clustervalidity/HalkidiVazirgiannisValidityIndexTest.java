/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.clustervalidity;

import java.util.ArrayList;
import junit.framework.Assert;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Test;

public class HalkidiVazirgiannisValidityIndexTest {
    
    /**
     * Test of getStandardDeviation method, of class HalkidiVazirgiannisValidityIndex.
     */
    @Test
    public void testGetStandardDeviation() {
        ClusterCentroid cluster1 = ClusterCentroid.of(1,2);
        cluster1.addDataItem(0, Vector.of(2,2));
        cluster1.addDataItem(0, Vector.of(3,1));
        ClusterCentroid cluster2 = ClusterCentroid.of(3,4);
        cluster2.addDataItem(0, Vector.of(6,1));
        cluster2.addDataItem(0, Vector.of(2,4));
        
        CentroidHolder holder = new CentroidHolder();
        holder.add(cluster1);
        holder.add(cluster2);
        
        HalkidiVazirgiannisValidityIndex instance = new HalkidiVazirgiannisValidityIndex();
        instance.centroidHolder = holder;
        
        double result = instance.getStandardDeviation();
        
        Assert.assertEquals(Math.round(4.6381608901666237867978273927983 * 1e10) / 1e10, Math.round(result * 1e10) / 1e10);
    }

    /**
     * Test of getVariance method, of class HalkidiVazirgiannisValidityIndex.
     */
    @Test
    public void testGetVariance() {
        ClusterCentroid cluster1 = ClusterCentroid.of(1,2);
        cluster1.addDataItem(0, Vector.of(2,2));
        cluster1.addDataItem(0, Vector.of(3,1));
        ClusterCentroid cluster2 = ClusterCentroid.of(3,4);
        cluster2.addDataItem(0, Vector.of(6,1));
        cluster2.addDataItem(0, Vector.of(2,4));
        cluster2.addDataItem(0, Vector.of(4,5));
        
        CentroidHolder holder = new CentroidHolder();
        holder.add(cluster1);
        holder.add(cluster2);
        
        HalkidiVazirgiannisValidityIndex instance = new HalkidiVazirgiannisValidityIndex();
        instance.centroidHolder = holder;
        
        double result = instance.getVariance(cluster1.getDataItems(), cluster1.toVector());
        
        Assert.assertEquals(Math.round(2.5495097567963924150141120545114 * 1e10) / 1e10, Math.round(result * 1e10) / 1e10);
        
    }

    /**
     * Test of getMiddlePoint method, of class HalkidiVazirgiannisValidityIndex.
     */
    @Test
    public void testGetMiddlePoint() {
        ClusterCentroid cluster1 = ClusterCentroid.of(1,2);
        ClusterCentroid cluster2 = ClusterCentroid.of(3,4);
        
        CentroidHolder holder = new CentroidHolder();
        holder.add(cluster1);
        holder.add(cluster2);
        
        HalkidiVazirgiannisValidityIndex instance = new HalkidiVazirgiannisValidityIndex();
        Vector result = instance.getMiddlePoint(cluster1, cluster2);
        
        Assert.assertTrue(result.containsAll(Vector.of(2,3)));
    }

    /**
     * Test of getNeighbourhoodValue method, of class HalkidiVazirgiannisValidityIndex.
     */
    @Test
    public void testGetNeighbourhoodValue() {
        ClusterCentroid cluster1 = ClusterCentroid.of(1,2);
        cluster1.addDataItem(0, Vector.of(2,2));
        cluster1.addDataItem(0, Vector.of(3,1));
        ClusterCentroid cluster2 = ClusterCentroid.of(3,4);
        cluster2.addDataItem(0, Vector.of(6,1));
        cluster2.addDataItem(0, Vector.of(2,4));
        
        CentroidHolder holder = new CentroidHolder();
        holder.add(cluster1);
        holder.add(cluster2);
        
        HalkidiVazirgiannisValidityIndex instance = new HalkidiVazirgiannisValidityIndex();
        instance.centroidHolder = holder;
        
        double result = instance.getNeighbourhoodValue(Vector.of(2,2), cluster1.toVector());
        
        Assert.assertEquals(1.0, result);
        
        result = instance.getNeighbourhoodValue(Vector.of(6,1), cluster1.toVector());
        
        Assert.assertEquals(0.0, result);
    }

    /**
     * Test of getDensity method, of class HalkidiVazirgiannisValidityIndex.
     */
    @Test
    public void testGetDensity() {
        ClusterCentroid cluster1 = ClusterCentroid.of(1,2);
        cluster1.addDataItem(0, Vector.of(2,2));
        cluster1.addDataItem(0, Vector.of(3,1));
        ClusterCentroid cluster2 = ClusterCentroid.of(3,4);
        cluster2.addDataItem(0, Vector.of(6,1));
        cluster2.addDataItem(0, Vector.of(2,4));
        
        CentroidHolder holder = new CentroidHolder();
        holder.add(cluster1);
        holder.add(cluster2);
        
        HalkidiVazirgiannisValidityIndex instance = new HalkidiVazirgiannisValidityIndex();
        instance.centroidHolder = holder;
        
        double result = instance.getDensity(Vector.of(2,3));
        
        Assert.assertEquals(4.0, result);
        
    }

    /**
     * Test of getDensityAmongClusters method, of class HalkidiVazirgiannisValidityIndex.
     */
    @Test
    public void testGetDensityAmongClusters() {
        ClusterCentroid cluster1 = ClusterCentroid.of(1,2);
        cluster1.addDataItem(0, Vector.of(2,2));
        cluster1.addDataItem(0, Vector.of(3,1));
        ClusterCentroid cluster2 = ClusterCentroid.of(3,4);
        cluster2.addDataItem(0, Vector.of(6,1));
        cluster2.addDataItem(0, Vector.of(2,4));
        
        CentroidHolder holder = new CentroidHolder();
        holder.add(cluster1);
        holder.add(cluster2);
        
        HalkidiVazirgiannisValidityIndex instance = new HalkidiVazirgiannisValidityIndex();
        instance.centroidHolder = holder;
        
        double result = instance.getDensityAmongClusters();
        
        Assert.assertEquals(1.0, result);
    }

    /**
     * Test of getAllPatterns method, of class HalkidiVazirgiannisValidityIndex.
     */
    @Test
    public void testGetAllPatterns() {
        ClusterCentroid cluster1 = ClusterCentroid.of(1,2);
        cluster1.addDataItem(0, Vector.of(2,2));
        cluster1.addDataItem(0, Vector.of(3,1));
        ClusterCentroid cluster2 = ClusterCentroid.of(3,4);
        cluster2.addDataItem(0, Vector.of(6,1));
        cluster2.addDataItem(0, Vector.of(2,4));
        
        CentroidHolder holder = new CentroidHolder();
        holder.add(cluster1);
        holder.add(cluster2);
        
        HalkidiVazirgiannisValidityIndex instance = new HalkidiVazirgiannisValidityIndex();
        instance.centroidHolder = holder;
        
        ArrayList<Vector> result = instance.getAllPatterns();
        ArrayList<Vector> expected = new ArrayList<Vector>();
        expected.add(Vector.of(2,2));
        expected.add(Vector.of(3,1));
        expected.add(Vector.of(6,1));
        expected.add(Vector.of(2,4));
    }

    /**
     * Test of getMiddlePointOfDataset method, of class HalkidiVazirgiannisValidityIndex.
     */
    @Test
    public void testGetMiddlePointOfDataset() {
        ClusterCentroid cluster1 = ClusterCentroid.of(1,2);
        cluster1.addDataItem(0, Vector.of(2,2));
        cluster1.addDataItem(0, Vector.of(3,1));
        ClusterCentroid cluster2 = ClusterCentroid.of(3,4);
        cluster2.addDataItem(0, Vector.of(6,1));
        cluster2.addDataItem(0, Vector.of(2,4));
        
        CentroidHolder holder = new CentroidHolder();
        holder.add(cluster1);
        holder.add(cluster2);
        
        HalkidiVazirgiannisValidityIndex instance = new HalkidiVazirgiannisValidityIndex();
        instance.centroidHolder = holder;
        
        Vector result = instance.getMiddlePointOfDataset();
        Vector expected = Vector.of(3.25,2);
        
        Assert.assertEquals(expected.get(0).doubleValue(), result.get(0).doubleValue());
        Assert.assertEquals(expected.get(1).doubleValue(), result.get(1).doubleValue());
    
    }

    /**
     * Test of getScattering method, of class HalkidiVazirgiannisValidityIndex.
     */
    @Test
    public void testGetScattering() {
        ClusterCentroid cluster1 = ClusterCentroid.of(1,2);
        cluster1.addDataItem(0, Vector.of(2,2));
        cluster1.addDataItem(0, Vector.of(3,1));
        ClusterCentroid cluster2 = ClusterCentroid.of(3,4);
        cluster2.addDataItem(0, Vector.of(6,1));
        cluster2.addDataItem(0, Vector.of(2,4));
        
        CentroidHolder holder = new CentroidHolder();
        holder.add(cluster1);
        holder.add(cluster2);
        
        HalkidiVazirgiannisValidityIndex instance = new HalkidiVazirgiannisValidityIndex();
        instance.centroidHolder = holder;
        
        double result = instance.getScattering();
        
        Assert.assertEquals(Math.round(1.5069884393930925742619778441626 * 1e10) / 1e10, Math.round(result * 1e10) / 1e10);
    }
}
