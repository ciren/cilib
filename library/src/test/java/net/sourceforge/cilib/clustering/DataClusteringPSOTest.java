/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering;

import junit.framework.Assert;
import net.sourceforge.cilib.algorithm.initialisation.DataDependantPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.clustering.iterationstrategies.SinglePopulationDataClusteringIterationStrategy;
import net.sourceforge.cilib.clustering.iterationstrategies.StandardDataClusteringIterationStrategy;
import net.sourceforge.cilib.clustering.SlidingWindow;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.TopologyBestContributionSelectionStrategy;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.QuantisationErrorMinimisationProblem;
import net.sourceforge.cilib.problem.boundaryconstraint.CentroidBoundaryConstraint;
import net.sourceforge.cilib.problem.boundaryconstraint.RandomBoundaryConstraint;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;

import org.junit.Test;

public class DataClusteringPSOTest {

    /**
     * Test of algorithmIteration method, of class DataClusteringPSO.
     */
    @Test
    public void testAlgorithmIteration() {
        DataClusteringPSO instance = new DataClusteringPSO();

        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");

        QuantisationErrorMinimisationProblem problem = new QuantisationErrorMinimisationProblem();
        problem.setDomain("R(-5.12:5.12)");
        problem.setWindow(window);
        IterationStrategy strategy = new StandardDataClusteringIterationStrategy();
        CentroidBoundaryConstraint constraint = new CentroidBoundaryConstraint();
        constraint.setDelegate(new RandomBoundaryConstraint());
        strategy.setBoundaryConstraint(constraint);
        instance.setIterationStrategy(strategy);
        instance.setOptimisationProblem(problem);
        DataDependantPopulationInitialisationStrategy init = new DataDependantPopulationInitialisationStrategy();

        init.setEntityType(new ClusterParticle());
        init.setEntityNumber(2);
        instance.setInitialisationStrategy(init);

        instance.setOptimisationProblem(problem);
        instance.addStoppingCondition(new MeasuredStoppingCondition());

        instance.performInitialisation();

        ClusterParticle particleBefore = instance.getTopology().head().getClone();

        instance.run();

        ClusterParticle particleAfter = instance.getTopology().head().getClone();

        Assert.assertFalse(particleAfter.getCandidateSolution().containsAll(particleBefore.getCandidateSolution()));

    }

    /**
     * Test of getTopology method, of class DataClusteringPSO.
     */
    @Test
    public void testGetTopology() {
        DataClusteringPSO instance = new DataClusteringPSO();
        ClusterParticle p = new ClusterParticle();
        CentroidHolder holder = new CentroidHolder();
        holder.add(ClusterCentroid.of(1,2,3,4,5));
        holder.add(ClusterCentroid.of(1,3,5,9,8));
        p.setCandidateSolution(holder);
//        Topology topology = new GBestTopology();
//        topology.add(p);
        fj.data.List<ClusterParticle> list = fj.data.List.list(p);

        instance.setTopology(list);

        Assert.assertEquals(list, instance.getTopology());
    }

    /**
     * Test of setTopology method, of class DataClusteringPSO.
     */
    @Test
    public void testSetTopology() {
        DataClusteringPSO instance = new DataClusteringPSO();
        ClusterParticle p = new ClusterParticle();
        CentroidHolder holder = new CentroidHolder();
        holder.add(ClusterCentroid.of(1,2,3,4,5));
        p.setCandidateSolution(holder);
//        Topology topology = new GBestTopology();
//        topology.add(p);

        fj.data.List<ClusterParticle> list = fj.data.List.list(p);
        instance.setTopology(list);

        Assert.assertEquals(list, instance.getTopology());
    }

    /**
     * Test of performInitialisation method, of class DataClusteringPSO.
     */
    @Test
    public void testPerformInitialisation() {
        DataClusteringPSO instance = new DataClusteringPSO();
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        QuantisationErrorMinimisationProblem problem = new QuantisationErrorMinimisationProblem();
        problem.setDomain("R(-5.12:5.12)");
        problem.setWindow(window);
        instance.setOptimisationProblem(problem);
        instance.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 1));
        PopulationInitialisationStrategy init = new DataDependantPopulationInitialisationStrategy();
        init.setEntityType(new ClusterParticle());
        instance.setInitialisationStrategy(init);
        instance.performInitialisation();

        Assert.assertTrue(((ClusteringProblem) instance.getOptimisationProblem()).getWindow().getCurrentDataset().size() > 0);
        Assert.assertTrue(!instance.getTopology().isEmpty());
    }

    /**
     * Test of getContributionSelectionStrategy method, of class DataClusteringPSO.
     */
    @Test
    public void testGetContributionSelectionStrategy() {
       DataClusteringPSO instance = new DataClusteringPSO();
       instance.setContributionSelectionStrategy(new TopologyBestContributionSelectionStrategy());

       Assert.assertTrue(instance.getContributionSelectionStrategy() instanceof TopologyBestContributionSelectionStrategy);
    }

    /**
     * Test of setContributionSelectionStrategy method, of class DataClusteringPSO.
     */
    @Test
    public void testSetContributionSelectionStrategy() {
        DataClusteringPSO instance = new DataClusteringPSO();
       instance.setContributionSelectionStrategy(new TopologyBestContributionSelectionStrategy());

       Assert.assertTrue(instance.getContributionSelectionStrategy() instanceof TopologyBestContributionSelectionStrategy);
    }
}
