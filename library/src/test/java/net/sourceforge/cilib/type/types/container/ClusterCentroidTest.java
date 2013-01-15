/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.types.container;

import junit.framework.Assert;
import net.sourceforge.cilib.type.types.Int;
import static org.junit.Assert.*;
import org.junit.Test;

public class ClusterCentroidTest {
    
    /**
     * Test of copy method, of class ClusterCentroid.
     */
    @Test
    public void testCopy() {
        Vector input = Vector.of(1,2,3,4);
        ClusterCentroid instance = new ClusterCentroid();
        instance.copy(input);
        Assert.assertTrue(instance.containsAll(input));
    }

    /**
     * Test of size method, of class ClusterCentroid.
     */
    @Test
    public void testSize() {
        Vector input = Vector.of(1,2,3,4);
        ClusterCentroid instance = new ClusterCentroid();
        instance.copy(input);
        Assert.assertEquals(instance.size(), 4);
    }

    /**
     * Test of isEmpty method, of class ClusterCentroid.
     */
    @Test
    public void testIsEmpty() {
        ClusterCentroid instance = new ClusterCentroid();
        Assert.assertTrue(instance.isEmpty());
    }

    /**
     * Test of add method, of class ClusterCentroid.
     */
    @Test
    public void testAdd() {
        ClusterCentroid instance = new ClusterCentroid();
        instance.add(Int.valueOf(5));
        Assert.assertTrue(instance.contains(Int.valueOf(5)));
    }

    /**
     * Test of remove method, of class ClusterCentroid.
     */
    @Test
    public void testRemove() {
        ClusterCentroid instance = new ClusterCentroid();
        instance.addAll(Vector.of(5,8,9,3));
        Int integer = Int.valueOf(7);
        instance.add(integer);
        assertTrue(instance.contains(Int.valueOf(7)));
        instance.remove(Int.valueOf(7));
        Assert.assertFalse(instance.contains(Int.valueOf(7)));
    }

    /**
     * Test of containsAll method, of class ClusterCentroid.
     */
    @Test
    public void testContainsAll() {
        ClusterCentroid instance = new ClusterCentroid();
        Vector numbers = Vector.of(5,8,9,2,3);
        instance.addAll(numbers);
        
        Assert.assertTrue(instance.containsAll(numbers));
    }

    /**
     * Test of contains method, of class ClusterCentroid.
     */
    @Test
    public void testContains() {
        ClusterCentroid instance = new ClusterCentroid();
        Int x = Int.valueOf(2);
        Vector numbers = Vector.of(5,8,9,3);
        instance.addAll(numbers);
        instance.add(x);
        
        Assert.assertTrue(instance.contains(x));
    }

    /**
     * Test of addAll method, of class ClusterCentroid.
     */
    @Test
    public void testAddAll() {
        ClusterCentroid instance = new ClusterCentroid();
        Vector numbers = Vector.of(5,8,9,3);
        instance.addAll(numbers);
        
         Assert.assertTrue(instance.containsAll(numbers));
    }

    /**
     * Test of removeAll method, of class ClusterCentroid.
     */
    @Test
    public void testRemoveAll() {
        ClusterCentroid instance = new ClusterCentroid();
        instance.addAll(Vector.of(5,8,9,3));
        assertTrue(instance.containsAll(Vector.of(5,8,9,3)));
        instance.removeAll(Vector.of(5,8));
        Assert.assertFalse(instance.containsAll(Vector.of(9,3)));
    }

    /**
     * Test of clear method, of class ClusterCentroid.
     */
    @Test
    public void testClear() {
        ClusterCentroid instance = new ClusterCentroid();
        instance.addAll(Vector.of(5,8,9,3));
        instance.clear();
        Assert.assertTrue(instance.isEmpty());
    }

    /**
     * Test of randomise method, of class ClusterCentroid.
     */
    @Test
    public void testRandomize() {
        ClusterCentroid instance = new ClusterCentroid();
        instance.addAll(Vector.of(1,5,6,7,9,5,2));
        instance.randomise();
        
        Assert.assertFalse(instance.containsAll(Vector.of(1,5,6,7,9,5,2)));
    }

    /**
     * Test of getDataItemDistances method, of class ClusterCentroid.
     */
    @Test
    public void testGetDataItemDistances() {
        ClusterCentroid instance = new ClusterCentroid();
        double[] array = {2.0,5.0,3.0,4.2};
        instance.setDataItemDistances(array);
        
        Assert.assertEquals(array, instance.getDataItemDistances());
    }

    /**
     * Test of setDataItemDistances method, of class ClusterCentroid.
     */
    @Test
    public void testSetDataItemDistances() {
        ClusterCentroid instance = new ClusterCentroid();
        double[] array = {2.0,5.0,3.0,4.2};
        instance.setDataItemDistances(array);
        
        Assert.assertEquals(array, instance.getDataItemDistances());
    }

    /**
     * Test of addDataItemDistance method, of class ClusterCentroid.
     */
    @Test
    public void testAddDataItemDistance() {
        ClusterCentroid instance = new ClusterCentroid();
        Vector pattern = Vector.of(1,2,3,4);
        instance.addDataItem(5.0, pattern);
        
        Assert.assertEquals(5.0, instance.getDataItemDistances()[0]);
        Assert.assertEquals(instance.getDataItems().get(0), pattern);
    }
}
