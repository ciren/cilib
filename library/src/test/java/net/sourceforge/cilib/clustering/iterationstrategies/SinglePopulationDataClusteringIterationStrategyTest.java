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
package net.sourceforge.cilib.clustering.iterationstrategies;

import net.sourceforge.cilib.algorithm.initialisation.DataDependantPopulationInitializationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.problem.QuantizationErrorMinimizationProblem;
import net.sourceforge.cilib.problem.boundaryconstraint.CentroidBoundaryConstraint;
import net.sourceforge.cilib.problem.boundaryconstraint.RandomBoundaryConstraint;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;
import junit.framework.Assert;
import net.sourceforge.cilib.clustering.SlidingWindow;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class SinglePopulationDataClusteringIterationStrategyTest {
    
    public SinglePopulationDataClusteringIterationStrategyTest() {
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
     * Test of performIteration method, of class StandardDataClusteringIterationStrategy.
     */
    @Test
    public void testPerformIteration() {
        DataClusteringPSO instance = new DataClusteringPSO();
        
        QuantizationErrorMinimizationProblem problem = new QuantizationErrorMinimizationProblem();
        problem.setDomain("R(-5.12:5.12)");
        IterationStrategy strategy = new StandardDataClusteringIterationStrategy();
        CentroidBoundaryConstraint constraint = new CentroidBoundaryConstraint();
        constraint.setDelegate(new RandomBoundaryConstraint());
        strategy.setBoundaryConstraint(constraint);
        instance.setIterationStrategy(strategy);
        instance.setOptimisationProblem(problem);
        DataDependantPopulationInitializationStrategy init = new DataDependantPopulationInitializationStrategy<ClusterParticle>();
      
        init.setEntityType(new ClusterParticle());
        init.setEntityNumber(2);
        instance.setInitialisationStrategy(init);
        instance.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        
        instance.setOptimisationProblem(problem);
        instance.addStoppingCondition(new MeasuredStoppingCondition());
        
        instance.performInitialisation();
        
        ClusterParticle particleBefore = instance.getTopology().get(0).getClone();
        
        instance.run();
        
        ClusterParticle particleAfter = instance.getTopology().get(0).getClone();
        
        Assert.assertFalse(particleAfter.getCandidateSolution().containsAll(particleBefore.getCandidateSolution()));
    }

    /**
     * Test of getDistanceMeasure method, of class SinglePopulationDataClusteringIterationStrategy.
     */
    @Test
    public void testGetDistanceMeasure() {
        SinglePopulationDataClusteringIterationStrategy instance = new StandardDataClusteringIterationStrategy();
        Assert.assertTrue(instance.getDistanceMeasure() instanceof EuclideanDistanceMeasure);
    }

    /**
     * Test of getDataset method, of class SinglePopulationDataClusteringIterationStrategy.
     */
    @Test
    public void testGetDataset() {
        SinglePopulationDataClusteringIterationStrategy instance = new StandardDataClusteringIterationStrategy();
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        window.setWindowSize(1);
        window.initializeWindow();
        instance.setWindow(window);
        
        Assert.assertEquals(instance.getDataset().size(), 1);
        
        Vector beforeSlide =  ((StandardPattern) instance.getDataset().getRow(0)).getVector();
        Vector expectedBeforeSlide = Vector.of(1.0,1.0,1.0,2.0);
        
        Assert.assertTrue(beforeSlide.containsAll(expectedBeforeSlide));
    }

    /**
     * Test of setReinitialisationInterval method, of class SinglePopulationDataClusteringIterationStrategy.
     */
    @Test
    public void testSetReinitialisationInterval() {
        SinglePopulationDataClusteringIterationStrategy instance = new StandardDataClusteringIterationStrategy();
        instance.setReinitialisationInterval(2);
        
        assertEquals(instance.getReinitialisationInterval(), 2);
    }
    /**
     * Test of getReinitialisationInterval method, of class SinglePopulationDataClusteringIterationStrategy.
     */
    @Test
    public void testGetReinitialisationInterval() {
        SinglePopulationDataClusteringIterationStrategy instance = new StandardDataClusteringIterationStrategy();
        instance.setReinitialisationInterval(2);
        
        assertEquals(instance.getReinitialisationInterval(), 2);
    }

    /**
     * Test of setDimensions method, of class SinglePopulationDataClusteringIterationStrategy.
     */
    @Test
    public void testSetDimensions() {
        SinglePopulationDataClusteringIterationStrategy instance = new StandardDataClusteringIterationStrategy();
        instance.setDimensions(2);
        
        assertEquals(instance.dimensions, 2);
    }

    /**
     * Test of setWindow method, of class SinglePopulationDataClusteringIterationStrategy.
     */
    @Test
    public void testSetWindow() {
        SinglePopulationDataClusteringIterationStrategy instance = new StandardDataClusteringIterationStrategy();
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        window.setWindowSize(1);
        instance.setWindow(window);
        
        Assert.assertEquals(window, instance.getWindow());
    }

    /**
     * Test of getWindow method, of class SinglePopulationDataClusteringIterationStrategy.
     */
    @Test
    public void testGetWindow() {
        SinglePopulationDataClusteringIterationStrategy instance = new StandardDataClusteringIterationStrategy();
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        window.setWindowSize(1);
        instance.setWindow(window);
        
        Assert.assertEquals(window, instance.getWindow());
    }

    /**
     * Test of assignDataPatternsToParticle method, of class SinglePopulationDataClusteringIterationStrategy.
     */
    @Test
    public void testAssignDataPatternsToParticle() {
        SinglePopulationDataClusteringIterationStrategy instance = new StandardDataClusteringIterationStrategy();
        CentroidHolder candidateSolution = new CentroidHolder();
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
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

}
