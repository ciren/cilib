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
package net.sourceforge.cilib.clustering;

import junit.framework.Assert;
import net.sourceforge.cilib.algorithm.initialisation.DataDependantPopulationInitializationStrategy;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.clustering.iterationstrategies.SinglePopulationDataClusteringIterationStrategy;
import net.sourceforge.cilib.clustering.iterationstrategies.StandardDataClusteringIterationStrategy;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.TopologyBestContributionSelectionStrategy;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.problem.QuantizationErrorMinimizationProblem;
import net.sourceforge.cilib.problem.boundaryconstraint.CentroidBoundaryConstraint;
import net.sourceforge.cilib.problem.boundaryconstraint.RandomBoundaryConstraint;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Kristina
 */
public class DataClusteringPSOTest {
    
    public DataClusteringPSOTest() {
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
     * Test of algorithmIteration method, of class DataClusteringPSO.
     */
    @Test
    public void testAlgorithmIteration() {
        System.out.println("algorithmIteration");
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
        instance.setSourceURL("src\\test\\resources\\datasets\\iris2.arff");
        
        instance.setOptimisationProblem(problem);
        instance.addStoppingCondition(new MeasuredStoppingCondition());
        
        instance.performInitialisation();
        
        ClusterParticle particleBefore = instance.getTopology().get(0).getClone();
        
        instance.run();
        
        ClusterParticle particleAfter = instance.getTopology().get(0).getClone();
        
        Assert.assertFalse(particleAfter.getCandidateSolution().containsAll(particleBefore.getCandidateSolution()));
        
    }

    /**
     * Test of getTopology method, of class DataClusteringPSO.
     */
    @Test
    public void testGetTopology() {
        System.out.println("getTopology");
        DataClusteringPSO instance = new DataClusteringPSO();
        Particle p = new ClusterParticle();
        CentroidHolder holder = new CentroidHolder();
        holder.add(ClusterCentroid.of(1,2,3,4,5));
        holder.add(ClusterCentroid.of(1,3,5,9,8));
        p.setCandidateSolution(holder);
        Topology topology = new GBestTopology();
        topology.add(p);
        
        instance.setTopology(topology);
        
        Assert.assertEquals(topology, instance.getTopology());
    }

    /**
     * Test of setTopology method, of class DataClusteringPSO.
     */
    @Test
    public void testSetTopology() {
        System.out.println("setTopology");
        DataClusteringPSO instance = new DataClusteringPSO();
        Particle p = new ClusterParticle();
        CentroidHolder holder = new CentroidHolder();
        holder.add(ClusterCentroid.of(1,2,3,4,5));
        p.setCandidateSolution(holder);
        Topology topology = new GBestTopology();
        topology.add(p);
        
        instance.setTopology(topology);
        
        Assert.assertEquals(topology, instance.getTopology());
    }

    /**
     * Test of performInitialisation method, of class DataClusteringPSO.
     */
    @Test
    public void testPerformInitialisation() {
        System.out.println("performInitialisation");
        DataClusteringPSO instance = new DataClusteringPSO();
        QuantizationErrorMinimizationProblem problem = new QuantizationErrorMinimizationProblem();
        problem.setDomain("R(-5.12:5.12)");
        instance.setOptimisationProblem(problem);
        instance.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 1));
        PopulationInitialisationStrategy init = new DataDependantPopulationInitializationStrategy<ClusterParticle>();
        init.setEntityType(new ClusterParticle());
        instance.setInitialisationStrategy(init);
        instance.setSourceURL("src\\test\\resources\\datasets\\iris2.arff");
        instance.performInitialisation();
        
        Assert.assertTrue(((SinglePopulationDataClusteringIterationStrategy) instance.getIterationStrategy()).getDataset().size() > 0);
        Assert.assertTrue(!instance.getTopology().isEmpty());
    }

    /**
     * Test of getContributionSelectionStrategy method, of class DataClusteringPSO.
     */
    @Test
    public void testGetContributionSelectionStrategy() {
       System.out.println("getContributionSelectionStrategy");
       DataClusteringPSO instance = new DataClusteringPSO();
       instance.setContributionSelectionStrategy(new TopologyBestContributionSelectionStrategy());
       
       Assert.assertTrue(instance.getContributionSelectionStrategy() instanceof TopologyBestContributionSelectionStrategy);
    }

    /**
     * Test of setContributionSelectionStrategy method, of class DataClusteringPSO.
     */
    @Test
    public void testSetContributionSelectionStrategy() {
        System.out.println("setContributionSelectionStrategy");
        DataClusteringPSO instance = new DataClusteringPSO();
       instance.setContributionSelectionStrategy(new TopologyBestContributionSelectionStrategy());
       
       Assert.assertTrue(instance.getContributionSelectionStrategy() instanceof TopologyBestContributionSelectionStrategy);
    }

    /**
     * Test of getDistanceMeasure method, of class DataClusteringPSO.
     */
    @Test
    public void testGetDistanceMeasure() {
        System.out.println("getDistanceMeasure");
        DataClusteringPSO instance = new DataClusteringPSO();
        
        Assert.assertTrue(((StandardDataClusteringIterationStrategy) instance.getIterationStrategy()).getDistanceMeasure() instanceof EuclideanDistanceMeasure);
    }
}
