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
import junit.framework.Assert;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class HalkidiVazirgiannisValidityIndexTest {
    
    public HalkidiVazirgiannisValidityIndexTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getStandardDeviation method, of class HalkidiVazirgiannisValidityIndex.
     */
    @Test
    public void testGetStandardDeviation() {
        System.out.println("getStandardDeviation");
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
        System.out.println("getVariance");
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
        System.out.println("getMiddlePoint");
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
        System.out.println("getNeighbourhoodValue");
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
        System.out.println("getDensity");
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
        System.out.println("getDensityAmongClusters");
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
        System.out.println("getAllPatterns");
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
        System.out.println("getMiddlePointOfDataset");
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
        System.out.println("getScattering");
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
