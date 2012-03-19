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
public class ConstantControlParameterTest {
    
    public ConstantControlParameterTest() {
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
     * Test of getClone method, of class ConstantControlParameter.
     */
    @Test
    public void testGetClone() {
        System.out.println("getClone");
        ConstantControlParameter instance = new ConstantControlParameter();
        ConstantControlParameter expResult = instance;
        ConstantControlParameter result = instance.getClone();
        Assert.assertEquals(expResult.getParameter(), result.getParameter());
        
    }

    /**
     * Test of getParameter method, of class ConstantControlParameter.
     */
    @Test
    public void testGetParameter_0args() {
        System.out.println("getParameter");
        ConstantControlParameter instance = new ConstantControlParameter();
        double expectedValue = 0.55;
        instance.setParameter(expectedValue);
        double result = instance.getParameter();
        Assert.assertEquals(expectedValue, result);
    }

    /**
     * Test of setParameter method, of class ConstantControlParameter.
     */
    @Test
    public void testSetParameter() {
        System.out.println("setParameter");
        ConstantControlParameter instance = new ConstantControlParameter();
        double expectedValue = 0.55;
        instance.setParameter(expectedValue);
        double result = instance.getParameter();
        Assert.assertEquals(expectedValue, result);
    }

    /**
     * Test of setVelocity method, of class ConstantControlParameter.
     */
    @Test
    public void testSetVelocity() {
        System.out.println("setVelocity");
        ConstantControlParameter instance = new ConstantControlParameter();
        double expResult = 0;
        double result = instance.getVelocity();
        Assert.assertEquals(expResult, result);
    }
    
    /**
     * Test of wasSetByUser method, of class BoundedModifiableControlParameter.
     */
    @Test
    public void testWasSetByUser() {
        ConstantControlParameter instance = new ConstantControlParameter(0.1);
        Assert.assertTrue(instance.wasSetByUser());
    }
}
