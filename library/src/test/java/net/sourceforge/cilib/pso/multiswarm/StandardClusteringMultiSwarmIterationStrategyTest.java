/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.multiswarm;

import junit.framework.Assert;
import net.sourceforge.cilib.algorithm.initialisation.DataDependantPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.clustering.SlidingWindow;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.clustering.iterationstrategies.StandardDataClusteringIterationStrategy;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.problem.QuantisationErrorMinimisationProblem;
import net.sourceforge.cilib.problem.boundaryconstraint.CentroidBoundaryConstraint;
import net.sourceforge.cilib.problem.boundaryconstraint.RandomBoundaryConstraint;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import static org.junit.Assert.*;
import org.junit.Test;

public class StandardClusteringMultiSwarmIterationStrategyTest {

    /**
     * Test of getExclusionRadius method, of class StandardClusteringMultiSwarmIterationStrategy.
     */
    @Test
    public void testGetExclusionRadius() {
        StandardClusteringMultiSwarmIterationStrategy instance = new StandardClusteringMultiSwarmIterationStrategy();
        instance.setExclusionRadius(5.2);

        Assert.assertEquals(instance.getExclusionRadius(), 5.2);
    }

    /**
     * Test of setExclusionRadius method, of class StandardClusteringMultiSwarmIterationStrategy.
     */
    @Test
    public void testSetExclusionRadius() {
        StandardClusteringMultiSwarmIterationStrategy instance = new StandardClusteringMultiSwarmIterationStrategy();
        instance.setExclusionRadius(5.2);

        Assert.assertEquals(instance.getExclusionRadius(), 5.2);
    }

    /**
     * Test of calculateRadius method, of class StandardClusteringMultiSwarmIterationStrategy.
     */
    @Test
    public void testCalculateRadius() {
        MultiSwarm multiswarm = new MultiSwarm();
        multiswarm.addPopulationBasedAlgorithm(new DataClusteringPSO());
        multiswarm.addPopulationBasedAlgorithm(new DataClusteringPSO());

        QuantisationErrorMinimisationProblem problem = new QuantisationErrorMinimisationProblem();
        problem.setDomain("R(-10:10)");
        problem.setDimension(5);

        multiswarm.setOptimisationProblem(problem);

        StandardClusteringMultiSwarmIterationStrategy msStrategy = new StandardClusteringMultiSwarmIterationStrategy();
        double result = msStrategy.calculateRadius(multiswarm);

        Assert.assertEquals(8.7055056329612413913627001747975, result);
    }

    /**
     * Test of isConverged method, of class StandardClusteringMultiSwarmIterationStrategy.
     */
    @Test
    public void testIsConverged() {
        MultiSwarm multiswarm = new MultiSwarm();
        DataClusteringPSO pso = new DataClusteringPSO();


        CentroidHolder candidateSolution = new CentroidHolder();
        candidateSolution.add(ClusterCentroid.of(1,2,3,4));
        CentroidHolder candidateSolution2 = new CentroidHolder();
        candidateSolution2.add(ClusterCentroid.of(1,2,5,2));

        ClusterParticle particle = new ClusterParticle();
        particle.setCandidateSolution(candidateSolution);
        ClusterParticle particle2 = new ClusterParticle();
        particle2.setCandidateSolution(candidateSolution2);

        pso.getTopology().add(particle);
        pso.getTopology().add(particle2);

        DataClusteringPSO pso2 = new DataClusteringPSO();

        CentroidHolder candidateSolution12 = new CentroidHolder();
        candidateSolution12.add(ClusterCentroid.of(5,8,7,9));
        CentroidHolder candidateSolution22 = new CentroidHolder();
        candidateSolution22.add(ClusterCentroid.of(20,12,4,6));

        ClusterParticle particle12 = new ClusterParticle();
        particle12.setCandidateSolution(candidateSolution12);
        ClusterParticle particle22 = new ClusterParticle();
        particle22.setCandidateSolution(candidateSolution22);

        pso2.getTopology().add(particle12);
        pso2.getTopology().add(particle22);

        multiswarm.addPopulationBasedAlgorithm(pso);
        multiswarm.addPopulationBasedAlgorithm(pso2);

        QuantisationErrorMinimisationProblem problem = new QuantisationErrorMinimisationProblem();
        problem.setDomain("R(-10:10)");
        problem.setDimension(5);

        multiswarm.setOptimisationProblem(problem);

        StandardClusteringMultiSwarmIterationStrategy msStrategy = new StandardClusteringMultiSwarmIterationStrategy();
        boolean firstPSO = msStrategy.isConverged(pso, multiswarm);
        boolean secondPSO = msStrategy.isConverged(pso2, multiswarm);

        Assert.assertTrue(firstPSO);
        Assert.assertFalse(secondPSO);
    }

    /**
     * Test of performIteration method, of class StandardClusteringMultiSwarmIterationStrategy.
     */
    @Test
    public void testPerformIteration() {
        DataClusteringPSO instance = new DataClusteringPSO();

        QuantisationErrorMinimisationProblem problem = new QuantisationErrorMinimisationProblem();
        problem.setDomain("R(-5.12:5.12)");
        IterationStrategy strategy = new StandardClusteringMultiSwarmIterationStrategy();
        CentroidBoundaryConstraint constraint = new CentroidBoundaryConstraint();
        constraint.setDelegate(new RandomBoundaryConstraint());
        strategy.setBoundaryConstraint(constraint);
        instance.setOptimisationProblem(problem);
        DataDependantPopulationInitialisationStrategy init = new DataDependantPopulationInitialisationStrategy<ClusterParticle>();

        init.setEntityType(new ClusterParticle());
        init.setEntityNumber(2);
        instance.setInitialisationStrategy(init);
        instance.setSourceURL("library/src/test/resources/datasets/iris2.arff");

        instance.setOptimisationProblem(problem);
        instance.addStoppingCondition(new MeasuredStoppingCondition());

        MultiSwarm ms = new MultiSwarm();
        ms.setMultiSwarmIterationStrategy(strategy);
        ms.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 30));
        ms.addPopulationBasedAlgorithm(instance);
        ms.setOptimisationProblem(problem);

        ms.performInitialisation();

        ClusterParticle particleBefore = instance.getTopology().get(0).getClone();

        ms.run();

        ClusterParticle particleAfter = instance.getTopology().get(0).getClone();

        Assert.assertFalse(particleAfter.getCandidateSolution().containsAll(particleBefore.getCandidateSolution()));
    }

    /**
     * Test of reInitialise method, of class StandardClusteringMultiSwarmIterationStrategy.
     */
    @Test
    public void testReInitialise() {
        DataClusteringPSO instance = new DataClusteringPSO();

        QuantisationErrorMinimisationProblem problem = new QuantisationErrorMinimisationProblem();
        problem.setDomain("R(-5.12:5.12)");
        IterationStrategy strategy = new StandardClusteringMultiSwarmIterationStrategy();
        CentroidBoundaryConstraint constraint = new CentroidBoundaryConstraint();
        constraint.setDelegate(new RandomBoundaryConstraint());
        strategy.setBoundaryConstraint(constraint);
        instance.setOptimisationProblem(problem);
        DataDependantPopulationInitialisationStrategy init = new DataDependantPopulationInitialisationStrategy<ClusterParticle>();

        init.setEntityType(new ClusterParticle());
        init.setEntityNumber(2);
        instance.setInitialisationStrategy(init);
        instance.setSourceURL("library/src/test/resources/datasets/iris2.arff");

        instance.setOptimisationProblem(problem);
        instance.addStoppingCondition(new MeasuredStoppingCondition());

        instance.performInitialisation();

        ClusterParticle particleBefore1 = instance.getTopology().get(0).getClone();
        ClusterParticle particleBefore2 = instance.getTopology().get(1).getClone();

        StandardClusteringMultiSwarmIterationStrategy msStrategy = new StandardClusteringMultiSwarmIterationStrategy();
        msStrategy.reInitialise(instance);

        ClusterParticle particleAfter1 = instance.getTopology().get(0).getClone();
        ClusterParticle particleAfter2 = instance.getTopology().get(1).getClone();

        assertFalse(particleAfter1.getCandidateSolution().containsAll(particleBefore1.getCandidateSolution()));
        assertFalse(particleAfter2.getCandidateSolution().containsAll(particleBefore2.getCandidateSolution()));
    }

    /**
     * Test of assignDataPatternsToParticle method, of class StandardClusteringMultiSwarmIterationStrategy.
     */
    @Test
    public void testAssignDataPatternsToParticle() {
        StandardClusteringMultiSwarmIterationStrategy instance = new StandardClusteringMultiSwarmIterationStrategy();
        DataClusteringPSO pso = new DataClusteringPSO();
        StandardDataClusteringIterationStrategy standardStrategy = new StandardDataClusteringIterationStrategy();

        CentroidHolder candidateSolution = new CentroidHolder();
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        window.setWindowSize(3);
        standardStrategy.setWindow(window);
        standardStrategy.getWindow().initialiseWindow();

        candidateSolution.add(ClusterCentroid.of(1.25,1.1,1.3,1.9));
        candidateSolution.add(ClusterCentroid.of(1.92,2.6,3.1,1.8));
        candidateSolution.add(ClusterCentroid.of(0.9,1.1,0.85,0.79));

        DataTable dataset = standardStrategy.getWindow().getCurrentDataset();

        instance.assignDataPatternsToParticle(candidateSolution, dataset);
        Assert.assertTrue(candidateSolution.get(0).getDataItems().contains(Vector.of(1.0,1.0,1.0,2.0)));
        Assert.assertTrue(candidateSolution.get(1).getDataItems().contains(Vector.of(2.0,3.0,4.0,2.0)));
        Assert.assertTrue(candidateSolution.get(2).getDataItems().contains(Vector.of(1.0,1.0,1.0,1.0)));
    }
}
