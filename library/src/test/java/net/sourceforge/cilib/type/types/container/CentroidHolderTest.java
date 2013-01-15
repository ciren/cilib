/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types.container;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;

public class CentroidHolderTest {
    
    /**
     * Test of size method, of class CentroidHolder.
     */
    @Test
    public void testSize() {
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
     * Test of randomise method, of class CentroidHolder.
     */
    @Test
    public void testRandomize() {
        CentroidHolder instance = new CentroidHolder();
        CentroidHolder centroids = new CentroidHolder();
        ClusterCentroid c1 = ClusterCentroid.of(5,8,9,3);
        ClusterCentroid c2 = ClusterCentroid.of(2,8,0,5);
        centroids.add(c1);
        centroids.add(c2);
        instance.addAll(centroids);
        instance.randomise();
        
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
