/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering.iterationstrategies;

import junit.framework.Assert;
import net.sourceforge.cilib.algorithm.initialisation.DataDependantPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.clustering.SlidingWindow;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.QuantisationErrorMinimisationProblem;
import net.sourceforge.cilib.problem.boundaryconstraint.CentroidBoundaryConstraint;
import net.sourceforge.cilib.problem.boundaryconstraint.RandomBoundaryConstraint;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;
import static org.junit.Assert.*;
import org.junit.Test;

public class SinglePopulationDataClusteringIterationStrategyTest {

    /**
     * Test of performIteration method, of class StandardDataClusteringIterationStrategy.
     */
    @Test
    public void testPerformIteration() {
        DataClusteringPSO instance = new DataClusteringPSO();

        QuantisationErrorMinimisationProblem problem = new QuantisationErrorMinimisationProblem();
        problem.setDomain("R(-5.12:5.12)");
        IterationStrategy strategy = new StandardDataClusteringIterationStrategy();
        CentroidBoundaryConstraint constraint = new CentroidBoundaryConstraint();
        constraint.setDelegate(new RandomBoundaryConstraint());
        strategy.setBoundaryConstraint(constraint);
        instance.setIterationStrategy(strategy);
        instance.setOptimisationProblem(problem);
        DataDependantPopulationInitialisationStrategy init = new DataDependantPopulationInitialisationStrategy<ClusterParticle>();

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
        window.initialiseWindow();
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
        instance.getWindow().initialiseWindow();

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
