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
package net.sourceforge.cilib.algorithm.initialisation;

import net.sourceforge.cilib.controlparameter.ControlParameter;
import java.util.ArrayList;
import junit.framework.Assert;
import net.sourceforge.cilib.clustering.SlidingWindow;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.DataTableBuilder;
import net.sourceforge.cilib.problem.QuantizationErrorMinimizationProblem;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataDependantPopulationInitializationStrategyTest {
    
    public DataDependantPopulationInitializationStrategyTest() {
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
     * Test of setEntityType method, of class DataDependantPopulationInitializationStrategy.
     */
    @Test
    public void testSetEntityType() {
        System.out.println("setEntityType");
        ClusterParticle type  = new ClusterParticle();
        DataDependantPopulationInitializationStrategy instance = new DataDependantPopulationInitializationStrategy();
        instance.setEntityType(type);
        
        Assert.assertEquals(instance.getEntityType(), type);
    }

    /**
     * Test of getEntityType method, of class DataDependantPopulationInitializationStrategy.
     */
    @Test
    public void testGetEntityType() {
        System.out.println("getEntityType");
        ClusterParticle type  = new ClusterParticle();
        DataDependantPopulationInitializationStrategy instance = new DataDependantPopulationInitializationStrategy();
        instance.setEntityType(type);
        
        Assert.assertEquals(instance.getEntityType(), type);
    }

    /**
     * Test of initialise method, of class DataDependantPopulationInitializationStrategy.
     */
    @Test
    public void testInitialise() {
        System.out.println("initialise");
        QuantizationErrorMinimizationProblem problem = new QuantizationErrorMinimizationProblem();
        problem.setDomain("R(-5:5)");
        problem.setNumberOfClusters(3);
        problem.setDimension(4);
        SlidingWindow window = new SlidingWindow();
        window.setWindowSize(3);
        window.setSourceURL("library\\src\\test\\resources\\datasets\\iris2.arff");
        window.initializeWindow();
        DataTable table = window.getCurrentDataset();
        
        DataDependantPopulationInitializationStrategy instance = new DataDependantPopulationInitializationStrategy();
        instance.setDataset(table);
        instance.setEntityType(new ClusterParticle());
        instance.setEntityNumber(2);
        
        Iterable resultingTopology = instance.initialise(problem);
        
        boolean isCorrectType = false;
        int size = 0;
        int numberOfClusters = 0;
        int dimensions = 0;
        for(Object entity : resultingTopology) {
            if(entity instanceof ClusterParticle) {
                isCorrectType = true;
                ClusterParticle newEntity = (ClusterParticle) entity;
                numberOfClusters = newEntity.getCandidateSolution().size();
                dimensions = ((CentroidHolder) newEntity.getCandidateSolution()).get(0).size();
            }
            size++;
        }
        Assert.assertEquals(2, size);
        Assert.assertTrue(isCorrectType);
        Assert.assertEquals(3, numberOfClusters);
        Assert.assertEquals(4, dimensions);
    }

    /**
     * Test of getEntityNumber method, of class DataDependantPopulationInitializationStrategy.
     */
    @Test
    public void testGetEntityNumber() {
        System.out.println("getEntityNumber");
        DataDependantPopulationInitializationStrategy instance = new DataDependantPopulationInitializationStrategy();
        instance.setEntityNumber(3);
        
        Assert.assertEquals(3, instance.getEntityNumber());
    }

    /**
     * Test of setEntityNumber method, of class DataDependantPopulationInitializationStrategy.
     */
    @Test
    public void testSetEntityNumber() {
        System.out.println("setEntityNumber");
        DataDependantPopulationInitializationStrategy instance = new DataDependantPopulationInitializationStrategy();
        instance.setEntityNumber(3);
        
        Assert.assertEquals(3, instance.getEntityNumber());
    }

    /**
     * Test of setDelegate method, of class DataDependantPopulationInitializationStrategy.
     */
    @Test
    public void testSetDelegate() {
        System.out.println("setDelegate");
        PopulationInitialisationStrategy newDelegate = new ClonedPopulationInitialisationStrategy();
        DataDependantPopulationInitializationStrategy instance = new DataDependantPopulationInitializationStrategy();
        instance.setDelegate(newDelegate);
        
        Assert.assertEquals(newDelegate, instance.getDelegate());
    }

    /**
     * Test of getDelegate method, of class DataDependantPopulationInitializationStrategy.
     */
    @Test
    public void testGetDelegate() {
        System.out.println("getDelegate");
        PopulationInitialisationStrategy newDelegate = new ClonedPopulationInitialisationStrategy();
        DataDependantPopulationInitializationStrategy instance = new DataDependantPopulationInitializationStrategy();
        instance.setDelegate(newDelegate);
        
        Assert.assertEquals(newDelegate, instance.getDelegate());
    }

    /**
     * Test of getBounds method, of class DataDependantPopulationInitializationStrategy.
     */
    @Test
    public void testGetBounds() {
        System.out.println("getBounds");
        SlidingWindow window = new SlidingWindow();
        window.setWindowSize(3);
        window.setSourceURL("library\\src\\test\\resources\\datasets\\iris2.arff");
        window.initializeWindow();
        DataTable table = window.getCurrentDataset();
        
        DataDependantPopulationInitializationStrategy instance = new DataDependantPopulationInitializationStrategy();
        instance.setDataset(table);
        
        ArrayList<ControlParameter[]> bounds = instance.getBounds();
        
        Assert.assertEquals(bounds.get(0)[0].getParameter(), 1.0);
        Assert.assertEquals(bounds.get(0)[1].getParameter(), 2.0);
        
        Assert.assertEquals(bounds.get(1)[0].getParameter(), 1.0);
        Assert.assertEquals(bounds.get(1)[1].getParameter(), 3.0);
        
        Assert.assertEquals(bounds.get(2)[0].getParameter(), 1.0);
        Assert.assertEquals(bounds.get(2)[1].getParameter(), 4.0);
        
        Assert.assertEquals(bounds.get(3)[0].getParameter(), 1.0);
        Assert.assertEquals(bounds.get(3)[1].getParameter(), 2.0);
    }

    /**
     * Test of setDataset method, of class DataDependantPopulationInitializationStrategy.
     */
    @Test
    public void testSetDataset() {
        System.out.println("setDataset");
        SlidingWindow window = new SlidingWindow();
        window.setWindowSize(3);
        window.setSourceURL("library\\src\\test\\resources\\datasets\\iris2.arff");
        window.initializeWindow();
        DataTable table = window.getCurrentDataset();
        
        DataDependantPopulationInitializationStrategy instance = new DataDependantPopulationInitializationStrategy();
        instance.setDataset(table);
        
        Assert.assertEquals(table, instance.dataset);
    }
}
