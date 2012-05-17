/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sourceforge.cilib.measurement.clustervalidity;

import junit.framework.Assert;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Kristina
 */
public class RayTuriFavouringValidityIndexTest {
    
    public RayTuriFavouringValidityIndexTest() {
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
     * Test of getGaussianValue method, of class RayTuriFavouringValidityIndex.
     */
    @Test
    public void testGetGaussianValue() {
        System.out.println("getGaussianValue");
        ClusterCentroid cluster1 = ClusterCentroid.of(1,5);
        ClusterCentroid cluster2 = ClusterCentroid.of(3,2);
        ClusterCentroid cluster3 = ClusterCentroid.of(3,2);
        
        CentroidHolder holder = new CentroidHolder();
        holder.add(cluster1);
        holder.add(cluster2);
        holder.add(cluster3);
        
        RayTuriFavouringValidityIndex instance = new RayTuriFavouringValidityIndex();
        double result = instance.getGaussianValue(holder);
        
        Assert.assertEquals(Math.round(0.00443184841194 * 1e10) / 1e10, Math.round(result * 1e10) / 1e10);
    }
}
