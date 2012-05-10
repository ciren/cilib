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

import java.util.Collection;
import java.util.Iterator;
import junit.framework.Assert;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Numeric;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kristina
 */
public class ClusterCentroidTest {
    
    public ClusterCentroidTest() {
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
     * Test of copy method, of class ClusterCentroid.
     */
    @Test
    public void testCopy() {
        System.out.println("copy");
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
        System.out.println("size");
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
        System.out.println("isEmpty");
        ClusterCentroid instance = new ClusterCentroid();
        Assert.assertTrue(instance.isEmpty());
    }

    /**
     * Test of add method, of class ClusterCentroid.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        ClusterCentroid instance = new ClusterCentroid();
        instance.add(Int.valueOf(5));
        Assert.assertTrue(instance.contains(Int.valueOf(5)));
    }

    /**
     * Test of remove method, of class ClusterCentroid.
     */
    @Test
    public void testRemove() {
        System.out.println("remove");
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
        System.out.println("containsAll");
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
        System.out.println("contains");
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
        System.out.println("addAll");
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
        System.out.println("removeAll");
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
        System.out.println("clear");
        ClusterCentroid instance = new ClusterCentroid();
        instance.addAll(Vector.of(5,8,9,3));
        instance.clear();
        Assert.assertTrue(instance.isEmpty());
    }

    /**
     * Test of randomize method, of class ClusterCentroid.
     */
    @Test
    public void testRandomize() {
        System.out.println("randomize");
        RandomProvider random = new MersenneTwister();
        ClusterCentroid instance = new ClusterCentroid();
        instance.addAll(Vector.of(1,5,6,7,9,5,2));
        instance.randomize(random);
        
        Assert.assertFalse(instance.containsAll(Vector.of(1,5,6,7,9,5,2)));
    }

    /**
     * Test of getDataItemDistances method, of class ClusterCentroid.
     */
    @Test
    public void testGetDataItemDistances() {
        System.out.println("getDataItemDistances");
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
        System.out.println("setDataItemDistances");
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
        System.out.println("addDataItemDistance");
        ClusterCentroid instance = new ClusterCentroid();
        instance.addDataItemDistance(5.0);
        
        Assert.assertEquals(5.0, instance.getDataItemDistances()[0]);
    }
}
