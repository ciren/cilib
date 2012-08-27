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
package net.sourceforge.cilib.entity.initialization;

import junit.framework.Assert;
import net.sourceforge.cilib.clustering.SlidingWindow;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataPatternInitializationStrategyTest {
    
    public DataPatternInitializationStrategyTest() {
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
     * Test of initialize method, of class DataPatternInitializationStrategy.
     */
    @Test
    public void testInitialize() {
        SlidingWindow window = new SlidingWindow();
        window.setWindowSize(3);
        window.setSourceURL("library\\src\\test\\resources\\datasets\\iris2.arff");
        window.initializeWindow();
        DataTable table = window.getCurrentDataset();
        
        ClusterParticle particle  = new ClusterParticle();
        CentroidHolder holder = new CentroidHolder();
        holder.add(ClusterCentroid.of(0,0));
        particle.setCandidateSolution(holder);
        
        DataPatternInitializationStrategy strategy = new DataPatternInitializationStrategy();
        strategy.setDataset(table);
        strategy.initialize(EntityType.CANDIDATE_SOLUTION, particle);
        
        Assert.assertTrue((((CentroidHolder)particle.getCandidateSolution()).get(0).containsAll(ClusterCentroid.of(1.0,1.0,1.0,2.0))) 
                || (((CentroidHolder)particle.getCandidateSolution()).get(0).containsAll(ClusterCentroid.of(2.0,3.0,4.0,2.0))) 
                || (((CentroidHolder)particle.getCandidateSolution()).get(0).containsAll(ClusterCentroid.of(1.0,1.0,1.0,1.0))));
    }

    /**
     * Test of setDataset method, of class DataPatternInitializationStrategy.
     */
    @Test
    public void testSetDataset() {
        SlidingWindow window = new SlidingWindow();
        window.setWindowSize(3);
        window.setSourceURL("library\\src\\test\\resources\\datasets\\iris2.arff");
        window.initializeWindow();
        DataTable table = window.getCurrentDataset();
        
        DataPatternInitializationStrategy strategy = new DataPatternInitializationStrategy();
        strategy.setDataset(table);
        
        Assert.assertEquals(table, strategy.getDataset());
    }

    /**
     * Test of getDataset method, of class DataPatternInitializationStrategy.
     */
    @Test
    public void testGetDataset() {
        SlidingWindow window = new SlidingWindow();
        window.setWindowSize(3);
        window.setSourceURL("library\\src\\test\\resources\\datasets\\iris2.arff");
        window.initializeWindow();
        DataTable table = window.getCurrentDataset();
        
        DataPatternInitializationStrategy strategy = new DataPatternInitializationStrategy();
        strategy.setDataset(table);
        
        Assert.assertEquals(table, strategy.getDataset());
    }
}
