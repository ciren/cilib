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
package net.sourceforge.cilib.algorithm.population;

import junit.framework.Assert;
import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.clustering.SlidingWindow;
import net.sourceforge.cilib.clustering.iterationstrategies.SinglePopulationDataClusteringIterationStrategy;
import net.sourceforge.cilib.clustering.iterationstrategies.StandardDataClusteringIterationStrategy;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.clustering.iterationstrategies.CooperativeDataClusteringPSOIterationStrategy;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.pso.multiswarm.MultiSwarm;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Kristina
 */
public class AbstractCooperativeIterationStrategyTest {
    
    public AbstractCooperativeIterationStrategyTest() {
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
     * Test of assignDataPatternsToParticle method, of class AbstractCooperativeIterationStrategy.
     */
    @Test
    public void testAssignDataPatternsToParticle() {
        System.out.println("assignDataPatternsToParticle");
        SinglePopulationDataClusteringIterationStrategy instance = new StandardDataClusteringIterationStrategy();
        CentroidHolder candidateSolution = new CentroidHolder();
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("src\\test\\resources\\datasets\\iris2.arff");
        window.setWindowSize(3);
        instance.setWindow(window);
        instance.getWindow().initializeWindow();
        
        candidateSolution.add(ClusterCentroid.of(1.25,1.1,1.3,1.9));
        candidateSolution.add(ClusterCentroid.of(1.92,2.6,3.1,1.8));
        candidateSolution.add(ClusterCentroid.of(0.9,1.1,0.85,0.79));
        
        DataTable dataset = instance.getWindow().getCurrentDataset();
        
        instance.assignDataPatternsToParticle(candidateSolution, dataset);
        Assert.assertTrue(candidateSolution.get(0).getDataItems().contains(Vector.of(1.0,1.0,1.0,2.0)));
        Assert.assertTrue(candidateSolution.get(1).getDataItems().contains(Vector.of(2.0,3.0,4.0,2.0)));
        Assert.assertTrue(candidateSolution.get(2).getDataItems().contains(Vector.of(1.0,1.0,1.0,1.0)));
    }

    /**
     * Test of getContextParticle method, of class AbstractCooperativeIterationStrategy.
     */
    @Test
    public void testGetContextParticle() {
        System.out.println("getContextParticle");
        ClusterParticle particle = new ClusterParticle();
        AbstractCooperativeIterationStrategy abstractCoop = new CooperativeDataClusteringPSOIterationStrategy();
        abstractCoop.contextParticle = particle;
        
        Assert.assertEquals(particle, abstractCoop.getContextParticle());
    }

    /**
     * Test of initializeContextParticle method, of class AbstractCooperativeIterationStrategy.
     */
    @Test
    public void testInitializeContextParticle() {
        System.out.println("initializeContextParticle");
        AbstractCooperativeIterationStrategy abstractCoop = new CooperativeDataClusteringPSOIterationStrategy();
        MultiSwarm ms = new MultiSwarm();
        DataClusteringPSO pso = new DataClusteringPSO();
        ClusterParticle particle = new ClusterParticle();
        CentroidHolder holder = new CentroidHolder();
        holder.add(ClusterCentroid.of(1,2,3,4));
        particle.setCandidateSolution(holder);
        particle.getProperties().put(EntityType.Particle.BEST_POSITION, holder);
        particle.getProperties().put(EntityType.Particle.VELOCITY, holder);
        pso.getTopology().add(particle);
        
        ms.addPopulationBasedAlgorithm(pso);
        
        abstractCoop.initializeContextParticle(ms);
        
        Assert.assertEquals(1, ((CentroidHolder) abstractCoop.getContextParticle().getCandidateSolution()).size());
        Assert.assertEquals(4, ((CentroidHolder) abstractCoop.getContextParticle().getCandidateSolution()).get(0).size());
        Assert.assertTrue(((CentroidHolder) abstractCoop.getContextParticle().getCandidateSolution()).containsAll(holder));
    }

}
