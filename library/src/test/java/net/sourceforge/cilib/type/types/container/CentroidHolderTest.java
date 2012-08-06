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
package net.sourceforge.cilib.type.types.container;

import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import org.junit.Assert;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CentroidHolderTest {
    
    public CentroidHolderTest() {
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
     * Test of size method, of class CentroidHolder.
     */
    @Test
    public void testSize() {
        Vector input = Vector.of(1,2,3,4);
        CentroidHolder instance = new CentroidHolder();
        instance.add(ClusterCentroid.of(1,2,3,4)) ;
        Assert.assertEquals(instance.size(), 1);
    }

    /**
     * Test of isEmpty method, of class CentroidHolder.
     */
    @Test
    public void testIsEmpty() {
        CentroidHolder instance = new CentroidHolder();
        Assert.assertTrue(instance.isEmpty());
    }

    /**
     * Test of contains method, of class CentroidHolder.
     */
    @Test
    public void testContains() {
        CentroidHolder instance = new CentroidHolder();
        ClusterCentroid x = ClusterCentroid.of(6,6,0,8);
        CentroidHolder centroids = new CentroidHolder();
        centroids.add(ClusterCentroid.of(5,8,9,3));
        centroids.add(ClusterCentroid.of(2,8,0,5));
        instance.addAll(centroids);
        instance.add(x);
        
        Assert.assertTrue(instance.contains(x));
    }

    /**
     * Test of add method, of class CentroidHolder.
     */
    @Test
    public void testAdd() {
        CentroidHolder instance = new CentroidHolder();
        ClusterCentroid x = ClusterCentroid.of(0,2,4,8);
        instance.add(x);
        Assert.assertTrue(instance.contains(x));
    }

    /**
     * Test of remove method, of class CentroidHolder.
     */
    @Test
    public void testRemove() {
        CentroidHolder instance = new CentroidHolder();
        CentroidHolder centroids = new CentroidHolder();
        centroids.add(ClusterCentroid.of(5,8,9,3));
        centroids.add(ClusterCentroid.of(2,8,0,5));
        instance.addAll(centroids);
        ClusterCentroid c = ClusterCentroid.of(0,7,7,5);
        instance.add(c);
        assertTrue(instance.contains(c));
        instance.remove(c);
        Assert.assertFalse(instance.contains(c));
    }

    /**
     * Test of containsAll method, of class CentroidHolder.
     */
    @Test
    public void testContainsAll() {
        CentroidHolder instance = new CentroidHolder();
        CentroidHolder centroids = new CentroidHolder();
        centroids.add(ClusterCentroid.of(5,8,9,3));
        centroids.add(ClusterCentroid.of(2,8,0,5));
        instance.addAll(centroids);
        
        Assert.assertTrue(instance.containsAll(centroids));
    }

    /**
     * Test of addAll method, of class CentroidHolder.
     */
    @Test
    public void testAddAll() {
        CentroidHolder instance = new CentroidHolder();
        CentroidHolder centroids = new CentroidHolder();
        centroids.add(ClusterCentroid.of(5,8,9,3));
        centroids.add(ClusterCentroid.of(2,8,0,5));
        instance.addAll(centroids);
        
        Assert.assertTrue(instance.containsAll(centroids));
    }

    /**
     * Test of clear method, of class CentroidHolder.
     */
    @Test
    public void testClear() {
        ClusterCentroid instance = new ClusterCentroid();
        instance.addAll(ClusterCentroid.of(5,2,4,6));
        instance.clear();
        Assert.assertTrue(instance.isEmpty());
    }

    /**
     * Test of randomize method, of class CentroidHolder.
     */
    @Test
    public void testRandomize() {
        RandomProvider random = new MersenneTwister();
        CentroidHolder instance = new CentroidHolder();
        CentroidHolder centroids = new CentroidHolder();
        ClusterCentroid c1 = ClusterCentroid.of(5,8,9,3);
        ClusterCentroid c2 = ClusterCentroid.of(2,8,0,5);
        centroids.add(c1);
        centroids.add(c2);
        instance.addAll(centroids);
        instance.randomize(random);
        
        Assert.assertFalse(instance.get(0).containsAll(Vector.of(5,8,9,3)));
        Assert.assertFalse(instance.get(1).containsAll(Vector.of(2,8,0,5)));
    }

    /**
     * Test of get method, of class CentroidHolder.
     */
    @Test
    public void testGet() {
        CentroidHolder instance = new CentroidHolder();
        CentroidHolder centroids = new CentroidHolder();
        ClusterCentroid c1 = ClusterCentroid.of(5,8,9,3);
        centroids.add(c1);
        centroids.add(ClusterCentroid.of(2,8,0,5));
        instance.addAll(centroids);
        
        Assert.assertEquals(instance.get(0), c1);
    }
}
