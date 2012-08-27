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

import junit.framework.Assert;
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
import net.sourceforge.cilib.clustering.CooperativePSO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class CooperativeDataClusteringPSOIterationStrategyTest {
    
    public CooperativeDataClusteringPSOIterationStrategyTest() {
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
     * Test of performIteration method, of class CooperativeDataClusteringPSOIterationStrategy.
     */
    @Test
    public void testPerformIteration() {
        System.out.println("performIteration");
        DataClusteringPSO instance = new DataClusteringPSO();
        
        QuantizationErrorMinimizationProblem problem = new QuantizationErrorMinimizationProblem();
        problem.setDomain("R(-5.12:5.12)");
        CentroidBoundaryConstraint constraint = new CentroidBoundaryConstraint();
        constraint.setDelegate(new RandomBoundaryConstraint());
        instance.setOptimisationProblem(problem);
        DataDependantPopulationInitializationStrategy init = new DataDependantPopulationInitializationStrategy<ClusterParticle>();
      
        init.setEntityType(new ClusterParticle());
        init.setEntityNumber(2);
        instance.setInitialisationStrategy(init);
        instance.setSourceURL("library\\src\\test\\resources\\datasets\\iris2.arff");
        
        instance.setOptimisationProblem(problem);
        instance.addStoppingCondition(new MeasuredStoppingCondition());
        
        DataClusteringPSO instance2 = new DataClusteringPSO();
        
        QuantizationErrorMinimizationProblem problem2 = new QuantizationErrorMinimizationProblem();
        problem2.setDomain("R(-5.12:5.12)");
        CentroidBoundaryConstraint constraint2 = new CentroidBoundaryConstraint();
        constraint2.setDelegate(new RandomBoundaryConstraint());
        instance2.setOptimisationProblem(problem2);
        DataDependantPopulationInitializationStrategy init2 = new DataDependantPopulationInitializationStrategy<ClusterParticle>();
      
        init2.setEntityType(new ClusterParticle());
        init2.setEntityNumber(2);
        instance2.setInitialisationStrategy(init2);
        instance2.setSourceURL("library\\src\\test\\resources\\datasets\\iris2.arff");
        
        instance2.setOptimisationProblem(problem2);
        instance2.addStoppingCondition(new MeasuredStoppingCondition());
        
        DataClusteringPSO instance3 = new DataClusteringPSO();
        
        QuantizationErrorMinimizationProblem problem3 = new QuantizationErrorMinimizationProblem();
        problem3.setDomain("R(-5.12:5.12)");
        CentroidBoundaryConstraint constraint3 = new CentroidBoundaryConstraint();
        constraint3.setDelegate(new RandomBoundaryConstraint());
        instance3.setOptimisationProblem(problem3);
        DataDependantPopulationInitializationStrategy init3 = new DataDependantPopulationInitializationStrategy<ClusterParticle>();
      
        init3.setEntityType(new ClusterParticle());
        init3.setEntityNumber(2);
        instance3.setInitialisationStrategy(init3);
        instance3.setSourceURL("library\\src\\test\\resources\\datasets\\iris2.arff");
        
        instance3.setOptimisationProblem(problem3);
        instance3.addStoppingCondition(new MeasuredStoppingCondition());
        
        CooperativePSO cooperative = new CooperativePSO();
        cooperative.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 30));
        cooperative.addPopulationBasedAlgorithm(instance);
        cooperative.addPopulationBasedAlgorithm(instance2);
        cooperative.addPopulationBasedAlgorithm(instance3);
        cooperative.setOptimisationProblem(problem);
        
        cooperative.performInitialisation();
        
        ClusterParticle particleBefore = instance.getTopology().get(0).getClone();
        
        cooperative.run();
        
        ClusterParticle particleAfter = instance.getTopology().get(0).getClone();
        
        Assert.assertFalse(particleAfter.getCandidateSolution().containsAll(particleBefore.getCandidateSolution()));
    }

    /**
     * Test of reinitializeContext method, of class CooperativeDataClusteringPSOIterationStrategy.
     */
    @Test
    public void testReinitializeContext() {
        System.out.println("reinitializeContext");
        DataClusteringPSO instance = new DataClusteringPSO();
        
        QuantizationErrorMinimizationProblem problem = new QuantizationErrorMinimizationProblem();
        problem.setDomain("R(-5.12:5.12)");
        IterationStrategy strategy = new StandardDataClusteringIterationStrategy();
        CentroidBoundaryConstraint constraint = new CentroidBoundaryConstraint();
        constraint.setDelegate(new RandomBoundaryConstraint());
        strategy.setBoundaryConstraint(constraint);
        instance.setOptimisationProblem(problem);
        DataDependantPopulationInitializationStrategy init = new DataDependantPopulationInitializationStrategy<ClusterParticle>();
      
        init.setEntityType(new ClusterParticle());
        init.setEntityNumber(2);
        instance.setInitialisationStrategy(init);
        instance.setSourceURL("library\\src\\test\\resources\\datasets\\iris2.arff");
        
        instance.setOptimisationProblem(problem);
        instance.addStoppingCondition(new MeasuredStoppingCondition());
        
        CooperativePSO cooperative = new CooperativePSO();
        cooperative.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 30));
        cooperative.addPopulationBasedAlgorithm(instance);
        cooperative.setOptimisationProblem(problem);
        
        cooperative.performInitialisation();
        
        ClusterParticle particleBefore = instance.getTopology().get(0).getClone();
        
        cooperative.run();
        
        ClusterParticle particleAfter = instance.getTopology().get(0).getClone();
        
        Assert.assertFalse(particleAfter.getCandidateSolution().containsAll(particleBefore.getCandidateSolution()));
    }
}
