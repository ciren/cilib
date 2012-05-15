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
package net.sourceforge.cilib.controlparameter;

import junit.framework.Assert;
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
public class BoundedModifiableControlParameterTest {
    
    public BoundedModifiableControlParameterTest() {
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
     * Test of updateParameter method, of class BoundedModifiableControlParameter.
     */
    @Test
    public void testUpdateParameter_double() {
        System.out.println("updateParameter");
        double value = 0.3;
        BoundedModifiableControlParameter instance = new BoundedModifiableControlParameter();
        instance.updateParameter(value);
        Assert.assertEquals(instance.getParameter(), value);
    }

    /**
     * Test of setLowerBound method, of class BoundedModifiableControlParameter.
     */
    @Test
    public void testSetLowerBound() {
        System.out.println("setLowerBound");
        double lower = 0.2;
        BoundedModifiableControlParameter instance = new BoundedModifiableControlParameter();
        instance.setLowerBound(lower);
        Assert.assertEquals(instance.getLowerBound(), lower);
    }

    /**
     * Test of getParameter method, of class BoundedModifiableControlParameter.
     */
    @Test
    public void testGetParameter_0args() {
        System.out.println("getParameter");
        BoundedModifiableControlParameter instance = new BoundedModifiableControlParameter();
        double expectedResult = 0.55;
        instance.setParameter(expectedResult);
        double result = instance.getParameter();
        Assert.assertEquals(expectedResult, result);
        
    }

    /**
     * Test of setParameter method, of class BoundedModifiableControlParameter.
     */
    @Test
    public void testSetParameter() {
        System.out.println("setParameter");
        double value = 0.55;
        BoundedModifiableControlParameter instance = new BoundedModifiableControlParameter();
        instance.setParameter(value);
        Assert.assertEquals(instance.getParameter(), value);
    }

    /**
     * Test of getLowerBound method, of class BoundedModifiableControlParameter.
     */
    @Test
    public void testGetLowerBound() {
        System.out.println("getLowerBound");
        BoundedModifiableControlParameter instance = new BoundedModifiableControlParameter();
        double expectedResult = 0.2;
        instance.setLowerBound(expectedResult);
        double result = instance.getLowerBound();
        Assert.assertEquals(expectedResult, result);
    }

    /**
     * Test of getUpperBound method, of class BoundedModifiableControlParameter.
     */
    @Test
    public void testGetUpperBound() {
        System.out.println("getUpperBound");
        BoundedModifiableControlParameter instance = new BoundedModifiableControlParameter();
        double expectedResult = 0.55;
        instance.setUpperBound(expectedResult);
        double result = instance.getUpperBound();
        Assert.assertEquals(expectedResult, result);
    }

    /**
     * Test of setUpperBound method, of class BoundedModifiableControlParameter.
     */
    @Test
    public void testSetUpperBound() {
        System.out.println("setUpperBound");
        double upper = 0.2;
        BoundedModifiableControlParameter instance = new BoundedModifiableControlParameter();
        instance.setUpperBound(upper);
        Assert.assertEquals(instance.getUpperBound(), upper);
    }
    
    /**
     * Test of wasSetByUser method, of class BoundedModifiableControlParameter.
     */
    @Test
    public void testWasSetByUser() {
        BoundedModifiableControlParameter instance = new BoundedModifiableControlParameter();
        Assert.assertFalse(instance.wasSetByUser());
        
        instance.setParameter(1.5);
        Assert.assertTrue(instance.wasSetByUser());
    }

}
